package cn.starchild.user.service;

import java.util.List;
import java.util.Map;

public interface ClassService {

    List<Map<String, Object>> getMyTeachingClassList(String id);

    List<Map<String, Object>> getMyStudyingClassList(String id);
}
