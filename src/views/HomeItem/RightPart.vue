<template>
  <el-card class="announce-card" shadow="hover">
    <div class="header">
      <VerticalBar text="通知公告" />
    </div>
    <div class="announce-content">
      通知内容
    </div>
  </el-card>
  <el-card class="time-card" shadow="hover">
    <div class="card-header">
      <VerticalBar text="时间列表" />
    </div>
    <div class="calendar-container">
      <!-- 年份和月份选择 -->
      <div class="calendar-header">
        <div class="select-header">
          <select v-model="selectedYear" @change="handleMonthYearChange">
            <option v-for="year in years" :key="year" :value="year">{{ year }}</option>
          </select>
          <select v-model="selectedMonth" @change="handleMonthYearChange">
            <option v-for="(month, index) in months" :key="index" :value="index">{{ month }}</option>
          </select>
        </div>
        <div class="teaching-week-info">
          当前是第
          <span style="margin: 0 5px; color: #6fc8ee; font-size: 20px">{{ teachingWeek }}</span>
          教学周
        </div>
      </div>

      <!-- 星期表头 -->
      <div class="calendar-body">
        <div class="calendar-week-header">
          <span v-for="day in weekDays" :key="day" class="week-day">{{ day }}</span>
        </div>
        <!-- 日期部分 -->
        <div class="calendar-dates">
          <span v-for="blank in firstDayOfWeek-1" :key="blank" class="calendar-blank"></span>
          <span
              v-for="date in dates"
              :key="date"
              :class="['calendar-date', { 'highlight': isHighlighted(date), 'today': isToday(date) }]"
              @click="handleDateClick(date)"
          >
          {{ date }}
        </span>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import VerticalBar from "../../components/VerticalBar.vue";
import {computed, ref, watchEffect} from 'vue';
import {useCourseStore} from "@/stores/course.js";

// 当前年份和月份
const selectedYear = ref(new Date().getFullYear());
const selectedMonth = ref(new Date().getMonth());
const selectedDate = ref(null); // 默认不选中任何日期

// 年份范围
const years = Array.from({ length: 30 }, (_, i) => i + 2000);
const months = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'];
const weekDays = ['一', '二', '三', '四', '五', '六', '日'];

const dates = ref([]);
const firstDayOfWeek = ref(0);
// 当前日期
const today = new Date();
const semesterStart = new Date(2024, 8, 2); // 9月2号，月份是从0开始计数
const nationalDayWeek = 5; // 国庆节是第五周

// 生成日历
const generateCalendar = () => {
  const daysInMonth = new Date(selectedYear.value, selectedMonth.value + 1, 0).getDate(); // 当月天数
  let firstDay = new Date(selectedYear.value, selectedMonth.value, 1).getDay(); // 当月第一天的星期
  // 将星期日（0）转换为 7
  firstDayOfWeek.value = firstDay === 0 ? 7 : firstDay;
  dates.value = Array.from({ length: daysInMonth }, (_, i) => i + 1); // 生成日期数组
};

const courseStore = useCourseStore()

// 判断当前是第几教学周，排除国庆周
const teachingWeek = computed(() => {
  const now = new Date(selectedYear.value, selectedMonth.value, selectedDate.value || today.getDate());
  const timeDifference = now - semesterStart;
  const weeksPassed = Math.floor(timeDifference / (1000 * 60 * 60 * 24 * 7));

  return weeksPassed >= nationalDayWeek ? weeksPassed : weeksPassed + 1; // 跳过国庆那一周
});

watchEffect(() => {
  courseStore.setTeachingWeek(teachingWeek.value);
});

// 判断是否是当前日期
const isToday = (date) => {
  return date === today.getDate() &&
      selectedMonth.value === today.getMonth() &&
      selectedYear.value === today.getFullYear();
};

// 判断是否高亮（选中的日期）
const isHighlighted = (date) => {
  return date === selectedDate.value;
};

// 处理日期点击事件
const handleDateClick = (date) => {
  // 切换选中日期的高亮
  selectedDate.value = date;
};

// 当切换年份或月份时，重新生成日历并取消选中状态
const handleMonthYearChange = () => {
  selectedDate.value = null; // 切换月份或年份时取消高亮
  generateCalendar();
};

// 页面加载时生成当前月份日历
generateCalendar();

</script>

<style lang="scss" scoped>
.announce-card{
  background-color: rgba(252, 223, 233, 0.7);
  height: 300px;
}
.time-card{
  margin-top: 20px;
  background-color: rgba(252, 223, 233, 0.7);
  //border: 1px solid black;

  .calendar-container {
    margin-top: 10px;
    padding-bottom: 2px;
    border-radius: 10px;
    background-color: rgba(249, 249, 249, 0.5);
    width: 100%;
    min-height: 340px;
    //border: 1px solid black;

    .calendar-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 10px;
      //border: 1px solid black;
      select {
        padding: 5px;
        border-radius: 5px;
        border: 1px solid #ccc;
        font-size: 12px;
        //border: 1px solid black;
      }
    }

    .calendar-body {
      display: grid;
      justify-content: space-evenly;
      grid-template-rows: auto 1fr;

      .calendar-week-header {
        width: 100%;
        display: grid;
        grid-column-gap: 3px;
        grid-template-columns: auto auto auto auto auto auto auto;
        text-align: center;
        font-weight: bold;
      }
      .week-day {
        padding: 4px;
        background-color: rgba(85, 121, 188, 0.8);
        color: white;
        border-radius: 5px;
        //border: 1px solid black;
      }

      .calendar-dates {
        margin-top: 4px;
        display: grid;
        grid-template-columns: repeat(7, 1fr);
        grid-gap: 5px;

        .calendar-date {
          padding: 9px;
          //background-color: rgba(238, 238, 238, 0);
          text-align: center;
          border-radius: 5px;
          transition: background-color 0.5s ease;
          cursor: pointer;
          //border: 1px solid #888282;
        }
        .calendar-date:hover{
          background-color: #6fc8ee;
          //color: #00adea;
        }

        .calendar-date.highlight {
          background-color: #6fc8ee; /* 高亮选中日期 */
        }

        .calendar-date.today {
          color: red; /* 当前日期字体变红 */
        }
      }

      .calendar-blank {
        //border-radius: 5px;
        //border: 1px solid #888282;
      }
    }
  }
}

</style>
