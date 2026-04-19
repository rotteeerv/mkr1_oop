package com.example.warehouse.controller;

import com.example.warehouse.builder.WarehouseOrderBuilder;
import com.example.warehouse.model1.Product;
import com.example.warehouse.model1.WarehouseOrder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @GetMapping("/demo")
    public WarehouseOrder createDemoOrder() {
        Product product1 = new Product(
                101L,
                "Ноутбук Lenovo IdeaPad",
                "Електроніка",
                2,
                1,
                "Основний склад",
                "Сухе приміщення",
                "2026-04-19T18:00:00"
        );

        Product product2 = new Product(
                102L,
                "SSD Samsung 500GB",
                "Комплектуючі",
                5,
                2,
                "Склад 2",
                "Без вологи",
                "2026-04-19T18:10:00"
        );

        return new WarehouseOrderBuilder()
                .setOrderId(5001L)
                .addProduct(product1)
                .addProduct(product2)
                .addWarehouse("Основний склад")
                .addWarehouse("Склад 2")
                .addStorageCondition("Сухе приміщення")
                .addStorageCondition("Без вологи")
                .setDeliveryType("Експрес-доставка")
                .setComment("Складне замовлення, сформоване через Builder")
                .build();
    }
}