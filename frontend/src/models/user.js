class User {
    constructor(id, firstName, lastName, email, password, role, teamId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.teamId = teamId;
    }

    static copyConstructor(member){
        if (member == null) return null;
        return Object.assign(new User(), member);
    }

    static randomNumber() {
        return Math.floor(Math.random() * 1000);

    }
    static createSample(id) {
        let newUser = new User(id)
        newUser.firstName = "Dannièl";
        newUser.lastName = "Herlaar";
        newUser.email = "dherlaar@outlook.com";
        newUser.password = "test";
        newUser.role = "user";
        newUser.teamId = null;

        return newUser;
    }

}

export default User;