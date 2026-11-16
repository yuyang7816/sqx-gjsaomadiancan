<template>
	<div class="nf-page nf-integral-detail-page">
		<div class="nf-page-hero nf-integral-detail-hero">
			<a href="#" class="nf-integral-detail-back" @click.prevent="prev">
				<icon-svg name="jiantou" class="nf-integral-detail-back__icon"></icon-svg>
				返回
			</a>
			<span class="nf-page-badge">积分</span>
			<h2 class="nf-integral-detail-hero__title">活动详情</h2>
			<p class="nf-integral-detail-hero__desc">查看积分推送活动的领取明细，包含用户昵称、活动名称、描述、金额与创建时间</p>
		</div>

		<div class="nf-integral-detail-tabs-wrap">
			<el-tabs v-model="activeName" class="nf-integral-detail-tabs">
				<el-tab-pane label="领取明细" name="list">
					<div class="nf-table-card nf-integral-detail-table-card">
						<el-table
							v-loading="tableDataLoading"
							:data="tableData.list">
							<el-table-column
								prop="nickName"
								label="昵称"
								width="150">
								<template slot-scope="scope">
									<span class="nf-integral-detail-user-link">{{ scope.row.nickName ? scope.row.nickName : '未绑定' }}</span>
								</template>
							</el-table-column>
							<el-table-column
								prop="integralName"
								label="活动名称"
								width="150">
							</el-table-column>
							<el-table-column
								prop="describes"
								label="活动描述">
							</el-table-column>
							<el-table-column
								prop="money"
								label="活动金额">
							</el-table-column>
							<el-table-column
								prop="createTime"
								label="创建时间"
								width="160">
							</el-table-column>
						</el-table>
						<div class="nf-integral-detail-pagination">
							<el-pagination
								@size-change="handleSizeChange"
								@current-change="handleCurrentChange"
								:page-sizes="[10, 15, 20, 25]"
								:page-size="limit"
								:current-page="page"
								layout="total,sizes, prev, pager, next"
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
    data () {
      return {
        limit: 10,
        page: 1,
        activeName: 'list',
        tableDataLoading: true,
        tableData: []
      }
    },
    methods: {
		// 返回上一级
		prev(){
			this.$router.back()
		},
		// 详情跳转
		updates (row) {
		  this.$router.push({path: '/userDetail', query: {userId: row.userId}})
		},
		handleSizeChange (val) {
			this.limit = val
			this.dataSelect()
		},
        handleCurrentChange (val) {
			this.page = val
			this.dataSelect()
        },
      // 获取数据列表
      dataSelect () {
		let sendMoneyId = this.$route.query.sendMoneyId
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('integral/selectSendMoneyDetailsList'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.page,
            'limit': this.limit,
            'sendMoneyId':sendMoneyId,
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
.nf-integral-detail-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-integral-detail-hero {
  margin-bottom: 12px;
}

.nf-integral-detail-back {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  margin-bottom: 8px;
  font-size: 14px;
  color: var(--nf-text-secondary, #909399);
  text-decoration: none;
  transition: color 0.2s;
}

.nf-integral-detail-back:hover {
  color: var(--nf-brand, #ffa8c2);
}

.nf-integral-detail-back__icon {
  width: 1.2em;
  height: 1.2em;
}

.nf-integral-detail-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-integral-detail-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-integral-detail-tabs-wrap {
  margin-bottom: 0;
}

.nf-integral-detail-tabs {
  margin-bottom: 0;
}

.nf-integral-detail-tabs >>> .el-tabs__header {
  margin-bottom: 0;
  background: #fff;
  border-radius: 8px 8px 0 0;
  padding: 0 16px;
  border: 1px solid var(--nf-border, #e8eaed);
  border-bottom: none;
}

.nf-integral-detail-table-card {
  margin-bottom: 0;
  border-radius: 0 0 8px 8px;
  border-top: none;
}

.nf-integral-detail-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.nf-integral-detail-user-link {
  color: var(--nf-brand, #ffa8c2);
  cursor: pointer;
}
</style>
