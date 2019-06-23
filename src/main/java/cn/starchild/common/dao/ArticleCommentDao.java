package cn.starchild.common.dao;

import cn.starchild.common.model.ArticleCommentModel;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface ArticleCommentDao extends Mapper<ArticleCommentModel> {
    ArticleCommentModel selectCommentByIdAndUserId(ArticleCommentModel articleCommentModel);

    void deleteComment(ArticleCommentModel articleCommentModel);

    List<Map<String, String>> selectCommentList(String articleId);
}