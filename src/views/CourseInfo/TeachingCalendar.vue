<template>
  <div class="tc-container">
    <div class="header">
      <vertical-bar text="教学日历"></vertical-bar>
      <template v-if="userInfo.type === '1'" >
        <el-button @click="dialogVisible = true" style="margin-right: 20px">上传日历</el-button>
      </template>
      <el-dialog
          title="上传日历"
          v-model="dialogVisible"
          width="600px"
      >
        <el-form ref="form" :model="uploadFormData" :rules="uploadRules" class="form">
          <el-form-item label="教学日历名称" prop="courseWareTitle">
            <el-input
                v-model="uploadFormData.calendarTitle"
                style="width: 240px"
                placeholder="选填"
            ></el-input>
          </el-form-item>
          <el-form-item label="描述" prop="content">
            <el-input
                type="textarea"
                :rows="6"
                placeholder="选填，3000字以内"
                v-model="uploadFormData.content"
            ></el-input>
          </el-form-item>
          <div>
            <el-button type="text" @click="selectFile">选择文件</el-button>
            <span v-if="uploadFormData.fileName">{{ uploadFormData.fileName }}</span>
            <input type="file" ref="fileInput" style="display: none" @change="handleFileChange" />
          </div>
          <!-- 分割线 -->
          <el-divider style="height: 2px; background-color: #a8a2a2; margin: 10px 0"></el-divider>
          <div class="el-upload__tip" style="margin: 0;padding: 0">
            允许上传的文件类型: doc, pdf, ppt, xls, docx, pptx, xlsx, jpg, gif, jpeg, png, bmp
          </div>
          <div class="buttons" style="margin-top: 20px; display: flex; justify-content: space-between">
            <el-button type="primary" @click="submitUploadForm">确定</el-button>
            <el-button @click="dialogVisible = false">取消</el-button>
          </div>
        </el-form>
      </el-dialog>
    </div>
    <div class="pdfContainer">
      <div class="content">
        <iframe :src="fileUrl"
                width="100%"
                height="100%"
                allow="fullscreen"></iframe>
      </div>
    </div>
  </div>
</template>

<script setup>
import VerticalBar from "../../components/VerticalBar.vue";
import {useUserStore} from "@/stores/user.js";
import {useCourseStore} from "@/stores/course.js";
import {computed, onMounted, ref} from "vue";
import {ElMessage} from "element-plus";
import {calendarPreviewService, calendarUploadService} from "@/api/course.js";

const userStore = useUserStore(); // 获取用户角色
const userInfo = computed(() => userStore.user)
const courseStore = useCourseStore()
const courseInfo = computed( () => courseStore.course);

const dialogVisible = ref(false);
const uploadFormData = ref({
  calendarTitle: '',
  content: '',
  file: null,
  fileName: '',
});
const uploadRules = {
  courseWareTitle: [
    { required: false, message: '请输入课程资源名称', trigger: 'blur' },
    { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
  ],
  content: [
    { required: false, message: '请输入描述', trigger: 'blur' },
    { max: 3000, message: '描述不能超过3000字', trigger: 'blur' }
  ]
};

const fileUrl = ref('')

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
// 提交表单的函数
const submitUploadForm = async () => {
  console.log('提交表单:', uploadFormData.value);
  try {
    if (uploadFormData.value.file) {
      await calendarUploadService(uploadFormData.value.file, courseInfo.value.cid)
      ElMessage.success('文件上传成功')
      dialogVisible.value = false
    } else {
      ElMessage.info('请先选择文件')
    }
  } catch (error) {
    ElMessage.error('上传失败', error)
    console.log('Courseware.vue: 上传失败222222222222222222222222', error)
  }
};

// ***************************************************预览教学日历**************************************************
// 点击文件名
const previewTeachingCalendar = async () => {
    // 预览文件
    console.log("TeachingCalendar.vue  222222222222222222222222222222222222222222222 fileUrl.value：", fileUrl.value);
    const res = await calendarPreviewService(courseInfo.value.cid);
    fileUrl.value = res.data.previewLink;
};

onMounted(() => {
  previewTeachingCalendar();
})
</script>

<style lang="scss" scoped>
.tc-container{
  height: 100%;
  //border: 1px solid black;
  .header {
    padding-top: 10px;
    margin-left: 10px;
    display: flex;
    justify-content: space-between;
    //border: 1px solid black;
  }
  .pdfContainer{
    height: calc(100vh - 205px);// 刚好填满tc-container-header的高度
    padding: 20px;
    //border: 1px solid black;
    .content{
      //padding: 10px;
      //border-radius: 5px;
      //background-color: #9f7390;
      //color: black;
      //font-size: 14px;
      height: 100%;
      //border: 5px solid black;
    }
  }
}

</style>