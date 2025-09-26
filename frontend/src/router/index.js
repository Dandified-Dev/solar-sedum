import { createRouter, createWebHashHistory } from 'vue-router';
import jwt_decode from 'jwt-decode';
import TeamComponent from '../components/teams/TeamComponent.vue';
import WarehouseComponent from "@/components/warehouses/WarehouseComponent.vue";
import ProjectComponent from "@/components/projects/ProjectComponent.vue";
import LoginComponent from "@/components/login/LoginComponent";
import DashboardComponent from "@/components/dashboard/DashboardComponent.vue";
import InventoryComponent from "@/components/inventory/InventoryComponent.vue";
import UserComponent from "@/components/users/UserComponent";
import UserBoardComponent from "@/components/userboard/UserBoardComponent.vue";
import ProductComponent from "@/components/products/ProductComponent.vue";

const routes = [
  {path: '/', redirect:'/dashboard'},
  {
    path: '/dashboard',
    name: 'dashboard',
    component: DashboardComponent,
    meta: { requiresAuth: true, allowedRoles: ['admin']}
  },
  {
    path: '/inventory',
    name: 'inventory',
    component: InventoryComponent,
    meta: { requiresAuth: true, allowedRoles: ['admin'] }
  },
  {
    path: '/products',
    name: 'products',
    component: ProductComponent,
    meta: { requiresAuth: true, allowedRoles: ['admin'] }
  },
  {
    path: '/warehouses',
    name: 'warehouses',
    component: WarehouseComponent,
    meta: { requiresAuth: true, allowedRoles: ['admin'] }
  },
  {
    path: '/teams',
    name: 'teams',
    component: TeamComponent,
    meta: { requiresAuth: true, allowedRoles: ['admin'] }
  },
  {
    path: '/projects',
    name: 'projects',
    component: ProjectComponent,
    meta: { requiresAuth: true, allowedRoles: ['admin'] }
  },
  {
    path: '/login',
    name: 'login',
    component: LoginComponent
  },
  {
    path: '/users',
    name: 'users',
    component: UserComponent,
    meta: { requiresAuth: true, allowedRoles: ['admin'] }
  },
  {
    path: '/userboard',
    name: 'userboard',
    component: UserBoardComponent,
    meta: { requiresAuth: true, allowedRoles: ['user']}
  },

  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  }
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
});

router.beforeEach((to, from, next) => {
  const requiresAuth = to.meta.requiresAuth;

  if (requiresAuth && !checkIfUserIsLoggedIn()) {
    next('/login');
  } else if (to.name === 'login' && checkIfUserIsLoggedIn()) {
    next('/dashboard');
  } else if (!router.hasRoute(to.name)) {
    next('/login');
  } else {
    const userRole = getUserRole();
    if (requiresAuth && !checkUserRole(userRole, to.meta.allowedRoles)) {

      next('/userboard');
    } else {
      // Alleen doorsturen naar '/userboard' als de huidige route '/userboard' is en de rol 'user' is
      if (to.name === 'userboard' && to.meta.allowedRoles.includes('user') && userRole === 'user') {
        next();
      } else {
        next();
      }
    }
  }
});


function getUserRole() {

  const token = localStorage.getItem('token');
  if (token) {
    try {
      const decodedToken = jwt_decode(token);
      return decodedToken.role;
    } catch (error) {
      console.error('Error decoding token:', error);
      return null;
    }
  }

}

function checkUserRole(userRole, allowedRoles) {
  return userRole !== null && allowedRoles.includes(userRole);
}


function checkIfUserIsLoggedIn() {
  const token = localStorage.getItem('token');
  return token !== null && token !== undefined && token !== "";
}

export default router;
