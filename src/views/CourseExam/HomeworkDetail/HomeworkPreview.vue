<template>
  <!--  <iframe :src="'https://view.officeapps.live.com/op/embed.aspx?src=' + fileUrl" width="100%" height="100%"></iframe>-->
  <iframe :src="url" width="100%" height="100%"></iframe>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import {getPreviewFileService} from '@/api/homework.js'

const route = useRoute()
const url = ref('')

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
