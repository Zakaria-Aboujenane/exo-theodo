package com.nimbleways.springboilerplate.handlers.product.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;
import com.nimbleways.springboilerplate.exceptions.InvalidSeasonalProductException;
import com.nimbleways.springboilerplate.handlers.product.api.ProductHandler;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import com.nimbleways.springboilerplate.services.api.INotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class SeasonalProductHandler implements ProductHandler {
    private final ProductRepository productRepository;
    private final INotificationService notificationService;

    @Override
    public ProductType getSupportedType() {
        return ProductType.SEASONAL;
    }

    @Override
    public void handle(Product p) {
        if(productNotReady(p)) throw new InvalidSeasonalProductException();

        LocalDate now = LocalDate.now();
        LocalDate start = p.getSeasonStartDate();
        LocalDate end = p.getSeasonEndDate();
        boolean inSeason = !now.isBefore(start) && !now.isAfter(end);

        if (inSeason && p.getAvailable() > 0) {
            p.setAvailable(p.getAvailable() - 1);
        } else if (now.plusDays(p.getLeadTime()).isAfter(end)) {
            notificationService.sendOutOfStockNotification(p.getName());
            p.setAvailable(0);
        } else {
            notificationService.sendDelayNotification(p.getLeadTime(), p.getName());
        }
        productRepository.save(p);
    }

    private boolean productNotReady(Product product) {
        return product.getSeasonStartDate() == null
                || product.getSeasonEndDate() == null;
    }
}
