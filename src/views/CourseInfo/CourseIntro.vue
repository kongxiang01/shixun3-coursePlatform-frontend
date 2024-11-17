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