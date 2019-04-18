package cn.starchild.common.domain;

public final class Code {
    private Code() {}

    static final String SUCCESS = "0000";//成功
    static final String FAIL = "500";//通用错误

    public static final String DATA_NOT_FOUND = "1001";//用户存在

    public static final String USER_EXIST = "1002";//用户存在
    public static final String IS_TEACHER = "1003";//用户存在

    public static final String CLASS_JOINED = "1011";//课堂已加入


    // 请求错误
    public static final String PARAM_FORMAT_ERROR = "1201";//请求参数错误

    // db错误
    public static final String DATABASE_INSERT_FAIL = "1101";//数据库插入失败
    public static final String DATABASE_UPDATE_FAIL = "1102";//数据库修改失败
    public static final String DATABASE_DELETE_FAIL = "1103";//数据库删除失败

}
