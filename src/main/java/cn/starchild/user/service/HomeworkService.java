package cn.starchild.user.service;

import cn.starchild.common.model.HomeWorkModel;

import java.util.List;
import java.util.Map;

public interface HomeworkService {
    boolean postHomework(HomeWorkModel homeWorkModel);

    boolean updateHomework(HomeWorkModel homeWorkModel);

    boolean validateHomework(String homeworkId);

    boolean deleteHomework(String id);

    List<Map<String, Object>> getHomeworkList(String classId);
}
