<template>
	<div class="nf-page nf-userdetail-page">
		<div class="nf-page-hero nf-userdetail-hero">
			<a href="#" class="nf-userdetail-back" @click.prevent="prev">
				<icon-svg name="jiantou" class="nf-userdetail-back__icon"></icon-svg>
				返回
			</a>
			<span class="nf-page-badge">用户</span>
			<h2 class="nf-userdetail-hero__title">用户详情</h2>
			<p class="nf-userdetail-hero__desc">查看用户基本信息、钱包与订单记录，支持修改用户状态与余额</p>
		</div>

		<div class="nf-userdetail-tabs-wrap">
			<el-tabs v-model="activeName" @tab-click="handleClick" class="nf-userdetail-tabs">
			<el-tab-pane label="用户详情" name="first">
				<div class="nf-table-card nf-userdetail-info-card">
					<div class="nf-filter-card__head nf-userdetail-info-card__head">
						<i class="el-icon-user"></i>
						<span>基本信息</span>
					</div>
					<div class="nf-userdetail-info-table">
						<table>
							<tbody>
								<tr>
									<th>本月下单数量</th>
									<td>{{ tablenum.orderMonth ? `${tablenum.orderMonth}单` : '0单' }}</td>
									<th>本月充值金额</th>
									<td>{{ tablenum.topUpMonth ? `${tablenum.topUpMonth}元` : '0元' }}</td>
									<th>openId</th>
									<td class="border-rt">{{ tableData.openId ? tableData.openId : '未绑定' }}</td>
									<!-- <th>本月邀请数量</th>
                  <td class = "border-rt">{{ tablenum.inviteCount ? `${tablenum.inviteCount}人` : '0人' }}</td> -->
								</tr>
								<!-- <tr>
                  <th>本月派单数量</th>
                  <td>{{ tablenum.helpTakeCount ? `${tablenum.helpTakeCount}笔` : '0笔' }}</td>
                  <th>本月提现金额</th>
                  <td>{{ tablenum.cashOutMoney ? `${tablenum.cashOutMoney}元` : '0元' }}</td>
                  <th>openId</th>
                  <td class = "border-rt">{{ tableData.openId ? tableData.openId : '未绑定' }}</td>
                </tr> -->
								<tr>
									<th>用户状态</th>
									<td>
										<span v-if="tableData.status == 1">正常</span>
										<span v-if="tableData.status == 2" style="color: #f56c6c;">禁用</span>
										<!-- <span style = "color: #4f9dec;cursor:pointer;" @click = "stateChange(tableData.userId)">更改状态</span> -->
										<el-button size="mini" type="text" class="nf-userdetail-text-btn"
											:disabled="!isAuth('userList:updateStatus')"
											@click="stateChange(tableData.userId)">更改状态
										</el-button>
									</td>
									<th>余额</th>
									<td style="color: rgb(245, 108, 108)">
										{{ tablemoney.money ? `${tablemoney.money}元` : '0元' }}
										<el-button size="mini" type="text" class="nf-userdetail-text-btn"
											:disabled="!isAuth('userList:updateMoney')"
											@click="rechargenone(tableData.userId)">修改余额
										</el-button>
										<!-- <span style = "color: #4f9dec;cursor:pointer;" @click = "rechargenone(tableData.userId)">修改余额</span> -->
									</td>
									<th>编号</th>
									<td class="border-rt">
										{{ tableData.userId }}
									</td>

								</tr>
								<tr>
									<th>创建时间</th>
									<td>{{ tableData.createTime }}</td>
									<th>手机号</th>
									<td class="nf-userdetail-editable" @click="setFrom.setPhone = tableData.phone; showPhone = true; upType = 1">
										{{ tableData.phone ? tableData.phone : '未绑定' }}
										<i class="el-icon-edit"></i>
									</td>
									<th>图像</th>
									<td class="border-rt nf-userdetail-editable"
										@click="setFrom.avatar = tableData.avatar; showPhone = true; upType = 3">
										<img :src="tableData.avatar ? tableData.avatar : '~@/assets/img/avatar.png'"
											width="80" height="80" />
									</td>
								</tr>
								<tr>
									<th>微信名称</th>
									<td class="nf-userdetail-editable"
										@click="setFrom.userName = tableData.userName; showPhone = true; upType = 2">
										{{ tableData.userName }}
										<i class="el-icon-edit"></i>
									</td>
									<th>支付宝账号</th>
									<td>{{ tableData.zhifubao ? tableData.zhifubao : '未绑定' }}</td>
									<th>支付宝姓名</th>
									<td class="border-rt">{{ tableData.zhifubaoName ? tableData.zhifubaoName : '未绑定' }}
									</td>
								</tr>
								<tr>
									<th>积分</th>
									<td>
										{{ integralNum ? integralNum : '0' }}
									</td>
									<!-- <th>是否是店铺管理员</th>
									<td>
										{{ tableData.shopAdminFlag == 1 ? '是' : '否' }}
									</td> -->
									<th>是否是会员</th>
									<td>
										{{ tableData.member == 1 ? '是' : '否' }}
									</td>
									<th>会员到期时间</th>
									<td class=" border-rt">
										{{ tableData.endTime }}
									</td>
								</tr>
								<tr>
									<th class="border-bt">邮箱</th>
									<td class="border-bt">
										{{tableData.email}}
										<el-button size="mini" type="text" class="nf-userdetail-text-btn"
											@click="setFrom.email = tableData.email; showPhone = true; upType = 4">修改</el-button>
									</td>
									
									<th class="border-bt"></th>
									<td class="border-bt">
									</td>
									<th class="border-bt"></th>
									<td class="border-bt border-rt">
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</el-tab-pane>
			<!--      <el-tab-pane label = "用户消息" name = "third">
              <el-table v-loading = "tableDataLoading" :data = "userData.list">
                <el-table-column prop = "id" label = "编号" width = "80">
                </el-table-column>
                <el-table-column prop = "userName" label = "用户名称">
                </el-table-column>
                <el-table-column prop = "title" label = "消息标题">
                </el-table-column>
                <el-table-column prop = "content" label = "消息内容" width = "280">
                </el-table-column>
                <el-table-column prop = "createAt" label = "创建时间" width = "150">
                </el-table-column>
              </el-table>
              <div style = "text-align: center;margin-top: 10px;">
                <el-pagination @size-change = "handleSizeChange1" @current-change = "handleCurrentChange1" :page-sizes = "[10,  20, 30, 50, 100]"
                               :page-size = "limit" :current-page = "page" layout = "total,sizes, prev, pager, next"
                               :total = "userData.totalCount">
                </el-pagination>
              </div>
            </el-tab-pane>-->
			<!-- <el-tab-pane label="任务消息" name="fifth">
        <el-table
        v-loading="tableDataLoading"
          :data="userData.list">
        <el-table-column
         prop="id"
         label="编号"
         width="80">
         </el-table-column>
         <el-table-column
          prop="userName"
          label="用户名称">
          </el-table-column>
          <el-table-column
           prop="title"
           label="消息标题">
           </el-table-column>
          <el-table-column
          prop="content"
          label="消息内容"
          width="280">
          </el-table-column>
          <el-table-column
          prop="createAt"
          label="创建时间"
          width="150">
          </el-table-column>
        </el-table>
        <div style="text-align: center;margin-top: 10px;">
          <el-pagination
            @size-change="handleSizeChange1"
            @current-change="handleCurrentChange1"
            :page-sizes="[10, 20, 30,50,100]"
            :page-size="limit"
            :current-page="page"
            layout="total,sizes, prev, pager, next"
            :total="userData.totalCount">
          </el-pagination>
        </div>
      </el-tab-pane> -->
			<!--			<el-tab-pane label="提现记录" name="sixth">
              <el-table
              v-loading="tableDataLoading"
                :data="withdrawData.list">
              <el-table-column
               prop="id"
               label="编号"
               width="80">
               </el-table-column>
               <el-table-column
                prop="zhifubao"
                label="支付宝账号">
                </el-table-column>
                <el-table-column
                 prop="zhifubaoName"
                 label="支付宝名称">
                 </el-table-column>
                <el-table-column
                prop="money"
                label="提现金额">
                <template slot-scope="scope">
                  <span style="color: #f56c6c;">{{scope.row.money}}</span>
                </template>
                </el-table-column>
                <el-table-column
                prop="orderNumber"
                label="付款编号">
                </el-table-column>
                <el-table-column
                prop="createAt"
                label="创建时间">
                </el-table-column>
                <el-table-column
                prop="outAt"
                label="结束时间">
                </el-table-column>
                <el-table-column
                 fixed='right'
                prop="state"
                label="状态"
                width="100">
                <template slot-scope="scope">
                  <span style="color: #4f9dec;" v-if="scope.row.state === 1 ">提现成功</span>
                  <span style="color: #4f9dec;" v-if="scope.row.state === -1 ">已退款</span>
                </template>
                </el-table-column>
              </el-table>
              <div style="text-align: center;margin-top: 10px;">
                <el-pagination
                  @size-change="handleSizeChange2"
                  @current-change="handleCurrentChange2"
                 :page-sizes="[10, 20, 30,50,100]"
                 :page-size="limit"
                  :current-page="page"
                  layout="total,sizes, prev, pager, next"
                  :total="walletData.totalCount">
                </el-pagination>
              </div>
            </el-tab-pane>-->
			<el-tab-pane label="钱包明细" name="eighth">
				<div class="nf-table-card nf-userdetail-table-card">
				<el-table v-loading="tableDataLoading" :data="walletData.records">
					<el-table-column prop="id" label="编号" width="100">
					</el-table-column>
					<el-table-column prop="title" label="标题" width="120">
					</el-table-column>
					<el-table-column prop="content" label="内容">
					</el-table-column>
					<el-table-column prop="money" label="金额" width="100">
						<template slot-scope="scope">
							<span style="color: #f56c6c;" v-if="scope.row.type == 2">- {{ scope.row.money }}</span>
							<span style="color: #008200;" v-if="scope.row.type == 1">+ {{ scope.row.money }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180">
					</el-table-column>
				</el-table>
				<div class="nf-userdetail-pagination">
					<el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="walletData.total">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>

			<el-tab-pane label="充值明细" name="seventh">
				<div class="nf-filter-card nf-userdetail-filter">
					<div class="nf-filter-card__head">
						<i class="el-icon-search"></i>
						<span>筛选条件</span>
					</div>
					<div class="nf-filter-card__body nf-userdetail-filter__body">
						<div class="nf-userdetail-filter__item">
							<span class="nf-userdetail-filter__label">开始时间：</span>
							<el-date-picker class="nf-userdetail-filter__date" v-model="startTime" align="right"
								type="datetime" default-time="00:00:00" placeholder="选择开始时间">
							</el-date-picker>
						</div>
						<div class="nf-userdetail-filter__item">
							<span class="nf-userdetail-filter__label">截止时间：</span>
							<el-date-picker class="nf-userdetail-filter__date" v-model="endTime" align="right" type="datetime"
								default-time="23:59:59" placeholder="选择截止时间">
							</el-date-picker>
						</div>
						<div class="nf-userdetail-filter__actions">
							<el-button size="mini" type="primary" icon="document" @click="timeDate2">查询</el-button>
						</div>
					</div>
				</div>
				<div class="nf-table-card nf-userdetail-table-card">
				<el-table v-loading="tableDataLoading" :data="rechargeData.list">
					<el-table-column prop="id" label="编号" width="50">
					</el-table-column>
					<el-table-column prop="orderId" label="充值订单号" width="200">
					</el-table-column>
					<el-table-column prop="money" label="充值金额">
					</el-table-column>
					<el-table-column prop="userId" label="会员编号">
					</el-table-column>
					<!-- <el-table-column label = "分类">
            <template slot-scope = "scope">
              <span style = "color: #4f9dec;cursor: pointer;" v-if = "scope.row.classify == 1">微信</span>
              <span style = "color: #4f9dec;cursor: pointer;" v-if = "scope.row.classify == 2">支付宝</span>
            </template>
          </el-table-column> -->
					<el-table-column label="状态">
						<template slot-scope="scope">
							<span class="nf-userdetail-status-text" v-if="scope.row.state == 0">待支付</span>
							<span class="nf-userdetail-status-text" v-if="scope.row.state == 1">支付成功</span>
							<span class="nf-userdetail-status-text" v-if="scope.row.state == 2">支付失败</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="170">
					</el-table-column>
					<el-table-column prop="payTime" label="支付时间" width="170">
					</el-table-column>
				</el-table>
				<div class="nf-userdetail-pagination">
					<el-pagination @size-change="handleSizeChange4" @current-change="handleCurrentChange4"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="rechargeData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="积分明细" name="jifen">
				<div class="nf-table-card nf-userdetail-table-card">
				<el-table v-loading="tableDataLoading" :data="jifenData.list">
					<el-table-column prop="id" label="编号" width="100">
					</el-table-column>
					<!-- <el-table-column prop = "title" label = "标题" width = "120">
          </el-table-column> -->
					<el-table-column prop="content" label="内容">
					</el-table-column>
					<el-table-column prop="num" label="金额" width="100">
						<template slot-scope="scope">
							<span style="color: #006600;" v-if="scope.row.type == 1">+ {{ scope.row.num }}</span>
							<span style="color: #f56c6c;" v-if="scope.row.type == 2">- {{ scope.row.num }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180">
					</el-table-column>
				</el-table>
				<div class="nf-userdetail-pagination">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="jifenData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的订单" name="tenth">
				<div class="nf-table-card nf-userdetail-table-card">
				<el-table v-loading="tableDataLoading" :data="takeData.list">
					<el-table-column prop="orderId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="orderNumber" label="订单编号" width="150">
					</el-table-column>
					<el-table-column prop="phone" label="手机号"></el-table-column>
					<el-table-column prop="shopName" label="门店"></el-table-column>
					<el-table-column prop="orderType" label="订单类型">
						<template slot-scope="scope">
							<div>{{ scope.row.orderType == 1 ? '到店取餐' : '外卖配送' }}</div>
						</template>
					</el-table-column>
					<el-table-column prop="content" label="内容" width="300">
						<template slot-scope="scope">
							<div>
								<div v-for="(item, index) in scope.row.orderGoodsList" :key="index">
									<span>{{ index + 1 }}.
										商品名：{{ item.goodsName }},</span><span>数量：{{ item.goodsNum }},</span><span>规格：{{
											item.skuMessage }}</span>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="orderRemarks" label="订单备注" width="200">
					</el-table-column>
					<el-table-column prop="payMoney" label="支付金额" width="100"></el-table-column>
					<el-table-column prop="payTime" label="支付时间"></el-table-column>
					<el-table-column prop="orderCode" label="取餐号" width="100"></el-table-column>
					<el-table-column fixed="right" prop="status" label="状态" width="100">
						<template slot-scope="scope">
							<span class="nf-userdetail-status-text" v-if="scope.row.status === 0">待结算</span>
							<span class="nf-userdetail-status-text" v-if="scope.row.status === 1">待支付</span>
							<span class="nf-userdetail-status-text" v-if="scope.row.status === 3">待取餐</span>
							<span class="nf-userdetail-status-text" v-if="scope.row.status === 4">已完成</span>
							<span class="nf-userdetail-status-text" v-if="scope.row.status === 5">已取消</span>
							<span class="nf-userdetail-status-text" v-if="scope.row.status === 6">制作中</span>
						</template>
					</el-table-column>
				</el-table>
				<div class="nf-userdetail-pagination">
					<el-pagination @size-change="handleSizeChange6" @current-change="handleCurrentChange6"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="takeData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的消息" name="message">
				<div class="nf-table-card nf-userdetail-table-card">
				<el-table v-loading="tableDataLoading" :data="userData.list">
					<el-table-column fixed prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="用户名称">
					</el-table-column>
					<el-table-column prop="title" label="消息标题">
					</el-table-column>
					<el-table-column prop="content" label="消息内容" width="280">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
				</el-table>
				<div class="nf-userdetail-pagination">
					<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="userData.totalCount">
					</el-pagination>
				</div>
				<!-- 消息推送 -->
				<el-dialog title="消息推送" :visible.sync="dialogFormVisible" center>
					<div style="margin-bottom: 10px;">
						<span style="width: 200px;display: inline-block;text-align: right;">消息标题：</span>
						<el-input style="width: 50%;" v-model="title" placeholder="请输入消息标题"></el-input>
					</div>
					<div style="margin-bottom: 10px;">
						<span
							style="width: 200px;display: inline-block;text-align: right;position: relative;top: -65px;">消息内容：</span>
						<el-input style="width: 50%;" type="textarea" rows="4" v-model="content" placeholder="请输入消息内容">
						</el-input>
					</div>
					<div slot="footer" class="dialog-footer">
						<el-button @click="dialogFormVisible = false">取 消</el-button>
						<el-button type="primary" @click="magNoticeTo()">确 定</el-button>
					</div>
				</el-dialog>
				</div>
			</el-tab-pane>

			<el-tab-pane label="我的优惠券" name="youhuiquan">
				<div class="nf-table-card nf-userdetail-table-card">
				<el-table v-loading="tableDataLoading" :data="youhuiquanData.list">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="couponName" label="优惠券名称">
					</el-table-column>
					<el-table-column prop="couponPicture" label="图片">
						<template slot-scope="scope">
							<img :src="scope.row.couponPicture" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="money" label="优惠券金额">
					</el-table-column>
					<el-table-column prop="minMoney" label="最低消费">
					</el-table-column>
					<el-table-column prop="needIntegral" label="兑换积分">
					</el-table-column>
					<el-table-column prop="createTime" label="领取时间">
					</el-table-column>
					<el-table-column prop="expirationTime" label="过期时间">
					</el-table-column>
					<el-table-column prop="expirationTime" label="状态">
						<template slot-scope="scope">
							<span v-if="scope.row.status == 0">待使用</span>
							<span v-else-if="scope.row.status == 1">已使用</span>
							<span v-else-if="scope.row.status == 2">已失效</span>
						</template>
					</el-table-column>
				</el-table>
				<div class="nf-userdetail-pagination">
					<el-pagination @size-change="handleSizeChange7" @current-change="handleCurrentChange7"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="youhuiquanData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的地址" name="dizhi">
				<div class="nf-table-card nf-userdetail-table-card">
				<div class="nf-userdetail-table-toolbar">
					<el-button size="mini" type="primary" icon="document"
						:disabled="!isAuth('mission:add')" @click="adddates(0)">添加
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="dizhiData.list">
					<el-table-column prop="addressId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="用户姓名" width="100">
					</el-table-column>
					<el-table-column prop="userPhone" label="用户电话" width="120">
					</el-table-column>
					<el-table-column prop="money" label="详细地址">
						<template slot-scope="scope">
							<span>{{ scope.row.address }}({{ scope.row.addressDetail }})</span>
						</template>
					</el-table-column>
					<el-table-column prop="minMoney" label="是否默认" width="80">
						<template slot-scope="scope">
							<span v-if="scope.row.addressDefault == 0">否</span>
							<span v-else-if="scope.row.addressDefault == 1">是</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="150">
					</el-table-column>
					<el-table-column label="操作" width="180">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('mission:update')"
								@click="adddates(scope.row)">
								编辑
							</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('mission:delete')"
								@click="deleteStair(scope.row)">删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div class="nf-userdetail-pagination">
					<el-pagination @size-change="handleSizeChange8" @current-change="handleCurrentChange8"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="dizhiData.totalCount">
					</el-pagination>
				</div>
				</div>
			</el-tab-pane>

			<!-- 不可提现添加金额 -->
			<el-dialog title="添加金额" :visible.sync="dialogFormVisible1" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">修改类型：</span>
					<el-radio-group v-model="type">
						<el-radio :label="1">添加</el-radio>
						<el-radio :label="2">减少</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">金额：</span>
					<el-input style="width:50%;" v-model="money" type="number" :min="0" :max="100" :controls="false"
						placeholder="请输入金额"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible1 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo1()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 添加修改门店 -->
			<el-dialog :title="titles" :visible.sync="dialogFormVisible2" center>

				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">用户姓名：</span>
					<el-input v-model="userName" style="width:45%;" placeholder="请输入用户姓名">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">用户电话：</span>
					<el-input v-model="userPhone" style="width:45%;" placeholder="请输入用户电话"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">所在地区：</span>
					<el-input v-model="address" style="width:45%;" placeholder="请输入所在地区">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">详细地址：</span>
					<el-input v-model="addressDetail" style="width:45%;" placeholder="请输入详细地址"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">维度：</span>
					<el-input v-model="latitude" style="width:45%;" placeholder="请输入维度"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">经度：</span>
					<el-input v-model="longitude" style="width:45%;" placeholder="请输入经度"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">是否默认：</span>
					<el-radio-group v-model="addressDefault">
						<el-radio :label="1">默认</el-radio>
						<el-radio :label="0">不默认</el-radio>
					</el-radio-group>
				</div>
				<div>
					<div id="container" style="width:80%;height:500px;margin-left: 10%;"></div>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="addmissionNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改手机号 -->
			<el-dialog title="修改" :visible.sync="showPhone" width="40%" :close-on-click-modal="false">
				<el-form ref="setFrom" :model="setFrom" label-width="80px" :rules="rules">
					<el-form-item v-if="upType == 1" label="手机号" prop="setPhone">
						<el-input v-model.number="setFrom.setPhone" placeholder="请输入手机号码"></el-input>
					</el-form-item>
					<el-form-item v-if="upType == 2" label="名称" prop="userName">
						<el-input v-model="setFrom.userName" placeholder="请输入名称"></el-input>
					</el-form-item>
					<el-form-item v-if="upType == 4" label="邮箱" prop="email">
						<el-input v-model="setFrom.email" placeholder="请输入邮箱"></el-input>
					</el-form-item>
					
					<el-form-item v-if="upType == 3" label="图像" prop="avatar">
						<!-- <el-input v-model="setFrom.userName" placeholder="请输入名称"></el-input> -->
						<el-upload class="avatar-uploader" :action="upAction"
							:show-file-list="false" :on-success="handleAvatarSuccess">
							<img v-if="setFrom.avatar" :src="setFrom.avatar" class="avatar">
							<i v-else class="el-icon-plus avatar-uploader-icon"></i>
						</el-upload>
					</el-form-item>
				</el-form>
				<span slot="footer" class="dialog-footer">
					<el-button @click="showPhone = false">取 消</el-button>
					<el-button type="primary" @click="submitForm('setFrom')">确 定</el-button>
				</span>
			</el-dialog>
		</el-tabs>
		</div>
	</div>
</template>

<script>
import {
	jsonp
} from 'vue-jsonp'
var geocoder, map, markersArray = [];
export default {
	data() {
		var ckPhone = (rule, value, callback) => {
			if (!value) {
				return callback(new Error('手机号不能为空'));
			} else {
				var reg_tel = /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/;    //11位手机号码正则
				if (!reg_tel.test(value)) {
					return callback(new Error('手机号格式错误'));
				}
				callback();
			}
		};
		return { 
			upAction:this.$http.adornUrlMsg('alioss/upload'),
			upType: 1, //1:修改手机号 2:修改名称 3:修改头像
			rules: {
				setPhone: [
					{ required: true, validator: ckPhone, trigger: 'blur' },
				],
				userName: [
					{ required: true, message: '请输入名称', trigger: 'blur' },
				],
				avatar: [
					{ required: true, message: '请上传头像', trigger: 'blur' },
				]
			},
			setFrom: {
				setPhone: '',
				userName: '',
				avatar: '',
				email:'',
			},

			showPhone: false,
			openValue: 1,
			closeValue: 0,
			state: 0,
			limit: 10,
			page: 1,
			userId: '',
			money: '',
			title: '',
			content: '',
			phone: '',
			startTime: '',
			endTime: '',
			invitationCode: '',
			feiDatamin: '',
			info: {
				stockDate: this.getNowTime(), //日期
			},
			info2: {
				stockDate2: this.getNowTime2(), //日期
			},
			tablejifen: '',
			autonymData: {},
			tablemoney: {},
			tablenum: {},
			tableData: {},
			youhuiquanData: {},
			dizhiData: {},
			userData: [],
			tableDatamin: [],
			withdrawData: [],
			walletData: [],
			jifenData: {},
			rechargeData: [],
			sendData: [],
			takeData: [],
			activeName: 'first',
			dialogFormVisible: false,
			dialogFormVisible1: false,
			dialogFormVisible2: false,
			tableDataLoading: true,
			relation_id: '',
			relationId: '',
			tkstatus: '',
			invitation: '',
			nowMonthOver: '',
			lastMonthOver: '',
			nowMonthPaid: '',
			lastMonthPaid: '',
			nowMonthOrderNumber: '',
			nowDayOrderNumber: '',
			hasCash: '',
			type: '',
			titles: '添加地址',
			addressId: '',
			userName: '',
			userPhone: '',
			addressDetail: '',
			address: '',
			addressDefault: '',
			longitude: '', //维度
			latitude: '', //经度
			integralNum: '',
		}
	},
	methods: {
		handleAvatarSuccess(res, file) {
			this.setFrom.avatar = res.data;
		},
		//修改手机号
		submitForm(formName) {
			this.$refs[formName].validate((valid) => {
				if (valid) {
					this.$confirm('确定修改吗？', '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					}).then(() => {
						let datas = {
							userId: this.tableData.userId,
						}
						if (this.upType == 1) {
							datas.phone = this.setFrom.setPhone
						} else if (this.upType == 2) {
							datas.userName = this.setFrom.userName
						}else if(this.upType == 3){
							datas.avatar = this.setFrom.avatar
						}else if(this.upType == 4){
							datas.email = this.setFrom.email
						}
						this.$http({
							url: this.$http.adornUrl('user/updateUserByUserId'),
							method: 'post',
							data: this.$http.adornData(datas)
						}).then(({
							data
						}) => {
							if (data.code == 0) {
								this.$message({
									message: '修改成功',
									type: 'success',
									duration: 1500,
									onClose: () => {
										this.setFrom.setPhone = ''
										this.setFrom.userName = ''
										this.setFrom.avatar = ''
										this.setFrom.email = ''
										this.showPhone = false
										this.dataSelect()
									}
								})
							} else {
								this.$message({
									message: data.msg,
									type: 'error',
									duration: 1500,
								})
							}
						})
					}).catch(() => {
						this.$message({
							type: 'info',
							message: '已取消'
						});
					});
				} else {
					console.log('error submit!!');
					return false;
				}
			});
		},
		// 返回上一级
		prev() {
			this.$router.back()
		},
		// 详情跳转
		updates(row) {
			this.$router.push({
				path: '/userDetail1',
				query: {
					userId: row.userId
				}
			})
		},
		//处理默认选中当前日期
		getNowTime() {
			var now = new Date()
			var year = now.getFullYear() //得到年份
			var month = now.getMonth() //得到月份
			var date = now.getDate() //得到日期
			var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
			var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
			var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
			month = month + 1
			month = month.toString().padStart(2, '0')
			date = date.toString().padStart(2, '0')
			var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`
			return defaultDate
			this.$set(this.info, 'stockDate', defaultDate)
		},
		//处理默认选中当前日期
		getNowTime2() {
			var now = new Date()
			var year = now.getFullYear() //得到年份
			var month = now.getMonth() - now.getMonth() //得到月份
			var date = now.getDate() - now.getDate() + 1 //得到日期
			var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
			var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
			var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
			month = month + 1
			month = month.toString().padStart(2, '0')
			date = date.toString().padStart(2, '0')
			var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`
			return defaultDate
			this.$set(this.info, 'stockDate', defaultDate)
		},
		// tabs切换
		handleClick(tab, event) {
			if (tab._props.label == '用户详情') {
				this.dataSelect()
				this.jfSelect()
			}
			if (tab._props.label == '直属') {
				this.limit = 10
				this.page = 1
				this.teamSelect()
			}
			if (tab._props.label == '非直属') {
				this.limit = 10
				this.page = 1
				this.feiSelect()
			}
			if (tab._props.label == '用户消息') {
				this.limit = 10
				this.page = 1
				this.state = 5
				this.userSelect()
			}
			if (tab._props.label == '任务消息') {
				this.state = 6
				this.userSelect()
			}
			if (tab._props.label == '提现记录') {
				this.limit = 10
				this.page = 1
				this.withdrawSelect()
			}
			if (tab._props.label == '钱包明细') {
				this.limit = 10
				this.page = 1
				this.walletSelect()
			}
			if (tab._props.label == '积分明细') {
				this.limit = 10
				this.page = 1
				this.jifenSelect()
			}
			if (tab._props.label == '充值明细') {
				this.limit = 10
				this.page = 1
				this.startTime = ''
				this.endTime = ''
				this.rechargeSelect()
			}
			if (tab._props.label == '接单任务') {
				this.limit = 10
				this.page = 1
				this.state = 0
				this.sendSelect()
			}
			if (tab._props.label == '我的订单') {
				this.limit = 10
				this.page = 1
				this.state = 0
				this.takeSelect()
			}
			if (tab._props.label == '我的消息') {
				this.limit = 10
				this.page = 1
				this.state = 8
				this.flag = 1
				this.userSelect()
			}
			if (tab._props.label == '我的优惠券') {
				this.limit = 10
				this.page = 1
				this.youhuiquanSelect()
			}
			if (tab._props.label == '我的地址') {
				this.limit = 10
				this.page = 1
				this.dizhiSelect()
			}
		},
		// 消息推送
		magNotice() {
			this.dialogFormVisible = true
		},
		// 消息推送
		magNoticeTo() {
			if (this.title == '') {
				this.$notify({
					title: '提示',
					duration: 1800,
					message: '请输入标题',
					type: 'warning'
				})
				return
			}
			if (this.content == '') {
				this.$notify({
					title: '提示',
					duration: 1800,
					message: '请输入内容',
					type: 'warning'
				})
				return
			}
			this.$http({
				url: this.$http.adornUrl('cash/sendMsg'),
				method: 'get',
				params: this.$http.adornParams({
					'title': this.title,
					'content': this.content,
					'phone': this.phone,
					'flag': this.flag
				})
			}).then(({
				data
			}) => {
				this.dialogFormVisible = false
				if (data.code == -100) {
					this.$message({
						message: data.msg,
						type: 'error',
						duration: 1500,
						onClose: () => {
							this.title = ''
							this.content = ''
							this.userSelect()
						}
					})
				}
				if (data.code == 0) {
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.title = ''
							this.content = ''
							this.userSelect()
						}
					})
				}
			})
		},
		handleSizeChange(val) {
			this.limit = val
			this.jifenSelect()
		},
		handleCurrentChange(val) {
			this.page = val
			this.jifenSelect()
		},
		handleSizeChange1(val) {
			this.limit = val
			this.userSelect()
		},
		handleCurrentChange1(val) {
			this.page = val
			this.userSelect()
		},
		handleSizeChange2(val) {
			this.limit = val
			this.withdrawSelect()
		},
		handleCurrentChange2(val) {
			this.page = val
			this.withdrawSelect()
		},
		handleSizeChange3(val) {
			this.limit = val
			this.walletSelect()
		},
		handleCurrentChange3(val) {
			this.page = val
			this.walletSelect()
		},
		handleSizeChange4(val) {
			this.limit = val
			this.rechargeSelect()
		},
		handleCurrentChange4(val) {
			this.page = val
			this.rechargeSelect()
		},
		handleSizeChange5(val) {
			this.limit = val
			this.sendSelect()
		},
		handleCurrentChange5(val) {
			this.page = val
			this.sendSelect()
		},
		handleSizeChange6(val) {
			this.limit = val
			this.takeSelect()
		},
		handleCurrentChange6(val) {
			this.page = val
			this.takeSelect()
		},
		handleSizeChange7(val) {
			this.limit = val
			this.youhuiquanSelect()
		},
		handleCurrentChange7(val) {
			this.page = val
			this.youhuiquanSelect()
		},
		handleSizeChange8(val) {
			this.limit = val
			this.dizhiSelect()
		},
		handleCurrentChange8(val) {
			this.page = val
			this.dizhiSelect()
		},
		timeDate2() {
			this.rechargeSelect()
		},
		// 可提现金额
		withdraw(id, val) {
			if (val == 0) {
				this.$message({
					message: '可提现金额为0元',
					type: 'error',
					duration: 1500,
					onClose: () => {
						this.dataSelect()
					}
				})
			} else {
				this.$confirm(`确定要推送提现消息吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`user/notification/${id}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '推送成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				}).catch(() => { })
			}

		},
		// 不可提现金额充值
		rechargenone() {
			this.type = ''
			this.money = ''
			this.dialogFormVisible1 = true
		},
		StairNoticeTo1() {
			let userId = this.$route.query.userId
			if (this.type == '') {
				this.$notify({
					title: '提示',
					duration: 1800,
					message: '请选择修改类型',
					type: 'warning'
				})
				return
			}

			if (this.money == '') {
				this.$notify({
					title: '提示',
					duration: 1800,
					message: '请输入金额',
					type: 'warning'
				})
				return
			}
			if (this.type == 2) {
				if (this.money > this.tablemoney.money) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '最高金额为' + this.tablemoney.money,
						type: 'warning'
					})
					return
				}
			}

			this.$http({
				url: this.$http.adornUrl(`admin/dataCentre/addUserMoney?userId=` + userId + '&money=' + this
					.money +
					'&type=' + this.type),
				method: 'get',
				data: this.$http.adornData({})
			}).then(({
				data
			}) => {
				if (data.code == 0) {
					this.$message({
						message: '添加金额成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.money = ''
							this.dataSelect()
						}
					})
					this.dialogFormVisible1 = false
				} else {
					this.$message.error(data.msg)
				}

			})
		},
		// 更改状态
		stateChange(userId) {
			this.$confirm(`确定要更改用户状态吗?`, '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.$http({
					url: this.$http.adornUrl(`user/updateUserStatusByUserId?userId=${userId}`),
					method: 'get',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.$message({
						message: '修改成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dataSelect()
						}
					})
				})
			}).catch(() => { })
		},
		// 获取数据列表
		dataSelect() {
			let userId = this.$route.query.userId
			this.$http({
				url: this.$http.adornUrl('admin/dataCentre/selectUserById'),
				method: 'get',
				params: this.$http.adornParams({
					'userId': userId
				})
			}).then(({
				data
			}) => {
				if (data.code === 0) {
					let returnData = data.data
					this.tablenum = returnData
					this.tablemoney = returnData
					this.tableData = returnData
					this.phone = returnData.phone
					this.invitationCode = returnData.invitationCode
				}
			})
		},
		// 获取积分
		jfSelect() {
			let userId = this.$route.query.userId
			this.$http({
				url: this.$http.adornUrl('userintegral/selectUserIntegral'),
				method: 'get',
				params: this.$http.adornParams({
					'userId': userId
				})
			}).then(({
				data
			}) => {
				if (data.code === 0) {
					let returnData = data.data
					this.integralNum = returnData.integralNum
				}
			})
		},
		// 获取团队成员
		teamSelect() {
			this.tableDataLoading = true
			let userId = this.$route.query.userId
			this.$http({
				url: this.$http.adornUrl('user/selectInviteByUserIdLists'),
				method: 'get',
				params: this.$http.adornParams({
					'page': this.page,
					'limit': this.limit,
					'userId': userId
				})
			}).then(({
				data
			}) => {
				this.tableDataLoading = false
				let returnData = data.data
				this.tableDatamin = returnData
			})
		},
		// 非直属
		feiSelect() {
			this.tableDataLoading = true
			this.$http({
				url: this.$http.adornUrl('user/selectInvitationCodeByUserIdLists'),
				method: 'get',
				params: this.$http.adornParams({
					'page': this.page,
					'limit': this.limit,
					'invitationCode': this.invitationCode
				})
			}).then(({
				data
			}) => {
				this.tableDataLoading = false
				let returnData = data.data
				this.feiDatamin = returnData
			})
		},
		// 获取用户/任务消息
		userSelect() {
			this.tableDataLoading = true
			let userId = this.$route.query.userId
			this.$http({
				url: this.$http.adornUrl('message/selectMessageByUserId'),
				method: 'get',
				params: this.$http.adornParams({
					'page': this.page,
					'limit': this.limit,
					'userId': userId,
					'state': this.state
				})
			}).then(({
				data
			}) => {
				this.tableDataLoading = false
				let returnData = data.data
				this.userData = returnData
			})
		},
		// 获取用户提现记录
		withdrawSelect() {
			this.tableDataLoading = true
			let userId = this.$route.query.userId
			this.$http({
				url: this.$http.adornUrl('/user/selectPayDetails'),
				method: 'get',
				params: this.$http.adornParams({
					'page': this.page,
					'limit': this.limit,
					'userId': userId,
				})
			}).then(({
				data
			}) => {
				this.tableDataLoading = false
				let returnData = data.data
				this.withdrawData = returnData
			})
		},
		// 获取钱包明细
		walletSelect() {
			this.tableDataLoading = true
			let userId = this.$route.query.userId
			this.$http({
				url: this.$http.adornUrl('Details/queryUserMoneyDetails'),
				method: 'get',
				params: this.$http.adornParams({
					'page': this.page,
					'limit': this.limit,
					'userId': userId
				})
			}).then(({
				data
			}) => {
				this.tableDataLoading = false
				let returnData = data.data
				this.walletData = returnData.pageUtils
			})
		},
		// 积分明细
		jifenSelect() {
			this.tableDataLoading = true
			let userId = this.$route.query.userId
			this.$http({
				url: this.$http.adornUrl('admin/dataCentre/selectSignIn'),
				method: 'get',
				params: this.$http.adornParams({
					'page': this.page,
					'limit': this.limit,
					'userId': userId
				})
			}).then(({
				data
			}) => {
				this.tableDataLoading = false
				let returnData = data.data
				this.jifenData = returnData
			})
		},

		// 获取充值明细
		rechargeSelect() {
			if (this.endTime == '') {
				this.endTime = this.info.stockDate
			}
			if (this.startTime == '') {
				this.startTime = this.info2.stockDate2
			}
			let userId = this.$route.query.userId
			this.tableDataLoading = true
			this.$http({
				url: this.$http.adornUrl('admin/paydetails/selectPayDetails'),
				method: 'get',
				params: this.$http.adornParams({
					'page': this.page,
					'limit': this.limit,
					'endTime': this.endTime,
					'startTime': this.startTime,
					'userId': userId
				})
			}).then(({
				data
			}) => {
				if (data.code == 0) {
					this.tableDataLoading = false
					let returnData = data.data
					this.rechargeData = returnData
				}
				if (data.code == 500) {
					this.$message({
						message: data.msg,
						type: 'error',
						duration: 2500,
						onClose: () => {
							this.tableDataLoading = false
						}
					})
				}
			})
		},
		// 获取接单
		sendSelect() {
			this.tableDataLoading = true
			let userId = this.$route.query.userId
			this.$http({
				url: this.$http.adornUrl('help/selectRunHelpOrder'),
				method: 'get',
				params: this.$http.adornParams({
					'page': this.page,
					'limit': this.limit,
					'status': this.state,
					'userId': userId
				})
			}).then(({
				data
			}) => {
				this.tableDataLoading = false
				let returnData = data.data
				this.sendData = returnData
			})
		},
		// 获取派单
		takeSelect() {
			this.tableDataLoading = true
			let userId = this.$route.query.userId
			this.$http({
				url: this.$http.adornUrl('admin/dataCentre/selectOrderDetails'),
				method: 'get',
				params: this.$http.adornParams({
					'page': this.page,
					'limit': this.limit,
					// 'status': this.state,
					'userId': userId
				})
			}).then(({
				data
			}) => {
				this.tableDataLoading = false
				let returnData = data.data
				console.log(returnData)
				// returnData.list.forEach(res=> {
				//   res.goodsMessage = JSON.parse(res.goodsMessage)
				// })
				this.takeData = returnData
			})
		},
		// 我的优惠券
		youhuiquanSelect() {
			this.tableDataLoading = true
			let userId = this.$route.query.userId
			this.$http({
				url: this.$http.adornUrl('admin/dataCentre/selectCouponByUserId'),
				method: 'get',
				params: this.$http.adornParams({
					'page': this.page,
					'limit': this.limit,
					'userId': userId,
					'status': ''
				})
			}).then(({
				data
			}) => {
				console.log(data)
				this.tableDataLoading = false
				let returnData = data.data
				this.youhuiquanData = returnData
			})
		},
		// 我的地址
		dizhiSelect() {
			this.tableDataLoading = true
			let userId = this.$route.query.userId
			this.$http({
				url: this.$http.adornUrl('admin/address/selectAddressList'),
				method: 'get',
				params: this.$http.adornParams({
					'page': this.page,
					'limit': this.limit,
					'userId': userId
				})
			}).then(({
				data
			}) => {
				console.log(data)
				this.tableDataLoading = false
				let returnData = data.data
				this.dizhiData = returnData
			})
		},
		// 添加地址
		adddates(row) {
			this.dialogFormVisible2 = true
			this.userId = this.$route.query.userId
			this.titles = '添加地址'
			if (row != 0) {
				this.missions = row
				this.titles = '修改地址'
				this.address = row.address
				this.latitude = row.lat
				this.longitude = row.lng
				this.addressId = row.addressId
				this.addressDetail = row.addressDetail
				this.userName = row.userName
				this.userPhone = row.userPhone
				this.addressDefault = Number(row.addressDefault)
			} else {
				this.address = ''
				this.latitude = ''
				this.longitude = ''
				this.addressId = ''
				this.addressDetail = ''
				this.userName = ''
				this.userPhone = ''
				this.isDefault = 0
			}
			console.log(this.userId)

			this.getMyLocation();
			return
			// this.$router.push({
			//   path: '/addressAdd',
			//   query: {
			//     missions: row,
			//     userid: userId
			//   }
			// })
		},
		// 删除地址
		deleteStair(row) {
			this.$confirm(`确定删除此条信息?`, '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.$http({
					url: this.$http.adornUrl(
						`admin/address/deleteAddress/?addressId=${row.addressId}`),
					method: 'post',
					params: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.$message({
						message: '删除成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dizhiSelect()
						}
					})
				})
			})
		},
		// 确定添加
		addmissionNoticeTo() {
			// this.select()
			if (this.address == '') {
				this.$notify({
					title: '提示',
					duration: 1800,
					message: '地址详情不能为空',
					type: 'warning'
				});
				return
			}
			if (this.addressDefault === '') {
				this.$notify({
					title: '提示',
					duration: 1800,
					message: '请选择地址是否默认',
					type: 'warning'
				});
				return
			}
			// toString()
			let urls = ''
			if (this.titles == '添加地址') {
				urls = 'admin/address/insertAddress'
			} else {
				urls = 'admin/address/updateAddress'
			}
			let that = this
			setTimeout(function () {
				that.$http({
					url: that.$http.adornUrl(urls),
					method: 'post',
					data: that.$http.adornData({
						'address': that.address,
						'addressDetail': that.addressDetail,
						'lat': that.latitude,
						'lng': that.longitude,
						'addressId': that.addressId,
						'addressDefault': that.addressDefault,
						'userId': that.userId,
						'userName': that.userName,
						'userPhone': that.userPhone
					})
				}).then(({
					data
				}) => {
					console.log('提交结果', data)
					that.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							that.dialogFormVisible2 = false
							that.address = ''
							that.isDefault = 0
							that.storeAddre = '请选择城市'
							that.dizhiSelect()
						}
					})

				})
			}, 1000)

		},
		//定位获得当前位置信息
		getMyLocation() {

			var geolocation = new qq.maps.Geolocation("ZBABZ-ZWECU-UQTVV-4LYDR-COK3F-5SF75", "来点小收入");
			geolocation.getIpLocation(this.showPosition, this.showErr);
			//geolocation.getLocation(this.showPosition, this.showErr);//或者用getLocation精确度比较高
		},
		showPosition(position) {
			console.log(position);
			// this.latitude = position.lat;
			// this.longitude = position.lng;
			// this.city = position.city;

			this.setMap();

			// this.setMap1();

		},
		showErr(e) {
			console.log("定位失败", e);
			this.getMyLocation(); //定位失败再请求定位，测试使用
		},
		//位置信息在地图上展示
		setMap() {
			//步骤：定义map变量 调用 qq.maps.Map() 构造函数   获取地图显示容器
			//设置地图中心点
			var myLatlng = new qq.maps.LatLng(this.latitude, this.longitude);
			//定义工厂模式函数
			var myOptions = {
				zoom: 13, //设置地图缩放级别
				center: myLatlng, //设置中心点样式
				mapTypeId: qq.maps.MapTypeId.ROADMAP //设置地图样式详情参见MapType
			}
			// //获取dom元素添加地图信息
			var map = new qq.maps.Map(document.getElementById("container"), myOptions);
			//给地图添加点击事件

			//给定位的位置添加图片标注
			var marker = new qq.maps.Marker({
				position: myLatlng,
				map: map
			});
			// `````````````
			var that = this;
			if (that.longitude == '') {
				var center = new qq.maps.LatLng(34.34281541842994, 108.93970884382725);
			} else {
				var center = new qq.maps.LatLng(that.latitude, that.longitude);
			}
			var map = new qq.maps.Map(document.getElementById("container"), {
				center: center,
				zoom: 13
			});
			var marker = new qq.maps.Marker({
				position: center,
				map: map
			});
			var latlngBounds = new qq.maps.LatLngBounds();
			qq.maps.event.addListener(map, "click", function (event) {
				console.log(event, qq.maps);
				that.longitude = event.latLng.getLng(); // 经度
				that.latitude = event.latLng.getLat(); // 纬度
				jsonp('https://apis.map.qq.com/ws/geocoder/v1/?location=' + event.latLng.getLat() + ',' + event
					.latLng
					.getLng() + '&key=ZBABZ-ZWECU-UQTVV-4LYDR-COK3F-5SF75&get_poi=1&output=jsonp', {
					myCustomUrlParam: 'veryNice'
				}).then(response => {
					console.log('response', response)
					that.address = response.result.address
					that.addressDetail = response.result.formatted_addresses.recommend

				}).catch(error => {
					// handle error
				}).then(() => {
					// always executed
				});
				if (markersArray) {
					for (let i in markersArray) {
						markersArray[i].setMap(null);
					}
				}
				var marker = new qq.maps.Marker({
					map: map,
					position: event.latLng
				});
				markersArray.push(marker);

			});
			geocoder = new qq.maps.Geocoder({
				complete: function (result) {
					console.log(result);
					that.longitude = result.detail.location.lng;
					that.latitude = result.detail.location.lat;
					map.setCenter(result.detail.location);
					var marker = new qq.maps.Marker({
						map: map,
						position: result.detail.location
					});
					markersArray.push(marker);
				}
			});

		},
		//位置信息在地图上展示
		setMap1() {
			//步骤：定义map变量 调用 qq.maps.Map() 构造函数   获取地图显示容器
			//设置地图中心点
			var myLatlng = new qq.maps.LatLng(this.latitude, this.longitude);
			//定义工厂模式函数
			var myOptions = {
				zoom: 13, //设置地图缩放级别
				center: myLatlng, //设置中心点样式
				mapTypeId: qq.maps.MapTypeId.ROADMAP //设置地图样式详情参见MapType
			}
			// //获取dom元素添加地图信息
			var maps = new qq.maps.Map(document.getElementById("aaa"), myOptions);
			//给地图添加点击事件

			//给定位的位置添加图片标注
			var marker3 = new qq.maps.Marker({
				position: myLatlng,
				map: maps
			});
			// `````````````
			var that = this;
			if (that.longitude == '') {
				var center1 = new qq.maps.LatLng(34.34281541842994, 108.93970884382725);
			} else {
				var center1 = new qq.maps.LatLng(that.latitude, that.longitude);
			}

			var maps = new qq.maps.Map(document.getElementById("aaa"), {
				center: center1,
				zoom: 13
			});
			var marker4 = new qq.maps.Marker({
				position: center1,
				map: maps
			});
			var latlngBounds = new qq.maps.LatLngBounds();
			qq.maps.event.addListener(maps, "click", function (event) {
				console.log(event, qq.maps);
				that.longitude = event.latLng.getLng(); // 经度
				that.latitude = event.latLng.getLat(); // 纬度
				jsonp('https://apis.map.qq.com/ws/geocoder/v1/?location=' + event.latLng.getLat() + ',' + event
					.latLng
					.getLng() + '&key=ZBABZ-ZWECU-UQTVV-4LYDR-COK3F-5SF75&get_poi=1&output=jsonp', {
					myCustomUrlParam: 'veryNice'
				}).then(response => {
					console.log('response', response)
					that.address = response.result.formatted_addresses.recommend

				}).catch(error => {
					// handle error
				}).then(() => {
					// always executed
				});
				if (markersArray) {
					for (let i in markersArray) {
						markersArray[i].setMap(null);
					}
				}
				var marker1 = new qq.maps.Marker({
					map: maps,
					position: event.latLng
				});
				markersArray.push(marker1);

			});
			geocoder = new qq.maps.Geocoder({
				complete: function (result) {
					console.log(result);
					that.longitude = result.detail.location.lng;
					that.latitude = result.detail.location.lat;
					map.setCenter(result.detail.location);
					var marker2 = new qq.maps.Marker({
						map: maps,
						position: result.detail.location
					});
					markersArray.push(marker2);
				}
			});

		},
		// 地图定位
		select() {
			console.log(this.address)
			let that = this
			jsonp('https://apis.map.qq.com/ws/geocoder/v1/?address==' + that.address +
				'&key=ZBABZ-ZWECU-UQTVV-4LYDR-COK3F-5SF75&get_poi=1&output=jsonp', {
				myCustomUrlParam: 'veryNice'
			}).then(response => {
				// handle success
				console.log('response`````', response)
				that.longitude = response.result.location.lng; // 经度
				that.latitude = response.result.location.lat; // 纬度
				that.address = response.result.title
				that.setMap()
				that.setMap1()
			}).catch(error => {
				// handle error
			}).then(() => {
				// always executed
			});
		},
	},
	mounted() {
		this.dataSelect()
		this.jfSelect()
	},
	watch: {
		'$route': 'dataSelect'
	}
}
</script>

<style scoped="scoped">
.nf-userdetail-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-userdetail-hero {
  margin-bottom: 12px;
}

.nf-userdetail-back {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  margin-bottom: 8px;
  font-size: 14px;
  color: var(--nf-text-secondary, #909399);
  text-decoration: none;
  transition: color 0.2s;
}

.nf-userdetail-back:hover {
  color: var(--nf-brand, #ffa8c2);
}

.nf-userdetail-back__icon {
  width: 1.2em;
  height: 1.2em;
}

.nf-userdetail-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-userdetail-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-userdetail-tabs-wrap {
  margin-bottom: 0;
}

.nf-userdetail-tabs >>> .el-tabs__header {
  margin-bottom: 0;
}

.nf-userdetail-info-card {
  margin-bottom: 0;
}

.nf-userdetail-info-card__head {
  margin-bottom: 12px;
}

.nf-userdetail-info-table {
  border: 1px solid var(--nf-border, #e8eaed);
  border-radius: 6px;
  overflow: hidden;
}

.nf-userdetail-info-table table {
  width: 100%;
  border-collapse: collapse;
}

.nf-userdetail-info-table tr {
  border-bottom: 1px solid var(--nf-border, #e8eaed);
}

.nf-userdetail-info-table th {
  background-color: var(--nf-sidebar-bg-alt, #f7f8fa);
  padding: 12px 16px;
  border-right: 1px solid var(--nf-border, #e8eaed);
  border-bottom: 1px solid var(--nf-border, #e8eaed);
  color: var(--nf-text-primary, rgba(0, 0, 0, 0.85));
  font-weight: 500;
  font-size: 13px;
  line-height: 1.5;
  white-space: nowrap;
}

.nf-userdetail-info-table td {
  padding: 12px 16px;
  border-right: 1px solid var(--nf-border, #e8eaed);
  border-bottom: 1px solid var(--nf-border, #e8eaed);
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
  line-height: 1.5;
  word-break: break-word;
}

.nf-userdetail-editable {
  cursor: pointer;
}

.nf-userdetail-editable:hover {
  color: var(--nf-brand, #ffa8c2);
}

.nf-userdetail-text-btn {
  color: var(--nf-brand, #ffa8c2) !important;
  padding: 0 4px;
}

.nf-userdetail-filter {
  margin-bottom: 12px;
}

.nf-userdetail-filter__body {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px 16px;
}

.nf-userdetail-filter__item {
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.nf-userdetail-filter__label {
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
  white-space: nowrap;
}

.nf-userdetail-filter__date {
  width: 200px;
}

.nf-userdetail-filter__actions {
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.nf-userdetail-table-card {
  margin-bottom: 0;
}

.nf-userdetail-table-toolbar {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 12px;
}

.nf-userdetail-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.nf-userdetail-status-text {
  color: var(--nf-brand, #ffa8c2);
}

.border-rt {
  border-right: none !important;
}

.border-bt {
  border-bottom: none !important;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: var(--nf-brand, #ffa8c2);
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
