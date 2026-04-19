package com.example.warehouse.model1;

public class PurchaseItem {
    private Long productId;
    private String productName;
    private int currentQuantity;
    private int minQuantity;
    private int recommendedOrderQuantity;

    public PurchaseItem() {
    }

    public PurchaseItem(Long productId, String productName, int currentQuantity,
                        int minQuantity, int recommendedOrderQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.currentQuantity = currentQuantity;
        this.minQuantity = minQuantity;
        this.recommendedOrderQuantity = recommendedOrderQuantity;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public int getRecommendedOrderQuantity() {
        return recommendedOrderQuantity;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public void setRecommendedOrderQuantity(int recommendedOrderQuantity) {
        this.recommendedOrderQuantity = recommendedOrderQuantity;
    }
}