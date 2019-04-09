package cn.starchild.common.util;

import cn.starchild.common.config.WeChatConfig;
import cn.starchild.common.http.HttpResponse;
import cn.starchild.common.http.HttpUtils;
import com.alibaba.fastjson.JSONObject;

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

        return  data.getResultStr();
    }

}
