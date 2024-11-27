<template>
  <div class="discussion-container">
    <!-- 发起讨论按钮 -->
    <el-button type="primary" @click="openNewDiscussionDialog">发起讨论</el-button>

    <!-- 讨论列表 -->
    <el-table :data="discussions" style="width: 100%">
      <el-table-column prop="title" label="讨论标题" />
      <el-table-column prop="creatorSno" label="发起人学号" />
      <!--      <el-table-column prop="time" label="发起时间" />-->
      <el-table-column label="操作">
        <template #default="scope">
          <el-button @click="openDiscussionDetails(scope.row)" type="text">查看</el-button>
          <!--          <el-button @click="openDiscussionDetails(scope.row.discussionUuid)" type="text">查看</el-button>-->
          <el-button @click="deleteDiscussion(scope.row.discussionUuid)" type="danger">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 讨论详情弹窗 -->
    <el-dialog title="讨论详情" v-model="discussionDetailsDialogVisible">
      <p>讨论标题: {{ currentDiscussion.title }}</p>
      <p>讨论内容: {{ currentDiscussion.content }}</p>
      <p>发起人: {{ currentDiscussion.creatorSno }}</p>
      <!--      <p>发起时间: {{ currentDiscussion.time }}</p>-->
      <p>回复列表:</p>
      <el-table :data="comments">
        <el-table-column prop="creatorSno" label="回复人学号" />
        <el-table-column prop="content" label="回复内容" />
        <!--        <el-table-column prop="time" label="回复时间" />-->
      </el-table>
      <el-input
          v-model="newReply.content"
          type="textarea"
          placeholder="输入回复内容"
      />
      <span slot="footer" class="dialog-footer">
        <el-button @click="discussionDetailsDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="submitReply">提交回复</el-button>
      </span>
    </el-dialog>

    <!-- 发起讨论弹窗 -->
    <el-dialog title="发起讨论" v-model="newDiscussionDialogVisible">
      <el-form :model="newDiscussion">
        <el-form-item label="讨论标题">
          <el-input v-model="newDiscussion.title" />
        </el-form-item>
        <el-form-item label="讨论内容">
          <el-input type="textarea" v-model="newDiscussion.content" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="newDiscussionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitDiscussion">确认发布</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, onMounted, computed} from 'vue';
import { ElMessage } from 'element-plus';
import { getDiscussions, createDiscussion, deleteDiscussionApi, createComment ,getComments} from '@/api/Discuss.js';
import { useUserStore } from '@/stores/user.js';
import { useCourseStore } from '@/stores/course.js';
const courseStore = useCourseStore();
const courseInfo = computed(() => courseStore.course);
const userStore = useUserStore();
const userInfo = computed(() => userStore.user);
// 讨论列表
const discussions = ref([]);
//评论列表
const comments = ref([]);
// 当前讨论详情
const currentDiscussion = ref({});
// 控制弹窗显示
const discussionDetailsDialogVisible = ref(false);
const newDiscussionDialogVisible = ref(false);
// 新讨论数据
const newDiscussion = ref({
  title: '',
  content: '',
  creatorSno: '', // 示例学号，实际需动态赋值
});
// 新回复数据
const newReply = ref({
  content: '',
  creatorSno: '20240101', // 示例学号，实际需动态赋值
  discussionUuid: '',
});
// 加载当前讨论的评论
const loadComments = async (discussionUuid) => {
  try {
    console.log('1111111response.data',discussionUuid)
    const response = await getComments(discussionUuid);

    console.log('1111111response.data',response.data)
    if (response.status === 200) {
      comments.value=response.data.comments
    } else {
      ElMessage.error('获取评论失败');
    }
  } catch (error) {
    console.error(error);
    ElMessage.error('加载评论时发生错误');
  }
};
// 加载讨论数据
const loadDiscussions = async () => {
  console.log('userInfo.value',userInfo.value.sno)
  try {
    const response = await getDiscussions();
    console.log('99999response.status',response.value)
    if (response.status === 200) {
      discussions.value = response.data.discussions;
      console.log(' discussions.value',discussions.value)
      console.log(' discussions.value')
    } else {
      ElMessage.error('加载讨论列表失败');
    }
  } catch (error) {
    console.error(error);
    ElMessage.error('加载讨论列表时发生错误');
  }
};

// 查看讨论详情
const openDiscussionDetails = async (discussion) => {
  console.log('0000currentDiscussion.value',discussion.discussionUuid)
  currentDiscussion.value = discussion;

  await loadComments(discussion.discussionUuid);
  newReply.value.discussionUuid = discussion.discussionUuid;
  console.log('discussion.uuid',discussion)
  discussionDetailsDialogVisible.value = true;
};
// const openDiscussionDetails = async (discussion) => {
//   //console.log('currentDiscussion.value',discussion.discussionUuid)
//   currentDiscussion.value = discussion;
//
//   await loadComments(discussion);
//   newReply.value.discussionUuid = discussion;
//   console.log('discussion.uuid',discussion)
//   discussionDetailsDialogVisible.value = true;
// };

// 提交回复
const submitReply = async () => {
  if (!newReply.value.content) {
    ElMessage.error('请输入回复内容');
    return;
  }
  try {
    console.log('111userInfo.value.Sno',userInfo.value.sno)
    newReply.value.creatorSno=userInfo.value.sno
    console.log('newReply.value',newReply.value)
    const response = await createComment(newReply.value);
    console.log('response.status',response.status)
    if (response.status === 200) {
      ElMessage.success('回复成功');
      newReply.value.content = '';
      // 重新加载评论
      await loadComments(newReply.value.discussionUuid);
    } else {
      ElMessage.error('回复失败');
    }
  } catch (error) {
    console.error(error);
    ElMessage.error('提交回复时发生错误');
  }
};

// 打开新讨论弹窗
const openNewDiscussionDialog = () => {
  newDiscussionDialogVisible.value = true;
};

// **************************************************提交新讨论*********************************
const submitDiscussion = async () => {
  if (!newDiscussion.value.title || !newDiscussion.value.content) {
    ElMessage.error('请填写完整的讨论信息');
    return;
  }
  newDiscussion.value.creatorSno = userInfo.value.sno
  console.log('newDiscussion.value',userInfo.value.sno,newDiscussion.value)
  try {
    const response = await createDiscussion(newDiscussion.value);
    if (response.status === 200) {
      ElMessage.success('讨论发布成功');
      await loadDiscussions(); // 重新加载列表
      newDiscussionDialogVisible.value = false;
      newDiscussion.value = { title: '', content: '', creatorSno: '20240101' };
    } else {
      ElMessage.error('讨论发布失败');
    }
  } catch (error) {
    console.error(error);
    ElMessage.error('发布讨论时发生错误');
  }
};

// ***********************************************删除讨论*********************************
const deleteDiscussion = async (discussionUuid) => {
  try {
    console.log('discussionUuid:', discussionUuid,'userInfo.value.sno: ', userInfo.value.sno)
    const response = await deleteDiscussionApi({
      discussionUuid: discussionUuid,
      Sno:userInfo.value.sno, // 示例学号，实际需动态替换
    });
    ElMessage.success('讨论已删除');
  } catch (error) {
    console.error(error);
    ElMessage.error('删除讨论时发生错误');
  }
};

// 初始化加载
onMounted(() => {
  loadDiscussions();
});
</script>

<style scoped>
.discussion-container {
  padding: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style>
