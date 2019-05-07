package cn.mxmefly.app.Common.LinearRegression;

import java.util.ArrayList;
import java.util.List;

public class MyLinearRegression {
    private List<DataNode> list;
    private double alpha;
    private double beta;
    private double r;
    /*构造函数 给定训练集 训练出 alpha，beta*/
    public MyLinearRegression(List<Double> doubles) {
        this.list = new ArrayList<DataNode>();
        for(int i=0;i<doubles.size();i++){
            this.list.add(new DataNode(i,doubles.get(i)));
        }
        this.getAB();
    }
    public double getAlpha() {
        return alpha;
    }
    public double getBeta() {
        return beta;
    }
    public double getR(){
        return this.r;
    }

    public List<DataNode> getList() {
        return list;
    }

    public void setList(List<DataNode> list) {
        this.list = list;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public void setR(double r) {
        this.r = r;
    }

    private void getAB(){
        int n = list.size();
        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumX2 = 0;
        for (DataNode dataNode : list){
            sumX += dataNode.getX();
            sumY += dataNode.getY();
            sumXY += dataNode.getXY();
            sumX2 += Math.pow(dataNode.getX(), 2);
        }
        this.alpha = (((sumY * sumX) / n) - sumXY) / (((sumX * sumX) / n) - sumX2);
        this.beta = (sumY - this.alpha * sumX) / n;
    }
    /*r 用来说明误差 r2越接近1 误差越小*/
    public void getR2(){
        double num = 0;
        double den = 0;
        double sumY = 0;
        for (DataNode dataNode : list){
            sumY += dataNode.getY();
        }
        double avgY = sumY / list.size();
        for (DataNode dataNode : list){
            num += Math.pow((dataNode.getY() - (dataNode.getX() * this.alpha + beta)), 2);
            den += Math.pow((dataNode.getY() - avgY), 2);
        }
        this.r = 1 - (num / den);
    }
    public List<Double> getPrediction(int num){
        List<Double> doubles= new ArrayList<>();
        //System.err.println("this.getAlpha()"+this.getAlpha());
        //System.err.println("this.getBeta()"+this.getBeta());
        for(int i=0;i<num;i++){
            double value=this.getAlpha()*(this.list.size()+i)+this.getBeta();
            doubles.add(value);
            /*this.list.add(new DataNode(this.list.size(),value));
            this.getAB();*/
        }
        return doubles;
    }
}
