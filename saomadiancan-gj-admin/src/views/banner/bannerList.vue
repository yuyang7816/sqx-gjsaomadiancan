<template>
	<div class="nf-page nf-banner-page">
		<div class="nf-page-hero nf-banner-hero">
			<span class="nf-page-badge">商城配置</span>
			<h2 class="nf-banner-hero__title">轮播与背景图管理</h2>
			<p class="nf-banner-hero__desc">管理首页轮播图与活动背景图，支持多语言版本配置、启用状态切换与图片上传</p>
		</div>

		<div class="nf-filter-card nf-banner-filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body nf-banner-filter__body">
				<div class="nf-banner-filter__item">
					<span class="nf-banner-filter__label">语言版本：</span>
					<el-select v-model="languageTypeT" class="nf-banner-filter__select" @change="animeDat()">
						<el-option v-for="item in statesnum" :key="item.language" :label="item.remark"
							:value="item.language">
						</el-option>
					</el-select>
				</div>
				<div class="nf-banner-filter__actions">
					<el-button
						v-if="activeName === 'first'"
						size="mini"
						type="primary"
						:disabled="!isAuth('bannerList:add')"
						icon="document"
						@click="addNotice">添加轮播图</el-button>
					<el-button
						v-if="activeName === 'fourthly'"
						size="mini"
						type="primary"
						:disabled="!isAuth('bannerList:add')"
						icon="document"
						@click="addNotice5">添加背景图</el-button>
				</div>
			</div>
		</div>

		<div class="nf-banner-tabs-wrap">
			<el-tabs v-model="activeName" @tab-click="handleClick" class="nf-banner-tabs">
				<el-tab-pane label="轮播图" name="first">
					<div class="nf-table-card nf-banner-table-card">
						<el-table v-loading="tableDataLoading" :data="tableData">
							<el-table-column fixed prop="id" label="编号" width="50">
							</el-table-column>
							<el-table-column prop="languageName" label="语言" width="100">
							</el-table-column>
							<el-table-column prop="imageUrl" label="图片">
								<template slot-scope="scope">
									<img :src="scope.row.imageUrl" class="nf-banner-thumb" />
								</template>
							</el-table-column>
							<el-table-column prop="name" label="轮播图名称">
							</el-table-column>
							<el-table-column prop="state" label="状态">
								<template slot-scope="scope">
									<span v-if='scope.row.state == 1'>显示</span>
									<span v-if='scope.row.state == 2' class="nf-banner-status-hidden">隐藏</span>
								</template>
							</el-table-column>
							<el-table-column prop="state" label="是否启用">
								<template slot-scope="scope">
									<el-switch v-model="scope.row.state" @change="change(scope.row.state,scope.row)"
										:active-value="openValue" :inactive-value="closeValue">
									</el-switch>
								</template>
							</el-table-column>
							<el-table-column prop="createTime" label="创建时间" width="160">
							</el-table-column>
							<el-table-column label="操作" width="180">
								<template slot-scope="scope">
									<el-button size="mini" type="primary" :disabled="!isAuth('bannerList:update')"
										@click="amendBanner(scope.$index, scope.row)">修改
									</el-button>
									<el-button size="mini" type="danger" :disabled="!isAuth('bannerList:delete')"
										@click="deletes(scope.row)">删除
									</el-button>
								</template>
							</el-table-column>
						</el-table>
						<div class="nf-banner-pagination">
							<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
								:page-sizes="[10, 20, 30,50,100]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next" :total="tableData.length">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="背景图" name="fourthly">
					<div class="nf-table-card nf-banner-table-card">
						<el-table v-loading="tableDataLoading" :data="tableData">
							<el-table-column fixed prop="id" label="编号" width="50">
							</el-table-column>
							<el-table-column prop="languageName" label="语言" width="100">
							</el-table-column>
							<el-table-column prop="imageUrl" label="图片">
								<template slot-scope="scope">
									<img :src="scope.row.imageUrl" class="nf-banner-thumb" />
								</template>
							</el-table-column>
							<el-table-column prop="name" label="背景图名称">
							</el-table-column>
							<el-table-column prop="describes" label="活动描述">
							</el-table-column>
							<el-table-column prop="url" label="链接" width="180">
							</el-table-column>
							<el-table-column prop="createTime" label="创建时间" width="160">
							</el-table-column>
							<el-table-column label="操作" width="180">
								<template slot-scope="scope">
									<el-button size="mini" type="primary" :disabled="!isAuth('bannerList:update')"
										@click="amendBanner(scope.$index, scope.row)">修改
									</el-button>
									<el-button size="mini" type="danger" :disabled="!isAuth('bannerList:delete')"
										@click="deletes(scope.row)">删除
									</el-button>
								</template>
							</el-table-column>
						</el-table>
						<div class="nf-banner-pagination">
							<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
								:page-sizes="[10, 20, 30,50,100]" :page-size="limit" :current-page="page"
								layout="total,sizes, prev, pager, next" :total="tableData.length">
							</el-pagination>
						</div>
					</div>
				</el-tab-pane>
			</el-tabs>
		</div>

		<!-- 添加轮播图弹框 -->
		<el-dialog title="添加" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">语言版本：</span>
				<el-radio-group v-model="languageType">
					<el-radio v-for="(item,index) in statesnum" :key="item.language" :label="item.language"
						style="margin: 5px;">{{item.remark}}</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;display: flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
				<div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="imageUrl"
						:action="$http.adornUrlMsg('alioss/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess">
						<img v-if="imageUrl" :src="imageUrl" class="avatar"
							style="border-radius: 6px;width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">链接：</span>
				<el-input style="width:50%;" v-model="url" placeholder="请输入链接"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
				<el-input style="width:50%;" v-model="name" placeholder="请输入名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">备注：</span>
				<el-input style="width:50%;" v-model="describes" placeholder="请输入描述"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="addNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>

		<!-- 添加背景图弹框 -->
		<el-dialog title="添加" :visible.sync="dialogFormVisible5" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">语言版本：</span>
				<el-radio-group v-model="languageType">
					<el-radio v-for="(item,index) in statesnum" :key="item.language" :label="item.language"
						style="margin: 5px;">{{item.remark}}</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;display: flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
				<div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="imageUrl"
						:action="$http.adornUrlMsg('alioss/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess">
						<img v-if="imageUrl" :src="imageUrl" class="avatar"
							style="border-radius: 6px;width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">链接：</span>
				<el-input style="width:50%;" v-model="url" placeholder="请输入链接"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
				<el-input style="width:50%;" v-model="name" placeholder="请输入名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">备注：</span>
				<el-input style="width:50%;" v-model="describes" placeholder="请输入描述"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible5 = false">取 消</el-button>
				<el-button type="primary" @click="addNoticeTo5()">确 定</el-button>
			</div>
		</el-dialog>

		<!-- 修改弹框 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible1" center>
			<el-form :model="form">
				<el-form-item label="语言版本：" :label-width="formLabelWidth">
					<el-radio-group v-model="languageType">
						<el-radio v-for="(item,index) in statesnum" :key="item.language" :label="item.language"
							style="margin: 5px;">{{item.remark}}</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="图片：" :label-width="formLabelWidth">
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="imageUrl"
							:action="$http.adornUrlMsg('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess2">
							<img v-if="form.imageUrl" :src="form.imageUrl" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"></i>
						</el-upload>
					</div>
				</el-form-item>
				<el-form-item label="链接：" :label-width="formLabelWidth">
					<el-input v-model="form.url" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="名称：" :label-width="formLabelWidth">
					<el-input v-model="form.name" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="备注：" :label-width="formLabelWidth">
					<el-input v-model="form.describes" style="width:65%;"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				limit: 10,
				classify: 1,
				openValue: 1,
				closeValue: 2,
				name: '',
				imageUrl: '',
				url: '',
				state: -1,
				describes: '',
				hideUpload: false,
				activeName: 'first',
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				dialogFormVisible4: false,
				dialogFormVisible5: false,
				dialogFormVisible6: false,
				tableDataLoading: true,
				formLabelWidth: '200px',
				tableData: [],
				choicenData: [],
				choicenData2: [],
				checkBoxData: [], //多选框选择的值
				keyword: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				states: [{
						label: '显示',
						value: 1
					},
					{
						label: '隐藏',
						value: 2
					}
				],
				form: {
					id: '',
					name: '',
					imageUrl: '',
					url: '',
					classify: '',
					createTime: '',
					sort: '',
					state: '',
					describes: ''

				},
				languageTypeT: '',
				statesnum: [{ //语言分类
					classificationName: '全部',
					classificationId: ''
				}],
				languageType: '',
			}
		},
		methods: {
			// 多选
			changeFun(val) {
				this.checkBoxData = val;
			},
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			// tabs切换
			handleClick(tab, event) {
				this.languageTypeT = this.statesnum[0].language
				if (tab._props.label == '轮播图') {
					this.page = 1
					this.limit = 5
					this.classify = 1
					this.dataSelect()
				}

				if (tab._props.label == '背景图') {
					this.page = 1
					this.limit = 5
					this.classify = 5
					this.dataSelect()
				}

			},
			//上传成功
			handleAvatarSuccess(file) {
				this.imageUrl = file.data
			},
			handleAvatarSuccess2(file2) {
				this.form.imageUrl = file2.data
			},
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect();
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect();
			},
			handleSizeChange3(val) {
				this.limit = val;
				this.choicenSelect()
			},
			handleCurrentChange3(val) {
				this.page = val;
				this.choicenSelect()
			},
			// 是否启用
			change(val, row) {
				this.$http({
					url: this.$http.adornUrl(`banner/updateBannerStateById?id=${row.id}`),
					method: 'post',
					data: this.$http.adornData({})
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
			//添加banner图
			addNotice() {
				this.languageType = ''
				this.imageUrl = ''
				this.state = ''
				this.url = ''
				this.name = ''
				this.describes = ''
				this.dialogFormVisible = true
			},
			// 添加banner图
			addNoticeTo() {
				if (this.languageType == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择语言版本',
						type: 'warning'
					})
					return
				}
				if (this.imageUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传图片',
						type: 'warning'
					});
					return
				}
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入名称',
						type: 'warning'
					});
					return
				}
				this.classify = 1
				this.$http({
					url: this.$http.adornUrl('banner/insertBanner'),
					method: 'post',
					data: this.$http.adornData({
						'classify': this.classify,
						'imageUrl': this.imageUrl,
						'state': this.state,
						'url': this.url,
						'name': this.name,
						'describes': this.describes,
						'languageType': this.languageType
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible = false
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
			// 修改
			amendBanner(index, rows) {
				this.dialogFormVisible1 = true
				this.form.id = rows.id
				this.form.imageUrl = rows.imageUrl
				this.form.url = rows.url
				this.form.sort = rows.sort
				this.form.state = rows.state
				this.form.name = rows.name
				this.form.describes = rows.describes
				this.languageType = rows.languageType
			},
			// 修改
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('banner/updateBannerById'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.id,
						'createTime': this.info.stockDate,
						'imageUrl': this.form.imageUrl,
						'url': this.form.url,
						'state': this.form.state,
						'sort': this.form.sort,
						'name': this.form.name,
						'describess': this.form.describes,
						'languageType': this.languageType
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

			//添加活动背景图
			addNotice5() {
				this.languageType = ''
				if (this.tableData.length == 1) {
					this.$message({
						title: '提示',
						duration: 1800,
						message: '活动背景图只能添加一张',
						type: 'warning'
					});
				} else {
					this.imageUrl = ''
					this.state = ''
					this.url = ''
					this.name = ''
					this.describes = ''
					this.dialogFormVisible5 = true
				}
			},
			// 添加背景图
			addNoticeTo5() {
				if (this.languageType == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择语言版本',
						type: 'warning'
					})
					return
				}
				if (this.imageUrl == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						type: 'success',
						message: '请上传图片',
						type: 'warning'
					});
					return
				}
				if (this.name == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入名称',
						type: 'warning'
					});
					return
				}
				this.classify = 5
				this.$http({
					url: this.$http.adornUrl('banner/insertBanner'),
					method: 'post',
					data: this.$http.adornData({
						'classify': this.classify,
						'imageUrl': this.imageUrl,
						'state': this.state,
						'url': this.url,
						'name': this.name,
						'describes': this.describes,
						'languageType': this.languageType
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible5 = false
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
			// 删除banner图
			deletes(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`banner/deleteBannerById?ids=${delid}`),
						method: 'get',
						data: this.$http.adornData({})
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
								onClose: () => {
									this.dataSelect()
								}
							})
						}

					})
				}).catch(() => {})

			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('banner/selectBannerList'),
					method: 'get',
					params: this.$http.adornParams({
						'state': this.state,
						'classify': this.classify,
						'languageType':this.languageTypeT,
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.tableData = returnData;
					}

				})
			},
			// select选择事件
			animeDat() {
				this.page = 1
				this.dataSelect()
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
						this.languageTypeT = returnData.records[0].language
						this.dataSelect()
					}
				})
			}

		},
		mounted() {
			
			this.dataSelectYy()
		}
	}
</script>

<style scoped>
.nf-banner-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-banner-hero {
  margin-bottom: 12px;
}

.nf-banner-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-banner-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-banner-filter {
  margin-bottom: 12px;
}

.nf-banner-filter__body {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
}

.nf-banner-filter__item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nf-banner-filter__label {
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
  white-space: nowrap;
}

.nf-banner-filter__select {
  width: 150px;
}

.nf-banner-filter__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-left: auto;
}

.nf-banner-tabs-wrap {
  margin-bottom: 0;
}

.nf-banner-tabs {
  margin-bottom: 0;
}

.nf-banner-tabs >>> .el-tabs__header {
  margin-bottom: 0;
  background: #fff;
  border-radius: 8px 8px 0 0;
  padding: 0 16px;
  border: 1px solid var(--nf-border, #e8eaed);
  border-bottom: none;
}

.nf-banner-table-card {
  margin-bottom: 0;
  border-radius: 0 0 8px 8px;
  border-top: none;
}

.nf-banner-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.nf-banner-thumb {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
}

.nf-banner-status-hidden {
  color: #f56c6c;
}
</style>
