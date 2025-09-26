import Inventory from "@/models/inventory";

class InventoryAdapter {
    resourceUrl;

    // constructor() {
    //     this.resourceUrl = process.env.VUE_APP_API_URL;
    // }
    constructor(resourceURL) {
        this.resourceUrl = resourceURL;

    }


    async fetchJson(url, method, bodyToSend) {
        const jsonBody = bodyToSend ? JSON.stringify(bodyToSend) : null;
        const response = await fetch(url, {
            method: method,
            body: jsonBody,
            headers: {
                'Content-Type': 'application/json',
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return method !== 'DELETE' ? await response.json() : null;
    }

    async findByWarehouse(warehouseId) {
        return await this.fetchJson(`${this.resourceUrl}/getWarehouse/${warehouseId}`, "GET");

    }

    async aggregatedInventory() {
        return await this.fetchJson(`${this.resourceUrl}/aggregatedInventory`, "GET")
    }

    async findAll() {
        const inventoryItems = await this.fetchJson(this.resourceUrl, 'GET');
        return inventoryItems.map(item => Inventory.copyConstructor(item));
    }

    async findById(id) {
        const item = await this.fetchJson(`${this.resourceUrl}/${id}`, 'GET');
        return Inventory.copyConstructor(item);
    }

    async findByTeamId(teamId) {
        return await this.fetchJson(`${this.resourceUrl}/teams/${teamId}`, "GET");
    }

    async addItem(item) {
        const itemToAdd = await this.fetchJson(this.resourceUrl, 'POST', item);
        return Inventory.copyConstructor(itemToAdd);
    }

    async updateItem(id, quantity, maxQuantity, lowStockLimit) {
        const url = `${this.resourceUrl}/updateInventory/${id}
        ?quantity=${quantity}&maxQuantity=${maxQuantity}&lowStockLimit=${lowStockLimit}`;

        const response = await this.fetchJson(url, 'PUT');

        return Inventory.copyConstructor(response);

    }

    async deleteItem(id) {
        await this.fetchJson(`${this.resourceUrl}/${id}`, 'DELETE');
    }

    async assignWarehouse(itemId, warehouseId) {
        const bodyToSend = [itemId, warehouseId];
        await this.fetchJson(`${this.resourceUrl}/${itemId}/warehouse/${warehouseId}`, 'POST', bodyToSend);
    }

    async addItemWithWarehouse(item, productId, warehouseId) {
        try {
            const foundProduct = await this.fetchJson(`http://localhost:8086/products/${productId}`, 'GET');

            if (!foundProduct) {
                throw new Error('Product not found');
            }

            const foundWarehouse = await this.fetchJson(`http://localhost:8086/warehouses/${warehouseId}`, 'GET');

            if (!foundWarehouse) {
                throw new Error('Warehouse not found');
            }

            item.product_id = foundProduct.id;
            item.warehouse_id = foundWarehouse.id;

            const addedItem = await this.fetchJson(`${this.resourceUrl}/${productId}/${warehouseId}`, 'POST', item);

            return Inventory.copyConstructor(addedItem);
        } catch (error) {
            throw new Error(`Error adding item with product: ${error.message}`);
        }
    }

}

export default InventoryAdapter;
