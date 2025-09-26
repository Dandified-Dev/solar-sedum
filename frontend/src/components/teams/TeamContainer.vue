<template>
  <div class="top_part">
    <h2 id="teamName">{{ team.name }}</h2>
    <img @click="delete_team(team)" src="@/assets/red_x_circle.png" alt="x" width="20" height="20" id="delete_team" />
  </div>
  <div class="middle_part">
    <div>
      <h2 id="member_text">Members:</h2>
      <div v-for="(user) in users" :key="user.id" class="users" id="member_container">
        <div v-if="user.teamId === team.id" class="users" id="member_container">
          <img @click="unassignMember(user)" src="@/assets/red_x_circle.png" alt="gray_x" width="20" height="20" id="gray_x" class="member_button w-5 h-5" />
          <p  id="member_name">{{  user.firstName + " " + user.lastName }}</p>
        </div>
      </div>
    </div>
    <div>
      <h2>Warehouse:</h2>
      <div class="warehouse">
        <img v-if="team.warehouseId !== null && !showWarehouseInput" @click="delete_warehouse(team, team.warehouseId)" src="@/assets/red_x_circle.png" alt="x" width="20" height="20" class="x_circle w-5 h-5 mb-[4%]" />
        <p v-if="!showWarehouseInput">{{ team.warehouseId }}</p>
        <div v-else>
          <select v-model="selectedWarehouseId" class="border-solid border-2 border-black">
            <option value="">Select Warehouse</option>
            <option v-for="warehouse in warehouses" :key="warehouse.id" :value="warehouse.id">{{ warehouse.name }}</option>
          </select>
          <button @click="assign_warehouse">Assign</button>
        </div>
      </div>
    </div>
    <div class="projects_container">
      <h2>Projects:</h2>
      <ul>
        <li v-for="(project, index) in team.projects" :key="index">{{ project.name }}</li>
      </ul>
    </div>
  </div>
  <div class="bottom_part">
    <div @click="toggleWarehouseInputField" class="button">
      <img src="@/assets/warehouse_icon.png" alt="warehouse" />
      <p class="button_text">Assign to warehouse</p>
    </div>
  </div>
</template>

<script>
export default {
  inject: ["warehousesService", "usersService"],
  name: "TeamContainer",
  props: ["team", "users"],
  emits: [
    "delete-team",
    "add-user",
    "unassignMember",
    "delete-warehouse",
    "add-warehouse",
    "update-user", // Add this line
  ],
  async created(){
    this.warehouses = await this.warehousesService.findAll();
  },
  data() {
    return {
      warehouses: [],
      showInputField: false,
      newMember: "",
      showWarehouseInput: false,
      showMemberInput: false,
      newWarehouse: "",
      selected_member: null,
      edit_member_input: null,
      editingMemberId: null,
      selectedWarehouseId: null,
      selectedMemberId: null,
    };
  },
  methods: {
    delete_team(team) {
      this.$emit("delete-team", team);
    },

    toggleWarehouseInputField() {
      this.showWarehouseInput = !this.showWarehouseInput;
    },

    assign_warehouse() {
      if (this.selectedWarehouseId !== null) {
        const selectedWarehouse = this.warehouses.find(warehouse => warehouse.id === this.selectedWarehouseId);
        if (selectedWarehouse) {
          this.$emit("add-warehouse", this.team, selectedWarehouse);
          this.selectedWarehouseId = null; // Reset the selectedWarehouseId
          this.showWarehouseInput = false; // Hide the input field and button
        }
      }
    },

    unassignMember(user) {
      this.$emit("unassignMember", user);
    },
    delete_warehouse(team, warehouseId) {
      this.$emit("delete-warehouse", team, warehouseId);
    },
  },
};
</script>

<style scoped>
h2 {
  margin-bottom: 10px;
  margin-top: 10px;
  font-weight: 400;
  font-size: revert;
}
p {
  font-size: larger;
  margin: 0;
  margin-bottom: 4%;
}

img {
  cursor: pointer;
}

#gray_x:hover {
  content: url(@/assets/red_x_circle.png);
}
.x_circle {
  margin-right: 1vh;
  cursor: pointer;
}

.top_part {
  display: flex;
  flex-direction: row;
  justify-content: center;
}
#delete_team {
  cursor: pointer;
  margin-top: 1.5rem;
  position: absolute;
  right: 2rem;
}

.middle_part {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin-bottom: 2vh;
  margin-right: 8vw;
}
#member_text {
  margin-left: 5rem;
  text-align: start;
}
#member_name {
  width: 10rem;
  text-align: start;
}
#member_container {
  margin-left: 1rem;
}
.users {
  display: flex;
}
.member_button {
  margin-right: 6%;
}
.warehouse {
  display: flex;
  flex-direction: row;
  align-items: center;
}
#warehouse_name {
  text-align: start;
  margin-right: 6vh;
  margin-left: 2vh;
}
.bottom_part {
  position: absolute;
  bottom: 3%;
  right: 5%;
  display: flex;
  justify-content: space-between;
}
.projects_container {
  margin-bottom: 2rem;
}
.button {
  display: flex;
  flex-direction: row;
  cursor: pointer;
  color: #ffffff;
  background-color: #5ada3a;
  border: 0;
  border-radius: 1vh;
  padding: 1.5vh;
  margin-left: 1rem;
  margin-top: 7rem;
}
.button_text {
  font-size: small;
  font-weight: 100;
  margin-top: auto;
  margin-bottom: auto;
  margin-left: 1vh;
}
</style>
