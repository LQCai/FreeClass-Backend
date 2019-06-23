package cn.starchild.common.dao;

import cn.starchild.common.model.ArticleCollectionModel;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface ArticleCollectionDao extends Mapper<ArticleCollectionModel> {
    ArticleCollectionModel selectCollectionByIdAndUserId(ArticleCollectionModel articleCollectionModel);

    void cancelCollect(ArticleCollectionModel articleCollection);

    List<Map<String, Object>> getMyCollectList(String userId);
}