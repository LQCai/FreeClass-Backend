package cn.starchild.user.controller;

import cn.starchild.common.domain.ResData;
import cn.starchild.common.model.UserModel;
import cn.starchild.common.util.UUIDUtils;
import cn.starchild.common.util.WechatUtils;
import cn.starchild.user.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
        if (code == null) {
            return  ResData.error("code不可为空");
        }

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

    /**
     * 通过openId获取用户信息
     *
     * @param openId
     * @return
     */
    @RequestMapping("/infoForOpenId")
    @ResponseBody
    public ResData getAccountInfoForOpenId(String openId) {
        if (openId == null) {
            return  ResData.error("code不可为空");
        }

        UserModel userModel = userService.findOneByOpenId(openId);

        if (userModel == null) {
            return  ResData.error("该用户未注册");
        }

        return  ResData.ok(userModel);
    }

    /**
     * 用户注册
     *
     * @param jsonString
     * @return
     */
    @RequestMapping("/register")
    @ResponseBody
    public ResData postRegister(@RequestBody String jsonString) {
        JSONObject json = JSONObject.parseObject(jsonString);
        JSONObject userObject = json.getJSONObject("user");

        String openId = userObject.getString("openId");
        String nickName = userObject.getString("nickName");

        if (openId == null) {
            return  ResData.error("openId不可为空");
        }

        if (nickName == null) {
            return  ResData.error("nickName不可为空");
        }

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

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResData UpdateInfo(@RequestBody String userInfo) {
        JSONObject userInfoJson = JSONObject.parseObject(userInfo).getJSONObject("user");

        boolean result = userService.updateInfo(userInfoJson);

        if (!result) {
            return ResData.error("修改失败");
        }
        return ResData.ok("修改成功");
    }
}
