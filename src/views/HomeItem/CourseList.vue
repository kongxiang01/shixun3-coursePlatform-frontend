<template>
  <el-card class="course-container">
    <div class="header">
      <VerticalBar text="课程列表" />
    </div>
    <!-- 选择学期 -->
    <el-row class="select-semester">
      <el-select v-model="selectedSemester" placeholder="请选择学期" @change="handleSemesterChange" style="width: 300px">
        <el-option
            v-for="(semester, index) in semesters"
            :key="index"
            :label="semester"
            :value="semester">
        </el-option>
      </el-select>
    </el-row>
    <!-- 课程列表 -->
    <el-row class="course-list" :gutter="10">
      <el-col v-for="(course, index) in courses" :key="index" :span="8">
        <el-card shadow="hover" class="course-card" @click="goToCourse(course)">
          <!-- 课程图片 -->
          <img src="https://via.placeholder.com/150x100" alt="课程图片" class="course-image" />
          <!-- 课程信息 -->
          <div class="course-info">
            <h3>{{ course.cname }}</h3>
            <p>课程号：{{ course.cno }}</p>
            <p>课序号：{{ course.cid }}</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </el-card>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import {useRouter} from 'vue-router';
import VerticalBar from "../../components/VerticalBar.vue";
import {getCourseInfo, getCourseList} from "@/api/user.js";
import {useUserStore} from "@/stores/user.js";
import {useCourseStore} from "@/stores/course.js";

const selectedSemester = ref('');
const semesters = [
  '2024-2025第一学期',
  '2023-2024第二学期',
];

const router = useRouter();
const userStore = useUserStore()
const courseStore = useCourseStore()
const courses = ref([]); // 存储获取的课程信息

// 获取学生课程数据
const getCourses = async () => {
  try {
    // console.log('CourseList.vue111:   userStore.user.sno:', userStore.user.sno);
    const res = await getCourseList(userStore.user.sno); // 向后端获取学生课程
    console.log('CourseList.vue2222222:   res.data.courseList:', res.data.courseList);
    courses.value = res.data.courseList; // 将返回的数据赋值给courses
    // console.log('CourseList.vue3333333:   courses.value:' , courses.value)
  } catch (error) {
    console.log('CourseList.vue:获取课程失败:', error);
  }
};


// 点击跳转到课程详情页面******************************************************************************
const goToCourse = async (course) => {
  const res = await getCourseInfo(course.cid, course.cno);
  console.log('CourseList.vue33333333333:   res.data.courseInfo:', res.data.courseInfo)
  courseStore.setCourse(res.data.courseInfo)
  console.log('CourseList.vue444444444444444444444444444:   courseStore.course:', courseStore.course)
  // 获取完整的路由路径
  const routePath = router.resolve({
    name: 'CoursePage',
  });
  // 在新标签页中打开该路径
  window.open(routePath.href, '_blank');
};

const handleSemesterChange = (value) => {
  console.log(`CourseList.vue:选择的学期是: ${value}`);
};

onMounted(()=>{
  getCourses();
  console.log('CourseList.vue: onMounted：获取课程列表', courses.value);
})
</script>

<style lang="scss" scoped>
.course-container {
  background-color: rgba(252, 223, 233, 0.8);

  .header {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
  }

  .select-semester {
    margin-bottom: 10px;
  }

  .course-list{
    height: 620px; /* 限制课程列表的最大高度 */
    overflow-y: auto; /* 启用垂直滚动条 */
    padding-right: 10px; /* 预留空间避免滚动条覆盖内容 */

    .course-card {
      display: flex;
      flex-direction: column;
      background-color: #f5cbdd;
      //justify-content: space-between;
      height: 250px;
      margin-bottom: 10px;

      .course-image {
        width: 100%;
        height: 60%;
        object-fit: cover;
      }

      .course-info {
        height: 40%;
      }

      .course-info h3 {
        margin: 0;
        font-size: 18px;
        font-weight: bold;
      }

      .course-info p {
        margin: 5px 0;
      }
    }
  }
}

</style>
