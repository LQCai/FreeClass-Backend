package cn.starchild.user.controller;

import cn.starchild.common.domain.Code;
import cn.starchild.common.domain.ResData;
import cn.starchild.user.service.ClassService;
import cn.starchild.user.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/user/class")
@RestController
public class ClassController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClassService classService;

    /**
     * 通过id获取用户的授课及上课列表
     *
     * @param id
     * @return list
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResData getList(String id) {
        List<Map<String, Object>> myTeachingClassList = classService.getMyTeachingClassList(id);

        List<Map<String, Object>> myStudyingClassList = classService.getMyStudyingClassList(id);

        Map<String, Object> data = new HashMap<>();
        data.put("myTeachingClassList", myTeachingClassList);
        data.put("myStudyingClassList", myStudyingClassList);

        return ResData.ok(data);
    }

    /**
     * 创建课堂
     * @param dataString:json字符串
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResData postCreate(@RequestBody String dataString) {
        JSONObject classData = JSONObject.parseObject(dataString).getJSONObject("class");

        if (!classData.containsKey("teacherId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "教师id不可为空");
        }
        if (!classData.containsKey("className")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "课堂名不可为空");
        }
        if (!classData.containsKey("peopleMaximum")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "课堂上限人数不可为空");
        }
        if (!classData.containsKey("topping")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "课堂默认置顶不可为空");
        }
        if (!classData.getInteger("topping").equals(1) && !classData.getInteger("topping").equals(2)) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "课堂默认置顶只能是1或2");
        }

        // 判断用户是否存在
        boolean isUser = userService.validateUser(classData.getString("teacherId"));
        if (!isUser) {
            return ResData.error(Code.USER_NOT_EXIST, "用户不存在");
        }

        boolean result = classService.createClass(classData);
        if (!result) {
            return ResData.error(Code.DATABASE_INSERT_FAIL, "创建课程失败");
        }

        return ResData.ok();
    }
}
