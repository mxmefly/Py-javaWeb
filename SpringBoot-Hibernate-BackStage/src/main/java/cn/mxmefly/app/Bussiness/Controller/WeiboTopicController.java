package cn.mxmefly.app.Bussiness.Controller;

import cn.mxmefly.app.Bussiness.Bean.WeiboInfo;
import cn.mxmefly.app.Bussiness.Dao.Repository.WeiboInfoRepository;
import cn.mxmefly.app.Bussiness.Dao.Repository.WeiboTopicRepository;
import cn.mxmefly.app.Common.CreateResult;
import cn.mxmefly.app.Common.GeneralMethod;
import cn.mxmefly.app.Common.LinearRegression.MyLinearRegression;
import cn.mxmefly.app.Common.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class WeiboTopicController {
    @Autowired
    private WeiboTopicRepository weiboTopicRepository;
    @Autowired
    private WeiboInfoRepository weiboInfoRepository;

    private CreateResult createResult = new CreateResult();

    @PostMapping("/getTopicOderData")
    public Results getTopicOderData(@RequestBody Map map){
        String t1=(String) map.get("startTime");
        String t2=(String) map.get("endTime");
        return  createResult.getResults(weiboTopicRepository.getHotTopicOrder(t1,t2));
    }
    @PostMapping("/getTopicUser")
    public Results getTopicUser(@RequestBody Map map){
        String name= (String) map.get("name");
        String t1=(String) map.get("startTime");
        String t2=(String) map.get("endTime");
        return  createResult.getResults(weiboTopicRepository.getHotTopicUser(name,t1,t2));
    }

    @PostMapping("/getTopicUserWeiboInfo")
    public Results getTopicUserWeiboInfo(@RequestBody Map map){
        String topic= (String) map.get("topic");
        String t1=(String) map.get("startTime");
        String t2=(String) map.get("endTime");
        String userId=(String)map.get("userId");
        return createResult.getResults(weiboInfoRepository.getByTopicAndUserIdAndTime(topic,t1,t2,userId));
    }

    @PostMapping("/getTopicHotlineDate")
    public Results getUserHotlineDate(@RequestBody Map map) throws ParseException {
        String topic=(String) map.get("topic");
        String t1=(String) map.get("startTime");
        String t2=(String) map.get("endTime");
        ArrayList<String> dateList = new GeneralMethod().getDateArrBySAndE(t1,t2);
        Map<String, List<Float>> dateMap=new HashMap<>();
        for(int i=0;i<dateList.size();i++){
            List<Float> floats = new ArrayList<>();
            dateMap.put(dateList.get(i),floats);
        }
        List<WeiboInfo> weiboInfoList =weiboInfoRepository.getByTopicWeiboInfo(topic,t1,t2);
        for(int i=0;i<weiboInfoList.size();i++){
            WeiboInfo weiboInfo = weiboInfoList.get(i);
            String dateStr=weiboInfo.getCreatedAt().substring(0,10);
            List<Float> floats = dateMap.get(dateStr);
            try {
                floats.add((float)(weiboInfo.getRepostNum()*1.2+weiboInfo.getLikeNum()*1.0+weiboInfo.getCommentNum()*1.1));
                dateMap.put(dateStr,floats);
            }catch (Exception e){
                continue;
            }
        }
        List<Map> maps = new ArrayList<>();
        for(int i=0;i<dateList.size();i++){
            Map map1 = new HashMap<>();
            map1.put("时间",dateList.get(i));
            map1.put("热度",0);
            maps.add(map1);
        }
        List<Double> doubles = new ArrayList<>();
        for(int i=0;i<maps.size();i++){
            Map map1 = maps.get(i);
            float value=0;
            for (Map.Entry<String, List<Float>> entry : dateMap.entrySet()){
                if(entry.getKey()==map1.get("时间")){
                    if(entry.getValue().size()==0){
                        if(i!=0){
                            value=(float)maps.get(i-1).get("热度");
                        }
                    }else {
                        float sum = 0;
                        for(int j=0;j<entry.getValue().size();j++){
                            sum+=entry.getValue().get(j);
                        }
                        value = sum/entry.getValue().size();
                    }
                    map1.put("热度",value);
                    doubles.add((double)value);
                    maps.set(i,map1);
                    break;
                }
            }
        }
        Map lastMap= maps.get(maps.size()-1);
        lastMap.put("预测",lastMap.get("热度"));
        maps.remove(maps.size()-1);
        maps.add(lastMap);
        MyLinearRegression myLinearRegression = new MyLinearRegression(doubles);
        List<Double> doubleList=myLinearRegression.getPrediction(5);
        for(int i=0;i<5;i++){
            Map map1 = new HashMap<>();
            map1.put("时间","预测"+(i+1));
            map1.put("预测",doubleList.get(i));
            maps.add(map1);
        }
        return createResult.getResults(maps);
    }
}
