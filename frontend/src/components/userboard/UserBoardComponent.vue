<template>
    <UserBoardContainer
            :loggedInUserEmail="loggedInUserEmail"
            :warehouse="warehouse"
            :userTeamName="userTeamName"
            :userWarehouseName="userWarehouseName"
    />
</template>

<script>
import UserBoardContainer from "@/components/userboard/UserBoardContainer.vue";
import jwtDecode from 'jwt-decode';

export default {
    name: "UserBoardComponent",
    inject:['usersService', 'teamsService', 'warehousesService'],
    components: {UserBoardContainer},
    data() {
        return {
            loggedInUserEmail: "",
            warehouse: [],
            userTeamName: null,
            userWarehouseName: null,
        };
    },
    async created() {
        const token = localStorage.getItem("token");
        if (token) {
            this.extractUserInfoFromToken(token);
            await this.fetchUserData();
        } else {
            console.log("No token found in localStorage");
        }
    },
    methods: {
        extractUserInfoFromToken(token) {
            try {
                const decodedToken = jwtDecode(token);

                if (!decodedToken || typeof decodedToken !== 'object') {
                    console.error("Decoded token is not an object in UserBoardComponent:", decodedToken);
                    return;
                }

                if (!decodedToken.sub) {
                    console.error("Decoded token does not contain 'sub' property in UserBoardComponent:", decodedToken);
                    return;
                }

                this.loggedInUserEmail = decodedToken.sub;
                console.log("Decoded email in UserBoardComponent:", this.loggedInUserEmail);
            } catch (error) {
                console.error("Error decoding token in UserBoardComponent:", error.message);
            }
        },

        async fetchUserData() {
            try {
                // Verander de methode om de gegevens van de ingelogde gebruiker op te halen
                const loggedInUser = await this.usersService.findByEmail(this.loggedInUserEmail);
                const userTeam = await this.teamsService.findById(loggedInUser.teamId)
                const userWarehouse = await this.warehousesService.findById(userTeam.warehouseId)

                // Gebruik de gegevens van de ingelogde gebruiker om de nodige velden in te vullen
                this.userTeamName = userTeam.name;
                this.userWarehouseName = userWarehouse.name;
                this.warehouse = userWarehouse;

                console.log("warehouse:", this.warehouse);


            } catch (error) {
                console.error("Error fetching user data:", error.message);
            }
        },
    },
};
</script>

<style scoped>
</style>
