package cn.starchild.user.service;

import cn.starchild.common.model.ArticleModel;

import java.util.List;
import java.util.Map;

public interface ArticleService {
    boolean postArticle(ArticleModel articleModel);

    List<Map<String, Object>> getArticleList(String pageIndex, String userId);

    boolean validateArticle(String articleId);

    boolean collectArticle(String articleId, String userId);

    boolean validateCollected(String userId, String articleId);

    boolean cancelCollect(String userId, String articleId);

    boolean commentArticle(String articleId, String userId, String content);

    boolean validateCommented(String userId, String articleCommentId);

    boolean deleteComment(String articleCommentId);

    Map<String, Object> getArticleInfo(String articleId);
}
