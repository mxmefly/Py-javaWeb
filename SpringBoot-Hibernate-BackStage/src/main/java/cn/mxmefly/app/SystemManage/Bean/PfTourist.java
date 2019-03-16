package cn.mxmefly.app.SystemManage.Bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class PfTourist {
    @Id
    @GeneratedValue
    int id;
    String ip;
    String osInfo;
    String bowerInfo;
    Date  date;
    public PfTourist(){

    }
    public PfTourist(String ip, String osInfo, String bowerInfo, Date date){
        this.ip=ip;
        this.date=date;
        this.bowerInfo=bowerInfo;
        this.osInfo=osInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOsInfo() {
        return osInfo;
    }

    public void setOsInfo(String osInfo) {
        this.osInfo = osInfo;
    }

    public String getBowerInfo() {
        return bowerInfo;
    }

    public void setBowerInfo(String bowerInfo) {
        this.bowerInfo = bowerInfo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
