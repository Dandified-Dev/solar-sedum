<template>
  <div class="bg-white relative border rounded-lg w-full">
    <div class="flex items-center justify-between">
      <div class="flex justify-between w-1/5 py-3 px4">

        <SearchForm @search="handleSearch" />
        <form class="py-3 px4">
            <button class="bg-[#02A9F5] text-white border border-gray-300 text-gray-900  p-2 rounded-2xl py-3 px4 addButton w-max ml-[1rem]" @click="handleAddProjectClicked()">Add Project</button>
        </form>
      </div>

      <div class="flex items-center justify-end text-sm font-semibold">
        <FilterRadios @filter="handleRadioFilter"/>
        <FilterDropdown :projects="projects" @filter="handleCheckBoxFilter"/>
      </div>
    </div>
    <table class="w-full text-sm text-left text-gray-500">
      <thead>
      <tr>
        <th class="px-4 py-3">Project</th>
        <th class="px-4 py-3">Assigned to</th>
        <th class="px-4 py-3">Status</th>
        <th class="px-4 py-3">Location</th>
        <th class="px-4 py-3">Due at</th>
        <th class="px-4 py-3">
          <span class="sr-only">Actions</span>
        </th>
      </tr>
      </thead>
      <tbody class="border-b" v-for="project in filteredProjects" :key="project.id">
      <tr class=" border-t-2 border-gray-500">
        <td class="px-4 py-3">{{ project.name }}</td>
        <td class="px-4 py-3">{{ getTeamName(project.teamId) }}</td>
        <td class="px-4 py-3">{{ project.status }}</td>
        <td class="px-4 py-3">{{ project.location }}</td>
        <td class="px-4 py-3">{{ formatDate(project.dueAt) }}</td>
<!--        <td class="px-4 py-3 flex items-center justify-end">-->
<!--          <ProjectInfoComponent :project="project" @deleteProject="deleteProject"/>-->
<!--        </td>-->
        <td class="px-4 py-3 flex items-center justify-end">
          <ProjectInfoComponent :project="project" @deleteProject="deleteProject" @showUpdateForm="showUpdateForm"/>
        </td>
      </tr>
      </tbody>
    </table>
  </div>


</template>
<script >

import {defineComponent} from "vue";
import SearchForm from "@/components/SearchForm.vue";
import FilterRadios from "@/components/projects/FilterRadios.vue";
import FilterDropdown from "@/components/projects/FilterDropdown.vue";
import ProjectInfoComponent from "@/components/projects/ProjectInfoComponent.vue";


export default defineComponent({
  props: ["projects"],
  inject:['projectsService', "teamsService", "orderService"],
  components: { SearchForm, FilterRadios, FilterDropdown, ProjectInfoComponent},
  emits: ["deleteProject", "closeForm", "openForm", "showUpdateForm", "deleted_project_filter"],
  async created() {
    this.teams = await this.teamsService.findAll()
    console.log(this.showForm)
  },
  data() {
    return {
      showForm: false,
      searchFilter: '',
      radioFilter: '',
      statusesFilter: [],
      teams:[]
    };
  },
  computed: {
    teamsMap() {
      // Assuming teams is an array of team objects with 'id' and 'name' properties
      return this.teams.reduce((map, team) => {
        map[team.id] = team.name;
        return map;
      }, {});
    },
    filteredProjects() {
      const searchValue = this.searchFilter.toLowerCase();
      let items = this.projects;

      // Apply radio filter based on the selected value
      switch (this.radioFilter) {
        case "today":
          // Show projects due today
          items = items.filter(
              (item) =>
                  new Date(item.dueAt).getDate() === new Date().getDate()
          );
          break;
        case "past":
          // Show projects past due
          items = items.filter(
              (item) =>
                  new Date(item.dueAt).getDate() < new Date().getDate()
          );
          break;
          // Add more cases for other radio filter options if needed
      }

      console.log(this.statusesFilter)
      if (this.statusesFilter.length) {
        items = items.filter(item => this.statusesFilter.includes(item.status));
      }


      // Apply search filter
      if (searchValue.trim() !== "") {
        items = items.filter(
            (project) =>
                project.name.toLowerCase().includes(searchValue) ||
                project.location.toLowerCase().includes(searchValue)
        );
      }

      return items;
    },
  },

  methods: {
    handleFormCollapse() {
      // this.showForm = false;
      this.$emit("closeForm")

    },
    handleAddProjectClicked() {
      // this.showForm = true;
      this.$emit("openForm")
    },
    handleSearch(search) {
      this.searchFilter = search;
    },
    handleRadioFilter(filter) {
      this.radioFilter = filter;
    },
    getTeamName(teamId) {
      return this.teamsMap[teamId] || "Unknown Team";
    },
    handleCheckBoxFilter(filter){
      console.log(filter)
      if (this.statusesFilter.includes(filter)){
        return this.statusesFilter.splice(this.statusesFilter.indexOf(filter), 1);
      }

      return this.statusesFilter.push(filter)
    },
    formatDate(date) {
      return new Date(date).toLocaleDateString();
    },
    // async add_project(project, teamId, order) {
    //   console.log(project)
    //   console.log(teamId)
    //   console.log(order)
    //
    //   const projectAdded = await this.projectsService.add(project, teamId);
    //   if(projectAdded) {
    //     console.log("Project added successfully:", projectAdded);
    //     this.filteredProjects.push(project);
    //     let projectId = projectAdded.id;
    //     if (Array.isArray(order)) {
    //       for (const orderToAdd of order) {
    //         await this.orderService.add(orderToAdd, projectId);
    //       }
    //     } else {
    //       console.error("Orders is not an array:", order);
    //     }
    //   }
    //   this.handleFormCollapse()
    // },
    async deleteProject(project){
      await this.projectsService.deleteById(project.id)
      this.$emit("deleted_project_filter")
    },
    showUpdateForm(project){
      this.$emit("showUpdateForm", project)
    }
  },
})
</script>

<style scoped>
.addButton{
  border-radius: 10px !important;
  background-color: white !important;
  border : 1px solid #555 !important;
  color: #555 !important;
  box-shadow: 0 0 0 0 rgba(0, 0, 0, 0) !important;
}

.addButton:hover{
  border-radius: 10px !important;
  background-color: #555 !important;
  color: white !important;
}
</style>