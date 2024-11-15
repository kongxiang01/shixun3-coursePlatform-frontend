<template>
  <template v-if="userInfo.type === '0'">
    <div class="container">
      <el-table :data="tableData">
        <el-table-column prop="cname" label="作业标题" align="center" width="300px">
          <template #default="scope">
            <el-link type="primary" :underline="false" @click="handleHWDetail(scope.row)">{{scope.row.cname}}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="start" label="作业开始" align="center">
          <template #default="scope">
            {{ formatDate(scope.row.start) }}
          </template>
        </el-table-column>
        <el-table-column prop="end" label="作业截止" align="center">
          <template #default="scope">
            {{ formatDate(scope.row.end) }}
          </template>
        </el-table-column>
        <el-table-column prop="submitRatio" label="提交人数" align="center" width="100px"></el-table-column>
        <el-table-column prop="submitTime" label="提交时间" align="center">
          <template #default="scope">
            {{ formatDate(scope.row.submitTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="score" label="得分" align="center" width="100px"></el-table-column>
        <el-table-column prop="reviestatus" label="批改状态" align="center" width="120px"></el-table-column>
        <el-table-column label="操作" align="center">
          <template #default="scope">
            <el-link v-if="scope.row.submitStatus === '1' && scope.row.submitTime !== '未提交'" type="primary" :underline="false" @click="handleView(scope.row)">查看</el-link>
            <el-link v-if="scope.row.submitStatus === '1' && scope.row.submitTime !== '未提交'" type="primary" :underline="false" @click="handleSubmit(scope.row)">提交</el-link>
            <el-link v-if="scope.row.submitStatus === '0' && scope.row.submitTime !== '未提交'" type="primary" :underline="false" @click="handleView(scope.row)">查看</el-link>
            <el-link v-if="scope.row.submitStatus === '1' && scope.row.submitTime === '未提交'" type="primary" :underline="false" @click="handleSubmit(scope.row)">提交</el-link>
          </template>
        </el-table-column>
      </el-table>

      <el-drawer
          title="提交作业"
          v-model="drawerVisible"
          direction="rtl"
          size="40%"
      >
        <el-form ref="validateForm" :model="uploadFormData" :rules="uploadRules" class="form">
          <el-form-item label="课程资源名称" prop="homeworkTitle">
            <el-input
                v-model="uploadFormData.homeworkTitle"
                style="width: 240px"
                placeholder=""
            ></el-input>
          </el-form-item>
          <el-form-item label="描述" prop="content">
            <el-input
                type="textarea"
                :rows="6"
                placeholder="请输入3000字以内的描述！"
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
            请勿上传与已存在文件同名的文件，否则已存在文件将被替换。
            允许上传的文件类型: doc, pdf, ppt, xls, docx, pptx, xlsx, jpg, gif, jpeg, png, bmp
          </div><!--doc, pdf, ppt, xls, docx, pptx, xlsx, jpg, gif, jpeg, png, bmp-->
          <div class="buttons" style="margin-top: 20px; display: flex; justify-content: space-between">
            <el-button type="primary" @click="submitUploadForm">确定</el-button>
            <el-button @click="handleSubmitCancel">取消</el-button>
          </div>
        </el-form>
      </el-drawer>
    </div>
  </template>
  <template v-else-if="userInfo.type === '1'">
    <div class="container">
      <div class="header" style="margin-bottom: 5px; display: flex; justify-content: space-between">
        <vertical-bar text="课程作业"></vertical-bar>
        <div>
          <el-button size="default" @click="assignVisible = true">布置作业</el-button>
          <el-button @click="deleteItem">批量删除</el-button>
          <el-button size="default" @click="goToHomeworkDetail">测试跳转</el-button>
          <el-dialog
              title="布置作业"
              v-model="assignVisible"
              width="600px"
              :close-on-click-modal="false"
          >
            <el-form ref="validateUploadForm" :model="assignFormData" :rules="assignRules" class="form">
              <el-form-item label="作业序号" prop="workId">
                <div>
                  <el-input
                      v-model="assignFormData.workId"
                      style="width: 240px"
                      placeholder=""
                  ></el-input>
                </div>
              </el-form-item>
              <el-form-item label="作业满分" prop="fullScore">
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
                <el-form-item label="作业内容" prop="content">
                  <el-input
                      type="textarea"
                      :rows="6"
                      placeholder="选填，3000字以内！"
                      v-model="assignFormData.content"
                  ></el-input>
                </el-form-item>
              </div>
              <el-form-item label="提交时间" porp="timeRange">
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
                <span v-if="assignFormData.fileName" style="margin-left: 10px">{{ assignFormData.fileName }}</span>
                <input type="file" ref="fileInput" style="display: none" @change="handleAssignFileChange" />
              </div>
              <!-- 分割线 -->
              <el-divider style="height: 2px; background-color: #a8a2a2; margin: 10px 0"></el-divider>
              <!-- 文件上传提示 -->
              <div class="el-upload__tip" style="margin: 0;padding: 0">
                允许上传的文件类型: doc, pdf, ppt, xls, docx, pptx, xlsx, jpg, gif, jpeg, png, bmp
              </div>
              <!-- 确定和取消按钮 -->
              <div class="buttons" style="margin-top: 20px; display: flex; justify-content: space-between">
                <el-button type="primary" @click="submitAssignForm">确定</el-button>
                <el-button @click="handleAssignCancel">取消</el-button>
              </div>
            </el-form>
          </el-dialog>
        </div>
      </div>
      <el-table :data="assignedTableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center"/>
        <el-table-column prop="cname" label="作业标题" align="center" width="300px">
<!--          <template #default="scope">-->
<!--            <el-link type="primary" :underline="false" @click="handleAssignHW">{{scope.row.cname}}</el-link>-->
<!--          </template>-->
        </el-table-column>
        <el-table-column prop="start" label="作业开始" align="center" width="200px">
          <template #default="scope">
            {{ formatDate(scope.row.start) }}
          </template>
        </el-table-column>
        <el-table-column prop="end" label="作业截止" align="center" width="200px">
          <template #default="scope">
            {{ formatDate(scope.row.end) }}
          </template>
        </el-table-column>
        <el-table-column prop="submitRatio" label="提交人数" align="center"></el-table-column>
        <el-table-column label="发布作业" align="center">
          <template #default="scope">
            <!--这里需要后端传来的assignedTableData里有publish这个键值-->
            <el-switch
                v-model="scope.row.publish"
                :active-value="1"
                :inactive-value="0"
                @change="handlePublishChange(scope.row)"
                size="large"
                inline-prompt
                active-text="已发布"
                inactive-text="未发布">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="公布分数" align="center">
          <template #default="scope">
            <!--这里需要后端传来的assignedTableData里有publish这个键值-->
            <el-switch
                v-model="scope.row.publishScore"
                :active-value="1"
                :inactive-value="0"
                @change="handlePublishScore(scope.row)"
                size="large"
                inline-prompt
                active-text="已公布"
                inactive-text="未公布">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="批阅" align="center">
          <template #default="scope">
            <el-button size="small" type="primary" :underline="false" @click="handleCorrect(scope.row)" :icon="Document"></el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template #default="scope">
            <el-button size="small" type="danger" :icon="Delete" @click="handleDelete(scope.row)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </template>
</template>

<script setup>
import {useRoute, useRouter} from "vue-router";
import {computed, onMounted, ref} from "vue";
import {uploadHomeworkService} from "@/api/user.js";
import {useCourseStore} from "@/stores/course.js";
import {useUserStore} from "@/stores/user.js";
import VerticalBar from "@/components/VerticalBar.vue";
import {ElMessage, ElMessageBox} from "element-plus";
import {
  assignHomeworkService,
  deleteAssignedHomeworkService,
  getAssignedHomeworkListService, setHomeworkPublishScoreService, setHomeworkPublishService,
  submitHomeworkService
} from "@/api/homework.js";
import {Delete, Document} from "@element-plus/icons-vue";
import {useHomeworkStore} from "@/stores/homework.js";

const route = useRoute();

const courseStore = useCourseStore()
const courseInfo = computed(() => courseStore.course)
const userStore = useUserStore()
const userInfo = computed(() => userStore.user)
const homeworkStore = useHomeworkStore()
const router = useRouter();

const tableData = ref([])
const assignedTableData = ref([])

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
  workId: '',
  content: '',
  file: null,
  fileName: '',
  fullScore: '',
  timeRange: []
});

const validateForm = ref()
const validateUploadForm = ref()

const formatDate = (dateStr) => {
  if(dateStr === '未提交'){
    return;
  }
  const date = new Date(dateStr);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}
// ******************************************************上传文件(交作业)********************************************************

const uploadRules = {
  homeworkTitle: [
    { required: false, message: '请输入课程资源名称', trigger: 'blur' },
    { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
  ],
  content: [
    { required: false, message: '请输入描述', trigger: 'blur' },
    { max: 3000, message: '描述不能超过3000字', trigger: 'blur' }
  ]
};
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
const submittingWorkId = ref()
const handleSubmit = (row) => {
  submittingWorkId.value = row.workid
  drawerVisible.value = true;
}
const submitUploadForm = async () => {
  // await validateUploadForm.value.validate()
  console.log('提交上传作业表单:', uploadFormData.value);
  try {
    // 传输作业相关信息
    if (uploadFormData.value.file) {
      console.log('Homework.vue: uploadFormData.value.file:', uploadFormData.value.file, courseInfo.value.cid, userInfo.value.sno, submittingWorkId.value);
      await submitHomeworkService(uploadFormData.value.file, courseInfo.value.cid, userInfo.value.sno, submittingWorkId.value) // homeworkFile, cid, sno, workid
      ElMessage.success('作业上传成功')
    } else {
      ElMessage.info('请先选择文件')
    }
  } catch (error) {
    ElMessage.error('上传失败', error)
    console.log('Homework.vue: 上传失败222222222222222222222222', error)
  }
};

const handleSubmitCancel = () => {
  // 清空表单和文件
  uploadFormData.value = {
    homeworkTitle: '',
    content: '',
    file: null,
    fileName: '',
  };
  drawerVisible.value = false; // 关闭对话框
};

// ************************************************获取作业列表********************************************************
const getHWData = async () => {
  try {
      console.log('HomeWork.vue: courseInfo.value.cid是',courseInfo.value.cid,'userInfo.value.type是',userInfo.value.type);
      if(userInfo.value.type === "1"){
        console.log('HomeWork.vue: userInfo.value.tno是',userInfo.value.tno);

        const res = await getAssignedHomeworkListService(courseInfo.value.cid, userInfo.value.tno); // 向后端获取学生课程
        assignedTableData.value = res.data.homeworkList; // 将返回的数据赋值给courses
      }else{
        const res = await getAssignedHomeworkListService(courseInfo.value.cid, userInfo.value.sno); // 向后端获取学生课程
        tableData.value = res.data.homeworkList;
      }

    // console.log('HomeWork.vue22222222222222:   courseInfo.value.cid:', res.data.homeworkInfoList);
    console.log('HomeWork.vue3333333333333:   tableData:', tableData.value);
    console.log('HomeWork.vue3333333333333:   assignedTableData:', assignedTableData.value);
  } catch (error) {
    ElMessage.error('Homework.vue:获取作业列表失败:', error);
  }
};

// ************************************************跳转作业信息页面********************************************************
const handleHWDetail = (row) => {
  homeworkStore.setHomework(row)
  console.log('HomeWork.vue:  homeworkStore.homework: ', homeworkStore.homework)
  router.push({ name: 'HomeworkDetail'});
}

// ************************************************布置作业********************************************************
const handleAssignHW = () => {
  // 这里是点击文件名的处理
}
const handleAssignCancel = () => {
  // 清空表单和文件
  assignFormData.value = {
    workId: '',
    content: '',
    file: null,
    fileName: '',
    fullScore: '',
    timeRange: []
  };
  assignVisible.value = false; // 关闭抽屉
};

// 文件变化的处理
const handleAssignFileChange = (event) => {
  const file = event.target.files[0]
  if (file) {
    assignFormData.value.file = file
    assignFormData.value.fileName = file.name
  }
}

const assignRules = {
  workId: [
    { required: true, message: '请输入作业序号', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        const id = Number(value);
        if (!Number.isInteger(id) || id < 1) {
          callback(new Error('作业序号必须是大于等于1的整数'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ],
  fullScore: [
    { required: true, message: '请填写作业满分', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        const score = Number(value);
        if (isNaN(score)) {
          callback(new Error('满分必须是数字'));
        } else if (score < 1 || score > 100) {
          callback(new Error('满分应在1到100之间'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ],
  content: [
    { required: false, message: '请输入作业内容', trigger: 'blur' },
    { max: 3000, message: '内容不能超过3000字', trigger: 'blur' }
  ],
  timeRange: [
    { type: 'array', required: true, message: '请选择提交时间范围', trigger: 'blur' },
  ],
};

const submitAssignForm = async () => {
  console.log('validateUploadForm.value: ', validateUploadForm.value)
  await validateUploadForm.value.validate()
  console.log('提交布置作业表单:', assignFormData);
  try {
    if (!assignFormData.value.file) {
      ElMessage.info('请先选择文件');
      return;
    }
    // 上传作业相关信息
    await assignHomeworkService(
        assignFormData.value.file,
        assignFormData.value.timeRange[0],
        assignFormData.value.timeRange[1],
        assignFormData.value.workId,
        courseInfo.value.cid,
        assignFormData.value.content,
        assignFormData.value.fullScore,
    );
    ElMessage.success('布置作业成功');
  } catch (error) {
    ElMessage.error('布置作业失败');
    console.error('66666666666666Homework.vue: 布置作业失败', error);
  }
};

// ****************************************************批阅和操作相关*****************************************
const goToHomeworkDetail = () => {
  router.push({ name: 'HomeworkDetail' });
  // router.push({ name: 'CorrectPreview' });
}
// 打开批阅页面
const handleCorrect = (row) => {
  ElMessage.info(row.cname)
  // 跳转到批阅页面，可以传递作业ID或其他参数
  router.push({ name: 'CorrectHomework', query: {workid: row.workid, fullMark: row.fullMark}});
};

// ****************************************************删除操作*****************************************
// 用于存储选中的行
const selectedItems = ref([]);

// 更新选中的行
const handleSelectionChange = (selection) => {
  selectedItems.value = selection;
  console.log('CCCCCCCCCCCCCCCCourseWare.vue: selectedItems.value', selectedItems.value)
};
// 批量删除选中的行
const deleteItem = async () => {
  if (selectedItems.value.length === 0) {
    ElMessage.warning('请先选择要删除的项');
    return;
  }
  try {
    await ElMessageBox.confirm(
        '确认要删除这些作业吗？此操作不可撤回',
        '警告',
        {
          confirmButtonText: '删除',
          cancelButtonText: '取消',
          type: 'error',
        }
    ); // 选取消会直接跳掉 catch 块里
    const labelsToDelete = selectedItems.value.map((item) => item.label);
    console.log('DDDDDDDDDDDDDDDDDDDDDDDDDDDCourseWare.vue: labelsToDelete', labelsToDelete)
    await deleteAssignedHomeworkService(labelsToDelete);
    ElMessage.success('成功删除: ' + labelsToDelete);
  } catch (error) {
    ElMessage.error('已取消删除或删除请求出错');
    console.error(error);
  }
};

// 删除单个作业
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
        '确认要删除该作业吗？此操作不可撤回',
        '警告',
        {
          confirmButtonText: '删除',
          cancelButtonText: '取消',
          type: 'error',
        }
    );

    const labelToDelete = [row.label];
    console.log('DDDDDDDDDDDDDDDDDDDDDDDDDDDCourseWare.vue: labelsToDelete', labelToDelete)
    await deleteAssignedHomeworkService(labelToDelete);
    ElMessage.success('成功删除: ' + labelToDelete);

    // 在这里执行删除逻辑，例如请求后端 API 删除作业
    ElMessage.success('作业已删除');
  } catch(error) {
    ElMessage.info('已取消删除或删除请求出错');
    console.error(error);
  }
};

// **************************************************发布相关*****************************************************
const handlePublishChange = async (row) => {
  try {
    console.log('row.publish: ', row.publish)
    console.log('row.publish: ', row)
    await setHomeworkPublishService( row.publish, courseInfo.value.cid, row.workid)
    ElMessage.success(row.publish ? '作业已发布' : '已撤销发布');
  } catch (error) {
    // ElMessage.info('已撤销操作');
  }
}
// 公布成绩
const handlePublishScore = async (row) => {
  try {
    console.log('row.publishScore: ', row.publishScore)
    await setHomeworkPublishScoreService( row.publishScore, courseInfo.value.cid, row.workid)
    ElMessage.success(row.publishScore ? '分数已公布' : '已撤销公布');
  } catch {
    // ElMessage.info('已撤销操作');
  }
};
const handleView = (row) =>{
  router.push({ name: 'StudentPreview', query: {cid: courseInfo.value.cid, sno: userInfo.value.sno, workid: row.workid}});
  // 预览学生自己提交的作业
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
