package com.example.warehouse;

import com.example.warehouse.model1.Product;
import com.example.warehouse.service.JsonStorageService;
import com.example.warehouse.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class ProductServiceTest {

    @Test
    void shouldDecreaseQuantityAfterSale() {
        JsonStorageService storageService = Mockito.mock(JsonStorageService.class);
        ProductService productService = new ProductService(storageService);

        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Тестовий товар", "Категорія", 10, 3,
                "Склад", "Звичайні умови", "2026-04-19T20:00:00"));

        Mockito.when(storageService.readProducts()).thenReturn(products);

        productService.sellProduct(1L, 4);

        Assertions.assertEquals(6, products.get(0).getQuantity());
    }

    @Test
    void shouldUpdateLastUpdatedDateWhenQuantityChanges() {
        JsonStorageService storageService = Mockito.mock(JsonStorageService.class);
        ProductService productService = new ProductService(storageService);

        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Товар", "Категорія", 10, 3,
                "Склад", "Звичайні умови", "2020-01-01T00:00:00"));

        Mockito.when(storageService.readProducts()).thenReturn(products);

        productService.updateQuantity(1L, 15);

        Assertions.assertNotEquals("2020-01-01T00:00:00", products.get(0).getLastUpdated());
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNegative() {
        JsonStorageService storageService = Mockito.mock(JsonStorageService.class);
        ProductService productService = new ProductService(storageService);

        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "Товар", "Категорія", 10, 3,
                "Склад", "Звичайні умови", "2026-04-19T20:00:00"));

        Mockito.when(storageService.readProducts()).thenReturn(products);

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                productService.updateQuantity(1L, -2)
        );
    }
}