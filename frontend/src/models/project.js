class Project {
  constructor(id, name, location, description, status, team, materials) {
    this.id = id;
    this.name = name;
    this.location = location;
    this.description = description;
    this.status = status;
    this.team = team;
    this.materials = materials;
  }
  static randomNumber() {
    return Math.floor(Math.random() * 1000);
  }
  static createSampleProject() {
    const randomId = this.randomNumber();
    const randomName = `Project ${this.randomNumber()}`;
    const randomLocation = `Location ${this.randomNumber()}`;
    const randomDescription = `lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud`;
    const randomStatus = `Ongoing`;
    const randomTeam = this.randomNumber();
    const randomMaterialList = ["Material 1", "Material 2", "Material 3"];
    return new Project(
      randomId,
      randomName,
      randomLocation,
      randomDescription,
      randomStatus,
      randomTeam,
      randomMaterialList
    );
  }

  static copyConstructor(project){
    if (project == null) return null;
    return Object.assign(new Project(0), project);
  }
}
export default Project;
