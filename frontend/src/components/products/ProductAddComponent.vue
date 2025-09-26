<template>
  <div v-if="showForm" class="fixed inset-0 flex items-center justify-center bg-gray-500 bg-opacity-75">
    <div class="bg-white p-8 rounded shadow-md w-80">
      <span @click="collapseForm" class="absolute top-0 right-0 p-4 cursor-pointer">&times;</span>

      <label for="productName">Name Item:</label>
      <input v-model="item.name" type="text" id="productName" class="mb-4 p-2 border border-gray-300 rounded w-full">


      <label for="price">Price:</label>
      <input v-model="item.price" type="number" id="price" class="mb-4 p-2 border border-gray-300 rounded w-full">

      <label for="description">Description:</label>
      <textarea v-model="item.description" id="description" class="mb-4 p-2 border border-gray-300 rounded w-full"></textarea>


      <div class="flex justify-center space-x-6 mt-4">
        <button @click="cancelChanges" class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded">
          Cancel
        </button>
        <button @click="add_item" class="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
          Add Item
        </button>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: "ProductAddComponent",
  inject: ["productsService", "inventoryService"],

  props: {
    showFormValue: Boolean,
    warehouses: Array
  },
  created() {
    console.log("Received warehouses in InventoryAddComponent:", this.warehouses);
    console.log("Inventory Service in InventoryAddComponent:", this.inventoryService);
  },


  emits: ["add_item", "collapse_form", "changes-saved"],
  data() {
    return {
      showForm: false,
      item: {
        name: "",
        quantity: 0,
        fullStock: 0,
        price: 0,
        description: "",
        warehouse: null,

      },
      originalItem: {},
      filteredItems: [],

    };
  },
  methods: {
    async add_item() {
      const itemData = { ...this.item };

      try {
        await this.productsService.add(itemData)
        console.log("Item toegevoegd ", itemData);
      } catch (error) {
        console.error("Fout bij het toevoegen van het item:", error.message);
      }

      this.item = {
        name: "",
        price: 0,
        description: "",
      };

      this.originalItem = {};
      this.showForm = false;
      this.$emit("changes-saved");

    },


    cancelChanges() {
      if (this.hasUnsavedChanges()) {
        const confirmDiscard = window.confirm("Weet je zeker dat je onopgeslagen wijzigingen wilt negeren?");
        if (!confirmDiscard) {
          return;
        }
      }

      this.item = { ...this.originalItem };
      this.originalItem = {};
      this.showForm = false;
      this.$emit("collapse_form");
    },
    collapseForm() {
      if (this.hasUnsavedChanges()) {
        const confirmDiscard = window.confirm("Weet je zeker dat je onopgeslagen wijzigingen wilt negeren?");
        if (!confirmDiscard) {
          return;
        }
      }

      this.item = {
        name: "",
        quantity: 0,
        fullStock: 0,
        price: 0,
        description: "",
        warehouse: "",
      };
      this.originalItem = {};
      this.showForm = false;
      this.$emit("collapse_form");
    },
    hasUnsavedChanges() {
      return JSON.stringify(this.item) !== JSON.stringify(this.originalItem);
    },
  },
  watch: {
    showFormValue(newValue) {
      this.showForm = newValue;

      if (newValue) {
        this.originalItem = { ...this.item };
      }
    },
  },
};
</script>

<style scoped>
</style>
