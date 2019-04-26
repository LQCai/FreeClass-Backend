package cn.starchild.user.service.impl;

import cn.starchild.common.dao.ClassStudentDao;
import cn.starchild.common.model.ClassStudentModel;
import cn.starchild.common.model.UserModel;
import cn.starchild.common.util.UUIDUtils;
import cn.starchild.user.service.ClassStudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

@Service
public class ClassStudentServiceImpl implements ClassStudentService {

    @Resource
    private ClassStudentDao classStudentDao;

    private Logger logger = Logger.getLogger(this.getClass());


    @Override
    public boolean joinClass(ClassStudentModel classStudent) {
        classStudent.setId(UUIDUtils.uuid());
        classStudent.setStatus((byte) 1);
        classStudent.setCreated(new Date());
        classStudent.setModified(new Date());

        try {
            boolean result = classStudentDao.joinClass(classStudent);
            if (!result) {
                return false;
            }
        } catch (Exception e) {
            logger.error("加入课堂失败:" + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean validateJoined(ClassStudentModel classStudentModel) {
        // 判断该用户是否已加入课堂
        List<Map<String, Object>> classJoinList = classStudentDao.validateJoined(classStudentModel);
        if (classJoinList.size() != 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean quitClass(ClassStudentModel classStudent) {
        try {
            boolean result = classStudentDao.deleteForClassAndStudent(classStudent);
            if (!result) {
                return false;
            }
        } catch (Exception e) {
            logger.error("退出课堂失败:" + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public UserModel getStudentInfo(String classId, String studentId) {
        ClassStudentModel classStudentModel = new ClassStudentModel();
        classStudentModel.setStudentId(studentId);
        classStudentModel.setClassId(classId);

        return classStudentDao.getStudentInfo(classStudentModel);
    }
}
