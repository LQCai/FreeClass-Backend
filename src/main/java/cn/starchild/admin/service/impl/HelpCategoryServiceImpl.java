package cn.starchild.admin.service.impl;

import cn.starchild.admin.service.HelpCategoryService;
import cn.starchild.common.dao.HelpCategoryDao;
import cn.starchild.common.model.HelpCategoryModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class HelpCategoryServiceImpl implements HelpCategoryService {
    @Resource
    private HelpCategoryDao helpCategoryDao;


    @Override
    public boolean addHelpCategory(HelpCategoryModel helpCategory) {
        try {
            helpCategoryDao.insert(helpCategory);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
