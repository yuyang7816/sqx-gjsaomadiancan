<template>
  <div class="nf-page nf-fenxiaoorder-page">
    <div class="nf-page-hero nf-fenxiaoorder-hero">
      <span class="nf-page-badge">分销订单</span>
      <h2 class="nf-fenxiaoorder-hero__title">分销订单管理</h2>
      <p class="nf-fenxiaoorder-hero__desc">查看分销佣金订单记录，支持按分销状态、手机号筛选查询</p>
    </div>

    <div class="nf-filter-card nf-fenxiaoorder-filter">
      <div class="nf-filter-card__head">
        <i class="el-icon-search"></i>
        <span>筛选条件</span>
      </div>
      <div class="nf-filter-card__body nf-fenxiaoorder-filter__body">
        <div class="nf-fenxiaoorder-filter__item">
          <span class="nf-fenxiaoorder-filter__label">分销状态：</span>
          <el-select
            v-model="status"
            class="nf-fenxiaoorder-filter__select"
            placeholder="请选择分销类型"
            @change="selectTrigger(status)">
            <el-option
              v-for="item in statusnum"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div class="nf-fenxiaoorder-filter__item">
          <span class="nf-fenxiaoorder-filter__label">手机号：</span>
          <el-input
            class="nf-fenxiaoorder-filter__input"
            @keydown.enter.native="select"
            clearable
            placeholder="请输入手机号"
            v-model="phone">
          </el-input>
        </div>
        <div class="nf-fenxiaoorder-filter__actions">
          <el-button size="mini" type="primary" icon="document" @click="select">查询</el-button>
          <el-button size="mini" type="primary" icon="document" @click="cleans">重置</el-button>
        </div>
      </div>
    </div>

    <div class="nf-table-card nf-fenxiaoorder-table-card">
      <el-table v-loading="tableDataLoading" :data="tableData">
        <el-table-column prop="id" label="编号">
        </el-table-column>
        <el-table-column prop="userId" label="用户id">
          <template slot-scope="scope">
            <span class="nf-fenxiaoorder-user-link" @click="updates(scope.row)">{{ scope.row.userId }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号">
        </el-table-column>
        <el-table-column prop="commissionPrice" label="订单佣金">
        </el-table-column>
        <el-table-column prop="status" label="分销状态">
          <template slot-scope="scope">
            <span v-if="scope.row.status == 1">未到账</span>
            <span v-if="scope.row.status == 2">已到账</span>
          </template>
        </el-table-column>
        <el-table-column prop="detail" label="分销明细">
        </el-table-column>
        <el-table-column prop="createAt" label="创建时间">
        </el-table-column>
      </el-table>
      <div class="nf-fenxiaoorder-pagination">
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
  </div>
</template>
<script>
	export default {
    data () {
      return {
        size: 10,
        page: 1,
        userId: '',
        phone: '',
        totalnum: 0,
        moneyFrom: 0,
        formLabelWidth: '200px',
        activeName: 'first',
        tableDataLoading: false,
        tableData: [],
        status: 0,
        statusnum: [
          {
            value: 0,
            label: '全部'
          }, {
            value: 1,
            label: '未到账'
          }, {
            value: 2,
            label: '已到账'
          }],
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
      handleSizeChange (val) {
        this.size = val
        this.dataSelect()
      },
      handleCurrentChange (val) {
        this.page = val
        this.dataSelect()
      },
      // 详情跳转
      updates (row) {
        this.$router.push({path: '/userDetail', query: {userId: row.userId}})
      },
      // 详情跳转
      updates (row) {
        this.$router.push({path: '/userDetail', query: {userId: row.userId}})
      },
      selectTrigger () {
        this.size = 5
        this.page = 1
        this.dataSelect()
      },
      //搜索
      select () {
        this.page = 1
        this.size = 5
        this.dataSelect()
      },
      // 重置
      cleans () {
        this.status = 0
        this.phone = ''
        this.dataSelect()
      },
      // 获取订单数据
      dataSelect () {
        let page = this.page - 1
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('ordersRelation/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': page,
            'size': this.size,
            'userId': this.userId,
            'phone': this.phone,
            'status': this.status,
            'moneyFrom': this.moneyFrom
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
.nf-fenxiaoorder-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-fenxiaoorder-hero {
  margin-bottom: 12px;
}

.nf-fenxiaoorder-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-fenxiaoorder-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-fenxiaoorder-filter {
  margin-bottom: 12px;
}

.nf-fenxiaoorder-filter__body {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
}

.nf-fenxiaoorder-filter__item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nf-fenxiaoorder-filter__label {
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
  white-space: nowrap;
}

.nf-fenxiaoorder-filter__select {
  width: 150px;
}

.nf-fenxiaoorder-filter__input {
  width: 200px;
}

.nf-fenxiaoorder-filter__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-left: auto;
}

.nf-fenxiaoorder-table-card {
  margin-bottom: 0;
}

.nf-fenxiaoorder-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.nf-fenxiaoorder-user-link {
  color: var(--nf-brand, #ffa8c2);
  cursor: pointer;
}
</style>
