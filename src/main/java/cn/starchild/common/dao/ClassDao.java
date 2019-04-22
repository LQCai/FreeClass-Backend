package cn.starchild.common.dao;

import cn.starchild.common.model.ClassModel;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface ClassDao extends Mapper<ClassModel> {

    List<Map<String, Object>> selectMyTeachingClassList(String id);

    List<Map<String, Object>> selectMyStudyingClassList(String id);

    Integer selectCountForId(String classId);

    List<String> selectClassCodeList();

    boolean updateById(ClassModel classModel);

    ClassModel validateClass(String id);

    ClassModel validateClassByCode(String code);

    ClassModel validateIsClass(ClassModel classModel);

    boolean deleteById(String id);

    ClassModel validateClassForTeacher(ClassModel classModel);
}