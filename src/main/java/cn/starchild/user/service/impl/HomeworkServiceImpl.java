package cn.starchild.user.service.impl;

import cn.starchild.common.dao.HomeWorkDao;
import cn.starchild.common.model.HomeWorkModel;
import cn.starchild.user.service.HomeworkService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
