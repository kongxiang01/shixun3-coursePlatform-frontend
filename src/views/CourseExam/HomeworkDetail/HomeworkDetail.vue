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
            <span>{{ '第 ' + homeworkInfo?.workid + ' 次作业' || '暂无数据' }}</span>
          </div>
        </div>
        <div class="formItem">
          <div class="formItemTitle">作业满分：</div>
          <div class="formItemContent">
            <span>{{ homeworkInfo?.fullMark  || '未设置' }}</span> 分
          </div>
        </div>
        <div class="formItem">
          <div class="formItemTitle">作业内容：</div>
          <div class="formItemContent">
            <span>{{ homeworkInfo?.content || '暂无数据' }}</span>
          </div>
        </div>
<!--        <div class="formItem">
          <div class="formItemTitle">
            <el-link type="primary" :underline="false">参考答案</el-link>
            <el-icon style="margin-left: 3px"><InfoFilled /></el-icon>
          </div>
        </div>-->
        <div class="formItem">
          <div class="formItemTitle">提交时间：</div>
          <div class="formItemContent">
            <time class="custom-time" :datetime="homeworkInfo?.start">
              {{ formatDate(homeworkInfo.start) }}
            </time>
            <span> - </span>
            <time class="custom-time" :datetime="homeworkInfo?.end">
              {{ formatDate(homeworkInfo.end) }}
            </time>
          </div>
        </div>

        <div class="formItem">
          <div class="formItemTitle">作业附件：</div>
          <div class="formItemContent">
            <span style="color:#4daad2; font-size: 14px">{{ homeworkInfo?.cname || '无附件' }}</span>
          </div>
        </div>

        <div class="formItem" style="margin-left: 100px">
          <el-button type="primary" @click="handlePreviewAttachment">预览附件</el-button>
          <el-button type="success" @click="handleDownloadAttachment">下载附件</el-button>
        </div>

        <div class="closeButtonBox">
          <el-button class="closeButton" @click="handleClose">关闭</el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import VerticalBar from "@/components/VerticalBar.vue";
import { InfoFilled } from "@element-plus/icons-vue";
import { useRouter } from "vue-router";
import {getDownloadFileService, getHomeworkInfoService} from "@/api/user.js";
import {useHomeworkStore} from "@/stores/homework.js";
import {getPreviewFileService} from "@/api/homework.js";
import {ElMessage} from "element-plus";
import {getDownloadAssignedService} from "@/api/homework.js";
import {useCourseStore} from "@/stores/course.js";
import {useUserStore} from "@/stores/user.js"; // 假设已封装的请求方法路径

const router = useRouter();

const courseStore = useCourseStore()
const courseInfo = courseStore.course
const userStore = useUserStore()
const userInfo = userStore.user
const homeworkStore = useHomeworkStore()
const homeworkInfo = homeworkStore.homework // 用于存储作业信息
const handleClose = () => {
  router.back();
};

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

// 附件预览和下载方法
const handlePreviewAttachment = () => {
  console.log("HomeworkDetail.vue: qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqhomeworkInfo.value：", homeworkInfo);
  router.push({ name: 'HomeworkPreview', query: { cname: homeworkInfo.cname } });
};

const handleDownloadAttachment = async () => {
  console.log("HomeworkDetail.vue: qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqhomeworkInfo.value：", homeworkInfo);
  try {
    //                                            cid workid sno
    const res = await getDownloadAssignedService(courseInfo.cid, homeworkInfo.workid); // 假设你在 api 中定义了 getDownloadFileService
    const downloadUrl = res.data.downloadLink;
    console.log("HomeworkDetail.vue: qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq  downloadUrl：", downloadUrl);
    // 创建一个临时的 <a> 元素并触发点击事件来下载文件
    const link = document.createElement("a");
    link.href = downloadUrl;
    link.download = homeworkInfo.workid;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  } catch (error) {
    console.error("下载文件失败:", error);
    ElMessage.error("下载文件失败，请重试");
  }
};
</script>

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