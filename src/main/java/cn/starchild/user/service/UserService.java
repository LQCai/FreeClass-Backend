package cn.starchild.user.service;


import cn.starchild.common.model.UserModel;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<Map<String,Object>> getAllUsers() throws Exception;

    List<UserModel> getAll();

    boolean hasUserForOpenId(String openId);

    int addUser(UserModel userModel);

    UserModel findOneByOpenId(String openId);

    boolean updateInfo(JSONObject userInfoJson);

    boolean validateRegister(String openId);

    boolean validateUser(String teacherId);

    List<Map<String, Object>> getUserList();
}
