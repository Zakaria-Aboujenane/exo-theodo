package com.nimbleways.springboilerplate.handlers.product.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;
import com.nimbleways.springboilerplate.handlers.product.api.ProductHandler;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.api.INotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NormalProductHandler implements ProductHandler {
    private final ProductRepository productRepository;
    private final INotificationService notificationService;

    @Override
    public ProductType getSupportedType() {
        return ProductType.NORMAL;
    }

    @Override
    public void handle(Product p) {
        if (p.getAvailable() > 0) {
            p.setAvailable(p.getAvailable() - 1);
        } else if (p.getLeadTime() > 0) {
            notificationService.sendDelayNotification(p.getLeadTime(), p.getName());
        }
        productRepository.save(p);
    }
}
