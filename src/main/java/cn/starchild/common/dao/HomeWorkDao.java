package cn.starchild.common.dao;

import cn.starchild.common.model.HomeWorkModel;
import tk.mybatis.mapper.common.Mapper;

public interface HomeWorkDao extends Mapper<HomeWorkModel> {

    boolean updateHomework(HomeWorkModel homeWorkModel);

    HomeWorkModel validateHomeworkById(String homeworkId);
}