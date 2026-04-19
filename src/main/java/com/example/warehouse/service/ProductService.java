package com.example.warehouse.service;

import com.example.warehouse.model1.Invoice;
import com.example.warehouse.model1.Product;
import com.example.warehouse.model1.PurchaseItem;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class ProductService {

    private final JsonStorageService jsonStorageService;

    public ProductService(JsonStorageService jsonStorageService) {
        this.jsonStorageService = jsonStorageService;
    }

    public List<Product> getAllProducts() {
        return jsonStorageService.readProducts();
    }

    public Product addProduct(Product product) {
        List<Product> products = getAllProducts();

        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("Назва товару не може бути порожньою");
        }

        if (product.getCategory() == null || product.getCategory().isBlank()) {
            throw new IllegalArgumentException("Категорія не може бути порожньою");
        }

        if (product.getWarehouse() == null || product.getWarehouse().isBlank()) {
            throw new IllegalArgumentException("Склад не може бути порожнім");
        }

        if (product.getStorageCondition() == null || product.getStorageCondition().isBlank()) {
            throw new IllegalArgumentException("Умови зберігання не можуть бути порожніми");
        }

        if (product.getQuantity() < 0 || product.getMinQuantity() < 0) {
            throw new IllegalArgumentException("Кількість не може бути від’ємною");
        }

        long nextId = products.stream()
                .map(Product::getId)
                .filter(id -> id != null)
                .max(Comparator.naturalOrder())
                .orElse(0L) + 1;

        product.setId(nextId);
        product.setLastUpdated(LocalDateTime.now().toString());

        products.add(product);
        jsonStorageService.writeProducts(products);

        return product;
    }

    public void deleteProduct(Long productId) {
        List<Product> products = getAllProducts();

        boolean removed = products.removeIf(product -> product.getId().equals(productId));

        if (!removed) {
            throw new RuntimeException("Товар не знайдено");
        }

        jsonStorageService.writeProducts(products);
    }

    public void sellProduct(Long productId, int soldQuantity) {
        List<Product> products = getAllProducts();

        Product product = products.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Товар не знайдено"));

        if (soldQuantity <= 0) {
            throw new IllegalArgumentException("Кількість списання має бути більшою за 0");
        }

        if (product.getQuantity() < soldQuantity) {
            throw new IllegalArgumentException("Недостатньо товару на складі");
        }

        product.setQuantity(product.getQuantity() - soldQuantity);
        product.setLastUpdated(LocalDateTime.now().toString());

        jsonStorageService.writeProducts(products);
    }

    public void updateQuantity(Long productId, int newQuantity) {
        List<Product> products = getAllProducts();

        Product product = products.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Товар не знайдено"));

        if (newQuantity < 0) {
            throw new IllegalArgumentException("Кількість не може бути від’ємною");
        }

        product.setQuantity(newQuantity);
        product.setLastUpdated(LocalDateTime.now().toString());

        jsonStorageService.writeProducts(products);
    }

    public void processInvoice(Invoice invoice) {
        List<Product> products = getAllProducts();

        Product product = products.stream()
                .filter(p -> p.getId().equals(invoice.getProductId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Товар не знайдено"));

        if (invoice.getReceivedQuantity() <= 0) {
            throw new IllegalArgumentException("Кількість приходу має бути більшою за 0");
        }

        product.setQuantity(product.getQuantity() + invoice.getReceivedQuantity());
        product.setLastUpdated(LocalDateTime.now().toString());

        jsonStorageService.writeProducts(products);
    }

    public List<PurchaseItem> generatePurchaseList() {
        return getAllProducts().stream()
                .filter(product -> product.getQuantity() <= product.getMinQuantity())
                .map(product -> new PurchaseItem(
                        product.getId(),
                        product.getName(),
                        product.getQuantity(),
                        product.getMinQuantity(),
                        product.getMinQuantity() * 2 - product.getQuantity()
                ))
                .toList();
    }
}