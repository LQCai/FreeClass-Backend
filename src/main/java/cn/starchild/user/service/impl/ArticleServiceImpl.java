package cn.starchild.user.service.impl;

import cn.starchild.common.dao.ArticleCollectionDao;
import cn.starchild.common.dao.ArticleCommentDao;
import cn.starchild.common.dao.ArticleDao;
import cn.starchild.common.model.ArticleCollectionModel;
import cn.starchild.common.model.ArticleCommentModel;
import cn.starchild.common.model.ArticleModel;
import cn.starchild.common.util.UUIDUtils;
import cn.starchild.user.service.ArticleService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleDao articleDao;
    @Resource
    private ArticleCollectionDao articleCollectionDao;
    @Resource
    private ArticleCommentDao articleCommentDao;

    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public boolean postArticle(ArticleModel articleInfo) {
        articleInfo.setId(UUIDUtils.uuid());
        articleInfo.setCreated(new Date());
        articleInfo.setModified(new Date());
        articleInfo.setStatus((byte) 1);

        try {
            articleDao.insert(articleInfo);
        } catch (Exception e) {
            logger.error("发布动态失败:" + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public List<Map<String, Object>> getArticleList(String pageIndex) {
        List<Map<String, Object>> articleList = new ArrayList<>();

        int index = Integer.parseInt(pageIndex) * 10;

        List<Map<String, Object>> resultList = articleDao.getArticleList(index);
        for (Map<String, Object> result :
                resultList) {
            Map<String, Object> article = new HashMap<>();
            article.put("id", result.get("id"));
            article.put("name", result.get("name"));
            article.put("content", result.get("content"));
            article.put("createTime", result.get("created"));

            String imageString = result.get("image_url_array").toString();
            if (!imageString.equals("") && !imageString.equals("[]")) {
                imageString = imageString.substring(1, imageString.length() - 1);
                String[] images = imageString.split(",");
                List<String> imageUrls = new ArrayList<>();
                for (String image : images) {
                    image = image.substring(1, image.length() - 1);
                    imageUrls.add(image);
                }
                article.put("images", imageUrls);
            } else {
                article.put("images", new Arrays[0]);
            }

            articleList.add(article);
        }

        return articleList;
    }

    @Override
    public boolean validateArticle(String articleId) {
        ArticleModel article = articleDao.selectArticleById(articleId);
        if (article == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean collectArticle(String articleId, String userId) {
        ArticleCollectionModel articleCollection = new ArticleCollectionModel();
        articleCollection.setId(UUIDUtils.uuid());
        articleCollection.setArticleId(articleId);
        articleCollection.setUserId(userId);
        articleCollection.setStatus((byte) 1);
        articleCollection.setCreated(new Date());
        articleCollection.setModified(new Date());

        try {
            articleCollectionDao.insert(articleCollection);
        } catch (Exception e) {
            logger.error("收藏动态失败:" + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean validateCollected(String userId, String articleId) {
        ArticleCollectionModel articleCollectionModel = new ArticleCollectionModel();
        articleCollectionModel.setUserId(userId);
        articleCollectionModel.setArticleId(articleId);

        ArticleCollectionModel collected = articleCollectionDao.selectCollectionByIdAndUserId(articleCollectionModel);
        if (collected == null) {
            return false;
        }

        return true;
    }

    @Override
    public boolean cancelCollect(String userId, String articleId) {
        ArticleCollectionModel articleCollection = new ArticleCollectionModel();
        articleCollection.setUserId(userId);
        articleCollection.setArticleId(articleId);
        articleCollection.setStatus((byte) 2);
        articleCollection.setModified(new Date());

        try {
            articleCollectionDao.cancelCollect(articleCollection);
        } catch (Exception e) {
            logger.error("取消收藏失败:" + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean commentArticle(String articleId, String userId, String content) {
        ArticleCommentModel articleComment = new ArticleCommentModel();
        articleComment.setId(UUIDUtils.uuid());
        articleComment.setArticleId(articleId);
        articleComment.setComment(content);
        articleComment.setCreated(new Date());
        articleComment.setModified(new Date());
        articleComment.setUserId(userId);
        articleComment.setStatus((byte) 1);

        try {
            articleCommentDao.insert(articleComment);
        } catch (Exception e) {
            logger.error("评论动态失败:" + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean validateCommented(String userId, String articleCommentId) {
        ArticleCommentModel articleCommentModel = new ArticleCommentModel();
        articleCommentModel.setUserId(userId);
        articleCommentModel.setId(articleCommentId);

        ArticleCommentModel articleComment = articleCommentDao.selectCommentByIdAndUserId(articleCommentModel);
        if (articleComment == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteComment(String articleCommentId) {
        ArticleCommentModel articleCommentModel = new ArticleCommentModel();
        articleCommentModel.setId(articleCommentId);
        articleCommentModel.setModified(new Date());
        articleCommentModel.setStatus((byte) 2);

        try {
            articleCommentDao.deleteComment(articleCommentModel);
        } catch (Exception e) {
            logger.error("删除评论失败:" + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public Map<String, Object> getArticleInfo(String articleId) {
        Map<String, String> articleResult = articleDao.selectArticleDetail(articleId);
        if (articleResult == null || articleResult.get("id") == null) {
            return null;
        }

        List<Map<String, String>> articleCommentList = articleCommentDao.selectCommentList(articleId);

        Map<String, Object> articleInfo = new HashMap<>();
        articleInfo.put("id", articleResult.get("id"));
        articleInfo.put("content", articleResult.get("content"));
        articleInfo.put("images", articleResult.get("image_url_array"));
        articleInfo.put("createTime", articleResult.get("created"));
        articleInfo.put("collectionCount", articleResult.get("article_collection_count"));
        articleInfo.put("commentCount", articleResult.get("article_comment_count"));

        articleInfo.put("commentList", articleCommentList);

        return articleInfo;
    }
}
