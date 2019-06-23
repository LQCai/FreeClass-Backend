package cn.starchild.admin.service.impl;

import cn.starchild.admin.service.HelpInfoService;
import cn.starchild.common.dao.HelpInfoDao;
import cn.starchild.common.domain.Code;
import cn.starchild.common.domain.ResData;
import cn.starchild.common.model.HelpInfoModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HelpInfoServiceImpl implements HelpInfoService {
    @Resource
    private HelpInfoDao helpInfoDao;

    @Override
    public boolean addHelpInfo(HelpInfoModel helpInfo) {
        try {
            helpInfoDao.insert(helpInfo);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean validateHelp(String id) {
        HelpInfoModel helpInfoModel = helpInfoDao.selectById(id);
        if (helpInfoModel == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean updateHelpInfo(HelpInfoModel helpInfo) {
        try {
            helpInfoDao.updateHelpInfo(helpInfo);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteHelpInfo(HelpInfoModel helpInfo) {
        try {
            helpInfoDao.deleteHelpInfo(helpInfo);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<Map<String, Object>> getHelpList() {
        List<Map<String, Object>> helpList = new ArrayList<>();

        List<Map<String, Object>> resultList = helpInfoDao.getHelpList();
        for (Map<String, Object> resultItem :
                resultList) {
            Map<String, Object> help = new HashMap<>();

            help.put("id", resultItem.get("id"));
            help.put("title", resultItem.get("title"));
            help.put("content", resultItem.get("content"));
            help.put("createTime", resultItem.get("created"));
            help.put("sort", resultItem.get("sort"));
            help.put("creator", resultItem.get("name"));

            helpList.add(help);
        }

        return helpList;
    }

    @Override
    public HelpInfoModel getHelpDetail(String id) {
        return helpInfoDao.selectHelpInfo(id);
    }
}
