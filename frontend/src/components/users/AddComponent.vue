<template>
  <form class="bg-gray-50 border border-gray-300 text-gray-900 ml-4 p-2 rounded-2xl mr-5">
    <button @click="handleForm">Add User</button>
  </form>
</template>

<script>
import Swal from "sweetalert2";

export default {
  name: "AddComponent",
  inject: ["teamsService", "usersService"],
  emits: ['addUser'],
  data() {
    return {
      teams: [],
      selectedTeamId: null,
      newUser: {
        firstName: '',
        lastName: '',
        email: '',
        teamId: null
      },
    };
  },
  async created() {
    this.teams = await this.teamsService.findAll();
  },
  methods: {
    async handleForm() {
      const { value: formValues } = await Swal.fire({
        html: `
          <label class="flex justify-start mt-4 text-black">First Name: </label>
          <input id="swal-input1" class="mb-4 p-2 border border-gray-300 rounded w-full">

          <label class="flex justify-start text-black">Last Name: </label>
          <input id="swal-input2" class="mb-4 p-2 border border-gray-300 rounded w-full">

          <label class="flex justify-start text-black">Email: </label>
          <input id="swal-input3" class="mb-4 p-2 border border-gray-300 rounded w-full">

          <label class="flex justify-start text-black">Team: </label>
          <select id="swal-input4" class="p-2 mb-4 w-full flex justify start border-solid border border-gray-300 mt-2">
            <option value="" disabled selected>Select Team</option>
            ${this.teams.map((team) => `<option value="${team.id}">${team.name}</option>`).join("")}
          </select>
        `,

        showCancelButton: true,
        cancelButtonText: "Cancel",
        cancelButtonColor: "#ef4444",
        confirmButtonText: "Add User",
        confirmButtonColor: "#22c55e",
        reverseButtons: true,
        width: 320,

        preConfirm: async () => {
          const firstName = document.getElementById("swal-input1").value.trim();
          const lastName = document.getElementById("swal-input2").value.trim();
          const email = document.getElementById("swal-input3").value.trim();
          const selectedTeamId = document.getElementById("swal-input4").value;

          if (!firstName || !lastName || !email) {
            Swal.showValidationMessage(`First Name, Last Name, and Email are required`);
            return false;
          }

          // Validate email format
          if (!this.isValidEmail(email)) {
            Swal.showValidationMessage(`Invalid email format`);
            return false;
          }

          if (!await this.checkEmailAvailiblity(email)) {
            console.log("test")
            Swal.showValidationMessage(`Email is taken`);
            return false;
          }

          this.newUser.firstName = firstName;
          this.newUser.lastName = lastName;
          this.newUser.email = email;
          this.newUser.teamId = parseInt(selectedTeamId);
          // this.selectedTeamId = parseInt(selectedTeamId);
        },
      });

      if (formValues) {
        this.$emit('addUser', this.newUser);
      }
    },
    // Helper function to validate email format
    isValidEmail(email) {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return emailRegex.test(email);
    },
    async checkEmailAvailiblity(email){
      return await this.usersService.checkEmail(email);
    }
  },
};

</script>

<style scoped>
</style>
