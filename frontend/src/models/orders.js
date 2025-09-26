class Orders {
    constructor(id, project, inventory, quantity, status) {
        this.id = id;
        this.project = project;
        this.inventory = inventory;
        this.quantity = quantity;
        this.status = status;
    }

    static copyConstructor(order){
        if (order == null) return null;
        return Object.assign(new Orders(0), order);
    }
}

export default Orders;