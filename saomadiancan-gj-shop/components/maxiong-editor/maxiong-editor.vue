<template>
	<view class="container">
		<view class="page-body">
			<view class='wrapper'>
				<view class='toolbar' @tap="format">
					<view :class="formats.bold ? 'ql-active' : ''" class="iconfont icon-zitijiacu" data-name="bold">
					</view>
					<view :class="formats.italic ? 'ql-active' : ''" class="iconfont icon-zitixieti" data-name="italic">
					</view>
					<view :class="formats.underline ? 'ql-active' : ''" class="iconfont icon-zitixiahuaxian"
						data-name="underline"></view>
					<view :class="formats.strike ? 'ql-active' : ''" class="iconfont icon-zitishanchuxian"
						data-name="strike"></view>
					<view :class="formats.align === 'left' ? 'ql-active' : ''" class="iconfont icon-zuoduiqi"
						data-name="align" data-value="left"></view>
					<view :class="formats.align === 'center' ? 'ql-active' : ''" class="iconfont icon-juzhongduiqi"
						data-name="align" data-value="center"></view>
					<view :class="formats.align === 'right' ? 'ql-active' : ''" class="iconfont icon-youduiqi"
						data-name="align" data-value="right"></view>
					<view :class="formats.align === 'justify' ? 'ql-active' : ''" class="iconfont icon-zuoyouduiqi"
						data-name="align" data-value="justify"></view>
					<view :class="formats.lineHeight ? 'ql-active' : ''" class="iconfont icon-line-height"
						data-name="lineHeight" data-value="2"></view>
					<view :class="formats.letterSpacing ? 'ql-active' : ''" class="iconfont icon-Character-Spacing"
						data-name="letterSpacing" data-value="2em"></view>
					<view :class="formats.marginTop ? 'ql-active' : ''" class="iconfont icon-722bianjiqi_duanqianju"
						data-name="marginTop" data-value="20px"></view>
					<view :class="formats.previewarginBottom ? 'ql-active' : ''"
						class="iconfont icon-723bianjiqi_duanhouju" data-name="marginBottom" data-value="20px"></view>
					<view class="iconfont icon-clearedformat" @tap="removeFormat"></view>
					<view :class="formats.fontFamily ? 'ql-active' : ''" class="iconfont icon-font"
						data-name="fontFamily" data-value="Pacifico"></view>
					<view :class="formats.fontSize === '24px' ? 'ql-active' : ''" class="iconfont icon-fontsize"
						data-name="fontSize" data-value="24px"></view>

					<view :class="formats.color === '#0000ff' ? 'ql-active' : ''" class="iconfont icon-text_color"
						data-name="color" data-value="#0000ff"></view>
					<view :class="formats.backgroundColor === '#00ff00' ? 'ql-active' : ''"
						class="iconfont icon-fontbgcolor" data-name="backgroundColor" data-value="#00ff00"></view>

					<view class="iconfont icon-date" @tap="insertDate"></view>
					<view class="iconfont icon--checklist" data-name="list" data-value="check"></view>
					<view :class="formats.list === 'ordered' ? 'ql-active' : ''" class="iconfont icon-youxupailie"
						data-name="list" data-value="ordered"></view>
					<view :class="formats.list === 'bullet' ? 'ql-active' : ''" class="iconfont icon-wuxupailie"
						data-name="list" data-value="bullet"></view>
					<view class="iconfont icon-undo" @tap="undo"></view>
					<view class="iconfont icon-redo" @tap="redo"></view>

					<view class="iconfont icon-outdent" data-name="indent" data-value="-1"></view>
					<view class="iconfont icon-indent" data-name="indent" data-value="+1"></view>
					<view class="iconfont icon-fengexian" @tap="insertDivider"></view>
					<view class="iconfont icon-charutupian" @tap="insertImage"></view>
					<view :class="formats.header === 1 ? 'ql-active' : ''" class="iconfont icon-format-header-1"
						data-name="header" :data-value="1"></view>
					<view :class="formats.script === 'sub' ? 'ql-active' : ''" class="iconfont icon-zitixiabiao"
						data-name="script" data-value="sub"></view>
					<view :class="formats.script === 'super' ? 'ql-active' : ''" class="iconfont icon-zitishangbiao"
						data-name="script" data-value="super"></view>
					<view class="iconfont icon-shanchu" @tap="clear"></view>
					<view :class="formats.direction === 'rtl' ? 'ql-active' : ''" class="iconfont icon-direction-rtl"
						data-name="direction" data-value="rtl"></view>

				</view>

				<editor @input="editText" id="editors" class="ql-container" :placeholder="placeholder" showImgSize
					showImgToolbar showImgResize @statuschange="onStatusChange" :read-only="readOnly"
					@ready="onEditorReady">
				</editor>
			</view>
		</view>

	</view>
</template>

<script>
	export default {
		name:'maxiong-editor',
		props: {
			value: {
				type: String,
				default: ''
			},
			placeholder: {
				type: String,
				default: '开始输入...'
			},
			action:{
				type:String,
				default:'https://dcgjsh.xianmxkj.com/sqx_fast'
			}
		},
		data() {
			return {
				readOnly: false,
				formats: {},
				
			}
		},
		methods: {
			editText(res) {
				this.$emit('editorChange', res.detail.html)
			},
			readOnlyChange() {
				this.readOnly = !this.readOnly
			},
			onEditorReady() {
				this.createSelectorQuery().select('#editors').context((res) => {
					this.editorCtx = res.context
					this.editorCtx.setContents({
						html: this.value //初始化赋值内容。    
					})
				}).exec()
			},
			undo() {
				this.editorCtx.undo()
			},
			redo() {
				this.editorCtx.redo()
			},
			format(e) {
				let {
					name,
					value
				} = e.target.dataset
				if (!name) return
				// console.log('format', name, value)
				this.editorCtx.format(name, value)
			},
			onStatusChange(e) {
				const formats = e.detail
				this.formats = formats
			},
			insertDivider() {
				this.editorCtx.insertDivider({
					success: function() {
						console.log('insert divider success')
					}
				})
			},
			clear() {
				this.editorCtx.clear({
					success: function(res) {
						console.log("clear success")
					}
				})
			},
			removeFormat() {
				this.editorCtx.removeFormat()
			},
			insertDate() {
				const date = new Date()
				const formatDate = `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()}`
				this.editorCtx.insertText({
					text: formatDate
				})
			},
			insertImage() {
				let that = this
				uni.chooseImage({
					count: 1,
					success: (res) => {
						uni.uploadFile({ // 上传接口
							// url: that.config("APIHOST1") + '/alioss/upload', //真实的接口地址
							url: that.action+'/alioss/upload',
							filePath: res.tempFilePaths[0],
							name: 'file',
							success: (uploadFileRes) => {
								//上传图片成功后插入富文本编辑器中
								that.editorCtx.insertImage({
									src: JSON.parse(uploadFileRes.data).data,
									alt: '图像',
									success: function() {
										console.log('insert image success')
									}
								})
							}
						});
						
						
						
						
					}
				})
			}
		},
		onLoad() {
			uni.loadFontFace({
				family: 'Pacifico',
				source: 'url("./Pacifico.ttf")'
			})
		},
	}
</script>

<style>
	@font-face {
		font-family: "iconfont";
		src: url('@/components/maxiong-editor/editor-icon.ttf') format('truetype');
	}
	
	.iconfont {
	  font-family: "iconfont" !important;
	  font-size: 16px;
	  font-style: normal;
	  -webkit-font-smoothing: antialiased;
	  -moz-osx-font-smoothing: grayscale;
	}
	
	.icon-redo:before {
	  content: "\e627";
	}
	
	.icon-undo:before {
	  content: "\e633";
	}
	
	.icon-indent:before {
	  content: "\eb28";
	}
	
	.icon-outdent:before {
	  content: "\e6e8";
	}
	
	.icon-fontsize:before {
	  content: "\e6fd";
	}
	
	.icon-format-header-1:before {
	  content: "\e860";
	}
	
	.icon-format-header-4:before {
	  content: "\e863";
	}
	
	.icon-format-header-5:before {
	  content: "\e864";
	}
	
	.icon-format-header-6:before {
	  content: "\e865";
	}
	
	.icon-clearup:before {
	  content: "\e64d";
	}
	
	.icon-preview:before {
	  content: "\e631";
	}
	
	.icon-date:before {
	  content: "\e63e";
	}
	
	.icon-fontbgcolor:before {
	  content: "\e678";
	}
	
	.icon-clearedformat:before {
	  content: "\e67e";
	}
	
	.icon-font:before {
	  content: "\e684";
	}
	
	.icon-723bianjiqi_duanhouju:before {
	  content: "\e65f";
	}
	
	.icon-722bianjiqi_duanqianju:before {
	  content: "\e660";
	}
	
	.icon-text_color:before {
	  content: "\e72c";
	}
	
	.icon-format-header-2:before {
	  content: "\e75c";
	}
	
	.icon-format-header-3:before {
	  content: "\e75d";
	}
	
	.icon--checklist:before {
	  content: "\e664";
	}
	
	.icon-baocun:before {
	  content: "\ec09";
	}
	
	.icon-line-height:before {
	  content: "\e7f8";
	}
	
	.icon-quanping:before {
	  content: "\ec13";
	}
	
	.icon-direction-rtl:before {
	  content: "\e66e";
	}
	
	.icon-direction-ltr:before {
	  content: "\e66d";
	}
	
	.icon-selectall:before {
	  content: "\e62b";
	}
	
	.icon-fuzhi:before {
	  content: "\ec7a";
	}
	
	.icon-shanchu:before {
	  content: "\ec7b";
	}
	
	.icon-bianjisekuai:before {
	  content: "\ec7c";
	}
	
	.icon-fengexian:before {
	  content: "\ec7f";
	}
	
	.icon-dianzan:before {
	  content: "\ec80";
	}
	
	.icon-charulianjie:before {
	  content: "\ec81";
	}
	
	.icon-charutupian:before {
	  content: "\ec82";
	}
	
	.icon-wuxupailie:before {
	  content: "\ec83";
	}
	
	.icon-juzhongduiqi:before {
	  content: "\ec84";
	}
	
	.icon-yinyong:before {
	  content: "\ec85";
	}
	
	.icon-youxupailie:before {
	  content: "\ec86";
	}
	
	.icon-youduiqi:before {
	  content: "\ec87";
	}
	
	.icon-zitidaima:before {
	  content: "\ec88";
	}
	
	.icon-xiaolian:before {
	  content: "\ec89";
	}
	
	.icon-zitijiacu:before {
	  content: "\ec8a";
	}
	
	.icon-zitishanchuxian:before {
	  content: "\ec8b";
	}
	
	.icon-zitishangbiao:before {
	  content: "\ec8c";
	}
	
	.icon-zitibiaoti:before {
	  content: "\ec8d";
	}
	
	.icon-zitixiahuaxian:before {
	  content: "\ec8e";
	}
	
	.icon-zitixieti:before {
	  content: "\ec8f";
	}
	
	.icon-zitiyanse:before {
	  content: "\ec90";
	}
	
	.icon-zuoduiqi:before {
	  content: "\ec91";
	}
	
	.icon-zitiyulan:before {
	  content: "\ec92";
	}
	
	.icon-zitixiabiao:before {
	  content: "\ec93";
	}
	
	.icon-zuoyouduiqi:before {
	  content: "\ec94";
	}
	
	.icon-duigoux:before {
	  content: "\ec9e";
	}
	
	.icon-guanbi:before {
	  content: "\eca0";
	}
	
	.icon-shengyin_shiti:before {
	  content: "\eca5";
	}
	
	.icon-Character-Spacing:before {
	  content: "\e964";
	}

	.wrapper {
		padding: 5px;
	}

	.toolbar {
		/* border: 1upx # solid; */
		background-color: #EDF1F7;
		border-radius: 16rpx 16rpx 0 0;
	}

	.iconfont {
		display: inline-block;
		padding: 8px 8px;
		width: 24px;
		height: 24px;
		cursor: pointer;
		font-size: 20px;
	}

	.toolbar {
		box-sizing: border-box;
		border-bottom: 0;
		font-family: 'Helvetica Neue', 'Helvetica', 'Arial', sans-serif;
	}

	.ql-container {
		box-sizing: border-box;
		padding: 12px 15px;
		width: 100%;
		min-height: 30vh;
		height: auto;
		background-color: #EDF1F7;
		font-size: 16px;
		line-height: 1.5;
		/* border: 1px #000 solid; */
		border-radius: 0 0 16rpx 16rpx;
	}

	.ql-active {
		color: #06c;
	}
</style>
