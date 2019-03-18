package cn.starchild.common.service.impl;

import cn.starchild.common.dao.CommonDao;
import cn.starchild.common.service.CommonService;
import cn.starchild.common.utils.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service("commonService")
public class CommonServiceImpl implements CommonService {
    @Resource(name = "commonDao")
    protected CommonDao commonDao;


    @Override
    public <T extends Serializable> int save(T pojo) {
        return commonDao.save(pojo);
    }

    @Override
    public <T extends Serializable> int deleteById(Class<T> clazz,
                                                   Serializable id) {
        return commonDao.deleteById(clazz, id);
    }

    @Override
    public <T extends Serializable> T getById(Class<T> clazz, Serializable id) {
        return commonDao.getById(clazz, id);
    }

    @Override
    public <T extends Serializable> List<T> listAll(Class<T> clazz) {
        return commonDao.listAll(clazz);
    }


    @Override
    public <T extends Serializable> int pageCount(Class<T> clazz,
                                                  String[] attrs, Object[] values) {
        return commonDao.pageCount(clazz, attrs, values);
    }

    @Override
    public <T extends Serializable> Page<T> pageSelect(Class<T> clazz,
                                                       Page<T> p, String[] attrs, Object[] values) {
        return commonDao.pageSelect(clazz, p, attrs, values);
    }

    @Override
    public <T extends Serializable> int countAll(Class<T> clazz) {
        return commonDao.countAll(clazz);
    }

    @Override
    public List<Map<String, Object>> selectMap(String statement,
                                               Map<String, Object> paraMap) {
        return commonDao.selectMap(statement, paraMap);
    }
}
