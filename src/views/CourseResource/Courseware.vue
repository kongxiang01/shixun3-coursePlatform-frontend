<template>
  <div class="container" style="">
    <el-row class="resource-container">
      <el-col class="directory-container" :span="5">
<!--        :expand-on-click-node="false"-->
<!--        highlight-current-->
<!--        default-expand-all-->
        <el-tree
            class="directory"
            :data="treeData"
            :props="defaultProps"
            @node-click="handleNodeClick"
            node-key="label"
            :default-expanded-keys="['课程资源']"
        ></el-tree>
      </el-col>
      <el-col class="resource-table-container" :span="19">
        <el-row class="some-buttons">
          <!-- 判断用户是否是教师，只有教师显示这些按钮 -->
          <div style="margin: 10px">
            <template v-if="userInfo.type === '1'">
              <el-button @click="drawerVisible = true" type="primary">上传文件</el-button>
              <el-button @click="createFolderVisible = true">新建目录</el-button>
<!--              <el-button @click="moveItem">移动</el-button>-->
              <el-button @click="deleteItems">删除</el-button>
<!--              <el-button @click="publishItem">发布</el-button>-->
<!--              <el-button @click="unpublishItem">取消发布</el-button>-->
            </template>
            <el-dialog
                title="上传课件"
                v-model="drawerVisible"
                width="600px"
            >
              <el-form ref="uploadForm" :model="uploadFormData" :rules="uploadRules" class="form">
                <el-form-item label="课程资源名称" prop="courseWareTitle">
                    <el-input
                        v-model="uploadFormData.courseWareTitle"
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
                <!-- 上传文件按钮 -->
                <!-- 选择文件按钮 -->
                <div>
                  <el-button type="text" @click="selectFile">选择文件</el-button>
                  <span v-if="uploadFormData.fileName">{{ uploadFormData.fileName }}</span>
                  <input type="file" ref="fileInput" style="display: none" @change="handleFileChange" />
                </div>
                <!-- 分割线 -->
                <el-divider style="height: 2px; background-color: #a8a2a2; margin: 10px 0"></el-divider>
                <div class="el-upload__tip" style="margin: 0;padding: 0">
                  请勿上传与已存在文件同名的文件，否则已存在文件将被替换。
                  允许上传的文件类型: doc, pdf, ppt, xls, docx, pptx, xlsx, jpg, gif, jpeg, png, bmp
                </div><!--doc, pdf, ppt, xls, docx, pptx, xlsx, jpg, gif, jpeg, png, bmp-->
                <div class="buttons" style="margin-top: 20px; display: flex; justify-content: space-between">
                  <el-button type="primary" @click="submitUploadForm">确定</el-button>
                  <el-button @click="handleSubmitCancel">取消</el-button>
                </div>
              </el-form>
            </el-dialog>
            <el-dialog
                title="新建目录"
                v-model="createFolderVisible"
                width="600px"
            >
              <el-form ref="createForm" :model="createFolderFormData" :rules="createRules" class="form">
                <el-form-item label="目录名称" prop="folderName">
                  <el-input
                      v-model="createFolderFormData.folderName"
                      style="width: 240px"
                      placeholder=""
                  ></el-input>
                </el-form-item>
                <el-form-item label="描述" prop="describe">
                  <el-input
                      type="textarea"
                      :rows="6"
                      placeholder="选填，3000字以内的描述！"
                      v-model="createFolderFormData.describe"
                  ></el-input>
                </el-form-item>
                <!-- 确定和取消按钮 -->
                <div class="buttons" style="margin-top: 20px; display: flex; justify-content: space-between">
                  <el-button type="primary" @click="submitCreateFolderForm">确定</el-button>
                  <el-button @click="handleCreateCancel">取消</el-button>
                </div>
              </el-form>
            </el-dialog>
          </div>
        </el-row>
        <el-table :data="tableData" @selection-change="handleSelectionChange" class="resource-table">
          <el-table-column type="selection" width="50" align="center"/>
          <el-table-column label="文件名称">
            <template #default="scope">
              <el-link @click="handleNameClick(scope.row)">
                <el-icon v-if="scope.row.type === 'directory'">
                  <folder />
                </el-icon>
                <el-icon v-else>
                  <document />
                </el-icon>
                {{ scope.row.label }}
              </el-link>
            </template>
          </el-table-column>
          <el-table-column prop="type" label="属性" align="center"/>
          <!-- 操作列 -->
          <el-table-column label="操作" width="120" align="center">
            <template #default="scope">
              <div style="display: flex;justify-content: space-between">
                <el-link v-if="scope.row.type === 'file'" type="primary" @click="downloadFile(scope.row)">下载</el-link>
                <el-link type="danger" @click="deleteItem(scope.row)">删除</el-link>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import {computed, onMounted, ref} from 'vue';
import {Folder, Document } from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox} from "element-plus";
import { useUserStore } from "@/stores/user.js";
import {useCourseStore} from "@/stores/course.js";
import {createFolderService, getDirectoryService, getDownloadFileService, uploadCourseWareService} from "@/api/user.js";
import { useRouter } from 'vue-router'
import {deleteItemsService} from "@/api/asset.js";

const courseStore = useCourseStore()
const courseInfo = computed( () => courseStore.course);
console.log('CoursePage.vue:   courseInfo.value.cid: ', courseInfo.value.cid)

const input = ref('');
const treeData = ref([]);
const tableData = ref([]);
const currentPath = ref('E:/ICPlatformStorage/CourseResources' + '/' + courseInfo.value.cid); //  + '/' + '电子课件'
const treePath = ref('E:/ICPlatformStorage/CourseResources' + '/' + courseInfo.value.cid); // 初始化路径
console.log('CoursePage.vue:   treePath.value: ', treePath.value)

const userStore = useUserStore(); // 获取用户角色
const userInfo = computed(() => userStore.user)

const router = useRouter();
const validateForm = ref();

// 定义 el-tree 的 prop 配置
const defaultProps = {
  label: 'label',
  path: 'path',
  children: 'children',
};

// 用于存储选中的行
const selectedItems = ref([]);

// 更新选中的行
const handleSelectionChange = (selection) => {
  selectedItems.value = selection;
  console.log('CCCCCCCCCCCCCCCCourseWare.vue: selectedItems.value', selectedItems.value)
};

// 删除选中的行
const deleteItems = async () => {
  if (selectedItems.value.length === 0) {
    ElMessage.warning('请先选择要删除的项');
    return;
  }
  try {
    // 假设后端接收一个包含文件ID的数组
    await ElMessageBox.confirm(
        '确认要删除这些资源吗？此操作不可撤回',
        '警告',
        {
          confirmButtonText: '删除',
          cancelButtonText: '取消',
          type: 'error',
        }
    );
    const labelsToDelete = selectedItems.value.map((item) => item.label);
    console.log('DDDDDDDDDDDDDDDDDDDDDDDDDDDCourseWare.vue: labelsToDelete', labelsToDelete)
    let exampleItem = labelsToDelete[0]
    await deleteItemsService(labelsToDelete);
    ElMessage.success('成功删除: ' + labelsToDelete);
  } catch (error) {
    ElMessage.error('已取消删除或删除请求出错');
    console.error(error);
  }
};
// 删除一行
const deleteItem = async (row) => {
  try {
    await ElMessageBox.confirm(
        '确认要删除这些资源吗？此操作不可撤回',
        '警告',
        {
          confirmButtonText: '删除',
          cancelButtonText: '取消',
          type: 'error',
        }
    );
    const labelToDelete = [row.label];
    console.log('DDDDDDDDDDDDDDDDDDDDDDDDDDDCourseWare.vue: labelsToDelete', labelToDelete)
    await deleteItemsService(labelToDelete);
    ElMessage.success('成功删除: ' + labelToDelete);
  } catch (error) {
    ElMessage.error('已取消删除或删除请求出错');
    console.error(error);
  }
};

const drawerVisible = ref(false);
const createFolderVisible = ref(false);
// 表单数据
const uploadFormData = ref({
  courseWareTitle: '',
  content: '',
  file: null,
  fileName: '',
});
const uploadForm = ref()
const createFolderFormData = ref({
  folderName: '',
  describe: '',
});
const createForm = ref()

// ******************************************************上传文件**********************************************************
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

const createRules = {
  folderName: [
    { required: true, message: '请输入目录名称', trigger: 'blur' },
    { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur'}
  ],
  describe: [
    { required: false, message: '描述不能超过3000字', trigger: 'blur'},
    { max: 3000, message: '描述不能超过3000字', trigger: 'blur' }
  ]
}

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
  await uploadForm.value.validate()
  console.log('提交表单:', uploadFormData.value);
  try {
    if (uploadFormData.value.file) {
      console.log('Courseware.vue: currentPath.value:', currentPath.value);
      await uploadCourseWareService(uploadFormData.value.file, courseInfo.value.cid, currentPath.value)
      ElMessage.success('文件上传成功')
      uploadFormData.value = {
        courseWareTitle: '',
        content: '',
        file: null,
        fileName: '',
      };
      drawerVisible.value = false;
      window.location.reload();
    } else {
      ElMessage.info('请先选择文件')
    }
  } catch (error) {
    ElMessage.error('上传失败', error)
    console.log('Courseware.vue: 上传失败222222222222222222222222', error)
  }
};

const handleSubmitCancel = () => {
  // 清空表单和文件
  uploadFormData.value = {
    courseWareTitle: '',
    content: '',
    file: null,
    fileName: '',
  };
  drawerVisible.value = false; // 关闭对话框
};

// ******************************************************新建目录**********************************************************
const submitCreateFolderForm = async () => {
  console.log('提交表单:', uploadFormData.value);
  try {
    await createForm.value.validate()
    console.log('Courseware.vue5555555555555555555555555: createFolderFormData.value.folderName:', createFolderFormData.value.folderName);
    const folderPath = `${currentPath.value}/${createFolderFormData.value.folderName}`;
    await createFolderService(folderPath)
    ElMessage.success('新建目录成功')
    createFolderFormData.value.folderName = ''
    createFolderFormData.value.describe = ''
    createFolderVisible.value = false
  } catch (error) {
    ElMessage.error('上传失败', error)
    console.log('Courseware.vue: 上传失败222222222222222222222222', error)
  }
};
const handleCreateCancel = () => {
  // 清空表单和文件
  createFolderFormData.value = {
    folderName: '',
    describe: '',
  };
  createFolderVisible.value = false; // 关闭对话框
};
// *****************************************************显示目录***************************************************
// 递归函数：过滤出所有 type 为 'directory' 的项
const filterDirectories = (data) => {
  return data
      .filter(item => item.type === 'directory')  // 只保留 type 为 'directory' 的项
      .map(item => ({
        ...item,
        children: item.children ? filterDirectories(item.children) : [] // 递归处理子目录
      }));
};

// 从后端获取目录结构
const fetchFolderStructure = async () => {
  try {
    const response = await getDirectoryService(treePath.value);
    // console.log(' response.data.folderStructure：', response.data.folderStructure);
    treeData.value = filterDirectories(response.data.folderStructure);
    console.log(' 22222222222222222222222222222222222222222222222222222222treeData.value：', treeData.value);
    console.log(' 22222222222222222222222222222222222222222222222222222222treeData.value[0].label：', treeData.value[0].label);
  } catch (error) {
    console.log('Courseware.vue: fetch 目录结构失败：', error);
  }
};

// 处理节点点击事件，获取对应目录下的文件
const handleNodeClick = async (node) => {
  if (node.type === 'directory') {
    try {
      currentPath.value = node.path;
      console.log('Courseware.vue:  node.path: 333333333333333', node.path)
      const response = await getDirectoryService(currentPath.value)
      tableData.value = response.data.folderStructure;
      // console.log('Courseware.vue:  tableData.value: ', tableData.value)
      // console.log('node.label: ', node.label)
    } catch (error) {
      console.error('Courseware.vue: Failed to fetch files', error);
    }
  }
};

// 获取目录内容
const fetchDirectoryContents = async () => {
  try {
    const response = await getDirectoryService(currentPath.value)
    tableData.value = response.data.folderStructure;
  } catch (error) {
    ElMessage.error('获取内容失败');
  }
};

// ***************************************************点击文件名预览课件**************************************************
// 点击文件名
const handleNameClick = (item) => {
  if (item.type === 'directory') {
    // 打开目录
    currentPath.value = `${currentPath.value}/${item.label}`;
    console.log('currentPath.value11111111111: ' + currentPath.value)
    // breadcrumb.push({ label: item.label, path: currentPath.value });
    fetchDirectoryContents();
  } else {
    // 预览文件
    const fileName = item.label;
    console.log("Courseware.vue: iiiiiiiiiiiiiiiiiiiiiiiiii  item.label：", item.label);
    router.push({ name: 'FilePreview', query: { fileName: fileName } });
  }
};

// ******************************************************下载文件*******************************************
const downloadFile = async (item) => {
  try {
    // 获取带 Token 的下载链接

    console.log("Courseware.vue: qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqitem.label：", item.label);
    const res = await getDownloadFileService(item.label); // 假设你在 api 中定义了 getDownloadFileService
    const downloadUrl = res.data.downloadLink;
    console.log("Courseware.vue: qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq  downloadUrl：", downloadUrl);
    // 创建一个临时的 <a> 元素并触发点击事件来下载文件
    const link = document.createElement("a");
    link.href = downloadUrl;
    link.download = item.label;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  } catch (error) {
    console.error("下载文件失败:", error);
    ElMessage.error("下载文件失败，请重试");
  }
};

const moveItem = () => {
  // 实现移动功能逻辑
};

const publishItem = () => {
  // 实现发布功能逻辑
};

const unpublishItem = () => {
  // 实现取消发布功能逻辑
};

// 在组件挂载时获取数据
onMounted(() => {
  fetchFolderStructure();
  fetchDirectoryContents();
});


</script>

<style lang="scss" scoped>
.container{
  max-height: 100%;
  max-width: 100%;
  //border: 1px solid black;
  padding: 20px;

  .resource-container {
    //display: flex;
    .directory-container {
      //width: 30%;
      //border-right: 1px solid #ccc;
      .directory{
        min-height: 500px;
        background-color: #f4dbed;
        border-radius: 10px;
      }
    }
  }

  .resource-table-container {
    //width: 70%;
    padding-left: 20px;

    .search-input{
      width: 300px;
      margin: 10px;
      .search-button{
        background-color: rgb(255, 158, 244);
        //width: 200px;
        //margin: 0 -20px;// 自带的样式
      }
    }

    .resource-table{
      background-color: #f4dbed;
      border-radius: 10px;
    }
  }
}

</style>
