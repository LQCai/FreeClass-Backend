package cn.starchild.common.dao;

import cn.starchild.common.model.ArticleModel;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface ArticleDao extends Mapper<ArticleModel> {

    ArticleModel selectArticleById(String articleId);

    Map<String, String> selectArticleDetail(String articleId);

    List<Map<String, Object>> getArticleCommentList(@Param("articleIdList") List<String> articleIdList);

    List<Map<String, Object>> getArticleList(int index);

    List<Map<String, Object>> getArticleCollectList(@Param("articleIdList") List<String> articleIdList);
}