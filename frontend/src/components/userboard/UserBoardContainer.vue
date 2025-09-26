<template>
    <div>
        <div class="Mainbar">
            <div class="item team">
                <img src="@/assets/dashboard/UsersThree.png" alt="Icon" class="icon"/>
                <h2>Team: {{ userTeamName }}</h2>
            </div>
            <div class="item user">
                <img src="@/assets/dashboard/ArrowsDownUp.png" alt="Icon" class="icon"/>
                <h2>Welcome {{ loggedInUserEmail }}</h2>
            </div>
            <div class="item warehouse">
                <img src="@/assets/dashboard/HouseLine.png" alt="Icon" class="icon2"/>
                <h2>Warehouse: {{ userWarehouseName }}</h2>
            </div>
        </div>

        <div class="Midbar">
            <div class="projectnamer"><h1>Projects</h1></div>
            <div class="inventorynamer"><h1>Inventory</h1></div>
        </div>

        <div class="Subbar">
            <div class="projectbox">
                <div class="project-list" v-if="warehouse">
                    <h2><strong>Projects</strong></h2>
                    <ul>
                        <li v-for="team in warehouse.teams" :key="team.id">
                            <div v-if="team.name === userTeamName">
                                <ul v-if="team.projects && team.projects.length > 0">
                                    <li v-for="project in team.projects" :key="project.id">
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
                        </li>
                    </ul>
                </div>


                <div v-if="selectedProject" class="project-details">
                    <div class="projectname">{{ selectedProject.name }}</div>
                    <p><strong>Location:</strong> {{ selectedProject.location }}</p>
                    <p><strong>Start date:</strong> {{ formatDate(selectedProject.startDate) }}</p>
                    <p><strong>End date:</strong> {{ formatDate(selectedProject.dueAt) }}</p>
                    <p><strong>Status:</strong> {{ selectedProject.status }}</p>
                    <p><strong>Description:</strong> {{ selectedProject.description }}</p>
                </div>
            </div>

            <div class="inventorybox">

                <ul class="inventory-list" v-if="warehouse">
                    <div class="inventory-header" style="display: flex;">
                        <div class="font-bold col-span-1" style="margin-right: 23%;">Name</div>
                        <div class="font-bold col-span-1" style="margin-right: 21%;">Status</div>
                        <div class="font-bold col-span-1">Amount</div>
                    </div>
                    <li v-for="item in warehouse.inventory" :key="item.id">
                        <div class="middle-part grid grid-cols-3 gap-10 py-1 move-right">
                            <p class="col-span-1">{{ getProductById(item.productId) }}</p>
                            <p class="col-span-1">{{ item.status }}</p>
                            <p class="col-span-1">{{ item.quantity }}</p>
                        </div>
                    </li>
                </ul>
            </div>

        </div>
    </div>
</template>


<script>
export default {
    inject: ["productsService"],
    name: 'UserBoardContainer',

    props: {
        loggedInUserEmail: {
            type: String,
            required: true
        },
        warehouse: {
            type: Object,
            required: true
        },
        userTeamName: {
            type: String,
            default: null
        },
        userWarehouseName: {
            type: String,  // Change the type to Number
            default: null
        },
    },
    async created() {
        this.products = await this.productsService.findAll()
    },
    data() {
        return {
            products: [],
            selectedProject: null, // Track the selected project
        };
    },
    methods: {
        getProductById(productId) {
            const product = this.products.find(product => product.id === productId);
            if (product && product.name) {
                return product.name;
            } else {
                return "Unknown Product";
            }
        },
        formatDate(dateString) {
            const date = new Date(dateString);
            return date.toISOString().split('T')[0];
        },
        toggleProject(project) {
            if (this.selectedProject === project) {
                this.selectedProject = null; // Deselect the project if it's already selected
            } else {
                this.selectedProject = project; // Select the project if it's not selected
            }
        },
    }
};
</script>


<style scoped>

.Midbar {
    display: flex;
    align-items: center;
}

.projectnamer, .inventorynamer {
    display: flex;
    align-items: center;
    justify-content: center;
    text-align: center;
    color: white;
    margin-top: 19%;
    background-color: #c7d02c;
    border-radius: 20px;
    padding: 3px;
}

.projectnamer {
    margin-left: 25%;
    width: 32%;
}

.inventorynamer{
    margin-left: 10%;
    width: 18%;
}

.Subbar {
    position: absolute;
    justify-content: space-between;
    background-color: #693101;
    border-radius: 12px;
    padding: 1%;
    z-index: -1;
    width: 70%;
    height: 60%;
    margin-top: 2%;
    margin-left: 20%;
}

.project-button.selected {
    background-color: #767e16;
}

.project-details p {
    margin-bottom: 15px; /* Adjust the bottom margin as needed to create space between the rows */
}

.project-details {
    margin-left: 10%;
    overflow-y: auto; /* Add this line to enable vertical scroll */
    width: 50%;
    margin-bottom: 30%;
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


.project-button {
    background-color: #C7D02C;
    color: #ffffff;
    border-radius: 6px;
    padding: 5px 10px;
    margin: 5px 0; /* Add margin for spacing between buttons */
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.project-list {
    height: 99%;
    background-color: rgba(241, 241, 241, 0.41);
    margin-left: 1%;
    border-radius: 8px;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 10px;
    overflow-y: auto; /* Add this line to enable vertical scroll */
}

.inventorybox {
    position: absolute;
    display: flex;
    background-color: white;
    width: 33%;
    height: 90%;
    border-radius: 12px;
    margin-top: 1%;
    margin-left: 62%;
    flex-direction: column;
}

.inventory-header {
    margin-top: 5%;
    margin-left: 5%;
}

.inventory-list {
    height: 95%; /* Set your desired max height */
    max-width: 90%;
    overflow-y: auto; /* Add this line to enable vertical scroll if necessary */
    background-color: rgba(241, 241, 241, 0.41);
    border-radius: 20px;
    margin-left: 4%;
    margin-top: 4%;
}

.inventory-list p {
    padding: 15px;
}

.icon {
    width: 48px;
    height: 48px;
}

.icon2 {
    width: 42px;
    height: 42px;
    padding: 2px;
    margin-left: 4px;
    margin-top: 4px;
}

.projectbox {
    position: absolute;
    align-items: center;
    display: flex;
    background-color: white;
    margin-top: 1%;
    width: 58%;
    height: 90%;
    border-radius: 12px;
    margin-left: 1%;
    z-index: 15;
}

.Mainbar {
    position: absolute;
    align-items: center;
    display: flex;
    justify-content: space-between;
    background-color: #c7d02c;
    border-radius: 12px;
    padding: 1%;
    z-index: -1;
    width: 70%;
    margin-top: 9%;
    margin-left: 20%;
}

.item {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 30%;
    height: 70%;
    background-color: white;
    border-radius: 12px;
}

.Mainbar h2 {
    font-size: 18px;
    color: #767e16;
}

@media screen and (max-width: 1200px) {
    .Mainbar {
        flex-direction: column;
        margin-top: 30%;
    }

    .Subbar{
        flex-direction: column;
        margin-top: 90%;
    }

    .Midbar{
        display: none;
    }

    .item {
        width: 100%;
        margin-bottom: 2%;
    }
}

</style>
