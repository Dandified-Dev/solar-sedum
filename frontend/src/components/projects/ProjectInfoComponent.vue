<template>
  <div>
    <button @click="handleForm" class="text-indigo-500 hover:underline">Details</button>
  </div>
</template>

<script>
import Swal from "sweetalert2";
import confirmDialog from "@/models/confirmDialog";

export default {
  props: ['project'],
  inject: ['projectsService', "orderService", "inventoryService", "teamsService", "productsService"],
  emits: ["deleteProject", "showUpdateForm"],
  async created() {
    this.teams = await this.teamsService.findAll();
    if (this.project.id){
      this.orders = await this.orderService.findByProjectId(this.project.id);
    }
    this.products = await this.productsService.findAll();
  },
  data() {
    return {
      teams: [],
      orders: [],
      products: [],
      showUpdateForm: true,
    };
  },
  methods: {
    async handleForm() {
      // Construct the HTML content for project description
      let projectDescriptionHtml = `<h2>Project Description</h2><p>${this.project.description}</p>`;

      // Use a loop to construct the HTML content for each order with product names
      let ordersHtml = await Promise.all(
          this.orders.map(async (order) => {
            const product = await this.getProductById(order.inventory.productId);
            return `<p>${product.name}: ${order.quantity}</p>`;
          })
      );

      // Add a white line between description and orders
      let whiteLineHtml = '<div style="margin-top: 10px; border-top: 1px solid #fff;"></div>';

      // Combine project description, white line, and order details in SweetAlert
      Swal.fire({
        title: `${this.project.name}`,
        html: `${projectDescriptionHtml}${whiteLineHtml}<h2>Products needed</h2>${ordersHtml.join("")}`,
        showDenyButton: true,
        confirmButtonText: "Delete",
        denyButtonText: `Update`,
        customClass: {
          confirmButton: 'deleteButton',
          denyButton: 'updateButton' // Add your custom class name here
        }
      }).then(async (result) => {
        if (result.isConfirmed) {
          await confirmDialog.showConfirmationDialog(this.project.name, async () => {
            this.$emit("deleteProject", this.project);
          })
        } else if (result.isDenied) {
          console.log("Update button clicked");
          this.showUpdateForm = true;
        this.$emit("showUpdateForm", this.project)}
      });
    },
    async getProductById(productId) {
      // Use your productsService or any other appropriate service to find the product by ID
      return await this.productsService.findById(productId);
    },
  },
};
</script>

<style>
.deleteButton{
  border-radius: 10px !important;
  background-color: red !important;
  box-shadow: 0 0 0 0 rgba(0, 0, 0, 0) !important;
}
.updateButton{
  border-radius: 10px !important;
  background-color: white !important;
  border : 1px solid #02A9F5 !important;
  color: #02A9F5 !important;
  box-shadow: 0 0 0 0 rgba(0, 0, 0, 0) !important;
}

.updateButton:hover {
  border-radius: 10px !important;
  background-color: #02A9F5 !important;
  color: white !important;
}
</style>
