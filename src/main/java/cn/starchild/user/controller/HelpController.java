package cn.starchild.user.controller;

import cn.starchild.admin.service.HelpInfoService;
import cn.starchild.common.domain.ResData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/user/help")
@RestController
public class HelpController {
    @Autowired
    private HelpInfoService helpInfoService;

    /**
     * 帮助列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResData getList() {
        return ResData.ok(helpInfoService.getHelpList());
    }
}
