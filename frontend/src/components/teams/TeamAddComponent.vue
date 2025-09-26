<template>
  <div></div>
</template>
<script>
import Swal from "sweetalert2";

export default {
  inject: ["warehousesService"],
  name: "TeamAddComponent",
  props: ["showFormValue"],
  emits: ["add_team", "collapse_form"],
  async created() {
    this.warehouses = await this.warehousesService.findAll()
  },
  data() {
    return {
      warehouses: [],
      selectedWarehouseId: null,
      team: {
        name: "",
      },
    };
  },
  methods: {
    async handleForm() {
      const { value: formValues } = await Swal.fire({
        html: `
        <label class="flex justify-start mt-4 text-black">Name: </label>
          <input id="swal-input1" class="mb-4 p-2 border border-gray-300 rounded w-full">

          <label class="flex justify-start text-black mt-1">Warehouse: </label>
          <select id="swal-input4" class="flex justify-start p-2 border-solid w-full border border-gray-300 mt-2 ">
            <option value="" disabled selected>Select Warehouse</option>
            ${this.warehouses.map((warehouse) => `<option value="${warehouse.id}">${warehouse.name}</option>`).join("")}
          </select>
        `,
        showCancelButton: true,
        cancelButtonText: "Cancel",
        cancelButtonColor: "#ef4444",
        confirmButtonText: "Add Team",
        confirmButtonColor: "#22c55e",
        reverseButtons: true,
        width: 320,

        preConfirm: async () => {
          const name = document.getElementById("swal-input1").value.trim();

          const selectedWarehouseId = document.getElementById("swal-input4").value;

          if (!name && !selectedWarehouseId) {
            Swal.showValidationMessage(`Name and warehouse are required`);
            return false;
          }

          this.team.name = name;

          this.selectedWarehouseId = parseInt(selectedWarehouseId);
        },
      });

      if (formValues) {
        this.$emit("add_team", this.team, this.selectedWarehouseId);
      }
      this.$emit("collapse_form");
    },
  },
  computed: {
    teamMembers: {
      get() {
        return this.team.users.join(", ");
      },
      set(value) {
        this.team.users = value.split(", ");
      },
    },
  },
  watch: {
    showFormValue(newValue) {
      if (newValue == true){
        this.handleForm()
      }
    },
  },
};
</script>

<style scoped>
</style>
