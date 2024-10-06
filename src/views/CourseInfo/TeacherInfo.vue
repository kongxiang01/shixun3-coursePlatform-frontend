<script setup>

import VerticalBar from "../../components/VerticalBar.vue";
import {reactive} from "vue";

const personalInfo = reactive({
  name: '张三',
  gender: '男',
  studentId: '22300000',
  academy: '软件学院',
  class: '软件2000',
  email: 'zhangsan@example.com'
})

const editInfo = () => {
  console.log('编辑个人信息')
}
</script>

<template>
  <div class="t-info-container">
    <div class="header">
      <vertical-bar text="教师信息"></vertical-bar>
    </div>
    <div class="infoCardContainer">
<!--      <el-row style="height: 10px"></el-row>-->
      <el-row type="flex" justify="center" align="middle">
        <el-col :span="11">
          <el-card class="info-card">
            <el-row class="avatar">
              <el-avatar size="large" src="https://via.placeholder.com/50" />
            </el-row>
            <el-row justify="space-between" align="middle">
              <el-col span="12">
                <span class="info-title">个人信息</span>
              </el-col>
              <el-col span="12">
                <el-button type="primary" @click="editInfo">编辑</el-button>
              </el-col>
            </el-row>

            <el-divider></el-divider>
            <!--          <hr style="height:1px; border-width:0; background-color:#dadde4; margin:20px auto">-->

            <el-row>
              <el-col :span="12">
                <table>
                  <tbody>
                  <tr class="info-row">
                    <th class="info-left">姓名</th>
                    <td>{{ personalInfo.name }}</td>
                  </tr>
                  <tr class="info-row">
                    <th class="info-left">性别</th>
                    <td>{{ personalInfo.gender }}</td>
                  </tr>
                  <tr class="info-row">
                    <th class="info-left">学号</th>
                    <td>{{ personalInfo.studentId }}</td>
                  </tr>
                  <tr class="info-row">
                    <th class="info-left">学院</th>
                    <td>{{ personalInfo.academy }}</td>
                  </tr>
                  <tr class="info-row">
                    <th class="info-left">班级</th>
                    <td>{{ personalInfo.class }}</td>
                  </tr>
                  <tr class="info-row">
                    <th class="info-left">邮件</th>
                    <td>{{ personalInfo.email }}</td>
                  </tr>
                  </tbody>
                </table>
              </el-col>
            </el-row>
            <el-row :span="12"></el-row>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.t-info-container{
  //margin-top: 10px;
  height: 100%;
  //background-color: rgba(100, 238, 176, 0.82);
  .header {
    padding-top: 10px;
    margin-left: 10px;
    //border: 1px solid black;
  }
  .infoCardContainer{
    height: calc(100vh - 194px);
    padding: 20px;
    //border-radius: 5px;
    //background-color: #9f7390;
    //color: black;
    //font-size: 14px;

    .info-card {
      border: 1px solid #de98d1;
      min-width: 600px;
      //margin: 10px auto;
      min-height: 700px;
      background-color: rgb(228, 147, 187, 0.5);

      .avatar{
        display: flex;
        justify-content: center;
      }

      .info-title {
        margin-left: 10px;
        font-weight: bold;
        font-size: 16px;
      }
      .info{
        background-color: rgba(222, 152, 209, 0);
        .info-item {
          background-color: rgba(255, 255, 255, 0); // 半透明白色背景
        }
      }
      .info-row{
        height: 40px;
      }
      .info-left{
        width: 100px;
        //border: 1px solid #de98d1;
      }
    }
  }
}


</style>