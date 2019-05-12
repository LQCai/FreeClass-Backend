package cn.starchild.common.dao;

import cn.starchild.common.model.ArticleCollectionModel;
import tk.mybatis.mapper.common.Mapper;

public interface ArticleCollectionDao extends Mapper<ArticleCollectionModel> {
    ArticleCollectionModel selectCollectionByIdAndUserId(ArticleCollectionModel articleCollectionModel);

    void cancelCollect(ArticleCollectionModel articleCollection);
}