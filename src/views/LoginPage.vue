<template>
  <video autoplay loop muted class="background-video">
    <source src="../assets/Basketball_CS2.mp4" type="video/mp4">
    您的浏览器不支持视频播放。
  </video>
  <div class="login-page">
    <div class="login-form-container">
      <el-card class="login-form" shadow="hover" style="border: 2px solid #805e73; border-radius: 30px">
        <h2 class="login-title">Login</h2>
        <el-form :model="loginFormModel"
                 :rules="rules"
                 ref="form"
                 class="form"
                 autocomplete="off">
          <el-form-item prop="username">
            <el-input
                v-model="loginFormModel.username"
                placeholder="username"
                :prefix-icon="User"
            ></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
                v-model="loginFormModel.password"
                type="password"
                placeholder="Password"
                :prefix-icon="Lock"
                show-password
            ></el-input>
          </el-form-item>
          <el-form-item>
            <div style="width: 100%">
              <a href="#"
                 class="forgot-password"
                 style="float: right;
               font-size: 12px;
               color: #5e1d41;">Forgot Password?</a>
            </div>
          </el-form-item>
          <el-form-item>
            <el-button class="login-button" type="primary" @click="handleLogin">
              Login
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { Lock, User } from '@element-plus/icons-vue';
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import { useUserStore } from "../stores/user.js";
import { userLoginService } from "../api/user.js";

const form = ref()
const loginFormModel = ref({
  username: '',
  password: '',
});

// 验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户id', trigger: 'blur' },
    { min: 5, max: 10, message: 'id必须是 5-10位 的字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    {
      pattern: /^\S{6,15}$/,
      message: '密码必须是 6-15位 的非空字符',
      trigger: 'blur'
    },
  ]
};

// 登录处理函数
const userStore = useUserStore() // 用户仓库
const router = useRouter()
const handleLogin = async () => {
  await form.value.validate() // 点击登录后等待再一次校验完成
  console.log('LoginPage.vue： loginFormModel.value.username: ' + loginFormModel.value.username + 'password: ' + loginFormModel.value.password)
  const res = await userLoginService(loginFormModel.value) // 发送登录表单内容
  userStore.setToken(res.data.token) // 接收后端的token来设置当前用户的token
  userStore.setUser(res.data)
  console.log('LoginPage.vue： res.data.message: ' + res.data.message)
  ElMessage.success(res.data.message)
  await router.push('/')
};
</script>

<style lang="scss" scoped>
.login-page {
  height: 100vh;
  //background-color: #c48db0;
  //background-image: url("../assets/bg1.jpg");
  //background-size: cover;
  //background-position: center;
  overflow: hidden;// 为视频背景加的，好像不影响原图片背景
  position: relative;// 同上
  display: flex;
  justify-content: center;
  align-items: center;
}

.background-video {
  position: absolute;
  top: 0;
  //left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: -1;
}

.login-form-container {
  width: 350px;
  max-width: 90%;
  z-index: 1;
}

.login-form {
  padding: 20px;
  //background-color: rgba(241, 167, 214, 0.6);
  background-color: rgb(168, 159, 134, 0.6);
  border-radius: 15px;
}

.login-title {
  text-align: center;
  font-size: 30px;
  margin-bottom: 30px;
  color: #9f7390;
}

.forgot-password {
}

.el-input::placeholder {
  color: #bbb;
}

.login-button {
  width: 100%;
  background: linear-gradient(45deg, #e5a0dd, #d35e94);
  color: #ffffff;
  transition: background 0.3s;
  border-radius: 12px;
}

.login-button:hover {
  background: linear-gradient(45deg, #38a1db, #42b883);
}
</style>
