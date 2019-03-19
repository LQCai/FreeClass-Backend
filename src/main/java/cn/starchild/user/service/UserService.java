package cn.starchild.user.service;


import cn.starchild.common.model.UserModel;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<Map<String,Object>> getAllUsers() throws Exception;

    List<UserModel> getAll();
}
