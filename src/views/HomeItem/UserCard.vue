<script setup>
import { Edit } from '@element-plus/icons-vue';
import VerticalBar from "../../components/VerticalBar.vue";
import { useUserStore } from '@/stores/user.js'
import {computed} from "vue";

const userStore = useUserStore() // 使用userStore
const userInfo = computed(() => userStore.user) // 从 userStore 获取用户信息
</script>

<template>
  <el-card class="userCard" shadow="hover">
    <div class="userCard-header">
      <div class="left">
        <VerticalBar text="个人信息" />
      </div>
      <div class="right">
        <el-icon><Edit /></el-icon>
        <span class="label" @click="handleEdit" style="color: #ec2ba7">编辑</span>  <!-- 编辑文字 -->
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
  <el-card class="announceRemind" shadow="hover">
    <div class="card-header">
      <VerticalBar text="通知提醒" />
    </div>
    <div class="announceContent">
      0条通知未读
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