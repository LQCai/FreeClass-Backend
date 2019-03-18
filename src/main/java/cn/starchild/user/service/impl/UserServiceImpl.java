package cn.starchild.user.service.impl;

import cn.starchild.common.model.UserModel;
import cn.starchild.user.dao.UserDao;
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


}
