<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'

import { ref } from 'vue'
import { articleCategoryListService, articleListService, articleAddService, articleUpdateService, articleDeleteService } from '@/api/article.js'


//文章分类数据模型
const categories = ref([
    // {
    //     "id": "",
    //     "categoryName": "",
    //     "categoryAlias": "",
    //     "createTime": "",
    //     "updateTime": ""
    // }
])

//用户搜索时选中的分类id
const categoryId = ref('')

//用户搜索时选中的发布状态
const state = ref('')

//文章列表数据模型
const articles = ref([
    // {
    //     "id": "",
    //     "title": "",
    //     "content": "",
    //     "coverImg": "",
    //     "state": "",
    //     "categoryId": "",
    //     "createTime": "",
    //     "updateTime": ""
    // }
])

//分页条数据模型
const pageNum = ref(1)//当前页
const total = ref(20)//总条数
const pageSize = ref(3)//每页条数

//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
    pageSize.value = size
    articleList()
}
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
    pageNum.value = num
    articleList()
}

// 回写文章分类 --> 分类下拉框
const articleCategoryList = async () => {
    let result = await articleCategoryListService();
    categories.value = result.data;
}


// 获取文章列表数据
const articleList = async () => {
    let params = {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        categoryId: categoryId.value ? categoryId.value : null,
        state: state.value ? state.value : null
    }
    let result = await articleListService(params);
    // 渲染视图
    total.value = result.data.total;
    articles.value = result.data.items;
    // 处理数据
    for(let i=0;i<articles.value.length;i++){
        let article = articles.value[i];
        for(let j=0;j<categories.value.length;j++){
            if(article.categoryId == categories.value[j].id){
                article.categoryName = categories.value[j].categoryName;
            }
        }
    }
}
articleCategoryList();
articleList();
// 重置 --> 清空
const articleReset = () => {
    categoryId.value = '';
    state.value = '';
    articleList();
}

import { Plus } from '@element-plus/icons-vue'
import { Quill, QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
// 新增文章
//控制抽屉是否显示
const visibleDrawer = ref(false)
//添加表单数据模型
const articleModel = ref({
    title: '',
    categoryId: '',
    coverImg: '',
    content: '',
    state: ''
})
// 导入token
import { useTokenStore } from '@/stores/token';
const tokenStore = useTokenStore();

// 上传成功回调函数
const uploadSuccess = (result)=>{
    articleModel.value.coverImg = result.data;
    console.log(result.data);
}

// 添加文章
import { ElMessage,ElMessageBox } from 'element-plus';
const addArticle = async (clickState) =>{
    // 把发布状态赋值给数据模型
    articleModel.value.state = clickState;
    // 调用接口
    let result = await articleAddService(articleModel.value);
    ElMessage.success('添加成功')
    // 抽屉消失
    visibleDrawer.value = false;
    // 刷新列表
    articleList();
}

// 定义变量，控制弹窗标题展示
const articleTitle = ref('')
// 显示编辑弹窗
const showDrawer = (row) =>{
    visibleDrawer.value = true; articleTitle.value = '编辑文章'
    // 拷贝数据
    articleModel.value.title = row.title;
    articleModel.value.categoryId = row.categoryId;
    articleModel.value.coverImg = row.coverImg;
    articleModel.value.state = row.state;
    articleModel.value.content = row.content;
    articleModel.value.id = row.id
}

// 编辑分类
const updateArticle = async (clickState)=>{
    articleModel.value.state = clickState;
    let result = await articleUpdateService(articleModel.value);
    ElMessage.success('修改成功')
    // 抽屉消失刷新列表数据
    visibleDrawer.value = false;
    articleList();
}

// 清空数据模型
const clearData = ()=>{
    articleModel.value.title = '';
    articleModel.value.categoryId = '';
    articleModel.value.coverImg = '';
    articleModel.value.state = '';
    const doc = QuillEditor.getText(articleModel.value.content);
    const length = QuillEditor.getLength(doc);
    QuillEditor.deleteText(0, length)
}

// 删除文章
const deleteArticle = (row)=>{
    ElMessageBox.confirm(
        '确认要删除该文章信息么?',
        'Warning',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(async () => {
            // 调用接口删除
            await articleDeleteService(row.id);
            ElMessage({
                type: 'success',
                message: '删除成功',
            })
            // 刷新列表
            articleList();
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '取消删除',
            })
        })
}
const multipleSelection = ref([])
// 复选框
const handleSelectionChange = (val)=>{
    multipleSelection.value = val
}
const multiDelete = ()=>{
    ElMessageBox.confirm(
        '确认要删除该文章信息么?',
        'Warning',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then( () => {
            // 从multipleSelection中获取id集合
            multipleSelection.value.forEach(async row =>{
                await articleDeleteService(row.id);
            })
            ElMessage({
                type: 'success',
                message: '删除成功',
            })
            // 刷新列表
            articleList();
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '取消删除',
            })
        })
}


</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>文章管理</span>
                <div class="extra">
                    <el-button type="primary" @click="visibleDrawer = true; articleTitle = '添加文章'; clearData()">添加文章</el-button>
                </div>
            </div>
        </template>

        <!-- 搜索表单 -->
        <el-form inline>
            <el-form-item label="文章分类：">
                <el-select placeholder="请选择" v-model="categoryId" style="width: 200px;">
                    <el-option v-for="c in categories" :key="c.id" :label="c.categoryName" :value="c.id">
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="发布状态：">
                <el-select placeholder="请选择" v-model="state" style="width: 200px;">
                    <el-option label="已发布" value="已发布"></el-option>
                    <el-option label="草稿" value="草稿"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="articleList">搜索</el-button>
                <el-button @click="articleReset">重置</el-button>
                <el-button type="danger" size="medium" @click="multiDelete">批量删除</el-button>
            </el-form-item>
            
        </el-form>

        <!-- 文章列表 -->
        <el-table :data="articles" style="width: 100%" @selection-change="handleSelectionChange">
            <el-table-column  type="selection" width="50"/>
            <el-table-column label="文章ID" prop="id" width="100"></el-table-column>
            <el-table-column label="文章标题" width="400" prop="title"></el-table-column>
            <el-table-column label="分类" prop="categoryName"></el-table-column>
            <el-table-column label="发表时间" prop="createTime"> </el-table-column>
            <el-table-column label="状态" prop="state"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="showDrawer(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteArticle(row)"></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>

        <!-- 分页条 -->
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[2, 3, 5, 10, 15]"
            layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange"
            @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />

        <!-- 抽屉: 添加文章-->
        <el-drawer v-model="visibleDrawer" :title="articleTitle" direction="rtl" size="50%">
            <!-- 添加文章表单 -->
            <el-form :model="articleModel" label-width="100px">
                <el-form-item label="文章标题">
                    <el-input v-model="articleModel.title" placeholder="请输入标题"></el-input>
                </el-form-item>
                <el-form-item label="文章分类">
                    <el-select placeholder="请选择" v-model="articleModel.categoryId">
                        <el-option v-for="c in categories" :key="c.id" :label="c.categoryName" :value="c.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="文章封面">
                    <!-- 
                        auto-upload: 是否自动上传
                        action: 服务器接口路径
                        name: 上传文件字段名
                        headers: 上传请求头
                        on-success: 上传成功的回调函数
                     -->
                    <el-upload class="avatar-uploader" :auto-upload="true" :show-file-list="false"
                    action="/api/upload" name="file" :headers="{'Authorization': tokenStore.token}" :on-success="uploadSuccess">
                        <img v-if="articleModel.coverImg" :src="articleModel.coverImg" class="avatar" />
                        <el-icon v-else class="avatar-uploader-icon">
                            <Plus />
                        </el-icon>
                    </el-upload>
                </el-form-item>
                <el-form-item label="文章内容">
                    <div class="editor">
                        <quill-editor theme="snow" v-model:content="articleModel.content" contentType="html">
                        </quill-editor>
                    </div>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="articleTitle == '添加文章' ? addArticle('已发布') : updateArticle('已发布')">发布</el-button>
                    <el-button type="info" @click="articleTitle == '添加文章' ? addArticle('草稿') : updateArticle('草稿')">草稿</el-button>
                </el-form-item>
            </el-form>
        </el-drawer>

    </el-card>
</template>

<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}

/* 抽屉样式 */
.avatar-uploader {
    :deep() {
        .avatar {
            width: 178px;
            height: 178px;
            display: block;
        }

        .el-upload {
            border: 1px dashed var(--el-border-color);
            border-radius: 6px;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: var(--el-transition-duration-fast);
        }

        .el-upload:hover {
            border-color: var(--el-color-primary);
        }

        .el-icon.avatar-uploader-icon {
            font-size: 28px;
            color: #8c939d;
            width: 178px;
            height: 178px;
            text-align: center;
        }
    }
}

.editor {
    width: 100%;

    :deep(.ql-editor) {
        min-height: 200px;
    }
}
</style>