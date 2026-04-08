package com.example.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class NotificationService {

    public void sendWelcomeEmail(String name, String email) {
        System.out.println("EMAIL SENDING: Welcome to our application, " + name + "!");
        System.out.println("Address: " + email);
    }

    public void sendGoodbyeEmail(String name, String email) {
        System.out.println("EMAIL SENDING: We so regret that u r leaving our application, " + name + ". We will fix it up!");
        System.out.println("Address: " + email);
    }
}
