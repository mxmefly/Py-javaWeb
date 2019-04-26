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
    String _id;
    String commentUserId;
    String content;
    String weiboUrl;
    String createdAt;
    int crawlTime;
    int isProcess;
    float sentiments;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public int getIsProcess() {
        return isProcess;
    }

    public void setIsProcess(int isProcess) {
        this.isProcess = isProcess;
    }

    public float getSentiments() {
        return sentiments;
    }

    public void setSentiments(float sentiments) {
        this.sentiments = sentiments;
    }
}
