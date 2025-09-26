class Forecast {
    constructor(id, order, orderQuantity, stock, quantityAfter, date) {
        this.id = id;
        this.orders = order;
        this.orderQuantity = orderQuantity;
        this.stock = stock;
        this.quantityAfter = quantityAfter;
        this.date = date;
    }

    static copyConstructor(forecast){
        if (forecast == null) return null;
        return Object.assign(new Forecast(0), forecast);
    }
}

export default Forecast;