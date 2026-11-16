<template>
	<div class="nf-page nf-languagesconfig-page">
		<div class="nf-page-hero nf-languagesconfig-hero">
			<span class="nf-page-badge">国际化</span>
			<h2 class="nf-languagesconfig-hero__title">语种配置</h2>
			<p class="nf-languagesconfig-hero__desc">管理系统支持的语种列表，可设置语种名称、备注、排序及启用状态</p>
		</div>

		<div class="nf-filter-card nf-languagesconfig-filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>快捷操作</span>
			</div>
			<div class="nf-filter-card__body nf-languagesconfig-filter__body">
				<div class="nf-languagesconfig-filter__actions">
					<el-button size="mini" type="primary" icon="document" @click="select">刷新</el-button>
					<el-button size="mini" type="primary" icon="document"
						:disabled="!isAuth('languagesConfig:add')" @click="add">添加</el-button>
				</div>
			</div>
		</div>

		<div class="nf-table-card nf-languagesconfig-table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.records">
				<el-table-column fixed prop="id" label="编号" width="80"></el-table-column>
				<el-table-column prop="language" label="语种">
				</el-table-column>
				<el-table-column prop="remark" label="备注">
				</el-table-column>
				<el-table-column prop="sort" label="排序">
				</el-table-column>
				<el-table-column prop="isEnable" label="是否启用">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.isEnable" @change="change(scope.row.isEnable,scope.row)"
							:active-value="openValue" :inactive-value="closeValue">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="180" align="center" fixed="right">
					<template slot-scope="scope">
						<el-button class="nf-languagesconfig-table__btn" size="mini" type="primary" icon="document"
							:disabled="!isAuth('languagesConfig:update')" @click="updata(scope.row)">修改
						</el-button>
						<el-button class="nf-languagesconfig-table__btn" size="mini" type="danger"
							:disabled="!isAuth('languagesConfig:delete')" @click="deletes(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="nf-languagesconfig-pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
				</el-pagination>
			</div>
		</div>

		<el-dialog title="添加" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">语种：</span>
				<el-input style="width:50%;" v-model="language" type="text" placeholder="请输入语种"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">备注：</span>
				<el-input style="width:50%;" v-model="remark" type="text" placeholder="请输入备注"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
				<el-input style="width:50%;" v-model="sort" type="number" :min="0" placeholder="请输入排序"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="vipAdd()">确 定</el-button>
			</div>
		</el-dialog>
		<el-dialog title="修改" :visible.sync="dialogFormVisible1" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">语种：</span>
				<el-input style="width:50%;" v-model="language" type="text" placeholder="请输入语种"></el-input>

			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">备注：</span>
				<el-input style="width:50%;" v-model="remark" type="text" placeholder="请输入备注"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
				<el-input style="width:50%;" v-model="sort" type="number" :min="0" placeholder="请输入排序"></el-input>
			</div>

			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="vipUpdata()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>
<script>
	export default {
		data() {
			return {
				activeName: 'first',
				page: 1,
				limit: 10,
				classify: 1,
				languageId: '',
				language: '',
				remark: '',
				sort: '',
				isEnable: 1,
				openValue: 1,
				closeValue: 0,
				tableDataLoading: false,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				tableData: {},
			}
		},
		methods: {
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			// tabs切换
			handleClick(tab, event) {
				if (tab._props.label == '轮播图') {
					this.page = 1
					this.limit = 5
					this.classify = 1
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
			// 查询短剧列表
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 添加
			add() {
				this.language = ''
				this.remark = ''
				this.languageId = ''
				this.isEnable = 1
				this.sort = ''
				this.dialogFormVisible = true
			},
			updata(row) {
				this.language = row.language
				this.remark = row.remark
				this.languageId = row.id
				this.isEnable = row.isEnable
				this.sort = row.sort
				this.dialogFormVisible1 = true
			},
			vipAdd() {
				if (this.language == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入语种',
						type: 'warning'
					});
					return
				}
				if (this.remark == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入备注',
						type: 'warning'
					});
					return
				}
				if (this.sort === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入排序',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/languageType/saveLanguageType'),
					method: 'post',
					params: this.$http.adornParams({
						'language': this.language,
						'remark': this.remark,
						'isEnable': this.isEnable,
						'sort': this.sort,
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
								this.dialogFormVisible = false
								this.language = ''
								this.remark = ''
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
			},
			vipUpdata() {
				if (this.remark == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入备注',
						type: 'warning'
					});
					return
				}
				if (this.sort === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入排序',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/languageType/saveLanguageType'),
					method: 'post',
					params: this.$http.adornParams({
						'language': this.language,
						'remark': this.remark,
						'id': this.languageId,
						'isEnable': this.isEnable,
						'sort': this.sort,
					})
				}).then(({
					data
				}) => {

					if (data.code == 0) {
						this.dialogFormVisible1 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.language = ''
								this.remark = ''
								this.languageId = ''
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
			},
			// 删除banner图
			deletes(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(
							`admin/languageType/deleteLanguageType?languageId=${delid}`),
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
				}).catch(() => {})
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/languageType/getLanguageTypeList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data
						this.tableData = returnData
					}
				})
			},
			// 是否启用
			change(val, row) {
				this.$http({
					url: this.$http.adornUrl('admin/languageType/saveLanguageType'),
					method: 'post',
					params: this.$http.adornParams({
						'language': row.language,
						'remark': row.remark,
						'languageId': row.languageId,
						'isEnable': row.isEnable,
						'sort': row.sort,
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
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}

				})
			},
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style scoped>
.nf-languagesconfig-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-languagesconfig-hero {
  margin-bottom: 12px;
}

.nf-languagesconfig-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-languagesconfig-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-languagesconfig-filter {
  margin-bottom: 12px;
}

.nf-languagesconfig-filter__body {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
}

.nf-languagesconfig-filter__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-left: auto;
}

.nf-languagesconfig-table-card {
  margin-bottom: 0;
}

.nf-languagesconfig-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.nf-languagesconfig-table__btn {
  margin: 5px;
}
</style>
