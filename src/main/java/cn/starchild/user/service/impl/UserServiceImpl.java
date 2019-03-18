package cn.starchild.user.service.impl;

import cn.starchild.user.dao.UserDao;
import cn.starchild.user.service.UserSrevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserSrevice {

    @Autowired
    private UserDao userDao;

    @Override
    public List<Map<String, Object>> getAllUsers() throws Exception {
        return userDao.getAllUsers();
    }
}
