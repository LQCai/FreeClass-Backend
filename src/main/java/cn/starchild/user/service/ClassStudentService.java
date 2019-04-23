package cn.starchild.user.service;


import cn.starchild.common.model.ClassStudentModel;
import cn.starchild.common.model.UserModel;

public interface ClassStudentService {
    boolean joinClass(ClassStudentModel classStudent);

    // 判断该用户是否加入该课堂
    boolean validateJoined(ClassStudentModel classStudentModel);

    boolean quitClass(ClassStudentModel classStudent);

    UserModel getStudentInfo(String classId, String studentId);
}
