package cn.starchild.user.controller;

import cn.starchild.common.domain.Code;
import cn.starchild.common.domain.ResData;
import cn.starchild.common.model.ClassModel;
import cn.starchild.common.model.ClassStudentModel;
import cn.starchild.user.service.ClassService;
import cn.starchild.user.service.ClassStudentService;
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

    @Autowired
    private ClassStudentService classStudentService;

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
            return ResData.error(Code.DATA_NOT_FOUND, "用户不存在");
        }

        boolean result = classService.createClass(classData);
        if (!result) {
            return ResData.error(Code.DATABASE_INSERT_FAIL, "创建课程失败");
        }

        return ResData.ok();
    }

    /**
     * 修改课堂信息
     * @param jsonParams:json字符串
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResData putUpdate(@RequestBody String jsonParams) {
        JSONObject classData = JSONObject.parseObject(jsonParams).getJSONObject("class");

        if (!classData.containsKey("id")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "课堂id不可为空");
        }
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

        // 判断课堂是否存在
        boolean isClass = classService.validateClass(classData.getString("id"));
        if (!isClass) {
            return ResData.error(Code.DATA_NOT_FOUND, "找不到该课堂");
        }

        // 判断用户是否存在
        boolean isUser = userService.validateUser(classData.getString("teacherId"));
        if (!isUser) {
            return ResData.error(Code.DATA_NOT_FOUND, "用户不存在");
        }

        // 判断修改课堂是否成功
        boolean result = classService.updateClass(classData);
        if (!result) {
            return ResData.error(Code.DATABASE_UPDATE_FAIL, "修改课程失败");
        }

        return ResData.ok();
    }

    /**
     * 加入课堂
     * @param jsonParams
     * @return
     */
    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public ResData postJoin(@RequestBody String jsonParams) {
        JSONObject joinData = JSONObject.parseObject(jsonParams).getJSONObject("joinData");

        if (!joinData.containsKey("userId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "用户id不可为空");
        }
        if (!joinData.containsKey("code")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "课堂邀请码不可为空");
        }

        String userId = joinData.getString("userId");
        String code = joinData.getString("code");

        // 判断用户是否存在
        boolean isUser = userService.validateUser(userId);
        if (!isUser) {
            return ResData.error(Code.DATA_NOT_FOUND, "用户不存在");
        }

        // 判断课堂是否存在
        ClassModel classInfo = classService.getClassByCode(code);
        if (classInfo == null) {
            return ResData.error(Code.DATA_NOT_FOUND, "找不到该课堂");
        }

        // 判断是否已经是该课堂教师
        if (userId.equals(classInfo.getTeacherId())) {
            return ResData.error(Code.IS_TEACHER, "您已是该课堂教师");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("code", code);
        data.put("classId", classInfo.getId());

        ClassStudentModel classStudent = new ClassStudentModel();
        classStudent.setStudentId(userId);
        classStudent.setClassId(classInfo.getId());

        boolean isJoined = classStudentService.validateJoined(classStudent);
        if (isJoined) {
            return ResData.error(Code.CLASS_JOINED, "您已加入该课堂");
        }

        boolean result = classStudentService.joinClass(classStudent);
        if (!result) {
            return ResData.error(Code.DATABASE_INSERT_FAIL, "加入课堂失败");
        }

        return ResData.ok();
    }
}
