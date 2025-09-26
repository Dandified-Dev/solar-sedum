<template>
  <div>
    <SearchAddComponent @search="handleSearch" @add-element-clicked="handleAddTeamClicked" searchPlaceholder="Team name" addButtonLabel="Add team"/>
    <TeamAddComponent @add_team="add_team" @collapse_form="handleFormCollapse" :showFormValue="showForm" />
    <div class="main">
      <div v-for="team in filteredTeams" :key="team.id" class="card">
        <TeamContainer :team="team" :users="users" @delete-team="delete_team" @add-user="add_member" @unassignMember="unassignMember" @delete-warehouse="delete_warehouse" @add-warehouse="add_warehouse" @update-user="update_member"/>
      </div>
    </div>
  </div>
</template>

<script>
import SearchAddComponent from "@/components/SearchAddComponent.vue";
import TeamAddComponent from "@/components/teams/TeamAddComponent.vue";
import TeamContainer from "@/components/teams/TeamContainer.vue";
import confirmDialog from "@/models/confirmDialog";
export default {
  inject: ["teamsService", "usersService"],
  name: "TeamComponent",
  components: { SearchAddComponent, TeamAddComponent, TeamContainer },
  async created() {
    this.teams = await this.teamsService.findAll();
    this.filteredTeams = this.teams;
    this.users = await this.usersService.findAll();
  },

  data() {
    return {
      teams: [],
      users: [],
      filteredTeams: [], // Add this line
      showForm: false,
    };
  },
  methods: {
    async add_team(team, selectedWarehouseId) {
      await this.teamsService.add(team, selectedWarehouseId);
      this.filteredTeams.push(team);
    },
    async delete_team(team) {
      await confirmDialog.showConfirmationDialog(team.name, async () => {
        try {
          await this.teamsService.deleteById(team.id);
          this.filteredTeams = this.filteredTeams.filter((t) => t.id !== team.id);
        }catch (e) {
          console.log(e)
          confirmDialog.showWarningDialog("This team has orders and therefore can't be deleted")
        }
      })
    },
    async unassignMember(user) {
      await confirmDialog.showConfirmationDialog(user.firstName + " " + user.lastName, async () => {
        await this.usersService.unassignMember(user.id);
        this.users = await this.usersService.findAll();
      })

    },
    async delete_warehouse(team, warehouseId) {
       await confirmDialog.showConfirmationDialog(warehouseId, async () => {
          await this.teamsService.removeWarehouse(team.id, warehouseId)
          team.warehouseId = null;
      })

    },
    async add_member(team, user) {
      console.log(team, user)
      await this.teamsService.assignMember(team.id, user.id)
    },
    async add_warehouse(team, warehouse) {
        await this.teamsService.assignWarehouse(team.id, warehouse.id);
        team.warehouseId = warehouse.id
    },

    async update_member(updatedMember) {
      await this.membersService.save(updatedMember);
    },



    // edit_member(team, selected_member, new_member) {
    //   team.users = team.users.map((m) => {
    //     if (m === selected_member) {
    //       return new_member;
    //     } else {
    //       return m;
    //     }
    //   });
    //
    // },
    handleSearch(searchTerm) {
      // Convert both team name and search term to lowercase for case-insensitive search
      const lowerCaseSearchTerm = searchTerm.toLowerCase();

      // Filter teams based on the search term
      this.filteredTeams = this.teams.filter((team) =>
        team.name.toLowerCase().includes(lowerCaseSearchTerm)
      );
    },
    handleFormCollapse() {
      this.showForm = false;
    },
    handleAddTeamClicked() {
      this.showForm = true;
    },
  },
  computed: {
  },
};
</script>

<style scoped>
.main {
  position: absolute;
  z-index: -1;
  margin-left: 21%;
  background-color: #572700;
  width: 69%;
  border-radius: 12px;
  flex-direction: column;
  display: flex;
  align-items: center;
  min-height: 30rem;
  text-align: center;
  margin-top: 17%;
  padding-top: 35px;
  padding-bottom: 30px;
}

.card {
  position: relative;
  background-color: #ffffff;
  width: 90%;
  margin: 2vh;
  border-radius: 4vh;
  padding-bottom: 3vh;
}
</style>
