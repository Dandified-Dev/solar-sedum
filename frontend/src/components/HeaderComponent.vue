<template>
  <div class="main">
    <img src="../assets/solar_sedum.png" class="logo">
    <img v-if="!$route.path.includes('/userboard')" @click="handleForm" :class="{ 'bell_icon': true, 'shake': notificationItems.length > 0 }" src="../assets/bell_icon.png" alt="bell" class="bell_icon h-10">
    <div v-if="!$route.path.includes('/userboard')" class="user-info">Logged in as: <strong>{{ loggedInUserEmail }} </strong> <br> Role: <strong>{{ loggedInUserRole }}</strong></div>
  </div>

  <img src="../assets/style_circle_1.png" alt="" width="120" class="right_circle">
  <img src="../assets/style_circle_2.png" alt="" height="80" class="bottom_circle h-20">
</template>


<script>
import jwtDecode from 'jwt-decode';
import Swal from "sweetalert2";

export default {
  inject: ["warehousesService", "inventoryService", "productsService"],
  name: "HeaderComponent",
  async created() {
    this.warehouses = await this.warehousesService.findAll();
    this.items = await this.inventoryService.findAll();
    this.notificationItems= this.items.filter(item => item.status === "LOW");
    this.products = await this.productsService.findAll();
  },
  data() {
    return {
      loggedInUserEmail: "",
      loggedInUserRole: "",
      showNotifications: false,
      notificationItems: [],
      products: [],
    };
  },
  mounted() {

    // Check if token is present before decoding
    const token = localStorage.getItem("token");
    if (token) {
      this.extractUserInfoFromToken(token);
    } else {
      console.log("No token found in localStorage");
    }
  },
  methods: {
    async handleForm() {
      if (this.notificationItems.length > 0){
        const itemsHtml = this.notificationItems.map(item => `<p>${this.capitalizeFirstLetter(this.getProductById(item.productId))} at ${this.capitalizeFirstLetter(this.getWarehouseById(item.warehouseId))}</p>`).join('');


        await Swal.fire({
          title: "Products low on stock",
          html: itemsHtml,
          icon: "warning"
        });
        // Remove the 'shake' class after the modal is closed
        this.$nextTick(() => {
          document.querySelector('.bell_icon').classList.remove('shake');
        });
      }
    },
    capitalizeFirstLetter(string) {
      return string.charAt(0).toUpperCase() + string.slice(1);
    },
    extractUserInfoFromToken(token) {
      try {

        const decodedToken = jwtDecode(token);

        // Log the decoded token to check its structure
        console.log("Decoded token:", decodedToken);

        if (!decodedToken || typeof decodedToken !== 'object') {
          console.error("Decoded token is not an object:", decodedToken);
          return;
        }

        if (!decodedToken.sub || !decodedToken.role) {
          console.error("Decoded token is missing 'sub' or 'role' property:", decodedToken);
          return;
        }

        this.loggedInUserEmail = decodedToken.sub;
        this.loggedInUserRole = decodedToken.role;

      } catch (error) {
        console.error("Error decoding token:", error.message);
      }
    },
    getProductById(productId) {
      const product = this.products.find(product => product.id === productId);
      if (product && product.name) {
        return product.name;
      } else {
        return "Unknown Product";
      }
    },
    getWarehouseById(warehouseId) {
      const warehouse = this.warehouses.find(item => item.id === warehouseId);
      if (warehouse && warehouse.name) {
        return warehouse.name;
      } else {
        return "Unknown Warehouse";
      }
    },
  },
};
</script>

<style scoped>
.main{
    position: fixed;
    margin-left: 17%;
    overflow: hidden;
    background-color: white;
    width: 100%;
    padding-top: 1rem;
    height: 5rem;
    box-sizing: unset !important;
}
img{
    display: revert !important;
}

.logo{
    margin-left: 1rem
}

.user-info {
  position: fixed;
  right: 13rem;
  margin-top: -5vh;
}

.bell_icon {
    margin-top: 1vh;
    cursor: pointer;
    position: fixed;
    right: 5rem;
}
.right_circle{
    position: fixed;
    z-index: 9999;
    right: 0;
    margin-top: 5.7rem;
}
.bottom_circle{
    position: fixed;
    bottom: 0;
    right: 0;
}
@keyframes shakeAnimation {
  10%, 90% {
    transform: translateX(-5px);
  }

  20%, 80% {
    transform: translateX(5px);
  }

  30%, 50%, 70% {
    transform: translateX(-5px);
  }

  40%, 60% {
    transform: translateX(5px);
  }
}

.shake {
  animation: shakeAnimation 1s infinite;
}

</style>