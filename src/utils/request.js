import axios from 'axios'
import { useUserStore } from '../stores/user.js'
import { ElMessage } from 'element-plus'
import { useRouter } from "vue-router"
const baseURL = 'http://192.168.10.124:8080'

const instance = axios.create({
  // TODO 1. 基础地址，超时时间
  baseURL,
  timeout: 10000
})

// 请求拦截器
instance.interceptors.request.use(
  (config) => {
    // TODO 2. 携带token
    const userStore = useUserStore()
      console.log('request.js')
    if (userStore.token) {
      config.headers.Authorization = userStore.token
        console.log('request.js：成功携带token：' + userStore.token)
    }
    return config
  },
  (err) => Promise.reject(err)
)

// 响应拦截器
instance.interceptors.response.use(
  (res) => {
    // TODO 4. 摘取核心响应数据
      console.log('request.js  4444444')
    if (res.data.status === 'success') {
      return res
    }
    // TODO 3. 处理业务失败
    // 处理业务失败, 给错误提示，抛出错误
    ElMessage.error(res.data.message || '服务异常')
    return Promise.reject(res.data)
  },
  (err) => {
    // TODO 5. 处理401错误
    // 错误的特殊情况 => 401 权限不足 或 token 过期 => 拦截到登录
    if (err.response?.status === 401) {
        const router = useRouter()
      router.push('/login')
    }

    // 错误的默认情况 => 只要给提示
    ElMessage.error(err.response.data.message || '未知异常')
    return Promise.reject(err)
  }
)

export default instance
export { baseURL }
