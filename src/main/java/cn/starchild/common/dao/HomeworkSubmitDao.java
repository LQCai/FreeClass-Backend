package cn.starchild.common.dao;

import cn.starchild.common.model.HomeworkSubmitModel;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface HomeworkSubmitDao extends Mapper<HomeworkSubmitModel> {
    HomeworkSubmitModel selectSubmitRecord(HomeworkSubmitModel homeworkSubmitModel);

    List<Map<String, Object>> selectSubmitList(String homeworkId);
}