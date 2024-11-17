<template>
  <div class="notice-container">
    <div class="header">
      <vertical-bar text="通知"></vertical-bar>
      <template v-if="userInfo.type === '1'" >
        <el-button @click="dialogVisible = true" style="margin-right: 20px">发布通知</el-button>
      </template>
      <el-dialog
          title="上传通知"
          v-model="dialogVisible"
          width="600px"
          @close="resetFormData"
      >
        <el-form ref="uploadForm" :model="uploadFormData" :rules="uploadRules" class="form">
          <el-form-item label="通知名称" prop="name">
            <el-input
                v-model="uploadFormData.name"
                style="width: 240px"
                placeholder="请输入通知名称"
            ></el-input>
          </el-form-item>
          <el-form-item label="通知内容" prop="content">
            <el-input
                type="textarea"
                :rows="6"
                placeholder="请输入3000字以内的通知内容"
                v-model="uploadFormData.content"
            ></el-input>
          </el-form-item>
          <div class="buttons" style="margin-top: 20px; display: flex; justify-content: space-between">
            <el-button type="primary" @click="submitUploadForm">确定</el-button>
            <el-button @click="dialogVisible = false">取消</el-button>
          </div>
        </el-form>
      </el-dialog>
    </div>
    <div class="content">
      <el-table :data="tableData" style="width: 100%; border-radius: 5px">
        <el-table-column prop="name" label="通知名称" width="400" align="center"/>
        <el-table-column prop="date" label="日期" width="200" align="center"/>
        <el-table-column prop="poster" label="发布人" align="center"/>
        <el-table-column label="操作" align="center" >
          <template #default="scope">
            <el-button type="primary" @click="handlePreview(scope.row)">预览</el-button>
            <template v-if="userInfo.type === '1'" >
              <el-button type="primary" :icon="Edit" @click="handleEdit(scope.row)" ></el-button>
              <el-button type="danger" :icon="Delete" @click="handleDelete(scope.row)"></el-button>
              <el-dialog
                  title="编辑通知"
                  v-model="editDialogVisible"
                  width="600px"
                  @close="resetFormData"
              >
                <el-form ref="editForm" :model="uploadFormData" :rules="uploadRules" class="form">
                  <el-form-item label="通知名称" prop="name">
                    <el-input
                        v-model="uploadFormData.name"
                        style="width: 240px"
                        placeholder="请输入通知名称"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="通知内容" prop="content">
                    <el-input
                        type="textarea"
                        :rows="6"
                        placeholder="请输入3000字以内的通知内容"
                        v-model="uploadFormData.content"
                    ></el-input>
                  </el-form-item>
                  <div class="buttons" style="margin-top: 20px; display: flex; justify-content: space-between">
                    <el-button type="primary" @click="submitEditForm(scope.row)">确定</el-button>
                    <el-button @click="editDialogVisible = false">取消</el-button>
                  </div>
                </el-form>
              </el-dialog>
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
import {computed, onMounted, ref} from 'vue';
import VerticalBar from "../../components/VerticalBar.vue";
import {useUserStore} from "@/stores/user.js";
import {useCourseStore} from "@/stores/course.js";
import {Delete, Edit} from "@element-plus/icons-vue";
import {deleteNotificationService, getNotificationListService, notificationUploadService} from "@/api/course.js";
import {ElMessage, ElMessageBox} from "element-plus";

const userStore = useUserStore(); // 获取用户角色
const userInfo = computed(() => userStore.user)
const courseStore = useCourseStore()
const courseInfo = computed( () => courseStore.course);

const dialogVisible = ref(false);
const contentDialogVisible = ref(false);
const selectedNoticeContent = ref('');

const uploadFormData = ref({
  name: '',
  content: '',
});
const uploadForm = ref();
const uploadRules = {
  name: [
    { required: true, message: '请输入通知名称', trigger: 'blur' },
    { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入描述', trigger: 'blur' },
    { max: 3000, message: '描述不能超过3000字', trigger: 'blur' }
  ],
};


const tableData = [
  {
    name : '通知1',
    date: '2024/10/6',
    content: 'wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwqeqw',
    poster: 'teacher Li',
  }
]

const resetFormData = () => {
  uploadFormData.value = {
    name: '',
    content: '',
  };
};


// 点击预览按钮，显示对应的通知内容
const handlePreview = (row) => {
  selectedNoticeContent.value = row.content;
  contentDialogVisible.value = true;
  console.log('点击按钮');
}

// ******************************************提交上传表单
const submitUploadForm = async () => {
  await uploadForm.value.validate();
  let uploadTime = new Date().toISOString() // 自动生成当前日期
  let cid = courseInfo.value.cid
  let tno = userInfo.value.tno
  try {
    //                                              name, cid,tno,uploadTime, content
    await notificationUploadService(uploadFormData.value.name,cid,tno,uploadTime,uploadForm.value.content );
    ElMessage.success("上传成功");
    resetFormData()
  } catch (error) {
    console.error(error);
    ElMessage.error("请求出错！");
  }
}

// *******************************************编辑和删除通知******************************
const editDialogVisible = ref(false);
const editForm = ref();

const handleEdit = (row) => {
  uploadFormData.value = { ...row }; // 填充表单为选中通知的内容
  editDialogVisible.value = true;
};
// 提交编辑通知表单
const submitEditForm = async (row) => {
  try {
    await editForm.value.validate(); // 验证表单
    uploadFormData.value.name = row.name
    uploadFormData.value.content = row.content
    let cid = courseInfo.value.cid
    let tno = userInfo.value.tno
    await notificationUploadService(uploadFormData.value.name,cid,tno,row.uploadTime,uploadForm.value.content); // 调用上传服务发送更新请求
    ElMessage.success("通知编辑成功！");
    resetFormData()
    editDialogVisible.value = false; // 关闭编辑弹窗
    await fetchNoticeList(); // 刷新通知列表
  } catch (error) {
    console.error(error);
    ElMessage.error("编辑通知时出错！");
  }
};

const handleDelete = async (row) => {
  try {
    const confirm = await ElMessageBox.confirm(`确定删除通知 "${row.name}" 吗？`, "提示");
    if (confirm) {
      await deleteNotificationService(row.id); // 假设后端需要通知ID
      ElMessage.success("通知删除成功！");
      await fetchNoticeList(); // 刷新列表
    }
  } catch (error) {
    console.error(error);
    ElMessage.error("删除通知时出错！");
  }
};


// 获取通知列表
const fetchNoticeList = async () => {
  try {
    const res = await getNotificationListService(courseInfo.value.cid); // 假设需要课程ID
    tableData.value = res.data.notificationList; // 将后端返回数据赋值给表格
  } catch (error) {
    console.error(error);
    ElMessage.error("请求通知列表时出错！");
  }
}

onMounted(async () => {
  await fetchNoticeList()
  console.log('userInfo.value.tname', userInfo.value.teacherName);
})
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