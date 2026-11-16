<template>
  <div class="nf-page nf-shop-datas-page">
    <!-- 顶区：hero + 筛选白卡 -->
    <div class="nf-shop-datas-top">
      <div class="nf-page-hero nf-shop-datas-hero">
        <a href="#" class="nf-shop-datas-back" @click.prevent="prev">
          <icon-svg name="jiantou" class="nf-shop-datas-back__icon"></icon-svg>
          返回
        </a>
        <span class="nf-page-badge">门店数据</span>
        <h2 class="nf-shop-datas-hero__title">收入统计</h2>
        <p class="nf-shop-datas-hero__desc">查看门店订单金额、数量及退款等核心指标，支持按天/月/年筛选</p>
      </div>
      <div class="nf-filter-card nf-shop-datas-filter">
        <div class="nf-filter-card__head">
          <i class="el-icon-search"></i>
          <span>筛选条件</span>
        </div>
        <div class="nf-filter-card__body nf-shop-datas-filter__body">
          <el-select v-model="flag" class="nf-shop-datas-filter__select" @change="orderfenxi">
            <el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
          <el-date-picker
            class="nf-shop-datas-filter__date"
            v-model="info.stockDate"
            align="right"
            type="datetime"
            format="yyyy-MM-dd hh:mm:ss"
            value-format="yyyy-MM-dd hh:mm:ss"
            placeholder="选择开始时间"
            @change="animeOrder">
          </el-date-picker>
        </div>
      </div>
    </div>

    <!-- KPI 统计格 -->
    <div class="nf-table-card nf-shop-datas-kpi-card">
      <div class="nf-shop-datas-stat-grid">
        <div class="nf-stat-tile">
          <div class="nf-stat-tile__label">订单总金额</div>
          <div class="nf-stat-tile__value"><span>{{ taskStat.money ? taskStat.money : 0 }}</span>元</div>
        </div>
        <div class="nf-stat-tile">
          <div class="nf-stat-tile__label">订单数量</div>
          <div class="nf-stat-tile__value"><span>{{ taskStat.count ? taskStat.count : 0 }}</span>单</div>
        </div>
        <div class="nf-stat-tile">
          <div class="nf-stat-tile__label">到店订单数</div>
          <div class="nf-stat-tile__value"><span>{{ taskStat.takeCount ? taskStat.takeCount : 0 }}</span>单</div>
        </div>
        <div class="nf-stat-tile">
          <div class="nf-stat-tile__label">外卖订单数</div>
          <div class="nf-stat-tile__value"><span>{{ taskStat.takeCount2 ? taskStat.takeCount2 : 0 }}</span>单</div>
        </div>
        <div class="nf-stat-tile">
          <div class="nf-stat-tile__label">到店订单金额</div>
          <div class="nf-stat-tile__value"><span>{{ taskStat.takeMoney ? taskStat.takeMoney : 0 }}</span>元</div>
        </div>
        <div class="nf-stat-tile">
          <div class="nf-stat-tile__label">外卖订单金额</div>
          <div class="nf-stat-tile__value"><span>{{ taskStat.takeMoney1 ? taskStat.takeMoney1 : 0 }}</span>元</div>
        </div>
        <div class="nf-stat-tile">
          <div class="nf-stat-tile__label">退款订单数</div>
          <div class="nf-stat-tile__value"><span>{{ taskStat.cancelOrderCount ? taskStat.cancelOrderCount : 0 }}</span>单</div>
        </div>
        <div class="nf-stat-tile">
          <div class="nf-stat-tile__label">退款金额</div>
          <div class="nf-stat-tile__value"><span>{{ taskStat.cancelOrderMoney ? taskStat.cancelOrderMoney : 0 }}</span>元</div>
        </div>
      </div>
    </div>

    <!-- 订单列表表格白卡 -->
    <div class="nf-table-card nf-shop-datas-table-card">
      <el-table v-loading="tableDataLoading" :data="tableData2.list">
        <el-table-column fixed prop="orderId" label="编号" width="80"></el-table-column>
        <el-table-column fixed prop="userName" label="下单用户">
          <template slot-scope="scope">
            <span class="nf-link-text" @click="updates(scope.row)">
              {{ scope.row.userName ? scope.row.userName : '未绑定' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号"></el-table-column>
        <el-table-column prop="shopName" label="门店"></el-table-column>
        <el-table-column prop="orderType" label="订单类型">
          <template slot-scope="scope">
            <div v-if="scope.row.orderType==1">到店取餐</div>
            <div v-if="scope.row.orderType==2">外卖配送</div>
            <div v-if="scope.row.orderType==3">到店堂食</div>
          </template>
        </el-table-column>
        <el-table-column prop="orderType" label="配送信息" width="300">
          <template slot-scope="scope">
            <div v-if="scope.row.orderType==2">
              <div>订餐人：{{scope.row.address.userName}}</div>
              <div>订餐人电话：{{scope.row.address.userPhone}}</div>
              <div>订餐地址：{{scope.row.address.address}}({{scope.row.address.addressDetail}})</div>
            </div>
            <div v-else class="nf-shop-datas-empty-cell"> - </div>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="内容" width="300">
          <template slot-scope="scope">
            <div>
              <div v-for="(item,index) in scope.row.orderGoodsList" :key="index">
                <span>{{index+1}}.
                  商品名：{{item.goodsName}},</span><span>数量：{{item.goodsNum}},</span><span>规格：{{item.skuMessage?item.skuMessage:'单规格'}}</span>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="payMoney" label="接单价格" width="100"></el-table-column>
        <el-table-column prop="cancelMoney" label="退款金额" width="100">
          <template slot-scope="scope">
            <div class="nf-shop-datas-refund">{{scope.row.cancelMoney?scope.row.cancelMoney:0}}</div>
          </template>
        </el-table-column>
        <el-table-column prop="payTime" label="支付时间"></el-table-column>
        <el-table-column prop="orderCode" label="取餐号" width="100"></el-table-column>
        <el-table-column fixed="right" prop="status" label="状态" width="250">
          <template slot-scope="scope">
            <span class="nf-link-text" v-if="scope.row.status === 0 ">待结算</span>
            <span class="nf-link-text" v-if="scope.row.status === 1 ">待支付</span>
            <span class="nf-link-text" v-if="scope.row.status === 3 "> {{scope.row.orderType==1?'待取餐':'待配送'}}</span>
            <span class="nf-link-text" v-if="scope.row.status === 4 ">已完成</span>
            <span class="nf-shop-datas-status-cancel" v-if="scope.row.status === 5 ">已取消</span>
          </template>
        </el-table-column>
      </el-table>
      <div class="nf-shop-datas-pagination">
        <el-pagination
          @size-change="handleSizeChange3"
          @current-change="handleCurrentChange3"
          :page-sizes="[10, 20, 30, 40,50,100]"
          :page-size="limit"
          :current-page="page"
          layout="total,sizes, prev, pager, next"
          :total="tableData2.totalCount">
        </el-pagination>
      </div>
    </div>

    <!-- 任务拒绝弹框 -->
    <el-dialog title="任务拒绝" :visible.sync="dialogFormVisible7" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">拒绝理由：</span>
        <el-input style="width:50%;" v-model="content" type="text" placeholder="请输入拒绝理由"></el-input>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible7 = false">取 消</el-button>
        <el-button type="primary" @click="refuseto()">确 定</el-button>
      </div>
    </el-dialog>
    <!--    图片展示-->
    <el-dialog title="图片" :visible.sync="dialogVisible" width="35%" :before-close="handleClose">
      <span v-if="imageUrl.length>0" v-for="(item, index) in imageUrl" :key="index" style="width: 100px;
        height: 100px;display:inline-block;">
        <img :src="item" alt="" style="width: 100px; height: 100px">
      </span>
      <span v-else>暂无图片</span>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        dialogVisible: false,
        imageUrl: [],
        limit: 10,
        page: 1,
        phone: '',
        status: '',
        describes: '',
        token: '',
        taskNum: '',
        taskMoney: '',
        statesnum: [{
            label: '全部',
            value: 0
          },
          {
            label: '待接单',
            value: 2
          },
          {
            label: '待送达',
            value: 3
          },
          {
            label: '已完成',
            value: 4
          },
          {
            label: '已下架',
            value: 5
          },
        ],
        taskstates: [{
            label: '全部',
            value: ''
          },
          {
            label: '待结算',
            value: 0
          },
          {
            label: '待支付',
            value: 1
          },
          {
            label: '待取餐', //待取餐、派送中
            value: 3
          },
          {
            label: '已完成',
            value: 4
          },
          {
            label: '已取消',
            value: 5
          }
        ],
        shopId: '',
        shopList: '',
        sort: 10,
        id: '',
        btnChangeEnable: true,
        helpTakeId: '',
        helpMaintainId: '',
        content: '',
        classifyIcon: '',
        classifyId: '',
        classifyUrl: '',
        classifyName: '',
        classifyDeatilsName: '',
        formLabelWidth: '200px',
        activeName: 'first',
        tableDataLoading: false,
        dialogFormVisible: false,
        dialogFormVisible1: false,
        dialogFormVisible2: false,
        dialogFormVisible3: false,
        dialogFormVisible4: false,
        dialogFormVisible5: false,
        dialogFormVisible6: false,
        dialogFormVisible7: false,
        dialogFormVisible8: false,
        tableData: [],
        tableData2: {},
        form: {
          id: '',
          classifyName: '',
          classifyUrl: '',
          classifyIcon: '',
          describes: ''
        },
        form2: {
          id: '',
          classifyDeatilsName: '',
          classifyUrl: '',
          classifyIcon: '',
          taskNum: '',
          taskMoney: '',
        },
        protectjieData: [],
        protectpaiData: [],
        helpTaskData: [],
        safeguardData: [],
        classifyData: [],
        checkBoxData: [], //多选框选择的值
        checkBoxData2: [], //多选框选择的值
        userName: '',
        taskStat: {},
        startTime: '',
        endTime: '',
        info: {
          stockDate: this.getNowTime(), //日期
        },
        info1: {
          stockDate1: this.getNowTime1(), //日期
        },
        info2: {
          stockDate2: this.getNowTime2(), //日期
        },
        flag: 'day',
        flags: [{
          value: 'day',
          label: '按天查询'
        }, {
          value: 'month',
          label: '按月查询'
        }, {
          value: 'year',
          label: '按年查询'
        }],
      }
    },
    methods: {
      //处理默认选中当前日期
      getNowTime1() {
        var now = new Date();
        var year = now.getFullYear(); //得到年份
        var month = now.getMonth(); //得到月份
        var date = now.getDate(); //得到日期
        var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
        var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
        var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
        month = month + 1;
        month = month.toString().padStart(2, "0");
        date = date.toString().padStart(2, "0");
        var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
        return defaultDate;
        this.$set(this.info, "stockDate", defaultDate);
      },
      getNowTime2() {
        var now = new Date();
        var year = now.getFullYear(); //得到年份
        var month = now.getMonth(); //得到月份
        var date = now.getDate(); //得到日期
        var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
        var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
        var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
        month = month + 1;
        month = month.toString().padStart(2, "0");
        date = date.toString().padStart(2, "0");
        var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
        return defaultDate;
        this.$set(this.info, "stockDate", defaultDate);
      },
      //处理默认选中当前日期
      getNowTime() {
        var now = new Date();
        var year = now.getFullYear(); //得到年份
        var month = now.getMonth(); //得到月份
        var date = now.getDate(); //得到日期
        var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
        var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
        var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
        month = month + 1;
        month = month.toString().padStart(2, "0");
        date = date.toString().padStart(2, "0");
        var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
        return defaultDate;
        this.$set(this.info, "stockDate", defaultDate);
      },
      timeDate() {
        this.classifySelect()
        this.taskdataSelect()
      },
      // 详情跳转
      updates(row) {
        this.$router.push({
          path: '/userDetail',
          query: {
            userId: row.userId
          }
        })
      },
      // 订单分析选择日期
      animeOrder() {
        this.classifySelect()
        this.taskdataSelect()
      },
      // 订单分析年月日
      orderfenxi(value) {
        let vanumber = value
        if (vanumber === 'day') {
          this.flag = 'day'
          this.classifySelect()
          this.taskdataSelect()
        }
        if (vanumber === 'month') {
          this.flag = 'month'
          this.classifySelect()
          this.taskdataSelect()
        }
        if (vanumber === 'year') {
          this.flag = 'year'
          this.classifySelect()
          this.taskdataSelect()
        }
      },
      // 图标上传一级分类
      handleAvatarSuccess(file) {
        this.classifyIcon = file.data
      },
      // 图标上传一级分类编辑
      handleAvatarSuccess2(file) {
        this.form.classifyIcon = file.data
      },
      // 多选
      changeFun(val) {
        this.checkBoxData = val
      },
      // 多选
      changeFun2(val) {
        this.checkBoxData2 = val
      },
      handleSizeChange(val) {
        this.limit = val
        this.dataSelect()
      },
      handleCurrentChange(val) {
        this.page = val
        this.dataSelect()
      },
      handleSizeChange2(val) {
        this.limit = val
        this.classifySelect()
      },
      handleCurrentChange2(val) {
        this.page = val
        this.classifySelect()
      },
      handleSizeChange3(val) {
        this.limit = val
        this.taskdataSelect()
      },
      handleCurrentChange3(val) {
        this.page = val
        this.taskdataSelect()
      },
      // tabs切换
      handleClick(tab, event) {

        if (tab._props.label == '订单管理') {
          this.page = 1
          this.limit = 10
          this.taskdataSelect()
        }

      },
      // 下架
      soldClick(row) {
        this.$confirm(`确定要下架此任务?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl(`help/outHelpOrder/?helpOrderId=${row.id}`),
            method: 'post',
            data: this.$http.adornData({})
          }).then(({
            data
          }) => {
            if (data.code == 0) {
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
        }).catch(() => {})
      },
      //查看照片
      refund(e) {
        this.imageUrl = []
        if (e.image != null) {
          let img = e.image.split(',')
          if (img.length != 0) {
            this.imageUrl = img
          }
        }
        this.dialogVisible = true
      },
      handleClose(done) {
        done()
      },
      // 添加一级分类弹框
      classifyStair() {
        this.dialogFormVisible = true
      },
      // 添加一级分类确定
      StairNoticeTo() {
        if (this.classifyName == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入分类名称',
            type: 'warning'
          })
          return
        }
        if (this.describes == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入分类描述',
            type: 'warning'
          })
          return
        }
        if (this.classifyIcon == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请上传分类图标',
            type: 'warning'
          })
          return
        }
        this.$http({
          url: this.$http.adornUrl('helpClassify/saveHelpClassify'),
          method: 'post',
          data: this.$http.adornData({
            'classifyName': this.classifyName,
            'state': this.state,
            'sort': this.sort,
            'classifyIcon': this.classifyIcon,
            'classifyUrl': this.classifyUrl,
            'describes': this.describes
          })
        }).then(({
          data
        }) => {
          this.dialogFormVisible = false
          this.$message({
            message: '一级分类添加成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.classifyUrl = ''
              this.classifyName = ''
              this.classifyIcon = ''
              this.describes = ''
              this.classifySelect()
            }
          })
        })
      },
      // 修改一级分类
      compile(index, rows) {
        this.dialogFormVisible2 = true
        this.form.id = rows.id
        this.form.classifyName = rows.classifyName
        this.form.classifyUrl = rows.classifyUrl
        this.form.classifyIcon = rows.classifyIcon
        this.form.describes = rows.describes
      },
      // 修改一级分类确定
      CompileNoticeTo() {
        if (this.form.classifyName == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入分类名称',
            type: 'warning'
          })
          return
        }
        if (this.form.describes == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入分类描述',
            type: 'warning'
          })
          return
        }
        if (this.form.classifyIcon == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请上传分类图标',
            type: 'warning'
          })
          return
        }
        this.$http({
          url: this.$http.adornUrl('helpClassify/updateHelpClassify'),
          method: 'post',
          data: this.$http.adornData({
            'id': this.form.id,
            'classifyName': this.form.classifyName,
            'classifyUrl': this.form.classifyUrl,
            'classifyIcon': this.form.classifyIcon,
            'describes': this.form.describes
          })
        }).then(({
          data
        }) => {
          this.dialogFormVisible2 = false
          this.$message({
            message: '操作成功',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.classifySelect()
            }
          })
        })
      },
      //删除一级
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
          }).then(({
            data
          }) => {
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
      // 手机号搜索
      phoneSelect() {
        this.page = 1
        this.limit = 10
        this.taskdataSelect()
      },
      // 重置
      cleans2() {
        this.phone = ''
        this.status = ''
        this.userName = ''
        this.page = 1
        this.taskdataSelect()
      },
      // 查询
      select() {
        this.page = 1
        this.limit = 10
        this.dataSelect()
      },
      // 重置
      cleans() {
        this.page = 1
        this.phone = ''
        this.status = 0
        this.content = ''
        this.dataSelect()
      },
      // select选择事件
      animeDat(state) {
        this.page = 1
        this.dataSelect()
      },
      animeDat2(state) {
        this.page = 1
        this.taskdataSelect()
      },
      // 获取派单数据列表
      dataSelect() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('help/selectStatusHelpOrder'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit,
            'phone': this.phone,
            'status': this.status,
            'content': this.content
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data
          this.tableData = returnData
        })
      },
      // 获取接单数据列表
      taskdataSelect() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('admin/goodsShop/selectStoreMessage'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit,
            'date': this.info.stockDate,
            'dateType': this.flag,
            'shopId': this.shopId,
						'status':4
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data
          this.tableData2 = returnData
          for (var i in this.tableData2.list) {
            if (this.tableData2.list[i].address) {
              this.tableData2.list[i].address = JSON.parse(this.tableData2.list[i].address)
            }
          }
        })
      },
      // 门店统计
      classifySelect() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('admin/goodsShop/selectStoreData'),
          method: 'get',
          params: this.$http.adornParams({
            'shopId': this.shopId,
            'date': this.info.stockDate,
            'dateType': this.flag
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.hashMap
          this.taskStat = returnData
        })
      },
      // 餐品完成
      wancheng(row) {
        this.$http({
          url: this.$http.adornUrl('admin/order/updateOrder'),
          method: 'post',
          data: this.$http.adornData({
            'status': 4,
            'orderId': row.orderId,
            'orderNumber': row.orderNumber
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
                this.taskdataSelect()
              }
            })
          } else {
            this.$message({
              message: data.msg,
              type: 'warning',
              duration: 1500,
              onClose: () => {}
            })
          }
        })
      },
      // 返回上一级
      prev() {
        this.$router.back()
      },
    },
    mounted() {
      if (this.$cookie.get('shopId')) {
        this.shopId = this.$cookie.get('shopId')
      } else {
        this.shopId = this.$route.query.shopId
      }
      this.classifySelect()
      this.taskdataSelect()

    },
    watch: {
      '$route': 'taskdataSelect'

    }
  }
</script>

<style scoped>
.nf-shop-datas-page {
  padding: 0;
  overflow-x: hidden;
}

/* 顶区 hero + 筛选 */
.nf-shop-datas-top {
  display: flex;
  gap: 12px;
  margin-bottom: 12px;
  align-items: stretch;
}

.nf-shop-datas-hero {
  flex: 1;
  min-width: 0;
  margin-bottom: 0;
}

.nf-shop-datas-back {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  text-decoration: none;
  margin-bottom: 8px;
  transition: color 0.2s;
}

.nf-shop-datas-back:hover {
  color: var(--nf-brand, #ffa8c2);
}

.nf-shop-datas-back__icon {
  width: 1.2em;
  height: 1.2em;
}

.nf-shop-datas-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-shop-datas-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-shop-datas-filter {
  width: 320px;
  flex-shrink: 0;
  margin-bottom: 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.nf-shop-datas-filter__body {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.nf-shop-datas-filter__select,
.nf-shop-datas-filter__date {
  width: 100% !important;
}

/* KPI 统计格 */
.nf-shop-datas-kpi-card {
  margin-bottom: 12px;
}

.nf-shop-datas-stat-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
}

.nf-stat-tile {
  padding: 14px 16px;
  background: var(--nf-sidebar-bg-alt, #f7f8fa);
  border-radius: 8px;
  border: 1px solid var(--nf-border, #e8eaed);
}

.nf-stat-tile__label {
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  margin-bottom: 6px;
}

.nf-stat-tile__value {
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
}

.nf-stat-tile__value span {
  font-size: 20px;
  font-weight: 600;
  color: var(--nf-brand, #ffa8c2);
  margin-right: 4px;
}

/* 表格白卡 */
.nf-shop-datas-table-card {
  margin-bottom: 0;
}

.nf-link-text {
  color: var(--nf-brand, #ffa8c2);
  cursor: pointer;
}

.nf-link-text:hover {
  color: var(--nf-brand-deep, #ff759e);
}

.nf-shop-datas-refund {
  color: #f56c6c;
}

.nf-shop-datas-status-cancel {
  color: #999;
}

.nf-shop-datas-empty-cell {
  text-align: center;
  width: 100%;
}

.nf-shop-datas-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

@media (max-width: 1200px) {
  .nf-shop-datas-stat-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .nf-shop-datas-top {
    flex-direction: column;
  }

  .nf-shop-datas-filter {
    width: 100%;
  }

  .nf-shop-datas-stat-grid {
    grid-template-columns: 1fr;
  }
}
</style>
