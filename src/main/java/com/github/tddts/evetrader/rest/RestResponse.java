/*
 * Copyright 2018 Tigran Dadaiants
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.tddts.evetrader.rest;

import com.github.tddts.evetrader.exception.RestResponseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * {@code RestResponse} represents a response received from REST server.
 *
 * @author Tigran_Dadaiants dtkcommon@gmail.com
 */
public class RestResponse<T> {

  private T object;
  private HttpStatus status;
  private HttpHeaders headers;

  public RestResponse(T object, HttpStatus httpStatus, HttpHeaders headers) {
    this.object = object;
    this.status = httpStatus;
    this.headers = headers;
  }

  public RestResponse(ResponseEntity<T> responseEntity) {
    object = responseEntity.getBody();
    status = responseEntity.getStatusCode();
    headers = responseEntity.getHeaders();
  }

  public RestResponse(HttpStatusCodeException statusCodeException) {
    status = statusCodeException.getStatusCode();
  }

  public static <T> RestResponse<List<T>> fromArrayResponse(ResponseEntity<T[]> entity) {
    return new RestResponse<>(Arrays.asList(entity.getBody()), entity.getStatusCode(), entity.getHeaders());
  }

  public T getObject() {
    return object;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public Map<String, List<String>> getHeaders() {
    return headers;
  }

  public List<String> getHeader(String key) {
    if (headers == null) return null;
    return headers.get(key);
  }

  public String getStatusMessage() {
    return status.value() + " " + status.getReasonPhrase();
  }

  public boolean hasObject() {
    return object != null;
  }

  public boolean hasError() {
    return status.is5xxServerError() || status.is4xxClientError();
  }

  public boolean isSuccessful() {
    return status.is2xxSuccessful();
  }

  public void ifSuccessful(Consumer<T> consumer) {
    process(false, consumer);
  }

  public void process(boolean throwError, Consumer<T> consumer) throws RestResponseException {
    if (isSuccessful()) {
      consumer.accept(object);
    } else if (throwError) {
      throw new RestResponseException(status);
    }
  }

  public void process(Consumer<T> consumer) throws RestResponseException {
    process(true, consumer);
  }

  public void checkError() {
    if (!isSuccessful()) {
      throw new RestResponseException(status);
    }
  }
}
