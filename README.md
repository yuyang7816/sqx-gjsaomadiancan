<div align="center">

# **省钱兄JAVA国际扫码点餐四端开源系统（JAVA + Uni-app + Vue2）**

**服务端 · 管理端 · 用户端 · 商家端**

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![JDK](https://img.shields.io/badge/JDK-8-orange.svg)](docs/服务端-运行教程.md)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.6.11-brightgreen.svg)](docs/服务端-运行教程.md)
[![Vue](https://img.shields.io/badge/Vue-2.5-42b883.svg)](docs/管理端-运行教程.md)
[![uni-app](https://img.shields.io/badge/uni--app-Vue%202-2b9939.svg)](docs/用户端-运行教程.md)

八年沉淀，分批次开源由 AI 辅助开发的系统源码。  
开源为生态，服务才是根基。欢迎各位开发者一同交流。

</div>

---
⭐ 如果觉得项目不错，请点击右上角 Star 收藏支持！您的 Star 是我们持续开源迭代的最大动力！

## **1. 项目简介**

**省钱兄JAVA国际扫码点餐四端开源系统**是一套面向门店的扫码点餐业务系统，内置 **扫码进店、到店取餐、外卖配送、到店堂食、桌码、商品规格、会员积分、多语言**，采用前后端分离架构。服务端、管理端、用户端、商家端全套代码开源。程序里的系统名称是 **省钱兄国际扫码点餐系统**。

服务端基于 **Spring Boot 2.6.11**（**Java 8**）与 **MyBatis-Plus** 构建，负责接口、数据库，以及短信、对象存储、支付、地图等外部能力。  
管理端基于 **Vue 2.5 + Element UI + Webpack** 开发，供运营人员在电脑浏览器中维护门店、商品、订单、会员、国际化与系统设置。  
用户端基于 **uni-app（Vue 2）** 开发，覆盖 H5 与微信小程序。顾客扫码或选店后可以自取、外卖下单，并使用优惠券、钱包、积分和会员。  
商家端同样基于 **uni-app（Vue 2）**，给门店人员看收益、处理订单、上下架商品、管理桌位和用餐。

本仓库为四端全套源码。线上 H5 与仓库页面同源：用户端 [https://dcgj.xianmxkj.com](https://dcgj.xianmxkj.com)，商家端 [https://dcgjsh.xianmxkj.com](https://dcgjsh.xianmxkj.com)。

- **官方网站开源计划**：[https://www.xiansqx.com](https://www.xiansqx.com)
- **开源计划**：[https://opensource.xiansqx.cn](https://opensource.xiansqx.cn)
- **官方文档**：[功能说明与运行教程](docs/服务端-运行教程.md)
- **Gitee 仓库**：[https://gitee.com/maxiaodong1996/sqx-gjsaomadiancan.git](https://gitee.com/maxiaodong1996/sqx-gjsaomadiancan.git)
- **GitHub 仓库**：[https://github.com/yuyang7816/sqx-gjsaomadiancan.git](https://github.com/yuyang7816/sqx-gjsaomadiancan.git)


## **2. 移动端界面预览**

下面的图按手机宽度截自当前这套页面：用户端来自 [https://dcgj.xianmxkj.com](https://dcgj.xianmxkj.com)，商家端来自 [https://dcgjsh.xianmxkj.com](https://dcgjsh.xianmxkj.com)。

<div align="center">

| 首页 | 下单 | 订单 | 我的 | 登录 |
| :---: | :---: | :---: | :---: | :---: |
| <img src="https://dcgj.xianmxkj.com/file/uploadPath/2026/11/10/0a407a6adb57753d1b3b5fc302def7e7.png" width="160" alt="用户端首页" /> | <img src="https://dcgj.xianmxkj.com/file/uploadPath/2026/11/10/a3e7ae38bb970fe3bf1a382a27529204.png" width="160" alt="下单页" /> | <img src="https://dcgj.xianmxkj.com/file/uploadPath/2026/11/10/fb026665705f4f33c4fa56b4f3428603.png" width="160" alt="订单中心" /> | <img src="https://dcgj.xianmxkj.com/file/uploadPath/2026/11/10/82bd4188826185b35151e28db651ea6f.png" width="160" alt="我的" /> | <img src="https://dcgj.xianmxkj.com/file/uploadPath/2026/11/10/1a382b95dc25da3c4c5e9ec9e5b1cf71.png" width="160" alt="登录" /> |

| 商家端首页 |
| :---: |
| <img src="https://dcgj.xianmxkj.com/file/uploadPath/2026/11/10/71bf75b2eab6a386ae12b3b6cdac3d4e.png" width="160" alt="商家端首页" /> |

</div>

## **3. 开源说明**

> **【省钱兄科技｜开源计划启动】**
>
> 八年沉淀，我们将分批次开源全套由 AI 辅助开发的系统源码。  
> 本项目采用 **Apache 2.0** 许可协议，源码供学习、技术研究、商业使用、二次开发与参考使用。  
> **开源为生态，服务才是根基；** 欢迎各位开发者一同交流。

## **4. 快速开始**

建议顺序：**先服务端，再管理端，然后用户端和商家端。** 管理端和两个移动端都要等服务端已经启动，才能登录和加载数据。

**说明：** 仓库里**不包含** `node_modules`（体积大、随系统生成）。克隆或下载后，在 **`saomadiancan-gj-admin`** 目录下**必须先执行一次** `npm install`，再启动开发命令。用户端用 HBuilderX 打开 `saomadiancan-gj-user`，商家端打开 `saomadiancan-gj-shop`。详细步骤见下方与 [docs](docs/管理端-运行教程.md) 中的运行教程。

### **4.1 服务端**

安装 **JDK 8**、**Maven**、**MySQL 8**。新建数据库 `diancan4.0-gj`（字符集 `utf8mb4`），导入 [`saomadiancan-gj-service/db/diancan4.0-gj.sql`](saomadiancan-gj-service/db/diancan4.0-gj.sql)。脚本只建表和写入数据，不会自己创建数据库。

[`application.yml`](saomadiancan-gj-service/src/main/resources/application.yml) 里 `spring.profiles.active` 当前是 **`prod`**。数据源在 [`application-prod.yml`](saomadiancan-gj-service/src/main/resources/application-prod.yml)。本机运行时，把里面的地址改成 `127.0.0.1:3306`，库名保持 `diancan4.0-gj`，账号密码改成你自己的 MySQL。文件里注释掉的本地示例是 `root` / `123456`。

在 `saomadiancan-gj-service` 目录执行：

```bash
mvn spring-boot:run
```

默认 **端口 8878**，上下文路径 **`/sqx_fast`**。控制台出现「外卖点餐项目启动成功」后，接口文档为 `http://127.0.0.1:8878/sqx_fast/swagger-ui.html`。

配置里 `sqx.redis.open` 为 `false`，本地可以先不装 Redis。

更完整的步骤见 [服务端运行教程](docs/服务端-运行教程.md)，能力说明见 [服务端功能说明](docs/服务端-功能说明.md)。

### **4.2 管理端**

安装 **Node.js 18.13 或更高**（见 `saomadiancan-gj-admin/package.json` 的 `engines`）。进入 `saomadiancan-gj-admin` 目录，**首次运行前**安装依赖并启动：

```bash
cd saomadiancan-gj-admin
npm install
npm run dev
```

`npm install` 会根据 `package.json` / `package-lock.json` 在本机生成 `node_modules`，只需在依赖变更或新克隆后执行。

开发站点默认端口 **8001**。本机联调时，把 [`saomadiancan-gj-admin/config/index.js`](saomadiancan-gj-admin/config/index.js) 里 `/sqx_fast` 的 `target` 改成 `http://127.0.0.1:8878/sqx_fast/`。文件里如果还是线上管理端域名，页面会去连线上，看不到本机刚导入的数据。

导入脚本后的管理员账号是 `admin`。密码以库中 `sys_user` 的记录为准，登录页还要填写图片验证码。

### **4.3 用户端**

**HBuilderX：** 打开 `saomadiancan-gj-user` 目录，运行到浏览器或微信开发者工具。`manifest.json` 里 `vueVersion` 是 **2**。底部四个入口是 **首页、下单、订单、我的**。线上中文首页标题是「点餐」。

开发时请确认 [`saomadiancan-gj-user/common/config.js`](saomadiancan-gj-user/common/config.js) 中的接口地址。文件里现在指向线上 `https://dcgj.xianmxkj.com/sqx_fast`，打开就能看演示数据。要连本机服务端时，把接口地址改成 `http://127.0.0.1:8878/sqx_fast`。

### **4.4 商家端**

**HBuilderX：** 打开 `saomadiancan-gj-shop` 目录，运行到浏览器或微信开发者工具。底部入口是 **首页、订单、我的**。线上标题是「点餐门店端」。

开发时请确认 [`saomadiancan-gj-shop/common/config.js`](saomadiancan-gj-shop/common/config.js)。文件里现在指向线上 `https://dcgjsh.xianmxkj.com/sqx_fast`。连本机时改成 `http://127.0.0.1:8878/sqx_fast`。

短信、微信、支付宝、PayPal、Stripe、对象存储、地图等密钥需自行申请，并在管理端「系统配置」里填写。

## **5. 文档**

| 想做的事 | 打开哪一份 |
|---|---|
| 把服务端跑起来 | [服务端运行教程](docs/服务端-运行教程.md) |
| 看服务端能做什么 | [服务端功能说明](docs/服务端-功能说明.md) |
| 把管理端跑起来 | [管理端运行教程](docs/管理端-运行教程.md) |
| 看管理端能做什么 | [管理端功能说明](docs/管理端-功能说明.md) |
| 把用户端跑起来 | [用户端运行教程](docs/用户端-运行教程.md) |
| 看用户端能做什么 | [用户端功能说明](docs/用户端-功能说明.md) |
| 把商家端跑起来 | [商家端运行教程](docs/商家端-运行教程.md) |
| 看商家端能做什么 | [商家端功能说明](docs/商家端-功能说明.md) |

## **6. 目录结构**

仓库根目录放四端代码、说明和协议。编译出来的 `target`、`dist`、`unpackage`、`node_modules` 是运行时生成的，不需要改。

### **6.1 仓库根目录**

| 路径 | 是什么 |
|---|---|
| `saomadiancan-gj-service` | **服务端**。接口、数据库脚本、支付、短信、文件存储都在这里 |
| `saomadiancan-gj-admin` | **管理端**。运营人员在电脑浏览器里打开的后台 |
| `saomadiancan-gj-user` | **用户端**。顾客扫码点餐用的手机应用，也可在浏览器 H5 预览 |
| `saomadiancan-gj-shop` | **商家端**。门店看收益、接单、管商品和桌位 |
| `docs` | 功能说明与运行教程 |
| `LICENSE` | Apache License 2.0 全文 |

### **6.2 服务端 `saomadiancan-gj-service`**

| 路径 | 是什么 |
|---|---|
| `src/main/java/com/sqx/SqxApplication.java` | 启动入口 |
| `src/main/java/com/sqx/modules` | 业务模块：商品、订单、门店桌位、支付、会员、积分、优惠券、消息、国际化、文件 |
| `src/main/resources` | `application.yml` 与 `application-prod.yml` |
| `db/diancan4.0-gj.sql` | 要导入的数据库脚本。导入前先建库 `diancan4.0-gj` |

业务包包括 `goods`、`order`、`shopSeat`、`pay`、`iPay`、`member`、`integral`、`coupon`、`message`、`languageType`、`configI18n`、`banner`、`invite`、`datacentre`、`oss`、`sys`。

### **6.3 管理端 `saomadiancan-gj-admin`**

| 路径 | 是什么 |
|---|---|
| `src/views/common/home.vue` | 登录后的数据概览 |
| `src/views/common/login.vue` | 登录页 |
| `src/views/shopsList`、`src/views/shopManagement` | 门店、桌码、用餐、商品、订单、收入 |
| `src/views/user` | 用户列表与用户详情 |
| `src/views/language` | 国际化配置与语种配置 |
| `src/views/modules/sys` | 管理员、角色、菜单、字典、系统配置、操作日志 |
| `config/index.js` | 开发端口和接口代理 |
| `src/utils/httpRequest.js` | 请求封装。系统名称写在这里 |

菜单以导入的 `sys_menu` 为准，常见有：数据中心、用户中心、财务中心、门店中心、商品管理、订单中心、优惠券、会员、充值配置、国际化配置、系统配置。

### **6.4 用户端 `saomadiancan-gj-user`**

| 路径 | 是什么 |
|---|---|
| `pages/index` | 首页：轮播、每日任务、积分商城。带 `shopId` 或小程序码场景值时会进入下单 |
| `pages/diancan` | 下单：选店、自取 / 外卖、商品、规格、确认订单、支付 |
| `pages/order` | 订单中心：到店取餐、外卖配送、到店堂食 |
| `pages/my` | 我的：会员、优惠券、钱包、积分、语言切换 |
| `pages/login` | 邮箱登录、手机号登录、区号、注册、找回密码 |
| `my` | 积分商城、会员、优惠券、钱包、任务、地址、客服、消息、收银台 |
| `common/config.js` | 接口地址 |
| `pages.json` | 页面路由与底部导航 |
| `manifest.json` | 应用名称、Vue 版本、小程序 AppId、H5 配置 |

界面组件使用 **uView UI 1.8.4**。

### **6.5 商家端 `saomadiancan-gj-shop`**

| 路径 | 是什么 |
|---|---|
| `pages/index/index` | 首页：年 / 今日 / 本月收益、收银台、订单数量、商品上下架 |
| `pages/order`、`pages/orderDet` | 订单列表与详情 |
| `pages/goodlist`、`pages/addgood`、`pages/guige`、`pages/goodsType` | 商品、规格、分类 |
| `pages/books`、`pages/addbooks` | 桌位管理 |
| `pages/meals` | 用餐管理与店内点餐 |
| `pages/earnings` | 收益统计 |
| `pages/loginphone` | 门店管理员登录 |
| `common/config.js` | 接口地址 |
| `pages.json` | 页面路由与底部导航（首页 / 订单 / 我的） |

## **7. 核心特性与技术栈**

### **7.1 核心特性**

· **用户端底部四个入口：首页、下单、订单、我的**  
· **扫码或链接带上门店后直接进入该店菜单，支持自取和外卖**  
· **商品分类、规格、购物车、确认订单**  
· **订单分到店取餐、外卖配送、到店堂食，状态含制作中、待取餐、已完成、已取消**  
· **邮箱登录、手机号登录，手机号可选国际区号**  
· **界面文案走多语言，我的页面可以切换语言**  
· **会员、优惠券、钱包、积分任务、积分商城、邀请好友**  
· **支付支持钱包余额、微信、支付宝、PayPal、Stripe**  
· **商家端看收益和收银台，处理订单，上下架商品，管理桌位和用餐**  
· **管理端维护门店、桌码、商品、订单、会员、优惠券、语种和系统配置**

业务细节见 [用户端功能说明](docs/用户端-功能说明.md)、[商家端功能说明](docs/商家端-功能说明.md)、[管理端功能说明](docs/管理端-功能说明.md)、[服务端功能说明](docs/服务端-功能说明.md)。

版本以仓库里的配置文件为准。没写死版本号的，按上文快速开始安装对应大版本即可。

### **7.2 服务端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 语言 | **Java** | **1.8** | 整个服务端 |
| 框架 | **Spring Boot** | **2.6.11** | 提供网页接口，端口 `8878`，路径 `/sqx_fast` |
| 持久化 | **MyBatis-Plus** | **3.2.0** | 读写 MySQL |
| 构建 | **Maven** | 3.6 及以上 | 下载依赖、编译、启动 |
| 数据库 | **MySQL** | 8.0 及以上 | 业务数据。库名 `diancan4.0-gj`，脚本是 `saomadiancan-gj-service/db/diancan4.0-gj.sql` |
| 连接池 | **Druid** | 1.1.13 | 管理数据库连接 |
| 权限 | **Apache Shiro** | 1.4.0 | 管理端登录与接口权限 |
| 登录凭证 | **JWT** | 0.7.0 | 移动端请求头里的 `token` |
| 缓存 | **Redis** | 可选 | 本地 `sqx.redis.open` 为 `false`，可以先不装 |
| 接口文档 | **Swagger** | 2.9.2 | 浏览器打开 `swagger-ui.html` |
| 登录验证码 | **Kaptcha** | 0.0.9 | 管理端登录页上的图片验证码 |
| 对象存储 | **阿里云 OSS / 七牛 / 腾讯云 COS** | 可选 | 按系统配置选用 |
| 支付 | 微信、支付宝、PayPal、Stripe、钱包余额 | 随工程 | 下单、会员、充值。密钥要自己申请 |
| 定时任务 | **Quartz** | 2.3.0 | 后台调度 |

### **7.3 管理端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 运行环境 | **Node.js** | **18.13 及以上** | 安装依赖、启动开发网站 |
| 页面框架 | **Vue** | **2.5.16** | 后台每一个页面 |
| 构建 | **Webpack** | **4.47** | 开发时热更新，默认端口 `8001` |
| 界面组件 | **Element UI** | **2.8.2** | 表格、表单、弹窗、菜单 |
| 页面跳转 | **Vue Router** | **3.0.1** | 登录页、数据概览和各业务页的地址 |
| 状态 | **Vuex** | **3.0.1** | 记住登录等信息 |
| 请求 | **Axios** | **0.17** | 把操作发给服务端 |
| 图表 | **ECharts** | **3.8** | 数据概览页上的图 |

### **7.4 用户端与商家端**

| 类别 | 技术 | 版本 | 用来做什么 |
|---|---|---|---|
| 框架 | **uni-app** | Vue 2 | 同一套页面可运行到浏览器、微信小程序 |
| 开发工具 | **HBuilderX** | 正式版 | 打开 `saomadiancan-gj-user` 或 `saomadiancan-gj-shop` |
| 界面组件 | **uView UI** | **1.8.4** | 按钮、输入框、弹窗、布局 |
| 浏览器预览 | H5 | 以控制台为准 | 电脑上看用户端或商家端。用户端 H5 开发端口在 `manifest.json` 里是 `8080` |
| 微信小程序 | 微信开发者工具 | 可选 | 要自己的小程序 AppId 才能真机预览 |
| 地图 | **Google 地图** | 可配置 | 用户端 H5 与 App 的定位选点 |

## **8. 开源协议**

<div align="center">

本仓库使用 **Apache License 2.0**

全文见 [LICENSE](LICENSE)

</div>

## **9. 社区与支持**

运行、二次开发或商务合作，可以联系 **省钱兄科技**。

使用过程中如果接口起不来、页面打不开，先按本文 **4. 快速开始** 和 `docs` 里的运行教程排查。短信、微信、支付宝、PayPal、Stripe、对象存储、地图等密钥需要自行申请，并在管理端系统配置中填写。仍解决不了时，可通过官方渠道说明你的环境和报错。

当前项目正在测试中，如遇到 BUG，可自行修复或者留言反馈，我们将第一时间进行版本迭代。  
欢迎一起交流学习。开源是把源码开放出来，具体落地、定制和长期维护仍由服务来承接。

<div align="center">

<img src="docs/images/community-qr.jpg" alt="省钱兄科技二维码" width="220" />

**省钱兄科技**

</div>
