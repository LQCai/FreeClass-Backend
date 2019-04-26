package cn.starchild.user.service.impl;

import cn.starchild.common.dao.AttendanceDao;
import cn.starchild.common.dao.AttendanceDigtalDao;
import cn.starchild.common.dao.AttendanceRecordDao;
import cn.starchild.common.dao.ClassStudentDao;
import cn.starchild.common.model.AttendanceDigtalModel;
import cn.starchild.common.model.AttendanceModel;
import cn.starchild.common.model.AttendanceRecordModel;
import cn.starchild.common.util.DateUtils;
import cn.starchild.common.util.RandomUtils;
import cn.starchild.common.util.UUIDUtils;
import cn.starchild.user.service.AttendanceService;
import cn.starchild.user.service.ClassStudentService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


@Service
public class AttendanceServiceImpl implements AttendanceService {
    @Resource
    private AttendanceDao attendanceDao;
    @Resource
    private AttendanceDigtalDao attendanceDigtalDao;
    @Resource
    private AttendanceRecordDao attendanceRecordDao;
    @Resource
    private ClassStudentDao classStudentDao;

    private Logger logger = Logger.getLogger(this.getClass());


    @Transactional
    @Override
    public boolean startDigitalAttendance(String classId,
                                          String attendanceId) {
        AttendanceModel attendance = new AttendanceModel();

        String code = RandomUtils.getRandomNum(4);

        attendance.setId(attendanceId);
        attendance.setName("数字考勤");
        attendance.setClassId(classId);
        attendance.setType((byte) 1);
        attendance.setStatus((byte) 1);
        attendance.setCreated(new Date());
        attendance.setModified(new Date());

        AttendanceDigtalModel attendanceDigtal = new AttendanceDigtalModel();
        attendanceDigtal.setId(UUIDUtils.uuid());
        attendanceDigtal.setAttendanceId(attendanceId);
        attendanceDigtal.setCode(code);

        try {
            attendanceDao.insert(attendance);
            attendanceDigtalDao.insert(attendanceDigtal);
        } catch (Exception e) {
            logger.error("发布考勤失败:" + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public Map<String, Object> getAttendanceInfo(String attendanceId) {

        return attendanceDao.selectAttendanceInfo(attendanceId);
    }

    @Override
    public boolean validateHasAttendance(String classId) {
        List<Map<String, Object>> attendanceList = attendanceDao.selectNormalAttendanceList(classId);
        if (attendanceList.size() > 0) {
            return true;
        }

        return false;
    }

    @Transactional
    @Override
    public boolean stopStartingAttendanceResult() {
        List<Map<String, Object>> startingAttendanceList = attendanceDao.selectStartingAttendanceList();

        // 设置为考勤截至状态
        AttendanceModel stopAttendanceModal = new AttendanceModel();
        stopAttendanceModal.setModified(new Date());
        stopAttendanceModal.setStatus((byte) 3);

        try {
            // 轮询删除, 如果最后修改时间与现在相差10分钟即停止该考勤
            for (Map<String, Object> startingAttendance :
                    startingAttendanceList) {

                Date modified = (Date) startingAttendance.get("modified");
                Date now = new Date();

                String status = startingAttendance.get("status").toString();

                if (status.equals("1") && DateUtils.MinuteDifference(modified, now) >= 10) {
                    stopAttendanceModal.setId((String) startingAttendance.get("id"));

                    System.out.println("success");
                    attendanceDao.stopAttendance(stopAttendanceModal);
                }
            }
        } catch (Exception e) {
            logger.error("停止考勤失败:" + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean validateIsStarting(String id) {
        AttendanceModel attendance = attendanceDao.selectStartingAttendance(id);
        if (attendance == null) {
            return false;
        }

        return true;
    }

    @Override
    public boolean stopStartingAttendance(String attendanceId) {
        AttendanceModel stopAttendanceModal = new AttendanceModel();
        stopAttendanceModal.setModified(new Date());
        stopAttendanceModal.setStatus((byte) 3);
        stopAttendanceModal.setId(attendanceId);

        try {
            attendanceDao.stopAttendance(stopAttendanceModal);
        } catch (Exception e) {
            logger.error("停止考勤失败:" + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean validateHasChecked(String attendanceId, String studentId) {
        AttendanceRecordModel attendanceRecordModel = new AttendanceRecordModel();
        attendanceRecordModel.setAttendanceId(attendanceId);
        attendanceRecordModel.setStudentId(studentId);

        AttendanceRecordModel attendanceRecord = attendanceRecordDao.selectByIdAndStudentId(attendanceRecordModel);
        if (attendanceRecord == null) {
            return false;
        }

        return true;
    }

    @Override
    public boolean checkIn(String attendanceId, String studentId) {
        AttendanceRecordModel attendanceRecordModel = new AttendanceRecordModel();

        attendanceRecordModel.setId(UUIDUtils.uuid());
        attendanceRecordModel.setCreated(new Date());
        attendanceRecordModel.setStatus((byte) 1);
        attendanceRecordModel.setAttendanceId(attendanceId);
        attendanceRecordModel.setStudentId(studentId);

        try {
            attendanceRecordDao.insert(attendanceRecordModel);
        } catch (Exception e) {
            logger.error("签到失败：" + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public List<Map<String, Object>> getAttendanceList(String classId) {
        return attendanceDao.selectAttendanceList(classId);
    }

    @Override
    public List<Map<String, Object>> getCheckList(String attendanceId, String classId) {
        List<Map<String, Object>> studentList = classStudentDao.selectStudentList(classId);
        List<Map<String, Object>> checkList = attendanceRecordDao.selectCheckList(attendanceId);

        List<Map<String, Object>> resultList = new ArrayList<>();

        // 遍历该课堂学生列表,再遍历签到列表,匹配studentId相同的记录
        for (Map<String, Object> student :
                studentList) {

            Map<String, Object> result = new HashMap<>();
            result.put("studentId", student.get("id"));
            result.put("studentName", student.get("name"));
            result.put("studentCode", student.get("serial_code"));

            for (Map<String, Object> check :
                    checkList) {
                if (student.get("id").equals(check.get("student_id"))) {
                    result.put("status", check.get("status"));
                    result.put("created", check.get("created"));
                }
            }

            resultList.add(result);
        }

        return resultList;
    }

    @Override
    public Map<String, Object> getStartingAttendanceInfo(String attendanceId, String classId) {
        // 学生列表
        List<Map<String, Object>> studentList = classStudentDao.selectStudentList(classId);
        // 学生考勤记录
        List<Map<String, Object>> checkList = attendanceRecordDao.selectCheckList(attendanceId);
        // 考勤信息
        Map<String, Object> attendance = attendanceDao.selectAttendanceInfo(attendanceId);

        Map<String, Object> startingAttendanceInfo = new HashMap<>();

        startingAttendanceInfo.put("name", attendance.get("name"));
        startingAttendanceInfo.put("code", attendance.get("code"));
        startingAttendanceInfo.put("status", attendance.get("status"));
        startingAttendanceInfo.put("studentCount", studentList.size());
        startingAttendanceInfo.put("checkCount", checkList.size());

        return startingAttendanceInfo;
    }
}
