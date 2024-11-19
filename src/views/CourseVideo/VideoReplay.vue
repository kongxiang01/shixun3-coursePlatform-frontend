<template>
  <div class="buttons" style="margin: 0 10px">
    <template v-if="userInfo.type === '1'">
      <el-button type="primary" @click="uploadVisible = true" style="margin-right: 160px">上传视频</el-button>
      <el-button type="danger" :disabled="!activeVideo" @click="handleDelete">删除视频</el-button>
      <el-dialog
          title="上传视频"
          v-model="uploadVisible"
          width="500px"
          :close-on-click-modal="false"
      >
        <el-form ref="validateVideoForm" :model="videoFormData" :rules="videoRules" class="form">
          <!-- 视频标题 -->
          <el-form-item label="视频序号" prop="vid">
            <el-input
                v-model="videoFormData.vid"
                placeholder="请输入视频序号"
                style="width: 100%;"
            ></el-input>
          </el-form-item>
          <el-form-item label="上课时间" prop="timeRange">
            <el-date-picker
                v-model="videoFormData.timeRange"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                format="YYYY-MM-DD HH:mm"
                value-format="YYYY-MM-DDTHH:mm:ss"
                style="width: 100%;"
            />
          </el-form-item>
          <!-- 视频文件 -->
          <el-form-item label="选择视频文件" prop="file">
            <el-button type="text" @click="selectVideoFile">选择文件</el-button>
            <span v-if="videoFormData.fileName" style="margin-left: 10px">{{ videoFormData.fileName }}</span>
            <input type="file" ref="videoFileInput" accept="video/*" style="display: none" @change="handleVideoFileChange" />
          </el-form-item>
          <!-- 分割线 -->
          <el-divider style="height: 2px; background-color: #a8a2a2; margin: 10px 0"></el-divider>
          <!-- 文件上传提示 -->
          <div class="el-upload__tip" style="margin: 0;padding: 0">
            允许上传的视频类型: mp4, avi, mkv, mov
          </div>

          <!-- 确定和取消按钮 -->
          <div class="buttons" style="margin-top: 20px; display: flex; justify-content: space-between">
            <el-button type="primary" @click="submitVideoForm">确定</el-button>
            <el-button @click="handleUploadCancel">取消</el-button>
          </div>
        </el-form>
      </el-dialog>
    </template>
  </div>
  <div class="video-replay">
    <!-- 左侧视频列表 -->
    <div class="video-list">
      <el-scrollbar class="scrollbar">
        <el-card
            v-for="(video, index) in videoList"
            :key="video.vid"
            class="video-card"
            @click="playVideo(video)"
            :class="{ active: activeVideo?.vid === video.vid }"
        >
          <div class="video-info">
            <p class="video-title">课程回放 {{ video.vid }}</p>
          </div>
        </el-card>
      </el-scrollbar>
    </div>

    <!-- 右侧视频播放区域 -->
    <div class="video-player">
      <video
          v-if="activeVideo"
          controls
          :src="activeVideoUrl"
          class="player"
      ></video>
      <p v-else class="placeholder">请选择一个视频进行播放</p>
    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, ref} from 'vue';
import { useUserStore } from "@/stores/user.js";
import {uploadVideoService, deleteVideoService, getVideoListService, previewVideoService} from "@/api/video.js";
import {ElMessage} from "element-plus";
import {useCourseStore} from "@/stores/course.js";

const userStore = useUserStore();
const userInfo = userStore.user;
const courseStore = useCourseStore()
const courseInfo = computed( () => courseStore.course);

const videoList = ref([]);

const activeVideoUrl = ref();

const activeVideo = ref(null);

const playVideo = async (video) => {
  try {
    activeVideo.value = video;
    console.log('VideoReplay.vue: courseInfo.value.cid是',courseInfo.value.cid,"video.value.vid是：", video.vid );
    const res = await previewVideoService(courseInfo.value.cid, video.vid); // 向后端获取学生课程
    activeVideoUrl.value = res.data.previewLink; // 将返回的数据赋值给courses

  } catch (error) {
    ElMessage.error('视频播放失败:', error);
    console.error('视频播放失败:', error);
  }
};

// *****************************************上传视频*****************************************
const uploadVisible = ref(false);

const videoFormData = ref({
  vid: '',
  file: null,
  fileName: '',
  timeRange: []
});

const videoRules = {
  vid: [
    { required: true, message: '请输入课程视频序号', trigger: 'blur' },
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
  file: [{ required: true, message: '请上传视频文件', trigger: 'change' }],
  timeRange: [
    { type: 'array', required: true, message: '请选择上课时间范围', trigger: 'blur' },
  ],
};

const validateVideoForm = ref();

// 打开文件选择器
const selectVideoFile = () => {
  document.querySelector('input[type="file"]').click()
};

// 处理文件变化
const handleVideoFileChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    videoFormData.value.file = file;
    videoFormData.value.fileName = file.name;
  }
};

// 提交表单
const submitVideoForm = async () => {
  try {
    await validateVideoForm.value.validate();
    // 上传视频video, start, end, cid, vid
    await uploadVideoService(
        videoFormData.value.file,
        courseInfo.value.cid,
        videoFormData.value.vid,
        videoFormData.value.timeRange[0],
        videoFormData.value.timeRange[1]);
    ElMessage.success('视频上传成功');
    uploadVisible.value = false;

    // 清空表单
    videoFormData.value = { vid: '', file: null, fileName: '', timeRange: [] };
  } catch (error) {
    console.error('上传视频失败', error);
    ElMessage.error('视频上传失败');
  }
};

// 取消上传
const handleUploadCancel = () => {
  uploadVisible.value = false;
  videoFormData.value = { title: '', file: null, fileName: '', timeRange: [] };
};


// *****************************************删除视频*****************************************
const handleDelete = async () => {
  if (!activeVideo.value) {
    ElMessage.warning("请选择一个视频！");
    return;
  }
  try {
    await deleteVideoService(activeVideo.value.id);
    videoList.value = videoList.value.filter((video) => video.id !== activeVideo.value.id);
    activeVideo.value = null;
    ElMessage.success("视频删除成功！");
  } catch (error) {
    console.error(error);
    ElMessage.error("视频删除失败！");
  }
};

// ************************************************获取视频列表********************************************************
const getVideoListData = async () => {
  try {
    console.log('VideoReplay.vue: courseInfo.value.cid是',courseInfo.value.cid);
    const res = await getVideoListService(courseInfo.value.cid); // 向后端获取学生课程
    videoList.value = res.data.videoList; // 将返回的数据赋值给courses

    // console.log('HomeWork.vue22222222222222:   courseInfo.value.cid:', res.data.homeworkInfoList);
    console.log('VideoReplay.vue3333333333333:   videoList:', videoList.value);
  } catch (error) {
    ElMessage.error('Homework.vue:获取视频列表失败:', error);
  }
};

onMounted(()=>{
  getVideoListData()
})
</script>

<style scoped>
/*@import url('https://fonts.loli.net/css2?family=Pacifico&display=swap');*/
.video-replay {
  display: flex;
  height: calc(100% - 32px);

  .video-list {
    width: 27%;
    background-color: #f5f5f5;
    border-radius: 5px;
    overflow-y: auto;
    border-right: 1px solid #dcdfe6;

    .scrollbar {
      height: 100%;
      padding: 10px;
      background-color: rgb(244, 219, 237);
      //background-image: url("@/assets/selectVideo_bg1.jpg");
      //background-repeat: no-repeat; /* 防止背景图重复 */
      //background-size: cover; /* 让背景图适应容器尺寸 */
      //background-position: center; /* 居中对齐背景图 */

      .video-card {
        display: flex;
        align-items: center;
        border-radius: 10px;
        margin: 8px;
        cursor: pointer;
        transition: transform 0.3s, box-shadow 0.3s;
        background-color: rgba(255, 158, 244, 0.2);

        .video-info .video-title {
          font-family: cursive;
          font-size: 18px;
          font-weight: 800;
          color: #a52a2a;
          letter-spacing: 0.5px;
          line-height: 1.6;
          text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
        }
      }

      .video-card:hover {
        transform: scale(1.04);
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
      }
      .video-card.active {
        border: 3px solid #9f5b97;
        background-color: rgb(239, 169, 202);
        box-shadow: 0 4px 8px rgba(64, 158, 255, 0.2);
        transform: scale(1.04);
      }
    }
  }

  .video-player {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #000;
    //border: 2px solid #409eff;
    border-radius: 5px;

    .player {
      //max-width: 100%;
      //max-height: 100%;
      //border: 2px solid #409eff;
      width: 100%; /* 宽度充满容器 */
      height: 100%; /* 高度充满容器 */
      //border: 2px solid #409eff;
      border-radius: 5px;
      object-fit: contain; /* 视频内容适应容器，保持比例 */
    }

    .placeholder {
      color: #fff;
      font-size: 16px;
    }
  }
}

</style>
