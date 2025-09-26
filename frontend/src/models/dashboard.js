// import TeamDashboard from "@/models/teamdashboard.js";
//
// class Dashboard {
//     constructor(warehouseId, name, location, teams, projects) {
//         this.warehouseId = warehouseId;
//         this.name = name;
//         this.location = location;
//         this.teams = [];
//         this.projects = projects;
//     }
//
//     static randomNumber() {
//         return Math.floor(Math.random() * 1000);
//     }
//
//     static Project = ["1125", "1126", "1127", "1128", "1129", "1130", "1131", "1132", "1133", "1134", "1135", "1136",
//         "1137", "1138", "1139", "1140", "1141", "1142"]
//
//     static Teams = ["1", "2", "3", "4", "5", "6", "7", "8", "9"]
//
//     static createSampleDashboard() {
//         const randomId = this.randomNumber();
//         const randomName = `Warehouse ${this.randomNumber()}`;
//         const randomLocation = ['Amsterdam', 'Rotterdam', 'Den Haag', 'Utrecht', 'Delft'];
//         const selectedLocation = randomLocation[Math.floor(Math.random() * randomLocation.length)];
//
//
//         const dashboard = new Dashboard(randomId, randomName, selectedLocation);
//
//         const numberOfTeams = Math.floor(Math.random() * 3) + 1; // Randomly between 1 and 3 teams
//
//         for (let i = 0; i < numberOfTeams; i++) {
//             const randomTeam = TeamDashboard.createSampleTeam();
//             randomTeam.warehouse = randomId;
//             dashboard.teams.push(randomTeam);
//         }
//
//         console.log(dashboard);
//
//         return dashboard;
//     }
//
//
// }
//
//
// export default Dashboard;