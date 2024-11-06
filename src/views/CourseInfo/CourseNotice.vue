<template>
  <div class="notice-container">
    <div class="header">
      <vertical-bar text="通知"></vertical-bar>
      <template v-if="userInfo.type === '1'" >
        <el-button @click="dialogVisible = true" style="margin-right: 20px">发布通知</el-button>
      </template>
      <el-dialog
          title="上传大纲"
          v-model="dialogVisible"
          width="600px"
      >
        <el-form ref="form" :model="uploadFormData" :rules="uploadRules" class="form">
          <el-form-item label="课程大纲名称" prop="courseWareTitle">
            <el-input
                v-model="uploadFormData.outlineTitle"
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
    <div class="content">
      <el-table :data="tableData" stripe style="width: 100%; border-radius: 5px">
        <el-table-column prop="theme" label="主题" width="400" />
        <el-table-column prop="date" label="日期" width="200" />
        <el-table-column prop="checkTimes" label="查看次数" width="200" />
        <el-table-column prop="poster" label="发布人" />
        <el-table-column label="操作" align="center" >
          <template #default="scope">
            <el-button type="primary" @click="handlePreview(scope.row)">预览</el-button>
            <template v-if="userInfo.type === '1'" >
              <el-button type="primary" :icon="Edit" @click="editDialogVisible = true" ></el-button>
              <el-button type="danger" :icon="Delete" @click="handleDelete"></el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 弹窗显示通知详细内容 -->
    <el-dialog v-model="contentDialogVisible" title="通知详情" style="max-height: 800px">
      <div style="height: 500px">
        <span>{{ selectedNoticeContent }}</span>
      </div>
      <template #footer>
        <el-button type="info" @click="contentDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {computed, ref} from 'vue';
import VerticalBar from "../../components/VerticalBar.vue";
import {useUserStore} from "@/stores/user.js";
import {useCourseStore} from "@/stores/course.js";
import {Delete, Edit} from "@element-plus/icons-vue";

const userStore = useUserStore(); // 获取用户角色
const userInfo = computed(() => userStore.user)
const courseStore = useCourseStore()
const courseInfo = computed( () => courseStore.course);

const dialogVisible = ref(false);
const contentDialogVisible = ref(false);
const selectedNoticeContent = ref('');

const uploadFormData = ref({
  outlineTitle: '',
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


const tableData = [
  {
    theme: '通知1',
    date: '2024/10/6',
    checkTimes: 0,
    poster: 'teacher Li',
    content: '这是通知1的详细内容。',
  },
  {
    theme: '通知2',
    date: '2024/10/6',
    checkTimes: 0,
    poster: 'teacher Li',
    content: '这是通知2的详细内容。',
  },
  {
    theme: '通知3',
    date: '2024/10/6',
    checkTimes: 0,
    poster: 'teacher Li',
    content: '这是通知3的详细内容。',
  }
]


// 点击预览按钮，显示对应的通知内容
const handlePreview = (row) => {
  selectedNoticeContent.value = row.content;
  contentDialogVisible.value = true;
  console.log('点击按钮');
}
</script>

<style lang="scss" scoped>
.notice-container{
  height: 100%;
  .header {
    padding-top: 10px;
    margin-left: 10px;
    display: flex;
    justify-content: space-between;
    //border: 1px solid black;
  }
  .content{
    height: calc(100vh - 205px);// 刚好填满tc-container-header的高度
    //border: 1px solid black;
    padding: 20px;
    border-radius: 5px;
    //background-color: #9f7390;
    font-size: 14px;
    min-height: 20px;
  }
}

</style>