<script>
export default {
  emits: ['search'],
  methods: {
    search(e) {
      this.$emit('search', e.target.value);
    }
  }
}
</script>

<template>
  <form class="py-3 px4 flex items-center">
    <label class="sr-only">Search</label>
    <div class="relative w-full flex">
      <input @input="search" class="bg-gray-50 border border-gray-300 text-gray-900 ml-4 p-2 rounded-2xl" type="text" placeholder="Search">
    </div>
  </form>
</template>

<style scoped>

</style>