
class Team {
    constructor(id, name, warehouse) {
        this.id = id;
        this.name = name;
        this.warehouse = warehouse;
    }

    static randomNumber() {
        return Math.floor(Math.random() * 1000);

    }

    static createSampleTeam(id) {
        const newTeam = new Team(id);
        newTeam.name = "Team " + id;
        newTeam.warehouse = null;

        return newTeam;
    }

    static copyConstructor(team){
        if (team == null) return null;
        return Object.assign(new Team(), team);
    }
}


export default Team;