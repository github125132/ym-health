import { defineStore } from 'pinia'
import request from '../api/request'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('access_token') || '',
    user: null
  }),
  getters: {
    isLoggedIn: state => !!state.token,
    isAdmin: state => state.user?.type === 'ADMIN'
  },
  actions: {
    async login(phone, password) {
      const res = await request.post('/auth/login', { phone, password })
      this.token = res.data.accessToken
      localStorage.setItem('access_token', this.token)
    },
    logout() {
      this.token = ''
      this.user = null
      localStorage.removeItem('access_token')
    }
  }
})
