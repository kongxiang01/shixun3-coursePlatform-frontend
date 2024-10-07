<template>
  <div class="container" style="">
    <el-row class="resource-container">
      <el-col class="directory-container" :span="5">
        <el-tree
            class="directory"
            :data="directoryData"
            :props="treeProps"
            @node-click="handleNodeClick"
        ></el-tree>
      </el-col>
      <el-col class="resource-table-container" :span="19">
        <el-row>
          <el-input
              v-model="input"
              placeholder="输入资源名称查找"
              class="search-input"
          >
            <template #append>
              <el-button :icon="Search" @click="handleSearchResource" class="search-button"></el-button>
            </template>
          </el-input>
        </el-row>
        <el-table :data="files" class="resource-table">
          <el-table-column prop="name" label="文件名称" />
          <el-table-column prop="type" label="属性" />
          <el-table-column v-if="showOperations" prop="operations" label="操作">
            <template v-slot="scope">
              <el-button @click="downloadFile(scope.row)">下载</el-button>
              <el-button @click="deleteFile(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import {Search} from "@element-plus/icons-vue";

const input = ref('');

const directoryData = ref([
  { id: 1, label: '电子课件', children: [{ id: 4, label: '文档' }, { id: 5, label: '视频' }] },
  { id: 2, label: '作业', children: [] },
  { id: 3, label: '参考资料', children: [] },
]);

const files = ref([]);
const showOperations = ref(false);
const treeProps = {
  children: 'children',
  label: 'label',
};

const handleSearchResource = (input) => {
  console.log('handleSearchResource:' + input);
}

function handleNodeClick(data) {
  if (data.id === 1) {
    files.value = [
      { name: '课程大纲.pdf', type: 'PDF', id: 1 },
      { name: '视频讲解.mp4', type: '视频', id: 2 },
    ];
    showOperations.value = true;
  } else {
    files.value = [];
    showOperations.value = false;
  }
}

function downloadFile(file) {
  console.log(`Downloading ${file.name}`);
}

function deleteFile(file) {
  console.log(`Deleting ${file.name}`);
}
</script>

<style lang="scss" scoped>
.container{
  max-height: 100%;
  max-width: 100%;
  //border: 1px solid black;
  padding: 20px;

  .resource-container {
    //display: flex;
    .directory-container {
      //width: 30%;
      //border-right: 1px solid #ccc;
      .directory{
        min-height: 500px;
        background-color: #f4dbed;
        border-radius: 10px;
      }
    }
  }

  .resource-table-container {
    //width: 70%;
    padding-left: 20px;

    .search-input{
      width: 300px;
      margin: 10px 0;
      .search-button{
        background-color: rgb(255, 158, 244);
        //width: 200px;
        //margin: 0 -20px;// 自带的样式
      }
    }

    .resource-table{
      background-color: #f4dbed;
      border-radius: 10px;
    }
  }
}

</style>
