package cn.starchild.user.controller;

import cn.starchild.common.domain.Code;
import cn.starchild.common.domain.ResData;
import cn.starchild.common.util.UUIDUtils;
import cn.starchild.user.service.AttendanceService;
import cn.starchild.user.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user/attendance")
public class AttendanceController {
    @Autowired
    private ClassService classService;
    @Autowired
    private AttendanceService attendanceService;

    /**
     * 发起数字考勤
     *
     * @param classId
     * @param teacherId
     * @return
     */
    @RequestMapping(value = "/startDigital", method = RequestMethod.POST)
    public ResData startDigitalAttendance(String classId,
                                          String teacherId) {
        if (teacherId == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "teacherId为空");
        }

        if (classId == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "classId为空");
        }

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
        return null;
    }

    /**
     * 放弃考勤
     *
     * @return
     */
    @RequestMapping(value = "/drop", method = RequestMethod.DELETE)
    public ResData dropAttendance(@RequestBody String jsonParams) {
        return null;
    }

    /**
     * 考勤签到
     *
     * @param studentId
     * @param attendanceId
     * @return
     */
    @RequestMapping(value = "/checkIn", method = RequestMethod.POST)
    public ResData checkIn(String studentId,
                           String attendanceId) {
        return null;
    }
}
