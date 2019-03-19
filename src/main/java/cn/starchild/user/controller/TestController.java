package cn.starchild.user.controller;

import cn.starchild.common.domain.ResData;
import cn.starchild.common.http.HttpResponse;
import cn.starchild.common.http.HttpUtils;
import cn.starchild.user.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserService userService;

    @RequestMapping("/testMVC")
    @ResponseBody
    public Map<String, Object> testMVC() {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("name", "哈哈哈");
        return responseMap;
    }

    @RequestMapping("/testDb")
    @ResponseBody
    public List<Map<String, Object>> testDb() {

        try {
            return userService.getAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/test")
    @ResponseBody
    public ResData getAll() {
        return ResData.ok(userService.getAll());
    }

    @RequestMapping("/test2")
    @ResponseBody
    public ResData testError() {
        return ResData.error("error");
    }

    @RequestMapping("/testGet")
    @ResponseBody
    public ResData test() {
        HttpResponse data = HttpUtils.get("http://localhost:8080/freeClass/test/test", null);
        Object jsonData = JSON.parse(data.getResultStr());

        return  ResData.ok(jsonData);
    }
}
