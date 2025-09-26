import Team from "@/models/team";

class TeamsAdapter {
    resourceUrl;

    // constructor() {
    //     this.resourceUrl = process.env.VUE_APP_API_URL;
    // }
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
        const teams = await this.fetchJson(this.resourceUrl, 'GET');
        return teams.map(team => Team.copyConstructor(team));
    }

    async findById(id) {
        const team = await this.fetchJson(`${this.resourceUrl}/${id}`, 'GET');
        return Team.copyConstructor(team);
    }

    async add(team, warehouseId) {

        const teamToAdd = await this.fetchJson(`${this.resourceUrl}/${warehouseId}`, "POST", team)
        return Team.copyConstructor(teamToAdd);
    }

    async save(team) {
        if (!(team instanceof Team)) {
            throw new Error('Invalid team object');
        }

        const teamToSave = await this.fetchJson(`${this.resourceUrl}/${team.id}`, "PUT", team)
        return Team.copyConstructor(teamToSave);
    }

    async deleteById(id) {
        await this.fetchJson(`${this.resourceUrl}/${id}`, "DELETE")
    }

    async assignWarehouse(teamId, warehouseId) {
        const bodyToSend = [teamId, warehouseId]
        await this.fetchJson(`${this.resourceUrl}/${teamId}/warehouse/${warehouseId}`, "POST", bodyToSend)
    }

    async assignMember(teamId, memberId){
        const bodyToSend = [teamId, memberId]
        await this.fetchJson(`${this.resourceUrl}/${teamId}/addMember/${memberId}`, "POST", bodyToSend)
    }

    async removeWarehouse(teamId, warehouseId){
        const bodyToSend = [teamId, warehouseId]
        await this.fetchJson(`${this.resourceUrl}/${teamId}/warehouse/remove/${warehouseId}`, "PUT", bodyToSend)
    }
}

export default TeamsAdapter;
