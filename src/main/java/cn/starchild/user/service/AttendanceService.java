package cn.starchild.user.service;

import java.util.Map;

public interface AttendanceService {
    boolean startDigitalAttendance(String classId, String attendanceId);

    Map<String, Object> getAttendanceInfo(String attendanceId);

    boolean validateHasAttendance(String classId);

    boolean stopStartingAttendanceResult();
}
