<template>
  <div v-if="showForm" class="fixed inset-0 flex items-center justify-center bg-gray-500 bg-opacity-75">
    <div class="bg-white p-8 rounded shadow-md w-80">
      <span @click="collapseForm" class="absolute top-0 right-0 p-4 cursor-pointer">&times;</span>

      <label for="productName">Name Item:</label>
      <input v-model="item.name" type="text" id="productName" class="mb-4 p-2 border border-gray-300 rounded w-full">

<!--      <label for="stock">Stock:</label>-->
<!--      <input v-model="item.quantity" type="number" id="stock" class="mb-4 p-2 border border-gray-300 rounded w-full">-->

<!--      <label for="fullStock">Full Stock Aantal:</label>-->
<!--      <input v-model="item.fullStock" type="number" id="fullStock" class="mb-4 p-2 border border-gray-300 rounded w-full">-->

      <label for="price">Price:</label>
      <input v-model="item.price" type="number" id="price" class="mb-4 p-2 border border-gray-300 rounded w-full">

      <label for="description">Description:</label>
      <textarea v-model="item.description" id="description" class="mb-4 p-2 border border-gray-300 rounded w-full"></textarea>

<!--      <label for="warehouse">Warehouse:</label>-->
<!--&lt;!&ndash;      <DropdownComponent :warehouses="warehouses" :selected-warehouse="item.warehouse" :warehouse-selected="handleWarehouseSelected" />&ndash;&gt;-->
<!--      <DropdownComponent :warehouses="warehouses" :selected-warehouse="item.warehouse"-->
<!--                         @warehouse-selected="handleWarehouseSelected"-->
<!--                        />-->


      <div class="flex justify-end space-x-2 mt-4">
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
// import DropdownComponent from "@/components/inventory/DropdownComponent.vue";

export default {
  name: "InventoryAddComponent",
  // components:{DropdownComponent},
  props: {
    showFormValue: Boolean,
    warehouses: Array
  },
  created() {
    console.log("Received warehouses in InventoryAddComponent:", this.warehouses);
    console.log("Inventory Service in InventoryAddComponent:", this.inventoryService);
  },


  emits: ["add_item", "collapse_form"],
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
    async handleWarehouseSelected(selectedWarehouse) {
      this.item.warehouse = selectedWarehouse;
      if (selectedWarehouse) {
        this.filteredItems = await this.inventoryService.findByWarehouse(selectedWarehouse.id);
      } else {
        this.filteredItems = [];
      }
    },
    unselectWarehouse() {
      this.item.warehouse = null;
      this.filteredItems = this.items;
    },

    async add_item() {
      const itemData = { ...this.item };

      try {
        this.$emit("add_item", itemData);
        console.log("Item toegevoegd ", itemData);
      } catch (error) {
        console.error("Fout bij het toevoegen van het item:", error.message);
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
