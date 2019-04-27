package cn.starchild.user.service.impl;

import cn.starchild.common.dao.ClassStudentDao;
import cn.starchild.common.dao.HomeWorkDao;
import cn.starchild.common.dao.HomeworkSubmitDao;
import cn.starchild.common.model.HomeWorkModel;
import cn.starchild.common.model.HomeworkSubmitModel;
import cn.starchild.user.service.HomeworkService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HomeworkServiceImpl implements HomeworkService {
    @Resource
    private HomeWorkDao homeWorkDao;

    @Resource
    private ClassStudentDao classStudentDao;

    @Resource
    private HomeworkSubmitDao homeworkSubmitDao;

    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public boolean postHomework(HomeWorkModel homeWorkModel) {
        try {
            homeWorkDao.insert(homeWorkModel);
        } catch (Exception e) {
            logger.error("插入作业失败：" + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean updateHomework(HomeWorkModel homeWorkModel) {
        boolean result = homeWorkDao.updateHomework(homeWorkModel);

        if (!result) {
            return false;
        }
        return true;
    }

    @Override
    public boolean validateHomework(String homeworkId) {
        HomeWorkModel result = homeWorkDao.validateHomeworkById(homeworkId);
        if (result == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteHomework(String id) {
        try {
            homeWorkDao.deleteHomework(id);
        } catch (Exception e) {
            logger.error("删除作业失败:" + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public List<Map<String, Object>> getHomeworkList(String classId) {
        List<Map<String, Object>> homeworkList = new ArrayList<>();

        List<Map<String, Object>> resultList = homeWorkDao.selectHomeworkList(classId);
        for (Map<String, Object> result :
                resultList) {

            Map<String, Object> homework = new HashMap<>();
            homework.put("id", result.get("id"));
            homework.put("name", result.get("name"));
            homework.put("introduction", result.get("introduction"));
            homework.put("deadline", result.get("deadline"));
            homework.put("annexUrl", result.get("annex_url"));
            homework.put("created", result.get("created"));

            homeworkList.add(homework);
        }

        return homeworkList;
    }

    @Override
    public List<Map<String, Object>> getStudentHomeworkList(String classId, String homeworkId) {
        List<Map<String, Object>> studentList = classStudentDao.selectStudentList(classId);
        List<Map<String, Object>> submitHomeworkList = homeworkSubmitDao.selectSubmitList(homeworkId);

        List<Map<String, Object>> studentHomeworkList = new ArrayList<>();

        for (Map<String, Object> student:
             studentList) {
            Map<String, Object> studentHomework = new HashMap<>();

            studentHomework.put("studentId", student.get("id"));
            studentHomework.put("studentName", student.get("name"));
            studentHomework.put("studentCode", student.get("serial_code"));
            studentHomework.put("status", 2);

            // 匹配提交记录中与学生id相同的记录，获取已提交作业的学生记录
            for (Map<String, Object> submitRecord:
                 submitHomeworkList) {
                if (submitRecord.get("student_id").equals(student.get("id"))) {
                    studentHomework.put("status", 1);
                    studentHomework.put("content", submitRecord.get("content"));
                    studentHomework.put("annexUrl", submitRecord.get("annex_url"));
                    studentHomework.put("created", submitRecord.get("created"));
                }
            }

            studentHomeworkList.add(studentHomework);
        }

        return studentHomeworkList;
    }

    @Override
    public HomeWorkModel getHomeworkInfo(String homeworkId) {
        return homeWorkDao.selectHomework(homeworkId);
    }

    @Override
    public boolean submitHomework(HomeworkSubmitModel homeworkSubmitModel) {
        try {
            homeworkSubmitDao.insert(homeworkSubmitModel);
        } catch (Exception e) {
            logger.error("提交作业失败:" + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean validateSubmitted(String studentId, String homeworkId) {
        HomeworkSubmitModel homeworkSubmitModel = new HomeworkSubmitModel();
        homeworkSubmitModel.setHomeworkId(homeworkId);
        homeworkSubmitModel.setStudentId(studentId);

        HomeworkSubmitModel homeworkSubmit = homeworkSubmitDao.selectSubmitRecord(homeworkSubmitModel);
        if (homeworkSubmit == null) {
            return false;
        }
        return true;
    }

    @Override
    public Map<String, Object> getSubmittedInfo(String studentId, String homeworkId) {
        HomeworkSubmitModel homeworkSubmitModel = new HomeworkSubmitModel();
        homeworkSubmitModel.setHomeworkId(homeworkId);
        homeworkSubmitModel.setStudentId(studentId);

        HomeworkSubmitModel homeworkSubmit = homeworkSubmitDao.selectSubmitRecord(homeworkSubmitModel);

        Map<String, Object> homeworkSubmitInfo = new HashMap<>();

        if (homeworkSubmit == null) {
            homeworkSubmitInfo.put("status", 2);
        }else {
            homeworkSubmitInfo.put("status", 1);
            homeworkSubmitInfo.put("content", homeworkSubmit.getContent());
            homeworkSubmitInfo.put("annexUrl", homeworkSubmit.getAnnexUrl());
            homeworkSubmitInfo.put("id", homeworkSubmit.getId());
        }

        return  homeworkSubmitInfo;
    }

    @Override
    public boolean sendEmail() {

        return false;
    }
}
