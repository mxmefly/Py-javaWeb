package cn.mxmefly.app.Common.Log.Bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class PfLog {
    @Id
    @GeneratedValue
    int  id;
    String type;
    String content;
    String user;
    Date time;
    public PfLog(){

    }
    public PfLog(String user,String content,String type ,Date time){
        this.time =time;
        this.user =user;
        this.content=content;
        this.type=type;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
