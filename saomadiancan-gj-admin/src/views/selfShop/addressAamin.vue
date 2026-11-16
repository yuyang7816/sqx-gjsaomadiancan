<template>
	<div class="nf-page nf-address-page">
		<!-- hero -->
		<div class="nf-page-hero nf-address-hero">
			<span class="nf-page-badge">地址管理</span>
			<h2 class="nf-address-hero__title">收货地址</h2>
			<p class="nf-address-hero__desc">查看用户收货人、手机号及详细地址等配送信息</p>
		</div>

		<!-- 表格白卡 -->
		<div class="nf-table-card nf-address-table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.content">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="consignee" label="收货人">
				</el-table-column>
				<el-table-column prop="mobile" label="手机号">
				</el-table-column>
				<el-table-column prop="detail" label="详细地址">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间" width="180">
				</el-table-column>
			</el-table>
			<div class="nf-address-pagination">
				<el-pagination
					@size-change="handleSizeChange"
					@current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]"
					:page-size="size"
					:current-page="page"
					layout="total,sizes, prev, pager, next,jumper"
					:total="tableData.totalElements">
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
        consignee: '',
        mobile: '',
        detail: '',
        form: {
          id: '',
          consignee: '',
          mobile: '',
          detail: ''
        },
        formLabelWidth: '200px',
        tableDataLoading: false,
        dialogFormVisible1: false,
        dialogFormVisible: false,
        tableData: [],
      }
    },
    methods: {
      //处理默认选中当前日期
      getNowTime () {
        var now = new Date()
        var year = now.getFullYear() //得到年份
        var month = now.getMonth() //得到月份
        var date = now.getDate() //得到日期
        var hh = now.getHours() < 10 ? '0' + now.getHours() : now.getHours()
        var mm = now.getMinutes() < 10 ? '0' + now.getMinutes() : now.getMinutes()
        var ss = now.getSeconds() < 10 ? '0' + now.getSeconds() : now.getSeconds()
        month = month + 1
        month = month.toString().padStart(2, '0')
        date = date.toString().padStart(2, '0')
        var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`
        return defaultDate
        this.$set(this.info, 'stockDate', defaultDate)
      },
      handleSizeChange (val) {
        this.size = val
        this.dataSelect()
      },
      handleCurrentChange (val) {
        this.page = val
        this.dataSelect()
      },
      // 获取地址数据
      dataSelect () {
        let page = this.page - 1
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('address/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': page,
            'size': this.size
          })
        }).then(({data}) => {
          this.tableDataLoading = false
          let returnData = data.data
          this.tableData = returnData
        })
      }
    },
    mounted () {
      this.dataSelect()
    }
  }
</script>

<style scoped>
.nf-address-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-address-hero {
  margin-bottom: 12px;
}

.nf-address-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-address-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-address-table-card {
  margin-bottom: 0;
}

.nf-address-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}
</style>
