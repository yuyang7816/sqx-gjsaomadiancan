<template>
	<div class="nf-page nf-missiondetails-page">
		<div class="nf-page-hero nf-missiondetails-hero">
			<a href="#" class="nf-missiondetails-back" @click.prevent="prev">
				<icon-svg name="jiantou" class="nf-missiondetails-back__icon"></icon-svg>
				返回
			</a>
			<span class="nf-page-badge">任务</span>
			<h2 class="nf-missiondetails-hero__title">派单详情</h2>
			<p class="nf-missiondetails-hero__desc">查看派单任务基本信息与接单记录，支持查看接单提交内容与审核状态</p>
		</div>

		<div class="nf-table-card nf-missiondetails-info-card">
			<div class="nf-filter-card__head nf-missiondetails-info-card__head">
				<i class="el-icon-document"></i>
				<span>派单信息</span>
			</div>
			<div class="nf-missiondetails-info-table">
				<table>
					<tbody>
						<tr>
							<th>用户名称</th>
							<td>{{ tableData.nickName }}</td>
							<th>标题</th>
							<td>{{ tableData.title }}</td>
							<th class="nf-missiondetails-info-table__wide-th">任务要求</th>
							<td class="border-rt">{{ tableData.content }}</td>
						</tr>
						<tr>
							<th>验证信息</th>
							<td>{{ tableData.verifyContent }}</td>
							<th>打开内容</th>
							<td class="nf-missiondetails-info-table__wide-td">{{ tableData.openContent }}</td>
							<th>编号</th>
							<td class="border-rt">{{ tableData.id }}</td>
						</tr>
						<tr>
							<th>数量</th>
							<td>{{ tableData.taskNum }}</td>
							<th>接单数量</th>
							<td>{{ tableData.endNum }}</td>
							<th>发布时间</th>
							<td class="border-rt">{{ tableData.createTime }}</td>
						</tr>
						<tr>
							<th>截至时间</th>
							<td>{{ tableData.endTime }}</td>
							<th>审核时间（按分）</th>
							<td>{{ tableData.auditTime }}</td>
							<th>任务限时（按分）</th>
							<td class="border-rt">{{ tableData.restrictTime }}</td>
						</tr>
						<tr>
							<th>状态</th>
							<td>
								<span class="nf-missiondetails-status-text" v-if="tableData.state == 0">待审核</span>
								<span class="nf-missiondetails-status-text" v-if="tableData.state == 1">进行中</span>
								<span class="nf-missiondetails-status-text" v-if="tableData.state == 2">已结束</span>
								<span class="nf-missiondetails-status-text" v-if="tableData.state == 3">已拒绝</span>
								<span class="nf-missiondetails-status-text" v-if="tableData.state == 4">已结算</span>
							</td>
							<th>打开方式</th>
							<td>
								<span v-if="tableData.openType == 1">链接打开</span>
								<span v-if="tableData.openType == 2">口令打开</span>
								<span v-if="tableData.openType == 3">扫码打开</span>
							</td>
							<th>打开app分类</th>
							<td class="border-rt">{{ tableData.openApp }}</td>
						</tr>
						<tr>
							<th class="border-bt">验证信息</th>
							<td class="border-bt">{{ tableData.verifyContent }}</td>
							<th class="border-bt">价格(原价)</th>
							<td class="border-bt" colspan="3">{{ tableData.taskOriginalPrice }}</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>

		<div class="nf-table-card nf-missiondetails-table-card">
			<div class="nf-filter-card__head nf-missiondetails-table-card__head">
				<i class="el-icon-tickets"></i>
				<span>接单列表</span>
			</div>
			<el-table
				v-loading="tableDataLoading"
				:data="taskData.list">
				<el-table-column
					fixed
					prop="id"
					label="接单id">
				</el-table-column>
				<el-table-column
					prop="nickName"
					label="接单人名称"
					width="150">
				</el-table-column>
				<el-table-column
					prop="content"
					label="提交内容"
					width="150">
					<template slot-scope="scope">
						<span>{{ scope.row.content }}</span>
					</template>
				</el-table-column>
				<el-table-column
					prop="money"
					label="价格(接单时收取的价格)"
					width="180">
				</el-table-column>
				<el-table-column
					prop="zhiMoney"
					label="直属收益">
				</el-table-column>
				<el-table-column
					prop="feiMoney"
					label="非直属收益"
					width="150">
				</el-table-column>
				<el-table-column
					prop="pingMoney"
					label="平台收益">
				</el-table-column>
				<el-table-column
					prop="auditContent"
					label="拒绝原因"
					width="160">
				</el-table-column>
				<el-table-column
					prop="category"
					label="拒绝类型"
					width="160">
				</el-table-column>
				<el-table-column
					prop="createTime"
					label="创建时间"
					width="160">
				</el-table-column>
				<el-table-column
					prop="auditTime"
					label="审核时间"
					width="160">
				</el-table-column>
				<el-table-column
					fixed="right"
					prop="state"
					label="状态"
					width="100">
					<template slot-scope="scope">
						<span class="nf-missiondetails-status-text" v-if="scope.row.state === 0">接单成功</span>
						<span class="nf-missiondetails-status-text" v-if="scope.row.state === 1">提交待审核</span>
						<span class="nf-missiondetails-status-text" v-if="scope.row.state === 2">审核成功</span>
						<span class="nf-missiondetails-status-text" v-if="scope.row.state === 3">已拒绝</span>
						<span class="nf-missiondetails-status-text" v-if="scope.row.state === 4">已维权</span>
						<span class="nf-missiondetails-status-text" v-if="scope.row.state === 5">已放弃</span>
					</template>
				</el-table-column>
				<el-table-column
					fixed="right"
					label="操作">
					<template slot-scope="scope">
						<el-button
							size="mini"
							type="primary"
							@click="check(scope.$index, scope.row)">查看
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="nf-missiondetails-pagination">
				<el-pagination
					@size-change="handleSizeChange"
					@current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]"
					:page-size="limit"
					:current-page="page"
					layout="total,sizes, prev, pager, next"
					:total="taskData.totalCount">
				</el-pagination>
			</div>
		</div>

		<el-dialog title="查看" :visible.sync="dialogFormVisible" center>
			<div v-for="(item, index) in helpTaskData" :key="index" style="text-align: center;">
				<span>步骤 {{ index + 1 }}</span>
				<p class="red">验证图</p>
				<img :src="item.picture" alt="" width="260" height="460">
			</div>
			<div v-if="helpTaskData == ''" style="text-align: center;">暂无内容</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="dialogFormVisible = false">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
	    data() {
	        return {
				state:0,
				limit:5,
				page:1,
				dialogFormVisible:false,
				tableDataLoading:true,
				tableData:{},
			    taskData:[],
				helpTaskData:[]
	        }
	    },
	    methods: {
			prev(){
				this.$router.back()
			},
			handleSizeChange(val) {
				this.limit = val;
				this.taskSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.taskSelect()
			},
			check(index,rows){
				this.$http({
					url: this.$http.adornUrl('helpTask/selectSendOrderDetailsById'),
					method: 'get',
					params: this.$http.adornParams({
						'id':rows.id
					})
				}).then(({data}) => {
					if (data.code === 0) {
						let returnData = data.data;
						this.dialogFormVisible = true
						this.helpTaskData = returnData
					}
				})
				
			},
			dataSelect () {
				let id = this.$route.query.id
				this.$http({
					url: this.$http.adornUrl('helpTask/selectTakeOrderDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'id':id
					})
				}).then(({data}) => {
					if (data.code === 0) {
						let returnData = data.data;
						this.tableData = returnData;
					}
				})
			},
			taskSelect () {
				this.tableDataLoading = true
				let page = this.page-1
				let id = this.$route.query.id
				this.$http({
					url: this.$http.adornUrl('helpTask/selectSendOrderByTaskList'),
					method: 'get',
					params: this.$http.adornParams({
						'page':page,
						'limit':this.limit,
						'id':id
					})
				}).then(({data}) => {
					if (data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.taskData = returnData;
					}
				})
			},
	    },
		mounted() {
			this.dataSelect()
			this.taskSelect()
		},
	}
</script>

<style scoped>
.nf-missiondetails-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-missiondetails-hero {
  margin-bottom: 12px;
}

.nf-missiondetails-back {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  margin-bottom: 8px;
  font-size: 14px;
  color: var(--nf-text-secondary, #909399);
  text-decoration: none;
  transition: color 0.2s;
}

.nf-missiondetails-back:hover {
  color: var(--nf-brand, #ffa8c2);
}

.nf-missiondetails-back__icon {
  width: 1.2em;
  height: 1.2em;
}

.nf-missiondetails-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-missiondetails-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-missiondetails-info-card {
  margin-bottom: 12px;
}

.nf-missiondetails-info-card__head,
.nf-missiondetails-table-card__head {
  margin-bottom: 12px;
}

.nf-missiondetails-info-table {
  border: 1px solid var(--nf-border, #e8eaed);
  border-radius: 6px;
  overflow: hidden;
}

.nf-missiondetails-info-table table {
  width: 100%;
  border-collapse: collapse;
}

.nf-missiondetails-info-table tr {
  border-bottom: 1px solid var(--nf-border, #e8eaed);
}

.nf-missiondetails-info-table th {
  background-color: var(--nf-sidebar-bg-alt, #f7f8fa);
  padding: 12px 16px;
  border-right: 1px solid var(--nf-border, #e8eaed);
  border-bottom: 1px solid var(--nf-border, #e8eaed);
  color: var(--nf-text-primary, rgba(0, 0, 0, 0.85));
  font-weight: 500;
  font-size: 13px;
  line-height: 1.5;
  white-space: nowrap;
}

.nf-missiondetails-info-table td {
  padding: 12px 16px;
  border-right: 1px solid var(--nf-border, #e8eaed);
  border-bottom: 1px solid var(--nf-border, #e8eaed);
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
  line-height: 1.5;
  word-break: break-word;
}

.nf-missiondetails-info-table__wide-th {
  width: 180px;
}

.nf-missiondetails-info-table__wide-td {
  width: 180px;
}

.border-rt {
  border-right: none !important;
}

.border-bt {
  border-bottom: none !important;
}

.nf-missiondetails-table-card {
  margin-bottom: 0;
}

.nf-missiondetails-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.nf-missiondetails-status-text {
  color: var(--nf-brand, #ffa8c2);
}
</style>
