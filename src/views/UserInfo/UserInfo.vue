<template>
  <div class="bg">
    <el-row style="height: 50px"></el-row>
    <el-row type="flex" justify="center" align="middle">
      <el-col :span="8">
        <el-card class="info-card">
          <el-row class="avatar">
            <el-avatar size="large" :src="userInfo.avatar" />
          </el-row>
          <el-row justify="space-between" align="middle">
            <el-col span="12">
              <span class="info-title">{{ userInfo.type === '0' ? '学生信息' : '老师信息' }}</span>
            </el-col>
            <el-col span="12">
              <el-button type="primary" @click="openDialog">修改密码</el-button>
              <el-dialog v-model="dialogVisible" title="修改密码">
                <el-form :model="form" ref="formRef">
<!--                  <el-form-item label="原密码" prop="oldPassword" :rules="[{ required: true, message: '请输入原密码' }]">-->
<!--                    <el-input v-model="form.oldPassword" type="password" placeholder="请输入原密码"></el-input>-->
<!--                  </el-form-item>-->
                  <el-form-item label="新密码" prop="newPassword" :rules="[{ required: true, message: '请输入新密码' }]">
                    <el-input v-model="form.newPassword" type="password" placeholder="请输入新密码"></el-input>
                  </el-form-item>
                  <el-form-item label="确认新密码" prop="confirmPassword" :rules="[{ required: true, message: '请确认新密码' }]">
                    <el-input v-model="form.confirmPassword" type="password" placeholder="请确认新密码"></el-input>
                  </el-form-item>
                </el-form>
                <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleChangePassword">确 定</el-button>
      </span>
              </el-dialog>
            </el-col>
          </el-row>

          <el-divider></el-divider>

          <el-row>
            <el-col :span="12">
              <!-- 根据 userInfo.type 动态显示学生或老师的信息 -->
              <table v-if="userInfo.type === '0'"> <!-- 学生信息 -->
                <tbody>
                <tr class="info-row">
                  <th class="info-left">姓名</th>
                  <td>{{ userInfo.studentName }}</td>
                </tr>
                <tr class="info-row">
                  <th class="info-left">性别</th>
                  <td>{{ userInfo.gender }}</td>
                </tr>
                <tr class="info-row">
                  <th class="info-left">学号</th>
                  <td>{{ userInfo.sno }}</td>
                </tr>
                <tr class="info-row">
                  <th class="info-left">学院</th>
                  <td>{{ userInfo.major }}</td>
                </tr>
                <tr class="info-row">
                  <th class="info-left">班级</th>
                  <td>{{ userInfo.studentName }}</td>
                </tr>
                <tr class="info-row">
                  <th class="info-left">邮件</th>
                  <td>{{ userInfo.semail }}</td>
                </tr>
                </tbody>
              </table>
              <table v-else-if="userInfo.type === '1'"> <!-- 老师信息 -->
                <tbody>
                <tr class="info-row">
                  <th class="info-left">姓名</th>
                  <td>{{ userInfo.teacherName }}</td>
                </tr>
                <tr class="info-row">
                  <th class="info-left">性别</th>
                  <td>{{ userInfo.gender }}</td>
                </tr>
                <tr class="info-row">
                  <th class="info-left">教师编号</th>
                  <td>{{ userInfo.teacherName }}</td>
                </tr>
                <tr class="info-row">
                  <th class="info-left">院系</th>
                  <td>{{ userInfo.major }}</td>
                </tr>
                <tr class="info-row">
                  <th class="info-left">职称</th>
                  <td>{{ userInfo.title }}</td>
                </tr>
                <tr class="info-row">
                  <th class="info-left">邮件</th>
                  <td>{{ userInfo.temail }}</td>
                </tr>
                </tbody>
              </table>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import {computed, ref} from "vue";
import { useUserStore } from '@/stores/user.js' // 引入userStore
import { ElMessage } from 'element-plus';
import {changePasswordService} from "@/api/user.js";
import { useRouter } from 'vue-router'

const router = useRouter()
const dialogVisible = ref(false);
const form = ref({
  newPassword: '',
  confirmPassword: ''
});

const userStore = useUserStore() // 使用userStore
const userInfo = computed(() => userStore.user) // 从 userStore 获取用户信息

// 打开对话框
const openDialog = () => {
  dialogVisible.value = true;
};
// 修改密码逻辑
const handleChangePassword = async () => {
  // 验证新密码与确认密码是否一致
  if (form.value.newPassword !== form.value.confirmPassword) {
    ElMessage.error('新密码与确认密码不一致');
    return;
  }
  try {
    // 发送修改密码的请求
    console.log('form.value.newPassword: ', form.value.newPassword)
    await changePasswordService(form.value.newPassword);
    ElMessage.success('密码修改成功');
    dialogVisible.value = false; // 关闭对话框
    // 清空表单
    form.value.newPassword = '';
    form.value.confirmPassword = '';
    userStore.removeToken() // 清空token重新登录
    await router.push('/login')
  } catch (error) {
    ElMessage.error('修改密码时出错');
  }
};


// 下面这个设置userInfo为响应式数据的方法行不通，用computed()可以，为什么呢
// const userInfo = ref()
//
// onMounted(() => {
//   userInfo.value = userStore.user;
//   console.log('UserInfo.vue: UserInfo.type: ', userInfo.value.type)
// })
</script>

<style lang="scss" scoped>
.bg {
  margin: 0;
  width: 100vw;
  height: calc(100vh - 80px);
  background-image: url('../../assets/bg3.jpg');
  background-size: cover;
  background-position: center;
}

.info-card {
  border: 1px solid #de98d1;
  width: 600px;
  min-height: 700px;
  background-color: rgb(228, 147, 187, 0.5);

  .avatar {
    display: flex;
    justify-content: center;
  }

  .info-title {
    margin-left: 10px;
    font-weight: bold;
    font-size: 16px;
  }

  .info-row {
    height: 40px;
  }

  .info-left {
    width: 100px;
  }
}
</style>