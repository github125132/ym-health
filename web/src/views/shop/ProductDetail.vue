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
    <el-divider />
    <div style="padding:0 15px 15px">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="商品详情" name="detail">
          <div v-html="product.memo" style="line-height:1.8"></div>
        </el-tab-pane>
        <el-tab-pane :label="`商品评价(${reviewTotal})`" name="review">
          <div v-if="reviews.length">
            <div v-for="r in reviews" :key="r.id" style="border-bottom:1px solid #f0f0f0;padding:12px 0">
              <div style="display:flex;align-items:center;gap:8px;margin-bottom:4px">
                <span style="font-weight:bold">{{ r.userId }}</span>
                <el-rate :model-value="r.rating" disabled size="small" />
              </div>
              <div style="color:#666;font-size:14px">{{ r.content }}</div>
              <div style="color:#999;font-size:12px;margin-top:4px">{{ formatDate(r.createdAt) }}</div>
            </div>
          </div>
          <div v-else style="color:#999;text-align:center;padding:30px 0">暂无评价</div>
        </el-tab-pane>
      </el-tabs>
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
const activeTab = ref('detail')
const reviews = ref([]), reviewTotal = ref(0)
const specList = computed(() => product.value?.specs ? product.value.specs.split(',') : [])
const formatDate = d => d ? d.substring(0, 10) : ''
const loadReviews = async () => {
  const res = await request.get('/reviews', { params: { productId: route.params.id, page: 1, pageSize: 10 } })
  reviews.value = res.data.records
  reviewTotal.value = res.data.total
}
onMounted(async () => {
  const res = await request.get(`/products/${route.params.id}`)
  product.value = res.data
  loadReviews()
})
const addToCart = async () => {
  await request.post('/cart', null, { params: { productId: product.value.id, quantity: quantity.value, spec: selectedSpec.value } })
  ElMessage.success('已加入购物车')
}
const buyNow = () => router.push('/cart')
</script>
<style scoped>.price { color: #f56c6c; font-size: 24px; font-weight: bold; }</style>
