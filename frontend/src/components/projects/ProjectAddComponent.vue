<template>
  <div class="fixed inset-0 flex items-center justify-center bg-gray-500 bg-opacity-75 "
       v-if="showForm">
    <div class="project-form bg-white my-2 h-fit rounded-xl text-center p-8 swal2-show ">

      <div class="w-40 justify-center contents">
        <p class="text flex justify-start mt-4 text-black">Project name:</p>
        <input type="text" v-model="project.name" class="w-full swal2-input mb-4 p-2 flex justify-start"/>
      </div>

      <div class="flex flex-col justify-between">

        <div>
          <p class="text flex justify-start text-black">Location:</p>
          <input type="text" v-model="project.location" class="w-full swal2-input mb-4 p-2 flex justify-start"/>
        </div>

        <div>
          <div class="flex flex-col">
            <p class="text flex justify-start">Start date:</p>
            <input type="date" v-model="project.startDate" class="justify-start mb-4 p-2 swal2-input"></div>
          <div class="flex flex-col">
            <p class="text flex justify-start">End date:</p>
            <input type="date" v-model="project.dueAt" class=" justify-start mb-4 p-2 swal2-input"></div>
        </div>

        <div class="flex flex-col justify-start">
          <p class="text flex  justify-start text-black">Teams:</p>
          <select name="" id="" v-model="selectedTeamId" class="swal2-input mb-4 p-2">
            <option value="" disabled selected>Select Team</option>
            <option v-for="team in teams" :key="team.id" :value="team.id">{{ team.name }}</option>
          </select>
        </div>

        <div v-if="selectedTeamId != null">
          <p class="text">Items:</p>
          <select name="" id="" v-model="selectedInventoryId" class="swal2-input">
            <option value="" disabled selected>Select Product</option>
            <option v-for="inventory in inventories" :key="inventory.productId" :value="inventory.id">{{
                this.getProductById(inventory.productId)
              }}
            </option>
          </select>
          <button @click="addMaterial(selectedInventoryId, this.quantity)">
            <img class="h-5" src="../../assets/addIcon.png" alt="">
          </button>
          <div v-for="item in orders" :key="item.id" class="py-[0.75em] text-xl m-[1em]">
            <div class="flex justify-center">
              <img class="h-5 my-auto mr-2" @click="removeMaterial(item)" src="@/assets/red_x_circle.png" alt="deleteButton">
              <p>{{ item.product ? item.product : 'Unknown product' }}:</p>
              <input class="ml-2 w-10" type="number" v-model="item.quantity" @keyup="validateQuantity">
            </div>
          </div>
        </div>

        <div v-if="selectedInventoryId != null">
          <div>
            <p class="text">Quantity:</p>
            <div class="flex">
              <img class="h-5 my-auto" @click="closeQuantity" src="@/assets/red_x_circle.png" alt="deleteButton">
              <input type="number" v-model="this.quantity" class="swal2-input" max="maxQuantity" @keyup="validateQuantity"/>
            </div>
          </div>
        </div>

        <div>
          <p class="text flex justify-start text-black ">Description:</p>
          <input type="text" v-model="project.description" class="swal2-input flex justify-start w-full mb-4 p-2"/>
        </div>
      </div>
      <div v-if="this.validationError === true" class="swal2-validation-message" id="swal2-validation-message" style="display: flex;">
        <div class="text-[#666] flex bg-[#f0f0f0] p-[0.625em] font-light justify-center w-full">
          <p class="validateCharacter">!</p>
          <p>Please fill in all the fields</p>
        </div>
      </div>
      <div v-if="this.validationDateError === true" class="swal2-validation-message" id="swal2-validation-message" style="display: flex;">
        <div class="text-[#666] flex bg-[#f0f0f0] p-[0.625em] font-light justify-center w-full">
          <p class="validateCharacter">!</p>
          <p>End date can't be earlier than Start date</p>
        </div>
      </div>
      <div class="flex justify-center space-x-6 mt-4 mb-4">
        <button class="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded" @click="collapseForm">
          Cancel
        </button>
        <button @click="add_project" class="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded">
          Add Project
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ProjectAddComponent",
  inject: ['inventoryService', 'teamsService', "productsService"],
  props: ["showFormValue"],
  emits: ["add_project", "collapse_form"],
  async created() {
    this.teams = await this.teamsService.findAll();
    this.products = await this.productsService.findAll();
  },
  data() {
    return {
      showForm: false,
      inventories: [],
      computedInventories:[],
      teams: [],
      selectedTeamId: null,
      selectedInventoryId: null,
      maxQuantity: null,
      selectedInventoryItems: [],
      products: [],
      selectedInventoryQuantity: null,
      quantity: null,
      validationError: false,
      validationDateError: false,
      project: {
        name: "",
        location: "",
        description: "",
        status: "UNSTARTED",
        startDate: null,
        dueAt: null
      },
      orders: [],
      numMaterials: 1, // New data property to track the number of members
    };
  },
  methods: {
    getProductById(productId) {
      let product = this.products.find(product => product.id === productId);
     if (product === undefined) {
        const inventory = this.computedInventories.find(inventory => inventory.id === productId);
        product = this.products.find(product => product.id === inventory.productId);
      }
      return product.name
    },
    randomId() {
      return Math.floor(Math.random() * 1000000000);
    },
    add_project() {
      if(this.selectedInventoryId != null && this.quantity != null) {
        const data = {inventoryId: this.selectedInventoryId, quantity: this.quantity }
        this.orders.push(data);
      }
      if (
          !this.project.name ||
          !this.project.location ||
          !this.project.startDate ||
          !this.project.dueAt ||
          !this.selectedTeamId ||
          this.orders.length === 0
      ) {
        this.validationError = true;
        // Display an error message or handle the validation failure appropriately
        console.error("Please fill in all required fields and add at least one item.");
        return;
      }

      if (this.project.startDate> this.project.dueAt){
        this.validationDateError = true;
        return;
      }

      const projectData = {...this.project};

      this.$emit("add_project", projectData, this.selectedTeamId, this.orders);

      this.project = {
        name: "",
        location: "",
        products: [], // Reset to one member after adding team
        description: "",
        status: "UNSTARTED",
        startDate: null,
        endDate: null
      };
      this.quantity = null;
      this.selectedTeamId = null;
      this.orders = [];
      this.numMaterials = 1; // Reset numMembers
      this.selectedInventoryQuantity = null;
      this.selectedInventoryId = null;
      this.collapseForm()
    },
    collapseForm() {
      this.project.name = "";
      this.project.products = [];
      this.project.location = "";
      this.project.startDate="";
      this.project.dueAt="";
      this.selectedTeamId = null;
      this.selectedTeamId = null;
      this.selectedInventoryItems = [];
      this.orders = [];
      this.quantity = "";
      this.project.description = "";
      this.showForm = false;
      this.validationError = false;
      this.validationDateError = false
      this.$emit("collapse_form");
    },
    addMaterial(inventoryId, quantity) {
      if (this.selectedInventoryId !== null) {
        const selectedInventoryItem = this.inventories.find(item => item.id === this.selectedInventoryId);
        if (quantity > 0) {
          const product = this.getProductById(selectedInventoryItem.productId);
          const data = { inventoryId: this.selectedInventoryId, quantity: quantity, product: product };

          // Remove the selected inventory item from the inventories array
          const index = this.inventories.findIndex(item => item.id === this.selectedInventoryId);
          if (index !== -1) {
            this.inventories.splice(index, 1);
          }

          this.selectedInventoryItems.push(selectedInventoryItem);
          this.orders.push(data);
          this.quantity = null;
          this.selectedInventoryQuantity = null;
          this.selectedInventoryId = null;
        } else {
          console.error("Invalid quantity input");
        }
      }
    },
    removeMaterial(material) {
      const index = this.orders.findIndex(item => item.id === material.id);
      if (index !== -1) {
        // Get the removed inventory from the orders array
        const removedInventory = this.orders[index].inventoryId;

        // Remove the material from the orders array
        this.orders.splice(index, 1);

        // Add the removed inventory back to the inventories array
        this.inventories.push(this.computedInventories.find(inventory => inventory.id === removedInventory));
      }
    },
    closeQuantity(){
      this.selectedInventoryId = null;
      this.quantity = ""
    },
    async findInventories(teamId) {
      this.inventories = await this.inventoryService.findByTeamId(teamId);
      this.computedInventories = await this.inventoryService.findByTeamId(teamId);
    },
    async calculateMaxQuantity(id){
      const inventory = await this.inventoryService.findById(id);
      this.maxQuantity = (inventory.quantity);
    },
    validateQuantity() {
      if (this.quantity !== null && this.quantity !== undefined) {
        const intValue = parseInt(this.quantity);

        if (!isNaN(intValue)) {
          this.quantity = intValue;

          if (this.quantity < 0) {
            this.quantity = 0;
          }

          if (this.quantity > this.maxQuantity) {
            this.quantity = this.maxQuantity;
          }
        }
      }
    },
  },
  computed: {
    projectMaterials: {
      get() {
        return this.project.products.join(", ");
      },
      set(value) {
        this.project.products = value.split(", ");
      },
    },
  },
  watch: {
    showFormValue(newValue) {
      this.showForm = newValue;
    },
    selectedTeamId(newValue) {
      if (newValue !== null) {
        this.findInventories(newValue);
        this.orders = []
      }
    },
    selectedInventoryId(newValue){
      if (newValue !== null) {
        this.calculateMaxQuantity(newValue);
      }
    }
  },
};
</script>

<style>
.swal2-input {
  height: 2.5em;
  padding: 0 0.75em;
  font-size: 1.125em;
  /*margin: 1em 2em 3px;*/
  box-sizing: border-box;
  width: 100%;
  transition: border-color .1s,box-shadow .1s;
  border: 1px solid #d9d9d9;
  border-radius: 0.1875em;
  background: rgba(0,0,0,0);
  box-shadow: inset 0 1px 1px rgba(0,0,0,.06), 0 0 0 3px rgba(0,0,0,0);
  color: inherit;
}
.swal2-show {
  animation: swal2-show .3s;
  width: 320px;
}

.project-form{
  width: 320px;
}
.validateCharacter{
  content: "!";
  display: inline-block;
  width: 1.5em;
  min-width: 1.5em;
  height: 1.5em;
  margin: 0 0.625em;
  border-radius: 50%;
  background-color: #f27474;
  color: #fff;
  font-weight: 600;
  line-height: 1.5em;
  text-align: center;
}
</style>
