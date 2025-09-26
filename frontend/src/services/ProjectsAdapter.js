import Project from "@/models/project";

class ProjectsAdapter{
    resourceUrl;
    // constructor() {
    //     this.resourceUrl = process.env.VUE_APP_API_URL;
    // }
    constructor(resourceURL) {
        this.resourceUrl = resourceURL;

    }

    async findAll(){
        const projects = await this.fetchJson(this.resourceUrl, 'GET');
        return projects.map(project => Project.copyConstructor(project));
    }

    async findById(id){
        const project = await this.fetchJson(`${this.resourceUrl}/${id}`, 'GET');
        return Project.copyConstructor(project);
    }

    async add(project, teamId){
        const projectToAdd = await this.fetchJson(`${this.resourceUrl}/team/${teamId}`, "POST", project)
        console.log(projectToAdd)
        return Project.copyConstructor(projectToAdd);
    }

    async save(project, teamId){
        const data = {project, teamId}
        const projectToSave = await this.fetchJson(this.resourceUrl + "/" + project.id, "PUT", data)
        return Project.copyConstructor(projectToSave);
    }

    async deleteById(id){
        await this.fetchJson(`${this.resourceUrl}/${id}`, "DELETE")
    }

    async assignTeam(projectId, teamId){
        await this.fetchJson(`${this.resourceUrl}/${projectId}/team/${teamId}`, "POST")
    }

    async fetchJson(url, method, bodyToSend){
        let jsonBody = bodyToSend ? JSON.stringify(bodyToSend) : null;
        let response = await fetch(url,  {
            method: method,
            body: jsonBody,
            headers: {
                'Content-Type': 'application/json',
            }

        });
        let data = null;
        if (method != 'DELETE' ) {
            data = await response.json();
        }
        if (response.ok) {
            return data;
        } else {
            console.log("Error while getting data.")
            return null;
        }
    }

}
export default ProjectsAdapter;