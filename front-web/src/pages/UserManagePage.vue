<template>
  <div class="user-manage-page">
    <a-card  :bordered="false">
      <!-- 搜索表单 -->
      <a-form
        :model="searchForm"
        layout="inline"
        class="search-form"
        @finish="handleSearch"
      >
        <a-form-item label="用户ID" name="id">
          <a-input
            v-model:value="searchForm.id"
            placeholder="请输入用户ID"
            allow-clear
            style="width: 200px"
          />
        </a-form-item>
        <a-form-item label="账号" name="userAccount">
          <a-input
            v-model:value="searchForm.userAccount"
            placeholder="请输入账号"
            allow-clear
            style="width: 200px"
          />
        </a-form-item>
        <a-form-item label="用户名" name="userName">
          <a-input
            v-model:value="searchForm.userName"
            placeholder="请输入用户名"
            allow-clear
            style="width: 200px"
          />
        </a-form-item>
        <a-form-item>
          <a-space>
            <a-button type="primary" html-type="submit" :loading="loading">
              <SearchOutlined />
              搜索
            </a-button>
            <a-button @click="handleReset">
              <ReloadOutlined />
              重置
            </a-button>
          </a-space>
        </a-form-item>
      </a-form>

      <!-- 操作按钮 -->
      <div class="table-actions">
        <a-button type="primary" @click="handleAdd">
          <PlusOutlined />
          新增用户
        </a-button>
      </div>

      <!-- 用户表格 -->
      <a-table
        :columns="columns"
        :data-source="userList"
        :loading="loading"
        :pagination="pagination"
        @change="handleTableChange"
        row-key="id"
        class="user-table"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'updateTime'">
            {{ formatDate(record.updateTime) }}
          </template>
          <template v-else-if="column.key === 'action'">
            <a-space>
              <a-button
                type="link"
                size="small"
                @click="handleEdit(record)"
              >
                <EditOutlined />
                编辑
              </a-button>
              <a-popconfirm
                title="确定要删除该用户吗？"
                ok-text="确定"
                cancel-text="取消"
                @confirm="handleDelete(record.id)"
              >
                <a-button type="link" danger size="small">
                  <DeleteOutlined />
                  删除
                </a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 编辑用户弹窗 -->
    <a-modal
      v-model:open="editModalVisible"
      :title="editingUser?.id ? '编辑用户' : '新增用户'"
      @ok="handleSaveUser"
      @cancel="handleCancelEdit"
      :confirm-loading="saveLoading"
      width="600px"
    >
      <a-form
        :model="editForm"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
      >
        <a-form-item label="账号" name="userAccount" required>
          <a-input
            v-model:value="editForm.userAccount"
            placeholder="请输入账号"
            :disabled="!!editingUser?.id"
          />
        </a-form-item>
        <a-form-item label="用户名" name="userName" required>
          <a-input
            v-model:value="editForm.userName"
            placeholder="请输入用户名"
          />
        </a-form-item>
        <a-form-item 
          label="密码" 
          name="userPassword" 
          :required="!editingUser"
        >
          <a-input-password
            v-model:value="editForm.userPassword"
            :placeholder="editingUser ? '不修改请留空' : '请输入密码（至少6位）'"
          />
        </a-form-item>
        <a-form-item label="头像" name="userAvatar">
          <a-input
            v-model:value="editForm.userAvatar"
            placeholder="请输入头像URL"
          />
        </a-form-item>
        <a-form-item label="简介" name="userProfile">
          <a-textarea
            v-model:value="editForm.userProfile"
            placeholder="请输入用户简介"
            :rows="3"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import {
  SearchOutlined,
  ReloadOutlined,
  EditOutlined,
  DeleteOutlined,
  PlusOutlined,
} from '@ant-design/icons-vue'
import { page, remove, update, save } from '@/api/userController'
import type { TableProps } from 'ant-design-vue'

// 搜索表单
const searchForm = reactive({
  id: undefined as number | undefined,
  userAccount: '',
  userName: '',
})

// 表格列定义
const columns = [
  {
    title: '用户ID',
    dataIndex: 'id',
    key: 'id',
    width: 100,
  },
  {
    title: '账号',
    dataIndex: 'userAccount',
    key: 'userAccount',
    width: 150,
  },
  {
    title: '用户名',
    dataIndex: 'userName',
    key: 'userName',
    width: 150,
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    key: 'updateTime',
    width: 180,
  },
  {
    title: '操作',
    key: 'action',
    width: 180,
    fixed: 'right' as const,
  },
]

// 用户列表数据
const userList = ref<API.LoginUserVO[]>([])
const loading = ref(false)

// 分页配置
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
  showSizeChanger: true,
  showQuickJumper: true,
  showTotal: (total: number) => `共 ${total} 条`,
  pageSizeOptions: ['10', '20', '50', '100'],
})

// 编辑相关
const editModalVisible = ref(false)
const editingUser = ref<API.LoginUserVO | null>(null)
const saveLoading = ref(false)
const editForm = reactive({
  userAccount: '',
  userName: '',
  userPassword: '',
  userAvatar: '',
  userProfile: '',
})

// 加载用户列表
const loadUserList = async () => {
  loading.value = true
  try {
    const params: API.UserListRequest = {
      current: pagination.current,
      pageSize: pagination.pageSize,
    }

    // 添加搜索条件
    if (searchForm.id) {
      params.id = searchForm.id
    }
    if (searchForm.userAccount) {
      params.userAccount = searchForm.userAccount
    }
    if (searchForm.userName) {
      params.userName = searchForm.userName
    }

    const res = await page(params)
    if (res.data.code === 0 && res.data.data) {
      userList.value = res.data.data.records || []
      pagination.total = res.data.data.totalRow || 0
    } else {
      message.error(res.data.message || '加载用户列表失败')
    }
  } catch (error) {
    message.error('加载用户列表失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  loadUserList()
}

// 重置搜索
const handleReset = () => {
  searchForm.id = undefined
  searchForm.userAccount = ''
  searchForm.userName = ''
  pagination.current = 1
  loadUserList()
}

// 表格变化处理
const handleTableChange: TableProps['onChange'] = (pag) => {
  pagination.current = pag.current || 1
  pagination.pageSize = pag.pageSize || 10
  loadUserList()
}

// 新增用户
const handleAdd = () => {
  editingUser.value = null
  editForm.userAccount = ''
  editForm.userName = ''
  editForm.userPassword = ''
  editForm.userAvatar = ''
  editForm.userProfile = ''
  editModalVisible.value = true
}

// 编辑用户
const handleEdit = (record: API.LoginUserVO) => {
  editingUser.value = record
  editForm.userAccount = record.userAccount || ''
  editForm.userName = record.userName || ''
  editForm.userPassword = ''
  editForm.userAvatar = record.userAvatar || ''
  editForm.userProfile = record.userProfile || ''
  editModalVisible.value = true
}

// 取消编辑
const handleCancelEdit = () => {
  editModalVisible.value = false
  editingUser.value = null
  editForm.userAccount = ''
  editForm.userName = ''
  editForm.userPassword = ''
  editForm.userAvatar = ''
  editForm.userProfile = ''
}

// 保存用户
const handleSaveUser = async () => {
  if (!editForm.userAccount || !editForm.userName) {
    message.warning('请填写账号和用户名')
    return
  }

  // 新增用户时必须填写密码
  if (!editingUser.value && !editForm.userPassword) {
    message.warning('请填写密码')
    return
  }

  // 新增用户时密码长度验证
  if (!editingUser.value && editForm.userPassword.length < 6) {
    message.warning('密码长度至少6位')
    return
  }

  saveLoading.value = true
  try {
    const userData: API.User = {
      id: editingUser.value?.id,
      userAccount: editForm.userAccount,
      userName: editForm.userName,
      userAvatar: editForm.userAvatar,
      userProfile: editForm.userProfile,
    }

    // 如果填写了密码，则包含密码字段
    if (editForm.userPassword) {
      userData.userPassword = editForm.userPassword
    }

    // 根据是否有 id 判断是新增还是更新
    const res = editingUser.value 
      ? await update(userData)
      : await save(userData)
      
    if (res.data.code === 0) {
      message.success(editingUser.value ? '更新成功' : '新增成功')
      handleCancelEdit()
      loadUserList()
    } else {
      message.error(res.data.message || '保存失败')
    }
  } catch (error) {
    message.error('保存失败')
    console.error(error)
  } finally {
    saveLoading.value = false
  }
}

// 删除用户
const handleDelete = async (id?: number) => {
  if (!id) {
    message.error('用户ID不存在')
    return
  }

  try {
    const res = await remove({ id })
    if (res.data.code === 0) {
      message.success('删除成功')
      loadUserList()
    } else {
      message.error(res.data.message || '删除失败')
    }
  } catch (error) {
    message.error('删除失败')
    console.error(error)
  }
}

// 格式化日期
const formatDate = (dateStr?: string) => {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleString('zh-CN')
}

// 初始化加载
onMounted(() => {
  loadUserList()
})
</script>

<style scoped>
.user-manage-page {
  padding: 24px;
}

.search-form {
  margin-bottom: 24px;
  padding: 16px;
  border-radius: 4px;
}

.table-actions {
  margin-bottom: 16px;
}

.user-table {
  margin-top: 16px;
}
</style>
