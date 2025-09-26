<template>
  <div class="fixed inset-0 flex items-center justify-center bg-gray-500 bg-opacity-75">
    <div class="bg-white p-8 rounded shadow-md w-80">
      <span @click="closePopup" class="absolute top-0 right-0 p-4 cursor-pointer">&times;</span>

      <label for="productName">Name Item:</label>
      <input v-model="editedProduct.name" type="text" id="productName" class="mb-4 p-2 border border-gray-300 rounded w-full">

      <label for="price">Price:</label>
      <input v-model="editedProduct.price" type="number" id="price" class="mb-4 p-2 border border-gray-300 rounded w-full">

      <label for="description">Description:</label>
      <textarea v-model="editedProduct.description" id="description" class="mb-4 p-2 border border-gray-300 rounded w-full"></textarea>

      <div class="flex justify-center space-x-6 mt-4">
        <button @click="closePopup" class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded">
          Cancel
        </button>
        <button @click="saveChanges" class="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
          Save
        </button>
      </div>
    </div>
  </div>
</template>

<script>


export default {
  name: "ProductEditComponent",
  inject: ["productsService", "inventoryService"],
  emits: ["close-popup","collapse_form", "changes-saved"],

  data() {
    return {
      editedProduct: {
        name: "",
        price: 0,
        description: "",
      },
    };
  },
  methods: {

    async saveChanges() {
      try {
        if (!this.editedProduct.name || isNaN(this.editedProduct.price)) {
          console.error("Ongeldige invoer. Zorg ervoor dat alle velden correct zijn ingevuld.");
          return;
        }

        const updatedData = {
          name: this.editedProduct.name,
          price: this.editedProduct.price,
          description: this.editedProduct.description
        };

        await this.productsService.updateProduct(this.editedProduct.id, updatedData.name, updatedData.price,
            updatedData.description);
        console.log("Item aangepast ", this.editedProduct);

        this.editedProduct = {
          name: "",
          price: 0,
          description: "",
        };

        this.$emit("close-popup");
        this.$emit("changes-saved");

      } catch (error) {
        console.error("Fout bij het aanpassen van het item:", error.message);
      }
    },



    closePopup() {
      this.$emit("close-popup");
    },

  }
}
</script>

<style scoped>

</style>
