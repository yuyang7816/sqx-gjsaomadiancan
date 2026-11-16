<template>
  <div class="nf-page nf-message-page">
    <div class="nf-page-hero nf-message-hero">
      <span class="nf-page-badge">消息</span>
      <h2 class="nf-message-hero__title">消息中心</h2>
      <p class="nf-message-hero__desc">查看系统推送与用户消息记录，支持点击用户名称跳转用户详情</p>
    </div>

    <div class="nf-filter-card nf-message-filter">
      <div class="nf-filter-card__head">
        <i class="el-icon-search"></i>
        <span>筛选条件</span>
      </div>
      <div class="nf-filter-card__body nf-message-filter__body">
        <div class="nf-message-filter__actions">
          <el-button size="mini" type="primary" icon="document" @click="animeDat">查询</el-button>
          <el-button size="mini" type="primary" icon="document" @click="cleans">重置</el-button>
          <!-- <el-button style='margin: 10px 0;' :disabled="!isAuth('message:push')" size="mini" type="primary"
            icon="document" @click="magNotice">发布消息</el-button> -->
        </div>
      </div>
    </div>

    <div class="nf-message-tabs-wrap">
      <el-tabs v-model="activeName" @tab-click="handleClick" class="nf-message-tabs">
        <!-- <el-tab-pane label="用户反馈" name="first">
          ...
        </el-tab-pane>
        <el-tab-pane label="用户消息" name="fourth">
          ...
        </el-tab-pane> -->
        <el-tab-pane label="消息中心" name="seventh">
          <div class="nf-table-card nf-message-table-card">
            <el-table v-loading="tableDataLoading" :data="tableData.list">
              <el-table-column fixed prop="id" label="编号" width="80">
              </el-table-column>
              <el-table-column prop="userName" label="用户名称">
                <template slot-scope="scope">
                  <span
                    v-if="scope.row.userName"
                    class="nf-message-user-link"
                    @click="updataDetails(scope.row)">{{ scope.row.userName }}</span>
                  <span v-else>未绑定</span>
                </template>
              </el-table-column>
              <el-table-column prop="title" label="消息标题">
              </el-table-column>
              <el-table-column prop="content" label="消息内容" width="280">
              </el-table-column>
              <el-table-column prop="createAt" label="创建时间">
              </el-table-column>
              <el-table-column label="操作" width="120">
                <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="updataDetails(scope.row)">用户详情
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="nf-message-pagination">
              <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :page-sizes="[10, 20, 30,50,100]" :page-size="limit" :current-page="page" layout="total,sizes, prev, pager, next"
                :total="tableData.totalCount">
              </el-pagination>
            </div>
          </div>
          <!-- 发布消息 -->
          <el-dialog title="发布消息" :visible.sync="dialogFormVisible2" center>
            <div style="margin-bottom: 10px;">
              <span style="width: 200px;display: inline-block;text-align: right;">选择：</span>
              <el-select v-model="flag" placeholder="请选择类型" style="width:50%">
                <el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>&nbsp;&nbsp;&nbsp;&nbsp;
            </div>
            <div style="margin-bottom: 10px;" v-if="flag == 1">
              <span style="width: 200px;display: inline-block;text-align: right;">用户手机号：</span>
              <el-input style="width: 50%;" v-model="phone" placeholder="请输入用户手机号"></el-input>
            </div>
            <div style="margin-bottom: 10px;">
              <span style="width: 200px;display: inline-block;text-align: right;">消息标题：</span>
              <el-input style="width: 50%;" v-model="title" placeholder="请输入消息标题"></el-input>
            </div>
            <div style="margin-bottom: 10px;">
              <span style="width: 200px;display: inline-block;text-align: right;position: relative;top: -65px;">消息内容：</span>
              <el-input style="width: 50%;" type="textarea" rows="4" v-model="content" placeholder="请输入消息内容"></el-input>
            </div>
            <div slot="footer" class="dialog-footer">
              <el-button @click="dialogFormVisible2 = false">取 消</el-button>
              <el-button type="primary" @click="magNoticeTo()">确 定</el-button>
            </div>
          </el-dialog>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        limit: 10,
        page: 1,
        state: 2,
        url: '',
        title: '',
        type: '',
        id: '',
        types: [{
            value: 'word',
            label: '文字'
          },
          {
            value: 'url',
            label: '链接'
          }
        ],
        form: {
          id: '',
          title: '',
          url: '',
          type: '',
          state: '',
          types: [{
              value: 'word',
              label: '文字'
            },
            {
              value: 'url',
              label: '链接'
            }
          ],
        },
        phone: '',
        content: '',
        flag: '',
        flags: [{
            value: 1,
            label: '单个用户'
          },
          {
            value: 2,
            label: '全部用户'
          }
        ],
        formLabelWidth: '200px',
        activeName: 'seventh',
        tableDataLoading: false,
        dialogFormVisible1: false,
        dialogFormVisible2: false,
        dialogFormVisible: false,
        tableData: [],
      }
    },
    methods: {
      // 详情跳转
      updataDetails(row) {
        this.$router.push({
          path: '/userDetail',
          query: {
            userId: row.userId
          }
        });
      },
      handleSizeChange(val) {
        this.limit = val;
        this.dataSelect()
      },
      handleCurrentChange(val) {
        this.page = val;
        this.dataSelect()
      },
      handleClick(tab, event) {
        if (tab._props.label == '用户反馈') {
          this.page = 1
          this.limit = 10
          this.state = 2
          this.dataSelect()
        }
        if (tab._props.label == '用户消息') {
          this.page = 1
          this.limit = 10
          this.state = 5
          this.dataSelect()
        }
        if (tab._props.label == '消息中心') {
          this.page = 1
          this.limit = 10
          this.state = 8
          this.dataSelect()
        }
      },
      animeDat() {
        this.page = 1
        this.dataSelect()
      },
      cleans() {
        this.page = 1
        this.limit = 10
        this.dataSelect()
      },
      // 消息推送
      magNotice() {
        this.dialogFormVisible2 = true
      },
      // 消息推送
      magNoticeTo() {
        if (this.flag == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请选择类型',
            type: 'warning'
          });
          return
        }
        if (this.title == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入标题',
            type: 'warning'
          });
          return
        }
        if (this.content == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入内容',
            type: 'warning'
          });
          return
        }
        if (this.flag == 1) {
          if (this.phone == '') {
            this.$notify({
              title: '提示',
              duration: 1800,
              message: '请输入用户手机号',
              type: 'warning'
            });
            return
          }
          this.$http({
            url: this.$http.adornUrl('/cash/sendMsg'),
            method: 'get',
            params: this.$http.adornParams({
              'title': this.title,
              'content': this.content,
              'phone': this.phone,
              'flag': this.flag
            })
          }).then(({
            data
          }) => {
            this.dialogFormVisible2 = false
            if (data.code == -100) {
              this.$message({
                message: data.msg,
                type: 'error',
                duration: 1500,
                onClose: () => {
                  this.title = ''
                  this.content = ''
                  this.phone = ''
                  this.flag = ''
                  this.dataSelect()
                }
              })
            }
            if (data.code == 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.title = ''
                  this.content = ''
                  this.phone = ''
                  this.flag = ''
                  this.dataSelect()
                }
              })
            }
          })
        }
        if (this.flag == 2) {
          this.$http({
            url: this.$http.adornUrl('/cash/sendMsg'),
            method: 'get',
            params: this.$http.adornParams({
              'title': this.title,
              'content': this.content,
              'flag': this.flag,
            })
          }).then(({
            data
          }) => {
            this.dialogFormVisible2 = false
            if (data.code == 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.title = ''
                  this.content = ''
                  this.flag = ''
                  this.dataSelect()
                }
              })
            }
          })
        }
      },
      // 获取数据列表
      dataSelect() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('message/findMessageList'),
          method: 'get',
          params: this.$http.adornParams({
            'page':this.page,
            'limit':this.limit
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data;
          this.tableData = returnData
        })
      },
    },
    mounted() {
      this.dataSelect()
    }
  };
</script>

<style scoped>
.nf-message-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-message-hero {
  margin-bottom: 12px;
}

.nf-message-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-message-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-message-filter {
  margin-bottom: 12px;
}

.nf-message-filter__body {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
}

.nf-message-filter__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-left: auto;
}

.nf-message-tabs-wrap {
  margin-bottom: 0;
}

.nf-message-tabs {
  margin-bottom: 0;
}

.nf-message-tabs >>> .el-tabs__header {
  margin-bottom: 0;
  background: #fff;
  border-radius: 8px 8px 0 0;
  padding: 0 16px;
  border: 1px solid var(--nf-border, #e8eaed);
  border-bottom: none;
}

.nf-message-table-card {
  margin-bottom: 0;
  border-radius: 0 0 8px 8px;
  border-top: none;
}

.nf-message-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.nf-message-user-link {
  color: var(--nf-brand, #ffa8c2);
  cursor: pointer;
}
</style>
