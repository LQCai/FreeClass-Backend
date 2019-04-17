package cn.starchild.user.service.impl;

import cn.starchild.common.dao.UserDao;
import cn.starchild.common.model.UserModel;
import cn.starchild.user.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public List<Map<String, Object>> getAllUsers() throws Exception {
        return userDao.getAllUsers();
    }

    @Override
    public List<UserModel> getAll() {
        return userDao.selectAll();
    }

    @Override
    public boolean hasUserForOpenId(String openId) {
        String id = userDao.hasUserForOpenId(openId);

        if (id == null) {
            return false;
        }

        return true;
    }

    @Override
    public int addUser(UserModel userModel) {
        return userDao.insert(userModel);
    }

    @Override
    public UserModel findOneByOpenId(String openId) {
        return userDao.findOneByOpenId(openId);
    }

    @Override
    public boolean updateInfo(JSONObject userInfoJson) {
        UserModel user = new UserModel();

        if (!userInfoJson.containsKey("openId")) {
            return false;
        }

        user.setOpenId(userInfoJson.getString("openId"));

        if (userInfoJson.containsKey("name")) {
            String name = userInfoJson.getString("name");
            user.setName(name);
        }

        if (userInfoJson.containsKey("email")) {
            String email = userInfoJson.getString("email");
            user.setEmail(email);
        }

        if (userInfoJson.containsKey("serialCode")) {
            String serialCode = userInfoJson.getString("serialCode");
            user.setSerialCode(serialCode);
        }

        user.setModified(new Date());

        return userDao.updateByOpenId(user);
    }

    @Override
    public boolean validateRegister(String openId) {
        String id = userDao.hasUserForOpenId(openId);

        if (id == null) {
            return false;
        }

        return true;
    }

    @Override
    public boolean validateUser(String id) {
        if (userDao.validateUserById(id) == null) {
            return false;
        }

        return true;
    }


}
