package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.services.api.IOrderService;
import com.nimbleways.springboilerplate.services.api.IProductService;
import com.nimbleways.springboilerplate.exceptions.EmptyOrderException;
import com.nimbleways.springboilerplate.exceptions.OrderNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class OrderService implements IOrderService {
    OrderRepository orderRepository;
    IProductService productService;
    public Order get(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
    }

    @Transactional
    public void process(Order order)  {
        if (order.getItems().isEmpty()) {
            throw new EmptyOrderException();
        }
        for (Product p : order.getItems()) {
            productService.handle(p);
        }
    }
}
