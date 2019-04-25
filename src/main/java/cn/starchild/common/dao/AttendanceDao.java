package cn.starchild.common.dao;

import cn.starchild.common.model.AttendanceModel;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface AttendanceDao extends Mapper<AttendanceModel> {
    Map<String, Object> selectAttendanceInfo(String attendanceId);

    List<Map<String, Object>> selectNormalAttendanceList(String classId);

    List<Map<String, Object>> selectStartingAttendanceList();

    void stopAttendance(AttendanceModel stopAttendanceModal);

    AttendanceModel selectStartingAttendance(String id);

    List<Map<String, Object>> selectAttendanceList(String classId);
}