
Certainly! Here are comments added to explain different sections of your Vue.js component:

html
Copy code
<template>
    <!-- Main container for teams, projects, inventory, and statistics -->
    <div class="maind">
        <!-- Display teams if dashboard is available, otherwise show a message -->
        <div v-if="dashboard">
            <div class="teams-list">
                <!-- Iterate through teams and display buttons for each team -->
                <button
                    class="team-button"
                    v-for="team in dashboard.teams"
                    :key="team.id"
                    :class="{ 'selected': team === selectedTeam }"
                    @click="toggleTeam(team)"
                >{{ team.name }}
                </button>
            </div>
        </div>
        <!-- Show message when no warehouse is selected -->
        <h2 v-else>No warehouse selected</h2>
    </div>

    <!-- Projects Section -->
    <div class="maind2">
        <div class="project-list">
            <h2><strong>Projects</strong></h2>
            <!-- Display projects of the selected team -->
            <div v-if="selectedTeam">
                <ul>
                    <!-- Iterate through projects of the selected team -->
                    <li v-for="project in selectedTeam.projects" :key="project.id">
                        <!-- Project button with click handler to toggle project details -->
                        <button
                            class="project-button"
                            @click="toggleProject(project)"
                            :class="{ 'selected': project === selectedProject }"
                        >
                            {{ project.name }}
                        </button>
                    </li>
                </ul>
            </div>
            <!-- Display message when no project is selected -->
            <div class="noselect" v-else>
                <h2>No project selected</h2>
            </div>
        </div>
        <!-- Display details of the selected project if available -->
        <div v-if="selectedProject" class="project-details">
            <div class="projectname">{{ selectedProject.name }}</div>
            <p><strong>Location:</strong> {{ selectedProject.location }}</p>
            <p><strong>Start date:</strong> {{ formatDate(selectedProject.startDate) }}</p>
            <p><strong>End date:</strong> {{ formatDate(selectedProject.dueAt) }}</p>
            <p><strong>Status:</strong> {{ selectedProject.status }}</p>
            <p><strong>Description:</strong> {{ selectedProject.description }}</p>
        </div>
    </div>

    <!-- Inventory Section -->
    <div class="maind4">
        <ul class="inventory-list" v-if="dashboard">
            <!-- Inventory header -->
            <div class="inventory-header" style="display: flex;">
                <div class="font-bold col-span-1" style="margin-right: 25%;">Name</div>
                <div class="font-bold col-span-1" style="margin-right: 24%;">Status</div>
                <div class="font-bold col-span-1">Amount</div>
            </div>
            <ul>
                <!-- Iterate through inventory items and display details -->
                <li v-for="item in dashboard.inventory" :key="item.id">
                    <div class="middle-part grid grid-cols-3 gap-1 py-1 move-right">
                        <p class="col-span-1">{{ getProductById(item.productId) }}</p>
                        <p class="col-span-1">{{ item.status }}</p>
                        <p class="col-span-1">{{ item.quantity }}</p>
                    </div>
                </li>
            </ul>
        </ul>
    </div>

    <!-- Statistics Section -->
    <div class="maind5">
        <!-- Display counts and icons for full, low, and total items -->
        <div class="full">
            <img src="@/assets/dashboard/Check.png" alt="Icon" class="icon"/>
            <h1>{{ fullItemCount }}</h1>
            <h2>Full Stock</h2>
        </div>
        <div class="low">
            <img src="@/assets/dashboard/Warning.png" alt="Icon" class="icon"/>
            <h1>{{ lowItemCount }}</h1>
            <h2>Low Stock</h2>
        </div>
        <div class="total">
            <img src="@/assets/dashboard/Vector.png" alt="Icon" class="icon2"/>
            <h1>{{ totalItemCount }}</h1>
            <h2>Total Items</h2>
        </div>
    </div>
</template>

<script>
export default {
    // Inject productsService and forecastService as dependencies
    inject: ["productsService", "forecastService"],
    name: "DashboardContainer",
    props: ["dashboard", "forecasting"],
    // Fetch all products on component creation
    async created() {
        this.products = await this.productsService.findAll();
    },
    data() {
        return {
            selectedTeam: null, // Track the selected team
            selectedProject: null, // Track the selected project
            products: [],
        };
    },
    methods: {
        // Format date string to a readable format
        formatDate(dateString) {
            const date = new Date(dateString);
            return date.toISOString().split('T')[0];
        },
        // Toggle selected team on button click
        toggleTeam(team) {
            if (this.selectedTeam === team) {
                this.selectedTeam = null; // Deselect the team if it's already selected
            } else {
                this.selectedTeam = team; // Select the team if it's not selected
            }
            this.selectedProject = null; // Deselect the project when toggling teams
        },
        // Toggle selected project on button click
        toggleProject(project) {
            if (this.selectedProject === project) {
                this.selectedProject = null; // Deselect the project if it's already selected
            } else {
                this.selectedProject = project; // Select the project if it's not selected
            }
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
    watch: {
        // Watch for changes to the dashboard prop
        dashboard: function () {
            // Reset selectedTeam, selectedProject, and forecasting when the dashboard changes
            this.selectedTeam = null;
            this.selectedProject = null;
        },
    },
    computed: {
        // Calculate count of items with 'FULL' status
        fullItemCount() {
            if (this.dashboard) {
                return this.dashboard.inventory.filter(item => item.status === 'FULL').length;
            }
            return 0; // Handle the case when there's no dashboard data.
        },
        // Calculate count of items with 'LOW' status
        lowItemCount() {
            if (this.dashboard) {
                return this.dashboard.inventory.filter(item => item.status === 'LOW').length;
            }
            return 0; // Handle the case when there's no dashboard data.
        },
        // Calculate count of items with 'PENDING' status
        pendingItemCount() {
            if (this.dashboard) {
                return this.dashboard.inventory.filter(item => item.status === 'PENDING').length;
            }
            return 0; // Handle the case when there's no dashboard data.
        },
        // Calculate total item count
        totalItemCount() {
            if (this.dashboard) {
                return this.dashboard.inventory.length;
            }
            return 0; // Handle the case when there's no dashboard data.
        },
    },
};
</script>


<style scoped>

.icon {
    width: 48px; /* Adjust the width as needed */
    height: 48px; /* Adjust the height as needed */
    margin-right: 8px; /* Add some spacing between the icon and the counter */
}

.icon2 {
    width: 42px; /* Adjust the width as needed */
    height: 42px; /* Adjust the height as needed */
    padding: 2px;
    margin-left: 4px; /* Add some spacing between the icon and the counter */
    margin-top: 4px; /* Add some spacing between the icon and the counter */

}

/* Add these styles to make the counter numbers big, bold, and green */
.maind5 h1 {
    font-size: 60px; /* Adjust the font size as needed */
    font-weight: bold;
    color: #767e16;
    margin-left: 20%;
    margin-top: 7%;
}

/* Style the text under the counter numbers */
.maind5 h2 {
    font-size: 18px; /* Adjust the font size as needed */
    color: #767e16; /* Adjust the color as needed */
    margin-top: 4px; /* Add some spacing between the counter and the text */
    margin-left: 10%;
}

.full {
    position: absolute;
    display: flex;
    margin-left: 3%;
    width: 28%;
    height: 70%;
    background-color: white;
    border-radius: 12px;
}

.low {
    position: absolute;
    display: flex;
    margin-left: 34%;
    width: 28%;
    height: 70%;
    background-color: white;
    border-radius: 12px;
}

.pending {
    position: absolute;
    display: flex;
    margin-left: 51%;
    width: 22%;
    height: 70%;
    background-color: white;
    border-radius: 12px;
}

.total {
    position: absolute;
    display: flex;
    margin-left: 65%;
    width: 30%;
    height: 70%;
    background-color: white;
    border-radius: 12px;
}

.inventory-list {
    margin-top: 10%;
    width: 100%;
}

.move-right {
    margin-left: 5%; /* Adjust the margin value as needed to move it right. */
}

.inventory-item-details p {
    margin: 0; /* Remove default paragraph margin for spacing */
    padding: 5px; /* Add padding to separate the items */
}


.project-button.selected {
    background-color: #767e16;
}


.team-button.selected {
    background-color: #767e16;
}


.maind {
    position: absolute;
    height: 4rem;
    align-items: center;
    display: flex;
    background-color: white;
    width: 37%;
    border-radius: 12px;
    margin-top: 22%;
    margin-left: 53%;
    z-index: 15;
    border: 5px solid rgba(204, 211, 86, 0.37); /* Brown border color */
    box-sizing: border-box; /* Include padding and border in the total width/height */
}

.maind2 {
    position: absolute;
    align-items: center;
    display: flex;
    background-color: white;
    width: 37%;
    height: 44%;
    border-radius: 12px;
    margin-top: 26%;
    margin-left: 53%;
    z-index: 15;
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
    overflow-y: auto; /* Add this line to enable vertical scroll */
    border: 5px solid rgba(204, 211, 86, 0.37); /* Brown border color */
    box-sizing: border-box; /* Include padding and border in the total width/height */
}

.move-right {
    margin-left: 5%; /* Adjust the margin value as needed to move it right. */
}

.inventory-item-details p {
    padding: 5px; /* Add padding to separate the items */
}

.inventory-list {
    margin-top: 3%;
    width: 100%;
}

.inventory-header {
    margin-left: 5%;
    margin-bottom: 4%;
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
    z-index: -1;
}

.project-list {
    height: 95%; /* Set your desired max height */
    max-width: 90%;
    min-width: 100px; /* Set your desired minimum width */
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

.projectname {
    background-color: #C7D02C;
    margin-bottom: 10%;
    color: #ffffff;
    border-radius: 6px;
    padding: 5px 10px;
    width: 80%;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.noselect{
    background-color: #C7D02C;
    margin-bottom: 10%;
    color: #ffffff;
    border-radius: 6px;
    padding: 5px 10px;
    width: 40%;
    margin-left: 15%;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.project-details {
    margin-left: 5%; /* Adjust the left margin */
    width: 80%;
}

.project-details p {
    margin-bottom: 10px; /* Adjust the bottom margin as needed to create space between the rows */
}

.teams-list {
    list-style: none;
    padding: 0;
    margin: 0;
}

/* Make the buttons display horizontally */
.team-button {
    cursor: pointer;
    display: inline-flex;
    background-color: #C7D02C;
    color: white;
    border: none;
    border-radius: 6px;
    padding: 5px 10px;
    margin-left: 10px;
    z-index: 15;
}

.team-button:hover {
    background-color: #767e16;
    z-index: 15;
}

/* Media query for smaller screens */
@media screen and (max-width: 1200px) {
    .maind, .maind2,.maind4 {
        width: 70%;
        margin-left: 0;
    }

    .maind{
        margin-left: 20%;
        margin-top: 35%;
    }

    .maind2{
        margin-left: 20%;
        margin-top: 50%;
    }

    .maind4{
        margin-left: 20%;
        margin-top: 135%;
    }

    .maind5 {
        display: none; /* Add this line to hide .maind5 */
    }
}

</style>