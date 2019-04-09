package cn.starchild.user.controller;

import cn.starchild.common.domain.ResData;
import cn.starchild.common.model.UserModel;
import cn.starchild.common.util.UUIDUtils;
import cn.starchild.common.util.WechatUtils;
import cn.starchild.user.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@RequestMapping("/user/account")
@Controller
public class AccountInfoController {
    @Autowired
    private UserService userService;

    @RequestMapping("/info")
    @ResponseBody
    public ResData getAccountInfo(String code) {
        WechatUtils wechatUtils = new WechatUtils();
        JSONObject accessInfo = wechatUtils.getAccessInfo(code);

        if (accessInfo.containsKey("errcode")) {
            return ResData.error("获取openId失败");
        }

        String openId = accessInfo.getString("openid");

        UserModel userModel = userService.findOneByOpenId(openId);

        if (userModel == null) {
            return  ResData.error("该用户未注册");
        }

        return  ResData.ok(userModel);
    }

    @RequestMapping("/register")
    @ResponseBody
    public ResData postRegister(String openId, String nickName) {
        UserModel userModel = new UserModel();
        userModel.setId(UUIDUtils.uuid());
        userModel.setName(nickName);
        userModel.setOpenId(openId);
        userModel.setCreated(new Date());
        userModel.setModified(new Date());
        userModel.setEmail("");
        userModel.setStatus(true);
        userModel.setSerialCode("");

        try {
            userService.addUser(userModel);
        } catch (Exception e) {
            return  ResData.error(e.getMessage());
        }

        return  ResData.ok();
    }
}
