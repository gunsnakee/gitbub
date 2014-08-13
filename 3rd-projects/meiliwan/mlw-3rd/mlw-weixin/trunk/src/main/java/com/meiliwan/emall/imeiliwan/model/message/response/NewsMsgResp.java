package com.meiliwan.emall.imeiliwan.model.message.response;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-12-9
 * Time: 上午11:24
 *  图文消息-发送被动响应消息
 */
public class NewsMsgResp extends BaseMsgResp{
    // 	图文消息个数，限制为10条以内
    private String ArticleCount;
    //图文消息集合
    private List<Article> Articles;

    public String getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(String articleCount) {
        ArticleCount = articleCount;
    }

    public List<Article> getArticles() {
        return Articles;
    }

    public void setArticles(List<Article> articles) {
        Articles = articles;
    }
}
