<template>
  <!--  <iframe :src="'https://view.officeapps.live.com/op/embed.aspx?src=' + fileUrl" width="100%" height="100%"></iframe>-->
  <iframe :src="url" width="100%" height="100%"></iframe>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import {getPreviewFileService} from '@/api/user.js'

const route = useRoute()
const url = ref('')

onMounted(async () => {
  const workid = route.query.workid
  if (workid) {
    console.log("FilePreview.vue  222222222222222222222222222222222222222222222 fileUrl.value：", url.value);
    const res = await getPreviewFileService(workid);
    url.value = res.data.previewLink;
    console.log("FilePreview.vue  222222222222222222 fileUrl.value：", url.value);
  }
})
</script>
