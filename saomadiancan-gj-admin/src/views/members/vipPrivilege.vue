<template>
  <div class="nf-page nf-vipprivilege-page">
    <div class="nf-page-hero nf-vipprivilege-hero">
      <span class="nf-page-badge">会员</span>
      <h2 class="nf-vipprivilege-hero__title">会员特权</h2>
      <p class="nf-vipprivilege-hero__desc">管理会员特权展示内容，支持按语言版本筛选，可添加、修改与删除特权项</p>
    </div>

    <div class="nf-filter-card nf-vipprivilege-filter">
      <div class="nf-filter-card__head">
        <i class="el-icon-search"></i>
        <span>筛选条件</span>
      </div>
      <div class="nf-filter-card__body nf-vipprivilege-filter__body">
        <div class="nf-vipprivilege-filter__item">
          <span class="nf-vipprivilege-filter__label">语言版本：</span>
          <el-select v-model="languageTypeT" class="nf-vipprivilege-filter__select" @change="animeDat()">
            <el-option v-for="item in statesnum" :key="item.language" :label="item.remark" :value="item.language">
            </el-option>
          </el-select>
        </div>
        <div class="nf-vipprivilege-filter__actions">
          <el-button size="mini" type="primary" icon="document" :disabled="!isAuth('bannerList:add')" @click="addNotice">添加会员特权</el-button>
        </div>
      </div>
    </div>

    <div class="nf-vipprivilege-tabs-wrap">
      <el-tabs v-model="activeName" class="nf-vipprivilege-tabs">
        <el-tab-pane label="会员特权列表" name="first">
          <div class="nf-table-card nf-vipprivilege-table-card">
            <el-table v-loading="tableDataLoading" :data="tableData.list">
              <el-table-column fixed prop="memberId" label="编号" width="50">
              </el-table-column>
              <el-table-column prop="languageName" label="语言">
              </el-table-column>
              <el-table-column prop="memberImg" label="图片">
                <template slot-scope="scope">
                  <img :src="scope.row.memberImg" class="nf-vipprivilege-thumb" />
                </template>
              </el-table-column>
              <el-table-column prop="memberName" label="特权名称">
              </el-table-column>
              <el-table-column prop="memberDescribe" label="描述">
              </el-table-column>
              <el-table-column prop="sort" label="排序">
              </el-table-column>
              <el-table-column label="操作" width="180">
                <template slot-scope="scope">
                  <el-button size="mini" type="primary" :disabled="!isAuth('bannerList:update')"
                    @click="amendBanner(scope.$index, scope.row)">修改
                  </el-button>
                  <el-button size="mini" type="danger" :disabled="!isAuth('bannerList:delete')" @click="deletes(scope.row)">删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="nf-vipprivilege-pagination">
              <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
                layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
              </el-pagination>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <el-dialog :title="title" :visible.sync="dialogFormVisible" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">语言版本：</span>
        <el-radio-group v-model="languageType">
          <el-radio v-for="(item,index) in statesnum" :key="item.language" :label="item.language"
            style="margin: 5px;">{{item.remark}}</el-radio>
        </el-radio-group>
      </div>
      <div style="margin-bottom: 10px;display: flex;">
        <span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
        <div
          style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
          <el-upload class="avatar-uploader" v-model="memberImg"
            action="https://xxpwadmin.xianmxkj.com/sqx_fast/alioss/upload" :show-file-list="false"
            :on-success="handleAvatarSuccess">
            <img v-if="memberImg" :src="memberImg" class="avatar"
              style="border-radius: 6px;width: 148px;height: 148px;" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
        <el-input style="width:50%;" v-model="memberName" placeholder="请输入名称"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">描述：</span>
        <el-input style="width:50%;" v-model="memberDescribe" placeholder="请输入描述"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
        <el-input style="width:50%;" v-model="sort" placeholder="请输入排序"
          onkeyup="value=value.replace(/^(0+)|[^\d]+/g,'')"></el-input>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addNoticeTo()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        page: 1,
        limit: 10,
        tableDataLoading: true,
        dialogFormVisible: false,
        tableData: [],
        memberName: '',
        memberDescribe:'',
        memberImg: '',
        memberId: '',
        sort: '',
        title: '添加',
        languageTypeT: '',
        statesnum: [{
          classificationName: '全部',
          classificationId: ''
        }],
        languageType: '',
        activeName: 'first',
      }
    },
    methods: {
      // 获取数据列表
      dataSelect() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('member/selectMemberPage'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit,
            'languageType': this.languageTypeT,
          })
        }).then(({
          data
        }) => {
          if (data && data.code === 0) {
            console.log('会员列表', data)
            this.tableDataLoading = false
            let returnData = data.data;
            this.tableData = returnData;
          }

        })
      },
      //上传成功
      handleAvatarSuccess(file) {
        this.memberImg = file.data
      },
      addNotice() {
        this.memberImg = ''
        this.memberId = ''
        this.memberName = ''
        this.memberDescribe = ''
        this.sort = ''
        this.languageType = ''
        this.title = '添加'
        this.dialogFormVisible = true
      },
      handleCurrentChange(val) {
        this.page = val
        this.dataSelect()
      },
      handleSizeChange(val) {
        this.limit = val
        this.dataSelect()
      },
      // 添加banner图
      addNoticeTo() {
        if (this.languageType == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请选择语言版本',
            type: 'warning'
          })
          return
        }
        if (this.memberImg == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请上传图片',
            type: 'warning'
          });
          return
        }
        if (this.memberName == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入名称',
            type: 'warning'
          });
          return
        }
        if (this.memberDescribe == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入描述',
            type: 'warning'
          });
          return
        }

        this.classify = 1
        if (this.title == '添加') {
          var url = 'member/insertMember'
        } else {
          var url = 'member/updateMember'
        }
        this.$http({
          url: this.$http.adornUrl(url),
          method: 'post',
          data: this.$http.adornData({
            'memberImg': this.memberImg,
            'memberId': this.memberId,
            'memberName': this.memberName,
            'memberDescribe':this.memberDescribe,
            'sort': this.sort,
            'languageType': this.languageType
          })
        }).then(({
          data
        }) => {
          if(data.code==0){
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.dialogFormVisible = false
                this.dataSelect()
              }
            })
          }else{
            this.$message({
              message: data.mdg,
              type: 'warning',
              duration: 1500,
              onClose: () => {
              }
            })
          }

        })
      },
      // 修改
      amendBanner(index, rows, a) {
        if (a == 1) {
          this.pl = 1
        } else {
          this.pl = 0
        }
        this.dialogFormVisible = true
        this.id = rows.id
        this.memberImg = rows.memberImg
        this.memberId = rows.memberId
        this.sort = rows.sort
        this.memberName = rows.memberName
        this.memberDescribe = rows.memberDescribe
        this.languageType = rows.languageType
        this.title = '修改'
      },
      // 修改
      amendNoticeTo() {
        this.$http({
          url: this.$http.adornUrl(
            `?memberId=${this.memberId}&memberImg=${this.memberImg}&sort=${this.sort}&memberName=${this.memberName}`
            ),
          method: 'post',
          data: this.$http.adornData({})
        }).then(({
          data
        }) => {
          this.dialogFormVisible = false
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.dataSelect()
            }
          })
        })
      },
      //删除一级
      deletes(row) {
        let delid = row.memberId
        this.$confirm(`确定删除此条信息?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl(`member/deleteMemberById/?memberId=${delid}`),
            method: 'post',
            data: this.$http.adornData({})
          }).then(({
            data
          }) => {
            if(data.code==0){
              this.$message({
                message: '删除成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.dataSelect()
                }
              })
            } else{
              this.$message({
                message: data.mdg,
                type: 'warning',
                duration: 1500,
                onClose: () => {
                }
              })
            }
          })
        }).catch(() => {})
      },
      animeDat() {
        this.page = 1
        this.dataSelect()
      },

      // 语言类型
      dataSelectYy() {
        this.$http({
          url: this.$http.adornUrl('admin/languageType/getLanguageTypeList'),
          method: 'get',
          params: this.$http.adornParams({})
        }).then(({
          data
        }) => {
          if (data && data.code === 0) {
            let returnData = data.data
            this.statesnum = returnData.records
          }
        })
      }
    },
    mounted() {
      this.dataSelect()
      this.dataSelectYy()
    }
  }
</script>

<style scoped>
.nf-vipprivilege-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-vipprivilege-hero {
  margin-bottom: 12px;
}

.nf-vipprivilege-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-vipprivilege-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-vipprivilege-filter {
  margin-bottom: 12px;
}

.nf-vipprivilege-filter__body {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
}

.nf-vipprivilege-filter__item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nf-vipprivilege-filter__label {
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
  white-space: nowrap;
}

.nf-vipprivilege-filter__select {
  width: 150px;
}

.nf-vipprivilege-filter__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-left: auto;
}

.nf-vipprivilege-tabs-wrap {
  margin-bottom: 0;
}

.nf-vipprivilege-tabs {
  margin-bottom: 0;
}

.nf-vipprivilege-tabs >>> .el-tabs__header {
  margin-bottom: 0;
  background: #fff;
  border-radius: 8px 8px 0 0;
  padding: 0 16px;
  border: 1px solid var(--nf-border, #e8eaed);
  border-bottom: none;
}

.nf-vipprivilege-table-card {
  margin-bottom: 0;
  border-radius: 0 0 8px 8px;
  border-top: none;
}

.nf-vipprivilege-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.nf-vipprivilege-thumb {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
}
</style>
