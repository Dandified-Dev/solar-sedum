<template>
    <div class="top">
        <div class="maind">
            <!-- "All Warehouses" button -->
            <div
                class="allbutton"
                :class="{ 'selected': selectedTitle === 'All Warehouses' }"
                @click="handleAddElementClicked('All Warehouses')"
            >
                <div class="button-content">
                    <img src="@/assets/ChartBar.png" alt="Icon" class="icon" />
                    <p class="button-title">All Warehouses</p>
                </div>
            </div>

            <!-- Existing buttons loop -->
            <div
                class="button"
                v-for="(title, index) in dashboardTitles"
                :key="title"
                :class="{ 'selected': selectedTitle === title }"
                @click="handleAddElementClicked(title)"
                v-show="index < visibleButtonsCount"
            >
                <div class="button-content">
                    <img src="@/assets/warehouse.png" alt="Icon" class="icon" />
                    <p class="button-title">{{ title }}</p>
                </div>
            </div>

            <!-- "More" button with dropdown -->
            <div class="button more-button" @click="toggleDropdown">
                <img src="@/assets/Stack.png" alt="More" class="icon3" />
                <p class="button-title">More</p>
                <div v-if="showDropdown" class="dropdown">
                    <!-- Display hidden buttons in the dropdown -->
                    <div
                        class="button"
                        v-for="(title, index) in dashboardTitles"
                        :key="title"
                        :class="{ 'selected': selectedTitle === title }"
                        @click="handleAddElementClicked(title)"
                        v-show="index >= visibleButtonsCount"
                    >
                        <div class="button-content">
                            <img src="@/assets/warehouse.png" alt="Icon" class="icon" />
                            <p class="button-title">{{ title }}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>


<script>
export default {
    name: "SearchDashboardComponent",
    emits: ["dashboard-selected", "all-warehouses-selected"],
    props: {
        dashboardTitles: Array,
    },
    data() {
        return {
            search: "",
            selectedTitle: "All Warehouses", // Set default selectedTitle
            visibleButtonsCount: 4,
            showDropdown: false,
        };
    },
    created() {
        // Emit the event for "All Warehouses" when the component is created
        this.$emit("all-warehouses-selected");
    },
    methods: {
        handleAddElementClicked(title) {
            this.selectedTitle = title;
            if (title === "All Warehouses") {
                this.$emit("all-warehouses-selected");
            } else {
                this.$emit("dashboard-selected", title);
            }
        },
        toggleDropdown() {
            this.showDropdown = !this.showDropdown;
        },
    },
};
</script>


<style scoped>

.icon {
    margin-right: 3rem;
}

.icon {
    margin: 10px;
    width: 15%;
}

.icon3{
    margin: 10px;
    width: 25%;
}

.top {
    display: flex;
    align-items: center;
    margin-left: 17%;
    background: #f4f4f4;
    height: 6rem;
    width: 100%;
    position: fixed;
    margin-top: 6rem;
}

.button-content {
    display: flex; /* Make the image and text display in a row */
    align-items: center; /* Align them vertically in the center */
}

.maind {
    margin-left: 4%;
    position: absolute;
    height: 4rem;
    align-items: center;
    display: flex;
    background-color: #ffffff;
    width: 69%;
    border-radius: 12px;
    flex-direction: row; /* Set the direction to row (default, just for clarification) */
    max-height: 4rem; /* Set a maximum height to enable vertical overflow */
}

.maind::before,
.maind::after {
    box-sizing: unset !important;
}

::-webkit-input-placeholder {
    color: #C7D02C;
}

text:focus,
input:focus {
    outline: none;
    font-size: large;
}

.button {
    cursor: pointer;
    color: #ffffff;
    display: flex;
    background-color: #C7D02C;
    height: 70%;
    border-radius: 6px;
    align-items: center;
    margin: 10px;
}

.button:hover {
    background-color: #767e16;
}

.button.selected {
    background-color: #767e16;
}

.button-title {
    font-weight: bold;
}

.allbutton {
    cursor: pointer;
    color: #ffffff;
    display: flex;
    background-color: #753600;
    height: 70%;
    border-radius: 6px;
    align-items: center;
    margin: 10px;
}

.allbutton:hover {
    background-color: #4f2700;
}

.allbutton.selected {
    background-color: #4f2700;
}

/* Styles for the dropdown */
.more-button {
    cursor: pointer;
}

.dropdown {
    display: flex;
    flex-wrap: wrap;
    background-color: #ffffff;
    border-radius: 12px;
    position: absolute;
    top: 110%;
    left: 0;
    z-index: 2;
}

.dropdown .button {
    margin: 10px;
}

.dropdown .button:hover {
    background-color: #767e16;
}

/* Add media query for smaller screens */
@media screen and (max-width: 1200px) {
  .top {

    align-items: stretch;
    height: auto;
  }

  .maind {
    width: 70%;
      margin-top: 2%;
  }

  .icon {
      display: none;
  }

  .icon3 {
      display: none;
  }

  .button {
    width: 20%;
    margin: 10px 5px;
      padding: 10px;
  }

  .dropdown {
    width: 100%;
    left: unset;
  }
}


</style>
