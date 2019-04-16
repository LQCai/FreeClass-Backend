package cn.starchild.user.service.impl;

import cn.starchild.common.dao.ClassDao;
import cn.starchild.common.dao.UserDao;
import cn.starchild.user.service.ClassService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ClassServiceImpl implements ClassService {

    @Resource
    private ClassDao classDao;

    @Resource
    private UserDao userDao;

    @Override
    public List getMyTeachingClassList(String id) {
        List list = userDao.selectMyTeachingClassList(id);

        return list;
    }
}
