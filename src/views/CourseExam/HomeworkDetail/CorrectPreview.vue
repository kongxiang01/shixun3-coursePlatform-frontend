<template>
  <div class="review-page">
    <!-- 左侧预览部分 -->
    <div class="preview-section">
      <iframe
          v-if="fileUrl"
          :src="fileUrl"
          width="100%"
          height="100%"
          title="作业预览"
      ></iframe>
      <p v-else>没有可预览的文件</p>
    </div>

    <!-- 右侧批改部分 -->
    <div class="grading-section">
      <el-form ref="gradingForm" :model="gradingData" label-width="80px">
        <el-form-item label="分数">
          <el-input v-model="gradingData.score" placeholder="请输入分数" />
        </el-form-item>
        <el-form-item label="评语">
          <el-input
              v-model="gradingData.comments"
              type="textarea"
              placeholder="请输入评语"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitGrading">提交</el-button>
          <el-button class="closeButton" @click="router.back();">关闭</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted, computed} from 'vue'
import { ElMessage } from 'element-plus'
import { getHomeworkPreviewService, correctHomeworkService } from '@/api/homework.js'
import {useRoute, useRouter} from "vue-router";
import {useCourseStore} from "@/stores/course.js";
import {useUserStore} from "@/stores/user.js";
import {useHomeworkStore} from "@/stores/homework.js";

const router = useRouter()
const route = useRoute()

const courseStore = useCourseStore()
const courseInfo = computed(() => courseStore.course)
const userStore = useUserStore()
const userInfo = computed(() => userStore.user)
const homeworkStore = useHomeworkStore()
const homeworkInfo = computed(() => homeworkStore.homework)

// 初始化文件预览 URL
const fileUrl = ref('')

const cid = route.query.cid;
const workid = route.query.workid;
const sno = route.query.sno

// 获取作业预览 URL
const loadPreviewUrl = async () => {
  try {
    fileUrl.value = await getHomeworkPreviewService(cid, workid, sno) // cid, workid, sno
  } catch (error) {
    ElMessage.error('获取预览链接失败')
  }
}

onMounted(() => {
  loadPreviewUrl()
})

// 批改数据模型
const gradingData = ref({
  score: '',
  comments: ''
})

// 提交批改
const submitGrading = async () => {
  if (!gradingData.value.score || !gradingData.value.comments) {
    ElMessage.error('请填写分数和评语')
    return
  }

  try {
    await correctHomeworkService(gradingData.value)
    ElMessage.success('批改提交成功')
    gradingData.value.score = ''
    gradingData.value.comments = ''
  } catch (error) {
    ElMessage.error('批改提交失败')
  }
}
</script>

<style scoped>
.review-page {
  display: flex;
  height: 100vh;
}

.preview-section {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  border-right: 1px solid #ebebeb;
}

.grading-section {
  flex: 1;
  padding: 20px;
}
</style>
