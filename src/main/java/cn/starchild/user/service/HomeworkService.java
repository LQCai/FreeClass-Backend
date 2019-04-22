package cn.starchild.user.service;

import cn.starchild.common.model.HomeWorkModel;

public interface HomeworkService {
    boolean postHomework(HomeWorkModel homeWorkModel);

    boolean updateHomework(HomeWorkModel homeWorkModel);

    boolean validateHomework(String homeworkId);
}
