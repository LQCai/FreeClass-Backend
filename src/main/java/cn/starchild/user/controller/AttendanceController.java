package cn.starchild.user.controller;

import cn.starchild.common.domain.Code;
import cn.starchild.common.domain.ResData;
import cn.starchild.common.model.ClassStudentModel;
import cn.starchild.common.util.UUIDUtils;
import cn.starchild.user.service.AttendanceService;
import cn.starchild.user.service.ClassService;
import cn.starchild.user.service.ClassStudentService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/attendance")
public class AttendanceController {
    @Autowired
    private ClassService classService;
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private ClassStudentService classStudentService;

    /**
     * 发起数字考勤
     *
     * @return
     */
    @RequestMapping(value = "/startDigital", method = RequestMethod.POST)
    public ResData startDigitalAttendance(@RequestBody String jsonParams) {
        JSONObject data = JSONObject.parseObject(jsonParams).getJSONObject("startData");

        if (!data.containsKey("teacherId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "教师id不可为空");
        }
        if (!data.containsKey("classId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "课堂id不可为空");
        }

        String classId = data.getString("classId");
        String teacherId = data.getString("teacherId");

        // 验证该课堂是否存在，且是不是该用户创建的
        boolean isClass = classService.validateClassForTeacher(classId, teacherId);
        if (!isClass) {
            return ResData.error(Code.DATA_NOT_FOUND, "找不到该教师的课堂");
        }

        // 检验是否有考勤正在进行
        boolean hasAttendance = attendanceService.validateHasAttendance(classId);
        if (hasAttendance) {
            return ResData.error(Code.ATTENDANCE_STARTING, "考勤正在进行");
        }

        // 预生成考勤id
        String attendanceId = UUIDUtils.uuid();

        boolean result = attendanceService.startDigitalAttendance(classId, attendanceId);
        if (!result) {
            return ResData.error(Code.DATABASE_INSERT_FAIL, "发起考勤失败");
        }

        Map<String, Object> attendanceInfo = attendanceService.getAttendanceInfo(attendanceId);

        return ResData.ok(attendanceInfo);
    }

    /**
     * 停止考勤
     *
     * @return
     */
    @RequestMapping(value = "/stop", method = RequestMethod.PUT)
    public ResData stopAttendance(@RequestBody String jsonParams) {
        JSONObject data = JSONObject.parseObject(jsonParams).getJSONObject("stopData");

        if (!data.containsKey("teacherId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "教师id不可为空");
        }
        if (!data.containsKey("classId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "课堂id不可为空");
        }
        if (!data.containsKey("attendanceId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "考勤id不可为空");
        }

        String teacherId = data.getString("teacherId");
        String classId = data.getString("classId");
        String attendanceId = data.getString("attendanceId");

        // 验证该课堂是否存在，且是不是该用户创建的
        boolean isClass = classService.validateClassForTeacher(classId, teacherId);
        if (!isClass) {
            return ResData.error(Code.DATA_NOT_FOUND, "该作业不是该教师所创建");
        }

        // 验证考勤是否已停止
        boolean isStartingAttendance = attendanceService.validateIsStarting(attendanceId);
        if (!isStartingAttendance) {
            return ResData.error(Code.DATA_NOT_FOUND, "考勤不存在或已停止");
        }

        boolean result = attendanceService.stopStartingAttendance(attendanceId);
        if (!result) {
            return ResData.error(Code.DATABASE_UPDATE_FAIL, "停止考勤失败");
        }

        return ResData.ok();
    }

    /**
     * 放弃考勤
     *
     * @return
     */
    @RequestMapping(value = "/drop", method = RequestMethod.DELETE)
    public ResData dropAttendance(@RequestBody String jsonParams) {
        JSONObject data = JSONObject.parseObject(jsonParams).getJSONObject("dropData");

        if (!data.containsKey("teacherId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "教师id不可为空");
        }
        if (!data.containsKey("classId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "课堂id不可为空");
        }
        if (!data.containsKey("attendanceId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "考勤id不可为空");
        }

        String teacherId = data.getString("teacherId");
        String classId = data.getString("classId");
        String attendanceId = data.getString("attendanceId");

        // 验证该课堂是否存在，且是不是该用户创建的
        boolean isClass = classService.validateClassForTeacher(classId, teacherId);
        if (!isClass) {
            return ResData.error(Code.DATA_NOT_FOUND, "该作业不是该教师所创建");
        }

        // 验证考勤是否已停止
        boolean isStartingAttendance = attendanceService.validateIsStarting(attendanceId);
        if (!isStartingAttendance) {
            return ResData.error(Code.DATA_NOT_FOUND, "考勤不存在或已停止");
        }

        boolean result = attendanceService.stopStartingAttendance(attendanceId);
        if (!result) {
            return ResData.error(Code.DATABASE_UPDATE_FAIL, "停止考勤失败");
        }

        return ResData.ok();
    }

    /**
     * 考勤签到
     *
     * @return
     */
    @RequestMapping(value = "/checkIn", method = RequestMethod.POST)
    public ResData checkIn(@RequestBody String jsonParams) {
        JSONObject data = JSONObject.parseObject(jsonParams).getJSONObject("checkData");

        if (!data.containsKey("studentId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "学生id不可为空");
        }
        if (!data.containsKey("classId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "课堂id不可为空");
        }
        if (!data.containsKey("attendanceId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "考勤id不可为空");
        }

        String studentId = data.getString("studentId");
        String classId = data.getString("classId");
        String attendanceId = data.getString("attendanceId");

        // 判断该学生是否加入该课堂
        ClassStudentModel classStudent = new ClassStudentModel();
        classStudent.setClassId(classId);
        classStudent.setStudentId(studentId);
        boolean isJoined = classStudentService.validateJoined(classStudent);
        if (!isJoined) {
            return ResData.error(Code.CLASS_NOT_JOINED, "未加入该课堂");
        }

        // 验证是否已签到
        boolean checked = attendanceService.validateHasChecked(attendanceId, studentId);
        if (checked) {
            return ResData.error(Code.CHECKED, "您已签到");
        }

        boolean result = attendanceService.checkIn(attendanceId, studentId);
        if (!result) {
            return ResData.error(Code.DATABASE_INSERT_FAIL, "签到失败");
        }

        return ResData.ok();
    }

    /**
     * 获取指定课堂的考勤列表
     * @param classId
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResData getAttendanceList(String classId) {
        List<Map<String, Object>> attendanceList = attendanceService.getAttendanceList(classId);
        return ResData.ok(attendanceList);
    }

    /**
     * 获取指定考勤签到记录
     * @param attendanceId
     * @param classId
     * @return
     */
    @RequestMapping(value = "/checkList", method = RequestMethod.GET)
    public ResData getCheckList(String attendanceId,
                                String classId) {
        List<Map<String, Object>> checkList = attendanceService.getCheckList(attendanceId, classId);
        return ResData.ok(checkList);
    }

    /**
     * 获取实时考勤信息
     * @param attendanceId
     * @param classId
     * @return
     */
    @RequestMapping(value = "/startingInfo", method = RequestMethod.GET)
    public ResData getStartingInfo(String attendanceId,
                                   String classId) {
        Map<String, Object> startingInfo = attendanceService.getStartingAttendanceInfo(attendanceId, classId);
        if (startingInfo == null) {
            return ResData.error(Code.DATA_NOT_FOUND, "找不到该考勤信息");
        }

        return ResData.ok(startingInfo);
    }
}
