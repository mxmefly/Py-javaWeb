package cn.mxmefly.app.SystemManage.Bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PfMenu {
    @Id
    @GeneratedValue
    String  id;

    String name;
    String parentId;
    int isParent;
    int isNewwin;
    String Icon;
    String des;
    String path;
    int menuOrder;


    public PfMenu(){

    }

    public int getOrder() {
        return menuOrder;
    }

    public void setOrder(int menuOrder) {
        this.menuOrder = menuOrder;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getIsParent() {
        return isParent;
    }

    public void setIsParent(int isParent) {
        this.isParent = isParent;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getIsNewwin() {
        return isNewwin;
    }

    public void setIsNewwin(int isNewwin) {
        this.isNewwin = isNewwin;
    }

    public String getDesc() {
        return des;
    }

    public void setDesc(String desc) {
        this.des = desc;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
