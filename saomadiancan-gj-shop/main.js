import App from './App'
import HttpRequest from './common/httpRequest'
import HttpCache from './common/cache'
import queue from './common/queue'
import VueI18n from 'vue-i18n'
import config from './common/config'
import share from './common/share.js'
import Vue from 'vue'

// 引入全局uView
import uView from "uview-ui";



let messages={}
uni.request({
	url: config.APIHOST1 + '/app/common/type/442',
	success: (ret) => {
		let retData = ret.data
		console.log(retData)
		if (retData.code == 0) {
			var language = ''
			if (retData.data && retData.data.value) {
				language = uni.getStorageSync('UNI_LOCALE') ? uni.getStorageSync('UNI_LOCALE') : retData
					.data.value
				if (uni.getStorageSync('language') == '') {
					uni.setStorageSync("language", retData.data.value)
				}
			} else {
				language = uni.getStorageSync('UNI_LOCALE') ? uni.getStorageSync('UNI_LOCALE') : 'en'
				if (uni.getStorageSync('language') == '') {
					uni.setStorageSync("language", 'en')
				}
			}
			uni.request({
				url: config.APIHOST1 + '/app/configI18n/getLanguageJsonList?language=' + language,
				header: {
					"Accept-Language": language
				},
				success: (res) => {
					let languageData = res.data
					if (languageData.code == 0) {
						let obj = {}
						obj[language] = languageData.data

						messages = obj
						uni.setStorageSync("messages", messages)
						let i18nConfig = {
							locale: language,
							messages
						}

						Vue.config.productionTip = false
						Vue.prototype.$Request = HttpRequest;
						Vue.prototype.$queue = queue;

						Vue.prototype.$Sysconf = HttpRequest.config;
						Vue.prototype.$SysCache = HttpCache;

						Vue.use(uView);
						Vue.use(VueI18n);
						Vue.mixin(share);
						const i18n = new VueI18n(i18nConfig)

						const app = new Vue({
							i18n,
							...App
						});
						app.$mount()

					}

				}
			})
		}
	}
})

App.mpType = 'app'
