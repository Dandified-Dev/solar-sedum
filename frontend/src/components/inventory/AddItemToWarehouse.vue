<template>
  <div class="fixed inset-0 flex items-center justify-center bg-gray-500 bg-opacity-75">
    <div class="bg-white p-8 rounded shadow-md w-80">
      <span @click="collapseForm" class="absolute top-0 right-0 p-4 cursor-pointer">&times;</span>

      <div v-if="selectedWarehouse" class="mb-4">
        Selected Warehouse: {{ getWarehouseName(selectedWarehouse) }}
      </div>

      <label for="productName">Name:</label>
      <select v-model="selectedMissingProduct" id="missingProducts" class="mb-4 p-2 border border-gray-300 rounded w-full">
        <option class="text-black" v-for="product in missingProducts" :key="product.id" :value="product">
          {{ product.name }}
        </option>
      </select>


      <label for="stock">Stock:</label>
      <input v-model="item.quantity" type="number" id="stock" class="mb-4 p-2 border border-gray-300 rounded w-full">

      <label for="fullStock">Full Stock Amount:</label>
      <input v-model="item.fullStock" type="number" id="fullStock" class="mb-4 p-2 border border-gray-300 rounded w-full">

      <label for="lowStockLimit">Low Stock Limit:</label>
      <input type="number" v-model="item.lowStockLimit" class="mb-4 p-2 border border-gray-300 rounded w-full"/>

      <div class="error-message text-red-500">{{ errorMessage }}</div>

      <div class="flex justify-center space-x-6 mt-4">
        <button @click="closePopup" class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded">
          Cancel
        </button>
        <button @click="saveChanges" class="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
          Add item
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "AddItemToWarehouse",
  inject: ["productsService", "inventoryService"],
  async created() {
    await this.getProductsByWarehouse(this.selectedWarehouse.id)

  },
  props: {
    showFormProp: Boolean,
    selectedWarehouse: Object,
  },
  emits: ["add_item", "close-popup", "update:showFormProp", "inventory-changes-saved"],
  data() {
    return {
      item: {
        name: "",
        quantity: 0,
        fullStock: 0,
        warehouse: null,
        lowStockLimit: 0,
      },
      errorMessage: "",
      selectedMissingProduct: null,
      missingProducts: [],
      originalItem: {},
    };
  },
  methods: {
    async saveChanges() {
      try {

        if (!/^-?\d+$/.test(this.item.quantity) || !/^-?\d+$/.test(this.item.fullStock) || !/^-?\d+$/.test(this.item.lowStockLimit)) {
          throw new Error('Input values should only contain numbers');
        }

        if (!this.selectedWarehouse || !this.selectedMissingProduct || !this.item.quantity || !this.item.fullStock || !this.item.lowStockLimit) {
          throw new Error('Fill in all fields');
        }

        if (this.item.fullStock <= 0 || this.item.lowStockLimit <= 0) {
          throw new Error('Full stock amount and Low stock limit should be positive numbers');
        }


        const newItem = {
          quantity: this.item.quantity,
          max_quantity: this.item.fullStock,
          product_id: this.selectedMissingProduct.id,
          warehouse_id: this.selectedWarehouse.id,
          low_stock_limit: this.item.lowStockLimit,
        };

        await this.inventoryService.addItemWithWarehouse(newItem, this.selectedMissingProduct.id, this.selectedWarehouse.id);

        this.$emit("close-popup");
        this.$emit("inventory-changes-saved");

      } catch (error) {
        this.errorMessage = error.message; // Toon foutmelding in de error-message
        console.error('Error saving changes:', error.message);
      }
    },


    async getProductsByWarehouse(id){
      const products = await this.productsService.findAll();
      const productsByWarehouse = await this.productsService.getByWarehouseId(id);

      this.missingProducts = products.filter(product =>
          !productsByWarehouse.some(productInWarehouse => productInWarehouse.id === product.id)
      );
    },
    getWarehouseName(warehouse) {
      return warehouse ? warehouse.name : "Onbekend magazijn";
    },

    closePopup() {
      this.$emit("close-popup");
    },
    handleWarehouseSelected(selectedWarehouse) {
      this.item.warehouse = selectedWarehouse;
    },
    addItem() {
      const itemData = {...this.item};
      this.$emit("add_item", itemData);

      this.item = {
        name: "",
        quantity: 0,
        fullStock: 0,
        warehouse: null,
      };

      this.originalItem = {};
      this.$emit("update:showFormProp", false);
    },
    cancelChanges() {
      if (this.hasUnsavedChanges()) {
        const confirmDiscard = window.confirm("Are you sure you want to discard unsaved changes?");
        if (!confirmDiscard) {
          return;
        }
      }

      this.item = {...this.originalItem};
      this.originalItem = {};
      this.$emit("update:showFormProp", false);
    },
    collapseForm() {
      if (this.hasUnsavedChanges()) {
        const confirmDiscard = window.confirm("Are you sure you want to discard unsaved changes?");
        if (!confirmDiscard) {
          return;
        }
      }
      this.item = {
        name: "",
        quantity: 0,
        fullStock: 0,
        warehouse: null,
      };
      this.originalItem = {};
      this.$emit("update:showFormProp", false);
    },
    hasUnsavedChanges() {
      return JSON.stringify(this.item) !== JSON.stringify(this.originalItem);
    },
  },
  watch: {
    showFormProp(newValue) {
      if (newValue) {
        this.originalItem = {...this.item};
      }
    },
  },
};
</script>

<style scoped>
</style>
