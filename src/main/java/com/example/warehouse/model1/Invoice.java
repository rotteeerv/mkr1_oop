package com.example.warehouse.model1;

public class Invoice {
    private Long productId;
    private int receivedQuantity;
    private String supplier;
    private String date;

    public Invoice() {
    }

    public Invoice(Long productId, int receivedQuantity, String supplier, String date) {
        this.productId = productId;
        this.receivedQuantity = receivedQuantity;
        this.supplier = supplier;
        this.date = date;
    }

    public Long getProductId() {
        return productId;
    }

    public int getReceivedQuantity() {
        return receivedQuantity;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getDate() {
        return date;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setReceivedQuantity(int receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public void setDate(String date) {
        this.date = date;
    }
}