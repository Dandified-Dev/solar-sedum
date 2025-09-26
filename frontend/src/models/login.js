class Login {
    constructor( username, password) {
        this.username = username;
        this.password = password;
    }

    users() {
        return this.username === 'admin' && this.password === 'admin';
    }
}

export default Login;
