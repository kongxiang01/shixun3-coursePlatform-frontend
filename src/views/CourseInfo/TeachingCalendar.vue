<script setup>

import VerticalBar from "../../components/VerticalBar.vue";
</script>

<template>
  <div class="tc-container">
    <div class="header">
      <vertical-bar text="教学日历"></vertical-bar>
    </div>
    <div class="pdfContainer">
      <div class="content">
<!--        <iframe src="http://127.0.0.1:5000/get-pdf"-->
<!--                width="100%"-->
<!--                height="100%"-->
<!--                allow="fullscreen"></iframe>-->
<!--        <iframe src="https://view.officeapps.live.com/op/embed.aspx?src=../../assets/testword.docx" width="100%" height="600px"></iframe>-->

      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.tc-container{
  height: 100%;
  //border: 1px solid black;
  .header {
    padding-top: 10px;
    margin-left: 10px;
    //border: 1px solid black;
  }
  .pdfContainer{
    height: calc(100vh - 195px);// 刚好填满tc-container-header的高度
    padding: 20px;
    //border: 1px solid black;
    .content{
      //padding: 10px;
      //border-radius: 5px;
      //background-color: #9f7390;
      //color: black;
      //font-size: 14px;
      height: 100%;
      //border: 5px solid black;
    }
  }
}

</style>