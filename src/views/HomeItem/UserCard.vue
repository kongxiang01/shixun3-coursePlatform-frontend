<script setup>
import { Edit } from '@element-plus/icons-vue';
import VerticalBar from "../../components/VerticalBar.vue";
import { useUserStore } from '@/stores/user.js'
import {computed, onMounted, ref} from "vue";
import { useRouter } from "vue-router";
import {getCourseListService, getNotificationListService} from "@/api/course.js";

const router = useRouter()
const userStore = useUserStore() // 使用userStore
const userInfo = computed(() => userStore.user) // 从 userStore 获取用户信息

const notificationNum = ref()
const courses = ref([])
// ******************************************************获取通知列表***************************************
// 获取学生课程数据
const totalNotifications = ref(0); // 用于存储所有课程的通知总数

// 获取课程列表
const getCourses = async () => {
  try {
    let res;
    if (userInfo.value.type === '0') {
      res = await getCourseListService(userInfo.value.sno); // 学生课程
    } else {
      res = await getCourseListService(userInfo.value.tno); // 教师课程
    }
    courses.value = res.data.courseList; // 将课程列表存储
  } catch (error) {
    console.error('获取课程失败:', error);
  }
};

// 获取单个课程的通知列表
const fetchNoticeList = async (cid) => {
  try {
    const res = await getNotificationListService(cid); // 获取通知列表
    return res.data.notificationList.length; // 返回通知数
  } catch (error) {
    console.error(`请求通知出错 (课程ID: ${cid}):`, error);
    return 0; // 请求失败返回 0
  }
};

// 计算所有课程的通知总数
const calculateTotalNotifications = async () => {
  try {
    totalNotifications.value = 0; // 初始化为 0
    for (const course of courses.value) {
      const count = await fetchNoticeList(course.cid); // 获取单个课程通知数
      totalNotifications.value += count; // 累加通知数
    }
  } catch (error) {
    console.error('计算总通知数出错:', error);
  }
};

// 页面加载时获取数据
onMounted(async () => {
  if(userInfo.value.type === '0'){
    await getCourses(); // 获取课程列表
    await calculateTotalNotifications(); // 计算通知总数
    console.log('所有课程通知总数:', totalNotifications.value);
  }
});

const handleEdit = () => {
  router.push('/userInfo')
}
</script>

<template>
  <el-card class="userCard" shadow="hover">
    <div class="userCard-header">
      <div class="left">
        <VerticalBar text="个人信息" />
      </div>
      <div class="right">
        <el-icon><Edit /></el-icon>
        <span class="label" @click="handleEdit" style="color: #ec2ba7; cursor: pointer">编辑</span>  <!-- 编辑文字 -->
      </div>
    </div>
    <div class="userCard-mid">
      <img src="../../assets/kedaya.jpg" alt="无法加载" style="width: 100px; height: 100px; border-radius: 50%">
<!--      <el-avatar :size="100" src="../../assets/kedaya.jpg"></el-avatar>-->
      <div class="name"
           style="margin-top: 10px; font-size: 20px">
        {{userInfo.teacherName}}
      </div>
      <el-tag type="warning" size="large" style="font-size: 20px; margin-top: 5px" v-if="userInfo.type === '0'">学生</el-tag>
      <el-tag type="warning" size="large" style="font-size: 20px; margin-top: 5px" v-else-if="userInfo.type === '1'">{{userInfo.title}}</el-tag>
    </div>
    <div class="userCard-down">
      <el-col>
        <el-row justify="center">学院: {{userInfo.major}}</el-row>
        <el-row justify="center" v-if="userInfo.type === '0'">邮箱: {{userInfo.semail}}</el-row>
        <el-row justify="center" v-if="userInfo.type === '1'">邮箱: {{userInfo.temail}}</el-row>
        <el-row justify="center">电话: {{userInfo.title}}</el-row>
      </el-col>
    </div>
  </el-card>
  <el-card v-if="userInfo.type === '0'" class="announceRemind" shadow="hover">
    <div class="card-header">
      <VerticalBar text="通知提醒" />
    </div>
    <div class="announceContent">
      <span style="margin: 0 5px; color: #b72323; font-size: 20px">{{ totalNotifications }}</span>条通知未读
    </div>
  </el-card>
</template>

<style lang="scss" scoped>
.userCard{
  background-color: #fcdfe9;

  .userCard-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
    //height: 40px;
    //padding: 15px;
    //border: 2px solid black;
    .left, .right {
      display: flex;
      align-items: center;
    }
  }
  .userCard-mid {
    display: flex;
    flex-direction: column;
    align-items: center;
    height: 180px;
    //border: 2px solid black;
  }
  .userCard-down {
    margin-top: 20px;
    //display: flex;
    //flex-direction: column;
    //align-items: center;
    height: 150px;
    //border: 2px solid black;
    font-size: 18px;
    color: #989898;
  }
}

.announceRemind {
  background-color: #fcdfe9;
  margin-top: 20px;
  .announceContent{}
}
</style>