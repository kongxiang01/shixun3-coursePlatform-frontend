<template>
  <div class="discussion-container">
    <!-- 发起讨论按钮，弹窗形式 -->
    <el-button type="primary" @click="openNewDiscussionDialog">发起讨论</el-button>

    <!-- 讨论列表展示 -->
    <el-table :data="sortedDiscussions" style="width: 100%">
      <el-table-column prop="title" label="讨论标题" />
      <el-table-column prop="initiator" label="发起人" />
      <el-table-column prop="replier" label="回复人" />
      <el-table-column label="操作">
        <template #default="scope">
          <el-button @click="openDiscussionDetails(scope.row)" type="text">查看</el-button>
          <el-button @click="openReplyDialog(scope.row)" type="text">回复</el-button>
          <el-button @click="deleteDiscussion(scope.row)" type="danger">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新建讨论的弹窗 -->
    <el-dialog title="发起讨论" v-model ="newDiscussionDialogVisible">
      <el-form :model="newDiscussion">
        <el-form-item label="讨论标题">
          <el-input v-model="newDiscussion.title"/>
        </el-form-item>
        <el-form-item label="讨论内容">
          <el-input type="textarea" v-model="newDiscussion.content"/>
        </el-form-item>
        <el-form-item label="讨论类型">
          <el-select v-model="newDiscussion.type" placeholder="请选择讨论类型">
            <el-option label="普通" value="general"></el-option>
            <el-option label="课程讨论" value="course"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="关联课程节次">
          <el-input v-model="newDiscussion.section"/>
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="newDiscussion.isAnonymous">匿名发布</el-checkbox>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="newDiscussionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitDiscussion">确认发布</el-button>
      </span>
    </el-dialog>

    <!-- 讨论详情的弹窗 -->
    <el-dialog title="讨论详情" v-model ="discussionDetailsDialogVisible">
      <p>讨论内容: {{ currentDiscussion.content }}</p>
      <p>发起人: {{ currentDiscussion.initiator }}</p>
      <p>发起时间: {{ currentDiscussion.time }}</p>
      <p>回复:</p>
      <el-table :data="currentDiscussion.replies">
        <el-table-column prop="replier" label="回复人"/>
        <el-table-column prop="content" label="回复内容"/>
        <el-table-column prop="time" label="回复时间"/>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="discussionDetailsDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>

    <!-- 回复的弹窗 -->
    <el-dialog title="回复讨论" v-model ="replyDialogVisible" @close="resetReplyForm">
      <el-input type="textarea" v-model="newReply.content" placeholder="输入回复内容"/>
      <span slot="footer" class="dialog-footer">
        <el-button @click="replyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReply">提交回复</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'

// 模拟讨论数据
const discussions = ref([
  { id: 1, title: '讨论1', content: '内容1', initiator: '用户A', time: '2024-10-01', replies: [] },
  { id: 2, title: '讨论2', content: '内容2', initiator: '用户B', time: '2024-10-02', replies: [] },
])

// 控制弹窗显示
const newDiscussionDialogVisible = ref(false)
const discussionDetailsDialogVisible = ref(false)
const replyDialogVisible = ref(false)

// 当前操作的讨论和回复
const currentDiscussion = ref({})
const newDiscussion = ref({ title: '', content: '', type: '', section: '', isAnonymous: false })
const newReply = ref({ content: '' })

// 打开新建讨论弹窗
const openNewDiscussionDialog = () => {
  newDiscussionDialogVisible.value = true
  console.log(newDiscussionDialogVisible.value+'发起讨论按钮被点击'); // 检查函数是否被调用
}

// 提交新讨论
const submitDiscussion = () => {
  if (!newDiscussion.value.title || !newDiscussion.value.content) {
    ElMessage.error('请填写完整的讨论信息')
    return
  }
  discussions.value.push({
    id: discussions.value.length + 1,
    ...newDiscussion.value,
    initiator: newDiscussion.value.isAnonymous ? '匿名用户' : '用户X',
    time: new Date().toISOString().slice(0, 10),
    replies: [],
  })
  newDiscussionDialogVisible.value = false
  // 重置表单
  newDiscussion.value = { title: '', content: '', type: '', section: '', isAnonymous: false };
  ElMessage.success('讨论发布成功')
}

// 打开讨论详情
const openDiscussionDetails = (discussion) => {
  currentDiscussion.value = discussion
  discussionDetailsDialogVisible.value = true
}

// 打开回复弹窗
const openReplyDialog = (discussion) => {
  currentDiscussion.value = discussion
  replyDialogVisible.value = true
}
// 重置回复表单
const resetReplyForm =() => {
  newReply.value={content:''}
}
// 提交回复
const submitReply = () => {
  if (!newReply.value.content) {
    ElMessage.error('请填写回复内容')
    return
  }
  currentDiscussion.value.replies.push({
    replier: '用户X',
    content: newReply.value.content,
    time: new Date().toISOString().slice(0, 10),
  })
  replyDialogVisible.value = false
  ElMessage.success('回复提交成功')
}

// 删除讨论
const deleteDiscussion = (discussion) => {
  discussions.value = discussions.value.filter((d) => d.id !== discussion.id)
  ElMessage.success('讨论已删除')
}

// 按时间排序讨论
const sortedDiscussions = computed(() =>
    discussions.value.slice().sort((a, b) => new Date(b.time) - new Date(a.time))
)
</script>

<style scoped>
.parent-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}


</style>
