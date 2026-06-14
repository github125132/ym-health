<template>
  <div class="app-container">
    <router-view />
    <BottomNav v-if="showNav" />
  </div>
</template>
<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import BottomNav from './components/BottomNav.vue'
const route = useRoute()
const showNav = computed(() => !route.path.startsWith('/admin'))
</script>
<style>
body { margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; -webkit-font-smoothing: antialiased; }
.app-container { padding-bottom: 56px; max-width: 750px; margin: 0 auto; }
</style>
