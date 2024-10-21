<script setup>
import VerticalBar from "../../components/VerticalBar.vue";
import {useCourseStore} from "@/stores/course.js";
import {computed} from "vue";
const courseStore = useCourseStore()

const courseInfo = computed( () => courseStore.course);
</script>

<template>
  <div class="info-container">
      <div class="header">
        <vertical-bar text="课程简介"></vertical-bar>
      </div>
    <div class="content">
      {{ courseInfo.description }}
    </div>
  </div>
</template>

<style lang="scss" scoped>
.info-container{
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