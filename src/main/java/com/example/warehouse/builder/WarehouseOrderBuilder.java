package com.example.warehouse.builder;

import com.example.warehouse.model1.Product;
import com.example.warehouse.model1.WarehouseOrder;

import java.util.ArrayList;
import java.util.List;

public class WarehouseOrderBuilder {
    private Long orderId;
    private final List<Product> products = new ArrayList<>();
    private final List<String> warehouses = new ArrayList<>();
    private final List<String> storageConditions = new ArrayList<>();
    private String deliveryType;
    private String comment;

    public WarehouseOrderBuilder setOrderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public WarehouseOrderBuilder addProduct(Product product) {
        this.products.add(product);
        return this;
    }

    public WarehouseOrderBuilder addWarehouse(String warehouse) {
        this.warehouses.add(warehouse);
        return this;
    }

    public WarehouseOrderBuilder addStorageCondition(String condition) {
        this.storageConditions.add(condition);
        return this;
    }

    public WarehouseOrderBuilder setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
        return this;
    }

    public WarehouseOrderBuilder setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public WarehouseOrder build() {
        return new WarehouseOrder(orderId, products, warehouses, storageConditions, deliveryType, comment);
    }
}