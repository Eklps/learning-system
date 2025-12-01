<template>
  <el-container style="height: 100vh; border: 1px solid #eee">
    <el-header style="font-size: 40px; background-color: rgb(238, 241, 246)">tlias 智能学习辅助系统</el-header>
    
    <el-container>
      <el-aside width="230px" style="border-right: 1px solid #eee">
        <el-menu :default-openeds="['1']">
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
        <el-form :inline="true" :model="searchForm" class="demo-form-inline">
          <el-form-item label="姓名">
            <el-input v-model="searchForm.name" placeholder="请输入员工姓名" />
          </el-form-item>
          
          <el-form-item label="性别">
            <el-select v-model="searchForm.gender" placeholder="请选择" style="width: 100px;">
              <el-option label="男" value="1" />
              <el-option label="女" value="2" />
            </el-select>
          </el-form-item>
          
          <el-form-item label="入职日期">
            <el-date-picker
              v-model="searchForm.entrydate"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="YYYY-MM-DD"
            />
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="handleSearch">查询</el-button>
            <el-button type="info" @click="clearSearch">清空</el-button>
            <el-button type="success" @click="openAddDialog">+ 新增员工</el-button>
          </el-form-item>
        </el-form>

        <el-table :data="tableData" border style="width: 100%">
          <el-table-column label="序号" type="index" width="60" />
          <el-table-column prop="username" label="用户名" width="120" />
          <el-table-column prop="name" label="姓名" width="100" />
          
          <el-table-column label="图像" width="120">
            <template #default="scope">
              <el-image 
                style="width: 50px; height: 50px; border-radius: 5px;" 
                :src="scope.row.image" 
                :preview-src-list="[scope.row.image]"
              />
            </template>
          </el-table-column>
          
          <el-table-column label="性别" width="80">
            <template #default="scope">
              {{ scope.row.gender == 1 ? '男' : '女' }}
            </template>
          </el-table-column>
          
          <el-table-column label="职位" width="120">
            <template #default="scope">
              <span v-if="scope.row.job == 1">班主任</span>
              <span v-else-if="scope.row.job == 2">讲师</span>
              <span v-else-if="scope.row.job == 3">学工主管</span>
              <span v-else-if="scope.row.job == 4">教研主管</span>
              <span v-else-if="scope.row.job == 5">咨询师</span>
              <span v-else>其他</span>
            </template>
          </el-table-column>
          
          <el-table-column prop="entrydate" label="入职日期" width="140" />
          <el-table-column prop="updateTime" label="最后修改时间" width="180" />
          
          <el-table-column label="操作" fixed="right" width="150">
            <template #default="scope">
              <el-button type="primary" size="small" @click="handleEdit(scope.row.id)">编辑</el-button>
              <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <br>
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[5, 10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />

        <el-dialog v-model="dialogVisible" title="新增员工" width="40%">
          <el-form :model="empForm" label-width="100px">
            <el-form-item label="用户名">
              <el-input v-model="empForm.username" placeholder="请输入用户名" />
            </el-form-item>
            <el-form-item label="员工姓名">
              <el-input v-model="empForm.name" placeholder="请输入姓名" />
            </el-form-item>
            <el-form-item label="性别">
              <el-select v-model="empForm.gender" placeholder="请选择">
                <el-option label="男" value="1" />
                <el-option label="女" value="2" />
              </el-select>
            </el-form-item>
            <el-form-item label="头像">
              <el-upload
                class="avatar-uploader"
                action="http://localhost:8080/upload"
                name="image"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
              >
                <img v-if="empForm.image" :src="empForm.image" class="avatar" style="width: 100px; height: 100px; display: block;" />
                <el-icon v-else class="avatar-uploader-icon" style="border: 1px dashed #d9d9d9; width: 100px; height: 100px; line-height: 100px; text-align: center; font-size: 28px; color: #8c939d;">+</el-icon>
              </el-upload>
            </el-form-item>
            <el-form-item label="职位">
              <el-select v-model="empForm.job" placeholder="请选择">
                <el-option label="班主任" value="1" />
                <el-option label="讲师" value="2" />
                <el-option label="学工主管" value="3" />
                <el-option label="教研主管" value="4" />
                <el-option label="咨询师" value="5" />
              </el-select>
            </el-form-item>
            <el-form-item label="入职日期">
              <el-date-picker v-model="empForm.entrydate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%;" />
            </el-form-item>
            <el-form-item label="归属部门">
              <el-select v-model="empForm.deptId" placeholder="请选择">
                <el-option v-for="item in deptList" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-form>
          <template #footer>
            <span class="dialog-footer">
              <el-button @click="dialogVisible = false">取消</el-button>
              <el-button type="primary" @click="saveEmp">保存</el-button>
            </span>
          </template>
        </el-dialog>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import request from '@/utils/request';
import { ElMessage, ElMessageBox } from 'element-plus';

// 数据定义
const tableData = ref([]);
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(10);
const dialogVisible = ref(false);
const deptList = ref([]);

const searchForm = reactive({
  name: '',
  gender: '',
  entrydate: []
});

const empForm = reactive({
  id: '',
  username: '',
  name: '',
  gender: '',
  image: '',
  job: '',
  entrydate: '',
  deptId: ''
});

// 分页查询
const pageQuery = async () => {
  try {
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value,
      name: searchForm.name,
      gender: searchForm.gender,
      begin: searchForm.entrydate ? searchForm.entrydate[0] : null,
      end: searchForm.entrydate ? searchForm.entrydate[1] : null
    };
    const res = await request.get('/emps', { params });
    if (res.data.code === 1) {
      tableData.value = res.data.data.rows;
      total.value = res.data.data.total;
    } else {
      ElMessage.error(res.data.message);
    }
  } catch (error) {
    console.error(error);
  }
};

// 各种事件处理
const handleSearch = () => { currentPage.value = 1; pageQuery(); };
const clearSearch = () => {
  searchForm.name = '';
  searchForm.gender = '';
  searchForm.entrydate = [];
  handleSearch();
};
const handleSizeChange = (val) => { pageSize.value = val; pageQuery(); };
const handleCurrentChange = (val) => { currentPage.value = val; pageQuery(); };

// 删除
const handleDelete = (id) => {
  ElMessageBox.confirm('确认删除吗?', '提示', { type: 'warning' })
    .then(async () => {
      await request.delete(`/emps/${id}`);
      ElMessage.success('删除成功');
      pageQuery();
    }).catch(() => {});
};

// 加载部门
const getDeptList = async () => {
  const res = await request.get('/depts');
  if (res.data.code === 1) deptList.value = res.data.data;
};

// 打开新增
const openAddDialog = () => {
  dialogVisible.value = true;
  Object.assign(empForm, {
    id: '', username: '', name: '', gender: '', image: '', job: '', entrydate: '', deptId: ''
  });
  getDeptList();
};

// 打开编辑 (回显)
const handleEdit = async (id) => {
  dialogVisible.value = true;
  getDeptList();
  const res = await request.get(`/emps/${id}`);
  if (res.data.code === 1) {
    Object.assign(empForm, res.data.data);
  }
};

// 上传回调
const handleAvatarSuccess = (res) => {
  if (res.code === 1) empForm.image = res.data;
  else ElMessage.error(res.message);
};

// 保存 (新增/修改)
const saveEmp = async () => {
  if (!empForm.name) return ElMessage.warning("姓名不能为空");
  let res;
  if (empForm.id) {
    res = await request.put('/emps', empForm);
  } else {
    res = await request.post('/emps', empForm);
  }
  if (res.data.code === 1) {
    ElMessage.success('操作成功');
    dialogVisible.value = false;
    pageQuery();
  } else {
    ElMessage.error(res.data.message);
  }
};

onMounted(() => {
  pageQuery();
});
</script>

<style scoped>
.el-aside { color: #333; }
.avatar-uploader .avatar { width: 100px; height: 100px; display: block; }
.avatar-uploader .el-upload { border: 1px dashed #d9d9d9; border-radius: 6px; cursor: pointer; position: relative; overflow: hidden; }
.avatar-uploader .el-upload:hover { border-color: #409EFF; }
</style>