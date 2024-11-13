<template>
<!--  <iframe :src="'https://view.officeapps.live.com/op/embed.aspx?src=' + fileUrl" width="100%" height="100%"></iframe>-->
  <el-button class="closeButton" @click="router.back();">关闭</el-button>
  <iframe :src="fileUrl" width="100%" height="100%"></iframe>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter} from 'vue-router'
import { getPreviewFileService} from '@/api/user.js'

const route = useRoute()
const router = useRouter()
const fileUrl = ref('')

onMounted(async () => {
  const filename = route.query.fileName
  if (filename) {
    console.log("FilePreview.vue  222222222222222222222222222222222222222222222 fileUrl.value：", fileUrl.value);
    const res = await getPreviewFileService(filename);
    fileUrl.value = res.data.previewLink;
    console.log("FilePreview.vue  222222222222222222 fileUrl.value：", fileUrl.value);
  }
})
</script>
