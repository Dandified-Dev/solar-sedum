<template>
  <div>
    <div class="main">
      <UserTable :users="users" @addUser="addUser" @deleteUser="deleteUser" @editUser="editUser"/>
    </div>
  </div>
</template>

<script>
import UserTable from "@/components/users/UserTable.vue";
import confirmDialog from "@/models/confirmDialog";

export default {
  inject:['usersService'],
  components:{
    UserTable
  },
  data() {
    return{
      users: []
    }
  },
  async created(){
    this.users = await this.usersService.findAll();
  },
  methods:{
    async addUser(newUser){
      await this.usersService.add(newUser);
      this.users = await this.usersService.findAll();
    },
    async deleteUser(user){
      await confirmDialog.showConfirmationDialog((user.firstName + " " + user.lastName), async () =>{
        await this.usersService.deleteById(user.id)
      })
    },
    async editUser(user, teamId){
      await this.usersService.save(user, teamId)
      this.users = await this.usersService.findAll();
    }
  }
};
</script>

<style scoped>
.main {
  margin-top: 9%;
  position: absolute;
  z-index: -1;
  margin-left: 21%;
  width: 69%;
  border-radius: 12px;
  flex-direction: column;
  display: flex;
  align-items: center;
  min-height: 30rem;
  text-align: center;
}
</style>
