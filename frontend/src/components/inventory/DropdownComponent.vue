<template>
  <div class="dropdown mt-5">
    <select ref="warehouseDropdown" v-model="localSelectedWarehouse" @change="handleWarehouseSelected" style="min-width: 150px;">
      <option value="" disabled></option>
      <option v-for="warehouse in warehouses" :key="warehouse.id" :value="warehouse">
        {{ warehouse.name }}
      </option>
    </select>
    <p @click="unselectWarehouse" class="delete_team_button">Unselect</p>
  </div>
</template>

<script>
export default {
  props: {
    warehouses: Array,
    selectedWarehouse: Object,
    warehouseSelected: Function,
  },
  data() {
    return {
      localSelectedWarehouse: this.selectedWarehouse,
    };
  },
  created() {
    console.log("Received warehouses in DropdownComponent:", this.warehouses);
  },
  methods: {
    handleWarehouseSelected() {
      this.warehouseSelected(this.localSelectedWarehouse);
    },
    unselectWarehouse() {
      this.localSelectedWarehouse = "";
      this.warehouseSelected("");
    },
  },
};
</script>

<style scoped>
</style>
