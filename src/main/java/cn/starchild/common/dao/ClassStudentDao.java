package cn.starchild.common.dao;

import cn.starchild.common.model.ClassStudentModel;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface ClassStudentDao extends Mapper<ClassStudentModel> {
    List<Map<String, Object>> selectSimpleList(String teacherId);
}