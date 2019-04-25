package cn.starchild.user.service;

import cn.starchild.common.model.AnnouncementModel;

public interface AnnouncementService {
    boolean postAnnouncement(AnnouncementModel announcement);

    boolean validateAnnouncement(String id);

    boolean editAnnouncement(AnnouncementModel announcement);
}
