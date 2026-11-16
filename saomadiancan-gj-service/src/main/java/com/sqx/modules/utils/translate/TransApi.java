package com.sqx.modules.utils.translate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sqx.modules.common.service.CommonInfoService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TransApi {
    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    // 这里使用静态，让 service 属于类
    private static CommonInfoService commonInfoService;

    // 注入的时候，给类的 service 注入
    @Autowired
    public void setWxChatContentService(CommonInfoService commonInfoService) {
        TransApi.commonInfoService = commonInfoService;
    }


    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public static String getTransResult(String query, String from, String to) throws IOException {
        log.info("翻译入参："+query+" "+from+" "+to);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"from\":\""+from+"\",\"to\":\""+to+"\",\"q\":\""+query+"\"}");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/mt/texttrans/v1?access_token=" + getAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        String str=response.body().string();
        log.info("翻译返回值："+str);
        JSONObject jsonObject = JSONObject.parseObject(str);
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray transResult = result.getJSONArray("trans_result");
        JSONObject jsonObject1 = transResult.getJSONObject(0);
        return jsonObject1.getString("dst");
    }


    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    static String getAccessToken() throws IOException {
        String appkey = commonInfoService.findOne(422).getValue();     // 您的应用ID
        String securityKey = commonInfoService.findOne(423).getValue();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + appkey
                + "&client_secret=" + securityKey);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        JSONObject json =JSONObject.parseObject(response.body().string());
        String access_token = json.getString("access_token");
        return access_token;
    }

}
