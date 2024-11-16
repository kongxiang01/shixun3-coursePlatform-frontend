<template>
  <el-button type="primary" @click="handleUpload">上传视频</el-button>
  <el-dialog
      title="上传视频"
      v-model="uploadVisible"
      width="600px"
      :close-on-click-modal="false"
  >
    <el-form ref="validateVideoForm" :model="videoFormData" :rules="videoRules" class="form">
      <!-- 视频标题 -->
      <el-form-item label="视频标题" prop="title">
        <el-input
            v-model="videoFormData.title"
            placeholder="请输入视频标题"
            style="width: 100%;"
        ></el-input>
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
  <el-button type="danger" :disabled="!activeVideo" @click="handleDelete">删除视频</el-button>
  <div class="video-replay">
    <!-- 左侧视频列表 -->
    <div class="video-list">
      <el-scrollbar class="scrollbar">
        <el-card
            v-for="(video, index) in videoList"
            :key="video.id"
            class="video-card"
            @click="playVideo(video)"
            :class="{ active: activeVideo?.id === video.id }"
        >
          <div class="video-thumbnail">
            <img :src="video.thumbnail" alt="视频缩略图" />
          </div>
          <div class="video-info">
            <p class="video-title">{{ video.title }}</p>
          </div>
        </el-card>
      </el-scrollbar>
    </div>

    <!-- 右侧视频播放区域 -->
    <div class="video-player">
      <video
          v-if="activeVideo"
          controls
          autoplay
          :src="activeVideo.url"
          class="player"
      ></video>
      <p v-else class="placeholder">请选择一个视频进行播放</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useUserStore } from "@/stores/user.js";
import { uploadVideoService, deleteVideoService } from "@/api/video.js";
import {ElMessage} from "element-plus";

const userStore = useUserStore();
const userInfo = userStore.user;

const videoList = ref([
  {
    id: 1,
    title: '教学视频 1',
    url: 'https://www.example.com/video1.mp4',
    thumbnail: 'https://www.example.com/thumbnail1.jpg',
  },
  {
    id: 2,
    title: '教学视频 2',
    url: 'https://www.example.com/video2.mp4',
    thumbnail: 'https://www.example.com/thumbnail2.jpg',
  },
  {
    id: 3,
    title: '教学视频 3',
    url: 'https://www.example.com/video3.mp4',
    thumbnail: 'https://www.example.com/thumbnail3.jpg',
  },
]);

const activeVideo = ref(null);

const playVideo = (video) => {
  activeVideo.value = video;
};

// *****************************************上传视频*****************************************
const uploadVisible = ref(false);

const videoFormData = ref({
  title: '',
  file: null,
  fileName: '',
});

const videoRules = {
  title: [{ required: true, message: '请填写视频标题', trigger: 'blur' }],
  file: [{ required: true, message: '请上传视频文件', trigger: 'change' }],
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
    //
    // if (!videoFormData.value.file) {
    //   ElMessage.info('请先选择文件');
    //   return;
    // }

    // 上传视频
    await uploadVideoService(videoFormData.value.file, videoFormData.value.title);
    ElMessage.success('视频上传成功');
    uploadVisible.value = false;

    // 清空表单
    videoFormData.value = { title: '', file: null, fileName: '' };
  } catch (error) {
    console.error('上传视频失败', error);
    ElMessage.error('视频上传失败');
  }
};

// 取消上传
const handleUploadCancel = () => {
  uploadVisible.value = false;
  videoFormData.value = { title: '', file: null, fileName: '' };
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
</script>

<style scoped>
.video-replay {
  display: flex;
  height: calc(100% - 32px);

  .video-list {
    width: 27%;
    background-color: #f5f5f5;
    overflow-y: auto;
    border-right: 1px solid #dcdfe6;

    .scrollbar {
      height: 100%;
      padding: 10px;

      .video-card {
        display: flex;
        align-items: center;
        margin: 8px;
        cursor: pointer;
        transition: transform 0.3s, box-shadow 0.3s;

        .video-thumbnail img {
          width: 60px;
          height: 60px;
          border-radius: 4px;
          margin-right: 10px;
        }

        .video-info .video-title {
          font-size: 14px;
          font-weight: 500;
          color: #333;
        }
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

    .player {
      max-width: 100%;
      max-height: 50%;
    }

    .placeholder {
      color: #fff;
      font-size: 16px;
    }
  }
}

.video-card.active {
  //border: 2px solid #409eff;
  background-color: #ff9ef4;
  box-shadow: 0 4px 8px rgba(64, 158, 255, 0.2);
  transform: scale(1.05);
}

.video-card:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}
</style>
