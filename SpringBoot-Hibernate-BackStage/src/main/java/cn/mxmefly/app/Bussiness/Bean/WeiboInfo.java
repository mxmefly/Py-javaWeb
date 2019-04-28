package cn.mxmefly.app.Bussiness.Bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "weibo_info")
public class WeiboInfo {
    @Id
    @GeneratedValue
    int id;
    String weiboUrl;
    String userId;
    String content;
    String createdAt;
    int repostNum;
    int commentNum;
    int likeNum;
    int crawlTime;
    int isprocess;
    float sentiments;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeiboUrl() {
        return weiboUrl;
    }

    public void setWeiboUrl(String weiboUrl) {
        this.weiboUrl = weiboUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getRepostNum() {
        return repostNum;
    }

    public void setRepostNum(int repostNum) {
        this.repostNum = repostNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
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
