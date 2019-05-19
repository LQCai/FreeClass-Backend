package cn.starchild.admin.controller;

import cn.starchild.admin.service.HelpCategoryService;
import cn.starchild.common.domain.Code;
import cn.starchild.common.domain.ResData;
import cn.starchild.common.model.HelpCategoryModel;
import cn.starchild.common.util.UUIDUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RequestMapping(value = "/admin/helpCategory")
@RestController
public class HelpCategoryController {
    @Autowired
    private HelpCategoryService helpCategoryService;

    /**
     * 添加帮助分类
     * @param jsonParams
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResData postAdd(@RequestBody String jsonParams) {
        JSONObject data = JSONObject.parseObject(jsonParams);

        if (!data.containsKey("userId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "用户id不可为空");
        }
        if (!data.containsKey("name") || data.getString("name").equals("")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "分类名不可为空");
        }
        if (!data.containsKey("sort") || data.getString("sort").equals("")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "排序不可为空");
        }

//        String userId = data.getString("userId");
        String userId = "2019-05-19-19-44"; // 暂时默认
        String name = data.getString("name");
        int sort = Integer.parseInt(data.getString("sort"));

        HelpCategoryModel helpCategory = new HelpCategoryModel();
        helpCategory.setId(UUIDUtils.uuid());
        helpCategory.setName(name);
        helpCategory.setSort((byte) sort);
        helpCategory.setStatus((byte) 1);
        helpCategory.setCreator(userId);
        helpCategory.setCreated(new Date());
        helpCategory.setModifier(userId);
        helpCategory.setModified(new Date());

        boolean result = helpCategoryService.addHelpCategory(helpCategory);
        if (!result) {
            return ResData.error(Code.DATABASE_INSERT_FAIL, "添加帮助分类失败");
        }

        return ResData.ok();
    }
}
