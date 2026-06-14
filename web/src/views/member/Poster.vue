<template>
  <div class="poster-page">
    <div class="poster-card">
      <div class="poster-header">有梦健康</div>
      <div class="poster-qr">
        <img :src="qrUrl" alt="二维码" />
      </div>
      <div class="poster-text">扫码注册，开启健康之旅</div>
    </div>
  </div>
</template>
<script setup>
import { computed } from 'vue'
import { useAuthStore } from '../../stores/auth'
const auth = useAuthStore()
const qrUrl = computed(() =>
  `https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=${encodeURIComponent(window.location.origin + '/register?ref=' + (auth.user?.userId || ''))}`
)
</script>
<style scoped>
.poster-page { display: flex; justify-content: center; padding: 20px; background: linear-gradient(135deg, #667eea, #764ba2); min-height: 100vh; }
.poster-card { background: #fff; border-radius: 15px; padding: 30px; text-align: center; width: 320px; margin-top: 40px; }
.poster-header { font-size: 24px; font-weight: bold; color: #333; margin-bottom: 20px; }
.poster-qr img { width: 200px; height: 200px; }
.poster-text { color: #666; margin-top: 15px; }
</style>
