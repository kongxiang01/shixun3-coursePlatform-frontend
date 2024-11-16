<template>
  <!--  <iframe :src="'https://view.officeapps.live.com/op/embed.aspx?src=' + fileUrl" width="100%" height="100%"></iframe>-->
  <el-button class="closeButton" @click="router.back();">关闭</el-button>
  <el-button  type="primary" class="downloadButton" @click="handleDownload">下载</el-button>
  <iframe :src="url" width="100%" height="100%"></iframe>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {getDownloadSubmittedService, getPreviewFileService} from '@/api/homework.js'
import {useCourseStore} from "@/stores/course.js";
import {useUserStore} from "@/stores/user.js";
import {useHomeworkStore} from "@/stores/homework.js";

const route = useRoute()
const router = useRouter()
const url = ref('')

const courseStore = useCourseStore()
const courseInfo = courseStore.course
const userStore = useUserStore()
const userInfo = userStore.user
const homeworkStore = useHomeworkStore()
const homeworkInfo = homeworkStore.homework

const handleDownload = async () => {
  const res = await getDownloadSubmittedService(courseInfo.cid, homeworkInfo.workid, userInfo.sno);
  url.value = res.data.previewLink;
  console.log("FilePreview.vue  222222222222222222 url.value：", url.value);
}

onMounted(async () => {
  const cname = route.query.cname
  if (cname) {
    console.log("FilePreview.vue  222222222222222222222222222222222222222222222 cname：", cname);
    const res = await getPreviewFileService(cname);
    url.value = res.data.previewLink;
    console.log("FilePreview.vue  222222222222222222 url.value：", url.value);
  }
})
</script>
