package cn.starchild.user.controller;

import cn.starchild.common.domain.Code;
import cn.starchild.common.domain.ResData;
import cn.starchild.common.model.HomeWorkModel;
import cn.starchild.common.util.FileUtils;
import cn.starchild.common.util.UUIDUtils;
import cn.starchild.user.service.ClassService;
import cn.starchild.user.service.HomeworkService;
import cn.starchild.user.service.UserService;
import com.sun.istack.internal.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@RequestMapping("/user/homework")
@RestController
public class HomeworkController {

    @Autowired
    private ClassService classService;

    @Autowired
    private UserService userService;

    @Autowired
    private HomeworkService homeworkService;

    /**
     * 发布作业
     * @param annex
     * @param teacherId
     * @param homeworkName
     * @param classId
     * @param homeworkIntroduction
     * @param sendByEmail
     * @param fullScore
     * @param deadline
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public ResData postJob(MultipartFile annex,
                           @NotNull String teacherId,
                           @NotNull String homeworkName,
                           @NotNull String classId,
                           @NotNull String homeworkIntroduction,
                           @NotNull Integer sendByEmail,
                           @NotNull Integer fullScore,
                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date deadline) throws IOException {

        if (teacherId == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "teacherId为空");
        }

        if (homeworkName == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "homeworkName为空");
        }

        if (classId == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "classId为空");
        }

        if (homeworkIntroduction == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "homeworkIntroduction为空");
        }

        if (sendByEmail == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "sendByEmail为空");
        }

        if (fullScore == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "fullScore为空");
        }

        if (deadline == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "deadline为空");
        }

        if (sendByEmail != 1 && sendByEmail != 2) {
            return ResData.error(Code.PARAM_FORMAT_ERROR,"是否发送邮件格式错误");
        }

        // 验证该课堂是否存在，且是不是该用户创建的
        boolean isClass = classService.validateClassForTeacher(classId, teacherId);
        if (!isClass) {
            return ResData.error(Code.DATA_NOT_FOUND, "找不到该教师的课堂");
        }

        String uploadName = annex.getOriginalFilename().substring(0, annex.getOriginalFilename().lastIndexOf("."));
        String annexUrl = FileUtils.saveFile(annex, 0, uploadName);

        HomeWorkModel homeWorkModel = new HomeWorkModel();
        homeWorkModel.setId(UUIDUtils.uuid());
        homeWorkModel.setName(homeworkName);
        homeWorkModel.setClassId(classId);
        homeWorkModel.setIntroduction(homeworkIntroduction);
        homeWorkModel.setAnnexUrl(FileUtils.HOMEWORK_ANNEX_DOMAIN + annexUrl);
        homeWorkModel.setStatus((byte) 1);
        homeWorkModel.setSendByEmail(sendByEmail.byteValue());
        homeWorkModel.setFullScore(fullScore);
        homeWorkModel.setDeadline(deadline);
        homeWorkModel.setCreated(new Date());
        homeWorkModel.setModified(new Date());

        boolean result = homeworkService.postHomework(homeWorkModel);
        if (!result) {
            return ResData.error(Code.DATABASE_INSERT_FAIL, "发布作业失败!");
        }

        return ResData.ok();
    }
}
