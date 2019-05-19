package cn.starchild.common.dao;

import cn.starchild.common.model.HelpInfoModel;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface HelpInfoDao extends Mapper<HelpInfoModel> {
    HelpInfoModel selectById(String id);

    void updateHelpInfo(HelpInfoModel helpInfo);

    void deleteHelpInfo(HelpInfoModel helpInfo);

    List<Map<String, Object>> getHelpList();

    HelpInfoModel selectHelpInfo(String id);
}