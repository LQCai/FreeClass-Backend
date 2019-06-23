package cn.starchild.admin.service;

import cn.starchild.common.model.HelpInfoModel;

import java.util.List;
import java.util.Map;

public interface HelpInfoService {
    boolean addHelpInfo(HelpInfoModel helpInfo);

    boolean validateHelp(String id);

    boolean updateHelpInfo(HelpInfoModel helpInfo);

    boolean deleteHelpInfo(HelpInfoModel helpInfo);

    List<Map<String, Object>> getHelpList();

    HelpInfoModel getHelpDetail(String id);
}
