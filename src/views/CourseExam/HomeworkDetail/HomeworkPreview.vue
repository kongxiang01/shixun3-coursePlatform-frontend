<template>
  <!--  <iframe :src="'https://view.officeapps.live.com/op/embed.aspx?src=' + fileUrl" width="100%" height="100%"></iframe>-->
  <el-button type="primary" @click="handleDownload;">下载</el-button>
  <el-button class="closeButton" @click="router.back();">关闭</el-button>
  <iframe :src="url" width="100%" height="100%"></iframe>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {getDownloadSubmittedService, getPreviewFileService} from '@/api/homework.js'
import {useCourseStore} from "@/stores/course.js";
import {useUserStore} from "@/stores/user.js";

const route = useRoute()
const router = useRouter()
const url = ref('')

const courseStore = useCourseStore()
const courseInfo = courseStore.course
const userStore = useUserStore()
const userInfo = userStore.user

const workid = route.query.workid
const handleDownload = async () => {
  // cid, workid, sno
  await getDownloadSubmittedService(courseInfo.cid, workid, userInfo.sno)
}

onMounted(async () => {
  if (workid) {
    console.log("FilePreview.vue  222222222222222222222222222222222222222222222 courseInfo.value.cid workid：", courseInfo.cid, workid);
    const res = await getPreviewFileService( courseInfo.cid, workid);
    url.value = res.data.previewLink;
    console.log("FilePreview.vue  222222222222222222 url.value：", url.value);
  }
})
</script>
