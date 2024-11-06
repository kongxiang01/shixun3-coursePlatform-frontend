<template>
  <template v-if="userInfo.type === '0'">
    <div class="container">
      <el-table :data="tableData">
        <el-table-column prop="homeworkName" label="作业标题" align="center">
          <template #default="scope">
            <el-link type="primary" :underline="false" @click="handleHW">{{scope.row.homeworkName}}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="start" label="作业开始" align="center"></el-table-column>
        <el-table-column prop="end" label="作业截止" align="center"></el-table-column>
        <el-table-column prop="submitRatio" label="提交人数" align="center"></el-table-column>
        <el-table-column prop="submitTime" label="提交时间" align="center"></el-table-column>
        <el-table-column prop="score" label="得分" align="center"></el-table-column>
        <el-table-column prop="reviestatus" label="批改状态" align="center"></el-table-column>
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
  <template v-else-if="userInfo.type === '1'">
    <div class="container">
      <div class="header" style="margin-bottom: 5px">
        <vertical-bar text="课程作业"></vertical-bar>
      </div>
      <div style="margin-bottom: 5px">
        <el-button size="default" @click="assignVisible = true">布置作业</el-button>

        <el-button size="default" @click="goToHomeworkDetail">跳转作业详情</el-button>

        <el-dialog
            title="布置作业"
            v-model="assignVisible"
            width="600px"
            :close-on-click-modal="false"
        >
          <el-form ref="form" :model="assignFormData" class="form">
            <el-form-item label="作业名称">
              <div>
                <el-input
                    v-model="assignFormData.assignHomeworkTitle"
                    style="width: 240px"
                    placeholder=""
                ></el-input>
              </div>
            </el-form-item>
            <el-form-item label="作业满分">
              <div>
                <el-input
                    v-model="assignFormData.fullScore"
                    style="width: 40px"
                    placeholder=""
                ></el-input>
                <span style="margin-left: 5px">分</span>
              </div>
            </el-form-item>
            <div>
              <el-form-item label="作业内容">
                <el-input
                    type="textarea"
                    :rows="6"
                    placeholder="3000字以内！"
                    v-model="assignFormData.content"
                ></el-input>
              </el-form-item>
            </div>
            <el-form-item label="提交时间">
              <el-date-picker
                  v-model="assignFormData.timeRange"
                  type="datetimerange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  format="YYYY-MM-DD HH:mm"
                  value-format="YYYY-MM-DDTHH:mm:ss"
                  style="width: 100%;"
              />
            </el-form-item>
            <div>
              <el-button type="text" @click="selectFile">选择文件</el-button>
              <span v-if="uploadFormData.fileName" style="margin-left: 10px">{{ uploadFormData.fileName }}</span>
              <input type="file" ref="fileInput" style="display: none" @change="handleFileChange" />
            </div>
            <!-- 分割线 -->
            <el-divider style="height: 2px; background-color: #a8a2a2; margin: 10px 0"></el-divider>
            <!-- 文件上传提示 -->
            <div class="el-upload__tip" style="margin: 0;padding: 0">
              允许上传的文件类型: doc, pdf, ppt, xls, docx, pptx, xlsx, jpg, gif, jpeg, png, bmp
            </div>
            <!-- 确定和取消按钮 -->
            <div class="buttons" style="margin-top: 20px; display: flex; justify-content: space-between">
              <el-button type="primary" @click="submitUploadForm">确定</el-button>
              <el-button @click="handleCancel">取消</el-button>
            </div>
          </el-form>
        </el-dialog>
      </div>
      <el-table :data="assignedTableData">
        <el-table-column prop="homeworkName" label="作业标题" align="center">
          <template #default="scope">
            <el-link type="primary" :underline="false" @click="handleHW">{{scope.row.homeworkName}}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="start" label="作业开始" align="center"></el-table-column>
        <el-table-column prop="end" label="作业截止" align="center"></el-table-column>
        <el-table-column prop="submitRatio" label="提交人数" align="center"></el-table-column>
        <el-table-column prop="correctHW" label="批阅" align="center"></el-table-column>
        <el-table-column label="操作" align="center">
          <template #default>
            <el-link type="primary" :underline="false" @click="handlePublish">公布成绩</el-link>
            <el-link type="primary" :underline="false" @click="handleDelete">删除</el-link>
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
            <div>
              <el-button type="text" @click="selectFile">选择文件</el-button>
              <span v-if="uploadFormData.courseWareTitle">{{ uploadFormData.courseWareTitle }}</span>
              <input type="file" ref="fileInput" style="display: none" @change="handleFileChange" />
            </div>
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
</template>

<script setup>
import {useRoute, useRouter} from "vue-router";
import {computed, onMounted, ref} from "vue";
import homeworkDetail from "@/views/CourseExam/HomeworkDetail/HomeworkDetail.vue";
import {
  getAssignedHomeworkListService,
  getHomeworkListService,
  uploadHomeworkService
} from "@/api/user.js";
import {useCourseStore} from "@/stores/course.js";
import {useUserStore} from "@/stores/user.js";
import VerticalBar from "@/components/VerticalBar.vue";
import {ElMessage} from "element-plus";

const route = useRoute();

const courseStore = useCourseStore()
const courseInfo = computed(() => courseStore.course)
const userStore = useUserStore()
const userInfo = computed(() => userStore.user)
const router = useRouter();

const tableData = ref([])
const assignedTableData = ref([])

// ******************************************************上传文件********************************************************
const drawerVisible = ref(false);
const assignVisible = ref(false);
// 表单数据
const uploadFormData = ref({
  homeworkTitle: '',
  content: '',
  file: null,
  fileName: '',
});

const assignFormData = ref({
  assignHomeworkTitle: '',
  content: '',
  file: null,
  fileName: '',
  fullScore: '',
  timeRange: []
});

// 选择文件
const selectFile = () => {
  document.querySelector('input[type="file"]').click()
}

// 文件变化的处理
const handleFileChange = (event) => {
  const file = event.target.files[0]
  if (file) {
    uploadFormData.value.file = file
    uploadFormData.value.fileName = file.name
  }
}

const submitUploadForm = async () => {
  console.log('提交上传作业表单:', uploadFormData.value);
  try {

    // 传输作业相关信息

    if (uploadFormData.value.file) {
      console.log('Homework.vue: uploadFormData.value.file:', uploadFormData.value.file);
      await uploadHomeworkService(uploadFormData.value.file, courseInfo.value.cid)
      ElMessage.success('作业附件上传成功')
    } else {
      ElMessage.info('请先选择文件')
    }
  } catch (error) {
    ElMessage.error('上传失败', error)
    console.log('Homework.vue: 上传失败222222222222222222222222', error)
  }
};


// ************************************************获取作业列表********************************************************
const getHWData = async () => {
  try {
    console.log('HomeWork.vue111111111111111:   courseInfo.value.cid, userInfo.sno:', courseInfo.value.cid, userInfo.value.sno);
    const res = await getHomeworkListService(courseInfo.value.cid, userInfo.value.sno); // 向后端获取学生课程
    tableData.value = res.data.homeworkInfoList; // 将返回的数据赋值给courses
    console.log('HomeWork.vue22222222222222:   courseInfo.value.cid:', res.data.homeworkInfoList);
    console.log('HomeWork.vue3333333333333:   tableData:', tableData);
  } catch (error) {
    console.log('Homework.vue:获取作业列表失败:', error);
  }
};

const getassignedHWData = async () => {
  try {
    console.log('HomeWork.vue111111111111111:   courseInfo.value.cid, userInfo.sno:', courseInfo.value.cid, userInfo.value.sno);
    const res = await getAssignedHomeworkListService(courseInfo.value.cid, userInfo.value.sno); // 向后端获取学生课程
    tableData.value = res.data.homeworkInfoList; // 将返回的数据赋值给courses
    console.log('HomeWork.vue22222222222222:   courseInfo.value.cid:', res.data.homeworkInfoList);
    console.log('HomeWork.vue3333333333333:   tableData:', tableData);
  } catch (error) {
    console.log('Homework.vue:获取作业列表失败:', error);
  }
};


// 动态生成路径的函数
const generateCoursePath = (suffix) => {
  return `/course/${suffix}`;
};

const handleHW = () => {
  router.push(generateCoursePath('homeworkDetail'))
}

// 提交表单的函数
const submitForm = () => {
  console.log('提交表单:', formData.value);
  // 提交逻辑，例如发送请求
  drawerVisible.value = false; // 提交后关闭抽屉
};

// ************************************************布置作业对话框********************************************************
const handleCancel = () => {
  // 清空表单和文件
  assignFormData.value = {
    assignHomeworkTitle: '',
    content: '',
    file: null,
    fileName: '',
    fullScore: '',
    timeRange: []
  };
  uploadFormData.value.file = null;
  uploadFormData.value.fileName = '';

  assignVisible.value = false; // 关闭对话框
};

const goToHomeworkDetail = () => {
  router.push('/course/homeworkDetail')
}

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
