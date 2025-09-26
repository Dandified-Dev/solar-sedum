<template>
  <img src="@/assets/solar_sedum.png" alt="" class="logo-login">
  <div class="login-container">
    <h1 class="login-header">Login</h1>
    <p class="login-text">Enter your Credentials to access your account</p>

    <form @submit.prevent="login" class="login-form">
      <div class="form-group">
        <label for="username" class="form-label ">Email:</label>
        <input type="text" placeholder="Enter your username" id="username" v-model="email" class="form-input"/>
      </div>

      <div class="form-group">
        <label for="password" class="form-label">Password:</label>
        <input :type="passwordFieldType" placeholder="Enter your password" id="password" v-model="password"
               class="form-input"/>
        <button type="button" @click="switchVisibility" class="show-password-button">
          {{ passwordFieldType === 'password' ? 'Show password' : 'Hide password' }}
        </button>
      </div>

      <button type="submit" class="login-button">Login</button>
      <div v-if="error" class="error-message">{{ errorMessage }}</div>
    </form>

    <div class="right-image">
      <img src="@/assets/login_bg3.png" alt="image"/>
    </div>
  </div>
  <img src="@/assets/style_circle_3.png" alt="" class="left_bottom_circle ">
</template>

<script>

export default {
  name: "LoginComponent",
  inject: ['usersService'],
  data() {
    return {
      email: "",
      password: "",
      passwordFieldType: "password",
      error: false,
      errorMessage: "",
    };
  },
  methods: {
    async login(value) {

      try {
        // Reset error state
        this.error = false;
        this.errorMessage = "";

        // Validate input fields
        if (this.email.trim() === "" || this.password.trim() === "") {
          this.error = true;
          this.errorMessage = "Email or password can't be empty.";
          return;
        }

        const response = await this.usersService.authenticate(this.email, this.password);

        if (response.success) {
          // Store the token in localStorage
          localStorage.setItem(response.token, value);

          this.$router.push("/dashboard");

        } else {
          console.error("Login failed for email:", this.email, "Message:", response.message);
          this.error = true;
          this.errorMessage = response.message || "Login failed.";
        }
      } catch (error) {
        console.error("Error during authentication:", error);
        this.error = true;
        this.errorMessage = "An unexpected error occurred during login.";
      }
    },

    switchVisibility() {
      this.passwordFieldType = this.passwordFieldType === "password" ? "text" : "password";
    },
  },
};
</script>

<style scoped>
.error-message {
  color: red;
  font-size: 14px;
  margin-top: 10px;
  text-align: center;
}

.login-container {
  max-width: 350px;
  margin-top: 5%;
  margin-left: 15%;
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  overflow-y: hidden;
}

.login-header {
  font-size: 40px;
  font-weight: bold;
  text-align: center;
  color: black;
}

.logo-login {
  margin-top: 4%;
  margin-left: 16.2%;
}


.login-form {
  margin-top: 20px;
}

.login-text {
  font-size: 14px;
  margin-bottom: 15px;
  color: black;
}

.form-group {
  margin-bottom: 15px;
}

.form-label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-input {
  background-color: transparent;
  width: 100%;
  padding: 10px;
  border-bottom: 1px solid #ccc;
  border-radius: 3px;
}

.show-password-button {
  margin-top: 10px;
  background: none;
  border: none;
  cursor: pointer;
  text-decoration: underline;
}

.login-button {
  margin-top: 10px;
  width: 100%;
  padding: 10px;
  background-color: #572700;
  color: #fff;
  border: none;
  border-radius: 30px;
  cursor: pointer;
}

.login-button:hover {
  background-color: #703507;
}

.left_bottom_circle {
  position: fixed;
  bottom: 0;
  left: 0;
  height: 200px;
}

.right-image {
  position: fixed;
  bottom: 0;
  right: 0;
  max-width: 50%; /* Hiermee bepaal je de maximale breedte van de afbeelding */
  z-index: -1; /* Optioneel, om de afbeelding naar de achtergrond te verplaatsen */
}


@media only screen and (max-width: 1100px) {

  .right-image{
    display: none;
  }

}

@media only screen and (max-width: 600px) {

  .logo-login {
    margin-top: 15%;
    margin-left: 10%;
  }

  .login-container {
    margin-left: 5%;
  }


}



</style>
