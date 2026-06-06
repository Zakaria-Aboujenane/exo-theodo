package com.nimbleways.springboilerplate.handlers.product.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;
import com.nimbleways.springboilerplate.handlers.product.api.ProductHandler;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.api.INotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class ExpirableProductHandler implements ProductHandler {
    private final ProductRepository productRepository;
    private final INotificationService notificationService;

    @Override
    public ProductType getSupportedType() {
        return ProductType.EXPIRABLE;
    }

    @Override
    public void handle(Product p) {
        if (p.getAvailable() > 0 && p.getExpiryDate().isAfter(LocalDate.now())) {
            p.setAvailable(p.getAvailable() - 1);
        } else {
            notificationService.sendExpirationNotification(p.getName(), p.getExpiryDate());
            p.setAvailable(0);
        }
        productRepository.save(p);
    }


}
