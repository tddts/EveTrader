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

package com.github.tddts.evetrader.tools.filter;

import com.github.tddts.evetrader.model.db.ResultOrder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tigran_Dadaiants dtkcommon@gmail.com
 */
public class ResultOrderFilter {

  private final Logger logger = LogManager.getLogger(ResultOrderFilter.class);


  public List<ResultOrder> filter(List<ResultOrder> orders) {
    if (orders.isEmpty()) return orders;

    logger.debug("Orders before filtration: " + orders.size());

    List<ResultOrder> filtered = new ArrayList<>(orders.size());

    while (!orders.isEmpty()) {
      ResultOrder merging = orders.remove(0);
      orders = orders.stream().filter(item -> !merging.merge(item)).collect(Collectors.toList());
      filtered.add(merging);
    }

    logger.debug("Orders after filtration: " + filtered.size());
    return filtered;
  }

}
