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

package com.github.tddts.evetrader.context.events;

/**
 * {@code SearchStatusEvent} represents a enumeration of events occurring in a order search process.
 *
 * @author Tigran_Dadaiants dtkcommon@gmail.com
 */
public enum SearchStatusEvent {

  CLEARING_CACHE,
  LOADING_PRICES,
  LOADING_ORDERS,
  FILTERING_ORDERS,
  SEARCHING_FOR_PROFIT,
  SEARCHING_FOR_ROUTES,
  NO_ORDERS_FOUND,
  FINISHED,

  NONE;

  public boolean isFinished() {
    return this == FINISHED || this == NO_ORDERS_FOUND;
  }
}
