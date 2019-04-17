package cn.starchild.user.service.impl;

import cn.starchild.common.dao.ClassDao;
import cn.starchild.common.dao.ClassStudentDao;
import cn.starchild.common.dao.UserDao;
import cn.starchild.user.service.ClassService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClassServiceImpl implements ClassService {

    @Resource
    private ClassDao classDao;

    @Resource
    private UserDao userDao;

    @Resource
    private ClassStudentDao classStudentDao;

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
            myStudyingClass.put("name", classInfo.get("name"));
            myStudyingClass.put("invitationCode", classInfo.get("invitation_code"));
            myStudyingClass.put("teacherName", classInfo.get("teacher_name"));

            Integer classStudentCount = classDao.selectCountForId(classInfo.get("id").toString());
            myStudyingClass.put("peopleCount", classStudentCount);

            myStudyingClassList.add(myStudyingClass);
        }

        return myStudyingClassList;
    }
}
