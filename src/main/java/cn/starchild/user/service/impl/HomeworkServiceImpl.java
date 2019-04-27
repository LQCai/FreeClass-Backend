package cn.starchild.user.service.impl;

import cn.starchild.common.dao.ClassStudentDao;
import cn.starchild.common.dao.HomeWorkDao;
import cn.starchild.common.dao.HomeworkEmailDao;
import cn.starchild.common.dao.HomeworkSubmitDao;
import cn.starchild.common.model.HomeWorkModel;
import cn.starchild.common.model.HomeworkEmailModal;
import cn.starchild.common.model.HomeworkSubmitModel;
import cn.starchild.common.util.*;
import cn.starchild.user.service.HomeworkService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

@Service
public class HomeworkServiceImpl implements HomeworkService {
    @Resource
    private HomeWorkDao homeWorkDao;

    @Resource
    private ClassStudentDao classStudentDao;

    @Resource
    private HomeworkSubmitDao homeworkSubmitDao;

    @Resource
    private HomeworkEmailDao homeworkEmailDao;

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

        for (Map<String, Object> student :
                studentList) {
            Map<String, Object> studentHomework = new HashMap<>();

            studentHomework.put("studentId", student.get("id"));
            studentHomework.put("studentName", student.get("name"));
            studentHomework.put("studentCode", student.get("serial_code"));
            studentHomework.put("status", 2);

            // 匹配提交记录中与学生id相同的记录，获取已提交作业的学生记录
            for (Map<String, Object> submitRecord :
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
        } else {
            homeworkSubmitInfo.put("status", 1);
            homeworkSubmitInfo.put("content", homeworkSubmit.getContent());
            homeworkSubmitInfo.put("annexUrl", homeworkSubmit.getAnnexUrl());
            homeworkSubmitInfo.put("id", homeworkSubmit.getId());
        }

        return homeworkSubmitInfo;
    }

    @Override
    public void sendEmail() {
        // 获取作业截止时间与当前时间相差不足一天的作业列表
        List<Map<String, Object>> homeworkList = homeWorkDao.selectNearDeadlineHomeworkList();

        List<String> homeworkIdList = new ArrayList<>();

        // 先循环拿到作业id数组
        // 用于查询这些作业中是否有已发送成功的邮件
        // 有就在不用再发了
        for (Map<String, Object> homework :
                homeworkList) {
            homeworkIdList.add(homework.get("id").toString());
        }
        List<Map<String, Object>> homeworkEmailList = homeworkEmailDao.selectByHomeworkList(homeworkIdList);

        Date now = new Date();

        // 循环取出发送邮件记录表，如果status是成功则不需要发送,移除homeworkList元素
        for (int i = homeworkList.size() - 1; i >= 0; i--) {
            for (Map<String, Object> homeworkEmail :
                    homeworkEmailList) {
                String status = homeworkEmail.get("status").toString();

                if (status.equals("1") && homeworkEmail.get("homework_id").equals(homeworkList.get(i).get("id"))) {
                    homeworkList.remove(homeworkList.get(i));
                    break;
                }

                Date deadline = (Date) homeworkList.get(i).get("deadline");

                // 如果截止时间与当前时间间隔超过10分钟则移除数据（没10分钟执行一次此函数）
                if (deadline.after(now) && DateUtils.MinuteDifference(deadline, now) >= 10) {
                    homeworkList.remove(homeworkList.get(i));
                    break;
                }
            }
        }

        EmailUtils emailUtils = new EmailUtils();

        // 最后一次循环发送需要发送的邮件
        for (Map<String, Object> homework :
                homeworkList) {

            HomeworkEmailModal homeworkEmail = new HomeworkEmailModal();
            homeworkEmail.setId(UUIDUtils.uuid());
            homeworkEmail.setHomeworkId(homework.get("id").toString());
            homeworkEmail.setCreated(new Date());
            homeworkEmail.setModified(new Date());

            String annexDirUrl = "";
            try {
                annexDirUrl = FileUtils.homeworkSubmitCommonUrl + homework.get("class_id") + "/" + homework.get("id");
                File annexDir = new File(annexDirUrl);
                new FileZipUtils(new File(annexDirUrl, annexDir.getName() + ".zip")).zipFiles(annexDir);

                // 发送邮件
                File annex = new File(annexDirUrl + "/" + homework.get("id") + ".zip");
                emailUtils.sendWithFile(homework.get("email").toString(), "作业提交通知", "大家的作业都在这啦!", annex);

            } catch (Exception e) {
                logger.error("发送邮件失败：" + e.getMessage());
                homeworkEmail.setStatus((byte) 2);

                homeworkEmailDao.insert(homeworkEmail);
                break;
            }

            homeworkEmail.setStatus((byte) 1);

            // 插入发送成功通知
            homeworkEmailDao.insert(homeworkEmail);
        }
    }
}
