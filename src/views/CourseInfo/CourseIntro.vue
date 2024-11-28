<script setup>
import VerticalBar from "../../components/VerticalBar.vue";
import {useCourseStore} from "@/stores/course.js";
import {computed, ref} from "vue";
import {useUserStore} from "@/stores/user.js";
import {updateCourseIntroService} from "@/api/course.js";
import {ElMessage} from "element-plus";

const courseStore = useCourseStore()
const courseInfo = computed( () => courseStore.course);
const userStore = useUserStore(); // 获取用户角色
const userInfo = computed(() => userStore.user)

// 控制对话框的显示与输入
const dialogVisible = ref(false);
const newDescription = ref(courseInfo.value.description); // 用于绑定编辑输入框内容

// 更新课程简介
const handleUpdateIntro = async () => {
  try {
    const cid = courseInfo.value.cid; // 获取课程ID
    await updateCourseIntroService(cid, newDescription.value); // 调用封装的函数
    courseStore.course.description = newDescription.value; // 更新本地状态
    dialogVisible.value = false; // 关闭对话框
    ElMessage.success("课程简介更新成功！");
  } catch (error) {
    console.error("更新课程简介失败:", error);
    ElMessage.error("更新课程简介失败，请稍后再试！");
  }
};
</script>

<template>
  <div class="info-container">
      <div class="header">
        <vertical-bar text="课程简介"></vertical-bar>
        <template v-if="userInfo.type === '1'" >
          <div>
            <el-button @click="dialogVisible = true" style="margin-right: 20px">编辑简介</el-button>
          </div>
        </template>
      </div>
    <div class="content">
      {{ courseInfo.description }}
    </div>
    <el-dialog
        v-model="dialogVisible"
        width="500px"
        :before-close="() => (dialogVisible = false)"
    >
      <el-form label-position="top">
        <el-form-item label="课程简介">
          <el-input
              type="textarea"
              v-model="newDescription"
              rows="10"
              placeholder="请输入新的课程简介"
          ></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer" style="margin-top: 20px; display: flex; justify-content: space-between">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUpdateIntro">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.info-container{
  height: 100%;
  .header {
    padding-top: 10px;
    margin-left: 10px;
    display: flex;
    justify-content: space-between;
    //border: 1px solid black;
  }
  .content{
    padding: 20px;
    margin: 20px;
    border-radius: 5px;
    background-color: rgba(159, 115, 144, 0.2);
    min-height: 500px;
    color: black;
    font-size: 18px;

    //font-family: "Pacifico", "Comic Sans MS", cursive;
    font-weight: 400;
    letter-spacing: 0.5px;
    //line-height: 1.6;
    text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
  }
}

</style>