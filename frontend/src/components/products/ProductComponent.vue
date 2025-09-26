<template>
  <div>
    <SearchAddComponent @search="handleSearch" @add-element-clicked="handleAddItem" searchPlaceholder="Item name"
                        addButtonLabel="Add item"/>

    <ProductAddComponent
        @add_item="add_item"
        @collapse_form="handleFormCollapse"
        @changes-saved="reloadProducts"
        :showFormValue="showForm"
    />

    <product-edit-component
        ref="editComponent"
        v-show="showEditPopup"
        @close-popup="closeEditPopup"
        @changes-saved="reloadProducts"
    />

    <div class="main " :style="mainStyles">
      <!--      <div class="vla flex justify-start justify-between space-x-24 mr-5 py-2">-->
      <div class="w-full p-5 middle-part justify-start justify-between space-x-8 gap-2 grid grid-cols-12  py-2">
        <div class=" font-bold col-span-1">Item ID</div>
        <div class="font-bold col-span-2">Name</div>
        <div class="font-bold col-span-2">Total Stock</div>
        <div class="font-bold col-span-1">Price</div>
        <div class="font-bold col-span-2">Total Value</div>
        <div class="font-bold col-span-2">Description</div>

      </div>

      <div v-for="item in filteredItems" :key="item.id" class="card">
        <ProductContainer :item="item" @delete-item="delete_item" :openEditComponent="openEditComponent"/>
      </div>

    </div>
  </div>
</template>

<script>
import SearchAddComponent from "@/components/SearchAddComponent.vue";
import ProductContainer from "@/components/products/ProductContainer.vue";
import confirmDialog from "@/models/confirmDialog";
import ProductAddComponent from "@/components/products/ProductAddComponent.vue";
import ProductEditComponent from "@/components/products/ProductEditComponent.vue";

export default {
  name: "ProductComponent",
  inject: ["inventoryService", "productsService"],
  components: {ProductEditComponent, SearchAddComponent, ProductContainer, ProductAddComponent},

  async created() {
    try {
      this.items = await this.productsService.findAll();
      this.filteredItems = this.items;
    } catch (error) {
      console.error("Error loading data", error);
    }
  },
  data() {
    return {
      items: [],
      filteredItems: [],
      totalValue: null,
      showForm: false,
      // searchTerm: "",
      showEditPopup: false,
    };
  },
  methods: {

    async reloadProducts(){
    this.items = await this.productsService.findAll();
    this.filteredItems = [...this.items];

  },


  async fetchInventoryData() {
    try {
      this.items = await this.inventoryService.findAll();
    } catch (error) {
      console.error("Fout bij ophalen van inventaris", error);
    }
  },

  openEditComponent(item) {
    this.showEditPopup = true;
    this.$refs.editComponent.$data.editedProduct = {...item};
  },

  openAddItemToWarehouse() {
    this.showAddItemToWarehouseForm = true;
  },

  closeAddItempopup() {
    this.showAddItemToWarehouseForm = false;
  },
  closeEditPopup() {
    this.showEditPopup = false;
  },

  async add_item(item) {
    await this.productsService.addItem(item);
    this.filteredItems = this.items;
  },

  async delete_item(item) {
    await this.productsService.findAll;
      await confirmDialog.showConfirmationDialog(item.name, async () => {
        try {
          await this.productsService.deleteById(item.id);
          this.filteredItems = this.filteredItems.filter((i) => i.id !== item.id);
        }catch (e) {
          console.log(e)
          confirmDialog.showWarningDialog("This product has orders and therefore can't be deleted")
        }
      });
  },


  handleSearch(searchTerm) {
    const lowerCaseSearchTerm = searchTerm.toLowerCase();

    this.filteredItems = this.items.filter((item) =>
        item.name.toLowerCase().includes(lowerCaseSearchTerm)
    );
    console.log("items in search:", this.filteredItems);
  },


  handleFormCollapse() {
    this.showForm = false;
  },
  handleAddItem() {
    this.showForm = true;
  },
}
,
computed: {
  mainStyles()
  {
    return {
      marginTop: "17%",
    };
  }
,
  currentItems()
  {
    return this.items.slice(this.startIndex, this.endIndex);
  }
,
}
,
}
;
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


label {
  margin-right: 10px;
}

</style>