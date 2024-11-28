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
            <el-col :span="12">
              <span class="info-title">{{ userInfo.type === '0' ? '学生信息' : '老师信息' }}</span>
            </el-col>
            <el-col :span="12" style="text-align: right;">
              <el-button type="primary" @click="openDialog">修改密码</el-button>
              <el-dialog class="change-psw-dialog"
                         v-model="dialogVisible"
                         title="修改密码"
                         width="500"
                         align-center>
                <el-form :model="form" ref="formRef">
                  <el-form-item label="新密码" :rules="[{ required: true, message: '请输入新密码' }]">
                    <el-input v-model="form.newPassword" type="password" show-password placeholder="请输入新密码"></el-input>
                  </el-form-item>
                  <el-form-item label="确认新密码" prop="confirmPassword" :rules="[{ required: true, message: '请确认新密码' }]">
                    <el-input v-model="form.confirmPassword" type="password" show-password placeholder="请确认新密码"></el-input>
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
            <el-col :span="18">
              <!-- 根据 userInfo.type 动态显示学生或老师的信息 -->
              <table> <!-- 学生信息 -->
                <tbody>
                <tr class="info-row">
                  <th class="info-left">姓名</th>
                  <td>{{  userInfo.type === '0' ? userInfo.studentName : userInfo.teacherName  }}</td>
                </tr>
                <tr class="info-row">
                  <th class="info-left">性别</th>
                  <td>{{ userInfo.gender }}</td>
                </tr>
                <tr class="info-row">
                  <th class="info-left">编号</th>
                  <td>{{ userInfo.type === '0' ? userInfo.sno : userInfo.tno }}</td>
                </tr>
                <tr class="info-row">
                  <th class="info-left">学院/院系</th>
                  <td>{{ userInfo.major }}</td>
                </tr>
                <tr class="info-row" v-if="userInfo.type === '0'">
                  <th class="info-left">班级</th>
                  <td>{{ userInfo.studentName }}</td>
                </tr>
                <tr class="info-row">
                  <th class="info-left">邮件</th>
                  <td>
                    <div v-if="!isEditingEmail">
                      {{ userInfo.type === '0' ? userInfo.semail : userInfo.temail }}
                    </div>
                    <div v-else>
                      <el-input
                          v-model="editEmail"
                          placeholder="请输入新邮箱"
                          size="default"
                          style="width: 100%;"
                      ></el-input>
                    </div>
                  </td>
                </tr>
                </tbody>
              </table>
            </el-col>
          </el-row>

          <div style="margin-top: 20px; display: flex; justify-content: center">
            <div v-if="!isEditingEmail" >
              <el-button size="small" type="primary" @click="startEditEmail">编辑</el-button>
            </div>
            <div v-else>
              <el-button
                  size="small"
                  type="primary"
                  @click="updateEmail"
                  :disabled="!isEmailValid"
              >
                确认
              </el-button>
              <el-button size="small" type="danger" @click="cancelEditEmail">取消</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { computed, ref } from "vue";
import { useUserStore } from '@/stores/user.js';
import { ElMessage } from 'element-plus';
import { changePasswordService, updateUserEmail } from "@/api/user.js";
import { useRouter } from 'vue-router';

const router = useRouter();
const dialogVisible = ref(false);
const form = ref({
  newPassword: '',
  confirmPassword: ''
});

const userStore = useUserStore(); // 使用 userStore
const userInfo = computed(() => userStore.user); // 从 userStore 获取用户信息

// 邮箱编辑逻辑
const isEditingEmail = ref(false);
const editEmail = ref("");

// 邮箱格式验证
const isEmailValid = computed(() =>
    /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z]{2,}$/.test(editEmail.value)
);

const startEditEmail = () => {
  isEditingEmail.value = true;
  editEmail.value = userInfo.value.type === "0" ? userInfo.value.semail : userInfo.value.temail;
};

const cancelEditEmail = () => {
  isEditingEmail.value = false;
  editEmail.value = "";
};

const updateEmail = async () => {
  try {
    const pno = userInfo.value.type === "0" ? userInfo.value.sno : userInfo.value.tno;
    await updateUserEmail(pno, editEmail.value);
    ElMessage.success("邮箱修改成功");
    if (userInfo.value.type === "0") {
      userInfo.value.semail = editEmail.value;
    } else {
      userInfo.value.temail = editEmail.value;
    }
    cancelEditEmail();
  } catch (error) {
    ElMessage.error("邮箱修改失败");
  }
};

// 修改密码逻辑
const openDialog = () => {
  form.value = {
    newPassword: '',
    confirmPassword: '',
  };
  dialogVisible.value = true;
};

const handleChangePassword = async () => {
  if (form.value.newPassword !== form.value.confirmPassword) {
    ElMessage.error('新密码与确认密码不一致');
    return;
  }
  try {
    await changePasswordService(form.value.newPassword);
    ElMessage.success('密码修改成功');
    form.value = {
      newPassword: '',
      confirmPassword: '',
    };
    dialogVisible.value = false;
    userStore.removeToken();
    await router.push('/login');
  } catch (error) {
    ElMessage.error('修改密码时出错');
  }
};
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

.change-psw-dialog{
  width: 300px;
}
</style>