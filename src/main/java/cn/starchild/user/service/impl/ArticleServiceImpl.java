package cn.starchild.user.service.impl;

import cn.starchild.common.dao.ArticleDao;
import cn.starchild.common.model.ArticleModel;
import cn.starchild.common.util.UUIDUtils;
import cn.starchild.user.service.ArticleService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleDao articleDao;

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
    public List<Map<String, Object>> getArticleList() {
//        List<Map<String, Object>> articleList = new ArrayList<>();

        List<Map<String, Object>> resultList = articleDao.getArticleList();
        return resultList;
    }
}
