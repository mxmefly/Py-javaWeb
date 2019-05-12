package cn.mxmefly.app.Bussiness.Controller;

import cn.mxmefly.app.Bussiness.Bean.TempWord;
import cn.mxmefly.app.Bussiness.Bean.WeiboBaseData;
import cn.mxmefly.app.Bussiness.Dao.Repository.TempWordRepository;
import cn.mxmefly.app.Bussiness.Dao.Repository.WeiboBaseDataRepository;
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

public class WeiboBaseDataController {
    @Autowired
    private WeiboBaseDataRepository weiboBaseDataRepository;

    @Autowired
    private TempWordRepository tempWordRepository;

    private CreateResult createResult = new CreateResult();

    @PostMapping("/getPassYearHotWord")
    public Results getPassYearHotWord(@RequestBody Map map){
        List<TempWord> wordsList2017= tempWordRepository.findByName("2017年度");
        List<TempWord> wordsList2018= tempWordRepository.findByName("2018年度");
        List<TempWord> wordsList2019= tempWordRepository.findByName("2019年");
        Map returnMap = new HashMap();
        returnMap.put("wordsList2017",wordsList2017);
        returnMap.put("wordsList2018",wordsList2018);
        returnMap.put("wordsList2019",wordsList2019);
        return createResult.getResults(returnMap);
    }

    @PostMapping("/getHotBaseData")
    public Results getHotBaseData(@RequestBody Map map){
        String t1=(String) map.get("startTime");
        String t2=(String) map.get("endTime");
        String type=(String)map.get("type");
        return createResult.getResults(weiboBaseDataRepository.getHotBaseDataBytime(type,t1,t2));
    }

    @PostMapping("/getBaseDataUser")
    public Results getBaseDataUser(@RequestBody Map map){
        String name=(String)map.get("name");
        String type=(String)map.get("type");
        String t1=(String) map.get("startTime");
        String t2=(String) map.get("endTime");
        return  createResult.getResults(weiboBaseDataRepository.getWBaseDataUser(name,type,t1,t2));
    }

    @PostMapping("/getWordHotLine")
    public Results getWordHotLine(@RequestBody Map map) throws ParseException {
        String name=(String)map.get("name");
        String type=(String)map.get("type");
        String t1=(String) map.get("startTime");
        String t2=(String) map.get("endTime");
        List<WeiboBaseData> weiboBaseDataList = weiboBaseDataRepository.findByNameAndTypeAndDateBetween(name,type,t1,t2);
        ArrayList<String> dateList = new GeneralMethod().getDateArrBySAndE(t1,t2);
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
            for(int j=0;j<weiboBaseDataList.size();j++){
                WeiboBaseData weiboBaseData = weiboBaseDataList.get(j);
                if(weiboBaseData.getDate().equals(map1.get("时间"))){
                    value+=weiboBaseData.getCounts();
                }
            }
            map1.put("热度",value);
            doubles.add((double)value);
            maps.set(i,map1);
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
