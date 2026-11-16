<template>
	<div class="nf-page nf-tablecodesm-page">
		<div class="nf-page-hero nf-tablecodesm-hero">
			<span class="nf-page-badge">桌码</span>
			<h2 class="nf-tablecodesm-hero__title">桌码管理</h2>
			<p class="nf-tablecodesm-hero__desc">管理门店桌位与桌码，支持按状态、启用情况筛选，添加、修改与查看二维码</p>
		</div>

		<div class="nf-filter-card nf-tablecodesm-filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body nf-tablecodesm-filter__body">
				<div class="nf-tablecodesm-filter__item">
					<span class="nf-tablecodesm-filter__label">状态：</span>
					<el-select v-model="statusT" class="nf-tablecodesm-filter__select" @change="phoneSelect()">
						<el-option v-for="item in taskstates" :key="item.value" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-tablecodesm-filter__item">
					<span class="nf-tablecodesm-filter__label">是否启用：</span>
					<el-select v-model="orderType" class="nf-tablecodesm-filter__select" @change="phoneSelect()">
						<el-option v-for="item in orderTypes" :key="item.value" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-tablecodesm-filter__item">
					<span class="nf-tablecodesm-filter__label">桌位名称：</span>
					<el-input class="nf-tablecodesm-filter__input" @keydown.enter.native="phoneSelect" placeholder="请输入桌位名称"
						v-model="userName">
					</el-input>
				</div>
				<div class="nf-tablecodesm-filter__actions">
					<el-button size="mini" type="primary" icon="document" @click="phoneSelect">查询</el-button>
					<el-button size="mini" type="primary" icon="document" @click="cleans2">重置</el-button>
					<el-button size="mini" type="primary" :disabled="!isAuth('tableCodeSm:add')" icon="document" @click="add()">添加</el-button>
				</div>
			</div>
		</div>

		<div class="nf-tablecodesm-tabs-wrap">
			<el-tabs v-model="activeName" @tab-click="handleClick" class="nf-tablecodesm-tabs">
				<el-tab-pane label="桌码列表" name="first">
					<div class="nf-table-card nf-tablecodesm-table-card">
						<el-table v-loading="tableDataLoading" :data="tableData2.records">
							<el-table-column fixed prop="seatId" label="编号" width="80"></el-table-column>
							<el-table-column prop="seatName" label="桌位名称"></el-table-column>
							<el-table-column prop="sort" label="排序"></el-table-column>
							<el-table-column prop="isEnable" label="是否启用">
								<template slot-scope="scope">
									<span v-if="scope.row.isEnable === 1">启用</span>
									<span v-if="scope.row.isEnable === 0">不启用</span>
								</template>
							</el-table-column>
							<el-table-column fixed="right" prop="status" label="状态">
								<template slot-scope="scope">
									<span class="nf-tablecodesm-status--active" v-if="scope.row.status === 1">使用中</span>
									<span class="nf-tablecodesm-status--idle" v-if="scope.row.status === 2">空闲中</span>
								</template>
							</el-table-column>
							<el-table-column prop="createTime" label="创建时间" width="100"></el-table-column>
							<el-table-column label="桌码" width="100">
								<template slot-scope="scope">
									<el-button size="mini" type="primary" @click="erweima(scope.row)">查看</el-button>
								</template>
							</el-table-column>
							<el-table-column fixed="right" prop="status" label="操作" width="180">
								<template slot-scope="scope">
									<el-button size="mini" type="primary" :disabled="!isAuth('tableCodeSm:update')" @click="compile(scope.row)">
										修改</el-button>
									<el-button size="mini" type="danger" :disabled="!isAuth('tableCodeSm:delete')"
										@click="tuikuan(scope.row)">
										删除
									</el-button>
								</template>
							</el-table-column>
						</el-table>
						<div class="nf-tablecodesm-total" v-if="totalMoney">本页累计交易统计：{{totalMoney}}元</div>
						<div class="nf-tablecodesm-pagination">
							<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
								:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next" :total="tableData2.total">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>

		<!-- 添加/修改弹框 -->
		<el-dialog :title="titles" :visible.sync="dialogFormVisible7" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">桌位名称：</span>
				<el-input style="width:50%;" v-model="seatName" type="text" placeholder="请输入桌位名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
				<el-input style="width:50%;" v-model="sort" type="number" placeholder="请输入排序"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">是否启用：</span>
				<el-radio-group v-model="isEnable">
					<el-radio :label="1">启用</el-radio>
					<el-radio :label="0">不启用</el-radio>
				</el-radio-group>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible7 = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 店铺二维码 -->
		<el-dialog :visible.sync="dialogFormVisible2" center title="桌码">
			<div>
				<img :src="erweimaImg" style="width: 50%;margin-left: 25%;" />
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				totalMoney: 0,
				phone: '',
				statusT: '',
				describes: '',
				token: '',
				taskNum: '',
				taskMoney: '',
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
						label: '制作中',
						value: 6
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
				orderType: '',
				orderTypes: [{
						label: '全部',
						value: ''
					},
					{
						label: '启用',
						value: 1
					},
					{
						label: '不启用',
						value: 0
					},
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
				orderNumber: '',
				titles: '添加',
				seatName: '',
				isEnable: 1,
				status: 1,
				sort: '',
				seatId: '',
				erweimaImg:'',
			}
		},
		methods: {
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
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

				if (tab._props.label == '桌码列表') {
					this.page = 1
					this.limit = 10
					this.statusT = ''
					this.taskdataSelect()
				}
			},


			// 筛选
			phoneSelect() {
				this.page = 1
				this.limit = 10
				this.taskdataSelect()
			},
			// 重置
			cleans2() {
				this.phone = ''
				this.statusT = ''
				this.userName = ''
				this.orderNumber = ''
				this.orderType = ''
				this.page = 1
				this.taskdataSelect()
			},

			// 获取接单数据列表
			taskdataSelect() {
				// this.shopId = this.$route.query.shopId
				this.tableDataLoading = true
				this.totalMoney = 0
				this.$http({
					url: this.$http.adornUrl('admin/shopSeat/getShopSeatList'),
					method: 'get',
					params: this.$http.adornParams({
						// 'page': this.page,
						// 'limit': this.limit,
						// 'phone': this.phone,
						'seatName': this.userName,
						// 'sysUserId': this.orderNumber,
						'status': this.statusT,
						'shopId': this.shopId,
						'isEnable': this.orderType
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData2 = returnData
				})
			},

			// 餐品完成
			wancheng(row) {

				if (row.status == 3) {
					var status1 = 4
				}
				if (row.status == 6) {
					var status1 = 3
				}
				this.$http({
					url: this.$http.adornUrl('admin/order/updateOrder'),
					method: 'post',
					data: this.$http.adornData({
						'status': status1,
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
			// 删除
			tuikuan(row) {
				this.$confirm(`确定要取消退款?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					console.log('取定')
					this.$http({
						url: this.$http.adornUrl('admin/shopSeat/deleteShopSeatById'),
						method: 'get',
						params: this.$http.adornParams({
							'seatId': row.seatId
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
								type: 'error',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				}).catch(() => {
					console.log('取消')
				})

			},

			// 添加弹框
			add() {
				this.titles = '添加'
				this.seatName = ''
				this.isEnable = 1
				// this.status = 1
				this.sort = ''
				this.seatId = ''
				this.dialogFormVisible7 = true
			},
			// 修改一级分类
			compile(rows) {
				this.titles = '修改'
				this.seatName = rows.seatName
				this.isEnable = rows.isEnable
				// this.status = rows.status
				this.sort = rows.sort
				this.seatId = rows.seatId
				this.dialogFormVisible7 = true
			},
			// 修改一级分类确定
			refuseto() {
				if (this.seatName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入桌位名称',
						type: 'warning'
					})
					return
				}
				if (this.sort == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入排序',
						type: 'warning'
					})
					return
				}

				this.$http({
					url: this.$http.adornUrl('admin/shopSeat/insertSeat'),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'seatId': this.seatId,
						'seatName': this.seatName,
						'isEnable': this.isEnable,
						// 'status': this.status,
						'shopId': this.shopId,
						'sort': this.sort,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible7 = false
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
			// 查看二维码
			erweima(row){
			      // console.log('row.shopId',row.shopId)
			      // this.dialogFormVisible2 = true
			      // this.erweimaImg = this.$http.adornUrl('invite/shopSeatQr?shopId='+row.shopId+'&seatId='+row.seatId)

            this.$http({
            	url: this.$http.adornUrl('invite/shopSeatQr'),
            	method: 'get',
            	params: this.$http.adornParams({
            		'seatId': row.seatId,
            	})
            }).then(({
            	data
            }) => {
            	if (data) {
            		this.erweimaImg = data
            		this.dialogFormVisible2 = true
            		console.log(data, '获取二维码')
            	}else{
            		this.$message({
            			message:'生成二维码失败，请重试',
            			type: 'warning',
            			duration: 1500,
            			onClose: () => { }
            		})
            	}
            })

			},
			chakan(){
			  window.open(this.erweimaImg, '_blank');
			},
		},
		mounted() {
			// this.shopId = this.$route.query.shopId
			this.shopId = this.$cookie.get('shopId')
			this.taskdataSelect()

		},
		watch: {
			'$route': 'taskdataSelect'
		}
	}
</script>

<style scoped>
.nf-tablecodesm-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-tablecodesm-hero {
  margin-bottom: 12px;
}

.nf-tablecodesm-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-tablecodesm-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-tablecodesm-filter {
  margin-bottom: 12px;
}

.nf-tablecodesm-filter__body {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
}

.nf-tablecodesm-filter__item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nf-tablecodesm-filter__label {
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
  white-space: nowrap;
}

.nf-tablecodesm-filter__select {
  width: 150px;
}

.nf-tablecodesm-filter__input {
  width: 200px;
}

.nf-tablecodesm-filter__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-left: auto;
}

.nf-tablecodesm-tabs-wrap {
  margin-bottom: 0;
}

.nf-tablecodesm-tabs {
  margin-bottom: 0;
}

.nf-tablecodesm-tabs >>> .el-tabs__header {
  margin-bottom: 0;
  background: #fff;
  border-radius: 8px 8px 0 0;
  padding: 0 16px;
  border: 1px solid var(--nf-border, #e8eaed);
  border-bottom: none;
}

.nf-tablecodesm-table-card {
  margin-bottom: 0;
  border-radius: 0 0 8px 8px;
  border-top: none;
}

.nf-tablecodesm-total {
  margin-top: 12px;
  font-size: 14px;
  color: var(--nf-text-regular, #303133);
}

.nf-tablecodesm-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.nf-tablecodesm-status--active {
  color: var(--nf-brand, #ffa8c2);
}

.nf-tablecodesm-status--idle {
  color: #999;
}
</style>
