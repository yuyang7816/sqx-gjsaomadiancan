<template>
  <div class="nf-page nf-materials-page">
    <div class="nf-page-hero nf-materials-hero">
      <span class="nf-page-badge">好物圈</span>
      <h2 class="nf-materials-hero__title">好物圈管理</h2>
      <p class="nf-materials-hero__desc">管理好物圈文章分类与文章内容，支持分类维护、文章发布与编辑</p>
    </div>

    <div class="nf-filter-card nf-materials-filter">
      <div class="nf-filter-card__head">
        <i class="el-icon-search"></i>
        <span>筛选条件</span>
      </div>
      <div class="nf-filter-card__body nf-materials-filter__body">
        <div v-if="activeName === 'second'" class="nf-materials-filter__item">
          <span class="nf-materials-filter__label">分类：</span>
          <el-select
            v-model="classifyId"
            class="nf-materials-filter__select"
            placeholder="请选择类型"
            @change="articleSelect(classifyId)">
            <el-option
              v-for="item in classifyIds"
              :key="item.id"
              :label="item.classifyName"
              :value="item.id">
            </el-option>
          </el-select>
        </div>
        <div class="nf-materials-filter__actions">
          <el-button
            v-if="activeName === 'first'"
            size="mini"
            type="primary"
            :disabled="!isAuth('materialsList:add')"
            icon="document"
            @click="addNotice">添加分类</el-button>
          <el-button
            v-if="activeName === 'second'"
            size="mini"
            type="primary"
            :disabled="!isAuth('materialsList:add')"
            icon="document"
            @click="addNoticewz">添加文章</el-button>
        </div>
      </div>
    </div>

    <div class="nf-materials-tabs-wrap">
      <el-tabs v-model="activeName" @tab-click="handleClick" class="nf-materials-tabs">
        <el-tab-pane label="分类管理" name="first">
          <div class="nf-table-card nf-materials-table-card">
            <el-table v-loading="tableDataLoading" :data="tableData">
              <el-table-column fixed prop="id" label="编号" width="80">
              </el-table-column>
              <el-table-column prop="classifyName" label="分类名称">
              </el-table-column>
              <el-table-column prop="createTime" label="创建时间">
              </el-table-column>
              <el-table-column label="操作" width="150">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="primary"
                    :disabled="!isAuth('materialsList:update')"
                    @click="updates(scope.$index, scope.row)">编辑
                  </el-button>
                  <el-button
                    size="mini"
                    type="danger"
                    :disabled="!isAuth('materialsList:delete')"
                    @click="deletes(scope.row)">删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
        <el-tab-pane label="文章管理" name="second">
          <div class="nf-table-card nf-materials-table-card">
            <el-table v-loading="tableDataLoading" :data="tableData">
              <el-table-column prop="id" label="编号" width="80">
              </el-table-column>
              <el-table-column prop="title" label="标题" width="120">
              </el-table-column>
              <el-table-column prop="articleUrl" label="链接">
              </el-table-column>
              <el-table-column prop="picture" label="图片">
                <template slot-scope="scope">
                  <img :src="scope.row.picture" alt="" class="nf-materials-thumb">
                </template>
              </el-table-column>
              <el-table-column prop="content" label="内容">
              </el-table-column>
              <el-table-column prop="createTime" label="创建时间" width="160">
              </el-table-column>
              <el-table-column label="操作">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="primary"
                    :disabled="!isAuth('materialsList:update')"
                    @click="updateswz(scope.$index, scope.row)">编辑
                  </el-button>
                  <el-button
                    size="mini"
                    type="danger"
                    :disabled="!isAuth('materialsList:delete')"
                    @click="deleteswz(scope.row)">删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 添加分类弹框 -->
    <el-dialog title="添加分类" :visible.sync="dialogFormVisible" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">分类名称：</span>
        <el-input style="width:50%;" v-model="classifyName" placeholder="请输入分类名称"></el-input>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 修改分类弹框 -->
    <el-dialog title="修改分类" :visible.sync="dialogFormVisible1" center>
      <el-form :model="form">
        <el-form-item label="分类名称：" :label-width="formLabelWidth">
          <el-input v-model="form.classifyName" style="width:65%;"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible1 = false">取 消</el-button>
        <el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 添加文章弹框 -->
    <el-dialog title="添加文章" :visible.sync="dialogFormVisible2" center>
      <div style="margin-bottom: 10px;display: flex;">
        <span style="width: 200px;display: inline-block;text-align: right;">文章图片：</span>
        <div style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
          <el-upload
            class="avatar-uploader"
            v-model="picture"
            :action="$http.adornUrlMsg('alioss/upload')"
            :show-file-list="false"
            :on-success="handleAvatarSuccess">
            <img v-if="picture" :src="picture" class="avatar" style="border-radius: 6px;width: 148px;height: 148px;"/>
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">文章标题：</span>
        <el-input style="width: 50%;" v-model="title" placeholder="请输入文章标题"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">跳转链接：</span>
        <el-input style="width: 50%;" v-model="articleUrl" placeholder="请输入跳转链接"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;position: relative;top: -65px;">文章内容：</span>
        <el-input style="width: 50%;" type="textarea" rows="4" v-model="content" placeholder="请输入内容"></el-input>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible2 = false">取 消</el-button>
        <el-button type="primary" @click="releasNoticeTowz()">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 修改文章弹框 -->
    <el-dialog title="修改文章" :visible.sync="dialogFormVisible3" center>
      <el-form :model="form">
        <el-form-item label="文章图片：" :label-width="formLabelWidth">
          <div style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
            <el-upload
              class="avatar-uploader"
              v-model="picture"
              :action="$http.adornUrlMsg('alioss/upload')"
              :show-file-list="false"
              :on-success="handleAvatarSuccess2">
              <img v-if="formwz.picture" :src="formwz.picture" class="avatar" style="border-radius: 6px;width: 148px;height: 148px;"/>
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </div>
        </el-form-item>
        <el-form-item label="文章标题：" :label-width="formLabelWidth">
          <el-input v-model="formwz.title" style="width:65%;"></el-input>
        </el-form-item>
        <el-form-item label="跳转链接：" :label-width="formLabelWidth">
          <el-input v-model="formwz.articleUrl" style="width:65%;"></el-input>
        </el-form-item>
        <el-form-item label="文章内容：" :label-width="formLabelWidth">
          <el-input type="textarea" rows="4" v-model="formwz.content" style="width:65%;"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible3 = false">取 消</el-button>
        <el-button type="primary" @click="amendNoticeTowz()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
	export default {
	    data() {
	      return {
				limit:5,
				page:1,
				state:1,
				sort:0,
				remark:'',
				createTime:'',
				classifyName:'',
				id:'',
				classifyId:1,
				classifyIds: [],
				form:{
					id:'',
					state:'',
					sort:'',
					classifyName:'',
					createTime:'',
					remark:''
				},
				formwz:{
					id:'',
					picture:'',
					articleUrl:'',
					content:'',
					title:'',
					classifyId:'',
					createTime:''
				},
				info: {
					stockDate:this.getNowTime(),  //日期
				},
				picture:'',
				picture2:'',
				articleUrl:'',
				content:'',
				title:'',
				formLabelWidth:'200px',
				activeName: 'first',
				tableDataLoading:false,
				dialogFormVisible1:false,
				dialogFormVisible:false,
				dialogFormVisible2:false,
				dialogFormVisible3:false,
				tableData:[],
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime() {
				 var now = new Date();
				 var year = now.getFullYear(); //得到年份
				 var month = now.getMonth(); //得到月份
				 var date = now.getDate(); //得到日期
				 var hh = now.getHours() < 10? "0" + now.getHours(): now.getHours();
			     var mm = now.getMinutes() < 10? "0" + now.getMinutes(): now.getMinutes();
			     var ss = now.getSeconds() < 10? "0" + now.getSeconds(): now.getSeconds();
				 month = month + 1;
				 month = month.toString().padStart(2, "0");
				 date = date.toString().padStart(2, "0");
				 var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
				 return defaultDate;
				 this.$set(this.info, "stockDate", defaultDate);
			},
			handleClick(tab, event) {
				if(tab._props.label=='分类管理'){
					this.dataSelect()
				}
				if(tab._props.label=='文章管理'){
					this.classifyId = 1
					this.articleSelect()
				}
			},
			handleAvatarSuccess(file) {
			     this.picture = file.data;
			 },
			 handleAvatarSuccess2(file2) {
			      this.formwz.picture = file2.data;
			  },
			// 添加分类弹框
			addNotice(){
				this.dialogFormVisible = true
			},
			// 添加分类
			releasNoticeTo(){
				if (this.classifyName == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请选择分类名称',
				        type: 'warning'
				    });
				    return
				}
				this.$http({
				  url: this.$http.adornUrl('article/saveArticleClassify'),
				  method: 'post',
				  data: this.$http.adornData({
				    'state': this.state,
					'sort' :this.sort,
					'remark':this.remark,
					'classifyName':this.classifyName,
					'createTime':this.info.stockDate,
				  })
				}).then(({data}) => {
					this.dialogFormVisible = false
				    this.$message({
				      message: '操作成功',
				      type: 'success',
				      duration: 1500,
				      onClose: () => {
						this.classifyName = ''
				        this.dataSelect()
				      }
				    })
				})
			},
			// 编辑分类弹框
			updates(index,rows){
				this.form.state = rows.state;
				this.form.sort = rows.state;
				this.dialogFormVisible1=true;
				this.form.id = rows.id;
				this.form.classifyName = rows.classifyName;
				this.form.remark=rows.remark;
				this.form.createTime = rows.createTime;
			},
			// 修改类别
			amendNoticeTo(){
				this.$http({
				  url: this.$http.adornUrl(`article/updateClassifyName/${this.form.id}/${this.form.classifyName}`),
				  method: 'post',
				  data: this.$http.adornData({
				  })
				}).then(({data}) => {
					this.dialogFormVisible1 = false
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
			// 删除分类
			deletes(row){
				this.$confirm(`确定删除此条信息?`, '提示', {
				  confirmButtonText: '确定',
				  cancelButtonText: '取消',
				  type: 'warning'
				}).then(() => {
				  this.$http({
				    url: this.$http.adornUrl(`article/deleteArticleClassifyById?articleClassifyId=${row.id}`),
				    method: 'post',
				    data: this.$http.adornData({
				    })
				  }).then(({data}) => {
					  this.$message({
						message: '删除成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
						this.name = '';
						  this.dataSelect()
						}
					  })
				  })
				}).catch(() => {})
			},
			// 添加文章弹框
			addNoticewz(){
				this.dialogFormVisible2 = true
			},
			// 添加文章
			releasNoticeTowz(){
				if (this.title == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请输入文章标题',
				        type: 'warning'
				    });
				    return
				}
				if (this.articleUrl == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请输入跳转链接',
				        type: 'warning'
				    });
				    return
				}
				if (this.content == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请输入内容',
				        type: 'warning'
				    });
				    return
				}
				this.$http({
				  url: this.$http.adornUrl('article/addArticle'),
				  method: 'post',
				  data: this.$http.adornData({
					'articleUrl' :this.articleUrl,
					'classifyId':this.classifyId,
					'content':this.content,
					'createTime':this.info.stockDate,
					'picture': this.picture,
					'state':this.state,
					'title':this.title,	
				  })
				}).then(({data}) => {
					this.dialogFormVisible2 = false
				    this.$message({
				      message: '操作成功',
				      type: 'success',
				      duration: 1500,
				      onClose: () => {
						this.title = ''
						this.articleUrl = ''
						this.content = ''
						this.picture = ''
				        this.articleSelect()
				      }
				    })
				})
			},
			// 编辑文章弹框
			updateswz(index,rows){
				this.formwz.picture = rows.picture;
				this.dialogFormVisible3=true;
				this.formwz.id = rows.id;
				this.formwz.classifyId = rows.classifyId
				this.formwz.articleUrl = rows.articleUrl;
				this.formwz.content=rows.content;
				this.formwz.title=rows.title;
				this.formwz.createTime = rows.createTime;
			},
			// 修改文章
			amendNoticeTowz(){
				this.$http({
				  url: this.$http.adornUrl('article/updateArticle'),
				  method: 'post',
				  data: this.$http.adornData({
					'id':this.formwz.id,
					'picture': this.formwz.picture,
				    'articleUrl': this.formwz.articleUrl,
					'content': this.formwz.content,
					'classifyId':this.formwz.classifyId,
					'title': this.formwz.title,
					'createTime': this.info.stockDate,
				  })
				}).then(({data}) => {
					this.dialogFormVisible3 = false
				    this.$message({
				      message: '操作成功',
				      type: 'success',
				      duration: 1500,
				      onClose: () => {
						  this.articleSelect()
				      }
				    })
				})
			},
			// 删除文章
			deleteswz(row){
				this.$confirm(`确定删除此条信息?`, '提示', {
				  confirmButtonText: '确定',
				  cancelButtonText: '取消',
				  type: 'warning'
				}).then(() => {
				  this.$http({
				    url: this.$http.adornUrl(`article/deleteArticle?articleId=${row.id}`),
				    method: 'post',
				    data: this.$http.adornData({
				    })
				  }).then(({data}) => {
					  this.$message({
						message: '删除成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
						this.name = '';
						  this.articleSelect()
						}
					  })
				  })
				}).catch(() => {})
			},
			// 获取分类管理数据
			dataSelect () {
			  this.tableDataLoading = true
			  this.$http({
				url: this.$http.adornUrl('article/selectArticleClassifyList'),
				method: 'get',
				params: this.$http.adornParams({
				})
			  }).then(({data}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
					this.classifyIds = returnData
			  })
			},
			// 获取文章管理数据
			articleSelect () {
			  this.tableDataLoading = true
			  this.$http({
				url: this.$http.adornUrl('article/selectArticleList'),
				method: 'get',
				params: this.$http.adornParams({
					'classifyId':this.classifyId
				})
			  }).then(({data}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
			  })
			},
		},
		mounted() {
			this.dataSelect()
		}
	  };
</script>

<style scoped>
.nf-materials-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-materials-hero {
  margin-bottom: 12px;
}

.nf-materials-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-materials-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-materials-filter {
  margin-bottom: 12px;
}

.nf-materials-filter__body {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
}

.nf-materials-filter__item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nf-materials-filter__label {
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
  white-space: nowrap;
}

.nf-materials-filter__select {
  width: 150px;
}

.nf-materials-filter__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-left: auto;
}

.nf-materials-tabs-wrap {
  margin-bottom: 0;
}

.nf-materials-tabs {
  margin-bottom: 0;
}

.nf-materials-tabs >>> .el-tabs__header {
  margin-bottom: 0;
  background: #fff;
  border-radius: 8px 8px 0 0;
  padding: 0 16px;
  border: 1px solid var(--nf-border, #e8eaed);
  border-bottom: none;
}

.nf-materials-table-card {
  margin-bottom: 0;
  border-radius: 0 0 8px 8px;
  border-top: none;
}

.nf-materials-thumb {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
}
</style>
