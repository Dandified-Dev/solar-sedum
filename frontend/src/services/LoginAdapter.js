import Login from "@/models/login";

class LoginAdapter {
    resourceUrl;

    // constructor() {
    //     this.resourceUrl = process.env.VUE_APP_API_URL;
    // }
    constructor(resourceURL) {
        this.resourceUrl = resourceURL;

    }

    async authenticate(username, password) {
        const credentials = { username, password }; // Combine into an object
        const response = await this.fetchJson(`${this.resourceUrl}/authenticate`, 'POST', credentials);

        if (response.success) {
            // Authentication successful
            return {
                success: true,
                message: "Login successful!",
                userId: response.userId // Includes the user ID in the response
            };
        } else {
            // Authentication failed
            return { success: false, message: "Login failed!" };
        }
    }


    async findAll() {
        const logins = await this.fetchJson(this.resourceUrl, 'GET');
        return logins.map(login => new Login(login.username, login.password));
    }

    async findById(id) {
        const login = await this.fetchJson(`${this.resourceUrl}/${id}`, 'GET');
        return new Login(login.username, login.password);
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
            const errorText = await response.text();
            throw new Error(`HTTP error! Status: ${response.status}, Text: ${errorText}`);
        }

        const data = await response.json();
        return data;
    }

}

export default LoginAdapter;
