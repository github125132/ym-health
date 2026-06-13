<template>
  <div v-if="product">
    <img :src="product.purl" style="width:100%;height:300px;object-fit:cover" />
    <div style="padding:15px">
      <h2>{{ product.title }}</h2>
      <div class="price">¥{{ product.basePrice }}</div>
      <div v-if="product.specs" style="margin:10px 0">
        <el-tag v-for="s in specList" :key="s" :type="selectedSpec===s?'primary':''"
                @click="selectedSpec=s" style="margin:3px;cursor:pointer">{{ s }}</el-tag>
      </div>
      <el-input-number v-model="quantity" :min="1" :max="product.stock" />
      <div style="margin-top:15px;display:flex;gap:10px">
        <el-button type="warning" @click="addToCart">加入购物车</el-button>
        <el-button type="danger" @click="buyNow">立即购买</el-button>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '../../api/request'
import { ElMessage } from 'element-plus'
const route = useRoute(), router = useRouter()
const product = ref(null), quantity = ref(1), selectedSpec = ref('')
const specList = computed(() => product.value?.specs ? product.value.specs.split(',') : [])
onMounted(async () => {
  const res = await request.get(`/products/${route.params.id}`)
  product.value = res.data
})
const addToCart = async () => {
  await request.post('/cart', null, { params: { productId: product.value.id, quantity: quantity.value, spec: selectedSpec.value } })
  ElMessage.success('已加入购物车')
}
const buyNow = () => router.push('/cart')
</script>
<style scoped>.price { color: #f56c6c; font-size: 24px; font-weight: bold; }</style>
