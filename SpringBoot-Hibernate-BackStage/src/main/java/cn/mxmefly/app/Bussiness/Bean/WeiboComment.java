package cn.mxmefly.app.Bussiness.Bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "weibo_comment")
public class WeiboComment {
    @Id
    @GeneratedValue
    int id;
    String commentUserId;
    String content;
    String weiboUrl;
    String createdAt;
    int crawlTime;
    int isprocess;
    float sentiments;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWeiboUrl() {
        return weiboUrl;
    }

    public void setWeiboUrl(String weiboUrl) {
        this.weiboUrl = weiboUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getCrawlTime() {
        return crawlTime;
    }

    public void setCrawlTime(int crawlTime) {
        this.crawlTime = crawlTime;
    }

    public int getIsprocess() {
        return isprocess;
    }

    public void setIsprocess(int isprocess) {
        this.isprocess = isprocess;
    }

    public float getSentiments() {
        return sentiments;
    }

    public void setSentiments(float sentiments) {
        this.sentiments = sentiments;
    }
}
