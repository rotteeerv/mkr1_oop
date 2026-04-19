package com.example.warehouse.model1;

import java.util.List;

public class WarehouseOrder {
    private Long orderId;
    private List<Product> products;
    private List<String> warehouses;
    private List<String> storageConditions;
    private String deliveryType;
    private String comment;

    public WarehouseOrder(Long orderId, List<Product> products, List<String> warehouses,
                          List<String> storageConditions, String deliveryType, String comment) {
        this.orderId = orderId;
        this.products = products;
        this.warehouses = warehouses;
        this.storageConditions = storageConditions;
        this.deliveryType = deliveryType;
        this.comment = comment;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<String> getWarehouses() {
        return warehouses;
    }

    public List<String> getStorageConditions() {
        return storageConditions;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public String getComment() {
        return comment;
    }
}