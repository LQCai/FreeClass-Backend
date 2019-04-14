package cn.starchild.common.dao;

import cn.starchild.common.model.UserModel;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface UserDao extends Mapper<UserModel> {
    List<Map<String, Object>> getAllUsers();

    boolean hasUserForOpenId(String openId);

    UserModel findOneByOpenId(String openId);

    boolean updateByOpenId(UserModel user);
}