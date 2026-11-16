<template>
	<div class="nf-page nf-shopmoney-page">
		<div class="nf-page-hero nf-shopmoney-hero">
			<a href="#" class="nf-shopmoney-back" @click.prevent="prev">
				<icon-svg name="jiantou" class="nf-shopmoney-back__icon"></icon-svg>
				返回
			</a>
			<span class="nf-page-badge">收银台</span>
			<h2 class="nf-shopmoney-hero__title">收银台统计</h2>
			<p class="nf-shopmoney-hero__desc">查看收银台年/月/日收益及明细记录，支持按日期区间筛选查询</p>
		</div>

		<div class="nf-shopmoney-kpi">
			<div class="nf-kpi-card">
				<div class="nf-kpi-card__icon"><i class="el-icon-date"></i></div>
				<div class="nf-kpi-card__body">
					<div class="nf-kpi-card__label">年收益</div>
					<div class="nf-kpi-card__value"><span>{{ taskStat.cashierYearMoney ? taskStat.cashierYearMoney : 0 }}</span>元</div>
				</div>
			</div>
			<div class="nf-kpi-card">
				<div class="nf-kpi-card__icon"><i class="el-icon-tickets"></i></div>
				<div class="nf-kpi-card__body">
					<div class="nf-kpi-card__label">月收益</div>
					<div class="nf-kpi-card__value"><span>{{ taskStat.cashierMonthMoney ? taskStat.cashierMonthMoney : 0 }}</span>元</div>
				</div>
			</div>
			<div class="nf-kpi-card">
				<div class="nf-kpi-card__icon"><i class="el-icon-coin"></i></div>
				<div class="nf-kpi-card__body">
					<div class="nf-kpi-card__label">日收益</div>
					<div class="nf-kpi-card__value"><span>{{ taskStat.cashierDayMoney ? taskStat.cashierDayMoney : 0 }}</span>元</div>
				</div>
			</div>
		</div>

		<div class="nf-filter-card nf-shopmoney-filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body nf-shopmoney-filter__body">
				<div class="nf-shopmoney-filter__summary">
					<span class="nf-shopmoney-filter__summary-label">收益：</span>
					<span class="nf-shopmoney-filter__summary-value">¥{{ zongMoney }}</span>
				</div>
				<el-date-picker
					v-model="times"
					value-format="yyyy-MM-dd"
					class="nf-shopmoney-filter__date"
					type="daterange"
					range-separator="至"
					start-placeholder="开始日期"
					end-placeholder="结束日期"
					@change="selectTime">
				</el-date-picker>
				<div class="nf-shopmoney-filter__actions">
					<el-button size="mini" type="primary" icon="document" @click="result()">重置</el-button>
				</div>
			</div>
		</div>

		<div class="nf-table-card nf-shopmoney-table-card">
			<el-table v-loading="tableDataLoading" :data="list.records" height="400px">
				<el-table-column fixed prop="id" label="编号">
					<template slot-scope="scope">
						{{ scope.$index + 1}}
					</template>
				</el-table-column>
				<el-table-column fixed prop="title" label="标题">
				</el-table-column>
				<el-table-column fixed prop="money" label="金额">
				</el-table-column>
				<el-table-column fixed prop="content" label="内容">
				</el-table-column>
				<el-table-column fixed prop="createTime" label="时间">
				</el-table-column>
			</el-table>
			<div class="nf-shopmoney-pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 40, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="list.total">
				</el-pagination>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				times: '', //选择的时间区间
				limit: 5,
				page: 1,
				shopId: '',
				list: {},
				tableDataLoading: false,
				startTime: '', //开始时间
				endTime: '', //结束时间
				zongMoney: 0, //总价格
				seMoney: 0, //当前价格
				baifen: 0, //百分比
				taskStat:{},
			}
		},
		methods: {
			//重置日期筛选
			result() {
				this.page = 1
				this.times = ''
				this.startTime = ''
				this.endTime = ''
				this.baifen = 0
				this.zongMoney = 0
				this.seMoney = 0
				this.getList()
			},
			//日期选择
			selectTime(e) {
				this.startTime = e[0]
				this.endTime = e[1]
				this.getList()
			},
			//获取收银台明细数据
			getList() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/dataCentre/selectShopMoney'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'shopId': this.shopId,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'type': 1
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data.code == 0) {
						this.taskStat = data.data
					}
				})
				this.$http({
					url: this.$http.adornUrl('Details/queryUserMoneyDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'shopId': this.shopId,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'type': 1
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data.code == 0) {
						this.zongMoney = data.data.money
						this.list = data.data.pageUtils
					}
				})
			},
			//分页
			handleSizeChange(e) {
				this.limit = e
				this.getList()
			},
			handleCurrentChange(e) {
				this.page = e
				this.getList()
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
			this.getList()
		},
		watch: {
			'$route': 'taskdataSelect'

		}
	}
</script>

<style scoped>
.nf-shopmoney-page {
	padding: 0;
	overflow-x: hidden;
}

.nf-shopmoney-hero {
	margin-bottom: 12px;
}

.nf-shopmoney-back {
	display: inline-flex;
	align-items: center;
	gap: 4px;
	font-size: 13px;
	color: var(--nf-text-secondary, #909399);
	text-decoration: none;
	margin-bottom: 8px;
	transition: color 0.2s;
}

.nf-shopmoney-back:hover {
	color: var(--nf-brand, #ffa8c2);
}

.nf-shopmoney-back__icon {
	width: 1.2em;
	height: 1.2em;
}

.nf-shopmoney-hero__title {
	margin: 8px 0 4px;
	font-size: 18px;
	font-weight: 600;
	color: var(--nf-text-primary, #1a2433);
	line-height: 1.4;
}

.nf-shopmoney-hero__desc {
	margin: 0;
	font-size: 13px;
	color: var(--nf-text-secondary, #909399);
	line-height: 1.5;
}

.nf-shopmoney-kpi {
	display: grid;
	grid-template-columns: repeat(3, 1fr);
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

.nf-shopmoney-filter {
	margin-bottom: 12px;
}

.nf-shopmoney-filter__body {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 12px;
}

.nf-shopmoney-filter__summary {
	display: flex;
	align-items: baseline;
	gap: 4px;
}

.nf-shopmoney-filter__summary-label {
	font-size: 14px;
	color: var(--nf-text-regular, #303133);
}

.nf-shopmoney-filter__summary-value {
	font-size: 20px;
	font-weight: 600;
	color: var(--nf-brand, #ffa8c2);
}

.nf-shopmoney-filter__date {
	width: 400px;
	max-width: 100%;
}

.nf-shopmoney-filter__actions {
	display: flex;
	flex-wrap: wrap;
	gap: 8px;
	margin-left: auto;
}

.nf-shopmoney-table-card {
	margin-bottom: 0;
}

.nf-shopmoney-pagination {
	display: flex;
	justify-content: flex-end;
	margin-top: 12px;
}

@media (max-width: 992px) {
	.nf-shopmoney-kpi {
		grid-template-columns: 1fr;
	}
}
</style>
