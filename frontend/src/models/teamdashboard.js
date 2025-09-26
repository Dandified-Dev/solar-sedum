// import Warehouse from "@/models/warehouse.js";
// import Project from "@/models/project.js";
//
//
// class TeamDashboard extends Warehouse {
//     constructor(id, name, warehouse, members, projects) {
//         super();
//         this.id = id;
//         this.name = name;
//         this.warehouse = null;
//         this.members = members;
//         this.projects = projects;
//     }
//
//     static randomNumber() {
//         return Math.floor(Math.random() * 1000);
//     }
//
//     static createSampleTeam() {
//         const randomId = this.randomNumber();
//         const randomName = `Team ${this.randomNumber()}`;
//         // const randomWarehouse = `AMS ${this.randomNumber()}`;
//         const randomMembers = ['John', 'Jane', 'Jack'];
//         const randomProjects = Project.createSampleProject()
//
//         const team = new TeamDashboard(randomId, randomName, null, randomMembers, randomProjects);
//         team.warehouse = randomId;
//
//         return team;
//     }
// }
//
//
// export default TeamDashboard;