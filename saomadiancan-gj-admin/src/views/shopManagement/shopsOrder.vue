<template>
	<div class="nf-page nf-shopsordersm-page">
		<div class="nf-page-hero nf-shopsordersm-hero">
			<span class="nf-page-badge">订单</span>
			<h2 class="nf-shopsordersm-hero__title">门店订单</h2>
			<p class="nf-shopsordersm-hero__desc">查看与管理门店订单，支持按状态、订单类型及用户信息筛选查询</p>
		</div>

		<div class="nf-filter-card nf-shopsordersm-filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body nf-shopsordersm-filter__body">
				<div v-if="activeName === 'first'" class="nf-shopsordersm-filter__item">
					<span class="nf-shopsordersm-filter__label">状态：</span>
					<el-select v-model="status" class="nf-shopsordersm-filter__select" @change="animeDat2(status)">
						<el-option v-for="item in taskstates" :key="item.value" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-shopsordersm-filter__item">
					<span class="nf-shopsordersm-filter__label">订单类型：</span>
					<el-select v-model="orderType" class="nf-shopsordersm-filter__select"
						@change="animeDat2(orderType)">
						<el-option v-for="item in orderTypes" :key="item.value" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-shopsordersm-filter__item">
					<span class="nf-shopsordersm-filter__label">订单号：</span>
					<el-input class="nf-shopsordersm-filter__input" @keydown.enter.native="phoneSelect" placeholder="请输入订单号"
						v-model="orderNumber">
					</el-input>
				</div>
				<div class="nf-shopsordersm-filter__item">
					<span class="nf-shopsordersm-filter__label">手机号：</span>
					<el-input class="nf-shopsordersm-filter__input" @keydown.enter.native="phoneSelect" placeholder="请输入手机号"
						v-model="phone">
					</el-input>
				</div>
				<div class="nf-shopsordersm-filter__item">
					<span class="nf-shopsordersm-filter__label">昵称：</span>
					<el-input class="nf-shopsordersm-filter__input" @keydown.enter.native="phoneSelect" placeholder="请输入昵称"
						v-model="userName">
					</el-input>
				</div>
				<div class="nf-shopsordersm-filter__actions">
					<el-button size="mini" type="primary" icon="document"
						@click="phoneSelect">查询
					</el-button>
					<el-button size="mini" type="primary" icon="document" @click="cleans2">重置
					</el-button>
				</div>
			</div>
		</div>

		<div class="nf-shopsordersm-tabs-wrap">
			<el-tabs v-model="activeName" @tab-click="handleClick" class="nf-shopsordersm-tabs">

			<el-tab-pane label="全部订单" name="first">
			<div class="nf-table-card nf-shopsordersm-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData2.list">
					<el-table-column fixed prop="orderId" label="编号" width="80"></el-table-column>
					<el-table-column fixed prop="userName" label="下单用户">
						<template slot-scope="scope">
							<span class="nf-shopsordersm-brand-link" @click="updates(scope.row)">
								{{ scope.row.userName ? scope.row.userName : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号"></el-table-column>
					<el-table-column prop="shopName" label="门店"></el-table-column>
					<el-table-column prop="orderType" label="订单类型">
						<template slot-scope="scope">
							<div>{{scope.row.orderType==1?'到店取餐':'外卖配送'}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="orderType" label="配送信息" width="300">
						<template slot-scope="scope">
							<div v-if="scope.row.orderType==2">
								<div>订餐人：{{scope.row.address.userName}}</div>
								<div>订餐人电话：{{scope.row.address.userPhone}}</div>
								<div>订餐地址：{{scope.row.address.address}}({{scope.row.address.addressDetail}})</div>
							</div>
							<div v-else style="text-align: center;width: 100%;"> - </div>
						</template>
					</el-table-column>
					<el-table-column prop="orderNumber" label="订单号" width="180"></el-table-column>
					<el-table-column prop="content" label="内容" width="300">
						<template slot-scope="scope">
							<div>
								<div v-for="(item,index) in scope.row.orderGoodsList" :key="index">
									<span>{{index+1}}.
										商品名：{{item.goodsName}},</span><span>数量：{{item.goodsNum}},</span><span>规格：{{item.skuMessage}}</span>;<span
										v-if="item.goodRemarks">备注：{{ item.goodRemarks }}</span>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="orderRemarks" label="订单备注" width="200">
					</el-table-column>
					<el-table-column prop="goodsPriceZ" label="商品总价" width="100">
					</el-table-column>
					<el-table-column prop="couponMoney" label="优惠金额" width="100">
						<template slot-scope="scope">
							<div>
								<div>
									<span>{{scope.row.couponMoney?scope.row.couponMoney:'0'}}</span>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="packMoney" label="打包费" width="100"></el-table-column>
					<el-table-column prop="errandMoney" label="跑腿费" width="100"></el-table-column>
					<el-table-column prop="payMoney" label="支付金额" width="100"></el-table-column>
					<el-table-column prop="payTime" label="支付时间"></el-table-column>
					<el-table-column prop="payType" label="支付方式" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.payType === 1 ">微信</span>
							<span v-if="scope.row.payType === 2 ">零钱</span>
						</template>
					</el-table-column>
					<el-table-column prop="cancelMoney" label="退款金额" width="100">
						<template slot-scope="scope">
							<div style="color:red">{{scope.row.cancelMoney?scope.row.cancelMoney:0}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="orderCode" label="取餐号" width="100"></el-table-column>
					<el-table-column fixed="right" prop="status" label="状态" width="100">
						<template slot-scope="scope">
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 0 ">待结算</span>
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 1 ">待支付</span>
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 3 ">
								{{scope.row.orderType==1?'待取餐':'待配送'}}
							</span>
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 4 ">已完成</span>
							<span style="color: #999;" v-if="scope.row.status === 5 ">已取消</span>
							<span style="color: #999;" v-if="scope.row.status === 6 ">制作中</span>
						</template>
					</el-table-column>
					<el-table-column fixed="right" prop="status" label="操作" width="250">
						<template slot-scope="scope">

							<el-button type="primary" v-if="scope.row.status === 6 "
								:disabled="!isAuth('missionSm:qucan')" @click="wancheng(scope.row)">制作已完成</el-button>
							<el-button type="danger" :disabled="!isAuth('missionSm:tuikuan')" style="margin: 10px;"
								v-if="scope.row.status === 3|| scope.row.status === 6"
								@click="tuikuan(scope.row)">退款</el-button>
							<el-button type="primary" v-if="scope.row.status === 3 "
								:disabled="!isAuth('missionSm:qucan')"
								@click="wancheng(scope.row)">{{scope.row.orderType==1?'餐品已完成，提示顾客取餐':'餐品已完成，等待配送'}}</el-button>
							<el-button type="primary"
								v-if="scope.row.status == 3 ||scope.row.status === 4 || scope.row.status === 6"
								:disabled="!isAuth('missionSm:qucan')" @click="dayin(scope.row)"
								style="margin: 3px;">打印小票</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="nf-shopsordersm-total-stat" v-if="totalMoney">本页累计交易统计：{{ totalMoney }}元
				</div>
				<div class="nf-shopsordersm-pagination">
					<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData2.totalCount">
					</el-pagination>
				</div>
			</div>
			</el-tab-pane>
			<!-- 待结算订单 -->
			<el-tab-pane label="待结算" name="daijiesuan">
			<div class="nf-table-card nf-shopsordersm-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData2.list">
					<el-table-column fixed prop="orderId" label="编号" width="80"></el-table-column>
					<el-table-column fixed prop="userName" label="下单用户">
						<template slot-scope="scope">
							<span class="nf-shopsordersm-brand-link" @click="updates(scope.row)">
								{{ scope.row.userName ? scope.row.userName : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号"></el-table-column>
					<el-table-column prop="shopName" label="门店"></el-table-column>
					<el-table-column prop="orderType" label="订单类型">
						<template slot-scope="scope">
							<div>{{scope.row.orderType==1?'到店取餐':'外卖配送'}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="orderType" label="配送信息" width="300">
						<template slot-scope="scope">
							<div v-if="scope.row.orderType==2">
								<div>订餐人：{{scope.row.address.userName}}</div>
								<div>订餐人电话：{{scope.row.address.userPhone}}</div>
								<div>订餐地址：{{scope.row.address.address}}({{scope.row.address.addressDetail}})</div>
							</div>
							<div v-else style="text-align: center;width: 100%;"> - </div>
						</template>
					</el-table-column>
					<el-table-column prop="orderNumber" label="订单号" width="180"></el-table-column>
					<el-table-column prop="content" label="内容" width="300">
						<template slot-scope="scope">
							<div>
								<div v-for="(item,index) in scope.row.orderGoodsList" :key="index">
									<span>{{index+1}}.
										商品名：{{item.goodsName}},</span><span>数量：{{item.goodsNum}},</span><span>规格：{{item.skuMessage}}</span>;<span
										v-if="item.goodRemarks">备注：{{ item.goodRemarks }}</span>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="orderRemarks" label="订单备注" width="200">
					</el-table-column>
					<el-table-column prop="goodsPriceZ" label="商品总价" width="100">
					</el-table-column>
					<el-table-column prop="couponMoney" label="优惠金额" width="100">
						<template slot-scope="scope">
							<div>
								<div>
									<span>{{scope.row.couponMoney?scope.row.couponMoney:'0'}}</span>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="packMoney" label="打包费" width="100"></el-table-column>
					<el-table-column prop="errandMoney" label="跑腿费" width="100"></el-table-column>
					<el-table-column prop="payMoney" label="支付金额" width="100"></el-table-column>
					<el-table-column prop="payTime" label="支付时间"></el-table-column>
					<el-table-column prop="payType" label="支付方式" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.payType === 1 ">微信</span>
							<span v-if="scope.row.payType === 2 ">零钱</span>
						</template>
					</el-table-column>
					<el-table-column prop="cancelMoney" label="退款金额" width="100">
						<template slot-scope="scope">
							<div style="color:red">{{scope.row.cancelMoney?scope.row.cancelMoney:0}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="orderCode" label="取餐号" width="100"></el-table-column>
					<el-table-column fixed="right" prop="status" label="状态" width="100">
						<template slot-scope="scope">
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 0 ">待结算</span>
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 1 ">待支付</span>
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 3 ">
								{{scope.row.orderType==1?'待取餐':'待配送'}}
							</span>
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 4 ">已完成</span>
							<span style="color: #999;" v-if="scope.row.status === 5 ">已取消</span>
							<span style="color: #999;" v-if="scope.row.status === 6 ">制作中</span>
						</template>
					</el-table-column>
					<el-table-column fixed="right" prop="status" label="操作" width="250">
						<template slot-scope="scope">
							<el-button type="primary" v-if="scope.row.status === 6 "
								:disabled="!isAuth('missionSm:qucan')" @click="wancheng(scope.row)">制作已完成</el-button>
							<el-button type="danger" :disabled="!isAuth('missionSm:tuikuan')" style="margin: 10px;"
								v-if="scope.row.status === 3|| scope.row.status === 6"
								@click="tuikuan(scope.row)">退款</el-button>
							<el-button type="primary" v-if="scope.row.status === 3 "
								:disabled="!isAuth('missionSm:qucan')"
								@click="wancheng(scope.row)">{{scope.row.orderType==1?'餐品已完成，提示顾客取餐':'餐品已完成，等待配送'}}</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="nf-shopsordersm-total-stat" v-if="totalMoney">本页累计交易统计：{{ totalMoney }}元
				</div>
				<div class="nf-shopsordersm-pagination">
					<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData2.totalCount">
					</el-pagination>
				</div>
			</div>
			</el-tab-pane>
			<!-- 待支付订单 -->
			<el-tab-pane label="待支付" name="daizhifu">
			<div class="nf-table-card nf-shopsordersm-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData2.list">
					<el-table-column fixed prop="orderId" label="编号" width="80"></el-table-column>
					<el-table-column fixed prop="userName" label="下单用户">
						<template slot-scope="scope">
							<span class="nf-shopsordersm-brand-link" @click="updates(scope.row)">
								{{ scope.row.userName ? scope.row.userName : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号"></el-table-column>
					<el-table-column prop="shopName" label="门店"></el-table-column>
					<el-table-column prop="orderType" label="订单类型">
						<template slot-scope="scope">
							<div>{{scope.row.orderType==1?'到店取餐':'外卖配送'}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="orderType" label="配送信息" width="300">
						<template slot-scope="scope">
							<div v-if="scope.row.orderType==2">
								<div>订餐人：{{scope.row.address.userName}}</div>
								<div>订餐人电话：{{scope.row.address.userPhone}}</div>
								<div>订餐地址：{{scope.row.address.address}}({{scope.row.address.addressDetail}})</div>
							</div>
							<div v-else style="text-align: center;width: 100%;"> - </div>
						</template>
					</el-table-column>
					<el-table-column prop="orderNumber" label="订单号" width="180"></el-table-column>
					<el-table-column prop="content" label="内容" width="300">
						<template slot-scope="scope">
							<div>
								<div v-for="(item,index) in scope.row.orderGoodsList" :key="index">
									<span>{{index+1}}.
										商品名：{{item.goodsName}},</span><span>数量：{{item.goodsNum}},</span><span>规格：{{item.skuMessage}}</span>;<span
										v-if="item.goodRemarks">备注：{{ item.goodRemarks }}</span>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="orderRemarks" label="订单备注" width="200">
					</el-table-column>
					<el-table-column prop="goodsPriceZ" label="商品总价" width="100">
					</el-table-column>
					<el-table-column prop="couponMoney" label="优惠金额" width="100">
						<template slot-scope="scope">
							<div>
								<div>
									<span>{{scope.row.couponMoney?scope.row.couponMoney:'0'}}</span>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="packMoney" label="打包费" width="100"></el-table-column>
					<el-table-column prop="errandMoney" label="跑腿费" width="100"></el-table-column>
					<el-table-column prop="payMoney" label="支付金额" width="100"></el-table-column>
					<el-table-column prop="payTime" label="支付时间"></el-table-column>
					<el-table-column prop="payType" label="支付方式" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.payType === 1 ">微信</span>
							<span v-if="scope.row.payType === 2 ">零钱</span>
						</template>
					</el-table-column>
					<el-table-column prop="cancelMoney" label="退款金额" width="100">
						<template slot-scope="scope">
							<div style="color:red">{{scope.row.cancelMoney?scope.row.cancelMoney:0}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="orderCode" label="取餐号" width="100"></el-table-column>
					<el-table-column fixed="right" prop="status" label="状态" width="100">
						<template slot-scope="scope">
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 0 ">待结算</span>
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 1 ">待支付</span>
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 3 ">
								{{scope.row.orderType==1?'待取餐':'待配送'}}
							</span>
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 4 ">已完成</span>
							<span style="color: #999;" v-if="scope.row.status === 5 ">已取消</span>
							<span style="color: #999;" v-if="scope.row.status === 6 ">制作中</span>
						</template>
					</el-table-column>
					<el-table-column fixed="right" prop="status" label="操作" width="250">
						<template slot-scope="scope">
							<el-button type="primary" v-if="scope.row.status === 6 "
								:disabled="!isAuth('missionSm:qucan')" @click="wancheng(scope.row)">制作已完成</el-button>
							<el-button type="danger" :disabled="!isAuth('missionSm:tuikuan')" style="margin: 10px;"
								v-if="scope.row.status === 3|| scope.row.status === 6"
								@click="tuikuan(scope.row)">退款</el-button>
							<el-button type="primary" v-if="scope.row.status === 3 "
								:disabled="!isAuth('missionSm:qucan')"
								@click="wancheng(scope.row)">{{scope.row.orderType==1?'餐品已完成，提示顾客取餐':'餐品已完成，等待配送'}}</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="nf-shopsordersm-total-stat" v-if="totalMoney">本页累计交易统计：{{ totalMoney }}元
				</div>
				<div class="nf-shopsordersm-pagination">
					<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData2.totalCount">
					</el-pagination>
				</div>
			</div>
			</el-tab-pane>
			<!-- 制作中订单 -->
			<el-tab-pane label="制作中" name="zhizuozhong">
			<div class="nf-table-card nf-shopsordersm-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData2.list">
					<el-table-column fixed prop="orderId" label="编号" width="80"></el-table-column>
					<el-table-column fixed prop="userName" label="下单用户">
						<template slot-scope="scope">
							<span class="nf-shopsordersm-brand-link" @click="updates(scope.row)">
								{{ scope.row.userName ? scope.row.userName : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号"></el-table-column>
					<el-table-column prop="shopName" label="门店"></el-table-column>
					<el-table-column prop="orderType" label="订单类型">
						<template slot-scope="scope">
							<div>{{scope.row.orderType==1?'到店取餐':'外卖配送'}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="orderType" label="配送信息" width="300">
						<template slot-scope="scope">
							<div v-if="scope.row.orderType==2">
								<div>订餐人：{{scope.row.address.userName}}</div>
								<div>订餐人电话：{{scope.row.address.userPhone}}</div>
								<div>订餐地址：{{scope.row.address.address}}({{scope.row.address.addressDetail}})</div>
							</div>
							<div v-else style="text-align: center;width: 100%;"> - </div>
						</template>
					</el-table-column>
					<el-table-column prop="orderNumber" label="订单号" width="180"></el-table-column>
					<el-table-column prop="content" label="内容" width="300">
						<template slot-scope="scope">
							<div>
								<div v-for="(item,index) in scope.row.orderGoodsList" :key="index">
									<span>{{index+1}}.
										商品名：{{item.goodsName}},</span><span>数量：{{item.goodsNum}},</span><span>规格：{{item.skuMessage}}</span>;<span
										v-if="item.goodRemarks">备注：{{ item.goodRemarks }}</span>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="orderRemarks" label="订单备注" width="200">
					</el-table-column>
					<el-table-column prop="goodsPriceZ" label="商品总价" width="100">
					</el-table-column>
					<el-table-column prop="couponMoney" label="优惠金额" width="100">
						<template slot-scope="scope">
							<div>
								<div>
									<span>{{scope.row.couponMoney?scope.row.couponMoney:'0'}}</span>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="packMoney" label="打包费" width="100"></el-table-column>
					<el-table-column prop="errandMoney" label="跑腿费" width="100"></el-table-column>
					<el-table-column prop="payMoney" label="支付金额" width="100"></el-table-column>
					<el-table-column prop="payTime" label="支付时间"></el-table-column>
					<el-table-column prop="payType" label="支付方式" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.payType === 1 ">微信</span>
							<span v-if="scope.row.payType === 2 ">零钱</span>
						</template>
					</el-table-column>
					<el-table-column prop="cancelMoney" label="退款金额" width="100">
						<template slot-scope="scope">
							<div style="color:red">{{scope.row.cancelMoney?scope.row.cancelMoney:0}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="orderCode" label="取餐号" width="100"></el-table-column>
					<el-table-column fixed="right" prop="status" label="状态" width="100">
						<template slot-scope="scope">
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 0 ">待结算</span>
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 1 ">待支付</span>
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 3 ">
								{{scope.row.orderType==1?'待取餐':'待配送'}}
							</span>
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 4 ">已完成</span>
							<span style="color: #999;" v-if="scope.row.status === 5 ">已取消</span>
							<span style="color: #999;" v-if="scope.row.status === 6 ">制作中</span>
						</template>
					</el-table-column>
					<el-table-column fixed="right" prop="status" label="操作" width="250">
						<template slot-scope="scope">
							<el-button type="primary" v-if="scope.row.status === 6 "
								:disabled="!isAuth('missionSm:qucan')" @click="wancheng(scope.row)">制作已完成</el-button>
							<el-button type="danger" :disabled="!isAuth('missionSm:tuikuan')" style="margin: 10px;"
								v-if="scope.row.status === 3|| scope.row.status === 6"
								@click="tuikuan(scope.row)">退款</el-button>
							<el-button type="primary" v-if="scope.row.status === 3 "
								:disabled="!isAuth('missionSm:qucan')"
								@click="wancheng(scope.row)">{{scope.row.orderType==1?'餐品已完成，提示顾客取餐':'餐品已完成，等待配送'}}</el-button>
							<el-button type="primary"
								v-if="scope.row.status == 3 ||scope.row.status === 4 || scope.row.status === 6"
								:disabled="!isAuth('missionSm:qucan')" @click="dayin(scope.row)"
								style="margin: 3px;">打印小票</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="nf-shopsordersm-total-stat" v-if="totalMoney">本页累计交易统计：{{ totalMoney }}元
				</div>
				<div class="nf-shopsordersm-pagination">
					<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData2.totalCount">
					</el-pagination>
				</div>
			</div>
			</el-tab-pane>
			<!-- 待取餐订单 -->
			<el-tab-pane label="待取餐/待配送" name="qucan">
			<div class="nf-table-card nf-shopsordersm-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData2.list">
					<el-table-column fixed prop="orderId" label="编号" width="80"></el-table-column>
					<el-table-column fixed prop="userName" label="下单用户">
						<template slot-scope="scope">
							<span class="nf-shopsordersm-brand-link" @click="updates(scope.row)">
								{{ scope.row.userName ? scope.row.userName : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号"></el-table-column>
					<el-table-column prop="shopName" label="门店"></el-table-column>
					<el-table-column prop="orderType" label="订单类型">
						<template slot-scope="scope">
							<div>{{scope.row.orderType==1?'到店取餐':'外卖配送'}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="orderType" label="配送信息" width="300">
						<template slot-scope="scope">
							<div v-if="scope.row.orderType==2">
								<div>订餐人：{{scope.row.address.userName}}</div>
								<div>订餐人电话：{{scope.row.address.userPhone}}</div>
								<div>订餐地址：{{scope.row.address.address}}({{scope.row.address.addressDetail}})</div>
							</div>
							<div v-else style="text-align: center;width: 100%;"> - </div>
						</template>
					</el-table-column>
					<el-table-column prop="orderNumber" label="订单号" width="180"></el-table-column>
					<el-table-column prop="content" label="内容" width="300">
						<template slot-scope="scope">
							<div>
								<div v-for="(item,index) in scope.row.orderGoodsList" :key="index">
									<span>{{index+1}}.
										商品名：{{item.goodsName}},</span><span>数量：{{item.goodsNum}},</span><span>规格：{{item.skuMessage}}</span>;<span
										v-if="item.goodRemarks">备注：{{ item.goodRemarks }}</span>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="orderRemarks" label="订单备注" width="200">
					</el-table-column>
					<el-table-column prop="goodsPriceZ" label="商品总价" width="100">
					</el-table-column>
					<el-table-column prop="couponMoney" label="优惠金额" width="100">
						<template slot-scope="scope">
							<div>
								<div>
									<span>{{scope.row.couponMoney?scope.row.couponMoney:'0'}}</span>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="packMoney" label="打包费" width="100"></el-table-column>
					<el-table-column prop="errandMoney" label="跑腿费" width="100"></el-table-column>
					<el-table-column prop="payMoney" label="支付金额" width="100"></el-table-column>
					<el-table-column prop="payTime" label="支付时间"></el-table-column>
					<el-table-column prop="payType" label="支付方式" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.payType === 1 ">微信</span>
							<span v-if="scope.row.payType === 2 ">零钱</span>
						</template>
					</el-table-column>
					<el-table-column prop="cancelMoney" label="退款金额" width="100">
						<template slot-scope="scope">
							<div style="color:red">{{scope.row.cancelMoney?scope.row.cancelMoney:0}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="orderCode" label="取餐号" width="100"></el-table-column>
					<el-table-column fixed="right" prop="status" label="状态" width="100">
						<template slot-scope="scope">
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 0 ">待结算</span>
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 1 ">待支付</span>
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 3 ">
								{{scope.row.orderType==1?'待取餐':'待配送'}}
							</span>
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 4 ">已完成</span>
							<span style="color: #999;" v-if="scope.row.status === 5 ">已取消</span>
							<span style="color: #999;" v-if="scope.row.status === 6 ">制作中</span>
						</template>
					</el-table-column>
					<el-table-column fixed="right" prop="status" label="操作" width="250">
						<template slot-scope="scope">
							<el-button type="primary" v-if="scope.row.status === 6 "
								:disabled="!isAuth('missionSm:qucan')" @click="wancheng(scope.row)">制作已完成</el-button>
							<el-button type="danger" :disabled="!isAuth('missionSm:tuikuan')" style="margin: 10px;"
								v-if="scope.row.status === 3|| scope.row.status === 6"
								@click="tuikuan(scope.row)">退款</el-button>
							<el-button type="primary"
								v-if="scope.row.status == 3 ||scope.row.status === 4 || scope.row.status === 6"
								:disabled="!isAuth('missionSm:qucan')" @click="dayin(scope.row)"
								style="margin: 3px;">打印小票</el-button>
							<el-button type="primary" v-if="scope.row.status === 3 "
								:disabled="!isAuth('missionSm:qucan')"
								@click="wancheng(scope.row)">{{scope.row.orderType==1?'餐品已完成，提示顾客取餐':'餐品已完成，等待配送'}}</el-button>

						</template>
					</el-table-column>
				</el-table>
				<div class="nf-shopsordersm-total-stat" v-if="totalMoney">本页累计交易统计：{{ totalMoney }}元
				</div>
				<div class="nf-shopsordersm-pagination">
					<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData2.totalCount">
					</el-pagination>
				</div>
			</div>
			</el-tab-pane>
			<!-- 已完成订单 -->
			<el-tab-pane label="已完成" name="wancheng">
			<div class="nf-table-card nf-shopsordersm-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData2.list">
					<el-table-column fixed prop="orderId" label="编号" width="80"></el-table-column>
					<el-table-column fixed prop="userName" label="下单用户">
						<template slot-scope="scope">
							<span class="nf-shopsordersm-brand-link" @click="updates(scope.row)">
								{{ scope.row.userName ? scope.row.userName : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号"></el-table-column>
					<el-table-column prop="shopName" label="门店"></el-table-column>
					<el-table-column prop="orderType" label="订单类型">
						<template slot-scope="scope">
							<div>{{scope.row.orderType==1?'到店取餐':'外卖配送'}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="orderType" label="配送信息" width="300">
						<template slot-scope="scope">
							<div v-if="scope.row.orderType==2">
								<div>订餐人：{{scope.row.address.userName}}</div>
								<div>订餐人电话：{{scope.row.address.userPhone}}</div>
								<div>订餐地址：{{scope.row.address.address}}({{scope.row.address.addressDetail}})</div>
							</div>
							<div v-else style="text-align: center;width: 100%;"> - </div>
						</template>
					</el-table-column>
					<el-table-column prop="orderNumber" label="订单号" width="180"></el-table-column>
					<el-table-column prop="content" label="内容" width="300">
						<template slot-scope="scope">
							<div>
								<div v-for="(item,index) in scope.row.orderGoodsList" :key="index">
									<span>{{index+1}}.
										商品名：{{item.goodsName}},</span><span>数量：{{item.goodsNum}},</span><span>规格：{{item.skuMessage}}</span>;<span
										v-if="item.goodRemarks">备注：{{ item.goodRemarks }}</span>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="orderRemarks" label="订单备注" width="200">
					</el-table-column>
					<el-table-column prop="goodsPriceZ" label="商品总价" width="100">
					</el-table-column>
					<el-table-column prop="couponMoney" label="优惠金额" width="100">
						<template slot-scope="scope">
							<div>
								<div>
									<span>{{scope.row.couponMoney?scope.row.couponMoney:'0'}}</span>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="packMoney" label="打包费" width="100"></el-table-column>
					<el-table-column prop="errandMoney" label="跑腿费" width="100"></el-table-column>
					<el-table-column prop="payMoney" label="支付金额" width="100"></el-table-column>
					<el-table-column prop="payTime" label="支付时间"></el-table-column>
					<el-table-column prop="payType" label="支付方式" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.payType === 1 ">微信</span>
							<span v-if="scope.row.payType === 2 ">零钱</span>
						</template>
					</el-table-column>
					<el-table-column prop="cancelMoney" label="退款金额" width="100">
						<template slot-scope="scope">
							<div style="color:red">{{scope.row.cancelMoney?scope.row.cancelMoney:0}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="orderCode" label="取餐号" width="100"></el-table-column>
					<el-table-column fixed="right" prop="status" label="状态" width="100">
						<template slot-scope="scope">
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 0 ">待结算</span>
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 1 ">待支付</span>
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 3 ">
								{{scope.row.orderType==1?'待取餐':'待配送'}}
							</span>
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 4 ">已完成</span>
							<span style="color: #999;" v-if="scope.row.status === 5 ">已取消</span>
							<span style="color: #999;" v-if="scope.row.status === 6 ">制作中</span>
						</template>
					</el-table-column>
					<el-table-column fixed="right" prop="status" label="操作" width="250">
						<template slot-scope="scope">
							<!-- <el-button type="primary" v-if="scope.row.status === 6 " :disabled="!isAuth('missionSm:qucan')"
                @click="wancheng(scope.row)">制作已完成</el-button>
              <el-button type="danger" :disabled="!isAuth('missionSm:tuikuan')" style="margin: 10px;"
                v-if="scope.row.status === 3|| scope.row.status === 6" @click="tuikuan(scope.row)">退款</el-button>
              <el-button type="primary" v-if="scope.row.status === 3 " :disabled="!isAuth('missionSm:qucan')"
                @click="wancheng(scope.row)">{{scope.row.orderType==1?'餐品已完成，提示顾客取餐':'餐品已完成，等待配送'}}</el-button> -->
							<el-button type="primary"
								v-if="scope.row.status == 3 ||scope.row.status === 4 || scope.row.status === 6"
								:disabled="!isAuth('missionSm:qucan')" @click="dayin(scope.row)"
								style="margin: 3px;">打印小票</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="nf-shopsordersm-total-stat" v-if="totalMoney">本页累计交易统计：{{ totalMoney }}元
				</div>
				<div class="nf-shopsordersm-pagination">
					<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData2.totalCount">
					</el-pagination>
				</div>
			</div>
			</el-tab-pane>
			<!-- 已取消订单 -->
			<el-tab-pane label="已取消" name="quxiao">
			<div class="nf-table-card nf-shopsordersm-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData2.list">
					<el-table-column fixed prop="orderId" label="编号" width="80"></el-table-column>
					<el-table-column fixed prop="userName" label="下单用户">
						<template slot-scope="scope">
							<span class="nf-shopsordersm-brand-link" @click="updates(scope.row)">
								{{ scope.row.userName ? scope.row.userName : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号"></el-table-column>
					<el-table-column prop="shopName" label="门店"></el-table-column>
					<el-table-column prop="orderType" label="订单类型">
						<template slot-scope="scope">
							<div>{{scope.row.orderType==1?'到店取餐':'外卖配送'}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="orderType" label="配送信息" width="300">
						<template slot-scope="scope">
							<div v-if="scope.row.orderType==2">
								<div>订餐人：{{scope.row.address.userName}}</div>
								<div>订餐人电话：{{scope.row.address.userPhone}}</div>
								<div>订餐地址：{{scope.row.address.address}}({{scope.row.address.addressDetail}})</div>
							</div>
							<div v-else style="text-align: center;width: 100%;"> - </div>
						</template>
					</el-table-column>
					<el-table-column prop="orderNumber" label="订单号" width="180"></el-table-column>
					<el-table-column prop="content" label="内容" width="300">
						<template slot-scope="scope">
							<div>
								<div v-for="(item,index) in scope.row.orderGoodsList" :key="index">
									<span>{{index+1}}.
										商品名：{{item.goodsName}},</span><span>数量：{{item.goodsNum}},</span><span>规格：{{item.skuMessage}}</span>;<span
										v-if="item.goodRemarks">备注：{{ item.goodRemarks }}</span>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="orderRemarks" label="订单备注" width="200">
					</el-table-column>
					<el-table-column prop="goodsPriceZ" label="商品总价" width="100">
					</el-table-column>
					<el-table-column prop="couponMoney" label="优惠金额" width="100">
						<template slot-scope="scope">
							<div>
								<div>
									<span>{{scope.row.couponMoney?scope.row.couponMoney:'0'}}</span>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="packMoney" label="打包费" width="100"></el-table-column>
					<el-table-column prop="errandMoney" label="跑腿费" width="100"></el-table-column>
					<el-table-column prop="payMoney" label="支付金额" width="100"></el-table-column>
					<el-table-column prop="payTime" label="支付时间"></el-table-column>
					<el-table-column prop="payType" label="支付方式" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.payType === 1 ">微信</span>
							<span v-if="scope.row.payType === 2 ">零钱</span>
						</template>
					</el-table-column>
					<el-table-column prop="cancelMoney" label="退款金额" width="100">
						<template slot-scope="scope">
							<div style="color:red">{{scope.row.cancelMoney?scope.row.cancelMoney:0}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="orderCode" label="取餐号" width="100"></el-table-column>
					<el-table-column fixed="right" prop="status" label="状态" width="100">
						<template slot-scope="scope">
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 0 ">待结算</span>
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 1 ">待支付</span>
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 3 ">
								{{scope.row.orderType==1?'待取餐':'待配送'}}
							</span>
							<span class="nf-shopsordersm-status-text" v-if="scope.row.status === 4 ">已完成</span>
							<span style="color: #999;" v-if="scope.row.status === 5 ">已取消</span>
							<span style="color: #999;" v-if="scope.row.status === 6 ">制作中</span>
						</template>
					</el-table-column>
				</el-table>
				<div class="nf-shopsordersm-total-stat" v-if="totalMoney">本页累计交易统计：{{ totalMoney }}元
				</div>
				<div class="nf-shopsordersm-pagination">
					<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tableData2.totalCount">
					</el-pagination>
				</div>
			</div>
			</el-tab-pane>
			<!-- 任务拒绝弹框 -->
			<el-dialog title="任务拒绝" :visible.sync="dialogFormVisible7" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">拒绝理由：</span>
					<el-input style="width:50%;" v-model="content" type="text" placeholder="请输入拒绝理由"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible7 = false">取 消</el-button>
					<el-button type="primary" @click="refuseto()">确 定</el-button>
				</div>
			</el-dialog>
			<!--    图片展示-->
			<el-dialog title="图片" :visible.sync="dialogVisible" width="35%" :before-close="handleClose">
				<span v-if="imageUrl.length>0" v-for="(item, index) in imageUrl" :key="index" style="width: 100px;
        height: 100px;display:inline-block;">
					<!--      <el-image style = "width: 100px; height: 100px" :src = "item" :preview-src-list="imageUrl"></el-image>-->
					<img :src="item" alt="" style="width: 100px; height: 100px">
				</span>
				<span v-else>暂无图片</span>
			</el-dialog>
		</el-tabs>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				dialogVisible: false,
				imageUrl: [],
				limit: 10,
				page: 1,
				totalMoney: 0,
				phone: '',
				status: '',
				describes: '',
				token: '',
				taskNum: '',
				taskMoney: '',
				statesnum: [{
						label: '全部',
						value: 0
					},
					{
						label: '待接单',
						value: 2
					},
					{
						label: '待送达',
						value: 3
					},
					{
						label: '已完成',
						value: 4
					},
					{
						label: '已下架',
						value: 5
					},
				],
				taskstates: [{
						label: '全部',
						value: ''
					},
					{
						label: '待结算',
						value: 0
					},
					{
						label: '待支付',
						value: 1
					},
					{
						label: '制作中',
						value: 6
					},
					{
						label: '待取餐', //待取餐、派送中
						value: 3
					},
					{
						label: '已完成',
						value: 4
					},
					{
						label: '已取消',
						value: 5
					}
				],
				orderType: '',
				orderTypes: [{
						label: '全部',
						value: ''
					},
					{
						label: '到店取餐',
						value: 1
					},
					{
						label: '外卖配送',
						value: 2
					},
				],
				shopId: '',
				shopList: '',
				sort: 10,
				id: '',
				btnChangeEnable: true,
				helpTakeId: '',
				helpMaintainId: '',
				content: '',
				classifyIcon: '',
				classifyId: '',
				classifyUrl: '',
				classifyName: '',
				classifyDeatilsName: '',
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				dialogFormVisible4: false,
				dialogFormVisible5: false,
				dialogFormVisible6: false,
				dialogFormVisible7: false,
				dialogFormVisible8: false,
				tableData: [],
				tableData2: [],
				form: {
					id: '',
					classifyName: '',
					classifyUrl: '',
					classifyIcon: '',
					describes: ''
				},
				form2: {
					id: '',
					classifyDeatilsName: '',
					classifyUrl: '',
					classifyIcon: '',
					taskNum: '',
					taskMoney: '',
				},
				protectjieData: [],
				protectpaiData: [],
				helpTaskData: [],
				safeguardData: [],
				classifyData: [],
				checkBoxData: [], //多选框选择的值
				checkBoxData2: [], //多选框选择的值
				userName: '',
				orderNumber: '',
			}
		},
		methods: {
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			// 图标上传一级分类
			handleAvatarSuccess(file) {
				this.classifyIcon = file.data
			},
			// 图标上传一级分类编辑
			handleAvatarSuccess2(file) {
				this.form.classifyIcon = file.data
			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			// 多选
			changeFun2(val) {
				this.checkBoxData2 = val
			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange2(val) {
				this.limit = val
				this.classifySelect()
			},
			handleCurrentChange2(val) {
				this.page = val
				this.classifySelect()
			},
			handleSizeChange3(val) {
				this.limit = val
				this.taskdataSelect()
			},
			handleCurrentChange3(val) {
				this.page = val
				this.taskdataSelect()
			},
			// tabs切换
			handleClick(tab, event) {

				if (tab._props.label == '全部订单') {
					this.page = 1
					this.limit = 10
					this.status = ''
					this.taskdataSelect()
				}
				if (tab._props.label == '待结算') {
					this.page = 1
					this.limit = 10
					this.status = 0
					this.taskdataSelect()
				}
				if (tab._props.label == '待支付') {
					this.page = 1
					this.limit = 10
					this.status = 1
					this.taskdataSelect()
				}
				if (tab._props.label == '制作中') {
					this.page = 1
					this.limit = 10
					this.status = 6
					this.taskdataSelect()
				}
				if (tab._props.label == '待取餐/待配送') {
					this.page = 1
					this.limit = 10
					this.status = 3
					this.taskdataSelect()
				}
				if (tab._props.label == '已完成') {
					this.page = 1
					this.limit = 10
					this.status = 4
					this.taskdataSelect()
				}
				if (tab._props.label == '已取消') {
					this.page = 1
					this.limit = 10
					this.status = 5
					this.taskdataSelect()
				}

			},
			// 下架
			soldClick(row) {
				this.$confirm(`确定要下架此任务?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`help/outHelpOrder/?helpOrderId=${row.id}`),
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
			//查看照片
			refund(e) {
				this.imageUrl = []
				if (e.image != null) {
					let img = e.image.split(',')
					if (img.length != 0) {
						this.imageUrl = img
					}
				}
				this.dialogVisible = true
			},
			handleClose(done) {
				// this.$confirm('确认关闭？')
				//   .then(_ => {
				done()
				// })
				// .catch(_ => {});
			},
			// 添加一级分类弹框
			classifyStair() {
				this.dialogFormVisible = true
			},
			// 添加一级分类确定
			StairNoticeTo() {
				if (this.classifyName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入分类名称',
						type: 'warning'
					})
					return
				}
				// if (this.classifyUrl == '') {
				//     this.$notify({
				//         title: '提示',
				//         duration: 1800,
				//         message: '请输入分类链接',
				//         type: 'warning'
				//     });
				//     return
				// }
				if (this.describes == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入分类描述',
						type: 'warning'
					})
					return
				}
				if (this.classifyIcon == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传分类图标',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl('helpClassify/saveHelpClassify'),
					method: 'post',
					data: this.$http.adornData({
						'classifyName': this.classifyName,
						'state': this.state,
						'sort': this.sort,
						'classifyIcon': this.classifyIcon,
						'classifyUrl': this.classifyUrl,
						'describes': this.describes
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible = false
					this.$message({
						message: '一级分类添加成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.classifyUrl = ''
							this.classifyName = ''
							this.classifyIcon = ''
							this.describes = ''
							this.classifySelect()
						}
					})
				})
			},
			// 修改一级分类
			compile(index, rows) {
				this.dialogFormVisible2 = true
				this.form.id = rows.id
				this.form.classifyName = rows.classifyName
				this.form.classifyUrl = rows.classifyUrl
				this.form.classifyIcon = rows.classifyIcon
				this.form.describes = rows.describes
			},
			// 修改一级分类确定
			CompileNoticeTo() {
				if (this.form.classifyName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入分类名称',
						type: 'warning'
					})
					return
				}
				if (this.form.describes == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入分类描述',
						type: 'warning'
					})
					return
				}
				if (this.form.classifyIcon == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传分类图标',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl('helpClassify/updateHelpClassify'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.id,
						'classifyName': this.form.classifyName,
						'classifyUrl': this.form.classifyUrl,
						'classifyIcon': this.form.classifyIcon,
						'describes': this.form.describes
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible2 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.classifySelect()
						}
					})
				})
			},
			//删除一级
			deleteStair(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`helpClassify/deleteClassifyById/?id=${delid}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.name = ''
								this.classifySelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 手机号搜索
			phoneSelect() {
				this.page = 1
				this.limit = 10
				this.taskdataSelect()
			},
			// 重置
			cleans2() {
				this.phone = ''
				this.userName = ''
				this.orderNumber = ''
				this.status = ''
				this.orderType = ''
				this.taskdataSelect()
			},
			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.phone = ''
				this.status = 0
				this.content = ''
				this.dataSelect()
			},
			// select选择事件
			animeDat(state) {
				this.page = 1
				this.dataSelect()
			},
			animeDat2(state) {
				this.page = 1
				this.taskdataSelect()
			},
			// 获取派单数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('help/selectStatusHelpOrder'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'phone': this.phone,
						'status': this.status,
						'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			},
			// 获取接单数据列表
			taskdataSelect() {
				this.totalMoney = 0
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/order/selectAllOrder'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'phone': this.phone,
						'userName': this.userName,
						'orderNumber': this.orderNumber,
						'status': this.status,
						'shopId': this.shopId,
						'orderType': this.orderType
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData2 = returnData
					for (var i in this.tableData2.list) {
						this.tableData2.list[i].goodsPriceZ = 0
						this.totalMoney = this.totalMoney + this.tableData2.list[i].payMoney
						if (this.tableData2.list[i].address) {
							this.tableData2.list[i].address = JSON.parse(this.tableData2.list[i].address)
						}
						for (var j in this.tableData2.list[i].orderGoodsList) {
							this.tableData2.list[i].orderGoodsList[j].goodsPriceZ = (this.tableData2.list[i].orderGoodsList[j].goodsPrice * this.tableData2.list[i].orderGoodsList[j].goodsNum).toFixed(2)
							this.tableData2.list[i].goodsPriceZ = (Number(this.tableData2.list[i].goodsPriceZ) + Number(this.tableData2.list[i].orderGoodsList[j].goodsPriceZ)).toFixed(2)
						}
					}
					this.totalMoney = this.totalMoney.toFixed(2)
				})
			},
			// 门店列表
			classifySelect() {
				this.$http({
					url: this.$http.adornUrl('admin/goods/selectAllShopList'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.shopList = returnData
				})
			},
			// 返回上一级
			prev() {
				this.$router.back()
			},
			// 餐品完成
			wancheng(row) {
				if (row.status == 3) {
					var status1 = 4
				}
				if (row.status == 6) {
					var status1 = 3
				}
				this.$http({
					url: this.$http.adornUrl('admin/order/updateOrder'),
					method: 'post',
					data: this.$http.adornData({
						'status': status1,
						'orderId': row.orderId,
						'orderNumber': row.orderNumber
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
								this.taskdataSelect()
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
			// 退款
			tuikuan(row) {
				console.log('退款')
				this.$confirm(`确定要取消退款?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					console.log('取定')
					this.$http({
						url: this.$http.adornUrl('admin/order/adminCancelOrder'),
						method: 'post',
						params: this.$http.adornParams({
							'orderId': row.orderId
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
									this.taskdataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {}
							})
						}
					})
				}).catch(() => {
					console.log('取消')
				})
			},
			// 打印小票
			dayin(row) {
				this.$http({
					url: this.$http.adornUrl('admin/order/print'),
					method: 'post',
					params: this.$http.adornParams({
						'orderId': row.orderId
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
								this.taskdataSelect()
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
		},
		mounted() {
			this.shopId = this.$route.query.shopId
			this.classifySelect()
			this.taskdataSelect()

		}
	}
</script>

<style scoped>
.nf-shopsordersm-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-shopsordersm-hero {
  margin-bottom: 12px;
}

.nf-shopsordersm-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-shopsordersm-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-shopsordersm-filter {
  margin-bottom: 12px;
}

.nf-shopsordersm-filter__body {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
}

.nf-shopsordersm-filter__item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nf-shopsordersm-filter__label {
  font-size: 13px;
  color: var(--nf-text-secondary, #606266);
  white-space: nowrap;
}

.nf-shopsordersm-filter__select {
  width: 150px;
}

.nf-shopsordersm-filter__input {
  width: 200px;
}

.nf-shopsordersm-filter__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-left: auto;
}

.nf-shopsordersm-tabs-wrap {
  margin-bottom: 0;
}

.nf-shopsordersm-tabs {
  margin-bottom: 0;
}

.nf-shopsordersm-tabs >>> .el-tabs__header {
  margin-bottom: 0;
  background: #fff;
  border-radius: 8px 8px 0 0;
  padding: 0 16px;
  border: 1px solid var(--nf-border, #e8eaed);
  border-bottom: none;
}

.nf-shopsordersm-table-card {
  margin-bottom: 0;
  border-radius: 0 0 8px 8px;
  border-top: none;
}

.nf-shopsordersm-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.nf-shopsordersm-total-stat {
  color: #b94a48;
  font-size: 16px;
  font-weight: 600;
  margin-top: 12px;
}

.nf-shopsordersm-brand-link {
  color: var(--nf-brand, #ffa8c2);
  cursor: pointer;
}

.nf-shopsordersm-status-text {
  color: var(--nf-brand, #ffa8c2);
}
</style>
