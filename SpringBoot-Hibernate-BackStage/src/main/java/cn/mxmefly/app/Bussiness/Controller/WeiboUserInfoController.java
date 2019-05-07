package cn.mxmefly.app.Bussiness.Controller;

import cn.mxmefly.app.Bussiness.Bean.WeiboBaseData;
import cn.mxmefly.app.Bussiness.Bean.WeiboInfo;
import cn.mxmefly.app.Bussiness.Bean.WeiboUserInfo;
import cn.mxmefly.app.Bussiness.Bean.WordsCounts;
import cn.mxmefly.app.Bussiness.Dao.Repository.WeiboBaseDataRepository;
import cn.mxmefly.app.Bussiness.Dao.Repository.WeiboInfoRepository;
import cn.mxmefly.app.Bussiness.Dao.Repository.WeiboUserInfoReposity;
import cn.mxmefly.app.Common.CreateResult;
import cn.mxmefly.app.Common.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class WeiboUserInfoController {
    @Autowired
    private WeiboUserInfoReposity weiboUserInfoReposity;
    @Autowired
    private WeiboInfoRepository weiboInfoRepository;
    @Autowired
    private WeiboBaseDataRepository weiboBaseDataRepository;

    CreateResult createResult = new CreateResult();

    @PostMapping("/getUserHotData")
    public Results getUserHotData(@RequestBody Map map){
        String t1=(String) map.get("startTime");
        String t2=(String) map.get("endTime");
        List<Map> data = new ArrayList<>();
        List<WeiboUserInfo> bigVlist = weiboUserInfoReposity.findAllBigV();
        for(int i=0;i<bigVlist.size();i++){
             Map m= new HashMap();
             m.put("weiboUsr",bigVlist.get(i));
             float value= (float) 0;
             try {
                 value=weiboUserInfoReposity.getUserHotData(bigVlist.get(i).get_id(),t1,t2);
                 //long count=weiboInfoRepository.countByUserIdAndCreatedAtBetween(bigVlist.get(i).get_id(),t1,t2);
                 m.put("hotData",value);
                 data.add(m);
             }catch (Exception e){
                 value=0;
             }

        }
        Collections.sort(data, new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                if((float)o2.get("hotData")-(float)o1.get("hotData")>0){
                    return 1;
                }else{
                    return -1;
                }
            }
        });
        return createResult.getResults(data);
    }

    @PostMapping("/getUserPortrait")
    public Results getUserPortrait(@RequestBody Map map){
        String id=(String) map.get("id");
        String t1=(String) map.get("startTime");
        String t2=(String) map.get("endTime");
        WeiboUserInfo weiboUserInfo = weiboUserInfoReposity.findBy_id(id);
        String[] labelRes=weiboUserInfo.getLabels().split(",");
        List<WeiboBaseData> weiboBaseDataList = weiboBaseDataRepository.findByUserIdOrUpUserAndDateBetween(id,id,t1,t2);
        Map<String,Integer> wordsCount = new HashMap<>();
        for(int i=0;i<weiboBaseDataList.size();i++){
            WeiboBaseData weiboBaseData = weiboBaseDataList.get(i);
            if(!isInRes(labelRes,weiboBaseData.getName())){
                if(wordsCount.containsKey(weiboBaseData.getName())){
                    wordsCount.put(weiboBaseData.getName(),(int)wordsCount.get(weiboBaseData.getName())+weiboBaseData.getCounts());
                }else{
                    wordsCount.put(weiboBaseData.getName(),weiboBaseData.getCounts());
                }
            }
        }
        //map 入list
        List<WordsCounts> wordsCountsList =new ArrayList<>();
        for (Map.Entry<String, Integer> entry : wordsCount.entrySet()){
            wordsCountsList.add(new WordsCounts(entry.getKey(),entry.getValue()));
        }
        //排序
        Collections.sort(wordsCountsList, new Comparator<WordsCounts>() {
            @Override
            public int compare(WordsCounts o1, WordsCounts o2) {
                return o2.getCount()-o1.getCount();
            }
        });
        WordsCounts wordsCounts=wordsCountsList.get(0);
        List wordsCountsList1= getListByOrder(wordsCountsList,40);
        for(int i=0;i<labelRes.length;i++){
            wordsCountsList1.add(new WordsCounts(labelRes[i],wordsCounts.getCount()+10));
        }
        return createResult.getResults(wordsCountsList1);
    }
    private Boolean isInRes(String[] stringArray ,String str){
        List<String> tempList = Arrays.asList(stringArray);
        return tempList.contains(str);
    }
    private List getListByOrder(List list,int num){
        if(list.size()<num){
            return list;
        }else {
            List objects = new ArrayList<>();
            for(int i=0;i<num;i++){
                objects.add(list.get(i));
            }
            return objects;
        }
    }

    @PostMapping("/getFansInfo")
    public Results getFansInfo(@RequestBody Map map){
        String id=(String) map.get("id");
        Map<String,Integer> locationMap = new HashMap<>();
        Map<String,Integer> genderOrAgeMap= new HashMap<>();
        genderOrAgeMap.put("其他",0);
        genderOrAgeMap.put("70后",0);
        genderOrAgeMap.put("80后",0);
        genderOrAgeMap.put("90后",0);
        genderOrAgeMap.put("00后",0);
        List<WeiboUserInfo> weiboUserInfos = weiboUserInfoReposity.findFansByFollowedId(id);
        for(int i=0;i<weiboUserInfos.size();i++){
            WeiboUserInfo weiboUserInfo = weiboUserInfos.get(i);
            if(locationMap.containsKey(weiboUserInfo.getProvince())){
                locationMap.put(weiboUserInfo.getProvince(),locationMap.get(weiboUserInfo.getProvince())+1);
            }else {
                locationMap.put(weiboUserInfo.getProvince(),1);
            }
            if(genderOrAgeMap.containsKey(weiboUserInfo.getGender())){
                genderOrAgeMap.put(weiboUserInfo.getGender(),genderOrAgeMap.get(weiboUserInfo.getGender())+1);
            }else {
                genderOrAgeMap.put(weiboUserInfo.getGender(),1);
            }
            if(weiboUserInfo.getBirthday().length()==10){
                if(weiboUserInfo.getBirthday().compareTo("1970-01-01")<0){
                    genderOrAgeMap.put("其他",genderOrAgeMap.get("其他")+1);
                }else if(weiboUserInfo.getBirthday().compareTo("1970-01-01")>=0&&weiboUserInfo.getBirthday().compareTo("1980-01-01")<0){
                    genderOrAgeMap.put("70后",genderOrAgeMap.get("70后")+1);
                }else if(weiboUserInfo.getBirthday().compareTo("1980-01-01")>=0&&weiboUserInfo.getBirthday().compareTo("1990-01-01")<0){
                    genderOrAgeMap.put("80后",genderOrAgeMap.get("80后")+1);
                }else if(weiboUserInfo.getBirthday().compareTo("1990-01-01")>=0&&weiboUserInfo.getBirthday().compareTo("2000-01-01")<0){
                    genderOrAgeMap.put("90后",genderOrAgeMap.get("90后")+1);
                }else{
                    genderOrAgeMap.put("00后",genderOrAgeMap.get("00后")+1);
                }
            }
        }
        List<Map> locationList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : locationMap.entrySet()){
            Map tempMap = new HashMap();
            tempMap.put("位置",entry.getKey());
            tempMap.put("人数",entry.getValue());
            locationList.add(tempMap);
        }
        List<Map> genderOrAgeList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : genderOrAgeMap.entrySet()){
            Map tempMap = new HashMap();
            tempMap.put("数据",entry.getKey());
            tempMap.put("人数",entry.getValue());
            genderOrAgeList.add(tempMap);
        }
        Map returnMap = new HashMap();
        returnMap.put("locationList",locationList);
        returnMap.put("genderOrAgeList",genderOrAgeList);
        return createResult.getResults(returnMap);
    }

    @PostMapping("getUserWeiboSetimentData")
    public Results getUserWeiboSetimentData(@RequestBody Map map){
        String id=(String) map.get("id");
        String t1=(String) map.get("startTime");
        String t2=(String) map.get("endTime");
        List<WeiboInfo> weiboInfoList=weiboInfoRepository.findByUserIdAndCreatedAtBetween(id,t1,t2);
        float sum=0;
        int []counts={0,0,0,0,0,0,0,0,0,0,0};
        for(int i=0;i<weiboInfoList.size();i++){
            WeiboInfo weiboInfo = weiboInfoList.get(i);
            sum+=weiboInfo.getSentiments();
            int value = Math.round(weiboInfo.getSentiments());
            counts[value]++;
        }
        List<Map> maps = new ArrayList<>();
        for(int i=0;i<11;i++){
            Map tempMap = new HashMap();
            tempMap.put("情感值",i);
            tempMap.put("出现次数",counts[i]);
            maps.add(tempMap);
        }
        Map returnMap=new HashMap();
        returnMap.put("dataList",maps);
        returnMap.put("avg",sum/weiboInfoList.size());
        return createResult.getResults(returnMap);
    }
}
