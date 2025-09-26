<template>
  <div class="top_part">
    <h2>{{ warehouse.name }}</h2>
    <img @click="delete_warehouse(warehouse)" src="@/assets/red_x_circle.png" alt="x" width="20" height="20" id="delete_warehouse"/>
  </div>
  <div class="middle_part">
    <div class="info-section">
      <p>Location:</p>
      <ul class="team-item">
        <li v-if="!editingLocation">{{ warehouse.location }}</li>
        <input v-else v-model="editedLocation" @keyup.enter="savedLocation(warehouse)" style="width: 110px;" />
        <img @click="toggleEditLocation" src="@/assets/edit_icon.png" alt="editIcon" class="delete_team_button" />
      </ul>
    </div>
    <div class="info-section">
      <p>Teams:</p>
      <ul>
        <li v-for="(team, index) in warehouse.teams" :key="index" class="team-item">{{ team.name }}
          <img @click="delete_team(warehouse, team)" src="@/assets/red_x_circle.png" alt="x" width="20" height="20" class="delete_team_button" />
        </li>
      </ul>
    </div>
    <div class="info-section">
      <p>Projects:</p>
      <ul>
        <li v-for="(project, index) in warehouse.projects" :key="index">{{ project.name }}</li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  name: "WarehouseContainer",
  props: ["warehouse"],
  emits: [
    "delete-warehouse",
    "delete-team",
    "update-location"
  ],
  data() {
    return {
      editingLocation: false,
      editedLocation: this.warehouse.location,
    };
  },

  methods: {
    toggleEditLocation() {
      this.editingLocation = !this.editingLocation;
    },
    savedLocation(warehouse) {
      warehouse.location = this.editedLocation;
      this.$emit("update-location", warehouse);
      this.editingLocation = false;
    },
    delete_warehouse(warehouse) {
      this.$emit("delete-warehouse", warehouse);
    },
    delete_team(warehouse, team) {
      console.log(team)
      this.$emit("delete-team", warehouse, team);
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

.top_part {
  display: flex;
  flex-direction: row;
  justify-content: center;
}

#delete_warehouse {
  cursor: pointer;
  margin-top: 1.5rem;
  position: absolute;
  right: 2rem;
}

.middle_part {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin-top: 1vh;
  margin-bottom: 2vh;
  margin-right: 8vw;
  margin-left: 7vw;
}
.team-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
  padding: 5px;
}
.delete_team_button {
  margin-left: 10px;
}
</style>
