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

package com.github.tddts.evetrader.dao;

import com.github.tddts.evetrader.model.db.CachedOrder;
import com.github.tddts.evetrader.model.db.ResultOrder;

import java.util.Collection;
import java.util.List;

/**
 * DAO for {@link CachedOrder}
 *
 * @author Tigran_Dadaiants dtkcommon@gmail.com
 */
public interface OrderDao extends GenericDao<CachedOrder> {

  /**
   * Remove orders that will expire in given time
   *
   * @param time time in minutes
   * @return number of deleted orders
   */
  int removeSoonExpiredOrders(int time);

  /**
   * Remove orders with item which exceed given volume.
   *
   * @param volume maximum volume
   * @return number of deleted orders
   */
  int removeLargeItemOrders(double volume);

  /**
   * Remove orders which price exceeds given amount
   *
   * @param funds maximum price
   * @return number of deleted orders
   */
  int removeTooExpensiveOrders(double funds);

  /**
   * Find most profitable orders.
   *
   * @param security    minimum security status for order solar system
   * @param cargoVolume available cargo volume
   * @param taxRate     tax rate
   * @return a list of most profitable orders
   */
  List<ResultOrder> findProfitableOrders(double security, double cargoVolume, double taxRate);

  /**
   * Merge (save or update by ID) given collection of orders.
   * @param orders orders
   * @return number of updates
   */
  void merge(Collection<CachedOrder> orders);
}
