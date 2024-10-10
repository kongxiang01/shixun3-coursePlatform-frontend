<script setup>
import VerticalBar from "@/components/VerticalBar.vue";
import {InfoFilled} from "@element-plus/icons-vue";
import { useRouter } from "vue-router";

const router = useRouter()
const handleClose = () => {
  router.back();
}
</script>

<template>
  <div class="info-container">
    <div class="header">
      <vertical-bar text="作业信息"></vertical-bar>
    </div>
    <div class="content">
      <el-card class="homework-info-card">
        <div class="formItem">
          <div class="formItemTitle">作业标题：</div>
          <div class="formItemContent">
            <span>作业1</span>
          </div>
        </div>
        <div class="formItem">
          <div class="formItemTitle">作业满分：</div>
          <div class="formItemContent">
            <span>100</span>
            分
          </div>
        </div>
        <div class="formItem">
          <div class="formItemTitle">作业内容：</div>
          <div class="formItemContent">
            <span>111111111111111111111111111111111111111111111111111111111111112222222222222222222222222222222222222222</span>
          </div>
        </div>
        <div class="formItem">
          <div class="formItemTitle">
            <el-link type="primary" :underline="false">参考答案</el-link>
            <el-icon style="margin-left: 3px"><InfoFilled /></el-icon>
          </div>
        </div>
        <div class="formItem">
          <div class="formItemTitle">提交时间：</div>
          <div class="formItemContent">
            <time class="custom-time" datetime="2024-10-09T12:00">2024-10-09 12:00</time>
            <span> - </span>
            <time class="custom-time" datetime="2024-10-10T12:00">2024-10-10 12:00</time>
          </div>
        </div>

        <div class="formItem">
          <div class="formItemTitle">作业附件：</div>
          <div class="formItemContent">
            <span>作业内容见附件,提交时请按照要求命名文件.</span>
          </div>
        </div>

        <div class="closeButtonBox">
          <el-button class="closeButton" @click="handleClose">关闭</el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<style scoped lang="scss">
.info-container{
  height: 100%;
  .header{
    padding-top: 10px;
    padding-left: 10px;
  }
  .content{
    margin-top: 20px;
    margin-left: 10%;
    .homework-info-card{
      //padding: 0; // el-card原生padding为20px, 只能变大不能变小
      background-color: rgba(111, 200, 238, 0.1);
      width: 60%;
      min-height: 400px;

      .formItem {
        //border: 1px solid black;
        width: 100%;
        margin-bottom: 20px;
        display: flex;
        //align-items: center; /* 垂直居中 */

        .formItemTitle {
          //border: 1px solid black;
          width: 100px;
          height: 30px;
          text-align: right;
          line-height: 30px;
          margin-right: 10px;
          display: flex; /* 让图标和文字在同一行 */
          align-items: center; /* 垂直居中 */
          justify-content: flex-end; /* 右对齐文字和图标 */
        }

        .formItemContent {
          //border: 1px solid black;
          //background-color: #805e73;
          flex: 1; /* 占满剩余空间 */
          width: 100px;
          min-height: 30px;
          text-align: left;
          line-height: 30px;
          margin-right: 10px;
          word-wrap: break-word;
        }
      }

      .closeButtonBox{
        display: flex;
        justify-content: flex-end;
        .closeButton{
          background-color: rgba(66, 184, 131, 0.2);
        }
      }
    }
  }
}

.custom-time {
  font-family: 'Courier New', Courier, monospace; /* 使用特殊字体 */
  font-size: 1.2em; /* 字体大小 */
  color: #835579; /* 颜色 */
  font-weight: bold; /* 粗体 */
  background-color: #f0f0f0; /* 背景颜色 */
  padding: 2px 5px; /* 内边距 */
  border-radius: 5px; /* 圆角 */
}

</style>