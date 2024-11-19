<template>
  <!--  <iframe :src="'https://view.officeapps.live.com/op/embed.aspx?src=' + fileUrl" width="100%" height="100%"></iframe>-->
  <el-button type="primary" @click="handleDownload">下载</el-button>
  <el-button class="closeButton" @click="router.back();">关闭</el-button>
  <iframe :src="url" width="100%" height="100%"></iframe>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {
  getDownloadSubmittedService,
  getStudentHomeworkPreviewService
} from '@/api/homework.js'
import {useCourseStore} from "@/stores/course.js";
import {useUserStore} from "@/stores/user.js";
import {ElMessage} from "element-plus";

const route = useRoute()
const url = ref('')
const router = useRouter()

const courseStore = useCourseStore()
const courseInfo = courseStore.course
const userStore = useUserStore()
const userInfo = userStore.user

const workid = route.query.workid
const handleDownload = async () => {
  // cid, workid, sno
  await getDownloadSubmittedService(courseInfo.cid, workid, userInfo.sno)

  try {
    // cid, workid, sno
    const res = await getDownloadSubmittedService(courseInfo.cid, workid, userInfo.sno)
    const downloadUrl = res.data.downloadLink;
    console.log("StudentPreview.vue: qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq  downloadUrl：", downloadUrl);
    // 创建一个临时的 <a> 元素并触发点击事件来下载文件
    const link = document.createElement("a");
    link.href = downloadUrl;
    link.download = userInfo.sno;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  } catch (error) {
    console.error("下载文件失败:", error);
    ElMessage.error("下载文件失败，请重试");
  }
}

onMounted(async () => {
  const cid = route.query.cid
  const sno = route.query.sno
  const workid = route.query.workid
  if (workid) {
    console.log("FilePreview.vue  222222222222222222222222222222222222222222222 fileUrl.value：", url.value);
    const res = await getStudentHomeworkPreviewService(cid, sno, workid); // cid, sno, workid
    url.value = res.data.previewLink;
    console.log("FilePreview.vue  222222222222222222 fileUrl.value：", url.value);
  }
})
</script>