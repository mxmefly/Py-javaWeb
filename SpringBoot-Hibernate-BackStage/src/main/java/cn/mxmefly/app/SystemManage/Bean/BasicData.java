package cn.mxmefly.app.SystemManage.Bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class BasicData {
    @Id
    @GeneratedValue
    int id;
    String name;
    String type;
    String parent;
    float sentiments;
    int   value;
    Date date;
    public BasicData(){

    }
    public BasicData(String name,String type,String parent,int value ,Date date){
        this.id=id;
        this.date=date;
        this.type=type;
        this.name=name;
        this.parent=parent;
        this.value=value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public float getSentiments() {
        return sentiments;
    }

    public void setSentiments(float sentiments) {
        this.sentiments = sentiments;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
