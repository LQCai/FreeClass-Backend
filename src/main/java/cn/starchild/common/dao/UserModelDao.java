package cn.starchild.common.dao;

import cn.starchild.common.model.UserModel;
import tk.mybatis.mapper.common.Mapper;

public interface UserModelDao extends Mapper<UserModel> {
    int deleteByPrimaryKey(String id);

    int insert(UserModel record);

    int insertSelective(UserModel record);

    UserModel selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserModel record);

    int updateByPrimaryKey(UserModel record);
}