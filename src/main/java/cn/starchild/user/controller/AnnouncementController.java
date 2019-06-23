package cn.starchild.user.controller;

import cn.starchild.common.domain.Code;
import cn.starchild.common.domain.ResData;
import cn.starchild.common.model.AnnouncementModel;
import cn.starchild.common.util.FileUtils;
import cn.starchild.common.util.UUIDUtils;
import cn.starchild.user.service.AnnouncementService;
import cn.starchild.user.service.ClassService;
import com.alibaba.fastjson.JSONObject;
import com.sun.istack.internal.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;


@RequestMapping("/user/announcement")
@RestController
public class AnnouncementController {
    @Autowired
    private ClassService classService;
    @Autowired
    private AnnouncementService announcementService;

    /**
     * 发布公告
     *
     * @return
     */
    @RequestMapping(value = "post", method = RequestMethod.POST)
    public ResData postAnnounce(MultipartFile annex,
                                @NotNull String teacherId,
                                @NotNull String classId,
                                @NotNull String title,
                                @NotNull String content) {

        if (teacherId == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "teacherId为空");
        }

        if (classId == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "classId为空");
        }

        if (title == null || title.equals("")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "公告标题为空");
        }

        if (content == null || content.equals("")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "公告内容不能为空");
        }

        // 验证该课堂是否存在，且是不是该用户创建的
        boolean isClass = classService.validateClassForTeacher(classId, teacherId);
        if (!isClass) {
            return ResData.error(Code.DATA_NOT_FOUND, "找不到该教师的课堂");
        }

        String annexUrl = "";
        if (annex != null) {
            String uploadName = annex.getOriginalFilename().substring(0, annex.getOriginalFilename().lastIndexOf("."));
            annexUrl = FileUtils.ANNOUNCEMENT_ANNEX_DOMAIN + FileUtils.saveFile(annex, 1, uploadName);
        }

        AnnouncementModel announcement = new AnnouncementModel();
        announcement.setId(UUIDUtils.uuid());
        announcement.setClassId(classId);
        announcement.setTitle(title);
        announcement.setContent(content);
        announcement.setAnnexUrl(annexUrl);
        announcement.setTopping((byte) 2);
        announcement.setStatus((byte) 1);
        announcement.setCreated(new Date());
        announcement.setModified(new Date());

        boolean result = announcementService.postAnnouncement(announcement);
        if (!result) {
            return ResData.error(Code.DATABASE_INSERT_FAIL, "发布公告失败");
        }

        return ResData.ok();
    }

    /**
     * 发布公告（文字）
     * @param jsonParams
     * @return
     */
    @RequestMapping(value = "postText", method = RequestMethod.POST)
    public ResData postAnnounceNotFile(@RequestBody String jsonParams) {

        JSONObject data = JSONObject.parseObject(jsonParams);
        if (!data.containsKey("teacherId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "teacherId为空");
        }

        if (!data.containsKey("classId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "classId为空");
        }

        if (!data.containsKey("title") || data.getString("title").equals("")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "title为空");
        }

        if (!data.containsKey("content") || data.getString("content").equals("")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "content为空");
        }

        String teacherId = data.getString("teacherId");
        String classId = data.getString("classId");
        String title = data.getString("title");
        String content = data.getString("content");


        // 验证该课堂是否存在，且是不是该用户创建的
        boolean isClass = classService.validateClassForTeacher(classId, teacherId);
        if (!isClass) {
            return ResData.error(Code.DATA_NOT_FOUND, "找不到该教师的课堂");
        }

        String annexUrl = "";

        AnnouncementModel announcement = new AnnouncementModel();
        announcement.setId(UUIDUtils.uuid());
        announcement.setClassId(classId);
        announcement.setTitle(title);
        announcement.setContent(content);
        announcement.setAnnexUrl(annexUrl);
        announcement.setTopping((byte) 2);
        announcement.setStatus((byte) 1);
        announcement.setCreated(new Date());
        announcement.setModified(new Date());

        boolean result = announcementService.postAnnouncement(announcement);
        if (!result) {
            return ResData.error(Code.DATABASE_INSERT_FAIL, "发布公告失败");
        }

        return ResData.ok();
    }


    /**
     * 编辑公告
     *
     * @return
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ResData editAnnounce(MultipartFile annex,
                                @NotNull String teacherId,
                                @NotNull String classId,
                                @NotNull String id,
                                @NotNull String title,
                                @NotNull String content) {

        if (teacherId == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "teacherId为空");
        }

        if (classId == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "classId为空");
        }

        if (id == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "公告id为空");
        }

        // 验证该课堂是否存在，且是不是该用户创建的
        boolean isClass = classService.validateClassForTeacher(classId, teacherId);
        if (!isClass) {
            return ResData.error(Code.DATA_NOT_FOUND, "找不到该教师的课堂");
        }

        // 验证公告是否存在
        boolean isAnnounce = announcementService.validateAnnouncement(id);
        if (!isAnnounce) {
            return ResData.error(Code.DATA_NOT_FOUND, "该公告不存在");
        }

        AnnouncementModel announcement = new AnnouncementModel();
        announcement.setId(id);
        announcement.setModified(new Date());

        if (title != null) {
            announcement.setTitle(title);
        }

        if (content != null) {
            announcement.setContent(content);
        }

        if (annex != null) {
            String uploadName = annex.getOriginalFilename().substring(0, annex.getOriginalFilename().lastIndexOf("."));
            String annexUrl = FileUtils.ANNOUNCEMENT_ANNEX_DOMAIN + FileUtils.saveFile(annex, 1, uploadName);
            announcement.setAnnexUrl(annexUrl);
        }

        boolean result = announcementService.editAnnouncement(announcement);
        if (!result) {
            return ResData.error(Code.DATABASE_UPDATE_FAIL, "编辑公告失败");
        }

        return ResData.ok();
    }

    /**
     * 编辑公告
     *
     * @return
     */
    @RequestMapping(value = "editText", method = RequestMethod.POST)
    public ResData editAnnounceText(@RequestBody String jsonParams) {
        JSONObject data = JSONObject.parseObject(jsonParams);
        if (!data.containsKey("id")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "id为空");
        }
        if (!data.containsKey("teacherId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "teacherId为空");
        }
        if (!data.containsKey("classId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "classId为空");
        }

        String id = data.getString("id");
        String teacherId = data.getString("teacherId");
        String classId = data.getString("classId");
        String title = null;
        if (data.containsKey("title") && !data.getString("title").equals("")) {
            title = data.getString("title");
        }
        String content = null;
        if (data.containsKey("content") && !data.getString("content").equals("")) {
            content = data.getString("content");
        }

        // 验证该课堂是否存在，且是不是该用户创建的
        boolean isClass = classService.validateClassForTeacher(classId, teacherId);
        if (!isClass) {
            return ResData.error(Code.DATA_NOT_FOUND, "找不到该教师的课堂");
        }

        // 验证公告是否存在
        boolean isAnnounce = announcementService.validateAnnouncement(id);
        if (!isAnnounce) {
            return ResData.error(Code.DATA_NOT_FOUND, "该公告不存在");
        }

        AnnouncementModel announcement = new AnnouncementModel();
        announcement.setId(id);
        announcement.setModified(new Date());

        if (title != null) {
            announcement.setTitle(title);
        }

        if (content != null) {
            announcement.setContent(content);
        }


        boolean result = announcementService.editAnnouncement(announcement);
        if (!result) {
            return ResData.error(Code.DATABASE_UPDATE_FAIL, "编辑公告失败");
        }

        return ResData.ok();
    }

    /**
     * 删除公告
     *
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public ResData deleteAnnounce(@RequestBody String jsonParams) {
        JSONObject classData = JSONObject.parseObject(jsonParams).getJSONObject("deleteData");
        if (!classData.containsKey("id")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "公告id为空");
        }

        if (!classData.containsKey("teacherId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "teacherId为空");
        }

        if (!classData.containsKey("classId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "classId为空");
        }

        String id = classData.getString("id");
        String classId = classData.getString("classId");
        String teacherId = classData.getString("teacherId");

        // 验证该课堂是否存在，且是不是该用户创建的
        boolean isClass = classService.validateClassForTeacher(classId, teacherId);
        if (!isClass) {
            return ResData.error(Code.DATA_NOT_FOUND, "该公告不是该教师所创建");
        }

        // 判断作业是否存在
        boolean isAnnounce = announcementService.validateAnnouncement(id);
        if (!isAnnounce) {
            return ResData.error(Code.DATA_NOT_FOUND, "该公告不存在");
        }

        boolean result = announcementService.deleteAnnouncement(id);
        if (!result) {
            return ResData.error(Code.DATABASE_DELETE_FAIL, "删除公告失败");
        }

        return ResData.ok();
    }

    /**
     * 获取公告列表
     *
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ResData getAnnounceList(String classId) {
        return ResData.ok(announcementService.getAnnounceList(classId));
    }
}
