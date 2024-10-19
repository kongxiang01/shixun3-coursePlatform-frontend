<template>
  <div class="container" style="">
    <el-row class="resource-container">
      <el-col class="directory-container" :span="5">
<!--        :expand-on-click-node="false"-->
<!--        highlight-current-->
<!--        default-expand-all-->
        <el-tree
            class="directory"
            :data="treeData"
            :props="defaultProps"
            @node-click="handleNodeClick"
            node-key="label"
        ></el-tree>
      </el-col>
      <el-col class="resource-table-container" :span="19">
        <el-row class="some-buttons">
          <!-- 判断用户是否是教师，只有教师显示这些按钮 -->
<!--          <template v-if="userStore.data.identity === 'teacher'">-->
<!--            <el-button @click="uploadFile">上传文件</el-button>-->
<!--            <el-button @click="createFolder">新建目录</el-button>-->
<!--            <el-button @click="moveItem">移动</el-button>-->
<!--            <el-button @click="deleteItem">删除</el-button>-->
<!--            <el-button @click="publishItem">发布</el-button>-->
<!--            <el-button @click="unpublishItem">取消发布</el-button>-->
<!--          </template>-->
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
import {ElMessage} from "element-plus";
import { useUserStore } from "@/stores/user.js";

const input = ref('');
const treeData = ref([]);
const tableData = ref([]);
const currentPath = ref('E:/javaItem/assets'); // 初始化路径
// const breadcrumb = reactive([{ label: '根目录', path: '/your/base/path' }]);
const previewVisible = ref(false);
const previewUrl = ref('');

// 获取用户身份
const userStore = useUserStore(); // 获取用户角色


// 定义 el-tree 的 prop 配置
const defaultProps = {
  label: 'label',
  path: 'path',
  children: 'children',
};

// ****************************************************************************************************************
import {getDirectory } from "@/api/user.js";
// 获取目录内容
const fetchDirectoryContents = async () => {
  try {
    // const response = await axios.get('http://192.168.10.124:8080/api/assets/catalogue', {
    //   params: { path: currentPath.value },
    // });
    const response = await getDirectory(currentPath.value)
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
    console.log('currentPath.value11111111111: ' + currentPath.value)
    // breadcrumb.push({ label: item.label, path: currentPath.value });
    fetchDirectoryContents();
  } else {
    // 预览文件
    previewUrl.value = `/file-preview?filePath=${encodeURIComponent(currentPath.value + '/' + item.label)}`;
    previewVisible.value = true;
  }
};

// 下载文件***********************有问题：文件名带中文时下载不了***************
const downloadFile = (item) => {
  const filePath = encodeURIComponent(currentPath.value + '/' + item.label);
  const downloadUrl = `http://192.168.10.124:8080/api/assets/download?filePath=${filePath}`;

  // 使用 window.open 打开下载链接
  window.open(downloadUrl);
};

// 递归函数：过滤出所有 type 为 'directory' 的项
const filterDirectories = (data) => {
  return data
      .filter(item => item.type === 'directory')  // 只保留 type 为 'directory' 的项
      .map(item => ({
        ...item,
        children: item.children ? filterDirectories(item.children) : [] // 递归处理子目录
      }));
};

// 从后端获取目录结构
const fetchFolderStructure = async () => {
  try {
    // const response = await axios.get('http://192.168.10.124:8080/api/assets/catalogue', {
    //   params: { path: 'E:/javaItem/assets' }
    // });
    const response = await getDirectory('E:/javaItem/assets');
    treeData.value = filterDirectories(response.data);
  } catch (error) {
    console.error('Failed to fetch folder structure', error);
  }
};

// 处理节点点击事件，获取对应目录下的文件
const handleNodeClick = async (node) => {
  if (node.type === 'directory') {
    try {
      currentPath.value = node.path;
      // const response = await axios.get('http://192.168.10.124:8080/api/assets/catalogue', {
      //   params: { path: currentPath.value } // node.path 是目录路径
      // });
      const response = await getDirectory(currentPath.value)

      tableData.value = response.data;
      console.log('node.path: ', node.path)
      console.log('currentPath.value22222: ', currentPath.value)
      console.log('tableData.value: ', tableData.value)
      console.log('node.label: ', node.label)
    } catch (error) {
      console.error('Failed to fetch files', error);
    }
  }
};



// 示例按钮的点击处理函数
const uploadFile = () => {
  // 实现上传文件逻辑
};

const createFolder = () => {
  // 实现新建目录逻辑
};

const moveItem = () => {
  // 实现移动功能逻辑
};

const deleteItem = () => {
  // 实现删除功能逻辑
};

const publishItem = () => {
  // 实现发布功能逻辑
};

const unpublishItem = () => {
  // 实现取消发布功能逻辑
};


// treeData.value = [
//   {
//     "label": "folder1",
//     "type": "directory",
//     "children": [
//       {
//         "label": "file1.txt",
//         "type": "file"
//       },
//     ]
//   },
//   {
//     "label": "file3.txt",
//     "type": "file"
//   }
// ]

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
