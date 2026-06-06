package com.nimbleways.springboilerplate.services.implementations;

import java.time.LocalDate;

import com.nimbleways.springboilerplate.services.api.INotificationService;
import org.springframework.stereotype.Service;

// WARN: Should not be changed during the exercise
@Service
public class NotificationService implements INotificationService {

    public void sendDelayNotification(int leadTime, String productName) {
    }

    public void sendOutOfStockNotification(String productName) {
    }

    public void sendExpirationNotification(String productName, LocalDate expiryDate) {
    }
}