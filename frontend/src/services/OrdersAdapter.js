import Orders from "@/models/orders";

class OrdersAdapter {
    resourceUrl;

    constructor(resourceURL) {
        this.resourceUrl = resourceURL;

    }

    /**
     * Fetches JSON data from a given URL using a specified HTTP method.
     * @param {string} url - The URL to fetch data from.
     * @param {string} method - The HTTP method (GET, POST, PUT, DELETE).
     * @param {object} bodyToSend - The data to send with the request (optional).
     * @returns {Promise<any>} - A promise that resolves to the fetched data.
     * @throws {Error} - If an HTTP error occurs.
     */
    async fetchJson(url, method, bodyToSend) {
        let jsonBody = bodyToSend ? JSON.stringify(bodyToSend) : null;
        let response = await fetch(url, {
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

    async findAll() {
        const orders = await this.fetchJson(this.resourceUrl, 'GET');
        return orders.map(order => Orders.copyConstructor(order));
    }

    async findById(id) {
        const order = await this.fetchJson(`${this.resourceUrl}/${id}`, 'GET');
        return Orders.copyConstructor(order);
    }

    async findByProjectId(id){
        const orders = await this.fetchJson(`${this.resourceUrl}/project/${id}`, "GET");
        return orders.map(order => Orders.copyConstructor(order))
    }

    async add(order, projectId) {
        const inventoryId = order.inventoryId;
        console.log(inventoryId)

        const orderOb = new Orders(0, null, null, order.quantity, null);
        console.log(orderOb);

        const orderToAdd = await this.fetchJson(`${this.resourceUrl}/${projectId}/${inventoryId}`, "POST", orderOb);

        return Orders.copyConstructor(orderToAdd);
    }

    // async save(order, projectId) {
    //     const inventoryId = order.inventory.id;
    //
    //     const orderObject = new Orders(order.id, null, null, order.quantity, null);
    //     const data= {orderObject, projectId, inventoryId}
    //
    //     const processedOrder = await this.fetchJson(`${this.resourceUrl}/update-orders`, "PUT", data);
    //
    //     return Orders.copyConstructor(processedOrder);
    // }
    async save(orders, projectId, inventoryIds) {
        // Assuming orders is an array of order objects
        const data = { orders, projectId, inventoryIds};
        console.log(data)

        const processedOrders = await this.fetchJson(`${this.resourceUrl}/update-orders`, "PUT", data);

        // Assuming copyConstructor works for an array of orders
        return Orders.copyConstructor(processedOrders);
    }


    async deleteById(id) {
        await this.fetchJson(`${this.resourceUrl}/${id}`, "DELETE")
    }


    // async assignInventory(orderId, inventoryId) {
    //     await this.fetchJson(`${this.resourceUrl}/${orderId}/warehouse/${inventoryId}`, "POST")
    // }
    //
    // async assignProject(orderId, projectId){
    //     const bodyToSend = [orderId, projectId]
    //     await this.fetchJson(`${this.resourceUrl}/${orderId}/addMember/${projectId}`, "POST", bodyToSend)
    // }

//     @GetMapping("/{inventoryId}")
//     public int totalQuantity(@PathVariable int inventoryId) {
//     return ordersRepository.calculateQuantityLeftOfInventory(inventoryId);
// }

    async getTotalQuantity(inventoryId) {
        await this.fetchJson(`${this.resourceUrl}/${inventoryId}`, "GET")
    }
}

export default OrdersAdapter;
