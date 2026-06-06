package com.nimbleways.springboilerplate.services.api;

import com.nimbleways.springboilerplate.entities.Order;

public interface IOrderService {
    Order get(Long orderId);
    void process(Order order);
}
