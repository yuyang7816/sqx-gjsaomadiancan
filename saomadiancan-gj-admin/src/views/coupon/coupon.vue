<template>
	<div class="nf-page nf-coupon-page">
		<div class="nf-page-hero nf-coupon-hero">
			<span class="nf-page-badge">优惠券</span>
			<h2 class="nf-coupon-hero__title">优惠券管理</h2>
			<p class="nf-coupon-hero__desc">管理优惠券配置，支持添加、编辑、删除及向用户赠送优惠券</p>
		</div>

		<div class="nf-filter-card nf-coupon-filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body nf-coupon-filter__body">
				<!-- <div class="nf-coupon-filter__item">
					<span class="nf-coupon-filter__label">语言版本：</span>
					<el-select v-model="languageTypeT" class="nf-coupon-filter__select" @change="animeDat()">
						<el-option v-for="item in statesnum" :key="item.language" :label="item.remark"
							:value="item.language">
						</el-option>
					</el-select>
				</div> -->
				<div class="nf-coupon-filter__actions">
					<el-button size="mini" type="primary" icon="document" @click="refresh">刷新</el-button>
					<el-button :disabled="!isAuth('coupon:add')" size="mini" type="primary"
						icon="document" @click="classifyStair()">添加优惠券</el-button>
				</div>
			</div>
		</div>

		<div class="nf-table-card nf-coupon-table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<!-- <el-table-column type="selection" fixed>
      </el-table-column> @selection-change="changeFun"-->
				<el-table-column prop="couponId" label="编号" width="80">
				</el-table-column>
				<!-- <el-table-column prop="languageName" label="语言">
				</el-table-column> -->
				<el-table-column prop="couponName" label="优惠券名称">
				</el-table-column>
				<el-table-column prop="couponPicture" label="图片">
					<template slot-scope="scope">
						<img :src="scope.row.couponPicture" alt="" class="nf-coupon-thumb">
					</template>
				</el-table-column>
				<el-table-column prop="money" label="优惠券金额">
				</el-table-column>
				<el-table-column prop="minMoney" label="最低消费">
				</el-table-column>
				<el-table-column prop="endDate" label="有效天数">
				</el-table-column>
				<el-table-column prop="needIntegral" label="兑换积分">
				</el-table-column>
				<el-table-column label="操作" width="250">
					<template slot-scope="scope">
						<el-button :disabled="!isAuth('coupon:update')" size="mini" type="primary"
							icon="document" @click="songCoupon(scope.row)">赠送</el-button>
						<el-button size="mini" type="primary" :disabled="!isAuth('coupon:update')"
							@click="compile(scope.$index, scope.row)">编辑
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('coupon:delete')"
							@click="deleteStair(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="nf-coupon-pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</div>
		<!-- 添加优惠券 -->
		<el-dialog title="添加优惠券" :visible.sync="dialogFormVisible" center>
			<!-- <div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">语言版本：</span>
				<el-radio-group v-model="languageType">
					<el-radio v-for="(item,index) in statesnum" :key="item.language" :label="item.language"
						style="margin: 5px;">{{item.remark}}</el-radio>
				</el-radio-group>
			</div> -->
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">优惠券名称：</span>
				<el-input style="width:50%;" v-model="couponName" type="text" placeholder="请输入优惠券名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
				<!-- <el-input style="width:50%;" v-model="couponPicture" type="text" placeholder="请输入图片"></el-input> -->
				<div
					style=" width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;display: inline-block;">
					<el-upload class="avatar-uploader" v-model="couponPicture"
						:action="$http.adornUrlMsg('alioss/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess1">
						<img v-if="couponPicture" :src="couponPicture" class="avatar"
							style="width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">优惠券金额：</span>
				<el-input style="width:50%;" v-model="money" type="text" placeholder="请输入优惠券金额"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">最低消费：</span>
				<el-input style="width:50%;" v-model="minMoney" type="text" placeholder="请输入最低消费"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">有效天数：</span>
				<el-input style="width:50%;" v-model="endDate" type="text" placeholder="请输入有效天数"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">兑换积分：</span>
				<el-input style="width:50%;" v-model="needIntegral" type="text" placeholder="请输入兑换积分"></el-input>
			</div>

			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="StairNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改一级分类 -->
		<el-dialog title="修改优惠券" :visible.sync="dialogFormVisible1" center>
			<el-form :model="form">
				<!-- <el-form-item label="语言版本：" :label-width="formLabelWidth">
					<el-radio-group v-model="languageType">
						<el-radio v-for="(item,index) in statesnum" :key="item.language" :label="item.language"
							style="margin: 5px;">{{item.remark}}</el-radio>
					</el-radio-group>
				</el-form-item> -->
				<el-form-item label="优惠券名称：" :label-width="formLabelWidth">
					<el-input v-model="form.couponName" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="图片：" :label-width="formLabelWidth">
					<!-- <el-input v-model="form.campusDetails" style="width:65%;"></el-input> -->
					<div
						style=" width:148px;height:148px;background-color: #fbfdff; border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;display: inline-block;">
						<el-upload class="avatar-uploader" v-model="form.couponPicture"
							:action="$http.adornUrlMsg('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess2">
							<img v-if="form.couponPicture" :src="form.couponPicture" class="avatar"
								style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</el-form-item>
				<el-form-item label="优惠券金额：" :label-width="formLabelWidth">
					<el-input v-model="form.money" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="最低消费：" :label-width="formLabelWidth">
					<el-input v-model="form.minMoney" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="有效天数：" :label-width="formLabelWidth">
					<el-input v-model="form.endDate" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="兑换积分：" :label-width="formLabelWidth">
					<el-input v-model="form.needIntegral" style="width:65%;"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="CompileNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 赠送优惠券-->
		<el-dialog title="赠送优惠券" :visible.sync="dialogFormVisible0" center>
			<div style="margin: 10px 0;">
				<span style="width: 200px;display: inline-block;text-align: right;">选择用户：</span>
				<el-tag :key="j" v-for="(tag,j) in userNameList" closable :disable-transitions="false"
					@close="couponIdClose1(tag,j)">
					{{tag}}
				</el-tag>
				<el-button class="button-new-tag" size="small" @click="couponIdBtn1()">添加</el-button>
			</div>

			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible0 = false">取 消</el-button>
				<el-button type="primary" @click="songNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>

		<!-- 用户列表 -->
		<el-dialog title="用户列表" :visible.sync="dialogFormVisible5" center>

			<el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入手机号"
				v-model="phone">
			</el-input>&nbsp;&nbsp;&nbsp;&nbsp;
			<el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入昵称"
				v-model="nickName">
			</el-input>
			<div style="display: inline-block;">
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document"
					@click="select1">查询</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document"
					@click="cleans1">重置</el-button>
			</div>
			<el-button style="margin-left:15px;" :disabled="checkBoxData.length <= 0 " size="mini" type="primary"
			 				icon="document" @click="closes2()">确定</el-button>
			<el-table v-loading="tableDataLoading1" @selection-change="changeFun1" :data="userData.list">
			 	<el-table-column type="selection" fixed>
				</el-table-column>
				<el-table-column prop="userId" label="编号">
				</el-table-column>
				<el-table-column fixed prop="userName" label="昵称">
					<template slot-scope="scope">
						<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未设置' }}</span>
					</template>
				</el-table-column>
				<el-table-column label="图像">
					<template slot-scope="scope">
						<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40" height="40">
						<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号" width="120">
					<template slot-scope="scope">
						<span class="nf-coupon-phone-link"
							@click="updates(scope.row)">{{ scope.row.phone ? scope.row.phone : '未绑定' }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="platform" label="来源">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
					:page-sizes="[5, 10, 15, 20]" :page-size="size1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="userData.totalCount">
				</el-pagination>
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
				size1: 10,
				page1: 1,
				checkBoxData: [], //多选框选择的值
				formLabelWidth: '200px',
				tableDataLoading: false,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible0: false,
				dialogFormVisible5: false,
				tableDataLoading1: false,
				tableData: [],
				couponName: '',
				couponPicture: '',
				endDate: '',
				minMoney: '',
				money: '',
				needIntegral: '',
				form: {
					couponId: '',
					couponName: '',
					couponPicture: '',
					endDate: '',
					minMoney: '',
					money: '',
					needIntegral: ''
				},
				userNameList: [],
				userIdList: [],
				userData: [],
				phone: '',
				nickName: '',
				couponId: '',
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
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleSizeChange2(val) {
				this.size1 = val;
				this.dataSelect1()
			},
			handleCurrentChange2(val) {
				this.page1 = val;
				this.dataSelect1()
			},
			handleAvatarSuccess1(file, fileList) {
				this.couponPicture = file.data
			},
			handleAvatarSuccess2(file, fileList) {
				this.form.couponPicture = file.data
			},
			// 刷新
			refresh() {
				this.page = 1
				this.dataSelect()
			},
			// 添加优惠券弹框
			classifyStair() {
				this.languageType = ''
				this.couponName = ''
				this.couponPicture = ''
				this.endDate = ''
				this.minMoney = ''
				this.money = ''
				this.needIntegral = ''
				this.dialogFormVisible = true
			},
			// 添加优惠券
			StairNoticeTo() {
				// if (this.languageType == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '请选择语言版本',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				if (this.campusName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入优惠券名称',
						type: 'warning'
					});
					return
				}
				if (this.campusDetails == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入优惠券地址',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/coupon/issueCoupon'),
					method: 'post',
					data: this.$http.adornData({
						'couponName': this.couponName,
						'couponPicture': this.couponPicture,
						'endDate': this.endDate,
						'minMoney': this.minMoney,
						'money': this.money,
						'needIntegral': this.needIntegral,
						'languageType': this.languageType
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible = false
						this.$message({
							message: '添加成功',
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
			// 修改优惠券
			compile(index, rows) {
				this.form.couponName = rows.couponName
				this.form.couponPicture = rows.couponPicture
				this.form.endDate = rows.endDate
				this.form.minMoney = rows.minMoney
				this.form.money = rows.money
				this.form.needIntegral = rows.needIntegral
				this.form.couponId = rows.couponId
				this.languageType = rows.languageType
				this.dialogFormVisible1 = true;
			},
			// 修改优惠券
			CompileNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('admin/coupon/updateCoupon'),
					method: 'post',
					data: this.$http.adornData({
						'couponId': this.form.couponId,
						'couponName': this.form.couponName,
						'couponPicture': this.form.couponPicture,
						'endDate': this.form.endDate,
						'minMoney': this.form.minMoney,
						'money': this.form.money,
						'needIntegral': this.form.needIntegral,
						'languageType': this.languageType
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
								this.dialogFormVisible1 = false
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisible1 = false
								this.dataSelect()
							}
						})
					}
				})
			},

			//删除
			deleteStair(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/coupon/deleteCoupon'),
						method: 'post',
						params: this.$http.adornParams({
							'couponId': row.couponId
						})
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
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						}

					})
				}).catch(() => {})
			},
			// 重置
			cleans() {
				this.phone = ''
				this.status = 0
				this.dataSelect()
			},
			// 获取派单数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/coupon/seleteAllCoupon'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'content': this.content,
						'languageType': this.languageTypeT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
					if (this.page > 1) {
						if (data.data.list.length == 0) {
							this.page = this.page - 1
							this.dataSelect()
						}
					}

				})
			},
			// 赠送优惠券
			songCoupon(row) {
				this.couponId = row.couponId
				this.userIdList = []
				this.userNameList = []
				this.checkBoxData = []
				this.dialogFormVisible0 = true
			},
			// 打开用户列表
			couponIdBtn1() {
				this.page1 = 1
				this.phone = ''
				this.nickName = ''
				this.dataSelect1()

				this.dialogFormVisible5 = true
			},
			// 多选用户
			changeFun1(val) {
				console.log('多选', val)
				this.checkBoxData = val
			},
			// 赠送优惠券
			songNoticeTo() {
				if (this.userIdList.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择用户',
						type: 'warning'
					});
					return
				}
				var userIdList = this.userIdList.toString()
				this.$http({
					url: this.$http.adornUrl('admin/coupon/setCoupon'),
					method: 'post',
					params: this.$http.adornParams({
						'userIds': userIdList,
						// 'shopId': 0,
						'couponId': this.couponId,
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
								this.userIdList = []
								this.userNameList = []
								this.dataSelect()
							}
						})
						this.dialogFormVisible0 = false
					} else {
						this.$message({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}

				})
			},
			// 获取用户数据列表
			dataSelect1() {
				let phone = -1
				if (this.phone) {
					phone = this.phone
				}
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1,
						'shopAdminFlag': '',
						'phone': this.phone,
						'userName': this.nickName,
						'type': ''
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data
					this.userData = returnData
				})
			},
			// 查询
			select1() {
				this.page1 = 1
				this.dataSelect1()
			},
			// 重置
			cleans1() {
				this.page = 1
				this.phone = ''
				this.nickName = ''
				// this.languageTypeT = ''
				this.dataSelect1()
			},
			couponIdClose1(ietm,index) {
				console.log('index', index, 'ietm', ietm, this.userIdList.length)
				if (this.userIdList.length < 2) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '最少需要选中一位用户',
						type: 'warning'
					});
					return
				} else {
					this.userIdList.splice(index, 1)
					this.userNameList.splice(index, 1)
				}
			},
			// 批量开启
			closes2(id) {
				// var userIdList = []
				 				// var userNameList = []
				 				for (var i in this.checkBoxData) {
				 					this.userIdList.push(this.checkBoxData[i].userId)
				 					this.userNameList.push(this.checkBoxData[i].userName)
				 				}
				 				// this.userIdList = userIdList
				 				// this.userNameList = userNameList
				this.dialogFormVisible5 = false
			},
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
	};
</script>

<style scoped>
.nf-coupon-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-coupon-hero {
  margin-bottom: 12px;
}

.nf-coupon-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-coupon-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-coupon-filter {
  margin-bottom: 12px;
}

.nf-coupon-filter__body {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
}

.nf-coupon-filter__item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nf-coupon-filter__label {
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
  white-space: nowrap;
}

.nf-coupon-filter__select {
  width: 150px;
}

.nf-coupon-filter__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-left: auto;
}

.nf-coupon-table-card {
  margin-bottom: 0;
}

.nf-coupon-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.nf-coupon-thumb {
  width: 40px;
  height: 40px;
  object-fit: cover;
  border-radius: 4px;
}

.nf-coupon-phone-link {
  color: var(--nf-brand, #ffa8c2);
  cursor: pointer;
}
</style>