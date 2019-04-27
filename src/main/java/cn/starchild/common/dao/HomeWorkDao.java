package cn.starchild.common.dao;

import cn.starchild.common.model.HomeWorkModel;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface HomeWorkDao extends Mapper<HomeWorkModel> {

    boolean updateHomework(HomeWorkModel homeWorkModel);

    HomeWorkModel validateHomeworkById(String homeworkId);

    boolean deleteHomework(String id);

    List<Map<String, Object>> selectHomeworkList(String classId);

    HomeWorkModel selectHomework(String homeworkId);

    List<Map<String, Object>> selectNearDeadlineHomeworkList();
}