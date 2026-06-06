package com.nimbleways.springboilerplate.controllers;

import com.nimbleways.springboilerplate.dto.product.ProcessOrderResponse;
import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.services.api.IOrderService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
@Slf4j
public class OrderController {
    private IOrderService orderService;

    @PostMapping("{orderId}/processOrder")
    @ResponseStatus(HttpStatus.OK)
    public ProcessOrderResponse processOrder(@PathVariable Long orderId) {
        Order order = orderService.get(orderId);
        log.info("Processing the Order : {}", order.getId());
        orderService.process(order);
        return new ProcessOrderResponse(order.getId());
    }
}
