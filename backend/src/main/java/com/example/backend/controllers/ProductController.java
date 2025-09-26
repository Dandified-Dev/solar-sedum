package com.example.backend.controllers;

import com.example.backend.exceptions.NotFoundException;
import com.example.backend.exceptions.PreConditionFailedException;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.models.Product;
import com.example.backend.models.Project;
import com.example.backend.models.Warehouse;
import com.example.backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;

    @Autowired
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public Product getProducts(@PathVariable int id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new ResourceNotFoundException("Product " + id + " not found");
        }
        return product;
    }

    @GetMapping("/warehouseId/{id}")
    public List<Product> getByWarehouse(@PathVariable int id) {
        List<Product> products = productRepository.getByWarehouse(id);

        if (products == null) {
            throw new ResourceNotFoundException("no product found for warehouse: " + id);
        }

        return products;
    }

    @PostMapping("")
    ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product savedProduct = productRepository.save(product);

        // Build the location URI
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProduct.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedProduct);
    }

    //    @PutMapping(path = "/edit/{id}", produces = "application/json")
//    public Product updateProduct(@RequestBody Product product, @PathVariable int id) {
//        Product existingProduct = productRepository.findById(id);
//
//        if (existingProduct == null) {
//            throw new NotFoundException();
//        }
//
//        if (product.getId() != id) {
//            throw new PreConditionFailedException("Product ID in path does not match ID in request body");
//        }
//
//        return productRepository.editedProduct(product);
//    }
    @PutMapping(path = "/updateProduct/{id}", produces = "application/json")
    public Product updateProduct(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) String description,
            @PathVariable int id) {
        Product existingProduct = productRepository.findById(id);

        if (existingProduct != null) {
            if (name != null) {
                existingProduct.setName(name);
            }

            if (price != null) {
                existingProduct.setPrice(price);
            }

            if (description != null) {
                existingProduct.setDescription(description);
            }

        }
        return productRepository.editedProduct(existingProduct);
    }

    @PutMapping(path = "/updateQuantity/{id}", produces = "application/json")
    public ResponseEntity<Product> updateQuantity(@PathVariable int id, @RequestParam int updatedQuantity) {
        Product updatedProduct = productRepository.updateQuantity(id, updatedQuantity);

        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            // Product not found
            throw new NotFoundException();
        }
    }

    @DeleteMapping(path = "{id}", produces = "application/json")
    public void deleteProduct(@PathVariable int id) {
        Product deletedProduct = productRepository.findById(id);
        if (deletedProduct == null) {
            throw new ResourceNotFoundException("Product " + id + " not found");
        }
        productRepository.deleteById(id);
    }


}
