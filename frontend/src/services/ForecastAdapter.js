import Forecast from "@/models/forecast";

class ForecastAdapter {
    resourceUrl;

    /**
     * Constructor for ForecastAdapter.
     * @param {string} resourceURL - The base URL for the forecast resource.
     */
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
        // Convert the body to JSON if provided
        let jsonBody = bodyToSend ? JSON.stringify(bodyToSend) : null;

        // Perform the HTTP request
        let response = await fetch(url, {
            method: method,
            body: jsonBody,
            headers: {
                'Content-Type': 'application/json',
            }
        });

        // Check for HTTP errors
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        // Parse and return the JSON response (if not a DELETE request)
        return method !== 'DELETE' ? await response.json() : null;
    }

    /**
     * Fetch all forecasts from the API.
     * @returns {Promise<Forecast[]>} - A promise that resolves to an array of forecasts.
     */
    async findAll() {
        // Fetch all forecasts from the specified resource URL
        const forecasts = await this.fetchJson(this.resourceUrl, 'GET');
        // Map the JSON forecasts to Forecast objects using the copy constructor
        return forecasts.map(forecast => Forecast.copyConstructor(forecast));
    }

    /**
     * Fetch a forecast by its ID from the API.
     * @param {number} id - The ID of the forecast to fetch.
     * @returns {Promise<Forecast>} - A promise that resolves to the fetched forecast.
     */
    async findById(id) {
        // Fetch a forecast by its ID from the specified resource URL
        const forecast = await this.fetchJson(`${this.resourceUrl}/${id}`, 'GET');
        // Map the JSON forecast to a Forecast object using the copy constructor
        return Forecast.copyConstructor(forecast);
    }
}

export default ForecastAdapter;
