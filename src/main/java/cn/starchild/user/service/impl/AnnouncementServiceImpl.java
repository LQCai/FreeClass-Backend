package cn.starchild.user.service.impl;

import cn.starchild.common.dao.AnnouncementDao;
import cn.starchild.common.model.AnnouncementModel;
import cn.starchild.user.service.AnnouncementService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {
    @Resource
    private AnnouncementDao announcementDao;

    private Logger logger = Logger.getLogger(this.getClass());


    @Override
    public boolean postAnnouncement(AnnouncementModel announcement) {

        try {
            announcementDao.insert(announcement);
        } catch (Exception e) {
            logger.error("发布公告失败：" + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean validateAnnouncement(String id) {
        AnnouncementModel announcement = announcementDao.selectAnnounceById(id);
        if (announcement == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean editAnnouncement(AnnouncementModel announcement) {
        boolean result = announcementDao.updateAnnounce(announcement);

        if (!result) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteAnnouncement(String id) {
        try {
            announcementDao.deleteAnnouncement(id);
        } catch (Exception e) {
            logger.error("删除公告失败：" + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public List<Map<String, Object>> getAnnounceList(String classId) {
        List<Map<String, Object>> announceList = new ArrayList<>();

        List<Map<String, Object>> resultList = announcementDao.selectAnnounceList(classId);

        for (Map<String, Object> result :
                resultList) {
            Map<String, Object> announce = new HashMap<>();

            announce.put("id", result.get("id"));
            announce.put("title", result.get("title"));
            announce.put("content", result.get("content"));
            announce.put("annexUrl", result.get("annex_url"));
            announce.put("created", result.get("created"));

            announceList.add(announce);
        }

        return announceList;
    }
}
