<template>
  <div class="relative flex items-center w-full px-4">
    <button @click="show = !show" class="w-full flex items-center justify-center py-2 px-4 text-sm font-medium text-gray-900">
      Filter
    </button>
    <div v-if="show" class="absolute top-12 right-0 z-10 w-48 p-3 bg-white rounded-lg shadow flex flex-col items-baseline">
      <h6 class="mb-3 text-sm text-gray-900 font-semibold">Status</h6>
      <ul class="space-y-2 text-sm flex flex-col items-baseline">
        <li v-for="(status, index) in statuses" :key="index">
          <input :id="`filter_option_${index}`" @change="filter" type="checkbox" :value="status" class="w4 h4 bg-gray-300 rounded text">
          <label :for="`filter_option_${index}`" class="ml-2 text-sm font-medium text-gray-900">{{status}}</label>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>

export default {
  props: ['projects'],
  data() {
    return {
      show: false,
    };
  },
  computed: {
    statuses() {
      return [...new Set(this.projects.map(project => project.status))];
    },
  },
  methods: {
    filter(e) {
      this.$emit('filter', e.target.value);
    },
  },
};
</script>


<style scoped>

</style>