import Product from "@/models/team";

class ProductAdapter {
    resourceUrl;

    // constructor() {
    //     this.resourceUrl = process.env.VUE_APP_API_URL;
    // }
    constructor(resourceURL) {
        this.resourceUrl = resourceURL;

    }

    async findAll() {
        const products = await this.fetchJson(this.resourceUrl, 'GET');
        return products.map(product => Product.copyConstructor(product));
    }

    async findById(id) {
        const product = await this.fetchJson(`${this.resourceUrl}/${id}`, 'GET');
        return Product.copyConstructor(product);
    }

    async add(product) {
        const productToAdd = await this.fetchJson(this.resourceUrl, "POST", product)
        return Product.copyConstructor(productToAdd);
    }

    async getByWarehouseId(id) {
        return await this.fetchJson(`${this.resourceUrl}/warehouseId/${id}`, "GET");
    }

    async save(product) {
        if (!(product instanceof Product)) {
            throw new Error('Invalid product object');
        }
        const productToSave = await this.fetchJson(`${this.resourceUrl}/updateProduct/${product.id}`, "PUT", product)
        return Product.copyConstructor(productToSave);
    }

    async updateProduct(id, name, price, description) {
        console.log("adapter id", id)

        const url = `${this.resourceUrl}/updateProduct/${id}
        ?name=${name}&price=${price}&description=${description}`;

        const response = await this.fetchJson(url, 'PUT');

        return Product.copyConstructor(response);
    }

    async updateQuantity(productId, updatedQuantity) {
        const url = `${this.resourceUrl}/updateQuantity/${productId}?updatedQuantity=${updatedQuantity}`;

        const response = await this.fetchJson(url, "PUT");

        return Product.copyConstructor(response);
    }


    async deleteById(id) {
        await this.fetchJson(`${this.resourceUrl}/${id}`, "DELETE")
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
}

export default ProductAdapter;
