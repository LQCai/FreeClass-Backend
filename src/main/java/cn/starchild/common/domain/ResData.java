package cn.starchild.common.domain;

import java.util.HashMap;

import static cn.starchild.common.domain.Code.FAIL;
import static cn.starchild.common.domain.Code.SUCCESS;

/**
 * 泛型实体类
 *
 * @author lqcai
 */
public class ResData extends HashMap<String, Object> {

    private static final long serialVersionUID = -8713837118340960775L;


    public ResData() {
        put("code", SUCCESS);
    }

    /**
     * 错误返回数据
     *
     * @param code
     * @param msg
     * @return
     */
    public static ResData error(String code, Object msg) {
        ResData resData = new ResData();
        resData.put("code", code);
        resData.put("msg", msg);
        return resData;
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
