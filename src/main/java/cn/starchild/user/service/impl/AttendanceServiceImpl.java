package cn.starchild.user.service.impl;

import cn.starchild.common.dao.AttendanceDao;
import cn.starchild.common.dao.AttendanceDigtalDao;
import cn.starchild.common.model.AttendanceDigtalModel;
import cn.starchild.common.model.AttendanceModel;
import cn.starchild.common.util.DateUtils;
import cn.starchild.common.util.RandomUtils;
import cn.starchild.common.util.UUIDUtils;
import cn.starchild.user.service.AttendanceService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class AttendanceServiceImpl implements AttendanceService {
    @Resource
    private AttendanceDao attendanceDao;
    @Resource
    private AttendanceDigtalDao attendanceDigtalDao;

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
}
