package com.example.warehouse.service;

import com.example.warehouse.model1.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class JsonStorageService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Path path = Path.of("src/main/resources/data/products.json");

    public List<Product> readProducts() {
        try {
            if (!Files.exists(path)) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(path.toFile(), new TypeReference<List<Product>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Помилка читання JSON", e);
        }
    }

    public void writeProducts(List<Product> products) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), products);
        } catch (IOException e) {
            throw new RuntimeException("Помилка запису JSON", e);
        }
    }
}
