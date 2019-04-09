package cn.starchild.user.service.impl;

import cn.starchild.common.dao.UserDao;
import cn.starchild.common.model.UserModel;
import cn.starchild.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
        return userDao.hasUserForOpenId(openId);
    }

    @Override
    public int addUser(UserModel userModel) {
        return userDao.insert(userModel);
    }

    @Override
    public UserModel findOneByOpenId(String openId) {
        return userDao.findOneByOpenId(openId);
    }


}
