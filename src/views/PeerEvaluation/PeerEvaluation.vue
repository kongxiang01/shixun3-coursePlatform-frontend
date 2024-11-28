<template>
  <div class="peer-evaluation">
    <h2>作业互评</h2>

    <!-- 课程作业信息列表，使用 el-table -->
    <div v-if="homeworkList && homeworkList.length" class="homework-list">
      <h3>课程作业列表</h3>
      <el-table :data="homeworkList" style="width: 100%" border>
        <el-table-column prop="homeworkName" label="作业名称" />
        <el-table-column prop="start" label="开始时间" />
        <el-table-column prop="end" label="截止时间" />
        <el-table-column prop="studentscore" label="分数" width="120">
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button type="primary" size="small" @click="showHomeworkDetails(scope.row)">
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
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
        <p>作业名称: {{ selectedHomework.homeworkName }}</p>
        <p>开始时间: {{ selectedHomework.start }}</p>
        <p>截止时间: {{ selectedHomework.end }}</p>
        <el-button type="primary" @click="downloadHW">下载作业</el-button>

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
            placeholder="请输入 0-100 的分数"
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
import { ref, onMounted, computed } from 'vue';
import { ElMessage, ElButton, ElDialog, ElInput, ElInputNumber, ElTable, ElTableColumn } from 'element-plus';
import {
  peerDownload,
  submitReview,
  viewPeerReviews,
  modifyReview,
} from '@/api/Evaluation.js';
import { useUserStore } from '@/stores/user.js';
import { useCourseStore } from '@/stores/course.js';
import { useRoute } from 'vue-router'
import axios from "axios";
const homeworkList = ref([]);
const selectedHomework = ref(null);
const reviewData = ref({
  revieweeSno: '0',
  cid: '1',
  score: '0',
  comment: '',
  workid: '1',
});
const route = useRoute()
const peerReviews = ref([]);
const isDialogVisible = ref(false);
const isEditMode = ref(false);
const courseStore = useCourseStore();
const courseInfo = computed(() => courseStore.course);
const userStore = useUserStore();
const userInfo = computed(() => userStore.user);
const workid = route.query.workid

const loadCourseHomeworkList = async () => {
  try {
    console.log('courseInfo',courseInfo.value)
    //reviewData.value.cid = courseInfo.value.cid || '';
    //console.log('HomeWork.vue111111111111111:   courseInfo.value.cid, userInfo.sno:', courseInfo.value.cid, userInfo.value.sno,courseInfo.value.workid);
    // const  res  = await getCourseHomework(userInfo.value.sno,courseInfo.value.cid,workid);
    const res = await axios.post('http://192.168.10.127:8080/api/student/peerDisplay', {
      reviewerSno: userInfo.value.sno,
      cid: courseInfo.value.cid,
      workid,
    });

    console.log('res.data',res.data)
    console.log('userInfo',res.data.revieweeSno)
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
  isEditMode.value = false;
  isDialogVisible.value = true;
};
/*const downloadHW = async () => {
  try {
    const response = await peerDownload(userInfo.value.sno, courseInfo.value.cid, workid);

    // 检查 Blob 是否为空或异常
    if (!response.data || response.data.size < 1024) { // 假设 ZIP 文件最小为 1KB
      const errorText = await new Response(response.data).text(); // 尝试解析错误信息
      console.error('下载失败，错误信息:', errorText);
      ElMessage.error('下载失败，可能是服务器错误或文件不存在');
      return;
    }

    // 解析文件名
    const contentDisposition = response.headers['content-disposition'];
    const fileNameMatch = contentDisposition && contentDisposition.match(/filename="?([^"]*)"?/);
    const fileName = fileNameMatch ? decodeURIComponent(fileNameMatch[1]) : 'homework.zip';

    // 创建下载链接
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', fileName);
    document.body.appendChild(link);
    link.click();

    // 清理
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);

    ElMessage.success('下载成功');
  } catch (error) {
    console.error('下载失败:', error);
    ElMessage.error('下载失败，请稍后重试');
  }
};*/
const downloadHW = async () => {
  try {
    //                                            cid workid sno
    console.log('selectedHomework.value.revieweeSno',selectedHomework.value.revieweeSno)
    const res =  await peerDownload(selectedHomework.value.revieweeSno,courseInfo.value.cid,workid);// revieweesno, cid, workid
    console.log('res.data',res.data)// 假设你在 api 中定义了 getDownloadFileService
    const downloadUrl = res.data.downloadLink;
    console.log("HomeworkDetail.vue: qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq  downloadUrl：",  res.data.downloadLink);
    // 创建一个临时的 <a> 元素并触发点击事件来下载文件
    const link = document.createElement("a");
    link.href = downloadUrl;
    link.download = workid;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  } catch (error) {
    console.error("下载文件失败:", error);
    ElMessage.error("下载文件失败，请重试");
  }
};


// const downloadHW = async () => {
//   try {
//     //console.log('111response.data')
//     const response = await peerDownload(userInfo.value.sno,courseInfo.value.cid,workid);
//     console.log('111response.data',response.data.downloadLink)
//     const url = window.URL.createObjectURL(new Blob([response.data]));
//     const link = document.createElement('a');
//     link.href = url;
//     link.setAttribute('download', 'homework.zip');
//     document.body.appendChild(link);
//     link.click();
//     document.body.removeChild(link);
//   } catch (error) {
//     ElMessage.error('下载失败');
//   }
// };

const submitOrUpdateReview = async () => {
  try {
    console.log('selectedHomework.value',selectedHomework.value.revieweeSno)
    reviewData.value.revieweeSno=selectedHomework.value.revieweeSno
    reviewData.value.reviewerSno=userInfo.value.sno
    reviewData.value.score = String(reviewData.value.score);
    console.log('reviewData.value',reviewData.value)
    if (isEditMode.value) {
      await modifyReview(reviewData.value);
      ElMessage.success('修改评分成功');
    } else {
      await submitReview(reviewData.value);
      ElMessage.success('提交评分成功');
    }
    isDialogVisible.value = false;
    loadPeerReviews(selectedHomework.value);
  } catch (error) {
    ElMessage.error(isEditMode.value ? '修改评分失败' : '提交评分失败');
  }
};

const loadPeerReviews = async () => {
  try {
    const res = await viewPeerReviews(reviewData.value);
    peerReviews.value = res;
    if (res && res.length > 0) {
      console.log()
      reviewData.value = res[0];
      isEditMode.value = true;
    }
  } catch (error) {
    // Handle error
  }
};

onMounted(() => {
  loadCourseHomeworkList();
});
</script>

<style scoped>
.peer-evaluation {
  padding: 20px;
}
</style>
