package cn.starchild.user.service;

import cn.starchild.common.model.AnnouncementModel;

import java.util.List;
import java.util.Map;

public interface AnnouncementService {
    boolean postAnnouncement(AnnouncementModel announcement);

    boolean validateAnnouncement(String id);

    boolean editAnnouncement(AnnouncementModel announcement);

    boolean deleteAnnouncement(String id);

    List<Map<String, Object>> getAnnounceList(String classId);
}
