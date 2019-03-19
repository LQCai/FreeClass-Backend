package cn.starchild.user.dao;

import cn.starchild.common.model.UserModel;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface UserDao extends Mapper<UserModel> {
    List<Map<String, Object>> getAllUsers();
}
