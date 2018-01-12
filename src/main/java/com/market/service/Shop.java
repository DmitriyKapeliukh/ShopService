package com.market.service;

import com.market.domain.Order;

public interface Shop {

    PurchaseStatus makePurchase(Order order);
}
