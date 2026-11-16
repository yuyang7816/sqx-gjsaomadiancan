/**
 * 全站路由配置
 *
 * 建议:
 * 1. 代码中路由统一使用name属性跳转(不使用path属性)
 */
import Vue from 'vue'
import Router from 'vue-router'
import http from '@/utils/httpRequest'
import { isURL } from '@/utils/validate'
import { clearLoginInfo } from '@/utils'

Vue.use(Router)

// 开发环境不使用懒加载, 因为懒加载页面太多的话会造成webpack热更新太慢, 所以只有生产环境使用懒加载
const _import = require('./import-' + process.env.NODE_ENV)

// 全局路由(无需嵌套上左右整体布局)
const globalRoutes = [
  { path: '/404', component: _import('common/404'), name: '404', meta: { title: '404未找到' } },
  { path: '/login', component: _import('common/login'), name: 'login', meta: { title: '登录' } }
]

// 主入口路由(需嵌套上左右整体布局)
const mainRoutes = {
  path: '/',
  component: _import('main'),
  name: 'main',
  redirect: { name: 'home' },
  meta: { title: '主入口整体布局' },
  children: [
    // 通过meta对象设置路由展示方式
    // 1. isTab: 是否通过tab展示内容, true: 是, false: 否
    // 2. iframeUrl: 是否通过iframe嵌套展示内容, '以http[s]://开头': 是, '': 否
    // 提示: 如需要通过iframe嵌套展示内容, 但不通过tab打开, 请自行创建组件使用iframe处理!
  { path: '/home', component: _import('common/home'), name: 'home', meta: { title: '首页' } },
	{ path: '/userList', component: _import('user/userList'), name: 'userList', meta: { title: '用户列表',isTab: true} },
	{ path: '/allocationList', component: _import('allocation/allocationList'), name: 'allocationList', meta: { title: '配置列表',isTab: true} },
	{ path: '/financeList', component: _import('finance/financeList'), name: 'financeList', meta: { title: '财务中心',isTab: true} },
	{ path: '/message', component: _import('message/message'), name: 'message', meta: { title: '消息中心',isTab: true} },
	{ path: '/taskConfig', component: _import('taskConfig/taskConfig'), name: 'taskConfig', meta: { title: '任务配置',isTab: true} },
	{ path: '/bannerList', component: _import('banner/bannerList'), name: 'bannerList', meta: { title: '商城配置',isTab: true} },
	{ path: '/mission', component: _import('mission/mission'), name: 'mission', meta: { title: '订单中心',isTab: true} },
	{ path: '/system', component: _import('mission/system'), name: 'system', meta: { title: '系统任务',isTab: true} },
	{ path: '/missionsye', component: _import('sysmission/missionsye'), name: 'missionsye', meta: { title: '活动派送',isTab: true} },
	{ path: '/materialsList', component: _import('materials/materialsList'), name: 'materialsList', meta: { title: '好物圈',isTab: true} },
	{ path: '/missionAdd', component: _import('sysmission/missionAdd'), name: 'missionAdd', meta: { title: '发布任务',isTab: false} },
	{ path: '/missionRedact', component: _import('sysmission/missionRedact'), name: 'missionRedact', meta: { title: '修改任务',isTab: false} },
	{ path: '/userDetail', component: _import('user/userDetail'), name: 'userDetail', meta: { title: '用户详情',isTab: false} },
	{ path: '/userDetail1', component: _import('user/userDetail1'), name: 'userDetail1', meta: { title: '用户详情',isTab: false} },
	{ path: '/missionDetails', component: _import('mission/missionDetails'), name: 'missionDetails', meta: { title: '任务详情',isTab: false} },
	{ path: '/fitmentList', component: _import('fitment/fitmentList'), name: 'fitmentList', meta: { title: '首页装修',isTab: false} },
	{ path: '/integral', component: _import('integral/integral'), name: 'integral', meta: { title: '积分推送',isTab: true} },
	{ path: '/integralDetail', component: _import('integral/integralDetail'), name: 'integralDetail', meta: { title: '活动详情',isTab: false} },
	{ path: '/coupon', component: _import('coupon/coupon'), name: 'coupon', meta: { title: '优惠券管理',isTab: true} },
	{ path: '/missionComplain', component: _import('mission/missionComplain'), name: 'missionComplain', meta: { title: '任务投诉',isTab: true} },
	{ path: '/autonym', component: _import('autonym/autonym'), name: 'autonym', meta: { title: '实名认证',isTab: true} },

  {path: '/classifyAdmin', component: _import('selfShop/classifyAdmin'), name: 'classifyAdmin', meta: {title: '商品分类', isTab: true}},
  {path: '/orderAdmin', component: _import('selfShop/orderAdmin'), name: 'orderAdmin', meta: {title: '自营订单', isTab: true}},
  {path: '/addressAamin', component: _import('selfShop/addressAamin'), name: 'addressAamin', meta: {title: '地址管理', isTab: true}},
  {path: '/shopAdmin', component: _import('selfShop/shopAdmin'), name: 'shopAdmin', meta: {title: '商品管理', isTab: true}},
  {path: '/fenxiaoOrder', component: _import('selfShop/fenxiaoOrder'), name: 'fenxiaoOrder', meta: {title: '分销订单', isTab: true},},
  {path: '/disposeOrder', component: _import('selfShop/disposeOrder'), name: 'disposeOrder', meta: {title: '待处理订单', isTab: true},},
  {path: '/shopConfig', component: _import('selfShop/shopConfig'), name: 'shopConfig', meta: {title: '自营商城配置', isTab: true}},
  {path: '/shopPublish', component: _import('selfShop/shopPublish'), name: 'shopPublish', meta: {title: '发布商品', isTab: false}},
  {path: '/shopAmend', component: _import('selfShop/shopAmend'), name: 'shopAmend', meta: {title: '修改商品', isTab: false}},
  {path: '/specification', component: _import('selfShop/specification'), name: 'specification', meta: {title: '商品规格', isTab: true}},
  {path: '/orderDetails', component: _import('selfShop/orderDetails'), name: 'orderDetails', meta: {title: '订单详情', isTab: true}},
  

  {path: '/shopsList', component: _import('shopsList/shopsList'), name: 'shopsList', meta: {title: '门店中心', isTab: true}},
  {path: '/shopDatas', component: _import('shopsList/shopDatas'), name: 'shopDatas', meta: {title: '门店数据', isTab: false}},

  {path: '/shopsListAdmin', component: _import('shopsList/shopAdmin'), name: 'shopsListAdmin', meta: {title: '门店商品管理', isTab: false}},
  {path: '/shopsOrder', component: _import('shopsList/shopsOrder'), name: 'shopsOrder', meta: { title: '门店订单',isTab: false} },
  {path: '/topupmoney', component: _import('topupmoney/topupmoney'), name: 'topupmoney', meta: {title: '充值配置', isTab: false}},
  
  {path: '/memberDetails',component: _import('members/memberDetails'),name: 'memberDetails',meta: {title: '会员配置',isTab: true}},
  {path: '/vipPrivilege',component: _import('members/vipPrivilege'),name: 'vipPrivilege',meta: {title: '会员特权',isTab: true}},
  {path: '/couponList', component: _import('coupon/couponList'), name: 'couponList', meta: { title: '优惠券列表',isTab: true} },
  
// 门店端
  {path: '/shopsListSm', component: _import('shopManagement/shopsList'), name: 'shopsListSm', meta: {title: '门店中心', isTab: false}},
  {path: '/shopsListAdminSm', component: _import('shopManagement/shopAdmin'), name: 'shopsListAdminSm', meta: {title: '门店商品管理', isTab: true}},
  {path: '/missionSm', component: _import('shopManagement/mission'), name: 'missionSm', meta: { title: '订单中心',isTab: true} },
  {path: '/shopsOrderSm', component: _import('shopManagement/shopsOrder'), name: 'shopsOrderSm', meta: { title: '门店订单',isTab: true} },
  {path: '/shopDatasSm', component: _import('shopsList/shopDatas'), name: 'shopDatasSm', meta: {title: '门店数据', isTab: true}},
  {path: '/tableCodeSm', component: _import('shopManagement/tableCode'), name: 'tableCodeSm', meta: {title: '桌码管理', isTab: true}},
  	{path: '/shopClassifyAdminSm', component: _import('shopManagement/classifyAdmin'), name: 'shopClassifyAdminSm', meta: {title: '门店商品分类', isTab: false}},
  	{path: '/shopSpecificationSm', component: _import('shopManagement/specification'), name: 'shopSpecificationSm', meta: {title: '门店商品规格', isTab: false}},
		// 2023.07.03 新增
		{path: '/tableYoncanSm', component: _import('shopManagement/tableYoncan'), name: 'tableYoncanSm', meta: {title: '用餐管理', isTab: true}},
		{path: '/shopMoneySm', component: _import('shopManagement/shopMoney'), name: 'shopMoneySm', meta: {title: '收银台数据', isTab: true}},
		
  // 2023.02.15 点餐4.0版本 新增
  {path: '/tableCode', component: _import('shopsList/tableCode'), name: 'tableCode', meta: {title: '桌码管理', isTab: true}},
  {path: '/tableYoncan', component: _import('shopsList/tableYoncan'), name: 'tableYoncan', meta: {title: '用餐管理', isTab: true}},
	{path: '/shopClassifyAdmin', component: _import('shopsList/classifyAdmin'), name: 'shopClassifyAdmin', meta: {title: '门店商品分类', isTab: false}},
	{path: '/shopSpecification', component: _import('shopsList/specification'), name: 'shopSpecification', meta: {title: '门店商品规格', isTab: false}},
  {path: '/shopMoney', component: _import('shopsList/shopMoney'), name: 'shopMoney', meta: {title: '收银台数据', isTab: false}},
	
	// 2024.09.25 国际版 新增
	{path: '/language', component: _import('language/language'), name: 'language', meta: {title: '国际化配置', isTab: true}},
	{path: '/languagesConfig', component: _import('language/languagesConfig'), name: 'languagesConfig', meta: {title: '语种配置', isTab: true}},
	// {path: '/riderScheduling',component: _import('riderScheduling/riderSchedulingGoogle'),name: 'riderScheduling',meta: {title: '师傅调度', isTab: true}},

	
	],
  beforeEnter (to, from, next) {
    let token = Vue.cookie.get('token')
    if (!token || !/\S/.test(token)) {
      clearLoginInfo()
      next({ name: 'login' })
    }
    next()
  }
}

const router = new Router({
  mode: 'history', //  hash
  scrollBehavior(to,from,savedPosition){

  if (savedPosition){

  return savedPosition // 按下 后退/前进 按钮时，类似浏览器的原生表现

  }else {

  return {x:0,y:0} // 让页面滚动到顶部

  }

  },
  isAddDynamicMenuRoutes: false, // 是否已经添加动态(菜单)路由
  routes: globalRoutes.concat(mainRoutes)
})

router.beforeEach((to, from, next) => {
  // 添加动态(菜单)路由
  // 1. 已经添加 or 全局路由, 直接访问
  // 2. 获取菜单列表, 添加并保存本地存储
  if (router.options.isAddDynamicMenuRoutes || fnCurrentRouteType(to, globalRoutes) === 'global') {
    next()
  } else {
    http({
      url: http.adornUrl('/sys/menu/nav'),
      method: 'get',
      params: http.adornParams()
    }).then(({data}) => {
      if (data && data.code === 0) {
        fnAddDynamicMenuRoutes(data.menuList)
        router.options.isAddDynamicMenuRoutes = true
        sessionStorage.setItem('menuList', JSON.stringify(data.menuList || '[]'))
        sessionStorage.setItem('permissions', JSON.stringify(data.permissions || '[]'))
        next({ ...to, replace: true })
      } else {
        sessionStorage.setItem('menuList', '[]')
        sessionStorage.setItem('permissions', '[]')
        next()
      }
    }).catch((e) => {
      console.log(`%c${e} 请求菜单列表和权限失败，跳转至登录页！！`, 'color:blue')
      router.push({ name: 'login' })
    })
  }
})

/**
 * 判断当前路由类型, global: 全局路由, main: 主入口路由
 * @param {*} route 当前路由
 */
function fnCurrentRouteType (route, globalRoutes = []) {
  var temp = []
  for (var i = 0; i < globalRoutes.length; i++) {
    if (route.path === globalRoutes[i].path) {
      return 'global'
    } else if (globalRoutes[i].children && globalRoutes[i].children.length >= 1) {
      temp = temp.concat(globalRoutes[i].children)
    }
  }
  return temp.length >= 1 ? fnCurrentRouteType(route, temp) : 'main'
}

/**
 * 添加动态(菜单)路由
 * @param {*} menuList 菜单列表
 * @param {*} routes 递归创建的动态(菜单)路由
 */
function fnAddDynamicMenuRoutes (menuList = [], routes = []) {
  var temp = []
  for (var i = 0; i < menuList.length; i++) {
    if (menuList[i].list && menuList[i].list.length >= 1) {
      temp = temp.concat(menuList[i].list)
    } else if (menuList[i].url && /\S/.test(menuList[i].url)) {
      menuList[i].url = menuList[i].url.replace(/^\//, '')
      var route = {
        path: menuList[i].url.replace('/', '-'),
        component: null,
        name: menuList[i].url.replace('/', '-'),
        meta: {
          menuId: menuList[i].menuId,
          title: menuList[i].name,
          isDynamic: true,
          isTab: true,
          iframeUrl: ''
        }
      }
      // url以http[s]://开头, 通过iframe展示
      if (isURL(menuList[i].url)) {
        route['path'] = `i-${menuList[i].menuId}`
        route['name'] = `i-${menuList[i].menuId}`
        route['meta']['iframeUrl'] = menuList[i].url
      } else {
        try {
          route['component'] = _import(`modules/${menuList[i].url}`) || null
        } catch (e) {}
      }
      routes.push(route)
    }
  }
  if (temp.length >= 1) {
    fnAddDynamicMenuRoutes(temp, routes)
  } else {
    mainRoutes.name = 'main-dynamic'
    mainRoutes.children = routes
    router.addRoutes([
      mainRoutes,
      { path: '*', redirect: { name: '404' } }
    ])
    sessionStorage.setItem('dynamicMenuRoutes', JSON.stringify(mainRoutes.children || '[]'))
    console.log('\n')
    console.log('%c!<-------------------- 动态(菜单)路由 s -------------------->', 'color:blue')
    console.log(mainRoutes.children)
    console.log('%c!<-------------------- 动态(菜单)路由 e -------------------->', 'color:blue')
  }
}

export default router
