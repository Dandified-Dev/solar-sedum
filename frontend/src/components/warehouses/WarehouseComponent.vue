<template>
  <div>

    <SearchAddComponent @search="handleSearch" @add-element-clicked="handleAddWarehouse" searchPlaceholder="Warehouse name" addButtonLabel="Add warehouse"/>
    <WarehouseAddComponent @add_warehouse="add_warehouse" @collapse_form="handleFormCollapse" :showFormValue="showForm" />


    <div class="main">
      <div v-for="warehouse in filteredWarehouses" :key="warehouse.id" class="card">
        <WarehouseContainer :warehouse="warehouse" @delete-warehouse="delete_warehouse" @delete-team="delete_team" @update-location="savedLocation" />
      </div>
    </div>

    {{warehouses.name}}
  </div>
</template>

<script>
import SearchAddComponent from "@/components/SearchAddComponent.vue";
import WarehouseContainer from "@/components/warehouses/WarehouseContainer.vue";
import WarehouseAddComponent from "@/components/warehouses/WarehouseAddComponent.vue";
import confirmDialog from "@/models/confirmDialog";

export default {
  inject: ['warehousesService'],
  name: "WarehouseComponent",
  components: { SearchAddComponent, WarehouseContainer, WarehouseAddComponent},
  async created() {
    this.warehouses = await this.warehousesService.findAll();
    this.filteredWarehouses = this.warehouses;
    this.$emit('warehouses-loaded', this.warehouses);

  },
  data() {
    return {
      warehouses: [], // Define an empty array initially
      filteredWarehouses: [], // Initialize as needed
      showForm: false,
    };
  },
  methods: {
    async add_warehouse(warehouse) {
      await this.warehousesService.add(warehouse)
      this.filteredWarehouses.push(warehouse);
    },
    async delete_warehouse(warehouse) {
      await confirmDialog.showConfirmationDialog(warehouse.name, async () => {
        await this.warehousesService.deleteById(warehouse.id)
        this.filteredWarehouses = this.filteredWarehouses.filter((w) => w.id !== warehouse.id);
      })

    },
    async delete_team(warehouse, team) {
      await confirmDialog.showConfirmationDialog(team.name, async () => {
        await this.warehousesService.removeTeam(warehouse.id, team.id);
        this.filteredWarehouses = this.filteredWarehouses.map((wh) => {
          if (wh.id === warehouse.id) {
            wh.teams = wh.teams.filter((t) => t.id !== team);
            wh.projects = wh.projects.filter((project) => project.teamId !== team);
          }
          return wh;
        });
      })

    },
    async savedLocation(warehouse) {
      await this.warehousesService.save(warehouse);
    },
    handleSearch(searchTerm) {
      const lowerCaseSearchTerm = searchTerm.toLowerCase();

      this.filteredWarehouses = this.warehouses.filter((warehouse) =>
          warehouse.name.toLowerCase().includes(lowerCaseSearchTerm)
      );
    },
    handleFormCollapse() {
      this.showForm = false;
    },
    handleAddWarehouse() {
      this.showForm = true;
    },
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
