package com.quickbite.quickbite_backend.notification;

import com.quickbite.quickbite_backend.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    public void sendOrderConfirmation(Order order) {
        String email = order.getUser().getEmail();
        String subject = "QuickBite — Order Confirmed! #QB-" + order.getId();
        String body = "Hi " + order.getUser().getName() + "!\n\n"
                + "Your order from " + order.getRestaurant().getName()
                + " has been confirmed.\n"
                + "Total: $" + order.getTotalAmount() + "\n"
                + "Status: " + order.getStatus() + "\n\n"
                + "We will notify you when your food is on the way!\n\n"
                + "Thank you for using QuickBite!";
        sendEmail(email, subject, body);
    }

    public void sendStatusUpdate(Order order) {
        String email = order.getUser().getEmail();
        String subject = "QuickBite — Order Update #QB-" + order.getId();
        String body = "Hi " + order.getUser().getName() + "!\n\n"
                + "Your order status has been updated to: "
                + order.getStatus() + "\n\n"
                + "Thank you for using QuickBite!";
        sendEmail(email, subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Email sending failed: " + e.getMessage());
        }
    }
}