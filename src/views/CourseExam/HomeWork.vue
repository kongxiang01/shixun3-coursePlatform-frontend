<template>
  <div class="container">
    <el-table :data="tableData">
      <el-table-column prop="hTitle" label="作业标题" align="center">
        <template #default="scope">
          <el-link type="primary" :underline="false" @click="handleHW">{{scope.row.hTitle}}</el-link>
        </template>
      </el-table-column>
      <el-table-column prop="hStart" label="作业开始" align="center"></el-table-column>
      <el-table-column prop="hEnd" label="作业截止" align="center"></el-table-column>
      <el-table-column prop="hSubmitNum" label="提交人数" align="center"></el-table-column>
      <el-table-column prop="hSubmitTime" label="提交时间" align="center"></el-table-column>
      <el-table-column prop="hScore" label="得分" align="center"></el-table-column>
      <el-table-column prop="hStatus" label="批改状态" align="center"></el-table-column>
      <el-table-column label="操作" align="center">
        <template #default>
          <el-link type="primary" :underline="false" @click="drawerVisible = true">提交</el-link>
        </template>
      </el-table-column>
    </el-table>

    <el-drawer
        title="提交作业"
        v-model="drawerVisible"
        direction="rtl"
        size="40%"
    >
      <div style="margin: 20px 0">作业内容</div>
      <el-form ref="form" :model="formData" class="form">
        <el-form-item>
          <el-input
              type="textarea"
              :rows="6"
              placeholder="请输入3000字以内的作业内容！"
              v-model="formData.content"
          ></el-input>
        </el-form-item>

        <!-- 上传文件按钮 -->
        <div>
          <el-upload
              action="#"
              list-type="text"
              :auto-upload="false"
              v-model:file-list="formData.fileList"
          >
            <el-button size="default" type="primary">上传文件</el-button>
          </el-upload>
        </div>

        <!-- 分割线 -->
        <el-divider style="height: 2px; background-color: #a8a2a2; margin: 10px 0"></el-divider>

        <!-- 文件上传提示 -->
        <div class="el-upload__tip" style="margin: 0;padding: 0">
          允许上传的文件类型: doc, pdf, ppt, xls, docx, pptx, xlsx, jpg, gif, jpeg, png, bmp
        </div>

        <!-- 确定和取消按钮 -->
        <div class="buttons">
          <el-button type="primary" @click="submitForm">确定</el-button>
          <el-button @click="drawerVisible = false">取消</el-button>
        </div>
      </el-form>
    </el-drawer>
  </div>
</template>

<script setup>
import {useRoute, useRouter} from "vue-router";
import {computed, onMounted, ref} from "vue";
import homeworkDetail from "@/views/CourseExam/HomeworkDetail/HomeworkDetail.vue";
import {getHomeworkList} from "@/api/user.js";

const route = useRoute();

// 使用 computed 从 query 中获取课程信息
const courseInfo = computed(() => ({
  cname: route.query.cname,
  cno: route.query.cno,
  cid: route.query.cid
}));
const tableData = ref([])

const getHWData = async () => {
  try {
    // console.log('CourseList.vue111:   userStore.user.sno:', userStore.user.sno);
    const res = await getHomeworkList(courseInfo.value.cid, userStore.user.sno); // 向后端获取学生课程
    console.log('HomeWork.vue111111111:   courseInfo.value.cid:', courseInfo.value.cid);
    tableData.value = res.data.homeworkInfoList; // 将返回的数据赋值给courses
  } catch (error) {
    console.error('Homework.vue:获取作业列表失败:', error);
  }
};

// const tableData = [
//   {
//     hTitle: '作业一',
//     hStart: '11-11-1111',
//     hEnd: '22-22-2222',
//     hSubmitNum: 66,
//     hSubmitTime: '11-22-1111',
//     hScore: 80,
//     hStatus: '已批改',
//   }
// ]

const router = useRouter();
// const courseId = ref(route.params.id);
// const courseNumber = ref(route.params.courseNumber);

// 动态生成路径的函数
const generateCoursePath = (suffix) => {
  return `/course/${suffix}`;
};

const handleHW = () => {
  router.push(generateCoursePath('homeworkDetail'))
}

const drawerVisible = ref(false);
// 表单数据
const formData = ref({
  content: '',  // 作业内容
  fileList: []  // 上传文件列表
});

// 提交表单的函数
const submitForm = () => {
  console.log('提交表单:', formData.value);
  // 提交逻辑，例如发送请求
  drawerVisible.value = false; // 提交后关闭抽屉
};

onMounted(()=>{
  getHWData();
  console.log('HomeWork.vue: onMounted：获取作业列表', tableData.value);
})
</script>

<style lang="scss" scoped>
.container {
  padding: 20px;

  .el-table {
    padding: 20px 0;
    border-radius: 5px;
  }
}

.buttons {
  display: flex;
  justify-content: center;
  margin-top: 20px;

  .el-button {
    margin: 0 50px;
  }
}
</style>
