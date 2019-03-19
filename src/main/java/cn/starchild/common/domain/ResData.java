package cn.starchild.common.domain;

import java.util.HashMap;

/**
 * 泛型实体类
 *
 * @author lqcai
 */
public class ResData extends HashMap<String, Object> {

    private static final long serialVersionUID = -8713837118340960775L;

    // 成功
    private static final String SUCCESS = "0000";

    // 异常 失败
    private static final String FAIL = "500";

    public ResData() {
        put("code", SUCCESS);
    }

    public static ResData error(Object msg) {
        ResData resData = new ResData();
        resData.put("code", FAIL);
        resData.put("msg", msg);
        return resData;
    }

    public static ResData ok(Object data) {
        ResData resData = new ResData();
        resData.put("code", SUCCESS);
        resData.put("data", data);
        return resData;
    }

    public static ResData ok() {
        return new ResData();
    }


    @Override
    public ResData put(String key, Object value) {
        super.put(key, value);
        return this;
    }

}
