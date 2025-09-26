<template>
    <div>
        <!-- SearchDashboardComponent to allow user interaction -->
        <SearchDashboardComponent
            :dashboardTitles="dashboardTitles"
            @dashboard-selected="handleDashboardSelected"
            @all-warehouses-selected="handleAllWarehousesSelected"
        />

        <!-- Use v-if to conditionally render DashboardContainer or AllWarehousesContainer -->
        <DashboardContainer v-if="!showAllWarehouses" :dashboard="selectedDashboard" />
        <AllWarehousesContainer :warehouses="warehouses" :forecasting="forecasting"  v-if="showAllWarehouses" />
    </div>
</template>

<script>
import DashboardContainer from "@/components/dashboard/DashboardContainer.vue";
import SearchDashboardComponent from "@/components/dashboard/SearchDashboardComponent.vue";
import AllWarehousesContainer from "@/components/dashboard/AllWarehousesContainer.vue";

export default {
    // Dependency injection of services
    inject: ["warehousesService", "forecastService"],
    name: "DashboardComponent",
    components: { SearchDashboardComponent, DashboardContainer, AllWarehousesContainer },
    async created() {
        // Fetch warehouses and forecasts on component creation
        this.warehouses = await this.warehousesService.findAll();
        this.filteredWarehouses = this.warehouses;
        this.$emit("warehouses-loaded", this.warehouses);

        this.forecasting = await this.forecastService.findAll();
    },
    data() {
        // Component data properties
        return {
            warehouses: [],
            filteredDashboard: [],
            forecasting: [],
            showForm: false,
            selectedDashboard: null, // Track the selected dashboard
            showAllWarehouses: false, // Flag to control rendering
        };
    },
    methods: {
        // Handle selection of a specific dashboard
        handleDashboardSelected(title) {
            this.selectedDashboard = this.warehouses.find(
                (warehouse) => warehouse.name === title
            );
            this.showAllWarehouses = false; // Hide AllWarehousesContainer
        },
        // Handle selection of "All Warehouses"
        handleAllWarehousesSelected() {
            this.selectedDashboard = null; // Clear selected dashboard
            this.showAllWarehouses = true; // Show AllWarehousesContainer
        },
    },
    computed: {
        // Computed property to generate an array of dashboard titles
        dashboardTitles() {
            return this.warehouses.map((warehouse) => warehouse.name);
        },
    },
};
</script>

<style scoped>
</style>
