package cn.mxmefly.app.SystemManage.Bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class PfConfig {
    @Id
    @GeneratedValue
    int id;
    String name;
    String strVal1;
    String strVal2;
    String strVal3;
    int    intVal1;
    int    intVal2;
    int    intVal3;
    Date   dateVal1;
    Date   dateval2;

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

    public String getStrVal1() {
        return strVal1;
    }

    public void setStrVal1(String strVal1) {
        this.strVal1 = strVal1;
    }

    public String getStrVal2() {
        return strVal2;
    }

    public void setStrVal2(String strVal2) {
        this.strVal2 = strVal2;
    }

    public String getStrVal3() {
        return strVal3;
    }

    public void setStrVal3(String strVal3) {
        this.strVal3 = strVal3;
    }

    public int getIntVal1() {
        return intVal1;
    }

    public void setIntVal1(int intVal1) {
        this.intVal1 = intVal1;
    }

    public int getIntVal2() {
        return intVal2;
    }

    public void setIntVal2(int intVal2) {
        this.intVal2 = intVal2;
    }

    public int getIntVal3() {
        return intVal3;
    }

    public void setIntVal3(int intVal3) {
        this.intVal3 = intVal3;
    }

    public Date getDateVal1() {
        return dateVal1;
    }

    public void setDateVal1(Date dateVal1) {
        this.dateVal1 = dateVal1;
    }

    public Date getDateval2() {
        return dateval2;
    }

    public void setDateval2(Date dateval2) {
        this.dateval2 = dateval2;
    }
}
