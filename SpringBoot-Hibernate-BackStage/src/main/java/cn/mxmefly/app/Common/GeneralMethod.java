package cn.mxmefly.app.Common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    public ArrayList<String> getDateArrBySAndE(String startStr,String endStr) throws ParseException {
        ArrayList<String> arrayList = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = dateFormat1.parse(startStr);
        Date endDate = dateFormat1.parse(endStr);
        Date date = beginDate;
        while (!date.equals(endDate)) {
            arrayList.add(dateFormat1.format(date));
            c.setTime(date);
            c.add(Calendar.DATE, 1); // 日期加1天
            date = c.getTime();
        }
        arrayList.add(endStr);
        return  arrayList;
    }

}
