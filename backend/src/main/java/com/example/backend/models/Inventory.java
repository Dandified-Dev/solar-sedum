package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;

import java.util.List;

@Entity
@Table(name= "Inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int quantity;
    private int max_quantity;
    private boolean isPending;
    private STATUS status;
    private int low_stock_limit;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouseId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id")
    private Product productId;


    @OneToMany(mappedBy = "inventory", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Orders> orders;

    public Inventory(){}

    public Inventory(int id, int quantity, int max_quantity, int low_stock_limit,  boolean isPending, Warehouse warehouseId, Product productId, List<Orders> orders) {
        this.id = id;
        this.quantity = quantity;
        this.isPending = isPending;
        this.status = calculateStatus();
        this.warehouseId = warehouseId;
        this.productId = productId;
        this.max_quantity = max_quantity;
        this.orders = orders;
        this.low_stock_limit = low_stock_limit;
    }
    private static final int LOW_LIMIT = 25;


    public enum STATUS {
        PENDING, NO_STOCK, LOW, HIGH, FULL
    }
//    private STATUS calculateStatus() {
//        if (isPending) {
//            return STATUS.PENDING;
//        } else if (quantity <= 0) {
//            return STATUS.NO_STOCK;
//        } else if (quantity < (max_quantity/10 * 3)) {
//            return STATUS.LOW;
//        } else {
//            return STATUS.FULL;
//        }
//    }

    private STATUS calculateStatus() {
        int low_stock_limit = getLow_stock_limit(); // Haal de lowStockLimit op

        if (isPending) {
            return STATUS.PENDING;
        } else if (quantity <= 0) {
            return STATUS.NO_STOCK;
        } else if (quantity < low_stock_limit) {
            return STATUS.LOW;
        } else if (quantity < max_quantity) {
            return STATUS.HIGH;
        } else  {
            return STATUS.FULL;
        }
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getWarehouseId() {
        if (warehouseId != null) {
            return warehouseId.getId();
        }
        return null;
    }

    public Integer getProductId() {
        if (productId != null) {
            return productId.getId();
        }
        return null;
    }

    public void setLow_stock_limit(int low_stock_limit) {
        this.low_stock_limit = low_stock_limit;
    }

    public int getLow_stock_limit() {
        return low_stock_limit;
    }


    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public void setWarehouseId(Warehouse warehouseId) {
        this.warehouseId = warehouseId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    public List<Orders> getOrders() {
        if (orders != null) {
            return orders;
        }
        return null;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }

    public int getMax_quantity() {
        return max_quantity;
    }

    public void setMax_quantity(int max_quantity) {
        this.max_quantity = max_quantity;
    }
}
