package com.ecommerce.order.dto;

import java.util.List;

public class OrderRequest {
    private Long userId; // ✅ new field
    private String customerName;
    private List<OrderItemRequest> items;

    // Getters & setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }
}
