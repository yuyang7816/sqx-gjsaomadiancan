<template>
  <div class="nf-page nf-topupmoney-page">
    <div class="nf-page-hero nf-topupmoney-hero">
      <span class="nf-page-badge">充值</span>
      <h2 class="nf-topupmoney-hero__title">充值配置</h2>
      <p class="nf-topupmoney-hero__desc">管理用户充值可选金额档位，支持添加、编辑与删除配置</p>
    </div>

    <div class="nf-filter-card nf-topupmoney-toolbar">
      <div class="nf-filter-card__head">
        <i class="el-icon-setting"></i>
        <span>快捷操作</span>
      </div>
      <div class="nf-filter-card__body nf-topupmoney-toolbar__body">
        <div class="nf-topupmoney-toolbar__actions">
          <el-button size="mini" type="primary" icon="document" @click="refresh">刷新</el-button>
          <el-button size="mini" type="primary" :disabled="!isAuth('topupmoney:add')" icon="document"
            @click="classifyStair()">添加</el-button>
        </div>
      </div>
    </div>

    <div class="nf-table-card nf-topupmoney-table-card">
      <el-table v-loading="tableDataLoading" :data="tableData">
        <el-table-column prop="id" label="编号" width="80">
        </el-table-column>
        <el-table-column prop="money" label="金额">
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" :disabled="!isAuth('topupmoney:update')"
              @click="compile(scope.$index, scope.row)">编辑
            </el-button>
            <el-button size="mini" type="danger" :disabled="!isAuth('topupmoney:delete')" @click="deleteStair(scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 添加优惠券 -->
    <el-dialog title="添加" :visible.sync="dialogFormVisible" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">充值金额：</span>
        <el-input style="width:50%;" v-model="money" type="number" min="0" placeholder="请输入充值金额"></el-input>
      </div>
      <!-- <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
        <div
          style=" width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;display: inline-block;">
          <el-upload class="avatar-uploader" v-model="couponPicture"
            :action="$http.adornUrlMsg('alioss/upload')" :show-file-list="false"
            :on-success="handleAvatarSuccess1">
            <img v-if="couponPicture" :src="couponPicture" class="avatar" style="width: 148px;height: 148px;" />
            <i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
          </el-upload>
        </div>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">优惠券金额：</span>
        <el-input style="width:50%;" v-model="money" type="text" placeholder="请输入优惠券金额"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">最低消费：</span>
        <el-input style="width:50%;" v-model="minMoney" type="text" placeholder="请输入最低消费"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">有效天数：</span>
        <el-input style="width:50%;" v-model="endDate" type="text" placeholder="请输入有效天数"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">兑换积分：</span>
        <el-input style="width:50%;" v-model="needIntegral" type="text" placeholder="请输入兑换积分"></el-input>
      </div> -->

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="StairNoticeTo()">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 修改一级分类 -->
    <el-dialog title="修改" :visible.sync="dialogFormVisible1" center>
      <el-form :model="form">
        <el-form-item label="充值金额：" :label-width="formLabelWidth">
          <el-input v-model="form.money" style="width:65%;"></el-input>
        </el-form-item>
        <!-- <el-form-item label="图片：" :label-width="formLabelWidth">
          <div
            style=" width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;display: inline-block;">
            <el-upload class="avatar-uploader" v-model="form.couponPicture"
              :action="$http.adornUrlMsg('alioss/upload')" :show-file-list="false"
              :on-success="handleAvatarSuccess2">
              <img v-if="form.couponPicture" :src="form.couponPicture" class="avatar" style="width: 148px;height: 148px;" />
              <i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
            </el-upload>
          </div>
        </el-form-item>
        <el-form-item label="优惠券金额：" :label-width="formLabelWidth">
          <el-input v-model="form.money" style="width:65%;"></el-input>
        </el-form-item>
        <el-form-item label="最低消费：" :label-width="formLabelWidth">
          <el-input v-model="form.minMoney" style="width:65%;"></el-input>
        </el-form-item>
        <el-form-item label="有效天数：" :label-width="formLabelWidth">
          <el-input v-model="form.endDate" style="width:65%;"></el-input>
        </el-form-item>
        <el-form-item label="兑换积分：" :label-width="formLabelWidth">
          <el-input v-model="form.needIntegral" style="width:65%;"></el-input>
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible1 = false">取 消</el-button>
        <el-button type="primary" @click="CompileNoticeTo()">确 定</el-button>
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
        checkBoxData: [], //多选框选择的值
        formLabelWidth: '200px',
        tableDataLoading: false,
        dialogFormVisible: false,
        dialogFormVisible1: false,
        tableData: [],
        couponName: '',
        couponPicture: '',
        endDate:'',
        minMoney:'',
        money:'',
        needIntegral:'',
        form: {
          id: '',
          couponName: '',
          couponPicture: '',
          endDate:'',
          minMoney:'',
          money:'',
          needIntegral:''
        }
      }
    },
    methods: {
      // 多选
      changeFun(val) {
        this.checkBoxData = val;
      },
      handleSizeChange(val) {
        this.limit = val;
        this.dataSelect()
      },
      handleCurrentChange(val) {
        this.page = val;
        this.dataSelect()
      },
      handleAvatarSuccess1(file, fileList) {
        this.couponPicture = file.data
      },
      handleAvatarSuccess2(file, fileList) {
        this.form.couponPicture = file.data
      },
      // 刷新
      refresh() {
        this.dataSelect()
      },
      // 添加优惠券弹框
      classifyStair() {
        this.couponName = ''
        this.couponPicture = ''
        this.endDate = ''
        this.minMoney = ''
        this.money = ''
        this.needIntegral = ''
        this.dialogFormVisible = true
      },
      // 添加优惠券
      StairNoticeTo() {
        if (this.money == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入金额',
            type: 'warning'
          });
          return
        }
        this.$http({
          url: this.$http.adornUrl('admin/topupmoney/insertTopUpMoney'),
          method: 'post',
          data: this.$http.adornData({
            'money':this.money,
          })
        }).then(({
          data
        }) => {
          if(data.code==0){
            this.dialogFormVisible = false
            this.$message({
              message: '添加成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.dataSelect()
              }
            })
          }else{

          }this.$message({
              message: data.msg,
              type: 'warning',
              duration: 1500,
              onClose: () => {
              }
            })

        })
      },
      // 修改优惠券
      compile(index, rows) {
        this.form.id = rows.id
        this.form.money = rows.money
        this.dialogFormVisible1 = true;
      },
      // 修改优惠券
      CompileNoticeTo() {
        this.$http({
          url: this.$http.adornUrl('admin/topupmoney/updateTopUpMoney'),
          method: 'post',
          data: this.$http.adornData({
            'id': this.form.id,
            'money':this.form.money
          })
        }).then(({
          data
        }) => {
          if (data.code == 0) {
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.dialogFormVisible1 = false
                this.dataSelect()
              }
            })
          } else {
            this.$message({
              message: data.msg,
              type: 'error',
              duration: 1500,
              onClose: () => {
                this.dialogFormVisible1 = false
                this.dataSelect()
              }
            })
          }
        })
      },

      //删除
      deleteStair(row) {
        this.$confirm(`确定删除此条信息?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('admin/topupmoney/deleteTopUpMoney'),
            method: 'get',
            params: this.$http.adornParams({
              'id':row.id
            })
          }).then(({
            data
          }) => {
            if (data.code == 0) {
              this.$message({
                message: '删除成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.dataSelect()
                }
              })
            } else {
              this.$message({
                message: data.msg,
                type: 'error',
                duration: 1500,
                onClose: () => {
                  this.dataSelect()
                }
              })
            }

          })
        }).catch(() => {})
      },
      // 重置
      cleans() {
        this.phone = ''
        this.status = 0
        this.dataSelect()
      },
      // 获取派单数据列表
      dataSelect() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('admin/topupmoney/selectTopUpMoney'),
          method: 'get',
          params: this.$http.adornParams({})
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data;
          this.tableData = returnData
        })
      }
    },
    mounted() {
      this.dataSelect()
    }
  };
</script>

<style scoped>
.nf-topupmoney-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-topupmoney-hero {
  margin-bottom: 12px;
}

.nf-topupmoney-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-topupmoney-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-topupmoney-toolbar {
  margin-bottom: 12px;
}

.nf-topupmoney-toolbar__body {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
}

.nf-topupmoney-toolbar__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-left: auto;
}

.nf-topupmoney-table-card {
  margin-bottom: 0;
}
</style>
