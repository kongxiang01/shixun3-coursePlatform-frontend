<template>
<!--  <iframe :src="'https://view.officeapps.live.com/op/embed.aspx?src=' + fileUrl" width="100%" height="100%"></iframe>-->
  <el-button class="closeButton" @click="router.back();">关闭</el-button>
  <iframe :src="fileUrl" width="100%" height="100%"></iframe>
</template>

<script setup>
import {computed, onMounted, ref} from 'vue'
import { useRoute, useRouter} from 'vue-router'
import { getPreviewFileService} from '@/api/asset.js'
import {useCourseStore} from "@/stores/course.js";

const route = useRoute()
const router = useRouter()
const courseStore = useCourseStore()
const courseInfo = computed( () => courseStore.course);
const fileUrl = ref('')

onMounted(async () => {
  const aid = route.query.aid
  if (filename) {
    console.log("FilePreview.vue  222222222222222222222222222222222222222222222 fileUrl.value：", fileUrl.value);
    const res = await getPreviewFileService(courseInfo.value.cid, aid);
    fileUrl.value = res.data.previewLink;
    console.log("FilePreview.vue  222222222222222222 fileUrl.value：", fileUrl.value);
  }
})
</script>
