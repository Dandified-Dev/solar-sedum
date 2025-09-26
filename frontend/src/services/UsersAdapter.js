import User from "@/models/user"

class UsersAdapter {
    resourceUrl;

    // constructor() {
    //     this.resourceUrl = process.env.VUE_APP_API_URL;
    // }
    constructor(resourceURL) {
        this.resourceUrl = resourceURL;
    }

    async findAll() {
        const members = await this.fetchJson(this.resourceUrl, "GET");
        return members.map(member => User.copyConstructor(member));
    }

    async add(user) {
        const memberToAdd = await this.fetchJson(this.resourceUrl, "POST", user);
        return User.copyConstructor(memberToAdd);
    }

    async findByEmail(email) {
        const user = await this.fetchJson(`${this.resourceUrl}/getUser/${email}`, 'GET');
        return User.copyConstructor(user);
    }


    async save(user, teamId) {
        const data = {user, teamId}
        console.log(data)
        const memberToSave = await this.fetchJson(this.resourceUrl + "/" + user.id, "PUT", data)
        return User.copyConstructor(memberToSave);
    }

    async deleteById(id) {
        await this.fetchJson(`${this.resourceUrl}/${id}`, "DELETE")
    }

    async unassignTeam(id) {
        const memberToUpdate = await this.fetchJson(`${this.resourceUrl}/${id}/unassignTeam`, "PUT", id)
        return User.copyConstructor(memberToUpdate)
    }

    async unassignMember(id) {
        console.log("KDWADOWAOGIAWDKAWKDAKWDAKWD")
        await this.fetchJson(`${this.resourceUrl}/${id}/unassignTeam`, "PUT", id)
    }

    async checkEmail(email) {
        return await this.fetchJson(`${this.resourceUrl}/checkEmail/${email}`, "GET")
    }

    async findUserByEmail(email) {
        const user = await this.fetchJson(`${this.resourceUrl}/getUser/${email}`, "GET");
        return User.copyConstructor(user);
    }


    async fetchJson(url, method, bodyToSend) {
        // Convert the body to JSON format if provided
        let jsonBody = bodyToSend ? JSON.stringify(bodyToSend) : null;

        // Perform the fetch with provided parameters
        let response = await fetch(url, {
            method: method,
            body: jsonBody,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
            }
        });

        let data = null;
        try {
            if (method !== 'DELETE') {
                data = await response.json();
            }
        } catch (error) {
            console.error('Error parsing JSON:', error);
            return null; // Return null to indicate an issue with JSON parsing
        }

        if (!response.ok) {
            console.error(`Error while fetching data. URL: ${url}, Status: ${response.status}, Response:`, data);
        }

        return data;
    }


    // Asynchronous function to authenticate a user
    async authenticate(email, password) {

        // Create an object containing user credentials
        const credentials = {email, password};

        try {
            // Call fetchJson to authenticate the user
            const response = await this.fetchJson(`${this.resourceUrl}/authenticate`,
                'POST', credentials);

            console.log('Authentication response:', response); // Add this line for logging

            // Check if authentication was successful
            if (response && response.success) {
                // Authentication successful
                localStorage.setItem('token', response.token);

                // Return successful authentication details
                return {
                    success: true,
                    message: "Login successful!",
                    userId: response.userId,
                    role: response.role,
                    email: response.email,
                };
            } else {
                // Authentication failed
                console.error('Login failed:', response ? response.message : 'Adapter error');
                return {success: false, message: "Login failed!"};
            }

        } catch (error) {
            // Log and handle errors during authentication
            console.error('Error during authentication:', error);
            return {success: false, message: "An unexpected error occurred during login."};
        }
    }


}

export default UsersAdapter;