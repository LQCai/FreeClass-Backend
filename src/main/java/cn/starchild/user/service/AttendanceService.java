package cn.starchild.user.service;

import java.util.List;
import java.util.Map;

public interface AttendanceService {
    boolean startDigitalAttendance(String classId, String attendanceId);

    Map<String, Object> getAttendanceInfo(String attendanceId);

    boolean validateHasAttendance(String classId);

    boolean stopStartingAttendanceResult();

    boolean validateIsStarting(String attendanceId);

    boolean stopStartingAttendance(String attendanceId);

    boolean validateHasChecked(String attendanceId, String studentId);

    boolean checkIn(String attendanceId, String studentId);

    List<Map<String, Object>> getAttendanceList(String classId);

    List<Map<String, Object>> getCheckList(String attendanceId, String classId);

    Map<String, Object> getStartingAttendanceInfo(String attendanceId, String classId);
}
