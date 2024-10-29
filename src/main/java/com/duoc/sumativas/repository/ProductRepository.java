package com.duoc.sumativas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duoc.sumativas.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

