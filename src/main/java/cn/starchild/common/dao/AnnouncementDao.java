package cn.starchild.common.dao;

import cn.starchild.common.model.AnnouncementModel;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface AnnouncementDao extends Mapper<AnnouncementModel> {
    AnnouncementModel selectAnnounceById(String id);

    boolean updateAnnounce(AnnouncementModel announcement);

    void deleteAnnouncement(String id);

    List<Map<String, Object>> selectAnnounceList(String classId);
}