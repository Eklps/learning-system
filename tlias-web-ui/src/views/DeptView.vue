<template>
  <el-container style="height: 100vh; border: 1px solid #eee">
    <el-header style="font-size: 40px; background-color: rgb(238, 241, 246)">学习辅助系统</el-header>
    
    <el-container>
      <el-aside width="230px" style="border-right: 1px solid #eee">
        <el-menu :default-openeds="['1']" default-active="1-1">
          <el-sub-menu index="1">
            <template #title>系统信息管理</template>
            <el-menu-item index="1-1">
              <router-link to="/dept" style="text-decoration: none; color: inherit;">部门管理</router-link>
            </el-menu-item>
            <el-menu-item index="1-2">
              <router-link to="/emp" style="text-decoration: none; color: inherit;">员工管理</router-link>
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      
      <el-main>
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>部门列表</span>
              <el-button type="primary" @click="openAddDialog">新增部门</el-button>
            </div>
          </template>
          
          <el-table :data="deptList" style="width: 100%" border>
            <el-table-column type="index" label="序号" width="100" />
            <el-table-column prop="name" label="部门名称" />
            <el-table-column prop="createTime" label="创建时间" />
            <el-table-column prop="updateTime" label="修改时间" />
            
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="scope">
                <el-button type="primary" size="small" @click="handleEdit(scope.row.id)">编辑</el-button>
                <el-button 
                  type="danger" 
                  size="small" 
                  @click="handleDelete(scope.row)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          
          <el-dialog
              v-model="dialogVisible"
              :title="dialogTitle" width="30%"
              :before-close="handleClose"
          >
              <el-form :model="deptForm" label-width="100px">
                  <el-form-item label="部门名称">
                      <el-input v-model="deptForm.name" />
                  </el-form-item>
              </el-form>
              
              <template #footer>
                  <span class="dialog-footer">
                      <el-button @click="handleClose">取消</el-button>
                      <el-button type="primary" @click="handleSubmit">
                          确定
                      </el-button>
                  </span>
              </template>
          </el-dialog>
        </el-card>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue';
import request from '@/utils/request';
import { ElMessage, ElMessageBox } from 'element-plus'; 

// --- 原有的 Script 逻辑保持不变 ---

// 数据定义
const deptList = ref([]);
const dialogVisible = ref(false);
const dialogTitle = ref('');
const deptForm = reactive({
    id: '',
    name: ''
});

// 查询列表
const getDeptList = async () => {
    const response = await request.get('http://localhost:8080/depts');
    if (response.data.code === 1) deptList.value = response.data.data;
}

// 打开新增窗口
const openAddDialog = () => {
    dialogTitle.value = '新增部门';
    dialogVisible.value = true;
    deptForm.id = '';
    deptForm.name = '';
}

// 打开编辑窗口
const handleEdit = async (id) => {
    dialogTitle.value = '修改部门';
    dialogVisible.value = true;
    const response = await request.get(`http://localhost:8080/depts/${id}`);
    if (response.data.code === 1) {
        const data = response.data.data;
        deptForm.id = data.id;
        deptForm.name = data.name;
    }
}

// 提交表单
const handleSubmit = async () => {
    if (!deptForm.name) return ElMessage.warning('部门名称不能为空');

    let response;
    if (deptForm.id) {
        response = await request.put('http://localhost:8080/depts', deptForm);
    } else {
        response = await request.post('http://localhost:8080/depts', deptForm);
    }

    if (response.data.code === 1) {
        ElMessage.success(deptForm.id ? '修改成功' : '新增成功');
        dialogVisible.value = false;
        getDeptList();
    } else {
        ElMessage.error(response.data.message);
    }
}

// 删除
const handleDelete = (row) => {
    ElMessageBox.confirm(`确定删除部门 [${row.name}] 吗?`, '警告', { type: 'warning' })
    .then(async () => {
        const response = await axios.delete(`http://localhost:8080/depts/${row.id}`);
        if (response.data.code === 1) {
            ElMessage.success('删除成功');
            getDeptList();
        }
    });
}

// 关闭清理
const handleClose = () => {
    dialogVisible.value = false;
    deptForm.id = '';
    deptForm.name = '';
}

onMounted(() => {
    getDeptList();
});
</script>

<style scoped>
/* 样式调整 */
.el-header {
  line-height: 60px; /* 让标题垂直居中 */
}
.el-aside {
  color: #333;
}
/* 卡片样式，去掉外边距，让它在 Main 里自然撑开 */
.box-card {
  /* margin: 20px;  <-- 可以去掉这个，或者保留看你喜好 */
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>