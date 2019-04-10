package cn.starchild.user.controller;

import cn.starchild.common.domain.ResData;
import cn.starchild.common.model.UserModel;
import cn.starchild.common.util.WechatUtils;
import cn.starchild.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/user/wechat")
@Controller
public class WeChatController {
    @Autowired
    private UserService userService;

    @RequestMapping("/getOpenId")
    @ResponseBody
    public ResData getOpenId(String code) {
        if (code == null) {
            return  ResData.error("code不可为空");
        }

        WechatUtils wechatUtils = new WechatUtils();
        String openId = wechatUtils.getOpenId(code);

        boolean isUser = userService.hasUserForOpenId(openId);
        if (!isUser) {
            //获取微信信息并插入数据
            return  ResData.error("尚未注册");
        }

        ResData resData = new ResData();
        resData.put("openId", openId);
        return resData;
    }

    /**
     * 获取openId(用户注册)
     *
     * @param code:js_code
     * @return openId
     */
    @RequestMapping("/getAuthOpenId")
    @ResponseBody
    public ResData getAuthOpenId(String code) {
        if (code == null) {
            return  ResData.error("code不可为空");
        }

        WechatUtils wechatUtils = new WechatUtils();
        String openId = wechatUtils.getOpenId(code);

        UserModel user = userService.findOneByOpenId(openId);
        if (user == null) {
            return  ResData.error(openId);
        }

        ResData resData = new ResData();
        resData.put("openId", openId);
        return resData;
    }
}
