class Inventory {
    static STATUS = {
        PENDING: "pending",
        NO_STOCK: "no stock",
        LOW: "low",
        FULL: "full",
        HIGH: "high",
    };

    static LOW_LIMIT = 10;

    constructor(id, quantity, description, isPending = false, status, warehouseId, productId, max_quantity, orders, low_stock_limit) {
        this.id = id;
        this.quantity = quantity;
        this.isPending = isPending;
        this.status = status;
        this.warehouseId = warehouseId;
        this.productId = productId;
        this.max_quantity = max_quantity;
        this.orders = orders;
        this.low_stock_limit = low_stock_limit;
    }



    // static calculateStatus() {
    //     if (this.isPending) {
    //         return Inventory.STATUS.PENDING;
    //     } else if (this.quantity <= 0) {
    //         return Inventory.STATUS.NO_STOCK;
    //     } else if (this.quantity < Inventory.LOW_LIMIT) {
    //         return Inventory.STATUS.LOW;
    //     } else {
    //         return Inventory.STATUS.FULL;
    //     }
    // }


    static createSampleItem() {
        const image = {
            solarpanel: require('@/assets/inventory/solar_panels.png'),
            grass: require('@/assets/inventory/grass.png'),
            steel: require('@/assets/inventory/steel_foundation.png'),
            plants: require('@/assets/inventory/plants.png'),
            hammer: require('@/assets/inventory/hammer.png')
        };

        return [
            new Inventory(image.solarpanel, "solar panels", 1, Math.floor(Math.random() * 30), "Dit is het eerste item"),
            new Inventory(image.grass, "grass", 2, Math.floor(Math.random() * 30), "Dit is het tweede item", true),
            new Inventory(image.steel, "steel foundation", 3, Math.floor(Math.random() * 30), "Dit is het derde item"),
            new Inventory(image.plants, "plants", 4, Math.floor(Math.random() * 30), "Dit is het vierde item"),
            new Inventory(image.hammer, "hammer", 5, Math.floor(Math.random() * 30), "Dit is het vijfde item"),
        ];
    }

    static copyConstructor(inventory) {
        if (inventory == null) return null;
        return Object.assign(new Inventory(0), inventory);
    }

    static calculateStatus() {
        if (this.quantity <= 0) {
            return Inventory.STATUS.NO_STOCK;
        } else if (this.quantity < (this.maxQuantity/10 * 5)) {
            return Inventory.STATUS.LOW;
        } else if (this.quantity < (this.maxQuantity)) {
            return Inventory.STATUS.HIGH;
        } else {
            return Inventory.STATUS.FULL;
        }
    }
}




export default Inventory;