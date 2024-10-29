package com.duoc.sumativas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.duoc.sumativas.model.Product;
import com.duoc.sumativas.service.ProductService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sell/products")
public class SellProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> listAvailableProducts() {
        return productService.findAll().stream()
            .filter(product -> product.getStock() > 0)
            .toList();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchAvailableProducts(@RequestParam String name) {
        List<Product> products = productService.findAll().stream()
            .filter(product -> product.getName().toLowerCase().contains(name.toLowerCase()))
            .filter(product -> product.getStock() > 0)
            .toList();
        return ResponseEntity.ok(products);
    }

    @PostMapping("/buy/{id}/{quantity}")
    public ResponseEntity<String> buyProduct(@PathVariable Long id, @PathVariable Integer quantity) {
        Optional<Product> optionalProduct = productService.findById(id);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            
            if (product.getStock() >= quantity) {
                product.setStock(product.getStock() - quantity);
                productService.save(product);
                return ResponseEntity.ok("Compra realizada con éxito. Cantidad comprada: " + quantity);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Stock insuficiente.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado.");
        }
    }
}

