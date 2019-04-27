package cn.starchild.user.controller;

import cn.starchild.common.domain.Code;
import cn.starchild.common.domain.ResData;
import cn.starchild.common.model.HomeWorkModel;
import cn.starchild.common.model.HomeworkSubmitModel;
import cn.starchild.common.model.UserModel;
import cn.starchild.common.util.FileUtils;
import cn.starchild.common.util.UUIDUtils;
import cn.starchild.user.service.ClassService;
import cn.starchild.user.service.ClassStudentService;
import cn.starchild.user.service.HomeworkService;
import cn.starchild.user.service.UserService;
import com.alibaba.fastjson.JSONObject;
import com.sun.istack.internal.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequestMapping("/user/homework")
@RestController
public class HomeworkController {

    @Autowired
    private ClassService classService;

    @Autowired
    private UserService userService;

    @Autowired
    private HomeworkService homeworkService;

    @Autowired
    private ClassStudentService classStudentService;

    /**
     * 发布作业
     *
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

        if (homeworkName == null || homeworkName.equals("")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "homeworkName为空");
        }

        if (classId == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "classId为空");
        }

        if (homeworkIntroduction == null || homeworkIntroduction.equals("")) {
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
            return ResData.error(Code.PARAM_FORMAT_ERROR, "是否发送邮件格式错误");
        }

        // 验证该课堂是否存在，且是不是该用户创建的
        boolean isClass = classService.validateClassForTeacher(classId, teacherId);
        if (!isClass) {
            return ResData.error(Code.DATA_NOT_FOUND, "找不到该教师的课堂");
        }

        if (annex == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "附件未上传");
        }

        String uploadName = annex.getOriginalFilename().substring(0, annex.getOriginalFilename().lastIndexOf("."));
        String annexUrl = FileUtils.HOMEWORK_ANNEX_DOMAIN + FileUtils.saveFile(annex, 0, uploadName);

        HomeWorkModel homeWorkModel = new HomeWorkModel();
        homeWorkModel.setId(UUIDUtils.uuid());
        homeWorkModel.setName(homeworkName);
        homeWorkModel.setClassId(classId);
        homeWorkModel.setIntroduction(homeworkIntroduction);
        homeWorkModel.setAnnexUrl(annexUrl);
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


    /**
     * 更新作业
     *
     * @param annex
     * @param id
     * @param teacherId
     * @param homeworkName
     * @param classId
     * @param homeworkIntroduction
     * @param sendByEmail
     * @param fullScore
     * @param deadline
     * @return
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public ResData putJob(MultipartFile annex,
                          @NotNull String id,
                          @NotNull String teacherId,
                          @NotNull String homeworkName,
                          @NotNull String classId,
                          @NotNull String homeworkIntroduction,
                          @NotNull Integer sendByEmail,
                          @NotNull Integer fullScore,
                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date deadline) {

        HomeWorkModel homeWorkModel = new HomeWorkModel();

        if (id == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "作业id为空");
        }

        if (teacherId == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "teacherId为空");
        }

        if (classId == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "classId为空");
        }

        homeWorkModel.setId(id);
        homeWorkModel.setClassId(classId);

        if (homeworkName != null && !homeworkName.equals("")) {
            homeWorkModel.setName(homeworkName);
        }

        if (homeworkIntroduction != null && !homeworkIntroduction.equals("")) {
            homeWorkModel.setIntroduction(homeworkIntroduction);
        }

        if (sendByEmail != null) {
            if (sendByEmail != 1 && sendByEmail != 2) {
                return ResData.error(Code.PARAM_FORMAT_ERROR, "是否发送邮件格式错误");
            }
            homeWorkModel.setSendByEmail(sendByEmail.byteValue());
        }

        if (fullScore != null) {
            homeWorkModel.setFullScore(fullScore);
        }

        if (deadline != null) {
            homeWorkModel.setDeadline(deadline);
        }

        // 验证该课堂是否存在，且是不是该用户创建的
        boolean isClass = classService.validateClassForTeacher(classId, teacherId);
        if (!isClass) {
            return ResData.error(Code.DATA_NOT_FOUND, "找不到该教师的课堂");
        }

        // 判断作业是否存在
        boolean isHomework = homeworkService.validateHomework(id);
        if (!isHomework) {
            return ResData.error(Code.DATA_NOT_FOUND, "该作业不存在");
        }

        if (annex != null) {
            String uploadName = annex.getOriginalFilename().substring(0, annex.getOriginalFilename().lastIndexOf("."));
            String annexUrl = FileUtils.HOMEWORK_ANNEX_DOMAIN + FileUtils.saveFile(annex, 0, uploadName);
            homeWorkModel.setAnnexUrl(annexUrl);
        }

        homeWorkModel.setModified(new Date());

        boolean result = homeworkService.updateHomework(homeWorkModel);
        if (!result) {
            return ResData.error(Code.DATABASE_INSERT_FAIL, "发布作业失败!");
        }

        return ResData.ok();
    }

    /**
     * 删除作业
     *
     * @param jsonParams
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public ResData deleteJob(@RequestBody String jsonParams) {
        JSONObject classData = JSONObject.parseObject(jsonParams).getJSONObject("deleteData");
        if (!classData.containsKey("id")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "作业id为空");
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
            return ResData.error(Code.DATA_NOT_FOUND, "该作业不是该教师所创建");
        }

        // 判断作业是否存在
        boolean isJob = homeworkService.validateHomework(id);
        if (!isJob) {
            return ResData.error(Code.DATA_NOT_FOUND, "该作业不存在");
        }

        boolean result = homeworkService.deleteHomework(id);
        if (!result) {
            return ResData.error(Code.DATABASE_DELETE_FAIL, "删除作业失败");
        }

        return ResData.ok();
    }


    /**
     * 获取作业列表
     *
     * @param classId
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResData getTeacherList(String classId) {
        if (classId == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "classId为空");
        }

        boolean isClass = classService.validateClass(classId);
        if (!isClass) {
            return ResData.error(Code.DATA_NOT_FOUND, "该课堂不存在");
        }

        List<Map<String, Object>> homeworkList = homeworkService.getHomeworkList(classId);

        return ResData.ok(homeworkList);
    }


    /**
     * 获取学生提交作业列表
     *
     * @return
     */
    @RequestMapping(value = "studentList", method = RequestMethod.GET)
    public ResData getStudentJobList(String classId, String homeworkId) {
        List<Map<String, Object>> studentJobList = homeworkService.getStudentHomeworkList(classId, homeworkId);

        return ResData.ok(studentJobList);
    }

    /**
     * 提交作业
     *
     * @param studentId
     * @param classId
     * @param homeworkId
     * @param homeworkContent
     * @param annex
     * @return
     */
    @RequestMapping(value = "submit", method = RequestMethod.POST)
    public ResData postSubmit(String studentId,
                              String classId,
                              String homeworkId,
                              String homeworkContent,
                              MultipartFile annex) {
        if (studentId == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "studentId为空");
        }

        if (classId == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "classId为空");
        }

        if (homeworkId == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "homeworkId为空");
        }

        if (homeworkContent == null || homeworkContent.equals("")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "作业内容不可为空");
        }

        // 验证学生是否属于该课堂，并获取相关信息用于命名学生上传文件
        UserModel student = classStudentService.getStudentInfo(classId, studentId);
        if (student == null) {
            return ResData.error(Code.DATA_NOT_FOUND, "该学生不属于该课堂");
        }

        HomeWorkModel homeWork = homeworkService.getHomeworkInfo(homeworkId);
        if (homeWork == null) {
            return ResData.error(Code.DATA_NOT_FOUND, "找不到该发布作业");
        }

        // 判断是否已过截止时间
        if (homeWork.getDeadline().after(new Date())) {
            return ResData.error(Code.AFTER_DEADLINE, "作业已过截止时间,不可提交");
        }

        // 判断是否已经提交
        boolean isSubmit = homeworkService.validateSubmitted(studentId, homeworkId);
        if (isSubmit) {
            return ResData.error(Code.JOB_SUBMITTED, "作业已提交");
        }

        HomeworkSubmitModel homeworkSubmitModel = new HomeworkSubmitModel();

        if (annex != null) {
            String uploadFileName = homeWork.getName() + "_" + student.getSerialCode() + "_" + student.getName();
            String annexUrl = FileUtils.submitHomework(annex, classId, homeworkId, uploadFileName);

            homeworkSubmitModel.setAnnexUrl(FileUtils.HOMEWORK_SUBMIT_COMMON_DOMAIN
                    + classId + "/"
                    + homeworkId + "/"
                    + annexUrl);
        }

        homeworkSubmitModel.setId(UUIDUtils.uuid());
        homeworkSubmitModel.setContent(homeworkContent);
        homeworkSubmitModel.setHomeworkId(homeworkId);
        homeworkSubmitModel.setStatus((byte) 1);
        homeworkSubmitModel.setStudentId(studentId);
        homeworkSubmitModel.setCreated(new Date());
        homeworkSubmitModel.setModified(new Date());

        boolean result = homeworkService.submitHomework(homeworkSubmitModel);
        if (!result) {
            return ResData.error(Code.DATABASE_INSERT_FAIL, "提交作业失败");
        }

        return ResData.ok();
    }

    /**
     * 获取作业提交状态及提交信息
     * @param studentId
     * @param homeworkId
     * @return
     */
    @RequestMapping(value = "submitStatus", method = RequestMethod.GET)
    public ResData getSubmitStatus(String studentId,
                                   String homeworkId) {
        HomeworkSubmitModel homeworkSubmitModel = new HomeworkSubmitModel();
        homeworkSubmitModel.setStudentId(studentId);
        homeworkSubmitModel.setHomeworkId(homeworkId);

        Map<String, Object> submittedInfo = homeworkService.getSubmittedInfo(studentId, homeworkId);

        return ResData.ok(submittedInfo);
    }

    @RequestMapping(value = "send", method = RequestMethod.GET)
    public ResData sendEmail() {
        homeworkService.sendEmail();
        return ResData.ok();
    }

}
