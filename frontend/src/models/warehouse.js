class Warehouse {
    constructor(id, name, location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }
    static createSampleWarehouse(id) {
        const newWarehouse = new Warehouse(id)
        newWarehouse.name = "warehouse 1"
        newWarehouse.location = "Amsterdam"

        return newWarehouse;
    }

    addInventory(item, quantity) {
        if (this.inventory[item.id]) {
            this.inventory[item.id].quantity += quantity;
        } else {
            this.inventory[item.id] = {
                item,
                quantity,
            };
        }
    }

    removeInventory(item, quantity) {
        if (this.inventory[item.id]) {
            this.inventory[item.id].quantity -= quantity;
            if (this.inventory[item.id].quantity < 0) {
                this.inventory[item.id].quantity = 0;
            }
        }
    }

    assignInventory(selectedWarehouseId, items) {
        if (this.warehouseId === selectedWarehouseId) {
            items.forEach(item => {
                const quantity = Math.floor(Math.random() * 30);
                this.addInventory(item, quantity);
            });
        }
    }

    static copyConstructor(warehouse){
        if (warehouse == null) return null;
        return Object.assign(new Warehouse(), warehouse);
    }

}

export default Warehouse;