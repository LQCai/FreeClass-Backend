package cn.starchild.user.service;

import cn.starchild.common.model.HomeWorkModel;
import cn.starchild.common.model.HomeworkSubmitModel;

import java.util.List;
import java.util.Map;

public interface HomeworkService {
    boolean postHomework(HomeWorkModel homeWorkModel);

    boolean updateHomework(HomeWorkModel homeWorkModel);

    boolean validateHomework(String homeworkId);

    boolean deleteHomework(String id);

    List<Map<String, Object>> getHomeworkList(String classId);

    List<Map<String, Object>> getStudentHomeworkList(String classId);

    HomeWorkModel getHomeworkInfo(String homeworkId);

    boolean submitHomework(HomeworkSubmitModel homeworkSubmitModel);

    boolean validateSubmitted(String studentId, String homeworkId);
}
