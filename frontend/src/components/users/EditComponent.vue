<template>
  <form class="h-5">
    <button @click="handleForm">
      <img width="25" src="@/assets/edit_icon.png">
    </button>
  </form>
</template>

<script>
import Swal from "sweetalert2";

export default {
  inject: ["teamsService"],
  props: ['user'],
  emits: ['editUser'],
  data() {
    return {
      teams: [],
      selectedTeamId: null,
      editedUser: {
        id: '',
        firstName: '',
        lastName: '',
        email: '',
      },
    };
  },
  async created() {
    this.teams = await this.teamsService.findAll();
  },
  methods: {
    async handleForm() {
      const {value: formValues} = await Swal.fire({
        html: `
      <label class="flex justify-start mt-4 text-black">First Name: </label>
      <input id="swal-input1" class="mb-4 p-2 border border-gray-300 rounded w-full" value="${this.user.firstName}">

      <label class="flex justify-start text-black">Last Name: </label>
      <input id="swal-input2" class="mb-4 p-2 border border-gray-300 rounded w-full" value="${this.user.lastName}">

      <label class="flex justify-start text-black">Email: </label>
      <input id="swal-input3" class="mb-4 p-2 border border-gray-300 rounded w-full" value="${this.user.email}">

      <label class="flex justify-start text-black">Team: </label>
      <select id="swal-input4" class="p-2 mb-4 w-full flex justify-start border-solid border border-gray-300 mt-2">
        <option value="" disabled ${this.user.teamId === null ? 'selected' : ''}>Select Team</option>
        ${this.teams.map((team) => `<option value="${team.id}" ${team.id === this.user.teamId ? 'selected' : ''}>${team.name}</option>`).join("")}
      </select>
    `,

        showCancelButton: true,
        cancelButtonText: "Cancel",
        cancelButtonColor: "#ef4444",
        confirmButtonText: "Add User",
        confirmButtonColor: "#22c55e",
        reverseButtons: true,
        width: 320,


        preConfirm: () => {
          this.editedUser.id = this.user.id
          this.editedUser.firstName = document.getElementById("swal-input1").value;
          this.editedUser.lastName = document.getElementById("swal-input2").value;
          this.editedUser.email = document.getElementById("swal-input3").value;
          const selectedTeamId = document.getElementById("swal-input4").value;
          this.selectedTeamId = selectedTeamId === '' ? null : parseInt(selectedTeamId);
        },
      });

      if (formValues) {
        this.$emit('editUser', this.editedUser, this.selectedTeamId);
      }
    },
  },
};
</script>
<style scoped>

</style>
