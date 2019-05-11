package cn.starchild.common.dao;

import cn.starchild.common.model.ArticleModel;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface ArticleDao extends Mapper<ArticleModel> {
    List<Map<String, Object>> getArticleList();
}