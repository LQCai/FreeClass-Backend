package cn.starchild.common.dao;

import cn.starchild.common.model.HomeworkEmailModal;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface HomeworkEmailDao extends Mapper<HomeworkEmailModal> {

    List<Map<String, Object>> selectByHomeworkList(List<String> homeworkIdList);
}