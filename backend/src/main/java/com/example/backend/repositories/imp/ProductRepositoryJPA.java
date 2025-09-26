package com.example.backend.repositories.imp;

import com.example.backend.models.Inventory;
import com.example.backend.models.Product;
import com.example.backend.models.Project;
import com.example.backend.repositories.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Transactional
@Repository
@Primary
public class ProductRepositoryJPA implements ProductRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Product> findAll() {
        TypedQuery<Product> query =
                this.entityManager.createQuery(
                        "select p from Product p", Product.class);
        return query.getResultList();
    }

    @Override
    public List<Product> getByWarehouse(int id) {
        TypedQuery<Product> query =
                this.entityManager.createQuery(
                        "select p from Product p join Inventory i on p.id = i.productId.id " +
                                "WHERE i.warehouseId.id = :warehouseId"
                        , Product.class);
        query.setParameter("warehouseId", id);
        return query.getResultList();
    }

    @Override
    public Product findById(long id) {
        return entityManager.find(Product.class, id);
    }

    @Override
    public Product save(Product product) {
        if (product.getId() == 0) {
            entityManager.persist(product);
        } else {

            product = entityManager.merge(product);
        }
        return product;
    }

    @Override
    public Product editedProduct(Product product) {
        if (product.getId() == 0) {
            Product existingProduct = entityManager.find(Product.class, product.getId());

            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setDescription(product.getDescription());

            product = entityManager.merge(existingProduct);
        }
        return product;
    }

    @Override
    @Transactional
    public Product updateQuantity(int productId, int updatedQuantity) {
        Product foundProduct = entityManager.find(Product.class, productId);

        if (foundProduct != null) {
            foundProduct.setQuantity(updatedQuantity);
            entityManager.merge(foundProduct);

            return foundProduct;
        } else {
            return null;
        }
    }


    @Override
    public void deleteById(long id) {
        Product product = findById(id);
        entityManager.remove(product);
    }
}
