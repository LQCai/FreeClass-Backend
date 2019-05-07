package cn.starchild.common.util;

import cn.starchild.common.cache.provider.SimpleCache;
import cn.starchild.common.config.WeChatConfig;
import cn.starchild.common.domain.WechatTemplate;
import cn.starchild.common.http.HttpResponse;
import cn.starchild.common.http.HttpUtils;
import com.alibaba.fastjson.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WechatUtils {

    /**
     * 获取微信openId
     *
     * @param code:js_code
     * @return openId
     */
    public String getOpenId(String code) {
        WeChatConfig weChatConfig = new WeChatConfig();
        String getOpenIdUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + weChatConfig.getAppId() +
                "&secret=" + weChatConfig.getAppSecret() + "&code=CODE&grant_type=authorization_code";

        String requestUrl = getOpenIdUrl.replace("CODE", code);

        HttpResponse data = HttpUtils.get(requestUrl, null);
        JSONObject object = JSONObject.parseObject(data.getResultStr());

        if (object.containsKey("errcode")) {
            return object.getString("errcode");
        }

        return object.getString("openid");
    }

    /**
     * 获取access信息
     *
     * @param code
     * @return
     */
    public JSONObject getAccessInfo(String code) {
        WeChatConfig weChatConfig = new WeChatConfig();
        String getOpenIdUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + weChatConfig.getAppId() +
                "&secret=" + weChatConfig.getAppSecret() + "&code=CODE&grant_type=authorization_code";

        String requestUrl = getOpenIdUrl.replace("CODE", code);

        HttpResponse data = HttpUtils.get(requestUrl, null);

        return JSONObject.parseObject(data.getResultStr());
    }

    /**
     * 获取用户信息
     *
     * @param accessToken
     * @param openId
     * @return String
     */
    public String getUserInfo(String accessToken, String openId) {
        String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN";

        HttpResponse data = HttpUtils.get(requestUrl, null);

        System.out.println(data.getResultStr());

        return data.getResultStr();
    }

    /**
     * 获取accessToken
     * @return
     */
    public JSONObject getAccessToken() {
        WeChatConfig weChatConfig = new WeChatConfig();
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + weChatConfig.getAppId() +
                "&secret=" + weChatConfig.getAppSecret();

        HttpResponse data = HttpUtils.get(requestUrl, null);

        return JSONObject.parseObject(data.getResultStr());
    }

    public JSONObject sendTemplateMsg(String accessToken, Map<String, Object> postData) {
//        String accessToken = getAccessToken().getString("access_token");
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + accessToken;

        HttpResponse data = HttpUtils.postJson(requestUrl, new JSONObject(postData).toString());

        return JSONObject.parseObject(data.getResultStr());
    }


    public static void main(String[] args) {
        WechatUtils wechatUtils = new WechatUtils();

        String accessToken = wechatUtils.getAccessToken().getString("access_token");

        Map<String, String> homeworkData = new HashMap<>();
        homeworkData.put("keyword1", "className");
        homeworkData.put("keyword2", "TEST");
        homeworkData.put("keyword3", "desc");
        homeworkData.put("keyword4", new Date().toString());

        Map<String, Object> templateData = new HashMap<>();
        templateData.put("touser", "okmLw0NEO3Ia11cd72CMLu3nCdG0");
        templateData.put("template_id", "9h8OC1BeVXwLNuYiS8RYznXXB034R9VO4c_OMyBibaM");
        templateData.put("form_id", "wx20190101");
        templateData.put("data", homeworkData);


        System.out.println(wechatUtils.sendTemplateMsg(accessToken, templateData));
    }
}
