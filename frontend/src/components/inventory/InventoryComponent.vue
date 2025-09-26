<template>
  <div>
    <SearchAddComponent
        @search="handleSearch"
        @add-element-clicked="openAddItemToWarehouse"
        searchPlaceholder="Item name"
        addButtonLabel="Add item to warehouse"
    />

    <AddItemToWarehouse
        v-if="showAddItemToWarehouseForm"
        @close-popup="closeAddItempopup"
        @inventory-changes-saved="reloadItems"
        :selectedWarehouse="selectedWarehouse"
    />

    <inventory-edit-component
        ref="editComponent"
        v-show="showEditPopup"
        @close-popup="closeEditPopup"
        @inventory-changes-saved="reloadItems"
        :item="editedProduct"
    ></inventory-edit-component>

    <div class="main " :style="mainStyles">

      <div class="dropdown">
        <label :class="{ 'text-red-500': isDropdownTextRed }" for="warehouseDropdown">Select a warehouse:</label>
        <DropdownComponent
            :warehouses="warehouses"
            :selected-warehouse="selectedWarehouse"
            :warehouse-selected="handleWarehouseSelected"
            @unselect-warehouse="unselect_warehouse"
        />
      </div>

<!--      <div class="vla flex justify-start justify-between space-x-24 mr-5 py-2">-->
      <div class="w-full p-5 middle-part justify-start justify-between space-x-8  gap-2 grid grid-cols-12  py-2">
        <div class=" font-bold col-span-1">Item ID</div>
        <div class="font-bold col-span-2">Name</div>
<!--        <div class="font-bold col-span-2">Status</div>-->
        <div class="font-bold col-span-2" v-if="selectedWarehouse">Status</div>
        <div class="font-bold col-span-2" v-else>Total Value</div>
        <div class="font-bold col-span-2">Stock</div>
        <div class="font-bold col-span-2">Description</div>
        <div class="font-bold col-span-1">Price</div>

      </div>


      <div v-for="item in filteredItems" :key="item.id" class="card">
        <!--        <img :src="getImagePath(item.name)" alt="Inventory Item Image" />-->
<!--        <InventoryContainer :item="item" @delete-item="delete_item" :openEditComponent="openEditComponent" />-->
        <InventoryContainer :item="item"
                            :selectedWarehouse="selectedWarehouse"
                            :openEditComponent="openEditComponent"
                            @delete-item="delete_item"
        />


      </div>

      <!--      <div v-for="item in currentItems" :key="item.id" class="card">-->
      <!--        <InventoryContainer :item="item" @delete-item="delete_item" />-->
      <!--      </div>-->

    </div>
  </div>
</template>


<script>
import SearchAddComponent from "@/components/SearchAddComponent.vue";
import InventoryContainer from "@/components/inventory/InventoryContainer.vue";
import confirmDialog from "@/models/confirmDialog"
import InventoryEditComponent from "@/components/inventory/InventoryEditComponent.vue";
import DropdownComponent from "@/components/inventory/DropdownComponent.vue";
import AddItemToWarehouse from "@/components/inventory/AddItemToWarehouse.vue";

export default {
  name: "InventoryComponent",
  inject: ["inventoryService", "warehousesService", "productsService"],
  components: {DropdownComponent, InventoryEditComponent, SearchAddComponent, InventoryContainer,
  AddItemToWarehouse},

  async created() {
    try {
      this.warehouses = await this.warehousesService.findAll();
      this.items = await this.productsService.findAll();
      this.filteredItems = this.handleWarehouseSelected();
      this.sortItemsById();

    } catch (error) {
      console.error("Error loading data", error);
    }
  },
  data() {
    return {
      items: [],
      warehouses: [],
      filteredItems: [],
      totalValue: null,
      showForm: false,
      // searchTerm: "",
      selectedWarehouse: null,
      showEditPopup: false,
      showAddItemToWarehouseForm: false,
      isDropdownTextRed: false,
      originalItems: [],
      editedProduct: null,

    };
  },
  methods: {

    sortItemsById() {
      if (Array.isArray(this.filteredItems)) {
        this.filteredItems.sort((a, b) => a.id - b.id);
      }
    },

    reloadItems() {
      this.handleWarehouseSelected(this.selectedWarehouse);
    },

    // async handleWarehouseSelected(selectedWarehouse) {
    //   this.selectedWarehouse = selectedWarehouse;
    //
    //   if (selectedWarehouse) {
    //     this.filteredItems = await this.inventoryService.findByWarehouse(selectedWarehouse.id);
    //   } else {
    //     this.filteredItems = this.items;
    //   }
    // },


    async handleWarehouseSelected(selectedWarehouse) {
      this.selectedWarehouse = selectedWarehouse;
      this.isDropdownTextRed = false;

      if (selectedWarehouse) {
        if(selectedWarehouse.inventory?.length === 0) {
          this.filteredItems = [];
        } else {
          const inventoryItems = await this.inventoryService.findByWarehouse(selectedWarehouse.id);
          this.filteredItems = inventoryItems.map((inventoryItem) => {

            const correspondingProduct = this.items.find((product) => product.id === inventoryItem.productId);

            return {
              ...correspondingProduct,
              inventoryId: inventoryItem.id,
              productName: correspondingProduct.name,
              productDescription: correspondingProduct.description,
              productPrice: correspondingProduct.price,

              inventoryStatus: inventoryItem.status,
              inventoryQuantity: inventoryItem.quantity,
              // inventoryMaxQuantity: " / " +inventoryItem.max_quantity,
              inventoryMaxQuantity: inventoryItem.max_quantity,

              correspondingProduct,
            };

          });
          this.originalItems = [...this.filteredItems];
          this.sortItemsById();
        }

      } else if (!selectedWarehouse){
        const allProducts = await this.productsService.findAll();
        this.filteredItems = allProducts.map((product) => ({
          id: product.id,
          productName: product.name,
          productPrice: product.price,

          totalValue: Number((product.price * product.quantity).toFixed(2)),
          inventoryQuantity: product.quantity,
          productDescription: product.description,
        }));
        this.originalItems = [...this.filteredItems];
        this.sortItemsById();

      } else {
        this.originalItems = [...this.items];
        this.filteredItems = this.items;

      }
    },


    async unselect_warehouse() {
      this.filteredItems = this.items;
    },
    // async fetchAggregatedInventory() {
    //   try {
    //     this.items = await this.inventoryService.aggregatedInventory();
    //     this.filteredItems = this.items;
    //   } catch (error) {
    //     console.error("Fout bij ophalen van geaggregeerde inventaris", error);
    //   }
    // },
    async fetchInventoryData() {
      try {
        this.items = await this.inventoryService.findAll();
      } catch (error) {
        console.error("Fout bij ophalen van inventaris", error);
      }
    },

    async fetchWarehouseData() {
      try {
        this.warehouses = await this.warehousesService.findAll();
      } catch (error) {
        console.error("Fout bij ophalen van magazijngegevens", error);
      }
    },

    // getImagePath(itemName) {
    //   const formattedName = itemName.replace(/\s/g, '_');
    //   return require(`@/assets/inventory/${formattedName}.png`);
    // },

    openEditComponent(item) {
      console.log(item.inventoryId)
      this.showEditPopup = true;
      this.editedProduct = { ...item };
      this.$refs.editComponent.setEditedProduct(item.inventoryId);

    },

    openAddItemToWarehouse() {
      if (this.selectedWarehouse) {
        this.showAddItemToWarehouseForm = true;
        this.isDropdownTextRed = false;
      } else {
        this.isDropdownTextRed = true;
      }
    },

    closeAddItempopup() {
      this.showAddItemToWarehouseForm = false;
    },
    closeEditPopup() {
      this.showEditPopup = false;
    },

    async add_item(item) {
      await this.inventoryService.addItem(item);
      this.filteredItems = this.items;
      this.filteredItems = this.handleWarehouseSelected();
    },

    async showConfirmationDialog(message) {
      return new Promise((resolve) => {
        const userConfirmed = window.alert(message);
        resolve(userConfirmed);
      });
    },

    async delete_item(item) {
      try {
        await confirmDialog.showConfirmationDialog(item.name, async () => {
          console.log(item.quantity, item.inventoryQuantity);
          const updatedQuantity = item.quantity - item.inventoryQuantity;

          try {
            await this.inventoryService.deleteItem(item.inventoryId);
            await this.productsService.updateQuantity(item.id, updatedQuantity);

            this.filteredItems = this.filteredItems.filter((i) => i.id !== item.id);
          } catch (error) {
            console.error("Fout bij het verwijderen van het item:", error);

            await this.showConfirmationDialog("Item can not be deleted because of pending order");
          }
        });
      } catch (error) {
        console.error("Fout bij het verwijderen van het item:", error);
      }
    },



    handleSearch(searchTerm) {
      const lowerCaseSearchTerm = searchTerm.toLowerCase();

      if (lowerCaseSearchTerm === "") {
        this.filteredItems = [...this.originalItems];
      } else {
        this.filteredItems = this.originalItems.filter((item) =>
            item.productName.toLowerCase().includes(lowerCaseSearchTerm)
        );
      }
    },




    handleFormCollapse() {
      this.showForm = false;
    },
    handleAddItem() {
      this.showForm = true;
    },
  },
  computed: {
    isWarehouseSelected() {
      return this.selectedWarehouse !== null;
    },

    mainStyles() {
      return {
        marginTop: this.showForm ? "28%" : "17%",
        marginBottom: "10%"
      };
    },
  },
};
</script>

<style scoped>
.main {
  position: absolute;
  z-index: -1;
  margin-left: 21%;
  background-color: white;
  width: 69%;
  border-radius: 12px;
  flex-direction: column;
  display: flex;
  align-items: center;
  min-height: 30rem;
  /*text-align: center;*/

}

.card {
  position: relative;
  background-color: #ffffff;
  width: 100%;
  border-radius: 4vh;
}



.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
}

.page-button {
  padding: 8px 16px;
  margin: 8px;
  background-color: #007BFF;
  color: #ffffff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.middle-part {
  margin: 10px;
  padding: 10px;
}

.page-button:disabled {
  background-color: #d1d1d1;
  cursor: not-allowed;
}

.page-info {
  margin: 0 16px;
}
.dropdown {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

label {
  margin-right: 10px;
}

.warehouse-select {
  display: flex;
  align-items: center;
}

.delete_team_button {
  cursor: pointer;
  margin-left: 10px;
}
</style>