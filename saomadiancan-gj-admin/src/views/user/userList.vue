<template>
	<div class="nf-page nf-userlist-page">
		<div class="nf-page-hero nf-userlist-hero">
			<span class="nf-page-badge">用户</span>
			<h2 class="nf-userlist-hero__title">用户列表</h2>
			<p class="nf-userlist-hero__desc">管理平台注册用户，支持按手机号、昵称、邮箱筛选查询，可查看详情、赠送会员与状态管理</p>
		</div>

		<div class="nf-filter-card nf-userlist-filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body nf-userlist-filter__body">
				<div class="nf-userlist-filter__item">
					<span class="nf-userlist-filter__label">手机号：</span>
					<el-input
						class="nf-userlist-filter__input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入手机号"
						v-model="phone">
					</el-input>
				</div>
				<div class="nf-userlist-filter__item">
					<span class="nf-userlist-filter__label">昵称：</span>
					<el-input
						class="nf-userlist-filter__input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入昵称"
						v-model="userName">
					</el-input>
				</div>
				<div class="nf-userlist-filter__item">
					<span class="nf-userlist-filter__label">邮箱：</span>
					<el-input
						class="nf-userlist-filter__input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入邮箱"
						v-model="emailName">
					</el-input>
				</div>
				<div class="nf-userlist-filter__actions">
					<el-button size="mini" type="primary" icon="document" @click="select">查询</el-button>
					<el-button size="mini" type="primary" icon="document" @click="cleans">重置</el-button>
				</div>
			</div>
		</div>

		<div class="nf-userlist-tabs-wrap">
			<el-tabs v-model="activeName" @tab-click="handleClick" class="nf-userlist-tabs">
				<el-tab-pane label="全部用户" name="first"></el-tab-pane>
				<el-tab-pane label="普通用户" name="putong"></el-tab-pane>
				<el-tab-pane label="会员用户" name="vipUser"></el-tab-pane>
			</el-tabs>
			<div class="nf-table-card nf-userlist-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData.list">
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span class="nf-userlist-name">{{scope.row.userName ? scope.row.userName : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" class="nf-userlist-avatar">
							<img v-else :src="scope.row.avatar" alt="" class="nf-userlist-avatar">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
						<template slot-scope="scope">
							<span class="nf-userlist-link" @click="updates(scope.row)">{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column fixed prop="email" label="邮箱" width="150">
						<template slot-scope="scope">
							<span class="nf-userlist-name">{{scope.row.email ? scope.row.email : '-'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="platform" label="来源">
					</el-table-column>
					<el-table-column prop="zhifubaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{scope.row.zhifubaoName ? scope.row.zhifubaoName : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhifubao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{scope.row.zhifubao ? scope.row.zhifubao : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="member" label="是否是会员">
						<template slot-scope="scope">
							<span v-if="scope.row.member==1">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<el-table-column prop="endTime" label="会员到期时间" width="160">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column>
					<el-table-column prop="status " label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.userId)"
								:disabled="!isAuth('userList:updateStatus')" :active-value="openValue"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="150">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('userList:updateVip')" v-if="scope.row.isVip!=1" @click="updatesVip(scope.row)">赠送会员
							</el-button>
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">
								删除用户
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="nf-userlist-pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30,50,100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData.totalCount">
					</el-pagination>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				openValue: 1,
				closeValue: 2,
				state: 'false',
				limit: 10,
				page: 1,
				phone: '',
				userName: '',
				emailName:'',
				platform: 'all',
				isRelation: -1,
				isTuan: -1,
				value: '',
				member: -1,
				payData: [],
				nickName: '',
				grade: '',
				name: '',
				activeName: 'first',
				tableDataLoading: true,
				tableData: [],
				platforms: [{
					value: '',
					label: '全部'
				}, {
					value: 2,
					label: '普通用户'
				}, {
					value: 1,
					label: '门店管理员用户'
				}],
				shopAdminFlag: '',
				member:-1
			}
		},
		methods: {
			change(val, userId) {
				this.$http({
					url: this.$http.adornUrl(`user/updateUserStatusByUserId?userId=${userId}`),
					method: 'get',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dataSelect()
						}
					})
				})
			},
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			updatesShop(row) {
				if (row.shopAdminFlag == 1) {
					var texts = '确定取消门店管理员?'
					var shopAdminFlag1 = 2
				} else {
					var texts = '确定设置门店管理员?'
					var shopAdminFlag1 = 1
				}
				this.$confirm(texts, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('user/updateShopAdminFlag'),
						method: 'get',
						params: this.$http.adornParams({
							'userId': row.userId,
							'shopAdminFlag': shopAdminFlag1
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				})

			},
			updates2(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.superior
					}
				})
			},
			handleClick(tab, event) {
				this.page = 1
				this.limit = 10
				this.phone = ''
				this.userName = ''
				this.emailName = ''
				if (tab._props.label == '全部用户') {
					this.member = -1
					this.shopAdminFlag = ''
					this.dataSelect()
				}
				if (tab._props.label == '普通用户') {
					this.member = 0
					this.dataSelect()
				}
				if (tab._props.label == '会员用户') {
					this.member = 1
					this.dataSelect()
				}
				if (tab._props.label == '门店管理员用户') {
					this.member = -1
					this.shopAdminFlag = 1
					this.dataSelect()
				}

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
				this.paySelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.paySelect()
			},
			selectTrigger() {
				this.dataSelect()
			},
			select() {
				this.page = 1
				this.dataSelect()
			},
			select2() {
				this.paySelect()
			},
			cleans() {
				this.page = 1
				this.phone = ''
				this.userName = ''
				this.emailName = ''
				this.shopAdminFlag = ''
				this.dataSelect()
			},
			cleans2() {
				this.page = 1
				this.phone = ''
				this.userName = ''
				this.emailName = ''
				this.paySelect()
			},
			deleteuser(row) {
				let delid = row.userId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`user/deleteUserByUserId/${delid}`),
						method: 'post',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
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
				})
			},
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'shopAdminFlag': this.shopAdminFlag,
						'phone': this.phone,
						'userName': this.userName,
						'member':this.member,
						'email':this.emailName
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			},
			updatesVip(row) {
			  this.$confirm('是否赠送一个月会员？', '提示', {
			    confirmButtonText: '确定',
			    cancelButtonText: '取消',
			    type: 'warning'
			  }).then(() => {
			    this.$http({
			      url: this.$http.adornUrl('vipDetails/setUserVip'),
			      method: 'post',
			      params: this.$http.adornParams({
			        'userId': row.userId
			      })
			    }).then(({
			      data
			    }) => {
					if(data.code==0){
						this.$message({
						  message: '操作成功',
						  type: 'success',
						  duration: 1500,
						  onClose: () => {
						    this.dataSelect()
						  }
						})
					}else{
						this.$message({
						  message: data.msg,
						  type: 'warning',
						  duration: 1500,
						  onClose: () => {
						  }
						})
					}

			    })
			  })
			},
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style scoped>
.nf-userlist-page {
	padding: 0;
	overflow-x: hidden;
}

.nf-userlist-hero {
	margin-bottom: 12px;
}

.nf-userlist-hero__title {
	margin: 8px 0 4px;
	font-size: 18px;
	font-weight: 600;
	color: var(--nf-text-primary, #1a2433);
	line-height: 1.4;
}

.nf-userlist-hero__desc {
	margin: 0;
	font-size: 13px;
	color: var(--nf-text-secondary, #909399);
	line-height: 1.5;
}

.nf-userlist-filter {
	margin-bottom: 12px;
}

.nf-userlist-filter__body {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 12px;
}

.nf-userlist-filter__item {
	display: flex;
	align-items: center;
	gap: 8px;
}

.nf-userlist-filter__label {
	font-size: 13px;
	color: var(--nf-text-regular, #303133);
	white-space: nowrap;
}

.nf-userlist-filter__input {
	width: 150px;
}

.nf-userlist-filter__actions {
	display: flex;
	flex-wrap: wrap;
	gap: 8px;
	margin-left: auto;
}

.nf-userlist-tabs-wrap {
	margin-bottom: 0;
}

.nf-userlist-tabs {
	margin-bottom: 0;
}

.nf-userlist-tabs >>> .el-tabs__header {
	margin-bottom: 0;
	background: #fff;
	border-radius: 8px 8px 0 0;
	padding: 0 16px;
	border: 1px solid var(--nf-border, #e8eaed);
	border-bottom: none;
}

.nf-userlist-tabs >>> .el-tabs__content {
	display: none;
}

.nf-userlist-table-card {
	margin-bottom: 0;
	border-radius: 0 0 8px 8px;
	border-top: none;
}

.nf-userlist-pagination {
	display: flex;
	justify-content: flex-end;
	margin-top: 12px;
}

.nf-userlist-avatar {
	width: 40px;
	height: 40px;
	object-fit: cover;
	border-radius: 4px;
}

.nf-userlist-link {
	color: var(--nf-brand, #ffa8c2);
	cursor: pointer;
}

.nf-userlist-name {
	color: #f56c6c;
}

.nf-userlist-table-card .el-button + .el-button {
	margin-left: 0 !important;
	margin-top: 5px !important;
}
</style>
