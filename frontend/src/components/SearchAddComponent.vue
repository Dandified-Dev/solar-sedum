<template>
    <div class="top">
        <div class="maind">
            <div class="search_container">
                <img src="@/assets/search_icon.png" alt="search_icon" id="search_icon">
                <input v-model="search" type="text" :placeholder="searchPlaceholder" id="input_field" @input="handleSearchInput">
            </div>
            <div class="button" @click="handleAddElementClicked">
                <img src="@/assets/add_icon.png" alt="add_icon" id="add_icon">
                <p>{{ addButtonLabel }}</p>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: "TeamTopComponent",
    emits: ['search', 'add-element-clicked'], // Changed event name
    props: {
        searchPlaceholder: String,
        addButtonLabel: String
    },
    data() {
        return {
            search: ''
        }
    },
    methods: {
        handleSearchInput() {
            this.$emit('search', this.search);
        },
        handleAddElementClicked() {
            this.$emit('add-element-clicked', true); // Emitting the new event
        }
    }
}
</script>


<style scoped>
img{
    margin-right: 3rem;
}
.top{
    display: flex;
    align-items: center;
    margin-left: 17%;
    background: #f4f4f4;
    height: 6rem;
    width: 100%;
    position: fixed;
    overflow: hidden;
    margin-top: 6rem;
    box-sizing: unset !important;
}
.top::before, ::after{
    box-sizing: unset !important;
}
.maind{ 
    margin-left: 4%;
    position: absolute;
    height: 4rem;
    align-items: center;
    display: flex;
    background-color: #572700;
    width: 69%;
    border-radius: 12px;
    box-sizing: unset !important;
}
.maind::before, ::after{
    box-sizing: unset !important;
}
.search_container{
    align-items: center;
    background-color: white;
    height: 70%;
    display: flex;
    margin-left: 2rem;
    margin-right: 3rem;
    width: 70%;
    border-radius: 6px;
}
#search_icon{
    padding-left: 1.5rem;
    padding-right: 1.5rem;
    padding-top: 0.4rem;
    padding-bottom: 0.4rem;
    border-right: #e6e6e6 1px solid;
    cursor: pointer;
}
#input_field{
    border: 0;
    width: 37rem;
    font-size: large;
}
::-webkit-input-placeholder{
    color: #C7D02C;
}
text:focus, input:focus{
    outline: none;
    font-size: large;
}
#add_icon{
    margin-left: 1rem;
}
.button{
    cursor: pointer;
    color: white;
    display: flex;
    background-color: #5ada3a;
    height: 70%;
    border-radius: 6px;
    align-items: center;
    width: 20%;
}

</style>