package com.example.warehouse.model1;

public class Product {
    private Long id;
    private String name;
    private String category;
    private int quantity;
    private int minQuantity;
    private String warehouse;
    private String storageCondition;
    private String lastUpdated;

    public Product() {
    }

    public Product(Long id, String name, String category, int quantity, int minQuantity,
                   String warehouse, String storageCondition, String lastUpdated) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.minQuantity = minQuantity;
        this.warehouse = warehouse;
        this.storageCondition = storageCondition;
        this.lastUpdated = lastUpdated;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public String getStorageCondition() {
        return storageCondition;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public void setStorageCondition(String storageCondition) {
        this.storageCondition = storageCondition;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}