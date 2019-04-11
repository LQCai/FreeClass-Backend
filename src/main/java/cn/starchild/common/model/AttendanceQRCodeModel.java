package cn.starchild.common.model;

import javax.persistence.Table;

@Table(name = "t_attendance_qrcode")
public class AttendanceQRCodeModel {
    private String id;

    private String attendanceId;

    private String attendanceUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId == null ? null : attendanceId.trim();
    }

    public String getAttendanceUrl() {
        return attendanceUrl;
    }

    public void setAttendanceUrl(String attendanceUrl) {
        this.attendanceUrl = attendanceUrl == null ? null : attendanceUrl.trim();
    }
}