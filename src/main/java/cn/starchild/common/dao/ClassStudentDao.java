package cn.starchild.common.dao;

import cn.starchild.common.model.ClassStudentModel;
import cn.starchild.common.model.UserModel;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface ClassStudentDao extends Mapper<ClassStudentModel> {
    List<Map<String, Object>> selectSimpleList(String teacherId);

    List<Map<String, Object>> validateJoined(ClassStudentModel classStudent);

    boolean joinClass(ClassStudentModel data);

    boolean deleteByClassId(String classId);

    boolean deleteForClassAndStudent(ClassStudentModel classStudent);

    List<Map<String, Object>> selectStudentList(String classId);

    UserModel getStudentInfo(ClassStudentModel classStudentModel);
}