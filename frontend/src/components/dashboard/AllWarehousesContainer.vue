<template>
    <!-- Main container for project list, project details, forecasting, and warehouse statistics -->
    <div class="maind2">
        <!-- Project List Section -->
        <div class="project-list" v-if="warehouses">
            <h2><strong>All projects</strong></h2>
            <ul>
                <!-- Iterate through warehouses -->
                <li v-for="warehouse in warehouses" :key="warehouse.id">
                    <ul>
                        <!-- Iterate through teams within a warehouse -->
                        <li v-for="team in warehouse.teams" :key="team.id">
                            <!-- Check if the team has projects -->
                            <div v-if="team.projects && team.projects.length > 0">
                                <!-- Iterate through projects within a team -->
                                <li v-for="project in team.projects" :key="project.id">
                                    <!-- Project button with click handler to toggle project details -->
                                    <button
                                        class="project-button"
                                        @click="toggleProject(project)"
                                        :class="{ 'selected': project === selectedProject }"
                                    >
                                        {{ project.name }}
                                    </button>
                                </li>
                            </div>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>

        <!-- Project Details Section -->
        <div v-if="selectedProject" class="project-details">
            <!-- Display project details -->
            <div class="projectname">{{ selectedProject.name }}</div>
            <p><strong>Location:</strong> {{ selectedProject.location }}</p>
            <p><strong>Start date:</strong> {{ formatDate(selectedProject.startDate) }}</p>
            <p><strong>End date:</strong> {{ formatDate(selectedProject.dueAt) }}</p>
            <p><strong>Status:</strong> {{ selectedProject.status }}</p>
            <p><strong>Description:</strong> {{ selectedProject.description }}</p>
        </div>
    </div>

    <!-- Forecasting Section -->
    <div class="maind6">
        <ul class="forecast-list" v-if="forecasting">
            <!-- Forecast header -->
            <div class="forecast-header" style="display: flex;">
                <!-- Column headers -->
                <div class="font-bold col-span-1" style="margin-right: 12%;">Warehouse</div>
                <div class="font-bold col-span-1" style="margin-right: 14%;">Product</div>
                <div class="font-bold col-span-1" style="margin-right: 12%;">Order Amount</div>
                <div class="font-bold col-span-1" style="margin-right: 16%;">Amount After</div>
                <div class="font-bold col-span-1">Date</div>
            </div>
            <!-- Iterate through forecasting items -->
            <li v-for="orders in forecasting" :key="orders.id">
                <!-- Forecast item details -->
                <div class="middle-part grid grid-cols-5 gap-1 py-1 move-right">
                    <p class="col-span-1">{{ orders.orders.inventory.warehouseId }}</p>
                    <p class="col-span-1">{{ orders.orders.inventory.productId }}</p>
                    <p class="col-span-1">{{ orders.orderQuantity }}</p>
                    <p class="col-span-1">{{ orders.stock - orders.orderQuantity }}</p>
                    <p class="col-span-1">{{ formatDate(orders.date) }}</p>
                </div>
            </li>
        </ul>
    </div>

    <!-- Inventory List Section -->
    <div class="maind4">
        <ul class="inventory-list" v-if="warehouses">
            <!-- Inventory header -->
            <div class="inventory-header" style="display: flex;">
                <!-- Column headers -->
                <div class="font-bold col-span-1" style="margin-right: 25%;">Name</div>
                <div class="font-bold col-span-1" style="margin-right: 24%;">Status</div>
                <div class="font-bold col-span-1">Amount</div>
            </div>
            <!-- Iterate through warehouses and their inventory items -->
            <li v-for="warehouse in warehouses" :key="warehouse.id">
                <ul>
                    <li v-for="item in warehouse.inventory" :key="item.id">
                        <!-- Inventory item details -->
                        <div class="middle-part grid grid-cols-3 gap-1 py-1 move-right">
                            <p class="col-span-1">{{ getProductById(item.productId) }}</p>
                            <p class="col-span-1">{{ item.status }}</p>
                            <p class="col-span-1">{{ item.quantity }}</p>
                        </div>
                    </li>
                </ul>
            </li>
        </ul>
    </div>

    <!-- Warehouse Statistics Section -->
    <div class="maind5">
        <!-- Warehouse, Teams, Full Stock, Low Stock, and Total Items statistics -->
        <div class="warehouses">
            <!-- Warehouse count -->
            <img src="@/assets/dashboard/HouseLine.png" alt="Icon" class="icon2"/>
            <div class="centercard">
                <h2>Warehouses</h2>
                <h1>{{ totalWarehouseCount }}</h1>
            </div>
        </div>
        <div class="teams">
            <!-- Teams count -->
            <img src="@/assets/dashboard/UsersThree.png" alt="Icon" class="icon2"/>
            <div class="centercard">
                <h2>Teams</h2>
                <h1>{{ totalTeamCount }}</h1>
            </div>
        </div>
        <div class="full">
            <!-- Full Stock count -->
            <img src="@/assets/dashboard/Check.png" alt="Icon" class="icon2"/>
            <div class="centercard">
                <h2>Full Stock</h2>
                <h1>{{ fullItemCount }}</h1>
            </div>
        </div>
        <div class="low">
            <!-- Low Stock count -->
            <img src="@/assets/dashboard/Warning.png" alt="Icon" class="icon2"/>
            <div class="centercard">
                <h2>Low Stock</h2>
                <h1>{{ lowItemCount }}</h1>
            </div>
        </div>
        <div class="total">
            <!-- Total Items count -->
            <img src="@/assets/dashboard/Vector.png" alt="Icon" class="icon2"/>
            <div class="centercard">
                <h2>Total Items</h2>
                <h1>{{ totalItemCount }}</h1>
            </div>
        </div>
    </div>
</template>

<script>
// Import required module
import team from "@/models/team";

// Export the component configuration
export default {
    // Inject productsService as a dependency
    inject: ["productsService"],
    // Component name
    name: "AllWarehousesContainer",
    // Component lifecycle hook for asynchronous operations on component creation
    async created() {
        // Fetch all products on component creation
        this.products = await this.productsService.findAll()
    },
    // Component data properties
    data() {
        return {
            projects: [], // List of projects
            selectedProject: null, // Currently selected project
            products: [] // List of products
        };
    },
    // Component properties
    props: {
        warehouses: {
            type: Array,
            default: () => [],
        },
        forecasting: {
            type: Array,
            default: () => [],
        },
    },
    // Component methods
    methods: {
        // Format date string to a readable format
        formatDate(dateString) {
            const date = new Date(dateString);
            return date.toISOString().split('T')[0];
        },
        // Toggle selected project on button click
        toggleProject(project) {
            this.selectedProject = project;
        },
        // Get product name by product ID
        getProductById(productId) {
            const product = this.products.find(product => product.id === productId);
            if (product && product.name) {
                return product.name;
            } else {
                return "Unknown Product";
            }
        },
    },
    // Component computed properties
    computed: {
        // Get the team model
        team() {
            return team;
        },
        // Calculate total warehouse count
        totalWarehouseCount() {
            return this.warehouses.length;
        },
        // Calculate total team count
        totalTeamCount() {
            return this.warehouses.reduce((count, warehouse) => count + warehouse.teams.length, 0);
        },
        // Calculate count of items with 'FULL' status
        fullItemCount() {
            if (this.warehouses) {
                return this.warehouses.reduce((count, warehouse) => {
                    return count + warehouse.inventory.filter(item => item.status === 'FULL').length;
                }, 0);
            }
            return 0; // Handle the case when there's no warehouse data.
        },
        // Calculate count of items with 'LOW' status
        lowItemCount() {
            if (this.warehouses) {
                return this.warehouses.reduce((count, warehouse) => {
                    return count + warehouse.inventory.filter(item => item.status === 'LOW').length;
                }, 0);
            }
            return 0; // Handle the case when there's no warehouse data.
        },
        // Calculate count of items with 'PENDING' status
        pendingItemCount() {
            if (this.warehouses) {
                return this.warehouses.reduce((count, warehouse) => {
                    return count + warehouse.inventory.filter(item => item.status === 'PENDING').length;
                }, 0);
            }
            return 0; // Handle the case when there's no warehouse data.
        },
        // Calculate total item count
        totalItemCount() {
            if (this.warehouses) {
                return this.warehouses.reduce((count, warehouse) => {
                    return count + warehouse.inventory.length;
                }, 0);
            }
            return 0; // Handle the case when there's no warehouse data.
        },
    },
};
</script>

<style scoped>

.forecast-list {
    margin-top: 1%;
    width: 100%;
}

.forecast-header{
    margin-left: 4%;
    margin-bottom: 2%;
}

.maind6 {
    position: absolute;
    align-items: center;
    display: flex;
    background-color: white;
    width: 70%;
    height: 25%;
    border-radius: 12px;
    margin-top: 54%;
    margin-left: 20%;
    z-index: -15;
    overflow-y: auto; /* Add this line to enable vertical scroll */
    border: 5px solid rgba(204, 211, 86, 0.37); /* Brown border color */
    box-sizing: border-box; /* Include padding and border in the total width/height */
}

.project-list {
    height: 95%; /* Set your desired max height */
    max-width: 90%;
    overflow-y: auto; /* Add this line to enable vertical scroll if necessary */
    background-color: rgba(241, 241, 241, 0.41);
    border-radius: 10px;
    margin-left: 4%;
}

.project-list h2 {
    margin-top: 5%;
    margin-left: 10%;
}

.project-button {
    background-color: #C7D02C;
    color: #ffffff;
    border-radius: 6px;
    padding: 5px 10px;
    margin: 5px 0; /* Add margin for spacing between buttons */
    width: 80%;
    margin-left: 10%;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.project-button:hover {
    background-color: #767e16;
}

.project-button.selected {
    background-color: #767e16;
}

.icon2 {
    width: 42px; /* Adjust the width as needed */
    height: 42px; /* Adjust the height as needed */
    padding: 2px;
    margin-left: 10px; /* Add some spacing between the icon and the counter */
    margin-top: 4px; /* Add some spacing between the icon and the counter */

}

.warehouses {
    position: absolute;
    display: flex;
    margin-left: 3%;
    width: 16%;
    height: 70%;
    background-color: white;
    border-radius: 12px;
}

.teams {
    position: absolute;
    display: flex;
    margin-left: 21%;
    width: 16%;
    height: 70%;
    background-color: white;
    border-radius: 12px;
}

.full {
    position: absolute;
    display: flex;
    margin-left: 39%;
    width: 16%;
    height: 70%;
    background-color: white;
    border-radius: 12px;
}

.low {
    position: absolute;
    display: flex;
    margin-left: 57%;
    width: 16%;
    height: 70%;
    background-color: white;
    border-radius: 12px;
}

.inventory-header {
    margin-left: 5%;
    margin-bottom: 4%;
}

.total {
    position: absolute;
    display: flex;
    margin-left: 75%;
    width: 23%;
    height: 70%;
    background-color: white;
    border-radius: 12px;
}

.project-details p {
    margin-bottom: 15px; /* Adjust the bottom margin as needed to create space between the rows */
}

.project-details {
    margin-left: 10%;
    overflow-y: auto; /* Add this line to enable vertical scroll */
    width: 50%;
}

.projectname {
    background-color: #C7D02C;
    margin-bottom: 10%;
    color: #ffffff;
    border-radius: 6px;
    padding: 5px 10px;
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.maind2 {
    position: absolute;
    align-items: center;
    display: flex;
    background-color: white;
    width: 37%;
    height: 51%;
    border-radius: 12px;
    margin-top: 22%;
    margin-left: 53%;
    z-index: -15;
    border: 5px solid rgba(204, 211, 86, 0.37); /* Brown border color */
    box-sizing: border-box; /* Include padding and border in the total width/height */
}


.maind4 {
    position: absolute;
    display: flex;
    background-color: white;
    width: 30%;
    height: 51%;
    border-radius: 12px;
    margin-top: 22%;
    margin-left: 20%;
    z-index: -15;
    overflow-y: auto; /* Add this line to enable vertical scroll */
    border: 5px solid rgba(204, 211, 86, 0.37); /* Brown border color */
    box-sizing: border-box; /* Include padding and border in the total width/height */
}

.inventory-list {
    margin-top: 3%;
    width: 100%;
}

.move-right {
    margin-left: 5%; /* Adjust the margin value as needed to move it right. */
}

.inventory-item-details p {
    padding: 5px; /* Add padding to separate the items */
}

.maind5 {
    position: absolute;
    align-items: center;
    display: flex;
    background-color: #C7D02C;
    width: 70%;
    height: 17%;
    border-radius: 12px;
    margin-top: 11%;
    margin-left: 20%;
    z-index: -15;
}

.centercard {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    text-align: center;
    margin-left: 10%;
}

/* Add these styles to make the counter numbers big, bold, and green */
.centercard h1 {
    font-size: 50px; /* Adjust the font size as needed */
    font-weight: bold;
    color: #767e16;
    margin: 0; /* Remove default margin to center vertically */
}

/* Style the text under the counter numbers */
.centercard h2 {
    font-size: 18px; /* Adjust the font size as needed */
    color: #767e16; /* Adjust the color as needed */
    margin: 4px 0; /* Adjust the margin for spacing */
}


/* Add media query for smaller screens */
/* Media query for smaller screens */
@media screen and (max-width: 1200px) {
    .maind2 {
        margin-left: 20%;
        width: 80%;
        height: 50%;
        margin-top: 140%;
    }

    .maind4 {
        margin-left: 20%;
        width: 80%;
        height: 50%;
        margin-top: 40%;
    }

    .maind5 {
        display: none; /* Add this line to hide .maind5 */
    }
}



</style>
<script setup>
</script>