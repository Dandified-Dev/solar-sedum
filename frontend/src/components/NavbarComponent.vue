<template>
  <div class="main">
    <div class="nav mt-40">
      <!-- Dashboard Link -->
      <div v-if="$route.path !== '/userboard'" class="navbar_component">
        <img src="../assets/navbar/dashboard_w.png" alt="" class="img">
        <router-link to="/dashboard" class="button" :class="{ active: $route.path === '/dashboard' }">Dashboard</router-link>
      </div>

      <!-- Inventory Link -->
      <div v-if="$route.path !== '/userboard'" class="navbar_component">
        <img src="../assets/navbar/inventory.png" alt="">
        <router-link to="/inventory" class="button" :class="{ active: $route.path === '/inventory' }">Inventory</router-link>
      </div>

      <!-- Products Link -->
      <div v-if="$route.path !== '/userboard'" class="navbar_component">
        <img src="../assets/navbar/inventory.png" alt="">
        <router-link to="/products" class="button" :class="{ active: $route.path === '/products' }">Products</router-link>
      </div>

      <!-- Warehouses Link -->
      <div v-if="$route.path !== '/userboard'" class="navbar_component">
        <img src="../assets/navbar/warehouse.png" alt="">
        <router-link to="/warehouses" class="button" :class="{ active: $route.path === '/warehouses' }">Warehouses</router-link>
      </div>

      <!-- Teams Link -->
      <div v-if="$route.path !== '/userboard'" class="navbar_component">
        <img src="../assets/navbar/team.png" alt="">
        <router-link to="/teams" class="button" :class="{ active: $route.path === '/teams' }">Teams</router-link>
      </div>

      <!-- Projects Link -->
      <div v-if="$route.path !== '/userboard'" class="navbar_component">
        <img src="../assets/navbar/project.png" alt="">
        <router-link to="/projects" class="button" :class="{ active: $route.path === '/projects' }">Projects</router-link>
      </div>

      <!-- Users Link -->
      <div v-if="$route.path !== '/userboard'" class="navbar_component">
        <img class="h-5" src="../assets/navbar/user-removebg-preview.png" alt="">
        <router-link to="/users" class="button" :class="{ active: $route.path === '/users' }">Users</router-link>
      </div>

      <!-- Userboard Link -->
      <div v-if="$route.path === '/userboard'" class="navbar_component">
        <img class="h-5" src="../assets/navbar/user-removebg-preview.png" alt="">
        <router-link to="/userboard" class="button" :class="{ active: $route.path === '/userboard' }">Userboard</router-link>
      </div>

      <!-- Logout Link -->
      <div class="navbar_component">
        <img src="../assets/navbar/logout_icon.png" alt="" class="h-[25px] w-[24px]">
        <a @click="logout" class="button">Logout</a>
      </div>
    </div>
  </div>
</template>

<script>
import Swal from "sweetalert2";

export default {
  name: "NavbarComponent",
  methods: {
    // Add a method to handle logout
    logout() {
      Swal.fire({
        title: 'Are you sure?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, logout!'
      }).then(async (result) => {
        if (result.isConfirmed) {
          // Remove the JWT token from localStorage
          localStorage.removeItem('token');

          // Redirect to the login page
          this.$router.push('/login');
        }
      })

    },
  },
};
</script>

<style scoped>
.main {
  background-color: #572700;
  list-style-type: none;
  padding: 0;
  width: 17%;
  position: fixed;
  height: 100%;
  overflow: auto;
  align-content: center;
}

a {
  text-decoration: none;
  color: gray;
  font-size: 20px;
  display: block;
  padding: 10px;
  text-align: center;
  margin: 0.8rem, 0.8rem, 0.8rem, 0;
  font-style: italic;
}

img {
  filter: grayscale(100%);
}

.button.active {
  color: white; /* Style for active link */
}
.button {
  cursor: pointer;
}

.navbar_component {
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-top: 5%;
  margin-left: 15%;
}
</style>
