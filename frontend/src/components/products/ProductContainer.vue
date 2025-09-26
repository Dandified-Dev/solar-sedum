<template>
  <div class="inventory-container">
    <div class="top-part">
    </div>
    <div class="middle-part grid grid-cols-12 gap-4 py-2">

      <p class="col-span-1">{{ item.id }}</p>
      <h2 class="col-span-2">{{ item.name}}</h2>
      <p class="col-span-2">{{ item.quantity }}</p>
      <p class="col-span-1">{{ item.price }}</p>
      <p class="col-span-2"> {{ totalValue }}</p>
      <p class="col-span-2">{{ item.description}}</p>


    </div>

    <div class="flex justify-end ">
      <img @click="openEditComponent(item)" src="@/assets/edit_icon.png" alt="Bewerk" class="cursor-pointer" width="20" height="20" />
      <img @click="deleteItem(item)" src="@/assets/red_x_circle.png" alt="x" class="delete_item cursor-pointer ml-3" width="20" height="20" id="delete_item"/>
    </div>
  </div>
</template>

<script>
export default {
  name: "ProductContainer",
  props: ["item", "openEditComponent"],
  emits: ["delete-item"],
  inject: ["inventoryService"],
  data() {
    return {
      product: {}
    };
  },
  mounted() {
    console.log("Item in ProductContainer:", this.item);
  },
  computed: {
    totalValue() {
      return (this.item.price * this.item.quantity).toFixed(2);
    },
  },

  methods: {
    deleteItem(item) {
      this.$emit("delete-item", item);
    },
  },
};
</script>


<style scoped>
.inventory-container {
  border: 1px solid #ccc;
  border-radius: 5px;
  margin: 10px;
  padding: 10px;
  background-color: #f9f9f9;
}

.top-part {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
h2 {
  margin: 0;
}

.middle-part {
  margin-top: 10px;
  margin-left: 50px;
}
</style>
