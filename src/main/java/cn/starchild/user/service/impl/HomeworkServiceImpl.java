package cn.starchild.user.service.impl;

import cn.starchild.common.dao.HomeWorkDao;
import cn.starchild.common.model.HomeWorkModel;
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

        Map<String, Object> homework = new HashMap<>();

        List<Map<String, Object>> resultList = homeWorkDao.selectHomeworkList(classId);
        for (Map<String, Object> result :
                resultList) {
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
}