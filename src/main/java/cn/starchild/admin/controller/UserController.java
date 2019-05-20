package cn.starchild.admin.controller;

import cn.starchild.common.domain.ResData;
import cn.starchild.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping(value = "/admin/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResData getList() {
        return ResData.ok(userService.getUserList());
    }
}
