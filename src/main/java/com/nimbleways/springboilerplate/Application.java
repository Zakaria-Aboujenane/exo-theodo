package com.nimbleways.springboilerplate;

import com.nimbleways.springboilerplate.entities.Order;
import com.nimbleways.springboilerplate.entities.Product;
import com.nimbleways.springboilerplate.enums.ProductType;
import com.nimbleways.springboilerplate.repositories.OrderRepository;
import com.nimbleways.springboilerplate.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Set;

@SpringBootApplication
public class Application {

	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner seedData(ProductRepository productRepository, OrderRepository orderRepository) {
		return args -> {
			Product normal = productRepository.save(new Product(null, 0, 10, ProductType.NORMAL, "Widget", null, null, null));
			Product expirable = productRepository.save(new Product(null, 0, 5, ProductType.EXPIRABLE, "Milk", LocalDate.now().plusDays(7), null, null));
			Product seasonal = productRepository.save(new Product(null, 3, 8, ProductType.SEASONAL, "Strawberries", null, LocalDate.now().minusDays(10), LocalDate.now().plusDays(20)));

			Order order = new Order();
			order.setItems(Set.of(normal, expirable, seasonal));
			orderRepository.save(order);
		};
	}

}
