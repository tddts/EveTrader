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

package com.github.tddts.evetrader.rest.client.esi.impl;

import com.github.tddts.evetrader.config.spring.annotations.RestClient;
import com.github.tddts.evetrader.rest.RestResponse;
import com.github.tddts.evetrader.rest.client.esi.WalletClient;
import com.github.tddts.evetrader.rest.provider.RestClientTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

/**
 * @author Tigran_Dadaiants dtkcommon@gmail.com
 */
@Component
@RestClient
public class WalletClientImpl implements WalletClient {

  @Value("${path.wallet}")
  private String addressCharacterWallet;

  @Autowired
  private RestClientTemplate client;

  @Override
  public RestResponse<Double> getWalletAmount(long character_id) {
    return new RestResponse<>(client.restOperations().exchange(
        client.apiUrl(addressCharacterWallet), HttpMethod.GET, client.authorizedEntity(), Double.class, character_id));
  }
}
