<template>
  <div class="nf-page nf-couponlist-page">
    <div class="nf-page-hero nf-couponlist-hero">
      <span class="nf-page-badge">优惠券</span>
      <h2 class="nf-couponlist-hero__title">优惠券领取列表</h2>
      <p class="nf-couponlist-hero__desc">查看用户已领取的优惠券记录，支持按状态、手机号筛选查询</p>
    </div>

    <div class="nf-filter-card nf-couponlist-filter">
      <div class="nf-filter-card__head">
        <i class="el-icon-search"></i>
        <span>筛选条件</span>
      </div>
      <div class="nf-filter-card__body nf-couponlist-filter__body">
        <div class="nf-couponlist-filter__item">
          <span class="nf-couponlist-filter__label">状态：</span>
          <el-select v-model="state" class="nf-couponlist-filter__select" @change="animeDat(state)">
            <el-option v-for="item in typeList" :key="item.value" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </div>
        <div class="nf-couponlist-filter__item">
          <span class="nf-couponlist-filter__label">手机号：</span>
          <el-input
            class="nf-couponlist-filter__input"
            @keydown.enter.native="animeDat"
            placeholder="请输入手机号"
            v-model="phone">
          </el-input>
        </div>
        <div class="nf-couponlist-filter__actions">
          <el-button size="mini" type="primary" icon="document" @click="animeDat">查询</el-button>
          <el-button size="mini" type="primary" icon="document" @click="cleans">重置</el-button>
        </div>
      </div>
    </div>

    <div class="nf-couponlist-tabs-wrap">
      <el-tabs v-model="activeName" @tab-click="handleClick" class="nf-couponlist-tabs">
        <el-tab-pane label="优惠券列表" name="seventh">
          <div class="nf-table-card nf-couponlist-table-card">
            <el-table v-loading="tableDataLoading" :data="tableData.list">
              <el-table-column fixed prop="id" label="编号" width="80">
              </el-table-column>
              <el-table-column fixed prop="userName" label="昵称" width="150">
                <template slot-scope="scope">
                  <span class="nf-couponlist-user-link" @click="updataDetails(scope.row)">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
                </template>
              </el-table-column>
              <el-table-column prop="phone" label="手机号">
              </el-table-column>
              <el-table-column prop="couponName" label="优惠券名称">
              </el-table-column>
              <el-table-column prop="couponPicture" label="图片">
                <template slot-scope="scope">
                  <img :src="scope.row.couponPicture" alt="" class="nf-couponlist-thumb">
                </template>
              </el-table-column>
              <el-table-column prop="money" label="优惠券金额">
              </el-table-column>
              <el-table-column prop="minMoney" label="最低消费">
              </el-table-column>
              <el-table-column prop="needIntegral" label="兑换积分">
              </el-table-column>
              <el-table-column prop="createTime" label="领取时间">
              </el-table-column>
              <el-table-column prop="expirationTime" label="过期时间">
              </el-table-column>
              <el-table-column prop="expirationTime" label="状态">
                <template slot-scope="scope">
                  <span v-if="scope.row.status==0">待使用</span>
                  <span v-else-if="scope.row.status==1">已使用</span>
                  <span v-else-if="scope.row.status==2">已失效</span>
                </template>
              </el-table-column>
            </el-table>
            <div class="nf-couponlist-pagination">
              <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :page-sizes="[10, 20, 30,50,100]" :page-size="limit" :current-page="page" layout="total,sizes, prev, pager, next"
                :total="tableData.totalCount">
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
        state: '',
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
        phone: '',
        content: '',
        activeName: 'seventh',
        tableDataLoading: false,
        dialogFormVisible1: false,
        dialogFormVisible2: false,
        dialogFormVisible: false,
        tableData: [],
        typeList:[
          {
            value:'',
            label:'全部'
          },
          {
            value:0,
            label:'待使用'
          },
          {
            value:1,
            label:'已使用'
          },
          {
            value:2,
            label:'已失效'
          }
        ],
        phone:'',
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

        if (tab._props.label == '优惠券列表') {
          this.page = 1
          this.limit = 10
          this.state = ''
          this.dataSelect()
        }
      },
      // 获取数据列表
      dataSelect() {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('admin/coupon/couponList'),
          method: 'get',
          params: this.$http.adornParams({
            'page':this.page,
            'limit':this.limit,
            'userId':'',
            'status':this.state,
            'phone':this.phone
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data;
          this.tableData = returnData
        })
      },
      animeDat(){
        this.page = 1
        this.dataSelect()
      },
      cleans(){
        this.page = 1
        this.phone = ''
        this.state = ''
        this.dataSelect()
      }
    },
    mounted() {
      this.dataSelect()
    }
  };
</script>

<style scoped>
.nf-couponlist-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-couponlist-hero {
  margin-bottom: 12px;
}

.nf-couponlist-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-couponlist-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-couponlist-filter {
  margin-bottom: 12px;
}

.nf-couponlist-filter__body {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
}

.nf-couponlist-filter__item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nf-couponlist-filter__label {
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
  white-space: nowrap;
}

.nf-couponlist-filter__select {
  width: 150px;
}

.nf-couponlist-filter__input {
  width: 200px;
}

.nf-couponlist-filter__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-left: auto;
}

.nf-couponlist-tabs-wrap {
  margin-bottom: 0;
}

.nf-couponlist-tabs {
  margin-bottom: 0;
}

.nf-couponlist-tabs >>> .el-tabs__header {
  margin-bottom: 0;
  background: #fff;
  border-radius: 8px 8px 0 0;
  padding: 0 16px;
  border: 1px solid var(--nf-border, #e8eaed);
  border-bottom: none;
}

.nf-couponlist-table-card {
  margin-bottom: 0;
  border-radius: 0 0 8px 8px;
  border-top: none;
}

.nf-couponlist-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.nf-couponlist-thumb {
  width: 40px;
  height: 40px;
  object-fit: cover;
  border-radius: 4px;
}

.nf-couponlist-user-link {
  color: var(--nf-brand, #ffa8c2);
  cursor: pointer;
}
</style>
