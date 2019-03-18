package cn.starchild.user.service;

import java.util.List;
import java.util.Map;

public interface UserSrevice {
    public List<Map<String,Object>> getAllUsers() throws Exception;
}
