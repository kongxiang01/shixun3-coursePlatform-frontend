<script setup>
import { ref } from 'vue';
import VerticalBar from "../../components/VerticalBar.vue";

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

// 控制弹窗的显示与隐藏
const dialogVisible = ref(false);
const selectedNoticeContent = ref('');

// 点击预览按钮，显示对应的通知内容
const handlePreview = (row) => {
  selectedNoticeContent.value = row.content;
  dialogVisible.value = true;
  console.log('点击按钮');
}
</script>

<template>
  <div class="notice-container">
    <div class="header">
      <vertical-bar text="通知"></vertical-bar>
    </div>
    <div class="content">

      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="theme" label="主题" width="400" />
        <el-table-column prop="date" label="日期" width="200" />
        <el-table-column prop="checkTimes" label="查看次数" width="200" />
        <el-table-column prop="poster" label="发布人" />
        <el-table-column label="操作" align="center" >
          <template #default="scope">
            <el-button type="primary" @click="handlePreview(scope.row)">预览</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 弹窗显示通知详细内容 -->
    <el-dialog v-model="dialogVisible" title="通知详情" style="max-height: 800px">
      <div style="height: 500px">
        <span>{{ selectedNoticeContent }}</span>
      </div>
      <template #footer>
        <el-button type="info" @click="dialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.notice-container{
  height: 100%;
  .header {
    padding-top: 10px;
    margin-left: 10px;
    //border: 1px solid black;
  }
  .content{
    padding: 20px;
    margin: 20px;
    border-radius: 5px;
    background-color: #9f7390;
    color: black;
    font-size: 14px;
    min-height: 20px;
  }
}

</style>