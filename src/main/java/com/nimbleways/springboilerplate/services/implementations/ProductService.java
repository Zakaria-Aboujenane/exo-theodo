package com.nimbleways.springboilerplate.services.implementations;

import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;
import com.nimbleways.springboilerplate.exceptions.NoProductHandlerException;
import com.nimbleways.springboilerplate.handlers.product.api.ProductHandler;
import com.nimbleways.springboilerplate.services.api.IProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
    private final Map<ProductType, ProductHandler> handlers;

    public ProductService(List<ProductHandler> handlerList) {
        this.handlers = handlerList.stream()
                .collect(Collectors.toMap(ProductHandler::getSupportedType, h -> h));
    }

    public void handle(Product p) {
        ProductHandler handler = handlers.get(p.getType());
        if (handler == null) {
            throw new NoProductHandlerException(p.getType());
        }
        handler.handle(p);
    }
}
