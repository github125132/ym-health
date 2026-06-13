<template>
  <div>
    <div style="padding:10px"><el-input v-model="keyword" placeholder="搜索商品" @keyup.enter="load" /></div>
    <div class="product-grid">
      <el-card v-for="p in list" :key="p.id" class="product-card" @click="goDetail(p.id)">
        <img :src="p.purl" style="width:100%;height:150px;object-fit:cover" />
        <h4>{{ p.title }}</h4>
        <div class="price">¥{{ p.basePrice }}</div>
      </el-card>
    </div>
    <el-pagination v-model:current-page="page" :total="total" @current-change="load" layout="prev,pager,next" />
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../../api/request'
const router = useRouter()
const list = ref([]), page = ref(1), total = ref(0), keyword = ref('')
const load = async () => {
  const res = await request.get('/products', { params: { page: page.value, keywords: keyword.value } })
  list.value = res.data.records; total.value = res.data.total
}
const goDetail = id => router.push(`/products/${id}`)
onMounted(load)
</script>
<style scoped>.product-grid { display: grid; grid-template-columns: repeat(2,1fr); gap: 10px; padding: 10px; }
.product-card { cursor: pointer; } .price { color: #f56c6c; font-size: 18px; font-weight: bold; }</style>
