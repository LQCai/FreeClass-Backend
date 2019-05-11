package cn.starchild.user.service;

import cn.starchild.common.model.ArticleModel;

import java.util.List;
import java.util.Map;

public interface ArticleService {
    boolean postArticle(ArticleModel articleModel);

    List<Map<String, Object>> getArticleList();
}
