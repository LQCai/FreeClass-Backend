package cn.starchild.common.dao;

import cn.starchild.common.model.ClassModel;
import tk.mybatis.mapper.common.Mapper;

public interface ClassModelDao extends Mapper<ClassModel> {
    int deleteByPrimaryKey(String id);

    int insert(ClassModel record);

    int insertSelective(ClassModel record);

    ClassModel selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ClassModel record);

    int updateByPrimaryKey(ClassModel record);
}