package cn.mxmefly.app.Common.LinearRegression;

public class DataNode {
    private double x;
    private double y;
    public DataNode(double x,double y){
        this.x=x;
        this.y=y;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getXY(){
        return this.x * this.y;
    }
}
