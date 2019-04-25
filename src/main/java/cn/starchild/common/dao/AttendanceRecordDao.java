package cn.starchild.common.dao;

import cn.starchild.common.model.AttendanceRecordModel;
import tk.mybatis.mapper.common.Mapper;

public interface AttendanceRecordDao extends Mapper<AttendanceRecordModel> {
    AttendanceRecordModel selectByIdAndStudentId(AttendanceRecordModel attendanceRecordModel);
}