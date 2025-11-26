<template>
    <el-container style="height: 100vh; border: 1px solid #eee">
        <el-header style="font-size: 40px; background-color: rgb(238, 241, 246)">学习辅助系统</el-header>

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
                        <el-date-picker v-model="searchForm.entrydate" type="daterange" range-separator="至"
                            start-placeholder="开始日期" end-placeholder="结束日期" value-format="YYYY-MM-DD" />
                    </el-form-item>

                    <el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="handleSearch">查询</el-button>
                            <el-button type="info" @click="clearSearch">清空</el-button>
                            <el-button type="success" @click="openAddDialog">+ 新增员工</el-button>
                        </el-form-item>
                    </el-form-item>
                </el-form>

                <el-table :data="tableData" border style="width: 100%">
                    <el-table-column label="序号" type="index" width="60" />
                    <el-table-column prop="username" label="用户名" width="120" />
                    <el-table-column prop="name" label="姓名" width="100" />

                    <el-table-column label="图像" width="120">
                        <template #default="scope">
                            <el-image style="width: 50px; height: 50px; border-radius: 5px;" :src="scope.row.image"
                                :preview-src-list="[scope.row.image]" />
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
                            <el-button type="primary" size="small" @click="handleEdit(scope.row.id)">
                                编辑
                            </el-button>

                            <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">
                                删除
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>

                <br>
                <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize"
                    :page-sizes="[5, 10, 20, 50]" layout="total, sizes, prev, pager, next, jumper" :total="total"
                    @size-change="handleSizeChange" @current-change="handleCurrentChange" />

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
                            <el-upload class="avatar-uploader" action="http://localhost:8080/upload" name="image"
                                :show-file-list="false" :on-success="handleAvatarSuccess">
                                <img v-if="empForm.image" :src="empForm.image" class="avatar"
                                    style="width: 100px; height: 100px; display: block;" />
                                <el-icon v-else class="avatar-uploader-icon"
                                    style="border: 1px dashed #d9d9d9; width: 100px; height: 100px; line-height: 100px; text-align: center; font-size: 28px; color: #8c939d;">
                                    +
                                </el-icon>
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
                            <el-date-picker v-model="empForm.entrydate" type="date" placeholder="选择日期"
                                value-format="YYYY-MM-DD" style="width: 100%;" />
                        </el-form-item>

                        <el-form-item label="归属部门">
                            <el-select v-model="empForm.deptId" placeholder="请选择">
                                <el-option v-for="item in deptList" :key="item.id" :label="item.name"
                                    :value="item.id" />
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
import axios from 'axios';
import { ElMessage, ElMessageBox } from 'element-plus'; // 别忘了检查是否已导入 ElMessageBox

// --- 数据定义 ---
const tableData = ref([]);
const total = ref(0);
const currentPage = ref(1); // 当前页码
const pageSize = ref(10);   // 每页条数

// 搜索表单数据
const searchForm = reactive({
    name: '',
    gender: '',
    entrydate: [] // 日期范围数组 [begin, end]
});

// --- 核心方法：分页查询 ---
const pageQuery = async () => {
    try {
        // 构造请求参数
        const params = {
            page: currentPage.value,
            gender: searchForm.gender,
            // 如果选择了日期，拆分为 begin 和 end
            begin: searchForm.entrydate ? searchForm.entrydate[0] : null,
            end: searchForm.entrydate ? searchForm.entrydate[1] : null
        };

        const res = await axios.get('http://localhost:8080/emps', { params });

        if (res.data.code === 1) {
            tableData.value = res.data.data.rows;
            total.value = res.data.data.total;
        } else {
            ElMessage.error(res.data.message);
        }
    } catch (error) {
        console.error(error);
        ElMessage.error('数据加载失败');
    }
};

// --- 事件处理 ---
// 搜索按钮
const handleSearch = () => {
    currentPage.value = 1; // 搜索时重置为第一页
    pageQuery();
};

// 清空按钮
const clearSearch = () => {
    searchForm.name = '';
    searchForm.gender = '';
    searchForm.entrydate = [];
    handleSearch();
};

// 每页条数改变
const handleSizeChange = (val) => {
    pageSize.value = val;
    pageQuery();
};

// 页码改变
const handleCurrentChange = (val) => {
    currentPage.value = val;
    pageQuery();
};
// 删除员工 (单个删除也复用批量删除接口)
const handleDelete = (id) => {
    ElMessageBox.confirm('你确认要删除该员工信息吗?', '温馨提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
    })
        .then(async () => {
            // 发送请求，路径直接拼接 id
            // 虽然是批量接口，但传一个 id (如 /emps/1) 也是完全可以的
            await axios.delete(`http://localhost:8080/emps/${id}`);
            ElMessage.success('删除成功');
            // 重新加载列表
            pageQuery();
        })
        .catch(() => {
            ElMessage.info('已取消删除');
        });
};

// ... 原有的 import ...

// --- 新增：变量定义 ---
const dialogVisible = ref(false); // 控制弹窗显示
const deptList = ref([]); // 部门下拉框数据
const empForm = ref({     // 表单数据对象
    id: '',
    username: '',
    name: '',
    gender: '',
    image: '',
    job: '',
    entrydate: '',
    deptId: ''
});

// --- 新增：加载部门列表 (用于下拉框) ---
// 注意：你需要确保 DeptController 有一个查询所有部门的接口
const getDeptList = async () => {
    const res = await axios.get('http://localhost:8080/depts');
    if (res.data.code === 1) {
        deptList.value = res.data.data;
    }
};


// 点击“新增员工”时调用
const openAddDialog = () => {
    dialogVisible.value = true;
    empForm.id = ''; // <--- 重要！清空 ID 表示这是新增
    empForm.username = '';
    empForm.name = '';
    empForm.gender = '';
    empForm.image = '';
    empForm.job = '';
    empForm.entrydate = '';
    empForm.deptId = '';

    getDeptList(); // 加载部门下拉框
}

// --- 新增：图片上传成功回调 ---
const handleAvatarSuccess = (result) => {
    // result 是后端 Result 对象，data 属性是图片的 URL
    if (result.code === 1) {
        empForm.value.image = result.data;
    } else {
        ElMessage.error(result.message);
    }
};

// 点击“保存”时调用
const saveEmp = async () => {
    // 简单的非空校验 (可选)
    if (!empForm.name) return ElMessage.warning("姓名不能为空");

    let res;
    // 判断逻辑：有 ID 发 PUT (修改)，无 ID 发 POST (新增)
    if (empForm.id) {
        // 修改
        res = await axios.put('http://localhost:8080/emps', empForm);
    } else {
        // 新增
        res = await axios.post('http://localhost:8080/emps', empForm);
    }

    if (res.data.code === 1) {
        ElMessage.success(empForm.id ? '修改成功' : '新增成功');
        dialogVisible.value = false;
        pageQuery(); // 刷新列表
    } else {
        ElMessage.error(res.data.message);
    }
}




// --- 初始化 ---
onMounted(() => {
    pageQuery();
});
</script>

<style scoped>
/* 简单的侧边栏样式 */
.el-aside {
    color: #333;
}
</style>