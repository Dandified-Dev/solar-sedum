<template>
  <header-component v-if="!loginActive"/>
  <navbar-component v-if="!loginActive"/>
  <router-view/>
</template>

<script>
import HeaderComponent from './components/HeaderComponent.vue';
import NavbarComponent from './components/NavbarComponent.vue';
import ProjectsAdapter from "@/services/ProjectsAdapter";
import WarehousesAdapter from "@/services/WarehousesAdapter";
import TeamsAdapter from "@/services/TeamsAdapter";
import InventoryAdapter from "@/services/InventoryAdapter";
import LoginAdapter from "@/services/LoginAdapter";
import UsersAdapter from "@/services/UsersAdapter";
import ProductAdapter from "@/services/ProductAdapter";
import OrdersAdapter from "@/services/OrdersAdapter";
import ForecastAdapter from "@/services/ForecastAdapter";
const BACKEND_URL = "http://localhost:8086"
// const BACKEND_URL = "https://ewa-back-end-6158.onrender.com"

export default {
  name: "App",
  components: { HeaderComponent, NavbarComponent },
  computed: {
    loginActive() {
      return this.$route.path === "/login";
    }
  },
  provide() {
    return {
      warehousesService: new WarehousesAdapter(BACKEND_URL + "/warehouses"),
      teamsService: new TeamsAdapter(BACKEND_URL + "/teams"),
      projectsService: new ProjectsAdapter(BACKEND_URL + "/projects"),
      inventoryService: new InventoryAdapter(BACKEND_URL + "/inventory"),
      loginService: new LoginAdapter(BACKEND_URL + "/login"), //
      usersService: new UsersAdapter(BACKEND_URL + "/users"),
      productsService: new ProductAdapter(BACKEND_URL + "/products"),
      forecastService: new ForecastAdapter(BACKEND_URL + "/forecast"),
      orderService: new OrdersAdapter(BACKEND_URL + "/orders")
    }
  }
};
</script>
<style>
body {
  margin: 0;
  background-color: #f4f4f4;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>
