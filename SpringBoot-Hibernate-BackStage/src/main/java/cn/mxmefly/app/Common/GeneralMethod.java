package cn.mxmefly.app.Common;

import cn.mxmefly.app.Common.Arima.ARIMA;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/*md5 加密*/
public class GeneralMethod {
    public  String md5Password(String password) {
        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }
            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
    public String getRandomStr(){
        long time=new Date().getTime();
        return Integer.toHexString((int)time);
    }
    /*获取过去几天*/
    public  ArrayList<String> getPastDateList(int intervals ) {
        ArrayList<String> pastDaysList = new ArrayList<>();
        for (int i = 0; i <intervals; i++) {
            pastDaysList.add(getPastDate(i));
        }
        ArrayList<String> stringList = new ArrayList<>();
        for(int i=0;i<intervals;i++){
            stringList.add(pastDaysList.get(intervals-i-1));
        }
        return stringList;
    }
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }
    public List<Double> getArimaData(List<Double> data,int num){
        List<Double> doubleList = new ArrayList<>();
        for(int i=0;i<num;i++){
            double[] dataArray=new double[data.size()];
            for(int j=0;j<data.size();j++)
                dataArray[j]=data.get(j);
            ARIMA arima = new ARIMA(dataArray);
            int []model=arima.getARIMAmodel();
            double arimaValue=arima.aftDeal(arima.predictValue(model[0],model[1]));
            doubleList.add(arimaValue);
            data.add(arimaValue);
        }
        return doubleList;
    }
}
