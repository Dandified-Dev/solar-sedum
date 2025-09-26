<template>
  <div @mousedown="handleFormCollapse">
    <ProjectAddComponent ref="projectAddComponent" @add_project="add_project" @collapse_form="collapseForm" :showFormValue="showForm"/>
    <ProjectUpdateComponent @update_project="updateProject" @collapse_form="collapseUpdateForm" :showFormValue="showUpdateForm" :existingProject="project"/>
    <div class="main">
      <ProjectTable @openForm="handleAddProjectClicked" @closeForm="handleFormCollapse" @showUpdateForm="openUpdateForm" @deleted_project_filter="deleted_project_filter" :projects="projects"/>
    </div>
  </div>
</template>
<script>
import ProjectTable from "@/components/projects/ProjectTable.vue";
import ProjectAddComponent from "@/components/projects/ProjectAddComponent.vue";
import ProjectUpdateComponent from "@/components/projects/ProjectUpdateComponent.vue";
export default {
  inject: ['projectsService', 'orderService'],
  components:{ProjectUpdateComponent, ProjectAddComponent, ProjectTable},
  data(){
    return{
      projects: [],
      showForm: false,
      showUpdateForm:false,
      project: null
    }
  },
  async created(){
    this.projects = await this.projectsService.findAll();
  },
  methods:{
    async add_project(project, teamId, order) {
      console.log(project)
      console.log(teamId)
      console.log(order)
      const projectAdded = await this.projectsService.add(project, teamId);
      if(projectAdded) {
        console.log("Project added successfully:", projectAdded);
        // this.filteredProjects.push(project);
        let projectId = projectAdded.id;
        if (Array.isArray(order)) {
          console.log(order)
          for (const orderToAdd of order) {
            console.log(orderToAdd)
            await this.orderService.add(orderToAdd, projectId);
          }
        } else {
          console.error("Orders is not an array:", order);
        }
        this.projects = await this.projectsService.findAll();
      }
      this.handleFormCollapse()
    },
    // async updateProject(project, teamId, orders){
    //   project.teamId = null;
    //   project.warehouse = null;
    //   const projectUpdate = await this.projectsService.save(project, teamId)
    //   console.log(projectUpdate)
    //   let projectId = projectUpdate.id;
    //   if (Array.isArray(orders)) {
    //     console.log(orders)
    //     for (const order of orders) {
    //       if (order.id){
    //         await this.orderService.save(order, projectId)
    //       }
    //       else {
    //         await this.orderService.add(order, projectId);
    //       }
    //     }
    //   } else {
    //     console.error("Orders is not an array:", orders);
    //   }
    // },
    async updateProject(project, teamId, orders){
      project.teamId = null;
      project.warehouse = null;
      const projectUpdate = await this.projectsService.save(project, teamId)
      console.log(projectUpdate)
      let projectId = projectUpdate.id;
      const inventoryIds = [];
      console.log(orders)

      for (const order of orders){
          console.log(order.inventory.id)
          inventoryIds.push(order.inventory.id)
          console.log("inventory.id")
        order.project=null;
        order.inventory = null;
      }
      await this.orderService.save(orders, projectId, inventoryIds)
      this.projects = await this.projectsService.findAll();
    },
    handleFormCollapse(event) {
      if (event){
        // Check if the clicked element is not part of the ProjectAddComponent
        if (event.target === this.$refs.projectAddComponent.$el && event.target !== undefined) {
          console.log("close");
          this.showForm = false;
        }
      }
    },
    collapseForm(){
      this.showForm = false;

    },
    collapseUpdateForm(){
      this.showUpdateForm = false;
      this.project = null
    },
    handleAddProjectClicked() {
      console.log("open")
      this.showForm = true;
    },
    openUpdateForm(project){
      this.project = project
      this.showUpdateForm=true;

    },
    async deleted_project_filter() {
      this.projects = await this.projectsService.findAll();
    }
  }
}
</script>

<style scoped>
.main {
  margin-top: 6%;
  position: absolute;
  z-index: -1;
  margin-left: 21%;
  width: 69%;
  border-radius: 12px;
  flex-direction: column;
  display: flex;
  align-items: center;
  min-height: 30rem;
  text-align: center;
}
</style>