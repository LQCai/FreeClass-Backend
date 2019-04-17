package cn.starchild.user.controller;

import cn.starchild.common.domain.ResData;
import cn.starchild.user.service.ClassService;
import cn.starchild.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequestMapping("/user/class")
@RestController
public class ClassController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClassService classService;

    /**
     * 通过id获取用户的授课及上课列表
     * @param id
     * @return list
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResData getList(String id) {
        List<Map<String, Object>> myTeachingClassList = classService.getMyTeachingClassList(id);

        List<Map<String, Object>> myStudyingClassList = classService.getMyStudyingClassList(id);

        ResData res = new ResData();
        res.put("myTeachingClassList", myTeachingClassList);
        res.put("myStudyingClassList", myStudyingClassList);

        return res;
    }
}
