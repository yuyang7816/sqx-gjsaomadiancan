<template>
  <div class="nf-page nf-home-page">
    <!-- 顶区：hero + 筛选白卡 -->
    <div class="nf-home-top">
      <div class="nf-page-hero nf-home-hero">
        <span class="nf-page-badge">数据中心</span>
        <h2 class="nf-home-hero__title">数据概览</h2>
        <p class="nf-home-hero__desc">实时查看用户、订单与收入核心指标，支持按天/月/年筛选分析</p>
      </div>
      <div class="nf-filter-card nf-home-filter">
        <div class="nf-filter-card__head">
          <i class="el-icon-search"></i>
          <span>筛选条件</span>
        </div>
        <div class="nf-filter-card__body nf-home-filter__body">
          <el-select v-model="flag" class="nf-home-filter__select" @change="orderfenxi">
            <el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
          <el-date-picker
            class="nf-home-filter__date"
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

    <!-- KPI 四列 -->
    <div class="nf-home-kpi">
      <div class="nf-kpi-card">
        <div class="nf-kpi-card__icon"><i class="el-icon-user"></i></div>
        <div class="nf-kpi-card__body">
          <div class="nf-kpi-card__label">总用户数</div>
          <div class="nf-kpi-card__value"><span>{{ tableData.allCount ? tableData.allCount : 0 }}</span>人</div>
        </div>
      </div>
      <div class="nf-kpi-card">
        <div class="nf-kpi-card__icon"><i class="el-icon-plus"></i></div>
        <div class="nf-kpi-card__body">
          <div class="nf-kpi-card__label">今日新增</div>
          <div class="nf-kpi-card__value"><span>{{ tableData.dayAddUser ? tableData.dayAddUser : 0 }}</span>人</div>
        </div>
      </div>
      <div class="nf-kpi-card">
        <div class="nf-kpi-card__icon"><i class="el-icon-date"></i></div>
        <div class="nf-kpi-card__body">
          <div class="nf-kpi-card__label">本月新增</div>
          <div class="nf-kpi-card__value"><span>{{ tableData.monthAddUser ? tableData.monthAddUser : 0 }}</span>人</div>
        </div>
      </div>
      <div class="nf-kpi-card">
        <div class="nf-kpi-card__icon"><i class="el-icon-trophy"></i></div>
        <div class="nf-kpi-card__body">
          <div class="nf-kpi-card__label">本年新增</div>
          <div class="nf-kpi-card__value"><span>{{ tableData.yearAddUser ? tableData.yearAddUser : 0 }}</span>人</div>
        </div>
      </div>
    </div>

    <!-- 双列明细格 -->
    <div class="nf-home-detail">
      <div class="nf-stat-tile">
        <div class="nf-stat-tile__label">今日收入</div>
        <div class="nf-stat-tile__value"><span>{{ tableData.dayIncome ? tableData.dayIncome : 0 }}</span>元</div>
      </div>
      <div class="nf-stat-tile">
        <div class="nf-stat-tile__label">本月收入</div>
        <div class="nf-stat-tile__value"><span>{{ tableData.monthIncome ? tableData.monthIncome : 0 }}</span>元</div>
      </div>
      <div class="nf-stat-tile">
        <div class="nf-stat-tile__label">本年收入</div>
        <div class="nf-stat-tile__value"><span>{{ tableData.yearIncome ? tableData.yearIncome : 0 }}</span>笔</div>
      </div>
      <div class="nf-stat-tile">
        <div class="nf-stat-tile__label">今日接单数量</div>
        <div class="nf-stat-tile__value"><span>{{ tableData.indentCount ? tableData.indentCount : 0 }}</span>笔</div>
      </div>
    </div>

    <!-- 分析 tabs + 内容白卡 -->
    <div class="nf-home-tabs-wrap">
      <el-tabs v-model="activeName" @tab-click="handleClick" class="nf-home-tabs">
        <el-tab-pane label="订单分析" name="first">
          <div class="nf-table-card nf-home-tab-card">
            <div class="nf-home-stat-grid">
              <div class="nf-stat-tile">
                <div class="nf-stat-tile__label">订单总金额</div>
                <div class="nf-stat-tile__value"><span>{{ taskStat.sumMoney ? taskStat.sumMoney : 0 }}</span>元</div>
              </div>
              <div class="nf-stat-tile">
                <div class="nf-stat-tile__label">订单数量</div>
                <div class="nf-stat-tile__value"><span>{{ taskStat.countOrder ? taskStat.countOrder : 0 }}</span>单</div>
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
        </el-tab-pane>
        <el-tab-pane label="用户分析" name="yonghu">
          <div class="nf-table-card nf-home-tab-card">
            <div class="nf-home-stat-grid nf-home-stat-grid--compact">
              <div class="nf-stat-tile">
                <div class="nf-stat-tile__label">总用户</div>
                <div class="nf-stat-tile__value"><span>{{ tableDataNew.allUserCount ? tableDataNew.allUserCount : 0 }}</span>人</div>
              </div>
              <div class="nf-stat-tile">
                <div class="nf-stat-tile__label">手机号用户</div>
                <div class="nf-stat-tile__value"><span>{{ tableDataNew.phoneUserCount ? tableDataNew.phoneUserCount : 0 }}</span>人</div>
              </div>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="门店统计" name="mendian">
          <div class="nf-table-card nf-home-tab-card">
            <div class="nf-home-stat-grid nf-home-stat-grid--compact">
              <div class="nf-stat-tile">
                <div class="nf-stat-tile__label">新增门店</div>
                <div class="nf-stat-tile__value"><span>{{ tableData4.newShopCount ? tableData4.newShopCount : 0 }}</span>个</div>
              </div>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="订单收入分析" name="fourth">
          <div class="nf-table-card nf-home-tab-card">
            <el-table v-loading="tableDataLoading" :data="tableData2.list">
              <el-table-column prop="shopName" label="商户名">
              </el-table-column>
              <el-table-column prop="userName" label="下单用户">
                <template slot-scope="scope">
                  <span class="nf-link-text" @click="updates2(scope.row)">{{ scope.row.userName ? scope.row.userName : '暂无' }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="payMoney" label="支付金额">
              </el-table-column>
              <el-table-column prop="payTime" label="创建时间">
              </el-table-column>
            </el-table>
            <div class="nf-home-pagination">
              <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :page-sizes="[10, 20, 30, 50, 100]"
                :page-size="limit"
                :current-page="page"
                layout="total,sizes, prev, pager, next"
                :total="tableData2.totalCount">
              </el-pagination>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="门店收益排行" name="mendianTop">
          <div class="nf-table-card nf-home-tab-card">
            <el-table v-loading="tableDataLoading" :data="tableData3.list">
              <el-table-column prop="shopId" label="编号" width="80">
              </el-table-column>
              <el-table-column prop="shopName" label="门店名称">
              </el-table-column>
              <el-table-column prop="rank" label="排名">
              </el-table-column>
              <el-table-column prop="shopMoney" label="收益" width="200">
              </el-table-column>
              <el-table-column prop="payTime" label="操作">
                <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="updates3(scope.row)">商品列表</el-button>
                  <el-button size="mini" type="primary" @click="updates4(scope.row)">订单列表</el-button>
                  <el-button size="mini" type="primary" @click="updates(scope.row)">收入统计</el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="nf-home-pagination">
              <el-pagination
                @size-change="handleSizeChange1"
                @current-change="handleCurrentChange1"
                :page-sizes="[10, 20, 30, 50, 100]"
                :page-size="limit"
                :current-page="page"
                layout="total,sizes, prev, pager, next"
                :total="tableData3.totalCount">
              </el-pagination>
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
        limit: 10,
        page: 1,
        tableData: {},
        tableDataNew:{},
        tableData2: [],
        tableData3: {},
        tableData4:{},
        colonelData: [],
        commodityData: [],
        userjfData: [],
        incomeData: [],
        activeName: 'first',
        tableDataLoading: true,
        flag: 'day',
        usersumData: {},
        taskStat: {},
        mendianStat:{},
        data: '',
        time: '',
        platform: '',
        type: '',
        balance: -1,
        flag: 'day',
        info: {
          stockDate: this.getNowTime(), //日期
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
      }
    },
    mounted() {
      this.colonel()
      this.taskData()
      this._getData() //用户统计
      this._getData2()
      this.colonel1()
    },

    methods: {
      // 收益详情跳转
      updates(row) {
        this.$router.push({
          path: '/shopDatas',
          query: {
            shopId: row.shopId
          }
        })
      },
      // 商品列表跳转
      updates3(row) {
        this.$router.push({
          path: '/shopsListAdmin',
          query: {
            shopId: row.shopId
          }
        })
      },
      // 订单列表跳转
      updates4(row) {
        this.$router.push({
          path: '/shopsOrder',
          query: {
            shopId: row.shopId
          }
        })
      },
      //
      updates1(row) {
        this.$router.push({
          path: '/userDetail',
          query: {
            userId: row.userId1
          }
        })
      },
      updates2(row) {
        this.$router.push({
          path: '/userDetail',
          query: {
            userId: row.userId
          }
        })
      },
      handleSizeChange(val) {
        this.limit = val;
        this.colonel();
      },
      handleCurrentChange(val) {
        this.page = val;
        this.colonel();
      },
      handleSizeChange1(val) {
        this.limit = val;
        this.colonel1();
      },
      handleCurrentChange1(val) {
        this.page = val;
        this.colonel1();
      },
      // 用户分析选择日期
      animeDat() {
        this._getData()
      },
      // 订单分析选择日期
      animeOrder() {
        this.taskData()
        // this.mendianData()
        this._getData2()
        this.colonel()
        this.colonel1()
      },
      // 订单分析年月日
      orderfenxi(value) {
        let vanumber = value
        if (vanumber === 'day') {
          this.flag = 'day'
          this.taskData()
          this.colonel()
          // this.mendianData()
          this.colonel1()
          this._getData2()
        }
        if (vanumber === 'month') {
          this.flag = 'month'
          this.taskData()
          this.colonel()
          // this.mendianData()
          this.colonel1()
          this._getData2()
        }
        if (vanumber === 'year') {
          this.flag = 'year'
          this.taskData()
          this.colonel()
          // this.mendianData()
          this.colonel1()
          this._getData2()
        }
      },
      // Tabs点击事件
      handleClick(tab, event) {
        this.page = 1
        this.limit = 10
        if (tab._props.label == '订单分析') {
          this.taskData()
        }
        if (tab._props.label == '订单收入分析') {
          this.colonel()
        }
        if (tab._props.label == '门店统计') {
          this.colonel1()
        }
        if (tab._props.label == '门店收益排行') {
          this.colonel1()
        }
        if (tab._props.label == '用户分析') {
          this._getData2()
        }

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
      // 用户统计
      _getData() {
        this.$http({
          url: this.$http.adornUrl('admin/dataCentre/dataCentre'),
          method: 'get',
          params: this.$http.adornParams({})
        }).then(({
          data
        }) => {
          if (data.code == 0) {
            let returnData = data.data;
            this.tableData = returnData
          }

        })
      },
      // 新增统计
      _getData2() {
        this.$http({
          url: this.$http.adornUrl('admin/dataCentre/selectUserCenter'),
          method: 'get',
          params: this.$http.adornParams({
            'date': this.info.stockDate,
            'dateType': this.flag,
          })
        }).then(({
          data
        }) => {
          if (data.code == 0) {
            let returnData = data.data;
            this.tableDataNew = returnData
          }
        })
      },
      // 收入统计
      incomeSelect() {
        this.$http({
          url: this.$http.adornUrl('statistical/income'),
          method: 'get',
          params: this.$http.adornParams({})
        }).then(({
          data
        }) => {
          if (data.code == 0) {
            let returnData = data.data;
            this.incomeData = returnData
          }
        })
      },
      //订单分析
      taskData() {
        this.$http({
          url: this.$http.adornUrl('admin/dataCentre/selectOrderData'),
          method: 'get',
          params: this.$http.adornParams({
            'date': this.info.stockDate,
            'dateType': this.flag,
          })
        }).then(({
          data
        }) => {
          let returnData = data.data;
          this.taskStat = returnData
        })
      },
      // 门店统计
      mendianData() {
        this.$http({
          url: this.$http.adornUrl('admin/dataCentre/selectShopCenter'),
          method: 'get',
          params: this.$http.adornParams({
            'date': this.info.stockDate,
            'dateType': this.flag,
          })
        }).then(({
          data
        }) => {
          let returnData = data.data;
          this.mendianStat = returnData
        })
      },
      // 订单收入分析
      colonel() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('admin/dataCentre/selectOrderAnalyze'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit,
            'date': this.info.stockDate,
            'dateType': this.flag,
          })
        }).then(({
          data
        }) => {
          if (data.code == 0) {
            this.tableDataLoading = false
            let returnData = data.data
            this.tableData2 = returnData
          }
        })
      },
      // 门店排行
      colonel1() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('admin/dataCentre/selectShopCenter'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit,
            'date': this.info.stockDate,
            'dateType': this.flag,
          })
        }).then(({
          data
        }) => {
          if (data.code == 0) {
            this.tableDataLoading = false
            let returnData = data.data.pageUtils
            this.tableData3 = returnData
            this.tableData4 = data.data
          }
        })
      },
    }
  }
</script>

<style scoped>
.nf-home-page {
  padding: 0;
  overflow-x: hidden;
}

/* 顶区 hero + 筛选 */
.nf-home-top {
  display: flex;
  gap: 12px;
  margin-bottom: 12px;
  align-items: stretch;
}

.nf-home-hero {
  flex: 1;
  min-width: 0;
  margin-bottom: 0;
}

.nf-home-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-home-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-home-filter {
  width: 320px;
  flex-shrink: 0;
  margin-bottom: 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.nf-home-filter__body {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.nf-home-filter__select,
.nf-home-filter__date {
  width: 100% !important;
}

/* KPI 四列 */
.nf-home-kpi {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  margin-bottom: 12px;
}

.nf-kpi-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: #fff;
  border-radius: 8px;
  border: 1px solid var(--nf-border, #e8eaed);
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.nf-kpi-card__icon {
  width: 44px;
  height: 44px;
  border-radius: 8px;
  background: var(--nf-brand-lite, #fff5f8);
  color: var(--nf-brand, #ffa8c2);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  flex-shrink: 0;
}

.nf-kpi-card__body {
  min-width: 0;
}

.nf-kpi-card__label {
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.nf-kpi-card__value {
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
}

.nf-kpi-card__value span {
  font-size: 22px;
  font-weight: 600;
  color: var(--nf-brand, #ffa8c2);
  margin-right: 2px;
}

/* 双列明细格 */
.nf-home-detail {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  margin-bottom: 12px;
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

/* tabs 区 */
.nf-home-tabs-wrap {
  margin-bottom: 0;
}

.nf-home-tabs {
  margin-bottom: 0;
}

.nf-home-tabs >>> .el-tabs__header {
  margin-bottom: 0;
  background: #fff;
  border-radius: 8px 8px 0 0;
  padding: 0 16px;
  border: 1px solid var(--nf-border, #e8eaed);
  border-bottom: none;
}

.nf-home-tab-card {
  margin-bottom: 0;
  border-radius: 0 0 8px 8px;
  border-top: none;
}

.nf-home-stat-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.nf-home-stat-grid--compact {
  grid-template-columns: repeat(2, 1fr);
  max-width: 600px;
}

.nf-link-text {
  color: var(--nf-brand, #ffa8c2);
  cursor: pointer;
}

.nf-link-text:hover {
  color: var(--nf-brand-deep, #ff759e);
}

.nf-home-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.nf-home-tab-card .el-button + .el-button {
  margin-left: 8px;
}

@media (max-width: 1200px) {
  .nf-home-kpi {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .nf-home-top {
    flex-direction: column;
  }

  .nf-home-filter {
    width: 100%;
  }

  .nf-home-kpi {
    grid-template-columns: 1fr;
  }

  .nf-home-detail,
  .nf-home-stat-grid {
    grid-template-columns: 1fr;
  }
}
</style>
