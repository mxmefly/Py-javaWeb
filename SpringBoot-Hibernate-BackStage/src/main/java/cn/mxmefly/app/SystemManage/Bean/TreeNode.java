package cn.mxmefly.app.SystemManage.Bean;

import java.util.List;

public class TreeNode {
    String id;
    String name;
    int isparent;
    List<TreeNode> children;
    String icon;
    String path;
    String des;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsparent() {
        return isparent;
    }

    public void setIsparent(int isparent) {
        this.isparent = isparent;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
