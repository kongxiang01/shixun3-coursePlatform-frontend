<template>
  <div class="container">
    <div class="header" style="margin-bottom: 5px; display: flex; justify-content: space-between">
      <vertical-bar text="批阅作业"></vertical-bar>
      <div>
        <el-button size="default" @click="">预留的按钮</el-button>
        <el-button size="default" @click="goToHomeworkDetail">测试跳转</el-button>
        <el-button class="closeButton" @click="router.back();">关闭</el-button>
      </div>
    </div>
    <el-table :data="submittedTableData">
      <el-table-column prop="sno" label="学生学号" align="center"></el-table-column>
      <el-table-column prop="sname" label="学生姓名" align="center"></el-table-column>
      <el-table-column prop="submitTime" label="提交时间" align="center">
        <template #default="scope">
          {{ formatDate(scope.row.submitTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="fullScore" label="作业满分" align="center"></el-table-column>
      <el-table-column prop="score" label="得分" align="center"></el-table-column>
      <el-table-column label="操作" align="center">
        <template #default>
          <el-link type="primary" :underline="false" @click="handleCorrect" style="margin-right: 20px">批阅</el-link>
          <el-link type="danger" :underline="false" @click="handleDelete">删除</el-link>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import VerticalBar from "@/components/VerticalBar.vue";
import {computed, onMounted, ref} from "vue";
import {ElMessage} from "element-plus";
import {getSubmittedHomeworkListService} from "@/api/homework.js";
import {useCourseStore} from "@/stores/course.js";
import {useUserStore} from "@/stores/user.js";
import { useRouter } from "vue-router";

const router = useRouter()
const courseStore = useCourseStore()
const courseInfo = computed(() => courseStore.course)
const userStore = useUserStore()
const userInfo = computed(() => userStore.user)

const formatDate = (dateStr) => {
  const date = new Date(dateStr);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

const submittedTableData = ref();
const getSubmittedHomeworkList = async () => {
  try {
    const res = await getSubmittedHomeworkListService(courseInfo.value.cid)
    submittedTableData.value = res.data.submittedHomeworkList
  } catch (error) {
    ElMessage.error('CorrectHomework.vue11111111:获取学生提交作业列表失败:', error);
  }
}
// 跳转批阅详情页面
const goToHomeworkDetail = () => {
  // router.push({ name: 'HomeworkDetail' });
  router.push({ name: 'CorrectPreview' });
}

// ***************************************批阅和删除******************************
const handleCorrect = () => {

}
const handleDelete = () => {

}

onMounted(()=>{
  // const cname = route.params.cname;
  getSubmittedHomeworkList();
})
</script>

<style scoped lang="scss">
.container {
  padding: 20px;

  .el-table {
    padding: 20px 0;
    border-radius: 5px;
  }
}
</style>