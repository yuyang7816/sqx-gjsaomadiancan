package com.sqx.modules.utils.fieYun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.goods.entity.GoodsShop;
import com.sqx.modules.order.entity.OrderGoods;
import com.sqx.modules.order.entity.TbOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class FeiYunUtils {


    private static CommonInfoService commonInfoService;

    @Autowired
    public void setCommonInfoService(CommonInfoService commonInfoService) {
        FeiYunUtils.commonInfoService = commonInfoService;
    }

    /**
     * 绑定打印机推送
     *
     * @param
     * @return
     */
    public static String addPrinter(Integer facility, String snCode, String value, String remark) {
        //目前只有飞鹅pos机可以推送
        if (facility != null && facility.equals(1)) {
            String snlist = snCode + "#" + value + "#" + remark;
            String method = FeiYunUtils.addprinter(snlist);
            JSONObject jsonObject = JSON.parseObject(method);
            if (jsonObject != null) {
                if ("0".equals(jsonObject.getString("ret"))) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray no = data.getJSONArray("no");
                    if (no.size() > 0) {
                        return no.getString(0);
                    }
                    return "ok";
                }
                return jsonObject.getString("msg");
            }
            return "pos机添加失败！";
        } else {
            return "ok";
        }
    }


    /**
     * 添加打印机接口
     *
     * @param snlist 提示：打印机编号(必填) # 打印机识别码(必填) # 备注名称(选填) # 流量卡号码(选填)，多台打印机请换行（\n）添加新打印机信息，每次最多100行(台)。
     * @return 正确例子：{"msg":"ok","ret":0,"data":{"ok":["sn#key#remark#carnum","316500011#abcdefgh#快餐前台"],"no":["316500012#abcdefgh#快餐前台#13688889999  （错误：识别码不正确）"]},"serverExecutedTime":3}
     * 错误：{"msg":"参数错误 : 该帐号未注册.","ret":-2,"data":null,"serverExecutedTime":37}
     */
    public static String addprinter(String snlist) {
        String URL = commonInfoService.findOne(325).getValue();
        String USER = commonInfoService.findOne(326).getValue();
        String UKEY = commonInfoService.findOne(327).getValue();
        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user", USER));
        String STIME = String.valueOf(System.currentTimeMillis() / 1000);
        nvps.add(new BasicNameValuePair("stime", STIME));
        nvps.add(new BasicNameValuePair("sig", signature(USER, UKEY, STIME)));
        nvps.add(new BasicNameValuePair("apiname", "Open_printerAddlist"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("printerContent", snlist));

        CloseableHttpResponse response = null;
        String result = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if (statecode == 200) {
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null) {
                    result = EntityUtils.toString(httpentity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }

    /**
     * 修改打印机接口
     *
     * @param snlist 提示：打印机编号(必填) # 打印机识别码(必填) # 备注名称(选填) # 流量卡号码(选填)，多台打印机请换行（\n）添加新打印机信息，每次最多100行(台)。
     * @return 正确例子：{"msg":"ok","ret":0,"data":{"ok":["sn#key#remark#carnum","316500011#abcdefgh#快餐前台"],"no":["316500012#abcdefgh#快餐前台#13688889999  （错误：识别码不正确）"]},"serverExecutedTime":3}
     * 错误：{"msg":"参数错误 : 该帐号未注册.","ret":-2,"data":null,"serverExecutedTime":37}
     */
    public static String updatePrinter(String snlist, String name) {
        String URL = commonInfoService.findOne(325).getValue();
        String USER = commonInfoService.findOne(326).getValue();
        String UKEY = commonInfoService.findOne(327).getValue();
        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user", USER));
        String STIME = String.valueOf(System.currentTimeMillis() / 1000);
        nvps.add(new BasicNameValuePair("stime", STIME));
        nvps.add(new BasicNameValuePair("sig", signature(USER, UKEY, STIME)));
        nvps.add(new BasicNameValuePair("apiname", "Open_printerEdit"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn", snlist));
        nvps.add(new BasicNameValuePair("name", name));
        CloseableHttpResponse response = null;
        String result = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if (statecode == 200) {
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null) {
                    result = EntityUtils.toString(httpentity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 删除打印机接口
     *
     * @param snlist 提示：打印机编号(必填) # 打印机识别码(必填) # 备注名称(选填) # 流量卡号码(选填)，多台打印机请换行（\n）添加新打印机信息，每次最多100行(台)。
     * @return 正确例子：{"msg":"ok","ret":0,"data":{"ok":["sn#key#remark#carnum","316500011#abcdefgh#快餐前台"],"no":["316500012#abcdefgh#快餐前台#13688889999  （错误：识别码不正确）"]},"serverExecutedTime":3}
     * 错误：{"msg":"参数错误 : 该帐号未注册.","ret":-2,"data":null,"serverExecutedTime":37}
     */
    public static String deletePrinter(String snlist) {
        try {
            String URL = commonInfoService.findOne(325).getValue();
            String USER = commonInfoService.findOne(326).getValue();
            String UKEY = commonInfoService.findOne(327).getValue();
            //通过POST请求，发送打印信息到服务器
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(30000)//读取超时
                    .setConnectTimeout(30000)//连接超时
                    .build();

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .build();

            HttpPost post = new HttpPost(URL);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("user", USER));
            String STIME = String.valueOf(System.currentTimeMillis() / 1000);
            nvps.add(new BasicNameValuePair("stime", STIME));
            nvps.add(new BasicNameValuePair("sig", signature(USER, UKEY, STIME)));
            nvps.add(new BasicNameValuePair("apiname", "Open_printerDelList"));//固定值,不需要修改
            nvps.add(new BasicNameValuePair("snlist", snlist));

            CloseableHttpResponse response = null;
            String result = null;
            try {
                post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
                response = httpClient.execute(post);
                int statecode = response.getStatusLine().getStatusCode();
                if (statecode == 200) {
                    HttpEntity httpentity = response.getEntity();
                    if (httpentity != null) {
                        result = EntityUtils.toString(httpentity);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (response != null) {
                        response.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    post.abort();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除打印机异常:" + e.getMessage(), e);
        }
        return null;
    }


    /**
     * 小票机打印订单接口
     *
     * @param sn       打印机编号
     * @param classify 分类 1加油成功出票模版  2提货券推送模版  3加油订单结算模版  4商品消费结算
     * @return 成功：{"msg":"ok","ret":0,"data":"xxxxxxx_xxxxxxxx_xxxxxxxx","serverExecutedTime":5}
     * 失败：{"msg":"错误描述","ret":非0,"data":"null","serverExecutedTime":5}
     */
    public static String print(String sn, Integer classify, TbOrder tbOrder) {
        try {
            //标签说明：
            //单标签:
            //"<BR>"为换行,"<CUT>"为切刀指令(主动切纸,仅限切刀打印机使用才有效果)
            //"<LOGO>"为打印LOGO指令(前提是预先在机器内置LOGO图片),"<PLUGIN>"为钱箱或者外置音响指令
            //成对标签：
            //"<CB></CB>"为居中放大一倍,"<B></B>"为放大一倍,"<C></C>"为居中,<L></L>字体变高一倍
            //<W></W>字体变宽一倍,"<QR></QR>"为二维码,"<BOLD></BOLD>"为字体加粗,"<RIGHT></RIGHT>"为右对齐
            //拼凑订单内容时可参考如下格式
            //根据打印纸张的宽度，自行调整内容的格式，可参考下面的样例格式


        /*content = "<CB>测试打印</CB><BR>";
        content += "名称　　　　　 单价  数量 金额<BR>";
        content += "--------------------------------<BR>";
        content += "饭　　　　　　 1.0    1   1.0<BR>";
        content += "炒饭　　　　　 10.0   10  10.0<BR>";
        content += "蛋炒饭　　　　 10.0   10  100.0<BR>";
        content += "鸡蛋炒饭　　　 100.0  1   100.0<BR>";
        content += "番茄蛋炒饭　　 1000.0 1   100.0<BR>";
        content += "西红柿蛋炒饭　 1000.0 1   100.0<BR>";
        content += "西红柿鸡蛋炒饭 100.0  10  100.0<BR>";
        content += "备注：加辣<BR>";
        content += "--------------------------------<BR>";
        content += "合计：xx.0元<BR>";
        content += "送货地点：广州市南沙区xx路xx号<BR>";
        content += "联系电话：13888888888888<BR>";
        content += "订餐时间：2016-08-08 08:08:08<BR>";
        content += "<QR>http://www.dzist.com</QR>";*/
            List<OrderGoods> orderGoodsList = tbOrder.getOrderGoodsList();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<CB>").append(tbOrder.getShopName()).append("</CB><BR>");
            if (tbOrder.getOrderType() == 1) {
                stringBuilder.append("<BOLD>类型：").append("到店取餐</BOLD><BR>");
                stringBuilder.append("<BOLD>取餐号：").append(tbOrder.getOrderCode()).append("</BOLD><BR>");
            } else if (tbOrder.getOrderType() == 2) {
                stringBuilder.append("<BOLD>类型：").append("外卖配送</BOLD><BR>");
            } else {
                stringBuilder.append("<BOLD>类型：").append("到店堂食</BOLD><BR>");
                stringBuilder.append("<BOLD>取餐号：").append(tbOrder.getOrderCode()).append("</BOLD><BR>");
            }
            stringBuilder.append("<BOLD>订单号：").append(tbOrder.getOrderNumber()).append("</BOLD><BR>");
            stringBuilder.append("--------------------------------<BR>");
            stringBuilder.append("名称　　　  单价　 数量　 金额　<BR>");
            stringBuilder.append("--------------------------------<BR>");
            BigDecimal packMoney = BigDecimal.ZERO;
            BigDecimal goodsMoney = BigDecimal.ZERO;
            stringBuilder.append("<BR>");
            for (OrderGoods orderGoods : orderGoodsList) {
                BigDecimal sumMoney = orderGoods.getGoodsPrice().multiply(BigDecimal.valueOf(orderGoods.getGoodsNum()));
                BigDecimal sumPack = orderGoods.getGoodsPack().multiply(BigDecimal.valueOf(orderGoods.getGoodsNum()));
                String remark = orderGoods.getGoodRemarks();
                packMoney = packMoney.add(sumPack);
                goodsMoney = goodsMoney.add(sumMoney);
                String name;
                if (StringUtils.isNotEmpty(orderGoods.getSkuMessage())) {
                    name = orderGoods.getGoodsName() + "(" + orderGoods.getSkuMessage() + ")";
                } else {
                    name = orderGoods.getGoodsName();
                }
                stringBuilder.append(name).append("<BR>");
                stringBuilder.append("　　　　　  ");
                stringBuilder.append(orderGoods.getGoodsPrice());
                if (orderGoods.getGoodsPrice().doubleValue() < 10) {
                    stringBuilder.append("　   ");
                } else if (orderGoods.getGoodsPrice().doubleValue() < 100) {
                    stringBuilder.append("　  ");
                } else {
                    stringBuilder.append("　 ");
                }
                stringBuilder.append(orderGoods.getGoodsNum());
                stringBuilder.append("　  ");
                stringBuilder.append(sumMoney).append("<BR>");
                stringBuilder.append("<BR>");
                if (StringUtils.isNotBlank(remark)) {
                    stringBuilder.append("备注:").append(remark).append("<BR>");
                }


            }
            stringBuilder.append("--------------------------------<BR>");
            stringBuilder.append("打包费：").append(packMoney).append("元<BR>");
            stringBuilder.append("商品费：").append(goodsMoney).append("元<BR>");
            if (tbOrder.getCouponMoney() != null) {
                stringBuilder.append("优惠券：-").append(tbOrder.getCouponMoney()).append("元<BR>");
            }
            stringBuilder.append("合计：").append(tbOrder.getPayMoney()).append("元<BR>");
            if (StringUtils.isNotBlank(tbOrder.getOrderRemarks())) {
                stringBuilder.append("备注：").append(tbOrder.getOrderRemarks()).append("<BR>");
            }
            stringBuilder.append("--------------------------------<BR>");
            if (StringUtils.isNotEmpty(tbOrder.getAddress())) {
                JSONObject jsonObject = JSONObject.parseObject(tbOrder.getAddress());
                String addressDetail = jsonObject.getString("addressDetail");
                String address = jsonObject.getString("address");
                String userName = jsonObject.getString("userName");
                String phone = jsonObject.getString("userPhone");
                stringBuilder.append("姓名：").append(userName).append("<BR>");
                stringBuilder.append("联系电话：").append(phone).append("<BR>");
                stringBuilder.append("送货地点：").append(address).append(addressDetail).append("<BR>");
            } else {
                stringBuilder.append("联系电话：").append(tbOrder.getPhone()).append("<BR>");
            }

            stringBuilder.append("订餐时间：").append(tbOrder.getPayTime()).append("<BR>");
            stringBuilder.append("<BR>").append("<BR>").append("<BR>");
            stringBuilder.append("<CB>祝您用餐愉快</CB><BR>");

            stringBuilder.append("<BR>").append("<BR>").append("<BR>");
            stringBuilder.append("<CUT>");
            stringBuilder.append("<BR>").append("<BR>").append("<BR>");
            stringBuilder.append("<CB>").append(tbOrder.getShopName()).append("</CB><BR>");
            if (tbOrder.getOrderType() == 1) {
                stringBuilder.append("<BOLD>类型：").append("到店取餐</BOLD><BR>");
                stringBuilder.append("<BOLD>取餐号：").append(tbOrder.getOrderCode()).append("</BOLD><BR>");
            } else if (tbOrder.getOrderType() == 2) {
                stringBuilder.append("<BOLD>类型：").append("外卖配送</BOLD><BR>");
            } else {
                stringBuilder.append("<BOLD>类型：").append("到店堂食</BOLD><BR>");
                stringBuilder.append("<BOLD>取餐号：").append(tbOrder.getOrderCode()).append("</BOLD><BR>");
            }
            stringBuilder.append("<BOLD>订单号：").append(tbOrder.getOrderNumber()).append("</BOLD><BR>");
            stringBuilder.append("--------------------------------<BR>");
            stringBuilder.append("名称　　　  单价　 数量　 金额　<BR>");
            stringBuilder.append("--------------------------------<BR>");
            stringBuilder.append("<BR>");
            for (OrderGoods orderGoods : orderGoodsList) {
                String remark = orderGoods.getGoodRemarks();
                BigDecimal sumMoney = orderGoods.getGoodsPrice().multiply(BigDecimal.valueOf(orderGoods.getGoodsNum()));
                String name;
                if (StringUtils.isNotEmpty(orderGoods.getSkuMessage())) {
                    name = orderGoods.getGoodsName() + "(" + orderGoods.getSkuMessage() + ")";
                } else {
                    name = orderGoods.getGoodsName();
                }
                stringBuilder.append(name).append("<BR>");
                stringBuilder.append("　　　　　  ");
                stringBuilder.append(orderGoods.getGoodsPrice());
                if (orderGoods.getGoodsPrice().doubleValue() < 10) {
                    stringBuilder.append("　   ");
                } else if (orderGoods.getGoodsPrice().doubleValue() < 100) {
                    stringBuilder.append("　  ");
                } else {
                    stringBuilder.append("　 ");
                }
                stringBuilder.append(orderGoods.getGoodsNum());
                stringBuilder.append("　  ");
                stringBuilder.append(sumMoney).append("<BR>");
                stringBuilder.append("<BR>");
                if (StringUtils.isNotBlank(remark)) {
                    stringBuilder.append("备注:").append(remark).append("<BR>");
                }

            }

            stringBuilder.append("--------------------------------<BR>");
            stringBuilder.append("打包费：").append(packMoney).append("元<BR>");
            stringBuilder.append("商品费：").append(goodsMoney).append("元<BR>");
            if (tbOrder.getCouponMoney() != null) {
                stringBuilder.append("优惠券：-").append(tbOrder.getCouponMoney()).append("元<BR>");
            }
            stringBuilder.append("合计：").append(tbOrder.getPayMoney()).append("元<BR>");
            if (StringUtils.isNotBlank(tbOrder.getOrderRemarks())) {
                stringBuilder.append("备注：").append(tbOrder.getOrderRemarks()).append("<BR>");
            }
            stringBuilder.append("--------------------------------<BR>");
            if (StringUtils.isNotEmpty(tbOrder.getAddress())) {
                JSONObject jsonObject = JSONObject.parseObject(tbOrder.getAddress());
                String addressDetail = jsonObject.getString("addressDetail");
                String address = jsonObject.getString("address");
                String userName = jsonObject.getString("userName");
                String phone = jsonObject.getString("phone");
                stringBuilder.append("姓名：").append(userName).append("<BR>");
                stringBuilder.append("联系电话：").append(phone).append("<BR>");
                stringBuilder.append("送货地点：").append(address).append(addressDetail).append("<BR>");
            } else {
                stringBuilder.append("联系电话：").append(tbOrder.getPhone()).append("<BR>");
            }
            stringBuilder.append("订餐时间：").append(tbOrder.getPayTime()).append("<BR>");
            String content = stringBuilder.toString();
            //通过POST请求，发送打印信息到服务器
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(30000)//读取超时
                    .setConnectTimeout(30000)//连接超时
                    .build();

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .build();
            String URL = commonInfoService.findOne(325).getValue();
            String USER = commonInfoService.findOne(326).getValue();
            String UKEY = commonInfoService.findOne(327).getValue();
            HttpPost post = new HttpPost(URL);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("user", USER));
            String STIME = String.valueOf(System.currentTimeMillis() / 1000);
            nvps.add(new BasicNameValuePair("stime", STIME));
            nvps.add(new BasicNameValuePair("sig", signature(USER, UKEY, STIME)));
            nvps.add(new BasicNameValuePair("apiname", "Open_printMsg"));//固定值,不需要修改
            nvps.add(new BasicNameValuePair("sn", sn));
            nvps.add(new BasicNameValuePair("content", content));
            nvps.add(new BasicNameValuePair("times", "1"));//打印联数

            CloseableHttpResponse response = null;
            String result = null;
            try {
                post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
                response = httpClient.execute(post);
                int statecode = response.getStatusLine().getStatusCode();
                if (statecode == 200) {
                    HttpEntity httpentity = response.getEntity();
                    if (httpentity != null) {
                        //服务器返回的JSON字符串，建议要当做日志记录起来
                        result = EntityUtils.toString(httpentity);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (response != null) {
                        response.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    post.abort();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            log.error("打印返回值：" + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("打印异常：" + e.getMessage(), e);
        }
        return null;
    }


    /**
     * 标签机专用打印订单接口
     *
     * @param sn 打印机编号
     * @return 成功：{"msg":"ok","ret":0,"data":"xxxxxxx_xxxxxxxx_xxxxxxxx","serverExecutedTime":5}
     * 失败：{"msg":"错误描述","ret":非0,"data":"null","serverExecutedTime":5}
     */
    private static String printLabelMsg(String sn) {

        String content;
        content = "<DIRECTION>1</DIRECTION>";//设定打印时出纸和打印字体的方向，n 0 或 1，每次设备重启后都会初始化为 0 值设置，1：正向出纸，0：反向出纸，
        content += "<TEXT x='9' y='10' font='12' w='1' h='2' r='0'>#001       五号桌      1/3</TEXT><TEXT x='80' y='80' font='12' w='2' h='2' r='0'>可乐鸡翅</TEXT><TEXT x='9' y='180' font='12' w='1' h='1' r='0'>张三先生       13800138000</TEXT>";//40mm宽度标签纸打印例子，打开注释调用标签打印接口打印

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
        String URL = commonInfoService.findOne(325).getValue();
        String USER = commonInfoService.findOne(326).getValue();
        String UKEY = commonInfoService.findOne(327).getValue();
        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user", USER));
        String STIME = String.valueOf(System.currentTimeMillis() / 1000);
        nvps.add(new BasicNameValuePair("stime", STIME));
        nvps.add(new BasicNameValuePair("sig", signature(USER, UKEY, STIME)));
        nvps.add(new BasicNameValuePair("apiname", "Open_printLabelMsg"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn", sn));
        nvps.add(new BasicNameValuePair("content", content));
        nvps.add(new BasicNameValuePair("times", "1"));//打印联数

        CloseableHttpResponse response = null;
        String result = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if (statecode == 200) {
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null) {
                    //服务器返回的JSON字符串，建议要当做日志记录起来
                    result = EntityUtils.toString(httpentity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }


    /**
     * 查询某订单是否打印成功
     *
     * @param orderid 订单id
     * @return 成功：{"msg":"ok","ret":0,"data":true,"serverExecutedTime":2}//data:true为已打印,false为未打印
     * 失败：{"msg":"错误描述","ret":非0, "data":null,"serverExecutedTime":7}
     */
    private static String queryOrderState(String orderid) {

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
        String URL = commonInfoService.findOne(325).getValue();
        String USER = commonInfoService.findOne(326).getValue();
        String UKEY = commonInfoService.findOne(327).getValue();
        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user", USER));
        String STIME = String.valueOf(System.currentTimeMillis() / 1000);
        nvps.add(new BasicNameValuePair("stime", STIME));
        nvps.add(new BasicNameValuePair("sig", signature(USER, UKEY, STIME)));
        nvps.add(new BasicNameValuePair("apiname", "Open_queryOrderState"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("orderid", orderid));

        CloseableHttpResponse response = null;
        String result = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if (statecode == 200) {
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null) {
                    //服务器返回
                    result = EntityUtils.toString(httpentity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }


    /**
     * 查询指定打印机某天的订单详情
     *
     * @param sn      打印机编号
     * @param strdate 时间 "yyyy-MM-dd"
     * @return 成功：{"msg":"ok","ret":0,"data":{"print":6,"waiting":1},"serverExecutedTime":9}//print已打印，waiting为打印
     * 失败：{"msg":"错误描述","ret":非0,"data":"null","serverExecutedTime":5}
     */
    private static String queryOrderInfoByDate(String sn, String strdate) {

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
        String URL = commonInfoService.findOne(325).getValue();
        String USER = commonInfoService.findOne(326).getValue();
        String UKEY = commonInfoService.findOne(327).getValue();
        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user", USER));
        String STIME = String.valueOf(System.currentTimeMillis() / 1000);
        nvps.add(new BasicNameValuePair("stime", STIME));
        nvps.add(new BasicNameValuePair("sig", signature(USER, UKEY, STIME)));
        nvps.add(new BasicNameValuePair("apiname", "Open_queryOrderInfoByDate"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn", sn));
        nvps.add(new BasicNameValuePair("date", strdate));//yyyy-MM-dd格式

        CloseableHttpResponse response = null;
        String result = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if (statecode == 200) {
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null) {
                    //服务器返回
                    result = EntityUtils.toString(httpentity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }


    /**
     * 查询打印机的状态
     *
     * @param sn 打印机编号
     * @return 成功：{"msg":"ok","ret":0,"data":"状态","serverExecutedTime":4}
     * 失败：{"msg":"错误描述","ret":非0,"data":"null","serverExecutedTime":5}
     */
    private static String queryPrinterStatus(String sn) {

        //通过POST请求，发送打印信息到服务器
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(30000)//读取超时
                .setConnectTimeout(30000)//连接超时
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
        String URL = commonInfoService.findOne(325).getValue();
        String USER = commonInfoService.findOne(326).getValue();
        String UKEY = commonInfoService.findOne(327).getValue();
        HttpPost post = new HttpPost(URL);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("user", USER));
        String STIME = String.valueOf(System.currentTimeMillis() / 1000);
        nvps.add(new BasicNameValuePair("stime", STIME));
        nvps.add(new BasicNameValuePair("sig", signature(USER, UKEY, STIME)));
        nvps.add(new BasicNameValuePair("apiname", "Open_queryPrinterStatus"));//固定值,不需要修改
        nvps.add(new BasicNameValuePair("sn", sn));

        CloseableHttpResponse response = null;
        String result = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            response = httpClient.execute(post);
            int statecode = response.getStatusLine().getStatusCode();
            if (statecode == 200) {
                HttpEntity httpentity = response.getEntity();
                if (httpentity != null) {
                    //服务器返回
                    result = EntityUtils.toString(httpentity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                post.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }


    //生成签名字符串
    private static String signature(String USER, String UKEY, String STIME) {
        String s = DigestUtils.sha1Hex(USER + UKEY + STIME);
        return s;
    }


}
