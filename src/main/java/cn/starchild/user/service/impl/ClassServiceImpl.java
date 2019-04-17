package cn.starchild.user.service.impl;

import cn.starchild.common.dao.ClassDao;
import cn.starchild.common.dao.ClassStudentDao;
import cn.starchild.common.dao.UserDao;
import cn.starchild.common.model.ClassModel;
import cn.starchild.common.util.RandomUtils;
import cn.starchild.common.util.UUIDUtils;
import cn.starchild.user.service.ClassService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.logging.Logger;

@Service
public class ClassServiceImpl implements ClassService {

    @Resource
    private ClassDao classDao;

    @Resource
    private UserDao userDao;

    @Resource
    private ClassStudentDao classStudentDao;

    private Logger logger;

    @Override
    public List<Map<String, Object>> getMyTeachingClassList(String id) {
        // 获取该用户的课堂列表
        List<Map<String, Object>> classList = classDao.selectMyTeachingClassList(id);

        // 获取课堂与用户关联信息列表
        List<Map<String, Object>> classStudentList = classStudentDao.selectSimpleList(id);

        List<Map<String, Object>> myTeachingList = new ArrayList<>();
        for (Map<String, Object> classInfo :
                classList) {

            Integer peopleCount = 0;

            Map<String, Object> myTeachingClass = new HashMap<>();
            myTeachingClass.put("id", classInfo.get("id"));
            myTeachingClass.put("name", classInfo.get("name"));
            myTeachingClass.put("invitationCode", classInfo.get("invitation_code"));

            for (Map<String, Object> classStudentInfo :
                    classStudentList) {
                if (classInfo.get("id").equals(classStudentInfo.get("class_id"))) {
                    peopleCount++;
                }
            }
            myTeachingClass.put("peopleCount", peopleCount);

            myTeachingList.add(myTeachingClass);
        }

        return myTeachingList;
    }

    @Override
    public List<Map<String, Object>> getMyStudyingClassList(String id) {
        // 获取该用户的课堂列表
        List<Map<String, Object>> classList = classDao.selectMyStudyingClassList(id);

        List<Map<String, Object>> myStudyingClassList = new ArrayList<>();
        for (Map<String, Object> classInfo :
                classList) {

            Map<String, Object> myStudyingClass = new HashMap<>();

            myStudyingClass.put("id", classInfo.get("id"));

            if (!classInfo.containsKey("name")) {
                myStudyingClass.put("name", "未知");
            } else {
                myStudyingClass.put("name", classInfo.get("name"));
            }
            myStudyingClass.put("invitationCode", classInfo.get("invitation_code"));
            myStudyingClass.put("teacherName", classInfo.get("teacher_name"));


            Integer classStudentCount = classDao.selectCountForId(classInfo.get("id").toString());
            myStudyingClass.put("peopleCount", classStudentCount);

            myStudyingClassList.add(myStudyingClass);
        }

        return myStudyingClassList;
    }

    @Override
    public boolean createClass(JSONObject classData) {
        String teacherId = classData.getString("teacherId");
        String className = classData.getString("className");
        byte peopleMaximum = classData.getByte("peopleMaximum");
        byte topping = classData.getByte("topping");

        String invitationCode = RandomUtils.getRandomStr(6);
        List<String> invitationCodeList = classDao.selectClassCodeList();

        // 判断邀请码是否和已有课程重复
        for (String code :
                invitationCodeList) {
            if (invitationCode.equals(code)) {
                do {
                    invitationCode = RandomUtils.getRandomStr(6);
                }
                while (!invitationCode.equals(code));
                break;
            }
        }


        ClassModel classModel = new ClassModel();
        classModel.setId(UUIDUtils.uuid());
        classModel.setName(className);
        classModel.setTeacherId(teacherId);
        classModel.setInvitationCode(RandomUtils.getRandomStr(6));
        classModel.setStatus((byte) 1);
        classModel.setPeopleMaximum(peopleMaximum);
        classModel.setTopping(topping);
        classModel.setCreated(new Date());
        classModel.setModified(new Date());

        try {
            classDao.insert(classModel);
        } catch (Exception e) {
            logger.warning("创建班级失败:" + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean updateClass(JSONObject classData) {
        String teacherId = classData.getString("teacherId");
        String className = classData.getString("className");
        String id = classData.getString("id");
        byte peopleMaximum = classData.getByte("peopleMaximum");
        byte topping = classData.getByte("topping");

        ClassModel classModel = new ClassModel();
        classModel.setTeacherId(teacherId);
        classModel.setId(id);
        classModel.setName(className);
        classModel.setPeopleMaximum(peopleMaximum);
        classModel.setTopping(topping);
        classModel.setModified(new Date());

        try {
            boolean result = classDao.updateById(classModel);
            if (!result) {
                return false;
            }
        } catch (Exception e) {
            logger.warning("修改课堂失败:" + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean validateClass(String id) {
        ClassModel result = classDao.validateClass(id);
        if (result == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean validateClassByCode(String code) {
        ClassModel result = classDao.validateClassByCode(code);
        if (result == null) {
            return false;
        }
        return true;
    }

    @Override
    public ClassModel getClassByCode(String code) {
        return classDao.validateClassByCode(code);
    }
}
