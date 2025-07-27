package com.ecommerce.order.service;

import com.ecommerce.order.dto.OrderItemRequest;
import com.ecommerce.order.dto.OrderRequest;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    @Value("${services.user-service.url}")
    private String userServiceUrl;

    @Value("${services.product-service.url}")
    private String productServiceUrl;

    public OrderService(OrderRepository orderRepository, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }

    public void createOrder(OrderRequest orderRequest) {
        // ✅ Call user-service to validate userId
        String userUrl = userServiceUrl + orderRequest.getUserId();
        var userResponse = restTemplate.getForEntity(userUrl, Object.class);
        if (!userResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("User not found: " + orderRequest.getUserId());
        }

        // Validate products and build order items
        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderItemRequest itemRequest : orderRequest.getItems()) {
            // ✅ Validate product
            String productUrl = productServiceUrl + itemRequest.getProductId();
            var productResponse = restTemplate.getForEntity(productUrl, Object.class);
            if (!productResponse.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Product not found: " + itemRequest.getProductId());
            }

            OrderItem item = OrderItem.builder()
                    .productId(itemRequest.getProductId())
                    .quantity(itemRequest.getQuantity())
                    .price(itemRequest.getPrice())
                    .build();

            totalAmount += item.getPrice() * item.getQuantity();
            orderItems.add(item);
        }

        // Build and link order
        Order order = Order.builder()
                .userId(orderRequest.getUserId()) // ✅
                .customerName(orderRequest.getCustomerName())
                .orderDate(LocalDateTime.now())
                .totalAmount(totalAmount)
                .build();

        for (OrderItem item : orderItems) {
            item.setOrder(order);
        }

        order.setItems(orderItems);
        orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
}
