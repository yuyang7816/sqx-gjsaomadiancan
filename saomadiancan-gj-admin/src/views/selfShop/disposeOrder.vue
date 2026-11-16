<template>
  <div class="nf-page nf-disposeorder-page">
    <div class="nf-page-hero nf-disposeorder-hero">
      <span class="nf-page-badge">待处理订单</span>
      <h2 class="nf-disposeorder-hero__title">订单处理中心</h2>
      <p class="nf-disposeorder-hero__desc">管理待发货与待退款订单，支持按订单号筛选查询、发货与退款操作</p>
    </div>

    <div class="nf-filter-card nf-disposeorder-filter">
      <div class="nf-filter-card__head">
        <i class="el-icon-search"></i>
        <span>筛选条件</span>
      </div>
      <div class="nf-filter-card__body nf-disposeorder-filter__body">
        <div class="nf-disposeorder-filter__item">
          <span class="nf-disposeorder-filter__label">订单号：</span>
          <el-input
            class="nf-disposeorder-filter__input"
            @keydown.enter.native="select"
            clearable
            placeholder="请输入订单号"
            v-model="orderNum">
          </el-input>
        </div>
        <div class="nf-disposeorder-filter__actions">
          <el-button size="mini" type="primary" icon="document" @click="select">查询</el-button>
          <el-button size="mini" type="primary" icon="document" @click="cleans">重置</el-button>
        </div>
      </div>
    </div>

    <div class="nf-disposeorder-tabs-wrap">
      <el-tabs v-model="activeName" @tab-click="handleClick" class="nf-disposeorder-tabs">
        <el-tab-pane label="待发货订单" name="first">
          <div class="nf-table-card nf-disposeorder-table-card">
            <el-table v-loading="tableDataLoading" :data="tableData">
              <el-table-column fixed prop="img" label="商品图片">
                <template slot-scope="scope">
                  <img :src="scope.row.img" alt="" class="nf-disposeorder-thumb">
                </template>
              </el-table-column>
              <el-table-column prop="title" label="商品标题" width="200">
              </el-table-column>
              <el-table-column prop="goodsType.name" label="商品类型">
              </el-table-column>
              <el-table-column prop="price" label="商品价格">
                <template slot-scope="scope">
                  <span class="nf-disposeorder-price">{{ scope.row.price | numFilter }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="number" label="商品个数">
              </el-table-column>
              <el-table-column prop="descrition" label="订单备注">
                <template slot-scope="scope">
                  <span>{{ scope.row.descrition ? scope.row.descrition : '未填写' }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="orderNum" label="订单号" width="200">
                <template slot-scope="scope">
                  <span>{{ scope.row.orderNum }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="consignee" label="收货人" width="120">
              </el-table-column>
              <el-table-column prop="mobile" label="手机号" width="120">
              </el-table-column>
              <el-table-column prop="detail" label="详细地址" width="120">
                <template slot-scope="scope">
                  <span>{{ scope.row.provinces }}{{ scope.row.detail }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="payMoney" label="支付金额" width="120">
                <template slot-scope="scope">
                  <span class="nf-disposeorder-price nf-disposeorder-price--pointer">{{ scope.row.payMoney | numFilter }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="commissionPrice" label="佣金" width="120">
                <template slot-scope="scope">
                  <span class="nf-disposeorder-price">{{ scope.row.commissionPrice | numFilter }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="payWay" label="支付方式">
                <template slot-scope="scope">
                  <span class="nf-disposeorder-brand-text" v-if="scope.row.payWay === 1">微信</span>
                  <span class="nf-disposeorder-brand-text" v-if="scope.row.payWay === 2">支付宝</span>
                  <span class="nf-disposeorder-brand-text" v-if="scope.row.payWay === 3">零钱</span>
                  <span class="nf-disposeorder-brand-text" v-if="scope.row.payWay === null">未支付</span>
                </template>
              </el-table-column>
              <el-table-column fixed="right" prop="status" label="订单状态">
                <template slot-scope="scope">
                  <span class="nf-disposeorder-brand-text" v-if="scope.row.status === 2">待发货</span>
                </template>
              </el-table-column>
              <el-table-column prop="createAt" label="创建时间" width="160">
              </el-table-column>
              <el-table-column prop="payTime" label="付款时间" width="160">
                <template slot-scope="scope">
                  <span>{{ scope.row.payTime ? scope.row.payTime : '未付款' }}</span>
                </template>
              </el-table-column>
              <el-table-column fixed="right" label="操作" width="150">
                <template slot-scope="scope">
                  <el-button size="mini" type="primary" :disabled="!isAuth('orderAdmin:deliver')" @click="colonelClick(scope.row)">发货</el-button>
                  <el-button size="mini" type="primary" @click="orderDetails(scope.row.id)">详情</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="nf-disposeorder-pagination">
              <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :page-sizes="[10, 20, 30, 50, 100]"
                :page-size="size"
                :current-page="page"
                layout="total,sizes, prev, pager, next,jumper"
                :total="totalnum">
              </el-pagination>
            </div>
          </div>
          <!-- 发货弹框 -->
          <el-dialog title="发货" :visible.sync="dialogFormVisible" center>
            <div class="nf-disposeorder-dialog-row">
              <span class="nf-disposeorder-dialog-label">快递公司：</span>
              <el-input class="nf-disposeorder-dialog-input" v-model="expressName" placeholder="请输入快递公司"></el-input>
            </div>
            <div class="nf-disposeorder-dialog-row">
              <span class="nf-disposeorder-dialog-label">快递单号：</span>
              <el-input class="nf-disposeorder-dialog-input" v-model="expressNumber" placeholder="请输入快递单号"></el-input>
            </div>
            <div slot="footer" class="dialog-footer">
              <el-button @click="dialogFormVisible = false">取 消</el-button>
              <el-button type="primary" @click="deliverNoticeTo()">确 定</el-button>
            </div>
          </el-dialog>
        </el-tab-pane>
        <el-tab-pane label="待退款订单" name="second">
          <div class="nf-table-card nf-disposeorder-table-card">
            <el-table v-loading="tableDataLoading" :data="tableData">
              <el-table-column fixed prop="img" label="商品图片">
                <template slot-scope="scope">
                  <img :src="scope.row.img" alt="" class="nf-disposeorder-thumb">
                </template>
              </el-table-column>
              <el-table-column prop="title" label="商品标题" width="200">
              </el-table-column>
              <el-table-column prop="goodsType.name" label="商品类型">
              </el-table-column>
              <el-table-column prop="price" label="商品价格">
                <template slot-scope="scope">
                  <span class="nf-disposeorder-price">{{ scope.row.price | numFilter }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="number" label="商品个数">
              </el-table-column>
              <el-table-column prop="descrition" label="订单备注">
                <template slot-scope="scope">
                  <span>{{ scope.row.descrition ? scope.row.descrition : '未填写' }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="orderNum" label="订单号" width="200">
                <template slot-scope="scope">
                  <span>{{ scope.row.orderNum }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="consignee" label="收货人" width="120">
              </el-table-column>
              <el-table-column prop="mobile" label="手机号" width="120">
              </el-table-column>
              <el-table-column prop="detail" label="详细地址" width="120">
                <template slot-scope="scope">
                  <span>{{ scope.row.provinces }}{{ scope.row.detail }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="payMoney" label="支付金额" width="120">
                <template slot-scope="scope">
                  <span class="nf-disposeorder-price nf-disposeorder-price--pointer">{{ scope.row.payMoney | numFilter }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="commissionPrice" label="佣金" width="120">
                <template slot-scope="scope">
                  <span class="nf-disposeorder-price">{{ scope.row.commissionPrice | numFilter }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="payWay" label="支付方式">
                <template slot-scope="scope">
                  <span class="nf-disposeorder-brand-text" v-if="scope.row.payWay === 1">微信</span>
                  <span class="nf-disposeorder-brand-text" v-if="scope.row.payWay === 2">支付宝</span>
                  <span class="nf-disposeorder-brand-text" v-if="scope.row.payWay === 3">零钱</span>
                  <span class="nf-disposeorder-brand-text" v-if="scope.row.payWay === null">未支付</span>
                </template>
              </el-table-column>
              <el-table-column fixed="right" prop="status" label="订单状态">
                <template slot-scope="scope">
                  <span class="nf-disposeorder-brand-text" v-if="scope.row.status === 1">待付款</span>
                  <span class="nf-disposeorder-brand-text" v-if="scope.row.status === 2">待发货</span>
                  <span class="nf-disposeorder-brand-text" v-if="scope.row.status === 3">已发货</span>
                  <span class="nf-disposeorder-brand-text" v-if="scope.row.status === 4">已收货</span>
                  <span class="nf-disposeorder-brand-text" v-if="scope.row.status === 5">已取消</span>
                  <span class="nf-disposeorder-brand-text" v-if="scope.row.status === 6">退款中</span>
                  <span class="nf-disposeorder-brand-text" v-if="scope.row.status === 7">已退款</span>
                </template>
              </el-table-column>
              <el-table-column prop="createAt" label="创建时间" width="160">
              </el-table-column>
              <el-table-column prop="payTime" label="付款时间" width="160">
                <template slot-scope="scope">
                  <span>{{ scope.row.payTime ? scope.row.payTime : '未付款' }}</span>
                </template>
              </el-table-column>
              <el-table-column fixed="right" label="操作" width="280">
                <template slot-scope="scope">
                  <el-button size="mini" type="primary" :disabled="!isAuth('orderAdmin:refund')" @click="refundClick(scope.row)">确定退款</el-button>
                  <el-button size="mini" type="primary" @click="refuseClick(scope.row)">拒绝退款</el-button>
                  <el-button size="mini" type="primary" @click="orderDetails(scope.row.id)">详情</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="nf-disposeorder-pagination">
              <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :page-sizes="[10, 20, 30, 50, 100]"
                :page-size="size"
                :current-page="page"
                layout="total,sizes, prev, pager, next,jumper"
                :total="totalnum">
              </el-pagination>
            </div>
          </div>
          <!-- 拒绝退款 -->
          <el-dialog title="拒绝退款" :visible.sync="dialogFormVisible1" center>
            <div class="nf-disposeorder-dialog-row nf-disposeorder-dialog-row--textarea">
              <span class="nf-disposeorder-dialog-label">拒绝退款理由：</span>
              <el-input class="nf-disposeorder-dialog-input" type="textarea" rows="4" v-model="refusedRefund" placeholder="请输入拒绝退款理由"></el-input>
            </div>
            <div slot="footer" class="dialog-footer">
              <el-button @click="dialogFormVisible1 = false">取 消</el-button>
              <el-button type="primary" @click="refuseNoticeTo()">确 定</el-button>
            </div>
          </el-dialog>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>
<script>
	export default {
    data () {
      return {
        size: 10,
        page: 1,
        id: '',
        orderNum: '',
        status: 2,
        sumincome: '',
        totalnum: 0,
        income: '',
        wxincome: '',
        zfbincome: '',
        yueicome: '',
        ordersum0: '',
        ordersum1: '',
        ordersum2: '',
        expressName: '',
        expressNumber: '',
        refusedRefund: '',
        isExpress: '',
        formLabelWidth: '200px',
        activeName: 'first',
        tableDataLoading: false,
        dialogFormVisible: false,
        dialogFormVisible1: false,
        tableData: [],
      }
    },
    filters: {
      numFilter (value) {
        let realVal = ''
        if (!isNaN(value) && value !== '') {
          // 截取当前数据到小数点后两位
          realVal = parseFloat(value).toFixed(2)
        } else {
          realVal = '--'
        }
        return realVal
      }
    },
    methods: {
      // 发货
      colonelClick (rows) {
        if (rows.isExpress == 2) {
          this.$http({
            url: this.$http.adornUrl('orders/expressVirtual'),
            method: 'get',
            params: this.$http.adornParams({
              'id': rows.id,
            })
          }).then(({data}) => {
            if (data.status == 0) {
              this.dialogFormVisible = false
              let returnData = data.data
              this.$message({
                message: '操作成功',
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
        } else {
          this.dialogFormVisible = true
          this.id = rows.id
        }
      },
      deliverNoticeTo () {
        if (this.expressName == '') {
          this.$message({
            message: '请输入快递公司',
            type: 'error',
            duration: 1500
          })
          return
        }
        if (this.expressNumber == '') {
          this.$message({
            message: '请输入快递单号',
            type: 'error',
            duration: 1500
          })
          return
        }
        this.$http({
          url: this.$http.adornUrl('orders/express'),
          method: 'get',
          params: this.$http.adornParams({
            'id': this.id,
            'expressName': this.expressName,
            'expressNumber': this.expressNumber
          })
        }).then(({data}) => {
          if (data.status == 0) {
            this.dialogFormVisible = false
            let returnData = data.data
            this.orderName = returnData.expressName
            this.orderNumber = returnData.expressNumber
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.expressName = ''
                this.expressNumber = ''
                this.dataSelect()
              }
            })
          }
        })
      },
      // 退款
      refundClick (row) {
        this.$confirm(`确定退款?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('orders/refundMoney'),
            method: 'get',
            params: this.$http.adornParams({
              'ordersId': row.id
            })
          }).then(({data}) => {
            if (data.status == 0) {
              this.$message({
                message: '退款成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.dataSelect()
                }
              })
            }
            if (data.status == -1) {
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
        }).catch(() => {
        })
      },
      // 拒绝退款
      refuseClick (row) {
        this.ordersId = row.id
        this.dialogFormVisible1 = true
      },
      refuseNoticeTo () {
        if (this.refusedRefund == '') {
          this.$message({
            message: '请输入拒绝退款理由',
            type: 'error',
            duration: 1500,
            onClose: () => {
              this.refusedRefund = ''
              this.dataSelect()
            }
          })
        } else {
          this.$http({
            url: this.$http.adornUrl('orders/refusedRefund'),
            method: 'get',
            params: this.$http.adornParams({
              'ordersId': this.ordersId,
              'refusedRefund': this.refusedRefund,
            })
          }).then(({data}) => {
            this.dialogFormVisible1 = false
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.refusedRefund = ''
                this.dataSelect()
              }
            })
          })
        }

      },
      // 详情
      orderDetails (id) {
        this.$router.push({path: '/orderDetails', query: {id: id}})
      },
      handleSizeChange (val) {
        this.size = val
        this.dataSelect()
      },
      handleCurrentChange (val) {
        this.page = val
        this.dataSelect()
      },
      handleClick (tab, event) {
        if (tab._props.label == '待发货订单') {
          this.page = 1
          this.size = 5
          this.status = 2
          this.dataSelect()
        }
        if (tab._props.label == '待退款订单') {
          this.page = 1
          this.size = 5
          this.status = 6
          this.dataSelect()
        }
      },
      //搜索
      select () {
        this.page = 1
        this.size = 5
        this.dataSelect()
      },
      // 重置
      cleans () {
        this.orderNum = ''
        this.status = 2
        this.dataSelect()
      },
      // 获取订单数据
      dataSelect () {
        let page = this.page - 1
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('orders/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': page,
            'size': this.size,
            'orderNum': this.orderNum,
            'status': this.status
          })
        }).then(({data}) => {
          this.tableDataLoading = false
          let returnData = data.data
          this.tableData = returnData.content
          this.totalnum = returnData.totalElements
        })
      }
    },
    mounted () {
      this.dataSelect()
    }
  }
</script>
<style scoped>
.nf-disposeorder-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-disposeorder-hero {
  margin-bottom: 12px;
}

.nf-disposeorder-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-disposeorder-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-disposeorder-filter {
  margin-bottom: 12px;
}

.nf-disposeorder-filter__body {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
}

.nf-disposeorder-filter__item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nf-disposeorder-filter__label {
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
  white-space: nowrap;
}

.nf-disposeorder-filter__input {
  width: 200px;
}

.nf-disposeorder-filter__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-left: auto;
}

.nf-disposeorder-tabs-wrap {
  margin-bottom: 0;
}

.nf-disposeorder-tabs {
  margin-bottom: 0;
}

.nf-disposeorder-tabs >>> .el-tabs__header {
  margin-bottom: 0;
  background: #fff;
  border-radius: 8px 8px 0 0;
  padding: 0 16px;
  border: 1px solid var(--nf-border, #e8eaed);
  border-bottom: none;
}

.nf-disposeorder-table-card {
  margin-bottom: 0;
  border-radius: 0 0 8px 8px;
  border-top: none;
}

.nf-disposeorder-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.nf-disposeorder-thumb {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
}

.nf-disposeorder-price {
  color: #f56c6c;
}

.nf-disposeorder-price--pointer {
  cursor: pointer;
}

.nf-disposeorder-brand-text {
  color: var(--nf-brand, #ffa8c2);
}

.nf-disposeorder-dialog-row {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.nf-disposeorder-dialog-row--textarea {
  align-items: flex-start;
}

.nf-disposeorder-dialog-label {
  width: 200px;
  flex-shrink: 0;
  text-align: right;
  padding-right: 12px;
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
}

.nf-disposeorder-dialog-input {
  width: 50%;
}
</style>
