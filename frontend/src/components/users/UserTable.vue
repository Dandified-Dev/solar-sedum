<template>
  <div class="bg-white relative border rounded-lg w-full">
    <div class="flex items-center justify-between">
      <SearchForm @search="handleSearch" />
      <div class="flex items-center justify-end text-sm font-semibold">
        <AddComponent @addUser="addUser"/>
      </div>
    </div>
    <table class="w-full text-sm text-left text-gray-500">
      <thead>
      <tr>
        <th class="px-4 py-3">First name</th>
        <th class="px-4 py-3">Last name</th>
        <th class="px-4 py-3">Email</th>
        <th class="px-4 py-3">Team</th>
        <th class="px-4 py-3">Role</th>
      </tr>
      </thead>
      <tbody v-for="user in filteredUsers" :key="user.id">
      <tr class="border-t-2 border-gray-500 relative">
        <td class="px-4 py-3">{{ user.firstName }}</td>
        <td class="px-4 py-3">{{ user.lastName }}</td>
        <td class="px-4 py-3">{{ user.email }}</td>
        <td class="px-4 py-3">{{ this.getTeamName(user.teamId) }}</td>
        <td class="px-4 py-3">{{ user.role }}</td>
        <td class="py-3 ml-4 relative flex right-5 inset-y-0">
          <div class="flex items-center space-x-4">
            <EditComponent @editUser="editUser" :user="user"/>
            <img @click="deleteUser(user)" class="h-5" src="@/assets/red_x_circle.png" alt="" id="deleteUser">
          </div>
        </td>

      </tr>
      </tbody>

    </table>
  </div>
</template>

<script>
import SearchForm from "@/components/SearchForm.vue";
import AddComponent from "@/components/users/AddComponent.vue";
import EditComponent from "@/components/users/EditComponent.vue";

export default {
  props: ["users"],
  emits:['addUser', 'deleteUser', 'editUser'],
  inject: ["teamsService"],
  components: {SearchForm ,AddComponent, EditComponent },
  async created() {
    this.teams = await this.teamsService.findAll();
  },
  data() {
    return {
      searchFilter: '',
      showEditPopup: false,
      user: null,
      teams:[]
    };
  },
  computed: {
    filteredUsers() {
      const searchValue = this.searchFilter.toLowerCase();
      if (searchValue.trim() === '') {
        return this.users;
      } else {
        return this.users.filter(user =>
            user.firstName.toLowerCase().includes(searchValue) ||
            user.lastName.toLowerCase().includes(searchValue)
        );
      }
    },
  },
  methods: {
    getTeamName(teamId){
      const team = this.teams.find(team => team.id === teamId)
      if (team !== undefined){
        return team.name
      }
      return team;
    },
    handleSearch(search) {
      this.searchFilter = search;
    },
    addUser(newUser, teamId){
      this.$emit("addUser", newUser, teamId)
    },
    deleteUser(user){
      this.$emit("deleteUser", user)
    },
    editUser(user, teamId){
      this.$emit("editUser", user, teamId)
    },
    openEditComponent(user) {
      this.user = user;
      this.showEditPopup = true;
    },
  },
};
</script>

<style scoped>
/* Add your scoped styles here */
</style>
