<template>
	<div class="nf-page nf-language-page">
		<div class="nf-page-hero nf-language-hero">
			<span class="nf-page-badge">国际化</span>
			<h2 class="nf-language-hero__title">国际化配置</h2>
			<p class="nf-language-hero__desc">管理服务端与用户端多语言词条，支持按语言、关键词筛选，以及添加、修改与一键翻译</p>
		</div>

		<div class="nf-filter-card nf-language-filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body nf-language-filter__body">
				<div class="nf-language-filter__item">
					<span class="nf-language-filter__label">语言：</span>
					<el-select v-model="languageT" filterable class="nf-language-filter__select" @change="select()">
						<el-option v-for="item in statesnum" :key="item.language" :label="item.remark"
							:value="item.language">
						</el-option>
					</el-select>
				</div>
				<div class="nf-language-filter__item">
					<span class="nf-language-filter__label">语言关键词：</span>
					<el-input class="nf-language-filter__input" @keydown.enter.native="select" clearable
						placeholder="请输入语言关键词" v-model="primaryKeyT"></el-input>
				</div>
				<div class="nf-language-filter__item">
					<span class="nf-language-filter__label">语言值：</span>
					<el-input class="nf-language-filter__input" @keydown.enter.native="select" clearable
						placeholder="请输入语言值" v-model="languageValueT"></el-input>
				</div>
				<div class="nf-language-filter__actions">
					<el-button size="mini" type="primary" icon="document" @click="select">刷新</el-button>
					<el-button size="mini" type="primary" icon="document" @click="clears">重置</el-button>
					<el-button size="mini" type="primary" icon="document" :disabled="!isAuth('language:add')"
						@click="add">添加</el-button>
					<el-button size="mini" type="warning" icon="document" @click="fanyi">一键翻译</el-button>
				</div>
			</div>
		</div>

		<div class="nf-language-tabs-wrap">
			<el-tabs v-model="activeName" @tab-click="handleClick" class="nf-language-tabs">
				<el-tab-pane label="服务端" name="first">
					<div class="nf-table-card nf-language-table-card">
						<el-table v-loading="tableDataLoading" :data="tableData.records">
							<el-table-column fixed prop="id" label="编号" width="50"></el-table-column>
							<el-table-column prop="primaryKey" label="语言关键词" width="150">
							</el-table-column>
							<el-table-column prop="language" label="语言" width="100">
							</el-table-column>
							<el-table-column prop="remark" label="语言说明" width="100">
							</el-table-column>
							<el-table-column prop="languageValue" label="语言值">
							</el-table-column>
							<el-table-column prop="createTime" label="创建时间" width="160">
							</el-table-column>
							<el-table-column label="操作" width="180" fixed="right">
								<template slot-scope="scope">
									<el-button class="nf-language-table__btn" size="mini" type="primary" icon="document"
										:disabled="!isAuth('language:update')" @click="peizhiBtn(scope.row)">配置管理
									</el-button>
									<el-button class="nf-language-table__btn" size="mini" type="primary" icon="document"
										:disabled="!isAuth('language:update')" @click="updata(scope.row,1)">修改
									</el-button>
									<el-button class="nf-language-table__btn" size="mini" type="danger"
										:disabled="!isAuth('language:delete')" @click="deletes(scope.row,1)">删除</el-button>
								</template>
							</el-table-column>
						</el-table>
						<div class="nf-language-pagination">
							<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
								:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="用户端" name="second">
					<div class="nf-table-card nf-language-table-card">
						<el-table v-loading="tableDataLoading" :data="tableData.records">
							<el-table-column fixed prop="id" label="编号" width="50"></el-table-column>
							<el-table-column prop="primaryKey" label="语言关键词" width="150">
							</el-table-column>
							<el-table-column prop="language" label="语言" width="100">
							</el-table-column>
							<el-table-column prop="remark" label="语言说明" width="100">
							</el-table-column>
							<el-table-column prop="languageValue" label="语言值">
							</el-table-column>
							<el-table-column prop="createTime" label="创建时间" width="160">
							</el-table-column>
							<el-table-column label="操作" width="180" fixed="right">
								<template slot-scope="scope">
									<el-button class="nf-language-table__btn" size="mini" type="primary" icon="document"
										:disabled="!isAuth('language:update')" @click="peizhiBtn(scope.row)">配置管理
									</el-button>
									<el-button class="nf-language-table__btn" size="mini" type="primary" icon="document"
										:disabled="!isAuth('language:update')" @click="updata(scope.row,1)">修改
									</el-button>
									<el-button class="nf-language-table__btn" size="mini" type="danger"
										:disabled="!isAuth('language:delete')" @click="deletes(scope.row,1)">删除</el-button>
								</template>
							</el-table-column>
						</el-table>
						<div class="nf-language-pagination">
							<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
								:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>

		<el-dialog title="添加" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">类型：</span>
				<el-radio-group v-model="vipNameType">
					<el-radio :label="1">选择语言</el-radio>
					<el-radio :label="2">新增语言</el-radio>
				</el-radio-group>
			</div>

			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">语言简称：</span>
				<el-input style="width:50%;" v-if="vipNameType==2" v-model="language" type="text"
					placeholder="请输入语言简称,如:zh/en"></el-input>
				<el-select v-model="language" v-else filterable style="width:50%;" @change="selectX()">
					<el-option v-for="item in statesnum" :key="item.language" :label="item.remark"
						:value="item.language">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">语言说明：</span>
				<el-input style="width:50%;" v-model="remark" type="text" placeholder="请输入语言说明,如:简体中文/英文"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">语言关键词：</span>
				<el-input style="width:50%;" v-model="primaryKey" type="text" placeholder="请输入语言关键词"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">语言值：</span>
				<el-input style="width:50%;" v-model="languageValue" type="text" placeholder="请输入语言值"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="vipAdd()">确 定</el-button>
			</div>
		</el-dialog>
		<el-dialog title="修改" :visible.sync="dialogFormVisible1" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">类型：</span>
				<el-radio-group v-model="vipNameType">
					<el-radio :label="1">选择语言</el-radio>
					<el-radio :label="2">新增语言</el-radio>
				</el-radio-group>
			</div>

			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">语言简称：</span>
				<el-input style="width:50%;" v-if="vipNameType==2" v-model="language" type="text"
					placeholder="请输入语言简称,如:zh/en"></el-input>
				<el-select v-model="language" v-else filterable style="width:50%;" @change="selectX()">
					<el-option v-for="item in statesnum" :key="item.language" :label="item.remark"
						:value="item.language">
					</el-option>
				</el-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">语言说明：</span>
				<el-input style="width:50%;" v-model="remark" type="text" placeholder="请输入语言说明,如:简体中文/英文"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">语言关键词：</span>
				<el-input style="width:50%;" v-model="primaryKey" type="text" placeholder="请输入语言关键词"
					disabled></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">语言说明：</span>
				<el-input style="width:50%;" v-model="remark" type="text" placeholder="请输入语言"></el-input>
			</div>

			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">语言值：</span>
				<el-input style="width:50%;" v-model="languageValue" type="text" placeholder="请输入语言值"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="vipUpdata()">确 定</el-button>
			</div>
		</el-dialog>
		<el-dialog title="配置列表" :visible.sync="dialogFormVisibleP" center width="80%">
			<el-table v-loading="tableDataLoadingP" :data="tableDataP.records">
				<el-table-column fixed prop="id" label="编号" width="50"></el-table-column>
				<el-table-column prop="primaryKey" label="语言关键词" width="150">
				</el-table-column>
				<el-table-column prop="language" label="语言" width="100">
				</el-table-column>
				<el-table-column prop="remark" label="语言说明" width="100">
				</el-table-column>
				<el-table-column prop="languageValue" label="语言值">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column label="操作" width="180" align="center" fixed="right">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" icon="document"
							:disabled="!isAuth('language:update')" @click="updata(scope.row,2)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('language:delete')"
							@click="deletes(scope.row,2)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="nf-language-dialog-pagination">
				<el-pagination @size-change="handleSizeChangeP" @current-change="handleCurrentChangeP"
					:page-sizes="[5, 10, 15, 20]" :page-size="limit1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="tableDataP.total">
				</el-pagination>
			</div>
		</el-dialog>
		<el-dialog title="语种列表" :visible.sync="dialogFormVisibleY" center width="80%">
			<el-table v-loading="tableDataLoadingY" :data="tableDataY.records">
				<el-table-column fixed prop="id" label="编号" width="80"></el-table-column>
				<el-table-column prop="language" label="语种">
				</el-table-column>
				<el-table-column prop="remark" label="备注">
				</el-table-column>
				<el-table-column prop="sort" label="排序">
				</el-table-column>
				<el-table-column prop="isEnable" label="是否启用">
					<template slot-scope="scope">
						<span class="nf-language-status--on" v-if="scope.row.isEnable==1">是</span>
						<span class="nf-language-status--off" v-else>否</span>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="180" align="center" fixed="right">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" icon="document"
							:disabled="!isAuth('language:update')" @click="updataFy(scope.row)">一键翻译
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="nf-language-dialog-pagination nf-language-dialog-pagination--center">
				<el-pagination @size-change="handleSizeChangeY" @current-change="handleCurrentChangeY"
					:page-sizes="[5, 10, 15, 20]" :page-size="limitY" :current-page="pageY"
					layout="total,sizes, prev, pager, next,jumper" :total="tableDataY.total">
				</el-pagination>
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
				page1: 1,
				limit1: 10,
				showType: 1,
				id: '',
				remark: '',
				primaryKey: "40001", //编码
				language: "zh", //语言
				languageValue: "未知异常,请联系管理员", //语言值
				vipNameType: 1,
				tableDataLoading: false,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				tableData: {},
				languageT: '',
				primaryKeyT: '',
				languageValueT: '', 
				statesnum: [],
				languageTypeT: '',
				dialogFormVisibleP: false,
				tableDataLoadingP: false,
				tableDataP: {},
				typeIndex: 1,
				dialogFormVisibleY: false,
				tableDataLoadingY: false,
				tableDataY: {},
				pageY: 1,
				limitY: 10,
			}
		},
		methods: {
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			// tabs切换
			handleClick(tab, event) {
				if (tab._props.label == '服务端') {
					this.page = 1
					this.limit = 10
					this.showType = 1
					this.dataSelect()
				}
				if (tab._props.label == '用户端') {
					this.page = 1
					this.limit = 10
					this.showType = 2
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
			handleSizeChangeP(val) {
				this.limit1 = val
				this.dataSelectE()
			},
			handleCurrentChangeP(val) {
				this.page1 = val
				this.dataSelectE()
			},
			handleSizeChangeY(val) {
				this.limitY = val
				this.dataSelectY()
			},
			handleCurrentChangeY(val) {
				this.pageY = val
				this.dataSelectY()
			},
			// 查询短剧列表
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			clears() {
				this.page = 1
				this.languageT = this.$cookie.get('language')
				this.languageValueT = ''
				this.primaryKeyT = ''
				this.dataSelect()
				this.dataSelectYy()
			},
			// 添加
			add() {
				this.primaryKey = ''
				this.language = ''
				this.remark = ''
				this.languageValue = ''
				this.id = ''
				this.vipNameType = 1
				this.dialogFormVisible = true
			},
			updata(row, index) {
				this.typeIndex = index
				this.primaryKey = row.primaryKey
				this.language = row.language
				this.remark = row.remark
				this.languageValue = row.languageValue
				this.id = row.id
				this.vipNameType = 1
				this.dialogFormVisible1 = true
			},
			vipAdd() {
				if (this.primaryKey == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入语言关键词',
						type: 'warning'
					});
					return
				}

				if (this.language == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入语言',
						type: 'warning'
					});
					return
				}
				if (this.remark == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '语言说明不能为空',
						type: 'warning'
					});
					return
				}
				if (this.languageValue == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入语言值',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/configI18n/saveConfigI18n'),
					method: 'post',
					params: this.$http.adornParams({
						'primaryKey': this.primaryKey,
						'language': this.language,
						'languageValue': this.languageValue,
						'id': this.id,
						'remark': this.remark,
						'showType': this.showType,
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
								this.selectHc()
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
				if (this.primaryKey == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入语言关键词',
						type: 'warning'
					});
					return
				}
				if (this.language == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入语言',
						type: 'warning'
					});
					return
				}
				if (this.languageValue == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入语言值',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/configI18n/saveConfigI18n'),
					method: 'post',
					params: this.$http.adornParams({
						'primaryKey': this.primaryKey,
						'language': this.language,
						'languageValue': this.languageValue,
						'id': this.id,
						'remark': this.remark,
						'showType': this.showType,
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
								if (this.typeIndex == 1) {
									this.selectHc()
									this.dataSelect()
								} else {
									this.selectHc()
									this.dataSelectE()
								}

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
			deletes(row, index) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`admin/configI18n/removeI18n?id=${delid}`),
						method: 'get',
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
									if (index == 1) {
										this.dataSelect()
										this.selectHc()
									} else {
										this.dataSelectE()
									}
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
				this.$http({ //
					url: this.$http.adornUrl('admin/configI18n/getConfigI18nGroupList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'language': this.languageT,
						'languageValue': this.languageValueT,
						'primaryKey': this.primaryKeyT,
						'showType': this.showType,
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

			// 语言类型
			dataSelectYy() {
				this.$http({
					url: this.$http.adornUrl('admin/languageType/getLanguageTypeList'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						let returnData = data.data
						this.statesnum = returnData.records
					}
				})
			},
			selectX() {
				for (var i in this.statesnum) {
					if (this.language == this.statesnum[i].language) {
						this.remark = this.statesnum[i].remark
					}
				}

			},
			// 刷新缓存
			selectHc() {
				this.$http({
					url: this.$http.adornUrl('admin/configI18n/refreshI18nCache'),
					method: 'post',
					params: this.$http.adornParams({})
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
							onClose: () => {}
						})
					}
				})
			},
			// 获取缓存
			selectHqhc() {
				this.$http({
					url: this.$http.adornUrl('admin/configI18n/getI18nCacheCount'),
					method: 'get',
					params: this.$http.adornParams({})
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
							onClose: () => {}
						})
					}

				})
			},
			// 配置列表
			peizhiBtn(row) {
				this.primaryKey = row.primaryKey
				this.page1 = 1
				this.dataSelectE()
				this.dialogFormVisibleP = true
			},
			// 二级
			dataSelectE() {
				this.tableDataLoadingP = true
				this.$http({ //admin/configI18n/getConfigI18nGroupList
					url: this.$http.adornUrl('admin/configI18n/getConfigI18nList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.limit1,
						'language': '',
						'languageValue': '',
						'primaryKey': this.primaryKey,
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.tableDataLoadingP = false
						let returnData = data.data
						this.tableDataP = returnData
					}
				})
			},
			// 一键翻译弹框
			fanyi() {
				this.dataSelectY()
				this.dialogFormVisibleY = true
			},
			// 获取数据列表
			dataSelectY() {
				this.tableDataLoadingY = true
				this.$http({
					url: this.$http.adornUrl('admin/languageType/getLanguageTypeList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageY,
						'limit': this.limitY
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.tableDataLoadingY = false
						let returnData = data.data
						this.tableDataY = returnData
					}
				})
			},
			updataFy(row) {
				this.$message({
					message: '操作成功。正在翻译中，数据较多，请稍后·····',
					type: 'success',
					duration: 2500,
					onClose: () => {
						this.dialogFormVisibleY = false
						
					}
				})
				this.$http({
					url: this.$http.adornUrl('admin/configI18n/saveConfigI18nApi'),
					method: 'get',
					params: this.$http.adornParams({
						'language': this.languageT,
						'toLanguage': row.language,
						'showType': this.showType,
					})
				}).then(({
					data
				}) => {
					
					if (data.code == 0) {
						this.dataSelect()
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
				this.dataSelect()
			}
		},
		mounted() {
			this.languageT = this.$cookie.get('language')
			this.dataSelect()
			this.dataSelectYy()
		}
	}
</script>

<style scoped>
.nf-language-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-language-hero {
  margin-bottom: 12px;
}

.nf-language-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-language-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-language-filter {
  margin-bottom: 12px;
}

.nf-language-filter__body {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
}

.nf-language-filter__item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nf-language-filter__label {
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
  white-space: nowrap;
}

.nf-language-filter__select {
  width: 150px;
}

.nf-language-filter__input {
  width: 150px;
}

.nf-language-filter__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-left: auto;
}

.nf-language-tabs-wrap {
  margin-bottom: 0;
}

.nf-language-tabs {
  margin-bottom: 0;
}

.nf-language-tabs >>> .el-tabs__header {
  margin-bottom: 0;
  background: #fff;
  border-radius: 8px 8px 0 0;
  padding: 0 16px;
  border: 1px solid var(--nf-border, #e8eaed);
  border-bottom: none;
}

.nf-language-table-card {
  margin-bottom: 0;
  border-radius: 0 0 8px 8px;
  border-top: none;
}

.nf-language-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.nf-language-table__btn {
  margin: 5px;
}

.nf-language-dialog-pagination {
  text-align: right;
  margin-top: 10px;
}

.nf-language-dialog-pagination--center {
  text-align: center;
}

.nf-language-status--on {
  color: #13ce66;
}

.nf-language-status--off {
  color: #ff4949;
}
</style>
