<template>
  <div class="peer-evaluation">
    <h2>作业互评</h2>

    <!-- 课程作业信息列表，点击作业显示详情弹窗 -->
    <div v-if="homeworkList && homeworkList.length" class="homework-list">
      <h3>课程作业列表</h3>
      <div
          v-for="homework in homeworkList"
          :key="homework.id"
          class="homework-item"
      >
        <p>{{ homework.homeworkName }}</p>
        <el-button type="text" @click="showHomeworkDetails(homework)">
          查看详情
        </el-button>
      </div>
    </div>

    <!-- 详情弹窗 -->
    <el-dialog
        v-model="isDialogVisible"
        title="作业详情"
        width="50%"
        :before-close="handleDialogClose"
    >
      <div v-if="selectedHomework">
        <h3>作业详情</h3>
        <!--        <p>作业名称: {{ selectedHomework.title }}</p>-->
        <p>作业名称: {{ selectedHomework.homeworkName }}</p>
        <p>开始时间: {{ selectedHomework. start }}</p>
        <p>截止时间: {{ selectedHomework. end }}</p>
        <!--        <p>文件路径: {{ selectedHomework.filePath }}</p>-->
        <el-button type="primary" @click="downloadHW">
          下载作业
        </el-button>

        <h3>提交或修改评分</h3>
        <el-input
            v-model="reviewData.comment"
            type="textarea"
            placeholder="请输入评价内容"
        />
        <el-input-number
            v-model="reviewData.score"
            :min="0"
            :max="100"
            label="评分（百分制）"
            placeholder="请输入0- 100的分数"
        />

        <div class="dialog-footer" slot="footer">
          <el-button @click="isDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitOrUpdateReview">
            {{ isEditMode ? '修改评分' : '提交评分' }}
          </el-button>
        </div>
      </div>
    </el-dialog>

    <!-- 查看互评 -->
    <div v-if="peerReviews && peerReviews.length" class="view-reviews">
      <h3>查看互评</h3>
      <div v-for="review in peerReviews" :key="review.id" class="review">
        <p>评分: {{ review.score }}</p>
        <p>评论: {{ review.comment }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted ,computed} from 'vue';
import { ElMessage, ElButton, ElDialog, ElInput, ElInputNumber } from 'element-plus';
import {
  getCourseHomework,
  peerDownload,
  submitReview,
  viewPeerReviews,
  modifyReview,
} from '@/api/Evaluation.js';
import {useUserStore} from "@/stores/user.js";
import {useCourseStore} from "@/stores/course.js";
import { useRoute } from 'vue-router'
import axios from "axios";

const homeworkList = ref([]);
const selectedHomework = ref(null);
const reviewData = ref({
  revieweeSno: '0',  // 修改为字符串类型
  cid: '1',          // 如果需要，转换其他字段也为字符串
  score: '0',        // 如果 score 是字符串
  comment: '',
  workid: '1'        // 如果 workid 是字符串
});

const peerReviews = ref([]);
const isDialogVisible = ref(false);
const isEditMode = ref(false); // 是否为修改模式
const courseStore = useCourseStore()
const courseInfo = computed(() => courseStore.course)
const userStore = useUserStore()
const userInfo = computed(() => userStore.user)

const route = useRoute()
const workid = route.query.workid

// 获取课程作业列表
const loadCourseHomeworkList = async () => {
  try {
    console.log('courseInfo',courseInfo.value)
    //reviewData.value.cid = courseInfo.value.cid || '';
    //console.log('HomeWork.vue111111111111111:   courseInfo.value.cid, userInfo.sno:', courseInfo.value.cid, userInfo.value.sno,courseInfo.value.workid);
    // const  res  = await getCourseHomework(userInfo.value.sno,courseInfo.value.cid,workid);
    const res = await axios.post('http://192.168.10.161:8080/api/student/peerDisplay', {
      reviewerSno: userInfo.value.sno,
      cid: courseInfo.value.cid,
      workid,
    });
    console.log('res.data',res.data)
    console.log('userInfo',userInfo.value)
    console.log('courseInfo',courseInfo.value)

    homeworkList.value = res.data.homeworkList;
    console.log('响应结果:', res);
    // 重新更新 reviewData 的字段
    reviewData.value.revieweeSno = String(homeworkList.value[0]?.revieweeSno || '0');  // 将值转换为字符串
// 假设获取第一个作业信息
    reviewData.value.cid = courseInfo.value.cid || '';
    console.log('courseInfo111', reviewData.value.cid )
    // reviewData.value.workid = homeworkList.value[0]?.workid || '';
    console.log('111HomeWork',reviewData.value.revieweeSno,reviewData.value.cid);
    console.log('222HomeWork',reviewData.value);
  } catch (error) {
    ElMessage.error('获取作业信息失败');
  }
};

// 打开作业详情弹窗
const showHomeworkDetails = (homework) => {
  selectedHomework.value = homework;
  //reviewData.value = { score: 0, comment: '' };
  isEditMode.value = false; // 初始化为非修改模式
  isDialogVisible.value = true;
  //loadPeerReviews(homework);
};


const downloadHW = async() => {
  try {
    // 调用接口并传入用户学号
    const response = await peerDownload(userInfo.value.sno);

    // 假设返回的是二进制数据，你需要使用 Blob 来处理这个数据
    const url = window.URL.createObjectURL(new Blob([response.data])); // 创建下载链接
    const link = document.createElement('a'); // 创建一个隐藏的下载链接
    link.href = url;
    link.setAttribute('download', 'homework.zip'); // 设置下载文件的名称（可以根据实际情况修改）
    document.body.appendChild(link); // 将链接添加到文档中
    link.click(); // 自动触发点击事件，开始下载
    document.body.removeChild(link); // 下载完成后移除链接
  } catch (error) {
    // 错误处理
    ElMessage.error('下载失败');
  }
};

// 提交或修改评分
const submitOrUpdateReview = async () => {
  try {
    // 在提交时将 score 转换为字符串
    const reviewDataToSubmit = {
      ...reviewData.value,
      score: String(reviewData.value.score)  // 确保score是字符串
    };
    console.log('reviewData.value.score',reviewData.value.score)
    console.log('reviewData.value',reviewData.value)
    if (isEditMode.value) {

      await modifyReview(reviewData.value);

      ElMessage.success('修改评分成功');
    } else {
      await submitReview(reviewData.value);
      ElMessage.success('提交评分成功');
    }
    isDialogVisible.value = false; // 关闭弹窗
    await loadPeerReviews (selectedHomework.value); // 刷新互评列表
  } catch (error) {
    ElMessage.error(isEditMode.value ? '修改评分失败' : '提交评分失败');
  }
};

// 获取互评
const loadPeerReviews = async () => {
  console.log("res:" )
  try {
    const res = await viewPeerReviews( reviewData.value);

    peerReviews.value = res;
    if (res && res.length > 0) {
      reviewData.value = res[0]; // 加载已有的评价到表单中
      isEditMode.value = true;
    }
  } catch (error) {
    //ElMessage.error('加载互评信息失败');
  }
};

// 初始加载课程作业列表
onMounted(() => {
  loadCourseHomeworkList();
});
</script>

<style scoped>
.peer-evaluation {
  padding: 20px;
}
.homework-list,
.submit-review,
.view-reviews {
  margin-bottom: 20px;
}
</style>
