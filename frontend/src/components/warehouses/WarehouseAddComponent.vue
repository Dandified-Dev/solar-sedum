<template>
  <div></div>
</template>

<script>
import Swal from "sweetalert2";

export default {
  inject: ["warehousesService"],
  name: "WarehouseAddComponent",
  props: ["showFormValue"],
  emits: ["add_warehouse", "collapse_form"],
  async created() {
    this.warehouses = await this.warehousesService.findAll()
  },
  data() {
    return {
      warehouse: {
        name: "",
        location: "",
      },
    };
  },
  methods: {
    async handleForm() {
      const {value: formValues} = await Swal.fire({
        html: `
          <label class="flex justify-start mt-4 text-black">Name: </label>
          <input id="swal-input1" class="mb-4 p-2 border border-gray-300 rounded w-full " >

          <label class="flex justify-start text-black">Location: </label>
          <input id="swal-input2" class="mb-4 p-2 border border-gray-300 rounded w-full">
        `,
        showCancelButton: true,
        cancelButtonText: "Cancel",
        cancelButtonColor: "#ef4444",
        confirmButtonText: "Add Warehouse",
        confirmButtonColor: "#22c55e",
        reverseButtons: true,
        width: 320,

        preConfirm: async () => {
          const name = document.getElementById("swal-input1").value.trim();
          const location = document.getElementById("swal-input2").value.trim();

          if (!name && !location) {
            Swal.showValidationMessage(`Name and location are required`);
            return false;
          }

          this.warehouse.name = name;
          this.warehouse.location = location;
        },
      });
      if (formValues) {
        const warehouseData = {...this.warehouse};
        this.$emit("add_warehouse", warehouseData);
      }
      this.$emit("collapse_form");
    },
  },
  watch: {
    showFormValue(newValue) {
      if(newValue === true) {
        this.handleForm()
      }
    },
  },
};
</script>

<style scoped>

</style>