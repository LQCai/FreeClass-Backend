package cn.starchild.user.controller;

import cn.starchild.user.service.UserSrevice;
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
    private UserSrevice userSrevice;

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
            return userSrevice.getAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
