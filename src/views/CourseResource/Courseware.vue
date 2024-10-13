<template>
  <div class="container" style="">
    <el-row class="resource-container">
      <el-col class="directory-container" :span="5">
        <el-tree
            class="directory"
            :data="treeData"
            :props="defaultProps"
            @node-click="handleNodeClick"
            node-key="label"
            :expand-on-click-node="false"
            highlight-current
            default-expand-all
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
        <el-table :data="tableData" class="resource-table">
          <el-table-column label="文件名称">
            <template #default="scope">
              <el-link @click="handleNameClick(scope.row)">
                <el-icon v-if="scope.row.type === 'directory'">
                  <folder />
                </el-icon>
                <el-icon v-else>
                  <document />
                </el-icon>
                {{ scope.row.label }}
              </el-link>
            </template>
          </el-table-column>
          <el-table-column prop="type" label="属性" />
          <!-- 操作列 -->
          <el-table-column label="操作" width="120">
            <template #default="scope">
              <el-link v-if="scope.row.type === 'file'" @click="downloadFile(scope.row)">
                下载
              </el-link>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import {Search, Folder, Document } from "@element-plus/icons-vue";
import axios from "axios";
import {ElMessage} from "element-plus";

const input = ref('');

const treeData = ref([]);
const tableData = ref([]);
const currentPath = ref('/your/base/path'); // 初始化路径
// const breadcrumb = reactive([{ label: '根目录', path: '/your/base/path' }]);
const previewVisible = ref(false);
const previewUrl = ref('');

// 定义 el-tree 的 prop 配置
const defaultProps = {
  children: 'children',
  label: 'label',
};

treeData.value = [
  {
    "label": "folder1",
    "type": "directory",
    "children": [
      {
        "label": "file1.txt",
        "type": "file"
      },
      {
        "label": "subfolder1",
        "type": "directory",
        "children": [
          {
            "label": "file2.txt",
            "type": "file"
          }
        ]
      }
    ]
  },
  {
    "label": "file3.txt",
    "type": "file"
  }
]

// 获取目录内容
const fetchDirectoryContents = async () => {
  try {
    const response = await axios.get('/folder-files', {
      params: { folderPath: currentPath.value },
    });
    tableData.value = response.data;
  } catch (error) {
    ElMessage.error('获取目录内容失败');
  }
};

// 点击文件名
const handleNameClick = (item) => {
  if (item.type === 'directory') {
    // 打开目录
    currentPath.value = `${currentPath.value}/${item.label}`;
    // breadcrumb.push({ label: item.label, path: currentPath.value });
    fetchDirectoryContents();
  } else {
    // 预览文件
    previewUrl.value = `/file-preview?filePath=${encodeURIComponent(currentPath.value + '/' + item.label)}`;
    previewVisible.value = true;
  }
};

// 下载文件
const downloadFile = (item) => {
  window.open(`/file-download?filePath=${encodeURIComponent(currentPath.value + '/' + item.label)}`);
};

// 从后端获取目录结构
const fetchFolderStructure = async () => {
  try {
    const response = await axios.get('/folder-structure', {
      params: { folderPath: '/your/folder/path' }
    });
    treeData.value = response.data;
  } catch (error) {
    console.error('Failed to fetch folder structure', error);
  }
};

// 处理节点点击事件，获取对应目录下的文件
const handleNodeClick = async (node) => {
  if (node.type === 'directory') {
    try {
      const response = await axios.get('/folder-files', {
        params: { folderPath: `/your/folder/path${node.path}` } // node.path 是目录路径
      });
      tableData.value = response.data;
    } catch (error) {
      console.error('Failed to fetch files', error);
    }
  }
};

// 在组件挂载时获取数据
onMounted(() => {
  fetchFolderStructure();
});


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
