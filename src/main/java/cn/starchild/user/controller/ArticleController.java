package cn.starchild.user.controller;

import cn.starchild.common.domain.Code;
import cn.starchild.common.domain.ResData;
import cn.starchild.common.model.ArticleModel;
import cn.starchild.common.util.FileUtils;
import cn.starchild.user.service.ArticleService;
import cn.starchild.user.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        if (userId == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "userId为空");
        }

        if (content == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "内容不可为空");
        }

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

    /**
     * 动态列表
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResData getList(String pageIndex) {
        return ResData.ok(articleService.getArticleList(pageIndex));
    }

    /**
     * 获取动态详情
     * @param articleId
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ResData getDetail(String articleId) {
        if (articleId == null) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "动态id为空");
        }

        Map<String, Object> articleInfo = articleService.getArticleInfo(articleId);
        if (articleInfo == null) {
            return ResData.error(Code.DATA_NOT_FOUND, "该动态不见了");
        }

        return ResData.ok(articleInfo);
    }

    /**
     * 收藏动态
     * @param jsonParams
     * @return
     */
    @RequestMapping(value = "/collect", method = RequestMethod.POST)
    public ResData postCollect(@RequestBody String jsonParams) {
        JSONObject joinData = JSONObject.parseObject(jsonParams).getJSONObject("data");

        if (!joinData.containsKey("userId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "用户id不可为空");
        }
        if (!joinData.containsKey("articleId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "动态id不可为空");
        }

        String userId = joinData.getString("userId");
        String articleId = joinData.getString("articleId");

        boolean isUser = userService.validateUser(userId);
        if (!isUser) {
            return ResData.error(Code.DATA_NOT_FOUND, "用户不存在");
        }

        boolean isArticle = articleService.validateArticle(articleId);
        if (!isArticle) {
            return ResData.error(Code.DATA_NOT_FOUND, "动态不存在");
        }

        boolean collected = articleService.validateCollected(userId, articleId);
        if (collected) {
            return ResData.error(Code.COLLECTED, "您已收藏该动态");
        }

        boolean result = articleService.collectArticle(articleId, userId);
        if (!result) {
            return ResData.error("收藏动态失败");
        }

        return ResData.ok();
    }

    /**
     * 取消收藏
     * @param jsonParams
     * @return
     */
    @RequestMapping(value = "/cancelCollect", method = RequestMethod.PUT)
    public ResData putCancelCollect(@RequestBody String jsonParams) {
        JSONObject joinData = JSONObject.parseObject(jsonParams).getJSONObject("data");

        if (!joinData.containsKey("userId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "用户id不可为空");
        }
        if (!joinData.containsKey("articleId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "动态id不可为空");
        }

        String userId = joinData.getString("userId");
        String articleId = joinData.getString("articleId");

        boolean collected = articleService.validateCollected(userId, articleId);
        if (!collected) {
            return ResData.error(Code.DATA_NOT_FOUND, "您未收藏此动态");
        }

        boolean result = articleService.cancelCollect(userId, articleId);
        if (!result) {
            return ResData.error(Code.DATABASE_UPDATE_FAIL, "取消收藏失败");
        }

        return ResData.ok();
    }

    /**
     * 评论动态
     * @param jsonParams
     * @return
     */
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public ResData postComment(@RequestBody String jsonParams) {
        JSONObject joinData = JSONObject.parseObject(jsonParams).getJSONObject("data");

        if (!joinData.containsKey("userId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "用户id不可为空");
        }
        if (!joinData.containsKey("articleId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "动态id不可为空");
        }
        if (!joinData.containsKey("content")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "评论内容不可为空");
        }

        String userId = joinData.getString("userId");
        String articleId = joinData.getString("articleId");
        String content = joinData.getString("content");

        boolean isUser = userService.validateUser(userId);
        if (!isUser) {
            return ResData.error(Code.DATA_NOT_FOUND, "用户不存在");
        }

        boolean isArticle = articleService.validateArticle(articleId);
        if (!isArticle) {
            return ResData.error(Code.DATA_NOT_FOUND, "动态不存在");
        }

        boolean result = articleService.commentArticle(articleId, userId, content);
        if (!result) {
            return ResData.error("评论动态失败");
        }

        return ResData.ok();
    }

    /**
     * 删除评论
     * @param jsonParams
     * @return
     */
    @RequestMapping(value = "/deleteComment", method = RequestMethod.DELETE)
    public ResData deleteComment(@RequestBody String jsonParams) {
        JSONObject joinData = JSONObject.parseObject(jsonParams).getJSONObject("data");

        if (!joinData.containsKey("userId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "用户id不可为空");
        }
        if (!joinData.containsKey("articleCommentId")) {
            return ResData.error(Code.PARAM_FORMAT_ERROR, "动态id不可为空");
        }

        String userId = joinData.getString("userId");
        String articleCommentId = joinData.getString("articleCommentId");

        boolean commented = articleService.validateCommented(userId, articleCommentId);
        if (!commented) {
            return ResData.error(Code.DATA_NOT_FOUND, "您未对该动态发表过评论");
        }

        boolean result = articleService.deleteComment(articleCommentId);
        if (!result) {
            return ResData.error(Code.DATABASE_UPDATE_FAIL, "删除评论失败");
        }

        return ResData.ok();
    }
}
