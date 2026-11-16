package com.sqx.modules.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.sqx.common.utils.Result;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.shopSeat.entity.ShopSeat;
import com.sqx.modules.shopSeat.service.ShopSeatService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Slf4j
public class SenInfoCheckUtil {

    private static Logger logger = LoggerFactory.getLogger(SenInfoCheckUtil.class);

    private static String MpAccessToken;

    // 这里使用静态，让 service 属于类
    private static CommonInfoService commonInfoService;


    // 注入的时候，给类的 service 注入
    @Autowired
    public void setWxChatContentService(CommonInfoService commonInfoService) {
        SenInfoCheckUtil.commonInfoService = commonInfoService;
    }


    /**
     * 获取Token  小程序
     *
     * @param
     * @param
     * @return AccessToken
     */
    public static String getMpToken() {
        /*if(StringUtils.isEmpty(MpAccessToken)){
            getMpAccessToken();
        }*/
        return getMpAccessToken();
    }


    public static void getImg(String relation, String goodsId, String type, String page, HttpServletResponse response) {
        String mpToken = getMpToken();
        //获取二维码数据
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + mpToken;
        Map<String, Object> map = Maps.newHashMap();
        map.put("scene", relation + "&" + goodsId + "&" + type);
        String value = commonInfoService.findOne(105).getValue();
        if ("是".equals(value)) {
            map.put("page", page);
        }
        map.put("width", 280);
        String jsonString = JSON.toJSONString(map);
        InputStream inputStream = sendPostBackStream(url, jsonString);
        //生成二维码图片
        response.setContentType("image/png");
        try {
            BufferedImage bi = ImageIO.read(inputStream);
            ImageIO.write(bi, "JPG", response.getOutputStream());
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取二维码图片
     */
    public static void getPoster(String invitationCode, HttpServletResponse response) {
        String mpToken = getMpToken();
        //获取二维码数据
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + mpToken;
        Map<String, Object> map = Maps.newHashMap();
        map.put("scene", invitationCode);
        map.put("width", 280);
        String jsonString = JSON.toJSONString(map);
        InputStream inputStream = sendPostBackStream(url, jsonString);
        //生成二维码图片
        response.setContentType("image/png");
        try {
            BufferedImage bi = ImageIO.read(inputStream);
            ImageIO.write(bi, "JPG", response.getOutputStream());
            inputStream.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 获取二维码图片
     */
    public static void getShopQr(String shopId, HttpServletResponse response) {
        String mpToken = getMpToken();
        //获取二维码数据
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + mpToken;
        Map<String, Object> map = Maps.newHashMap();
        map.put("scene", shopId);
        CommonInfo one = commonInfoService.findOne(261);
        if ("否".equals(one.getValue())) {
            map.put("page", "pages/index/index");
        }

        map.put("width", 280);
        String jsonString = JSON.toJSONString(map);
        InputStream inputStream = sendPostBackStream(url, jsonString);
        //生成二维码图片
        response.setContentType("image/png");
        try {
            BufferedImage bi = ImageIO.read(inputStream);
            ImageIO.write(bi, "JPG", response.getOutputStream());
            inputStream.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private static InputStream sendPostBackStream(String url, String param) {
        PrintWriter out = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //解决乱码问题
            OutputStreamWriter outWriter = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
            out = new PrintWriter(outWriter);
            // 发送请求参数
            if (StringUtils.isNotBlank(param)) {
                out.print(param);
            }
            // flush输出流的缓冲
            out.flush();
            return conn.getInputStream();
        } catch (Exception e) {
            logger.error("发送 POST 请求出现异常！" + e);
        } finally {
            IOUtils.closeQuietly(out);
        }
        return null;
    }


    /**
     * 获取access_token
     * 每个两个小时自动刷新AcessTocken
     */
    // @Scheduled(cron = "0 0 */2 * * ?")
    public static String getMpAccessToken() {
        String appid = commonInfoService.findOne(45).getValue();
        String secret = commonInfoService.findOne(46).getValue();
        String jsonResult = HttpClientUtil.doPost("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret);
        JSONObject parseObject = JSON.parseObject(jsonResult);
        logger.info("=========accessTokenOut=========" + parseObject.toJSONString());

        String errcode = parseObject.getString("errcode");
        String accessToken = parseObject.getString("access_token");
        String expiresIn = parseObject.getString("expires_in");
        return accessToken;
    }


    /**
     * 小程序消息推送
     *
     * @param wxId       用户openId
     * @param templateId 模板id
     * @param msgList    消息内容集合
     * @param type       类型
     */
    public static void sendMsg(String wxId, String templateId, List<String> msgList, Integer type) {
        String mpToken = getMpToken();
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + mpToken;
        //拼接推送的模版
        WxMssVo wxMssVo = new WxMssVo();
        wxMssVo.setTouser(wxId);//用户的openid（要发送给那个用户，通常这里应该动态传进来的）
        wxMssVo.setTemplate_id(templateId);//订阅消息模板id
//        wxMssVo.setPage("pages/index/index");
        wxMssVo.setData(getParam(type, msgList));
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(url, wxMssVo, String.class);
        String body = responseEntity.getBody();
        //System.err.println(body);
        log.error("小程序消息通知返回值");
        log.error("小程序消息通知返回值：" + body);
    }

    /**
     * 订阅消息模板
     *
     * @param type    类型  1 用户下单成功通知
     * @param msgList 对应值
     * @return
     */
    public static Map<String, TemplateParam> getParam(Integer type, List<String> msgList) {
        /*if(type==1){
            Map<String, TemplateParam> paras = new HashMap<>();
            //店铺名称
            paras.put("name1",new TemplateParam( msgList.get(0)));
            //订单金额
            paras.put("amount3",new TemplateParam( msgList.get(1)));
            //订单编号
            paras.put("character_string4",new TemplateParam( msgList.get(2)));
            //下单时间
            paras.put("date2",new TemplateParam( msgList.get(3)));
            return paras;
        }else if(type==2){
            Map<String, TemplateParam> paras = new HashMap<>();
            //店铺名称
            paras.put("thing5",new TemplateParam( msgList.get(0)));
            //商品名称
            paras.put("character_string6",new TemplateParam( msgList.get(1)));
            //订单金额
            paras.put("phrase2",new TemplateParam( msgList.get(2)));
            //订单编号
            paras.put("time3",new TemplateParam( msgList.get(3)));
            //下单时间
            paras.put("thing4",new TemplateParam( msgList.get(4)));
            return paras;
        }*/
        if (type == 1) {
            Map<String, TemplateParam> paras = new HashMap<>();
            //订单状态
            paras.put("phrase3", new TemplateParam(msgList.get(0)));
            //订单号
            paras.put("character_string1", new TemplateParam(msgList.get(1)));
            //服务门店
            paras.put("thing17", new TemplateParam(msgList.get(2)));
            //订单时间
            paras.put("time12", new TemplateParam(msgList.get(3)));
            return paras;
        } else if (type == 3) {
            Map<String, TemplateParam> paras = new HashMap<>();
            //订单号
            paras.put("character_string1", new TemplateParam(msgList.get(0)));
            //取消原因
            paras.put("thing2", new TemplateParam(msgList.get(1)));
            //退款金额
            paras.put("amount14", new TemplateParam(msgList.get(2)));
            //取消时间
            paras.put("time5", new TemplateParam(msgList.get(3)));
            return paras;
        } else if (type == 4) {
            Map<String, TemplateParam> paras = new HashMap<>();
            //服务门店
            paras.put("thing17", new TemplateParam(msgList.get(0)));
            //订单号
            paras.put("character_string1", new TemplateParam(msgList.get(1)));
            //订单状态
            paras.put("phrase3", new TemplateParam(msgList.get(2)));
            //操作时间
            paras.put("time11", new TemplateParam(msgList.get(3)));
            //备注
            paras.put("thing8", new TemplateParam(msgList.get(4)));
            return paras;
        }
        return null;
    }


}
