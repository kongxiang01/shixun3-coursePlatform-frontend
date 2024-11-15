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
      <el-form ref="gradingForm" :model="gradingData" :rules="correctRules" label-width="80px">
        <el-form-item prop="score" label="分数">
          <el-input v-model="gradingData.score" :placeholder="'请输入分数, 满分为 ' + fullMark" />
        </el-form-item>
        <el-form-item prop="comments" label="评语">
          <el-input
              v-model="gradingData.comments"
              type="textarea"
              placeholder="请输入评语，选填"
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
import {getHomeworkPreviewService, correctHomeworkScoreService} from '@/api/homework.js'
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

// 获取作业预览 URL
const loadPreviewUrl = async () => {
  try {
    console.log('cid: ', cid,'workid: ', workid,'sno: ', sno)
    const res = await getHomeworkPreviewService(cid, workid, sno)// cid, workid, sno
    fileUrl.value =res.data.previewLink
    console.log('fileUrl.value: ', fileUrl.value)
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

// ****************************************************提交批改*********************************************
const correctRules = {
  score: [
    { required: true, message: '请输入作业得分', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        const score = Number(value);
        if (isNaN(score)) {
          callback(new Error('得分必须是数字'));
        // } else if (score < 1 || score > 100) {
        //   callback(new Error('满分应在1到100之间'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ],
  comments: [
    { required: false, message: '请输入评价', trigger: 'blur' },
    { max: 3000, message: '评价不能超过3000字', trigger: 'blur' }
  ]
};

const cid = route.query.cid;
const workid = route.query.workid;
const sno = route.query.sno
const fullMark = route.query.fullMark;

const submitGrading = async () => {
  try {
    await correctHomeworkScoreService(gradingData.value.score, cid, workid, sno) // score, cid, workid, sno
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
  height: calc(100vh - 120px);
}

.preview-section {
  flex: 1;
  padding: 10px;
  //overflow-y: auto;
  border-right: 1px solid #ebebeb;
}

.grading-section {
  flex: 1;
  padding: 20px;
}
</style>
