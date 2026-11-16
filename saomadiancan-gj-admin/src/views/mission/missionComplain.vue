<template>
  <div class="nf-page nf-missioncomplain-page">
    <div class="nf-page-hero nf-missioncomplain-hero">
      <span class="nf-page-badge">任务</span>
      <h2 class="nf-missioncomplain-hero__title">任务投诉</h2>
      <p class="nf-missioncomplain-hero__desc">查看与管理用户任务投诉记录，支持按投诉内容筛选查询与处理</p>
    </div>

    <div class="nf-filter-card nf-missioncomplain-filter">
      <div class="nf-filter-card__head">
        <i class="el-icon-search"></i>
        <span>筛选条件</span>
      </div>
      <div class="nf-filter-card__body nf-missioncomplain-filter__body">
        <div class="nf-missioncomplain-filter__item">
          <span class="nf-missioncomplain-filter__label">投诉内容：</span>
          <el-input
            class="nf-missioncomplain-filter__input"
            @keydown.enter.native="select"
            clearable
            placeholder="请输入投诉内容"
            v-model="content">
          </el-input>
        </div>
        <div class="nf-missioncomplain-filter__actions">
          <el-button size="mini" type="primary" icon="document" @click="select">查询</el-button>
          <el-button size="mini" type="primary" icon="document" @click="cleans">重置</el-button>
        </div>
      </div>
    </div>

    <div class="nf-table-card nf-missioncomplain-table-card">
      <el-table
        v-loading="tableDataLoading"
        :data="tableData.list">
        <el-table-column
          fixed
          prop="id"
          label="id"
          width="80">
        </el-table-column>
        <el-table-column
          prop="nickName"
          label="用户昵称"
          width="150">
          <template slot-scope="scope">
            <span class="nf-missioncomplain-user-link" @click="updates(scope.row)">
              {{ scope.row.nickName ? scope.row.nickName : '未绑定' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="title"
          label="标题">
        </el-table-column>
        <el-table-column
          prop="picture"
          label="截图">
          <template slot-scope="scope">
            <div v-if="scope.row.picture == null || scope.row.picture == ''">
              暂无图片
            </div>
            <div v-else-if="scope.row.picture.includes(',')" class="nf-missioncomplain-pics">
              <div v-for="item in scope.row.picture.split(',')" :key="item" class="nf-missioncomplain-pic-item">
                <el-popover placement="top-start" title="" trigger="hover">
                  <img class="nf-missioncomplain-pic-thumb" :src="item" alt="" slot="reference">
                  <img class="nf-missioncomplain-pic-preview" :src="item" alt="">
                </el-popover>
              </div>
            </div>
            <div v-else>
              <el-popover placement="top-start" title="" trigger="hover">
                <img class="nf-missioncomplain-pic-thumb" :src="scope.row.picture" alt="" slot="reference">
                <img class="nf-missioncomplain-pic-preview" :src="scope.row.picture" alt="">
              </el-popover>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="content"
          label="投诉内容">
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="创建时间"
          width="160">
        </el-table-column>
        <el-table-column
          fixed="right"
          prop="state"
          label="状态"
          width="100">
          <template slot-scope="scope">
            <span class="nf-missioncomplain-status-pending" v-if="scope.row.state === 1">待审核</span>
            <span v-if="scope.row.state === 2">已审核</span>
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="150">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              @click="complainDetails(scope.row)">详情/处理
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="nf-missioncomplain-pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :page-sizes="[10, 20, 30, 40]"
          :page-size="limit"
          :current-page="page"
          layout="total,sizes, prev, pager, next"
          :total="tableData.totalCount">
        </el-pagination>
      </div>
    </div>

    <!-- 任务投诉 -->
    <el-dialog title="任务投诉详情" :visible.sync="dialogFormVisible" center>
      <el-form :model="form">
        <el-form-item label="投诉内容：" :label-width="formLabelWidth">
          <span>{{ form.content }}</span>
        </el-form-item>
        <el-form-item label="投诉截图：" :label-width="formLabelWidth">
          <div class="nf-missioncomplain-dialog-pics">
            <div v-for="(item, index) of imgs" :key="index" class="nf-missioncomplain-dialog-pic">
              <img :src="item" class="avatar nf-missioncomplain-dialog-img" />
            </div>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" v-if="form.state == 1">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="amendNoticeTo()">处 理</el-button>
      </div>
    </el-dialog>
    <!-- 处理任务 -->
    <el-dialog title="处理任务" :visible.sync="dialogFormVisible1" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">任务状态：</span>
        <el-radio-group v-model="state">
          <el-radio :label="2">下架</el-radio>
          <el-radio :label="3">不下架</el-radio>
        </el-radio-group>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible1 = false">取 消</el-button>
        <el-button type="primary" @click="complainTo()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      limit: 10,
      page: 1,
      userId: '',
      content: '',
      form: {
        id: '',
        content: '',
        picture: '',
        state: ''
      },
      state: '',
      imgs: [],
      formLabelWidth: '200px',
      activeName: 'first',
      tableDataLoading: false,
      dialogFormVisible: false,
      dialogFormVisible1: false,
      tableData: []
    }
  },
  methods: {
    handleSizeChange(val) {
      this.limit = val
      this.dataSelect()
    },
    handleCurrentChange(val) {
      this.page = val
      this.dataSelect()
    },
    updates(row) {
      this.$router.push({ path: '/userDetail', query: { userId: row.userId } })
    },
    deleteStair(row) {
      let delid = row.id
      this.$confirm(`确定删除此条信息?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl(`helpClassify/deleteClassifyById/?id=${delid}`),
          method: 'post',
          data: this.$http.adornData({})
        }).then(({ data }) => {
          this.$message({
            message: '删除成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.name = ''
              this.classifySelect()
            }
          })
        })
      }).catch(() => {})
    },
    select() {
      this.dataSelect()
    },
    cleans() {
      this.content = ''
      this.dataSelect()
    },
    complainDetails(row) {
      this.form.id = row.id
      this.form.content = row.content
      this.form.picture = row.picture
      this.form.state = row.state
      let imgs = this.form.picture.split(',')
      this.imgs = imgs
      this.dialogFormVisible = true
    },
    amendNoticeTo() {
      this.dialogFormVisible1 = true
    },
    complainTo() {
      if (this.state == '') {
        this.$notify({
          title: '提示',
          duration: 1800,
          message: '请选择任务状态',
          type: 'warning'
        })
        return
      }
      this.$http({
        url: this.$http.adornUrl(`/helpComplaint/solveHelpComplaint/${this.form.id}/${this.state}`),
        method: 'post',
        data: this.$http.adornData({})
      }).then(({ data }) => {
        if (data.code == 0) {
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.dialogFormVisible = false
              this.dialogFormVisible1 = false
              this.dataSelect()
            }
          })
        }
      })
    },
    dataSelect() {
      this.tableDataLoading = true
      this.$http({
        url: this.$http.adornUrl('helpComplaint/selectHelpComplaintList'),
        method: 'get',
        params: this.$http.adornParams({
          page: this.page,
          limit: this.limit,
          userId: this.userId,
          content: this.content
        })
      }).then(({ data }) => {
        this.tableDataLoading = false
        let returnData = data.data
        this.tableData = returnData
      })
    }
  },
  mounted() {
    this.dataSelect()
  }
}
</script>

<style scoped>
.nf-missioncomplain-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-missioncomplain-hero {
  margin-bottom: 12px;
}

.nf-missioncomplain-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-missioncomplain-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-missioncomplain-filter {
  margin-bottom: 12px;
}

.nf-missioncomplain-filter__body {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
}

.nf-missioncomplain-filter__item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nf-missioncomplain-filter__label {
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
  white-space: nowrap;
}

.nf-missioncomplain-filter__input {
  width: 200px;
}

.nf-missioncomplain-filter__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-left: auto;
}

.nf-missioncomplain-table-card {
  margin-bottom: 0;
}

.nf-missioncomplain-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.nf-missioncomplain-user-link {
  color: var(--nf-brand, #ffa8c2);
  cursor: pointer;
}

.nf-missioncomplain-status-pending {
  color: var(--nf-brand, #ffa8c2);
}

.nf-missioncomplain-pics {
  display: flex;
  flex-wrap: wrap;
}

.nf-missioncomplain-pic-item {
  margin: 2px;
}

.nf-missioncomplain-pic-thumb {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 4px;
}

.nf-missioncomplain-pic-preview {
  width: 200px;
  height: 200px;
  object-fit: cover;
}

.nf-missioncomplain-dialog-pics {
  display: flex;
  flex-wrap: wrap;
}

.nf-missioncomplain-dialog-pic {
  margin-left: 10px;
  margin-top: 10px;
}

.nf-missioncomplain-dialog-img {
  width: 148px;
  height: 148px;
  object-fit: cover;
  border-radius: 4px;
}
</style>
