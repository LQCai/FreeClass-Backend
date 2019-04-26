package cn.starchild.common.dao;

import cn.starchild.common.model.AttendanceRecordModel;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface AttendanceRecordDao extends Mapper<AttendanceRecordModel> {
    AttendanceRecordModel selectByIdAndStudentId(AttendanceRecordModel attendanceRecordModel);

    List<Map<String, Object>> selectCheckList(String attendanceId);
}