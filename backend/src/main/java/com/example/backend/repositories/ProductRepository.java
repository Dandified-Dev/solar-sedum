package com.example.backend.repositories;

import com.example.backend.models.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> findAll();
    Product findById(long id);
    List<Product> getByWarehouse(int id);
    Product save(Product product);
    void deleteById(long id);

    Product editedProduct(Product product);
    Product updateQuantity(int id, int updatedQuantity);
}
