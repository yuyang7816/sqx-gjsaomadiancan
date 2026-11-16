<template>
  <div class="nf-page nf-missionredact-page">
    <div class="nf-page-hero nf-missionredact-hero">
      <span class="nf-page-badge">任务</span>
      <h2 class="nf-missionredact-hero__title">修改任务</h2>
      <p class="nf-missionredact-hero__desc">编辑任务标题、分类、幸运值与内容，更新标题图片后保存修改</p>
    </div>

    <div class="nf-table-card nf-missionredact-form-card">
      <div class="nf-missionredact-form-card__head">
        <div class="nf-missionredact-form-card__head-left">
          <i class="el-icon-edit"></i>
          <span>任务信息</span>
        </div>
        <div class="nf-missionredact-form-card__actions">
          <el-button size="mini" type="primary" icon="document" @click="artiReleass">修改任务</el-button>
        </div>
      </div>

      <div class="nf-missionredact-form-card__body">
        <div class="nf-missionredact-form-row">
          <span class="nf-missionredact-form-label">任务标题：</span>
          <el-input class="nf-missionredact-form-input" placeholder="请输入任务标题" v-model="title" autosize></el-input>
        </div>
        <div class="nf-missionredact-form-row">
          <span class="nf-missionredact-form-label">任务分类：</span>
          <el-select v-model="classifyId" placeholder="请选择任务分类" class="nf-missionredact-form-select">
            <el-option v-for="(item,index) in artiCletypes" :key="item.index" :label="item.classifyName" :value="item.id">
            </el-option>
          </el-select>
        </div>
        <div class="nf-missionredact-form-row">
          <span class="nf-missionredact-form-label">幸运值：</span>
          <el-input class="nf-missionredact-form-input" placeholder="请输入幸运值" type="number" v-model="luckyValue" autosize></el-input>
        </div>
        <div class="nf-missionredact-form-row nf-missionredact-form-row--upload">
          <span class="nf-missionredact-form-label">标题图片：</span>
          <el-upload
            v-model="titlePicture"
            :class="{hide:hideUpload}"
            :action="$http.adornUrlMsg('alioss/upload')"
            :limit=1
            :on-success="handleUploadSuccess"
            list-type="picture-card"
            :on-remove="handleRemove"
            :on-change="handleChange"
          >
            <img v-if="titlePicture" :src="titlePicture" class="nf-missionredact-upload-preview"/>
            <i class="el-icon-plus"></i>
          </el-upload>
        </div>
      </div>

      <div class="nf-missionredact-editor">
        <div class="nf-missionredact-editor__label">任务内容：</div>
        <div class="tinymce-content">
          <textarea id="tinymceId" :value="content" class="tinymce-textarea"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
    import load from './tinymce-components/dynamicLoadScript'
    import toolbar from './tinymce-components/toolbar'
    import plugins from './tinymce-components/plugins'

    const tinymceCDN = 'https://cdn.jsdelivr.net/npm/tinymce-all-in-one@4.9.3/tinymce.min.js'

    export default {
        name: 'Tinymce',
        data() {
            return {
                classifyId: '',
                title: '',
                luckyValue: '',
				titlePicture:'',
				isShow: false,
				hideUpload: false,
				limit:1,
				userId:'',
				id:'',
                hasChange: false,
                hasInit: false,
                tinymceId: 'tinymceId',
                height: 235,
                toolbar: [],
                menubar: 'file edit insert view format table',
                languageTypeList: {
                    'en': 'en',
                    'zh': 'zh_CN'
                },
				artiCletypes: [],
                // 富文本内容
                content: ''
            }
        },
        watch: {
            content(val) {
                if (!this.hasChange && this.hasInit) {
                    this.$nextTick(() => window.tinymce.get(this.tinymceId).setContent(val || ''))
                }
            }
        },
        methods: {
			handleChange(file, fileList){
				this.hideUpload = fileList.length >= this.limit;
			},
			handleRemove(file, fileList) {
				this.hideUpload = fileList.length >= this.limit;
			},
			//上传成功
			handleUploadSuccess(file,fileList) {
			  this.titlePicture= file.data
			},
			// 修改任务
			artiReleass(){
				if (this.title == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请输入任务标题',
				        type: 'warning'
				    });
				    return
				}
				if (this.classifyId == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请选择任务分类',
				        type: 'warning'
				    });
				    return
				}
				if (this.luckyValue == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请选择任务幸运值',
				        type: 'warning'
				    });
				    return
				}
				if (this.content == '') {
				    this.$notify({
				        title: '提示',
				        duration: 1800,
				        message: '请选择任务内容',
				        type: 'warning'
				    });
				    return
				}
				let id = this.$route.query.id
				this.$http({
				  url: this.$http.adornUrl('helpTaskPlatform/updateHelpTaskPlatform'),
				  method: 'post',
				  data: this.$http.adornData({
					 'id':id,
				    'title':this.title,
					'state':this.state,
				    'classifyId':this.classifyId,
				    'content':this.content,
				    'luckyValue':this.luckyValue,
				    'titlePicture':this.titlePicture,
					'createTime' : this.createTime
				  })
				}).then(({data}) => {
					this.dialogFormVisible = false
				    this.$message({
				      message: '修改成功',
				      type: 'success',
				      duration: 1500,
				      onClose: () => {
				        this.$router.push({path: '/missionsye'});
				      }
				    })
				})
			},
            init() {
                load(tinymceCDN, (err) => {
                    if (err) {
                        this.$message.error(err.message)
                        return
                    }
                    this.initTinymce()
                })
				let id = this.$route.query.id
				this.$http({
					url: this.$http.adornUrl('helpTaskPlatform/selectHelpTaskPlatformById'),
					method: 'get',
					params: this.$http.adornParams({
						'id':id
					})
				}).then(({data}) => {
					let returnData = data.data;
					this.title = returnData.title
					this.classifyId = returnData.classifyId
					this.title = returnData.title
					this.state = returnData.state
					this.content = returnData.content
					this.titlePicture = returnData.titlePicture
					this.luckyValue = returnData.luckyValue
					this.createTime = returnData.createTime
				})
            },

            initTinymce() {
                const _this = this
                window.tinymce.init({
                    selector: `#${this.tinymceId}`,
                    images_upload_handler: function (blobInfo, success, failure) {
                        var xhr, formData;

                        xhr = new XMLHttpRequest();
                        xhr.withCredentials = false;
                        xhr.open('POST', this.$http.adornUrlMsg('alioss/upload'));
                        xhr.onload = function () {
                            var json;

                            if (xhr.status != 200) {
                                failure('HTTP Error: ' + xhr.status);
                                return;
                            }
                            json = xhr.responseText;
                            if (json == "" || json == null) {
                                failure('Invalid JSON: ' + xhr.responseText);
                                return;
                            }
                            success(json);
                        };
                        formData = new FormData();
                        formData.append('file', blobInfo.blob(), blobInfo.filename());
                        xhr.send(formData);
                    },
                    language: this.languageTypeList['zh'],
                    height: this.height,
                    body_class: 'panel-body ',
                    object_resizing: false,
                    toolbar: this.toolbar.length > 0 ? this.toolbar : toolbar,
                    plugins,
                    menubar: this.menubar,
                    end_container_on_empty_block: true,
                    powerpaste_word_import: 'clean',
                    code_dialog_height: 450,
                    code_dialog_width: 1000,
                    advlist_bullet_styles: 'square',
                    advlist_number_styles: 'default',
                    imagetools_cors_hosts: ['www.tinymce.com', 'codepen.io'],
                    default_link_target: '_blank',
                    link_title: false,
                    // Image
                    imagetools_toolbar: 'rotateleft rotateright | flipv fliph | editimage imageoptions',
                    nonbreaking_force_tab: true, // inserting nonbreaking space &nbsp; need Nonbreaking Space Plugin
                    init_instance_callback: editor => {
                        if (_this.content) {
                            editor.setContent(_this.content)
                        }
                        _this.hasInit = true
                        editor.on('NodeChange Change KeyUp SetContent', () => {
                            this.hasChange = true
                            this.content = editor.getContent()
                        })
                    },
                    setup(editor) {
                        editor.on('FullscreenStateChanged', (e) => {
                            _this.fullscreen = e.state
                        })
                    }
                })
            },
            destroyTinymce() {
                const tinymce = window.tinymce.get(this.tinymceId)
                if (this.fullscreen) {
                    tinymce.execCommand('mceFullScreen')
                }
                if (tinymce) {
                    tinymce.destroy()
                }
            },
			// 获取分类
			classifySelect () {
			  this.tableDataLoading = true
			  this.$http({
				url: this.$http.adornUrl('platformClassify/selectClassifyList'),
				method: 'get',
				params: this.$http.adornParams({
				})
			  }).then(({data}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.artiCletypes = returnData
			  })
			},
        },
        mounted() {
            this.init()
			this.classifySelect()
        },
        beforeDestroy() {
            this.destroyTinymce()
        },
        activated() {
            if (window.tinymce) {
                this.initTinymce()
            }
        },
        deactivated() {
            this.destroyTinymce()
        }
    }
</script>

<style scoped>
.nf-missionredact-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-missionredact-hero {
  margin-bottom: 12px;
}

.nf-missionredact-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-missionredact-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-missionredact-form-card {
  margin-bottom: 0;
}

.nf-missionredact-form-card__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--nf-border, #e8eaed);
}

.nf-missionredact-form-card__head-left {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
  color: var(--nf-text-primary, #1a2433);
}

.nf-missionredact-form-card__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-left: auto;
}

.nf-missionredact-form-card__body {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.nf-missionredact-form-row {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}

.nf-missionredact-form-row--upload {
  align-items: flex-start;
}

.nf-missionredact-form-label {
  flex-shrink: 0;
  width: 88px;
  font-size: 14px;
  color: var(--nf-text-primary, #1a2433);
  line-height: 32px;
}

.nf-missionredact-form-row--upload .nf-missionredact-form-label {
  line-height: 148px;
}

.nf-missionredact-form-input,
.nf-missionredact-form-select {
  width: 240px;
  max-width: 100%;
}

.nf-missionredact-upload-preview {
  width: 100%;
  height: 100%;
  border-radius: 4px;
  object-fit: cover;
}

.nf-missionredact-editor {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid var(--nf-border, #e8eaed);
}

.nf-missionredact-editor__label {
  margin-bottom: 12px;
  font-size: 14px;
  font-weight: 500;
  color: var(--nf-text-primary, #1a2433);
}

.tinymce-content {
  position: relative;
  line-height: normal;
}

.tinymce-textarea {
  visibility: hidden;
  z-index: -1;
}
</style>

<style>
.nf-missionredact-page .hide .el-upload--picture-card {
  display: none;
}
</style>
