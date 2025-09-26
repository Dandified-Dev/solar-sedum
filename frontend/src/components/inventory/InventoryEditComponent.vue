<template>
  <div class="fixed inset-0 flex items-center justify-center bg-gray-500 bg-opacity-75">
    <div class="bg-white p-8 rounded shadow-md w-80">
      <span @click="closePopup" class="absolute top-0 right-0 p-4 cursor-pointer">&times;</span>

      <div class="mb-4 rounded w-full"> Name: {{editedProduct.name}}</div>

      <label for="stock">Stock:</label>
      <input v-model="editedProduct.quantity" type="number" id="stock" class="mb-4 p-2 border border-gray-300 rounded w-full">

      <label for="fullStock">Full Stock Amount:</label>
      <input v-model="editedProduct.maxQuantity" type="number" id="fullStock" class="mb-4 p-2 border border-gray-300 rounded w-full">

      <label for="lowStockLimit">Low Stock Limit:</label>
      <input v-model="editedProduct.lowStockLimit" type="number" id="lowStockLimit" class="mb-4 p-2 border border-gray-300 rounded w-full">

      <div class="mb-4 rounded w-full"> Price: {{editedProduct.price}}</div>

      <div class="mb-4 rounded w-full"> Description: {{editedProduct.description}}</div>


      <div class="error-message text-red-500">{{ errorMessage }}</div>


      <div class="flex justify-center items-center space-x-6 mt-4">
        <button @click="closePopup" class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded">
          Cancel
        </button>
        <button @click="saveInventoryChanges" class="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
          Save
        </button>
      </div>
    </div>
  </div>
</template>
<script>

export default {
  name: "InventoryEditComponent",
  inject: ["inventoryService"],
  props: {
    item: Object,
  },

  data() {
    return {
      editedProduct: {
        id: 0,
        name: "",
        quantity: 0,
        maxQuantity: 0,
        price: 0,
        description: "",
        lowStockLimit: 0,
      },
      errorMessage: "",
    };
  },



  methods: {
    async setEditedProduct(inventoryId) {
      try {
        const inventoryItem = await this.inventoryService.findById(inventoryId);
        console.log(inventoryItem);
        this.editedProduct = {
          id: inventoryItem.id,
          name: this.item.name,
          quantity: inventoryItem.quantity,
          maxQuantity: inventoryItem.max_quantity,
          price: this.item.price,
          description: this.item.description,
          lowStockLimit: inventoryItem.low_stock_limit,
        };
      } catch (error) {
        console.error("Error fetching inventoryItem:", error);
      }
    },

    closePopup() {
      this.editedProduct = "";
      this.$emit("close-popup");
    },

    async saveInventoryChanges() {
      try {
        if (!/^-?\d+$/.test(this.editedProduct.quantity) || !/^-?\d+$/.test(this.editedProduct.maxQuantity) || !/^-?\d+$/.test(this.editedProduct.lowStockLimit)) {
          throw new Error('Input values should only contain numbers');
        }

        if (this.editedProduct.maxQuantity <= 0 || this.item.lowStockLimit <= 0) {
          throw new Error('Full stock amount and Low stock limit should be positive numbers');
        }

        if (!this.editedProduct.quantity || !this.editedProduct.maxQuantity || !this.editedProduct.lowStockLimit) {
          throw new Error('Fill in all fields');
        }

        const updatedData = {
          quantity: this.editedProduct.quantity,
          maxQuantity: this.editedProduct.maxQuantity,
          lowStockLimit: this.editedProduct.lowStockLimit
        };


        await this.inventoryService.updateItem(this.editedProduct.id,
            updatedData.quantity, updatedData.maxQuantity, updatedData.lowStockLimit);

        this.closePopup();
        this.$emit("inventory-changes-saved");


      } catch (error){
        this.errorMessage = error.message;
        console.error("Error saving changes:", error);
      }
    }

  }
}
</script>

<style scoped>

</style>
