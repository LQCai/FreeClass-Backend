package cn.starchild.admin.controller;

import cn.starchild.admin.service.HelpInfoService;
import cn.starchild.common.domain.Code;
import cn.starchild.common.domain.ResData;
import cn.starchild.common.model.HelpInfoModel;
import cn.starchild.common.util.UUIDUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "/admin/helpInfo")
public class HelpInfoController {
    @Autowired
    private HelpInfoService helpInfoService;


    /**
     * 添加帮助信息
     * @param jsonParams
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResData postAdd(@RequestBody String jsonParams) {
        JSONObject data = JSONObject.parseObject(jsonParams);

        if (!data.containsKey("userId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "用户id不可为空");
        }
        if (!data.containsKey("categoryId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "分类id不可为空");
        }
        if (!data.containsKey("title") || data.getString("title").equals("")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "分类标题不可为空");
        }
        if (!data.containsKey("content") || data.getString("content").equals("")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "帮助内容不可为空");
        }
        if (!data.containsKey("sort") || data.getString("sort").equals("")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "排序不可为空");
        }

//        String userId = data.getString("userId");
        String userId = "2019-05-19-19-44"; // 暂时默认
        String helpCategoryId = "0001"; // 暂时默认
        String title = data.getString("title");
        String content = data.getString("content");
        int sort = Integer.parseInt(data.getString("sort"));

        // TODO 验证分类、用户是否存在

        HelpInfoModel helpInfo = new HelpInfoModel();
        helpInfo.setId(UUIDUtils.uuid());
        helpInfo.setTitle(title);
        helpInfo.setHelpCategoryId(helpCategoryId);
        helpInfo.setContent(content);
        helpInfo.setSort((byte) sort);
        helpInfo.setStatus((byte) 1);
        helpInfo.setCreator(userId);
        helpInfo.setCreated(new Date());
        helpInfo.setModifier(userId);
        helpInfo.setModified(new Date());

        boolean result = helpInfoService.addHelpInfo(helpInfo);
        if (!result) {
            return ResData.error(Code.DATABASE_INSERT_FAIL, "添加帮助信息失败");
        }

        return ResData.ok();
    }

    /**
     * 修改帮助信息
     * @param jsonParams
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResData putUpdate(@RequestBody String jsonParams) {
        JSONObject data = JSONObject.parseObject(jsonParams);

        if (!data.containsKey("userId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "用户id不可为空");
        }
        if (!data.containsKey("id")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "帮助信息id不可为空");
        }

        boolean isHelp = helpInfoService.validateHelp(data.getString("id"));
        if (!isHelp) {
            return ResData.error(Code.DATA_NOT_FOUND, "该帮助不存在");
        }

        // TODO 验证分类、用户是否存在

        HelpInfoModel helpInfo = new HelpInfoModel();
        helpInfo.setId(data.getString("id"));
        helpInfo.setModifier("2019-05-19-19-44");
        helpInfo.setModified(new Date());

        if (data.containsKey("title") && !data.getString("title").equals("")) {
            helpInfo.setTitle(data.getString("title"));
        }
        if (data.containsKey("content") && !data.getString("content").equals("")) {
            helpInfo.setContent(data.getString("content"));
        }
        if (data.containsKey("sort") && !data.getString("sort").equals("")) {
            helpInfo.setSort((byte) Integer.parseInt(data.getString("sort")));
        }

        boolean result = helpInfoService.updateHelpInfo(helpInfo);
        if (!result) {
            return ResData.error(Code.DATABASE_UPDATE_FAIL, "修改帮助信息失败");
        }

        return ResData.ok();
    }



    /**
     * 删除帮助信息
     * @param jsonParams
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResData deleteDelete(@RequestBody String jsonParams) {
        JSONObject data = JSONObject.parseObject(jsonParams);

        if (!data.containsKey("userId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "用户id不可为空");
        }
        if (!data.containsKey("id")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "帮助信息id不可为空");
        }

        boolean isHelp = helpInfoService.validateHelp(data.getString("id"));
        if (!isHelp) {
            return ResData.error(Code.DATA_NOT_FOUND, "改帮助不存在");
        }

        // TODO 验证分类、用户是否存在

        HelpInfoModel helpInfo = new HelpInfoModel();
        helpInfo.setId(data.getString("id"));
        helpInfo.setModifier("2019-05-19-19-44");
        helpInfo.setModified(new Date());
        helpInfo.setStatus((byte) 2);

        boolean result = helpInfoService.deleteHelpInfo(helpInfo);
        if (!result) {
            return ResData.error(Code.DATABASE_DELETE_FAIL, "删除帮助信息失败");
        }

        return ResData.ok();
    }

    /**
     * 帮助列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResData getList() {
        return ResData.ok(helpInfoService.getHelpList());
    }

    /**
     * 帮助详情
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ResData getDetail(String id) {
        HelpInfoModel helpInfo = helpInfoService.getHelpDetail(id);
        if (helpInfo == null) {
            return ResData.error(Code.DATA_NOT_FOUND, "该帮助不存在");
        }

        return ResData.ok(helpInfo);
    }
}
