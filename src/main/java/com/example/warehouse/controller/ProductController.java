package com.example.warehouse.controller;

import com.example.warehouse.model1.Invoice;
import com.example.warehouse.model1.Product;
import com.example.warehouse.model1.PurchaseItem;
import com.example.warehouse.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.addProduct(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Товар успішно видалено");
    }

    @PostMapping("/{id}/sell")
    public ResponseEntity<String> sellProduct(@PathVariable Long id, @RequestParam int quantity) {
        productService.sellProduct(id, quantity);
        return ResponseEntity.ok("Товар успішно списано");
    }

    @PutMapping("/{id}/quantity")
    public ResponseEntity<String> updateQuantity(@PathVariable Long id, @RequestParam int quantity) {
        productService.updateQuantity(id, quantity);
        return ResponseEntity.ok("Кількість успішно оновлено");
    }

    @PostMapping("/invoice")
    public ResponseEntity<String> processInvoice(@RequestBody Invoice invoice) {
        productService.processInvoice(invoice);
        return ResponseEntity.ok("Накладну успішно оброблено");
    }

    @GetMapping("/purchase-list")
    public List<PurchaseItem> getPurchaseList() {
        return productService.generatePurchaseList();
    }
}