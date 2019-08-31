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

package com.github.tddts.evetrader.model.client.esi.wallet;

import java.math.BigDecimal;

/**
 * {@code Wallet} represents a user wallet object from OpenAPI for EVE Online.
 *
 * @author Tigran_Dadaiants dtkcommon@gmail.com
 */
public class Wallet {

  private int wallet_id;
  private BigDecimal balance;

  public Wallet() {
  }

  public int getWallet_id() {
    return wallet_id;
  }

  public void setWallet_id(int wallet_id) {
    this.wallet_id = wallet_id;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  @Override
  public String toString() {
    return "Wallet{" + "wallet_id=[" + wallet_id + "], balance=[" + balance + "]}";
  }
}