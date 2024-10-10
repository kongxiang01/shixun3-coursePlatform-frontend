<template>
  <div class="login-page">
    <div class="login-form-container">
      <el-card class="login-form" shadow="hover" style="border: 2px solid #805e73; border-radius: 30px">
        <h2 class="login-title">Login</h2>
        <el-form :model="loginFormModel"
                 :rules="rules"
                 ref="form"
                 class="form"
                 autocomplete="off">
          <el-form-item prop="sno">
            <el-input
                v-model="loginFormModel.sno"
                placeholder="sno"
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
            <el-checkbox v-model="loginFormModel.rememberMe">Remember me</el-checkbox>
            <a href="#" class="forgot-password" style="margin-left: 40px">Forgot Password?</a>
          </el-form-item>
          <el-form-item>
            <el-button class="login-button" type="primary" @click="handleLogin">
              Login
            </el-button>
          </el-form-item>
          <div class="register-link">
            Don't have an account? <a href="#">Register</a>
          </div>
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
import {useUserStore} from "../stores/user.js";
import {userLoginService} from "../api/user.js";

const form = ref()
const loginFormModel = ref({
  sno: '',
  password: '',
  rememberMe: false,
});

// 验证规则
const rules = {
  sno: [
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
  await form.value.validate()// 点击登录后等待再一次校验完成
  ElMessage.info('111111')
  console.log('loginFormModel.value: ' + loginFormModel.value.sno + 'password: ' + loginFormModel.value.password)
  const res = await userLoginService(loginFormModel.value) // 发送登录表单内容
  ElMessage.info('22222')
  userStore.setToken(res.data.token) // 接收后端的token来设置当前用户的token
  // userStore.setToken(loginFormModel.value.sno) // 暂时代替
  console.log('res.data.message): ' + res.data.message)
  // ElMessage.success('登录成功')
  await router.push('/')
};
</script>

<style lang="scss" scoped>
.login-page {
  height: 100vh;
  background-color: #c48db0;
  background-image: url("../assets/bg1.jpg");
  background-size: cover;
  background-position: center;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-form-container {
  width: 350px;
  max-width: 90%;
}

.login-form {
  padding: 20px;
  background-color: rgba(241, 167, 214, 0.6);
  border-radius: 15px;
}

.login-title {
  text-align: center;
  font-size: 30px;
  margin-bottom: 30px;
  color: #9f7390;
}

.forgot-password {
  float: right;
  font-size: 12px;
  color: #5e1d41;
}

.el-input {

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

.register-link {
  text-align: center;
  margin-top: 10px;
  color: #fff;
}

.register-link a {
  color: #42b883;
  margin-left: 20px;
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}
</style>


