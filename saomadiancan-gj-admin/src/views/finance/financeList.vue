<template>
  <div class="nf-page nf-financelist-page">
    <div class="nf-page-hero nf-financelist-hero">
      <span class="nf-page-badge">财务中心</span>
      <h2 class="nf-financelist-hero__title">充值与财务</h2>
      <p class="nf-financelist-hero__desc">查看用户充值记录与充值统计数据，支持按状态、昵称、手机号及时间筛选</p>
    </div>

    <div v-show="activeName === 'fourth'" class="nf-filter-card nf-financelist-filter">
      <div class="nf-filter-card__head">
        <i class="el-icon-search"></i>
        <span>筛选条件</span>
      </div>
      <div class="nf-filter-card__body nf-financelist-filter__body">
        <div class="nf-financelist-filter__item">
          <span class="nf-financelist-filter__label">状态：</span>
          <el-select v-model="stateT" placeholder="请选择状态" class="nf-financelist-filter__select" @change="timeDate()">
            <el-option v-for="item in states" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div class="nf-financelist-filter__item">
          <span class="nf-financelist-filter__label">昵称：</span>
          <el-input class="nf-financelist-filter__input" @keydown.enter.native="timeDate" placeholder="请输入昵称"
            v-model="userNameT"></el-input>
        </div>
        <div class="nf-financelist-filter__item">
          <span class="nf-financelist-filter__label">手机号：</span>
          <el-input class="nf-financelist-filter__input" @keydown.enter.native="timeDate" placeholder="请输入手机号"
            v-model="phoneT"></el-input>
        </div>
        <div class="nf-financelist-filter__item">
          <span class="nf-financelist-filter__label">开始时间：</span>
          <el-date-picker class="nf-financelist-filter__date" v-model="startTime" align="right" type="datetime"
            format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
          </el-date-picker>
        </div>
        <div class="nf-financelist-filter__item">
          <span class="nf-financelist-filter__label">截止时间：</span>
          <el-date-picker class="nf-financelist-filter__date" v-model="endTime" align="right" type="datetime"
            format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
          </el-date-picker>
        </div>
        <div class="nf-financelist-filter__actions">
          <el-button size="mini" type="primary" icon="document" @click="timeDate">查询</el-button>
          <el-button size="mini" type="primary" icon="document" @click="chongzhiBtn">重置</el-button>
        </div>
      </div>
    </div>

    <div v-show="activeName === 'sixth'" class="nf-filter-card nf-financelist-filter">
      <div class="nf-filter-card__head">
        <i class="el-icon-search"></i>
        <span>筛选条件</span>
      </div>
      <div class="nf-filter-card__body nf-financelist-filter__body">
        <div class="nf-financelist-filter__item">
          <el-select v-model="flag" class="nf-financelist-filter__select" @change="animeDat3">
            <el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div class="nf-financelist-filter__item">
          <el-date-picker class="nf-financelist-filter__date nf-financelist-filter__date--wide" v-model="info1.stockDate1" align="right" type="datetime"
            format="yyyy-MM-dd hh:mm:ss" value-format="yyyy-MM-dd hh:mm:ss" placeholder="选择开始时间" @change="animeSelect3">
          </el-date-picker>
        </div>
      </div>
    </div>

    <div class="nf-financelist-tabs-wrap">
      <el-tabs v-model="activeName" @tab-click="handleClick" class="nf-financelist-tabs">
    <!-- <el-tab-pane label="提现管理" name="first">
      <div style="margin:2% 0;display: inline-block;">
        <div style="position: relative;display: inline-block;">
          <el-input style="width: 200px;" @keydown.enter.native="zfbselect" placeholder="请输入支付宝号查询" v-model="zhifubao">
          </el-input>&nbsp;&nbsp;
          <span @click="zfbselect" style="position: absolute;right: 18px;top:8px;">
            <icon-svg name="shousuo" class="site-sidebar__menu-icon"></icon-svg>
          </span>
        </div>
        <div style="position: relative;display: inline-block;">
          <el-input style="width: 200px;" @keydown.enter.native="qdselect" placeholder="请输入支付宝号名称"
            v-model="zhifubaoName"></el-input>&nbsp;&nbsp;
          <span @click="qdselect" style="position: absolute;right: 18px;top:8px;">
            <icon-svg name="shousuo" class="site-sidebar__menu-icon"></icon-svg>
          </span>
        </div>
      </div>
      <div style="display: inline-block;">
        <el-button style='margin:0 0 20px 20px;' size="mini" type="primary" icon="document" @click="transferClcik()"
          :disabled="checkBoxData.length <= 0 || !isAuth('financeList:transfer')">批量转账
        </el-button>
      </div>
      <el-table @selection-change="changeFun" v-loading="tableDataLoading" :data="tableData">
        <el-table-column type="selection">
        </el-table-column>
        <el-table-column fixed prop="id" label="编号" width="80">
        </el-table-column>
        <el-table-column prop="zhifubao" label="支付宝账号" width="150">
          <template slot-scope="scope">
            <span style="color: #4f9dec;cursor: pointer;"
              @click="updates(scope.row)">{{scope.row.zhifubao ? scope.row.zhifubao : '未绑定'}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="zhifubaoName" label="支付宝名称" width="100">
          <template slot-scope="scope">
            <span>{{scope.row.zhifubaoName ? scope.row.zhifubaoName : '未绑定'}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="money" label="提现金额" width="100">
        </el-table-column>
        <el-table-column prop="state" label="状态" width="100">
          <template slot-scope="scope">
            <span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.state == 0">待转账</span>
            <span v-if="scope.row.state == 1">已转账</span>
            <span v-if="scope.row.state == -1">已退款</span>
          </template>
        </el-table-column>
        <el-table-column prop="refund" label="退款原因" width="220">
        </el-table-column>
        <el-table-column prop="createAt" label="申请时间" width="170">
        </el-table-column>
        <el-table-column prop="outAt" label="转账/退款时间" width="180">
        </el-table-column>
        <el-table-column prop="orderNumber" label="转账订单号" width="150">
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="150">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" :disabled="!isAuth('financeList:transfer')" @click="batch(scope.row)">
              转账
            </el-button>
            <el-button size="mini" type="primary" :disabled="!isAuth('financeList:refund')" @click="refund(scope.row)">
              退款
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="text-align: center;margin-top: 10px;">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :page-sizes="[5, 10, 15]"
          :page-size="limit" :current-page="page" layout="total,sizes, prev, pager, next" :total="totalnum">
        </el-pagination>
      </div>
      退款弹框
      <el-dialog title="退款" :visible.sync="dialogFormVisible" center>
        <el-form :model="form">
          <el-form-item label="退款原因：" :label-width="formLabelWidth">
            <el-input v-model="form.content" type="textarea" rows="4" placeholder="请输入退款原因" style="width:65%;">
            </el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="refundNoticeTo()">确 定</el-button>
        </div>
      </el-dialog>
    </el-tab-pane> -->
    <!-- <el-tab-pane label="收入统计" name="second">
      <div>
        <el-select v-model="flag" style="width:150px;margin-left: 10px;" @change="animeDat">
          <el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>&nbsp;&nbsp;&nbsp;
        <el-date-picker style="width: 200px;margin-left: 10px;" v-model="info1.stockDate1" align="right" type="datetime"
          format="yyyy-MM-dd hh:mm:ss" value-format="yyyy-MM-dd hh:mm:ss" placeholder="选择开始时间" @change="animeSelect">
        </el-date-picker>
      </div>
      <el-row>
        <el-col :span="6" class="cards">
          <div class="box">
            <div class="box_num">
              <div class="box_color">发布任务支出金额</div>
              <div class="text_color"><span>{{MoneyData.sumPrice ? MoneyData.sumPrice : 0}}</span>元</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6" class="cards">
          <div class="box">
            <div class="box_num">
              <div class="box_color">用户收益</div>
              <div class="text_color"><span>{{MoneyData.sumMoneyBySend ? MoneyData.sumMoneyBySend : 0}}</span>元</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6" class="cards">
          <div class="box">
            <div class="box_num">
              <div class="box_color">平台任务收益</div>
              <div class="text_color"><span>{{MoneyData.sumMoneyByProfit ? MoneyData.sumMoneyByProfit  : 0}}</span>元
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-tab-pane>
    <el-tab-pane label="提现统计" name="third">
      <div>
        <el-select v-model="flag" style="width:150px;margin-left: 10px;" @change="animeDat2">
          <el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>&nbsp;&nbsp;&nbsp;
        <el-date-picker style="width: 200px;margin-left: 10px;" v-model="info1.stockDate1" align="right" type="datetime"
          format="yyyy-MM-dd hh:mm:ss" value-format="yyyy-MM-dd hh:mm:ss" placeholder="选择开始时间" @change="animeSelect2">
        </el-date-picker>
      </div>
      <el-row>
        <el-col :span="6" class="cards">
          <div class="box">
            <div class="box_num">
              <div class="box_color">累计提现金额</div>
              <div class="text_color">
                <span>{{withdrawData.sumMoney}}</span>元
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6" class="cards">
          <div class="box">
            <div class="box_num">
              <div class="box_color">提现次数</div>
              <div class="text_color">
                <span>{{withdrawData.countMoney}}</span>笔
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="6" class="cards">
          <div class="box">
            <div class="box_num">
              <div class="box_color">待转账次数</div>
              <div class="text_color">
                <span>{{withdrawData.stayMoney}}</span>笔
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-tab-pane> -->
    <el-tab-pane label="充值记录" name="fourth">
      <div class="nf-table-card nf-financelist-table-card">
      <el-table v-loading="tableDataLoading" :data="rechargeData">
        <el-table-column fixed prop="id" label="编号" width="80">
        </el-table-column>
        <el-table-column prop="userName" label="用户名称">
          <template slot-scope="scope">
            <span class="nf-financelist-user-link"
              @click="updates(scope.row)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
          </template>
        </el-table-column>
		<el-table-column prop="phone" label="手机号">
		</el-table-column>
		
        <el-table-column prop="orderId" label="充值订单号" width="250">
        </el-table-column>
        <el-table-column prop="money" label="充值金额" width="100">
        </el-table-column>
        <!-- <el-table-column label="分类" width="120">
          <template slot-scope="scope">
            <span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.classify == 1">微信</span>
            <span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.classify == 2">支付宝</span>
          </template>
        </el-table-column> -->
        <el-table-column label="状态" width="120">
          <template slot-scope="scope">
            <span class="nf-financelist-brand-text" v-if="scope.row.state == 0">待充值</span>
            <span class="nf-financelist-brand-text" v-if="scope.row.state == 1">充值成功</span>
            <span class="nf-financelist-brand-text" v-if="scope.row.state == 2">充值失败</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170">
        </el-table-column>
        <el-table-column prop="payTime" label="支付时间" width="170">
        </el-table-column>
      </el-table>
      <div class="nf-financelist-pagination">
        <el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1" :page-sizes="[10, 15, 20, 30]"
          :page-size="limit" :current-page="page" layout="total,sizes, prev, pager, next" :total="totalnum2">
        </el-pagination>
      </div>
      </div>
    </el-tab-pane>
    <el-tab-pane label="充值统计" name="sixth">
      <div class="nf-table-card nf-financelist-stats-card">
        <div class="nf-financelist-stat-grid">
          <div class="nf-stat-tile">
            <div class="nf-stat-tile__label">充值总金额</div>
            <div class="nf-stat-tile__value"><span>{{rechgeData ? rechgeData : 0}}</span>元</div>
          </div>
        </div>
      </div>
    </el-tab-pane>

      </el-tabs>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        page: 1,
        limit: 10,
        balance: -1,
        zhifubao: '',
        tkstatus: -1,
        state: 1,
        states: [{
            label: '全部',
            value: -1
          },
          {
            label: '待充值',
            value: 0
          },
          {
            label: '充值成功',
            value: 1
          },
          {
            label: '充值失败',
            value: 2
          }
        ],
        zhifubaoName: '',
        content: '',
        startTime: '',
        endTime: '',
        cashId: '',
        type: -1,
        totalnum: 0,
        totalnum2: 0,
        sqxTotal: '',
        userTotal: '',
        taunTotal: '',
        relationId: '',
        sumMoneyByTime: '',
        money: '',
        activeName: 'fourth',
        formLabelWidth: '200px',
        dialogFormVisible: false,
        dialogFormVisible1: false,
        dialogFormVisible2: false,
        tableDataLoading: false,
        tableData: [],
        rechargeData: [],
        rechgeData: {},
        withdrawData: {},
        depositData: [],
        MoneyData: {},
        checkBoxData: [], //多选框选择的值
        flag: 'day',
        form: {
          cashId: '',
          content: '',
        },
        form2: {
          id: '',
          money: '',
          createTime: ''
        },
        info: {
          stockDate: this.getNowTime(), //日期
        },
        info1: {
          stockDate1: this.getNowTime1(), //日期
        },
        info2: {
          stockDate2: this.getNowTime2(), //日期
        },
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
		userNameT:'',
		phoneT:'',
		stateT:'',
		
      }
    },
    methods: {
      // 会员详情跳转
      updates(row) {
        this.$router.push({
          path: '/userDetail',
          query: {
            userId: row.userId
          }
        })
      },
      // 批量转账
      transferClcik(id) {

        var ids = id ? [id] : this.checkBoxData.map(item => {
          return item.id
        })
        for (var i in ids) {
          this.$http({
            url: this.$http.adornUrl(`cash/alipay/${ids[i]}`),
            method: 'post',
            data: this.$http.adornData({})
          }).then(({
            data
          }) => {
            if (data.code == 0) {
              this.$notify({
                title: '提示',
                duration: 1800,
                message: data.msg,
                type: 'warning'
              })
              return
            }
            if (data.code == 9999) {
              this.$notify({
                title: '提示',
                duration: 1800,
                message: data.msg,
                type: 'error'
              })
              return
            }
          })
        }
      },
      // 多选
      changeFun(val) {
        this.checkBoxData = val
      },
      //转账
      batch(row) {
        if (row.state == 1) {
          this.$message({
            message: '已转账，请勿重复操作！',
            type: 'error',
            duration: 1500
          })
          return
        }
        this.$confirm(`确定转账?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl(`cash/alipay/${row.id}`),
            method: 'post',
            data: this.$http.adornData({})
          }).then(({
            data
          }) => {
            if (data.code == 0) {
              this.$message({
                message: data.msg,
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.dataSelect()
                }
              })
              return
            }
            if (data.code == 9999) {
              this.$message({
                message: data.msg,
                type: 'error',
                duration: 1500,
                onClose: () => {
                  this.dataSelect()
                }
              })
              return
            }
            if (data.code == 500) {
              this.$message({
                message: data.msg,
                type: 'error',
                duration: 1500,
                onClose: () => {
                  this.dataSelect()
                }
              })
              return
            }
          })
        })
      },
      // 退款
      refund(row) {
        if (row.state == -1) {
          this.$message({
            message: '已退款，请勿重复操作！',
            type: 'error',
            duration: 1500
          })
        } else {
          this.dialogFormVisible = true
          this.form.cashId = row.id
        }
      },
      // 退款操作
      refundNoticeTo() {
        if (this.form.content == '') {
          this.$message({
            message: '请输入退款原因',
            type: 'error',
            duration: 1500
          })
          return
        }
        this.$http({
          url: this.$http.adornUrl(`cash/refund/${this.form.cashId}/${this.form.content}`),
          method: 'post',
          data: this.$http.adornData({})
        }).then(({
          data
        }) => {
          if (data.code == 0) {
            this.dialogFormVisible = false
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.form.content = ''
                this.dataSelect()
              }
            })
            return
          }
          if (data.code == 500) {
            this.dialogFormVisible = false
            this.$message({
              message: data.msg,
              type: 'error',
              duration: 1500,
              onClose: () => {
                this.form.content = ''
                this.dataSelect()
              }
            })
            return
          }
          if (data.code == -100) {
            this.dialogFormVisible = false
            this.$message({
              message: data.msg,
              type: 'error',
              duration: 1500,
              onClose: () => {
                this.form.content = ''
                this.dataSelect()
              }
            })
            return
          }
        })
      },
     
      // 日期选择
      animeDat(value) {
        let vanumber = value
        if (vanumber === 'day') {
          this.flag = 'day'
          this.incomeSelect()
        }
        if (vanumber === 'month') {
          this.flag = 'month'
          this.incomeSelect()

        }
        if (vanumber === 'year') {
          this.flag = 'year'
          this.incomeSelect()
        }
      },
      // 日期选择2
      animeDat2(value) {
        let vanumber = value
        if (vanumber === 'day') {
          this.flag = 'day'
          this.withdrawSelect()
        }
        if (vanumber === 'month') {

          this.flag = 'month'
          this.withdrawSelect()
        }
        if (vanumber === 'year') {
          this.flag = 'year'
          this.withdrawSelect()
        }
      },
      // 日期选择3
      animeDat3(value) {
        let vanumber = value
        if (vanumber === 'day') {
          this.flag = 'day'
          this.rechSelect()
        }
        if (vanumber === 'month') {

          this.flag = 'month'
          this.rechSelect()
        }
        if (vanumber === 'year') {
          this.flag = 'year'
          this.rechSelect()
        }
      },
      animeSelect() {
        this.incomeSelect()
      },
      animeSelect2() {
        this.withdrawSelect()
      },
      animeSelect3() {
        this.rechSelect()
      },
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
        month = month + 1;
        month = month.toString().padStart(2, "0");
        date = date.toString().padStart(2, "0");
        var defaultDate = `${year}-${month}-${date}`;
        return defaultDate;
        this.$set(this.info, "stockDate", defaultDate);
      },
      //处理默认选中当前日期
      getNowTime() {
        var now = new Date()
        var year = now.getFullYear() //得到年份
        var month = now.getMonth() - now.getMonth() //得到月份
        var date = now.getDate() - now.getDate() + 1 //得到日期
        month = month + 1
        month = month.toString().padStart(2, '0')
        date = date.toString().padStart(2, '0')
        var defaultDate = `${year}-${month}-${date}`
        return defaultDate
        this.$set(this.info, 'stockDate', defaultDate)
      },
      handleSizeChange(val) {
        this.limit = val
        this.dataSelect()
      },
      handleCurrentChange(val) {
        this.page = val
        this.dataSelect()
      },
      handleSizeChange1(val) {
        this.limit = val
        this.rechargeSelect()
      },
      handleCurrentChange1(val) {
        this.page = val
        this.rechargeSelect()
      },
      handleClick(tab, event) {
        if (tab._props.label == '提现管理') {
          this.page = 1
          this.limit = 5
          this.dataSelect()
        }
        if (tab._props.label == '收入统计') {
          this.flag = 'day'
          this.incomeSelect()
        }
        if (tab._props.label == '充值统计') {
          this.flag = 'day'
          this.rechSelect()
        }
        if (tab._props.label == '提现统计') {
          this.flag = 'day'
          this.withdrawSelect()
        }
        if (tab._props.label == '充值记录') {
          this.page = 1
          this.limit = 10
          this.rechargeSelect()
        }
      },
      // 支付宝账号查询
      zfbselect() {
        if (this.zhifubao == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入支付宝账号',
            type: 'error'
          })
          return
        }
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('user/selectPayDetails'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit,
            'zhifubao': this.zhifubao
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data
          this.tableData = returnData.list
          this.totalnum = returnData.totalCount
        })
      },
      // 支付宝名称
      qdselect() {
        if (this.zhifubaoName == '') {
          this.$notify({
            title: '提示',
            duration: 1800,
            message: '请输入渠道账号',
            type: 'error'
          })
          return
        }
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('user/selectPayDetails'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit,
            'zhifubaoName': this.zhifubaoName
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data
          this.tableData = returnData.list
          this.totalnum = returnData.totalCount
        })
      },
      // 获取数据列表
      dataSelect() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('user/selectPayDetails'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data
          this.tableData = returnData.list
          this.totalnum = returnData.totalCount
        })
      },
      // 收入统计
      incomeSelect() {
        this.$http({
          url: this.$http.adornUrl('statistical/statisticsIncomeMoney'),
          method: 'get',
          params: this.$http.adornParams({
            'time': this.info1.stockDate1,
            'flag': this.flag
          })
        }).then(({
          data
        }) => {
          if (data.code == 0) {
            let returnData = data.data
            this.MoneyData = returnData
          }
        })
      },
      // 充值统计
      rechSelect() {
        this.$http({
          url: this.$http.adornUrl('admin/dataCentre/selectTopUpStatistics'),
          method: 'get',
          params: this.$http.adornParams({
            'date': this.info1.stockDate1,
            'dateType': this.flag
          })
        }).then(({
          data
        }) => {
          if (data.code == 0) {
            let returnData = data.data
            this.rechgeData = returnData
          }
        })
      },
      // 提现统计
      withdrawSelect() {
        this.$http({
          url: this.$http.adornUrl('statistical/statisticsCashMoney'),
          method: 'get',
          params: this.$http.adornParams({
            'time': this.info1.stockDate1,
            'flag': this.flag
          })
        }).then(({
          data
        }) => {
          let returnData = data.data
          this.withdrawData = returnData
        })
      },
      timeDate() {
      		this.page = 1
        this.rechargeSelect()
      },
      chongzhiBtn(){
      		  this.page = 1
      		  this.rechargeSelect()
      },
      // 获取充值记录
      rechargeSelect() {
        if (this.endTime == '') {
          this.endTime = this.info2.stockDate2
        }
        if (this.startTime == '') {
          this.startTime = this.info.stockDate
        }
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('cash/selectUserRecharge'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit,
            'state': this.stateT,
            'startTime': this.startTime,
            'endTime': this.endTime,
			'userName': this.userNameT,
			'phone': this.phoneT,
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data
          this.rechargeData = returnData.list
          this.totalnum2 = returnData.totalCount
        })
      }
    },
    mounted() {
      // this.dataSelect()
      this.rechargeSelect()
    }
  }
</script>

<style scoped>
.nf-financelist-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-financelist-hero {
  margin-bottom: 12px;
}

.nf-financelist-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-financelist-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-financelist-filter {
  margin-bottom: 12px;
}

.nf-financelist-filter__body {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
}

.nf-financelist-filter__item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nf-financelist-filter__label {
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
  white-space: nowrap;
}

.nf-financelist-filter__select {
  width: 150px;
}

.nf-financelist-filter__input {
  width: 200px;
}

.nf-financelist-filter__date {
  width: 160px;
}

.nf-financelist-filter__date--wide {
  width: 200px;
}

.nf-financelist-filter__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-left: auto;
}

.nf-financelist-tabs-wrap {
  margin-bottom: 0;
}

.nf-financelist-tabs {
  margin-bottom: 0;
}

.nf-financelist-tabs >>> .el-tabs__header {
  margin-bottom: 0;
  background: #fff;
  border-radius: 8px 8px 0 0;
  padding: 0 16px;
  border: 1px solid var(--nf-border, #e8eaed);
  border-bottom: none;
}

.nf-financelist-table-card,
.nf-financelist-stats-card {
  margin-bottom: 0;
  border-radius: 0 0 8px 8px;
  border-top: none;
}

.nf-financelist-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.nf-financelist-user-link {
  color: var(--nf-brand, #ffa8c2);
  cursor: pointer;
}

.nf-financelist-brand-text {
  color: var(--nf-brand, #ffa8c2);
}

.nf-financelist-stat-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 12px;
}

.nf-stat-tile {
  padding: 14px 16px;
  background: #fff;
  border-radius: 8px;
  border: 1px solid var(--nf-border, #e8eaed);
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
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
</style>
