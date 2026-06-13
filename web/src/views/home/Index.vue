<template>
  <div>
    <el-carousel v-if="ads.length" height="200px">
      <el-carousel-item v-for="ad in ads" :key="ad.id">
        <img :src="ad.imageUrl" style="width:100%;height:200px;object-fit:cover" />
      </el-carousel-item>
    </el-carousel>
    <div class="product-grid">
      <el-card v-for="p in products" :key="p.id" class="product-card" @click="goDetail(p.id)">
        <img :src="p.purl" style="width:100%;height:150px;object-fit:cover" />
        <h4>{{ p.title }}</h4>
        <div class="price">¥{{ p.basePrice }}</div>
      </el-card>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../../api/request'
const router = useRouter()
const ads = ref([]), products = ref([])
onMounted(async () => {
  const adRes = await request.get('/ads?position=shouye')
  ads.value = adRes.data || []
  const prodRes = await request.get('/products')
  products.value = prodRes.data?.records || []
})
const goDetail = id => router.push(`/products/${id}`)
</script>
<style scoped>
.product-grid { display: grid; grid-template-columns: repeat(2,1fr); gap: 10px; padding: 10px; }
.product-card { cursor: pointer; }
.price { color: #f56c6c; font-size: 18px; font-weight: bold; }
</style>
