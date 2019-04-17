package cn.starchild.user.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface ClassService {

    List<Map<String, Object>> getMyTeachingClassList(String id);

    List<Map<String, Object>> getMyStudyingClassList(String id);

    boolean createClass(JSONObject classData);
}
