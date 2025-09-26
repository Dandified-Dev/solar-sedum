import Warehouse from "@/models/warehouse";

class WarehousesAdapter{
    resourceUrl;
    // constructor() {
    //     this.resourceUrl = process.env.VUE_APP_API_URL;
    // }
    constructor(resourceURL) {
        this.resourceUrl = resourceURL;

    }

    async findAll(){
        const warehouses = await this.fetchJson(this.resourceUrl, 'GET');
        return warehouses.map(warehouse => Warehouse.copyConstructor(warehouse));
    }

    async findById(id){
        const warehouse = await this.fetchJson(`${this.resourceUrl}/${id}`, 'GET');
        return Warehouse.copyConstructor(warehouse);
    }

    async add(warehouse){
        const warehouseToAdd = await this.fetchJson(`${this.resourceUrl}/add`, "POST", warehouse)
        return Warehouse.copyConstructor(warehouseToAdd);
    }

    async save(warehouse){
        console.log(warehouse.id);
        console.log(warehouse);
        const warehouseToSave =  await this.fetchJson(`${this.resourceUrl}/${warehouse.id}`, "PUT", warehouse);
        return Warehouse.copyConstructor(warehouseToSave);
    }

    async deleteById(id){
        await this.fetchJson(`${this.resourceUrl}/${id}`, "DELETE")
    }

    async removeTeam(warehouseId, teamId){
        const bodyToSend = [warehouseId, teamId];
        await this.fetchJson(`${this.resourceUrl}/${warehouseId}/removeTeam/${teamId}`, "DELETE", bodyToSend);
    }

    async fetchJson(url, method, bodyToSend){
        let jsonBody = bodyToSend ? JSON.stringify(bodyToSend) : null;
        let response = await fetch(url,  {
            method: method,
            body: jsonBody,
            headers: {
                'Content-Type': 'application/json',
            }

        });
        let data = null;
        if (method != 'DELETE' ) {
            data = await response.json();
        }
        if (response.ok) {
            return data;
        } else {
            console.log("Error while getting data.")
            return null;
        }
    }

}
export default WarehousesAdapter;