<template>
  <el-tabs v-model="activeName" @tab-click="handleClick">
    <el-tab-pane label="全部订单" name="first">
      <div style="margin:2% 0;display: inline-block;">
        <span>订单状态：</span>
        <el-select v-model="status" placeholder="请选择订单状态" style="width:150px;" @change="select()">
          <el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>&nbsp;&nbsp;&nbsp;&nbsp;
        <span>订单号：</span>
        <el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入订单号"
          v-model="orderNum"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
        <span>订单类型：</span>
        <el-select v-model="orderType" placeholder="请选择订单状态" style="width:150px;" @change="select()">
          <el-option v-for="item in orderTypes" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>&nbsp;&nbsp;&nbsp;&nbsp;
      </div>
      <div style="display: inline-block;">
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select">查询</el-button>
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans">重置</el-button>
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="refresh">刷新</el-button>
      </div>
      <el-table v-loading="tableDataLoading" :data="tableData.content">
        <el-table-column fixed prop="img" label="商品图片">
          <template slot-scope="scope">
            <img :src="scope.row.img" alt="" width="60" height="60">
          </template>
        </el-table-column>
        <el-table-column prop="title" label="商品标题" width="200">
        </el-table-column>
        <el-table-column prop="detailJson" label="商品规格">
          <template slot-scope="scope">
            <span>{{scope.row.detailJson ? scope.row.detailJson : '无'}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="number" label="商品个数">
        </el-table-column>
        <el-table-column prop="descrition" label="订单备注">
          <template slot-scope="scope">
            <span>{{scope.row.descrition ? scope.row.descrition : '未填写'}}</span>
          </template>
        </el-table-column>
        <el-table-column label="订单类型">
          <template slot-scope="scope">
            <span v-if="scope.row.orderType == 1">普通订单</span>
            <span v-if="scope.row.orderType == 2">拼团订单</span>
            <span v-if="scope.row.orderType == 3">秒杀订单</span>
            <span v-if="scope.row.orderType == 4">积分订单</span>
          </template>
        </el-table-column>
        <el-table-column prop="orderNum" label="订单号" width="200">
          <template slot-scope="scope">
            <span>{{scope.row.orderNum}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="consignee" label="收货人" width="120">
        </el-table-column>
        <el-table-column prop="mobile" label="手机号" width="120">
        </el-table-column>
        <el-table-column prop="detail" label="详细地址" width="120">
          <template slot-scope="scope">
            <span>{{scope.row.provinces}}{{scope.row.detail}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="商品价格">
          <template slot-scope="scope">
            <span style="color: #f56c6c;">{{scope.row.price | numFilter}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="payMoney" label="支付金额" width="120">
          <template slot-scope="scope">
            <span style="color: #f56c6c;cursor:pointer;">{{scope.row.payMoney | numFilter}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="commissionPrice" label="佣金" width="120">
          <template slot-scope="scope">
            <span style="color: #f56c6c;">{{scope.row.commissionPrice | numFilter}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="refund" label="退款原因" width="160">
          <template slot-scope="scope">
            <span>{{scope.row.refund ? scope.row.refund : '未退款'}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="refusedRefund" label="驳回原因" width="160">
          <template slot-scope="scope">
            <span>{{scope.row.refusedRefund ? scope.row.refusedRefund : '未拒绝'}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="payWay" label="支付方式" width="120">
          <template slot-scope="scope">
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 1 ">微信</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 2 ">微信公众号</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 3 ">微信小程序</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 4 ">app支付宝</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 5 ">H5支付宝</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 6 ">零钱</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 7 ">积分</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === null ">未支付</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" prop="status" label="订单状态">
          <template slot-scope="scope">
            <span style="color: #4f9dec;" v-if="scope.row.status === 1 ">待付款</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 2 ">待发货 </span>
            <span style="color: #999999;" v-if="scope.row.status === 3 ">已发货 </span>
            <span style="color: #999999;" v-if="scope.row.status === 4 ">已收货</span>
            <span style="color: #999999;" v-if="scope.row.status === 5 ">已取消</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 6 ">退款中</span>
            <span style="color: #999999;" v-if="scope.row.status === 7 ">已退款</span>
            <span style="color: #999999;" v-if="scope.row.status === 8 ">已驳回</span>
            <span style="color: #999999;" v-if="scope.row.status === 9 ">拼团中</span>
            <span style="color: #999999;" v-if="scope.row.status === 10 ">已评价</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 11 ">待审核退货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 12 ">已同意退货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 13 ">拒绝退货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 14 ">申请换货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 15 ">已同意，买家待退货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 16 ">买家已发货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 17 ">拒绝换货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 18 ">退款，买家已发货</span>
          </template>
        </el-table-column>
        <el-table-column prop="createAt" label="创建时间" width="160">
        </el-table-column>
        <el-table-column prop="payTime" label="付款时间" width="160">
          <template slot-scope="scope">
            <span>{{scope.row.payTime ? scope.row.payTime : '未付款'}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="finishTime" label="收货时间" width="160">
          <template slot-scope="scope">
            <span>{{scope.row.finishTime ? scope.row.finishTime : '未收货'}}</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="100">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="orderDetails(scope.row.id)">订单详情
            </el-button>
            <el-button size="mini" type="primary" @click="contact(scope.row)">联系买家
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="text-align: center;margin-top: 10px;">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
          :page-sizes="[10, 20, 30,50,100]" :page-size="size" :current-page="page" layout="total,sizes, prev, pager, next"
          :total="tableData.totalElements">
        </el-pagination>
      </div>
    </el-tab-pane>
    <el-tab-pane label="待发货订单" name="thirdly">
      <div style="margin:2% 0;display: inline-block;">
        <span>订单号：</span>
        <el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入订单号"
          v-model="orderNum"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
        <span>订单类型：</span>
        <el-select v-model="orderType" placeholder="请选择订单状态" style="width:150px;" @change="select()">
          <el-option v-for="item in orderTypes" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div style="display: inline-block;">
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select">查询</el-button>
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans">重置</el-button>
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="refresh">刷新</el-button>
      </div>
      <el-table v-loading="tableDataLoading" :data="tableData.content">
        <el-table-column fixed prop="img" label="商品图片">
          <template slot-scope="scope">
            <img :src="scope.row.img" alt="" width="60" height="60">
          </template>
        </el-table-column>
        <el-table-column prop="title" label="商品标题" width="200">
        </el-table-column>
        <el-table-column prop="detailJson" label="商品规格">
          <template slot-scope="scope">
            <span>{{scope.row.detailJson ? scope.row.detailJson : '无'}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="number" label="商品个数">
        </el-table-column>
        <el-table-column prop="descrition" label="订单备注">
          <template slot-scope="scope">
            <span>{{scope.row.descrition ? scope.row.descrition : '未填写'}}</span>
          </template>
        </el-table-column>
        <el-table-column label="订单类型">
          <template slot-scope="scope">
            <span v-if="scope.row.orderType == 1">普通订单</span>
            <span v-if="scope.row.orderType == 2">拼团订单</span>
            <span v-if="scope.row.orderType == 3">秒杀订单</span>
            <span v-if="scope.row.orderType == 4">积分订单</span>
          </template>
        </el-table-column>
        <el-table-column prop="orderNum" label="订单号" width="200">
          <template slot-scope="scope">
            <span>{{scope.row.orderNum}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="consignee" label="收货人" width="120">
        </el-table-column>
        <el-table-column prop="mobile" label="手机号" width="120">
        </el-table-column>
        <el-table-column prop="detail" label="详细地址" width="120">
          <template slot-scope="scope">
            <span>{{scope.row.provinces}}{{scope.row.detail}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="商品价格">
          <template slot-scope="scope">
            <span style="color: #f56c6c;">{{scope.row.price | numFilter}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="payMoney" label="支付金额" width="120">
          <template slot-scope="scope">
            <span style="color: #f56c6c;cursor:pointer;">{{scope.row.payMoney | numFilter}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="commissionPrice" label="佣金" width="120">
          <template slot-scope="scope">
            <span style="color: #f56c6c;">{{scope.row.commissionPrice | numFilter}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="payWay" label="支付方式" width="120">
          <template slot-scope="scope">
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 1 ">微信</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 2 ">微信公众号</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 3 ">微信小程序</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 4 ">app支付宝</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 5 ">H5支付宝</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 6 ">零钱</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 7 ">积分</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === null ">未支付</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" prop="status" label="订单状态">
          <template slot-scope="scope">
            <span style="color: #4f9dec;" v-if="scope.row.status === 2 ">待发货 </span>
          </template>
        </el-table-column>
        <el-table-column prop="createAt" label="创建时间" width="160">
        </el-table-column>
        <el-table-column prop="payTime" label="付款时间" width="160">
          <template slot-scope="scope">
            <span>{{scope.row.payTime ? scope.row.payTime : '未付款'}}</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="100">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" :disabled="!isAuth('selfShopOrder:deliver')"
              @click="colonelClick(scope.row)">立即发货
            </el-button>
            <el-button size="mini" type="primary" @click="orderDetails(scope.row.id)">订单详情
            </el-button>
            <el-button size="mini" type="primary" @click="contact(scope.row)">联系买家
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="text-align: center;margin-top: 10px;">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
          :page-sizes="[10, 20, 30,50,100]" :page-size="size" :current-page="page" layout="total,sizes, prev, pager, next"
          :total="tableData.totalElements">
        </el-pagination>
      </div>

    </el-tab-pane>
    <el-tab-pane label="待退款订单" name="second">
      <div style="margin:2% 0;display: inline-block;">
        <span>订单号：</span>
        <el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入订单号"
          v-model="orderNum"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
        <span>订单类型：</span>
        <el-select v-model="orderType" placeholder="请选择订单状态" style="width:150px;" @change="select()">
          <el-option v-for="item in orderTypes" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div style="display: inline-block;">
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select">查询</el-button>
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans">重置</el-button>
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="refresh">刷新</el-button>
      </div>
      <el-table v-loading="tableDataLoading" :data="tableData.content">
        <el-table-column fixed prop="img" label="商品图片">
          <template slot-scope="scope">
            <img :src="scope.row.img" alt="" width="60" height="60">
          </template>
        </el-table-column>
        <el-table-column prop="title" label="商品标题" width="200">
        </el-table-column>
        <el-table-column prop="detailJson" label="商品规格">
          <template slot-scope="scope">
            <span>{{scope.row.detailJson ? scope.row.detailJson : '无'}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="number" label="商品个数">
        </el-table-column>
        <el-table-column prop="refund" label="退款原因">
        </el-table-column>
        <el-table-column prop="descrition" label="订单备注">
          <template slot-scope="scope">
            <span>{{scope.row.descrition ? scope.row.descrition : '未填写'}}</span>
          </template>
        </el-table-column>
        <el-table-column label="订单类型">
          <template slot-scope="scope">
            <span v-if="scope.row.orderType == 1">普通订单</span>
            <span v-if="scope.row.orderType == 2">拼团订单</span>
            <span v-if="scope.row.orderType == 3">秒杀订单</span>
            <span v-if="scope.row.orderType == 4">积分订单</span>
          </template>
        </el-table-column>
        <el-table-column prop="orderNum" label="订单号" width="200">
          <template slot-scope="scope">
            <span>{{scope.row.orderNum}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="consignee" label="收货人" width="120">
        </el-table-column>
        <el-table-column prop="mobile" label="手机号" width="120">
        </el-table-column>
        <el-table-column prop="detail" label="详细地址" width="120">
          <template slot-scope="scope">
            <span>{{scope.row.provinces}}{{scope.row.detail}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="商品价格">
          <template slot-scope="scope">
            <span style="color: #f56c6c;">{{scope.row.price | numFilter}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="payMoney" label="支付金额" width="120">
          <template slot-scope="scope">
            <span style="color: #f56c6c;cursor:pointer;">{{scope.row.payMoney | numFilter}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="commissionPrice" label="佣金" width="120">
          <template slot-scope="scope">
            <span style="color: #f56c6c;">{{scope.row.commissionPrice | numFilter}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="payWay" label="支付方式" width="120">
          <template slot-scope="scope">
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 1 ">微信</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 2 ">微信公众号</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 3 ">微信小程序</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 4 ">app支付宝</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 5 ">H5支付宝</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 6 ">零钱</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 7 ">积分</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === null ">未支付</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" prop="status" label="订单状态">
          <template slot-scope="scope">
            <span style="color: #4f9dec;" v-if="scope.row.status === 1 ">待付款</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 2 ">待发货 </span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 3 ">已发货 </span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 4 ">已收货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 5 ">已取消</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 6 ">退款中</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 7 ">已退款</span>
          </template>
        </el-table-column>
        <el-table-column prop="createAt" label="创建时间" width="160">
        </el-table-column>
        <el-table-column prop="payTime" label="付款时间" width="160">
          <template slot-scope="scope">
            <span>{{scope.row.payTime ? scope.row.payTime : '未付款'}}</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="100">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" :disabled="!isAuth('selfShopOrder:refund')"
              @click="refundClick(scope.row)">确定退款
            </el-button>
            <el-button size="mini" type="primary" :disabled="!isAuth('selfShopOrder:refund')"
              @click="refuseClick(scope.row)">拒绝退款
            </el-button>
            <el-button size="mini" type="primary" @click="orderDetails(scope.row.id)">订单详情
            </el-button>
            <el-button size="mini" type="primary" @click="contact(scope.row)">联系买家
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="text-align: center;margin-top: 10px;">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
          :page-sizes="[10, 20, 30,50,100]" :page-size="size" :current-page="page" layout="total,sizes, prev, pager, next"
          :total="tableData.totalElements">
        </el-pagination>
      </div>
      <!-- 拒绝退款 -->
      <el-dialog title="拒绝退款" :visible.sync="dialogFormVisible1" center>
        <div style="margin-bottom: 10px;">
          <span
            style="width: 200px;display: inline-block;text-align: right;position: relative;top: -65px;">拒绝退款理由：</span>
          <el-input style="width:50%;" type="textarea" rows="4" v-model="refusedRefund" placeholder="请输入拒绝退款理由">
          </el-input>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible1 = false">取 消</el-button>
          <el-button type="primary" @click="refuseNoticeTo()">确 定</el-button>
        </div>
      </el-dialog>
    </el-tab-pane>
    <el-tab-pane label="退货订单" name="seventh">
      <div style="margin:2% 0;display: inline-block;">
        <span>订单号：</span>
        <el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入订单号"
          v-model="orderNum"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
        <span>订单类型：</span>
        <el-select v-model="orderType" placeholder="请选择订单状态" style="width:150px;" @change="select()">
          <el-option v-for="item in orderTypes" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div style="display: inline-block;">
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select">查询</el-button>
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans">重置</el-button>
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="refresh">刷新</el-button>
      </div>
      <el-table v-loading="tableDataLoading" :data="tableData.content">
        <el-table-column fixed prop="img" label="商品图片">
          <template slot-scope="scope">
            <img :src="scope.row.img" alt="" width="60" height="60">
          </template>
        </el-table-column>
        <el-table-column prop="title" label="商品标题" width="200">
        </el-table-column>
        <el-table-column prop="detailJson" label="商品规格">
          <template slot-scope="scope">
            <span>{{scope.row.detailJson ? scope.row.detailJson : '无'}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="number" label="商品个数">
        </el-table-column>
        <el-table-column prop="refund" label="退款原因">
        </el-table-column>
        <el-table-column prop="descrition" label="订单备注">
          <template slot-scope="scope">
            <span>{{scope.row.descrition ? scope.row.descrition : '未填写'}}</span>
          </template>
        </el-table-column>
        <el-table-column label="订单类型">
          <template slot-scope="scope">
            <span v-if="scope.row.orderType == 1">普通订单</span>
            <span v-if="scope.row.orderType == 2">拼团订单</span>
            <span v-if="scope.row.orderType == 3">秒杀订单</span>
            <span v-if="scope.row.orderType == 4">积分订单</span>
          </template>
        </el-table-column>
        <el-table-column prop="orderNum" label="订单号" width="200">
          <template slot-scope="scope">
            <span>{{scope.row.orderNum}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="consignee" label="收货人" width="120">
        </el-table-column>
        <el-table-column prop="mobile" label="手机号" width="120">
        </el-table-column>
        <el-table-column prop="detail" label="详细地址" width="120">
          <template slot-scope="scope">
            <span>{{scope.row.provinces}}{{scope.row.detail}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="商品价格">
          <template slot-scope="scope">
            <span style="color: #f56c6c;">{{scope.row.price | numFilter}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="payMoney" label="支付金额" width="120">
          <template slot-scope="scope">
            <span style="color: #f56c6c;cursor:pointer;">{{scope.row.payMoney | numFilter}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="commissionPrice" label="佣金" width="120">
          <template slot-scope="scope">
            <span style="color: #f56c6c;">{{scope.row.commissionPrice | numFilter}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="payWay" label="支付方式" width="120">
          <template slot-scope="scope">
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 1 ">微信</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 2 ">微信公众号</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 3 ">微信小程序</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 4 ">app支付宝</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 5 ">H5支付宝</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 6 ">零钱</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 7 ">积分</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === null ">未支付</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" prop="status" label="订单状态" width="120">
          <template slot-scope="scope">
            <span style="color: #4f9dec;" v-if="scope.row.status === 1 ">待付款</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 2 ">待发货 </span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 3 ">已发货 </span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 4 ">已收货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 5 ">已取消</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 6 ">退款中</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 7 ">已退款</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 11 ">待审核退货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 12 ">已同意退货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 13 ">拒绝退货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 14 ">申请换货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 15 ">已同意，买家待退货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 16 ">买家已发货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 17 ">拒绝换货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 18 ">退款，买家已发货</span>
          </template>
        </el-table-column>
        <el-table-column prop="createAt" label="创建时间" width="160">
        </el-table-column>
        <el-table-column prop="payTime" label="付款时间" width="160">
          <template slot-scope="scope">
            <span>{{scope.row.payTime ? scope.row.payTime : '未付款'}}</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="100">
          <template slot-scope="scope">
            <el-button size="mini" v-if="scope.row.status==11" type="primary" :disabled="!isAuth('selfShopOrder:refund')"
              @click="refundClick1(scope.row)">审核订单
            </el-button>
            <el-button size="mini" type="primary" v-if="scope.row.status==18" :disabled="!isAuth('selfShopOrder:refund')"
              @click="refundClick(scope.row)">确定退款
            </el-button>
            <el-button size="mini" type="primary" @click="orderDetails(scope.row.id)">订单详情
            </el-button>
            <el-button size="mini" type="primary" @click="contact(scope.row)">联系买家
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="text-align: center;margin-top: 10px;">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
          :page-sizes="[10, 20, 30,50,100]" :page-size="size" :current-page="page" layout="total,sizes, prev, pager, next"
          :total="tableData.totalElements">
        </el-pagination>
      </div>

    </el-tab-pane>
    <el-tab-pane label="换货订单" name="eighth">
      <div style="margin:2% 0;display: inline-block;">
        <span>订单号：</span>
        <el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入订单号"
          v-model="orderNum"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
        <span>订单类型：</span>
        <el-select v-model="orderType" placeholder="请选择订单状态" style="width:150px;" @change="select()">
          <el-option v-for="item in orderTypes" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div style="display: inline-block;">
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select">查询</el-button>
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans">重置</el-button>
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="refresh">刷新</el-button>
      </div>
      <el-table v-loading="tableDataLoading" :data="tableData.content">
        <el-table-column fixed prop="img" label="商品图片">
          <template slot-scope="scope">
            <img :src="scope.row.img" alt="" width="60" height="60">
          </template>
        </el-table-column>
        <el-table-column prop="title" label="商品标题" width="200">
        </el-table-column>
        <el-table-column prop="detailJson" label="商品规格">
          <template slot-scope="scope">
            <span>{{scope.row.detailJson ? scope.row.detailJson : '无'}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="number" label="商品个数">
        </el-table-column>
        <el-table-column prop="refund" label="退款原因">
        </el-table-column>
        <el-table-column prop="descrition" label="订单备注">
          <template slot-scope="scope">
            <span>{{scope.row.descrition ? scope.row.descrition : '未填写'}}</span>
          </template>
        </el-table-column>
        <el-table-column label="订单类型">
          <template slot-scope="scope">
            <span v-if="scope.row.orderType == 1">普通订单</span>
            <span v-if="scope.row.orderType == 2">拼团订单</span>
            <span v-if="scope.row.orderType == 3">秒杀订单</span>
            <span v-if="scope.row.orderType == 4">积分订单</span>
          </template>
        </el-table-column>
        <el-table-column prop="orderNum" label="订单号" width="200">
          <template slot-scope="scope">
            <span>{{scope.row.orderNum}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="consignee" label="收货人" width="120">
        </el-table-column>
        <el-table-column prop="mobile" label="手机号" width="120">
        </el-table-column>
        <el-table-column prop="detail" label="详细地址" width="120">
          <template slot-scope="scope">
            <span>{{scope.row.provinces}}{{scope.row.detail}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="商品价格">
          <template slot-scope="scope">
            <span style="color: #f56c6c;">{{scope.row.price | numFilter}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="payMoney" label="支付金额" width="120">
          <template slot-scope="scope">
            <span style="color: #f56c6c;cursor:pointer;">{{scope.row.payMoney | numFilter}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="commissionPrice" label="佣金" width="120">
          <template slot-scope="scope">
            <span style="color: #f56c6c;">{{scope.row.commissionPrice | numFilter}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="payWay" label="支付方式" width="120">
          <template slot-scope="scope">
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 1 ">微信</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 2 ">微信公众号</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 3 ">微信小程序</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 4 ">app支付宝</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 5 ">H5支付宝</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 6 ">零钱</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 7 ">积分</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === null ">未支付</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" prop="status" label="订单状态">
          <template slot-scope="scope">
            <span style="color: #4f9dec;" v-if="scope.row.status === 1 ">待付款</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 2 ">待发货 </span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 3 ">已发货 </span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 4 ">已收货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 5 ">已取消</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 6 ">退款中</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 7 ">已退款</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 11 ">待审核退货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 12 ">已同意退货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 13 ">拒绝退货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 14 ">申请换货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 15 ">已同意，买家待退货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 16 ">买家已发货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 17 ">拒绝换货</span>
            <span style="color: #4f9dec;" v-if="scope.row.status === 18 ">退款，买家已发货</span>
          </template>
        </el-table-column>
        <el-table-column prop="createAt" label="创建时间" width="160">
        </el-table-column>
        <el-table-column prop="payTime" label="付款时间" width="160">
          <template slot-scope="scope">
            <span>{{scope.row.payTime ? scope.row.payTime : '未付款'}}</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="100">
          <template slot-scope="scope">
            <el-button size="mini" v-if="scope.row.status==14" type="primary" :disabled="!isAuth('selfShopOrder:refund')"
              @click="refundClick1(scope.row)">审核订单
            </el-button>
            <el-button size="mini" type="primary" :disabled="!isAuth('selfShopOrder:deliver')" v-if="scope.row.status==16"
              @click="colonelClick(scope.row)">立即发货
            </el-button>
            <el-button size="mini" type="primary" @click="orderDetails(scope.row.id)">订单详情
            </el-button>
            <el-button size="mini" type="primary" @click="contact(scope.row)">联系买家
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="text-align: center;margin-top: 10px;">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
          :page-sizes="[10, 20, 30,50,100]" :page-size="size" :current-page="page" layout="total,sizes, prev, pager, next"
          :total="tableData.totalElements">
        </el-pagination>
      </div>
      <!-- 拒绝退款 -->
      <!-- <el-dialog title="拒绝退款" :visible.sync="dialogFormVisible2" center>
        <div style="margin-bottom: 10px;">
          <span
            style="width: 200px;display: inline-block;text-align: right;position: relative;top: -65px;">拒绝退款理由：</span>
          <el-input style="width:50%;" type="textarea" rows="4" v-model="refusedRefund" placeholder="请输入拒绝退款理由">
          </el-input>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible2 = false">取 消</el-button>
          <el-button type="primary" @click="refuseNoticeTo1()">确 定</el-button>
        </div>
      </el-dialog> -->
    </el-tab-pane>
    <el-tab-pane label="分销订单" name="fourth">
      <div>
        <div style="margin:2% 0;display: inline-block;">
          <span>分销状态：</span>
          <el-select v-model="status" placeholder="请选择分销类型" style="width:150px;" @change="select1()">
            <el-option v-for="item in statusnum" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>&nbsp;&nbsp;&nbsp;&nbsp;
          <span>手机号：</span>
          <el-input style="width: 150px;" @keydown.enter.native="select1" clearable placeholder="请输入手机号"
            v-model="phone"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
        </div>
        <div style="display: inline-block;">
          <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select1">查询
          </el-button>
          <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans1">重置
          </el-button>
          <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="refresh1">刷新
          </el-button>
        </div>
        <el-table v-loading="tableDataLoading" :data="fenxiaoData">
          <el-table-column prop="id" label="编号">
          </el-table-column>
          <el-table-column prop="userId" label="用户id">
            <template slot-scope="scope">
              <span style="color: #4f9dec;cursor:pointer;" @click="updates(scope.row)">{{scope.row.userId}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="phone" label="手机号">
            <template slot-scope="scope">
              <span>{{scope.row.phone?scope.row.phone:'未绑定'}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="commissionPrice" label="订单佣金">
          </el-table-column>
          <el-table-column prop="status" label="分销状态">
            <template slot-scope="scope">
              <span v-if="scope.row.status == 1">未到账</span>
              <span v-if="scope.row.status == 2">已到账</span>
            </template>
          </el-table-column>
          <el-table-column prop="detail" label="分销明细">
          </el-table-column>
          <el-table-column prop="createAt" label="创建时间">
          </el-table-column>
        </el-table>
        <div style="text-align: center;margin-top: 10px;">
          <el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
            :page-sizes="[10, 20, 30,50,100]" :page-size="size" :current-page="page" layout="total,sizes, prev, pager, next"
            :total="totalnum">
          </el-pagination>
        </div>
      </div>
    </el-tab-pane>
    <el-tab-pane label="待核销订单" name="cancel">
      <div style="margin:2% 0;display: inline-block;">
        <span>订单号：</span>
        <el-input style="width: 150px;" @keydown.enter.native="select2" clearable placeholder="请输入订单号"
          v-model="orderNum"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
        <span>手机号：</span>
        <el-input style="width: 150px;" @keydown.enter.native="select2" clearable placeholder="请输入手机号" v-model="mobile">
        </el-input>&nbsp;&nbsp;&nbsp;&nbsp;
        <span>订单类型：</span>
        <el-select v-model="orderType" placeholder="请选择订单状态" style="width:150px;" @change="select2()">
          <el-option v-for="item in orderTypes" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div style="display: inline-block;">
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select2">查询</el-button>
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans2">重置</el-button>
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="refresh2">刷新</el-button>
        <el-button style='margin-left:15px;' :disabled="checkBoxData.length <= 0 || !isAuth('selfShopOrder:cancel')"
          size="mini" type="primary" icon="document" @click="volume()">批量核销</el-button>
      </div>
      <el-table @selection-change="changeFun" v-loading="tableDataLoading" :data="tableData2.content">
        <el-table-column type="selection" fixed>
        </el-table-column>
        <el-table-column fixed prop="img" label="商品图片">
          <template slot-scope="scope">
            <img :src="scope.row.img" alt="" width="60" height="60">
          </template>
        </el-table-column>
        <el-table-column prop="title" label="商品标题" width="200">
        </el-table-column>
        <el-table-column prop="detailJson" label="商品规格">
          <template slot-scope="scope">
            <span>{{scope.row.detailJson ? scope.row.detailJson : '无'}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="number" label="商品个数">
        </el-table-column>
        <el-table-column prop="descrition" label="订单备注">
          <template slot-scope="scope">
            <span>{{scope.row.descrition ? scope.row.descrition : '未填写'}}</span>
          </template>
        </el-table-column>
        <el-table-column label="订单类型">
          <template slot-scope="scope">
            <span v-if="scope.row.orderType == 1">普通订单</span>
            <span v-if="scope.row.orderType == 2">拼团订单</span>
            <span v-if="scope.row.orderType == 3">秒杀订单</span>
            <span v-if="scope.row.orderType == 4">积分订单</span>
          </template>
        </el-table-column>
        <el-table-column prop="orderNum" label="订单号" width="200">
          <template slot-scope="scope">
            <span>{{scope.row.orderNum}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="consignee" label="收货人" width="120">
        </el-table-column>
        <el-table-column prop="mobile" label="手机号" width="120">
        </el-table-column>
        <el-table-column prop="detail" label="详细地址" width="120">
          <template slot-scope="scope">
            <span>{{scope.row.provinces}}{{scope.row.detail}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="商品价格">
          <template slot-scope="scope">
            <span style="color: #f56c6c;">{{scope.row.price | numFilter}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="payMoney" label="支付金额" width="120">
          <template slot-scope="scope">
            <span style="color: #f56c6c;cursor:pointer;">{{scope.row.payMoney | numFilter}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="commissionPrice" label="佣金" width="120">
          <template slot-scope="scope">
            <span style="color: #f56c6c;">{{scope.row.commissionPrice | numFilter}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="refund" label="退款原因" width="160">
          <template slot-scope="scope">
            <span>{{scope.row.refund ? scope.row.refund : '未退款'}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="refusedRefund" label="驳回原因" width="160">
          <template slot-scope="scope">
            <span>{{scope.row.refusedRefund ? scope.row.refusedRefund : '未拒绝'}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="payWay" label="支付方式" width="120">
          <template slot-scope="scope">
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 1 ">微信</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 2 ">微信公众号</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 3 ">微信小程序</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 4 ">app支付宝</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 5 ">H5支付宝</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 6 ">零钱</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 7 ">积分</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === null ">未支付</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" prop="status" label="订单状态">
          <template slot-scope="scope">
            <span style="color: #3E8EF7;" v-if="scope.row.status === 3 ">待核销</span>
            <span v-if="scope.row.status === 4 ">已核销</span>
          </template>
        </el-table-column>
        <el-table-column prop="createAt" label="创建时间" width="160">
        </el-table-column>
        <el-table-column prop="payTime" label="付款时间" width="160">
          <template slot-scope="scope">
            <span>{{scope.row.payTime ? scope.row.payTime : '未付款'}}</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="120">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" :disabled="!isAuth('selfShopOrder:cancel')"
              @click="volumeOne(scope.row)">核销
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="text-align: center;margin-top: 10px;">
        <el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
          :page-sizes="[10, 20, 30,50,100]" :page-size="size" :current-page="page" layout="total,sizes, prev, pager, next"
          :total="tableData2.totalElements">
        </el-pagination>
      </div>
    </el-tab-pane>
    <el-tab-pane label="已核销订单" name="yetcancel">
      <div style="margin:2% 0;display: inline-block;">
        <span>订单号：</span>
        <el-input style="width: 150px;" @keydown.enter.native="select2" clearable placeholder="请输入订单号"
          v-model="orderNum"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
        <span>手机号：</span>
        <el-input style="width: 150px;" @keydown.enter.native="select2" clearable placeholder="请输入手机号" v-model="mobile">
        </el-input>&nbsp;&nbsp;&nbsp;&nbsp;
        <span>订单类型：</span>
        <el-select v-model="orderType" placeholder="请选择订单状态" style="width:150px;" @change="select2()">
          <el-option v-for="item in orderTypes" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div style="display: inline-block;">
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="select2">查询</el-button>
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="cleans2">重置</el-button>
        <el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="refresh2">刷新</el-button>
      </div>
      <el-table v-loading="tableDataLoading" :data="tableData2.content">
        <el-table-column fixed prop="img" label="商品图片">
          <template slot-scope="scope">
            <img :src="scope.row.img" alt="" width="60" height="60">
          </template>
        </el-table-column>
        <el-table-column prop="title" label="商品标题" width="200">
        </el-table-column>
        <el-table-column prop="detailJson" label="商品规格">
          <template slot-scope="scope">
            <span>{{scope.row.detailJson ? scope.row.detailJson : '无'}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="number" label="商品个数">
        </el-table-column>
        <el-table-column prop="descrition" label="订单备注">
          <template slot-scope="scope">
            <span>{{scope.row.descrition ? scope.row.descrition : '未填写'}}</span>
          </template>
        </el-table-column>
        <el-table-column label="订单类型">
          <template slot-scope="scope">
            <span v-if="scope.row.orderType == 1">普通订单</span>
            <span v-if="scope.row.orderType == 2">拼团订单</span>
            <span v-if="scope.row.orderType == 3">秒杀订单</span>
            <span v-if="scope.row.orderType == 4">积分订单</span>
          </template>
        </el-table-column>
        <el-table-column prop="orderNum" label="订单号" width="200">
          <template slot-scope="scope">
            <span>{{scope.row.orderNum}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="consignee" label="收货人" width="120">
        </el-table-column>
        <el-table-column prop="mobile" label="手机号" width="120">
        </el-table-column>
        <el-table-column prop="detail" label="详细地址" width="120">
          <template slot-scope="scope">
            <span>{{scope.row.provinces}}{{scope.row.detail}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="商品价格">
          <template slot-scope="scope">
            <span style="color: #f56c6c;">{{scope.row.price | numFilter}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="payMoney" label="支付金额" width="120">
          <template slot-scope="scope">
            <span style="color: #f56c6c;cursor:pointer;">{{scope.row.payMoney | numFilter}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="commissionPrice" label="佣金" width="120">
          <template slot-scope="scope">
            <span style="color: #f56c6c;">{{scope.row.commissionPrice | numFilter}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="refund" label="退款原因" width="160">
          <template slot-scope="scope">
            <span>{{scope.row.refund ? scope.row.refund : '未退款'}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="refusedRefund" label="驳回原因" width="160">
          <template slot-scope="scope">
            <span>{{scope.row.refusedRefund ? scope.row.refusedRefund : '未拒绝'}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="payWay" label="支付方式" width="120">
          <template slot-scope="scope">
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 1 ">微信</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 2 ">微信公众号</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 3 ">微信小程序</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 4 ">app支付宝</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 5 ">H5支付宝</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 6 ">零钱</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === 7 ">积分</span>
            <span style="color: #4f9dec;" v-if="scope.row.payWay === null ">未支付</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" prop="status" label="订单状态">
          <template slot-scope="scope">
            <span style="color: #3E8EF7;" v-if="scope.row.status === 3 ">待核销</span>
            <span v-if="scope.row.status === 4 ">已核销</span>
          </template>
        </el-table-column>
        <el-table-column prop="createAt" label="创建时间" width="160">
        </el-table-column>
        <el-table-column prop="payTime" label="付款时间" width="160">
          <template slot-scope="scope">
            <span>{{scope.row.payTime ? scope.row.payTime : '未付款'}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="finishTime" label="核销时间" width="160">
          <template slot-scope="scope">
            <span>{{scope.row.finishTime ? scope.row.finishTime : '未收货'}}</span>
          </template>
        </el-table-column>
      </el-table>
      <div style="text-align: center;margin-top: 10px;">
        <el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
          :page-sizes="[10, 20, 30,50,100]" :page-size="size" :current-page="page" layout="total,sizes, prev, pager, next"
          :total="tableData2.totalElements">
        </el-pagination>
      </div>
    </el-tab-pane>
    <!-- 拒绝退款 -->
    <el-dialog title="审核订单" :visible.sync="dialogFormVisible2" center>
      <div style="margin-bottom: 10px;">
        <span  style="width: 200px;display: inline-block;text-align: right;position: relative;">审核结果：</span>
        <el-radio-group v-model="isRecommend">
            <el-radio :label="1">同意</el-radio>
            <el-radio :label="2">拒绝</el-radio>
        </el-radio-group>
      </div>
      <div style="margin-bottom: 10px;">
        <span
          style="width: 200px;display: inline-block;text-align: right;position: relative;top: -65px;">审核理由：</span>
        <el-input style="width:50%;" type="textarea" rows="4" v-model="content" placeholder="请输入审核理由">
        </el-input>
      </div>
      <div style="margin-bottom: 10px;" v-if="isRecommend==1">
        <span style="width: 200px;display: inline-block;text-align: right;">收货人：</span>
        <el-input style="width:50%;" type="text" v-model="returnName" placeholder="请输入收货地址">
        </el-input>
      </div>
      <div style="margin-bottom: 10px;" v-if="isRecommend==1">
        <span style="width: 200px;display: inline-block;text-align: right;">电话：</span>
        <el-input style="width:50%;" type="text" v-model="returnPhone" placeholder="请输入收货地址">
        </el-input>
      </div>
      <div style="margin-bottom: 10px;" v-if="isRecommend==1">
        <span style="width: 200px;display: inline-block;text-align: right;">收货地址：</span>
        <el-input style="width:50%;" type="text" v-model="returnAddress" placeholder="请输入收货地址">
        </el-input>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible2 = false">取 消</el-button>
        <el-button type="primary" @click="refuseNoticeTo1()">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 发货弹框 -->
    <el-dialog title="发货" :visible.sync="dialogFormVisible" center>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">快递公司：</span>
        <el-input style="width:50%;" v-model="expressName" placeholder="请输入快递公司"></el-input>
      </div>
      <div style="margin-bottom: 10px;">
        <span style="width: 200px;display: inline-block;text-align: right;">快递单号：</span>
        <el-input style="width: 50%;" v-model="expressNumber" placeholder="请输入快递单号"></el-input>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="deliverNoticeTo()">确 定</el-button>
      </div>
    </el-dialog>
  </el-tabs>
</template>
<script>
  export default {
    data() {
      return {
        size: 5,
        page: 1,
        id: '',
        phone: '',
        orderNum: '',
        title: '',
        mobile: '',
        isExpress: '',
        status: 0,
        orderType: 0,
        orderTypes: [{
            value: 0,
            label: '全部'
          },
          {
            value: 1,
            label: '普通订单'
          },
          {
            value: 2,
            label: '拼团订单'
          },
          {
            value: 3,
            label: '秒杀订单'
          },
          {
            value: 4,
            label: '积分订单'
          }
        ],
        sumincome: '',
        totalnum: 0,
        income: '',
        wxincome: '',
        zfbincome: '',
        yueicome: '',
        ordersum0: '',
        ordersum1: '',
        ordersum2: '',
        expressName: '',
        expressNumber: '',
        refusedRefund: '',
        moneyFrom: 0,
        tableData2: [],
        info: {
          stockDate: this.getNowTime(), //日期
        },
        way: 3,
        ways: [{
          value: 3,
          label: '按天查询'
        }, {
          value: 2,
          label: '按月查询'
        }, {
          value: 1,
          label: '按年查询'
        }],
        statusmain: [{
            value: 0,
            label: '全部'
          },
          {
            value: 1,
            label: '待付款'
          },
          {
            value: 2,
            label: '待发货'
          },
          {
            value: 3,
            label: '已发货'
          },
          {
            value: 4,
            label: '已收货'
          },
          {
            value: 5,
            label: '已取消'
          },
          {
            value: 6,
            label: '退款中'
          },
          {
            value: 7,
            label: '已退款'
          },
          {
            value: 8,
            label: '已驳回'
          },
          {
            value: 9,
            label: '拼团中'
          },
          {
            value: 10,
            label: '已评价'
          }
        ],
        statusnum: [{
          value: 0,
          label: '全部'
        }, {
          value: 1,
          label: '未到账'
        }, {
          value: 2,
          label: '已到账'
        }],
        checkBoxData: [], //多选框选择的值
        userId: '',
        userHead: '',
        userName: '',
        storeId: 0,
        storeHead: '',
        storeName: '省钱兄自营',
        formLabelWidth: '200px',
        activeName: 'first',
        tableDataLoading: false,
        dialogFormVisible: false,
        dialogFormVisible1: false,
        dialogFormVisible2: false,
        dialogFormVisible3: false,
        tableData: [],
        userData: {},
        fenxiaoData: [],
        isRecommend:'',
        content:'',
        returnName:'',
        returnPhone:'',
        returnAddress:'',
      }
    },
    filters: {
      numFilter(value) {
        let realVal = ''
        if (!isNaN(value) && value !== '') {
          // 截取当前数据到小数点后两位
          realVal = parseFloat(value).toFixed(2)
        } else {
          realVal = '--'
        }
        return realVal
      }
    },
    methods: {
      // 多选
      changeFun(val) {
        this.checkBoxData = val;
      },
      // 刷新
      refresh() {
        this.dataSelect()
      },
      // 刷新
      refresh1() {
        this.fenxiaoSelect()
      },
      // 订单评价跳转
      discuss(index, rows) {
        this.$router.push({
          path: '/orderDiscuss',
          query: {
            orderId: rows.id
          }
        });
      },
      // 详情跳转
      updates(row) {
        this.$router.push({
          path: '/userDetail',
          query: {
            userId: row.userId
          }
        })
      },
      // 发货
      colonelClick(rows) {
        if (rows.isExpress == 2) {
          this.id = rows.id
          this.$http({
            url: this.$http.adornUrl('orders/expressVirtual'),
            method: 'get',
            params: this.$http.adornParams({
              'id': rows.id
            })
          }).then(({
            data
          }) => {
            if (data.status == 0) {
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
        } else {
          this.dialogFormVisible = true
          this.id = rows.id
        }
      },
      deliverNoticeTo() {
        if (this.expressName == '') {
          this.$message({
            message: '请输入快递公司',
            type: 'error',
            duration: 1500
          })
          return
        }
        if (this.expressNumber == '') {
          this.$message({
            message: '请输入快递单号',
            type: 'error',
            duration: 1500
          })
          return
        }
        this.$http({
          url: this.$http.adornUrl('orders/express'),
          method: 'get',
          params: this.$http.adornParams({
            'id': this.id,
            'expressName': this.expressName,
            'expressNumber': this.expressNumber
          })
        }).then(({
          data
        }) => {
          if (data.status == 0) {
            this.dialogFormVisible = false
            let returnData = data.data;
            this.orderName = returnData.expressName
            this.orderNumber = returnData.expressNumber
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.expressName = ''
                this.expressNumber = ''
                this.dataSelect()
              }
            })
          }
        })
      },
      // 退款
      refundClick(row) {
        this.$confirm(`确定退款?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('orders/refundMoney'),
            method: 'get',
            params: this.$http.adornParams({
              'ordersId': row.id
            })
          }).then(({
            data
          }) => {
            if (data.status == 0) {
              this.$message({
                message: '退款成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.dataSelect()
                }
              })
            }
            if (data.status == -1) {
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
      // 联系买家
      contact(row) {
        this.userId = row.userId;
        this.$http({
          url: this.$http.adornUrl(`user/${row.userId}`),
          method: "get",
          params: this.$http.adornParams({}),
        }).then(({
          data
        }) => {
          let returnData = data.data;
          if (returnData == null) {
            this.$message({
              message: "用户信息不正确",
              type: "error",
              duration: 1500,
            });
          } else {
            this.userHead = returnData.image_url;
            this.userName = returnData.nickName;
            this.contactSelect();
          }
        });
      },
      contactSelect() {
        this.$http({
          url: this.$http.adornUrl("chat/save"),
          method: "post",
          data: this.$http.adornData({
            userId: this.userId,
            userHead: this.userHead,
            userName: this.userName,
            storeId: this.storeId,
            storeHead: this.storeHead,
            storeName: this.storeName,
          }),
        }).then(({
          data
        }) => {
          if (data.status == 0) {
            console.log("data :>> ", data.userName);
            this.$confirm(`联系买家${data.data.userName}?`, "提示", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning",
            }).then(() => {
              this.$router.push({
                path: "/vueMchat",
                query: {
                  userId: data.data.userId,
                  userName: data.data.userName
                }
              });
            });
          }
        });
      },
      // 拒绝退款
      refuseClick(row) {
        this.ordersId = row.id
        this.dialogFormVisible1 = true
      },
      refuseNoticeTo() {
        if (this.refusedRefund == '') {
          this.$message({
            message: '请输入拒绝退款理由',
            type: 'error',
            duration: 1500
          })
        } else {
          this.$http({
            url: this.$http.adornUrl('orders/refusedRefund'),
            method: 'get',
            params: this.$http.adornParams({
              'ordersId': this.ordersId,
              'refusedRefund': this.refusedRefund,
            })
          }).then(({
            data
          }) => {
            this.dialogFormVisible1 = false
            this.$message({
              message: '操作成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.refusedRefund = ''
                this.dataSelect()
              }
            })
          })
        }
      },
      // 退款、退货审核
      refundClick1(row) {
        this.ordersId = row.id
        this.dialogFormVisible2 = true
      },
      refuseNoticeTo1() {
        if (this.isRecommend == '') {
          this.$message({
            message: '请选择审核结果',
            type: 'error',
            duration: 1500,
          })
          return
        }
        if(this.isRecommend==1){
          if (this.returnName == '') {
            this.$message({
              message: '请输入收货人',
              type: 'error',
              duration: 1500,
            })
            return
          }
          if (this.returnPhone == '') {
            this.$message({
              message: '请输入电话',
              type: 'error',
              duration: 1500,
            })
            return
          }
          if (this.returnAddress == '') {
            this.$message({
              message: '请输入退货地址',
              type: 'error',
              duration: 1500,
            })
            return
          }
        }

        if (this.content == '') {
          this.$message({
            message: '请输入拒绝退款理由',
            type: 'error',
            duration: 1500,
            onClose: () => {
            }
          })
        } else {
          this.$http({
            url: this.$http.adornUrl('orders/auditOrders'),
            method: 'get',
            params: this.$http.adornParams({
              'ordersId': this.ordersId,
              'status': this.isRecommend,
              'content':this.content,
              'returnName':this.returnName,
              'returnPhone':this.returnPhone,
              'returnAddress':this.returnAddress
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
                this.isRecommend = ''
                this.content = ''
                this.returnAddress = ''
                this.dataSelect()
              }
            })
          })
        }
      },
      // 详情
      orderDetails(id) {
        this.$router.push({
          path: '/orderDetails',
          query: {
            id: id
          }
        })
      },
      //处理默认选中当前日期
      getNowTime() {
        var now = new Date()
        var year = now.getFullYear() //得到年份
        var month = now.getMonth() //得到月份
        var date = now.getDate() //得到日期
        month = month + 1
        month = month.toString().padStart(2, '0')
        date = date.toString().padStart(2, '0')
        var defaultDate = `${year}-${month}-${date}`
        return defaultDate
        this.$set(this.info, 'stockDate', defaultDate)
      },
      // 日期选择
      animeDat(value) {
        let vanumber = value
        if (vanumber === 3) {
          this.moneySelect()
          this.orderSelect()
        }
        if (vanumber === 2) {
          this.info.stockDate = this.info.stockDate.substring(0, 7)
          this.moneySelect()
          this.info.stockDate = this.getNowTime()
        }
        if (vanumber === 1) {
          this.info.stockDate = this.info.stockDate.substring(0, 4)
          this.moneySelect()
          this.info.stockDate = this.getNowTime()
        }
      },
      timeDate() {
        this.moneySelect()
        this.orderSelect()
      },
      handleSizeChange(val) {
        this.size = val;
        this.dataSelect()
      },
      handleCurrentChange(val) {
        this.page = val;
        this.dataSelect()
      },
      handleSizeChange1(val) {
        this.size = val;
        this.fenxiaoSelect()
      },
      handleCurrentChange1(val) {
        this.page = val;
        this.fenxiaoSelect()
      },
      handleSizeChange2(val) {
        this.size = val;
        this.dataSelect2()
      },
      handleCurrentChange2(val) {
        this.page = val;
        this.dataSelect2()
      },
      handleClick(tab, event) {
        if (tab._props.label == '全部订单') {
          this.status = 0
          this.orderType = 0
          this.size = 5
          this.page = 1
          this.dataSelect()
        }
        if (tab._props.label == '待发货订单') {
          this.size = 5
          this.page = 1
          this.status = 2
          this.orderType = 0
          this.dataSelect()
        }
        if (tab._props.label == '待退款订单') {
          this.size = 5
          this.page = 1
          this.status = 6
          this.orderType = 0
          this.dataSelect()
        }
        if (tab._props.label == '退货订单') {
          this.size = 5
          this.page = 1
          this.status = 20
          this.orderType = 0
          this.dataSelect()
        }
        if (tab._props.label == '换货订单') {
          this.size = 5
          this.page = 1
          this.status = 21
          this.orderType = 0
          this.dataSelect()
        }
        if (tab._props.label == '分销订单') {
          this.size = 5
          this.page = 1
          this.status = 0
          this.fenxiaoSelect()
        }
        if (tab._props.label == '待核销订单') {
          this.orderNum = ''
          this.mobile = ''
          this.size = 5
          this.page = 1
          this.status = 3
          this.isExpress = 3
          this.orderType = 0
          this.dataSelect2()
        }
        if (tab._props.label == '已核销订单') {
          this.orderNum = ''
          this.mobile = ''
          this.size = 5
          this.page = 1
          this.status = 4
          this.isExpress = 3
          this.orderType = 0
          this.dataSelect2()
        }
      },
      //搜索
      select() {
        this.page = 1
        this.size = 5
        this.dataSelect()
      },
      cleans() {
        this.orderNum = ''
        this.status = 0
        this.orderType = 0
        this.dataSelect()
      },
      select1() {
        this.page = 1
        this.size = 5
        this.fenxiaoSelect()
      },
      cleans1() {
        this.phone = ''
        this.status = 0
        this.fenxiaoSelect()
      },
      select2() {
        this.page = 1
        this.size = 5
        this.dataSelect2()
      },
      cleans2() {
        this.orderNum = ''
        this.mobile = ''
        this.orderType = 0
        this.dataSelect2()
      },
      refresh2() {
        this.dataSelect2()
      },
      // 获取订单数据
      dataSelect() {
        let page = this.page - 1
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('orders/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': page,
            'size': this.size,
            'orderNum': this.orderNum,
            'status': this.status,
            'orderType': this.orderType
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data;
          this.tableData = returnData
        })
      },
      // 获取分销订单数据
      fenxiaoSelect() {
        let page = this.page - 1
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('ordersRelation/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': page,
            'size': this.size,
            'userId': this.userId,
            'phone': this.phone,
            'status': this.status,
            'moneyFrom': this.moneyFrom
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data;
          this.fenxiaoData = returnData.content
          this.totalnum = returnData.totalElements
        })
      },
      // 获取待核销订单数据
      dataSelect2() {
        let page = this.page - 1
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('orders/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': page,
            'size': this.size,
            'orderNum': this.orderNum,
            'status': this.status,
            'title': this.title,
            'mobile': this.mobile,
            'isExpress': this.isExpress,
            'orderType': this.orderType
          })
        }).then(({
          data
        }) => {
          this.tableDataLoading = false
          let returnData = data.data;
          this.tableData2 = returnData
        })
      },
      // 批量核销
      volume(id) {
        var ids = id ? [id] : this.checkBoxData.map(item => {
          return item.id
        })
        for (var i in ids) {
          this.$http({
            url: this.$http.adornUrl('orders/isExpress'),
            method: 'get',
            params: this.$http.adornParams({
              'ids': ids[i]
            })
          }).then(({
            data
          }) => {
            this.$message({
              message: '批量核销成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.dataSelect2()
              }
            })
          })
        }
      },
      // 核销
      volumeOne(row) {
        this.$confirm(`确定核销此条信息?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('orders/isExpress'),
            method: 'get',
            params: this.$http.adornParams({
              'ids': row.id
            })
          }).then(({
            data
          }) => {
            this.$message({
              message: '核销成功',
              type: 'success',
              duration: 1500,
              onClose: () => {
                this.dataSelect2()
              }
            })
          })
        }).catch(() => {})
      },
    },
    mounted() {
      this.dataSelect()
    }
  };
</script>
<style scoped="scoped">
  .box {
    height: 110px;
    padding: 24px;
    border: 1px solid #eee;
    margin: 15px 10px;
  }

  .box_num {
    font-size: 14px;
    color: #66b1ff;
  }

  .box_num .box_color {
    color: #333;
    font-size: 14px;
    margin-bottom: 15px;
  }

  .box_num div span {
    font-size: 20px;
    margin-left: 5px;
  }

  .text_color {
    color: #4f9dec;
  }

  .text_color span {
    margin-right: 5px;
  }

  .el-button+.el-button {
    margin-left: 0;
    margin-top: 5px;
  }
</style>
