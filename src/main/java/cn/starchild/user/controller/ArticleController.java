package cn.starchild.user.controller;

import cn.starchild.common.domain.Code;
import cn.starchild.common.domain.ResData;
import cn.starchild.common.model.ArticleModel;
import cn.starchild.common.util.FileUtils;
import cn.starchild.user.service.ArticleService;
import cn.starchild.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/user/article")
public class ArticleController {
    @Autowired
    private UserService userService;
    @Autowired
    private ArticleService articleService;

    /**
     * 发布动态
     * @param images
     * @param content
     * @param userId
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResData postAdd(@RequestParam("images") MultipartFile[] images,
                           String content,
                           String userId) {

        boolean isUser = userService.validateUser(userId);
        if (!isUser) {
            return ResData.error(Code.DATA_NOT_FOUND, "用户不存在");
        }

        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile image : images) {
            String uploadName = image.getOriginalFilename().substring(0, image.getOriginalFilename().lastIndexOf("."));
            String imageUrl = FileUtils.ARTICLE_IMAGE_DOMAIN + FileUtils.saveFile(image, 2, uploadName);
            imageUrls.add(imageUrl);
        }

        ArticleModel articleModel = new ArticleModel();
        articleModel.setUserId(userId);
        articleModel.setContent(content);
        articleModel.setImageUrlArray(imageUrls.toString());

        boolean result = articleService.postArticle(articleModel);
        if (!result) {
            return ResData.error(Code.DATABASE_INSERT_FAIL, "发布动态失败");
        }

        return ResData.ok();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResData getList() {
        return ResData.ok(articleService.getArticleList());
    }
}
