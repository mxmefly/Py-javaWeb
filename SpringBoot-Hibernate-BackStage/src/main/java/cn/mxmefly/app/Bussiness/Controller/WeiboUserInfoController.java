package cn.mxmefly.app.Bussiness.Controller;

import cn.mxmefly.app.Bussiness.Bean.*;
import cn.mxmefly.app.Bussiness.Dao.Repository.WeiboBaseDataRepository;
import cn.mxmefly.app.Bussiness.Dao.Repository.WeiboCommentReposity;
import cn.mxmefly.app.Bussiness.Dao.Repository.WeiboInfoRepository;
import cn.mxmefly.app.Bussiness.Dao.Repository.WeiboUserInfoReposity;
import cn.mxmefly.app.Common.CreateResult;
import cn.mxmefly.app.Common.GeneralMethod;
import cn.mxmefly.app.Common.LinearRegression.MyLinearRegression;
import cn.mxmefly.app.Common.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.*;

@RestController
public class WeiboUserInfoController {
    @Autowired
    private WeiboUserInfoReposity weiboUserInfoReposity;
    @Autowired
    private WeiboInfoRepository weiboInfoRepository;
    @Autowired
    private WeiboBaseDataRepository weiboBaseDataRepository;
    @Autowired
    private WeiboCommentReposity weiboCommentReposity;

    CreateResult createResult = new CreateResult();

    @PostMapping("/getWeiboUserInfo")
    public Results getWeiboUserInfo(@RequestBody Map map){
        String id = (String) map.get("id");
        return createResult.getResults(weiboUserInfoReposity.findBy_id(id));
    }

    @PostMapping("/getUserHotData")
    public Results getUserHotData(@RequestBody Map map){
        String name =(String)map.get("name");
        String t1=(String) map.get("startTime");
        String t2=(String) map.get("endTime");
        List<Map> data = new ArrayList<>();
        List<WeiboUserInfo> weiboUserInfos= new ArrayList<>();
        if(name.length()==0){
            return createResult.getResults(weiboUserInfoReposity.getHotOder(t1,t2));
        }else {
            return createResult.getResults(weiboUserInfoReposity.getHotOderByName(t1,t2,'%'+name+'%'));
        }
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
    @PostMapping("getFansWeiboSetimentData")
    public Results getFansWeiboSetimentData(@RequestBody Map map){
        String id=(String) map.get("id");
        String t1=(String) map.get("startTime");
        String t2=(String) map.get("endTime");
        String url="https://weibo.com/"+id+"%";
        List<WeiboComment> weiboComments = weiboCommentReposity.findByWeiboUrlLikeAndCreatedAtBetween(url,t1,t2);
        float sum=0;
        int []counts={0,0,0,0,0,0,0,0,0,0,0};
        for(int i=0;i<weiboComments.size();i++){
            WeiboComment weiboComment = weiboComments.get(i);
            sum+=weiboComment.getSentiments();
            int value = Math.round(weiboComment.getSentiments());
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
        returnMap.put("avg",sum/weiboComments.size());
        return createResult.getResults(returnMap);
    }
    @PostMapping("/getUserHotlineDate")
    public Results getUserHotlineDate(@RequestBody Map map) throws ParseException {
        String id=(String) map.get("id");
        String t1=(String) map.get("startTime");
        String t2=(String) map.get("endTime");
        ArrayList<String> dateList = new GeneralMethod().getDateArrBySAndE(t1,t2);
        Map<String,List<Float>> dateMap=new HashMap<>();
        for(int i=0;i<dateList.size();i++){
            List<Float> floats = new ArrayList<>();
            dateMap.put(dateList.get(i),floats);
        }
        List<WeiboInfo> weiboInfoList =weiboInfoRepository.findByUserIdAndCreatedAtBetween(id,t1+" 00:00:00",t2+" 00:00:00");
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

        /*Collections.sort(maps, new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                String str1 = (String)o1.get("时间");
                String str2 = (String)o2.get("时间");
                return str1.compareTo(str2);
            }
        });*/
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
    @PostMapping("/allData/getUserInfo")
    public Results getUserInfo(@RequestBody Map map){
        long allUserCounts= weiboUserInfoReposity.count();
        /*获取1000名用户做样本*/
        List<WeiboUserInfo> weiboUserInfos = weiboUserInfoReposity.getUserRandom();
        Map<String, Integer> fansNumMap = new HashMap();
        fansNumMap.put("<100",0);
        fansNumMap.put("100-1000",0);
        fansNumMap.put("1000-10000",0);
        fansNumMap.put("10000-100000",0);
        fansNumMap.put("100000-1000000",0);
        fansNumMap.put(">1000000",0);
        Map<String, Integer> ageNumMap = new HashMap();
        ageNumMap.put("其他",0);
        ageNumMap.put("70后",0);
        ageNumMap.put("80后",0);
        ageNumMap.put("90后",0);
        ageNumMap.put("00后",0);
        Map<String, Integer> genderNumMap = new HashMap();
        Map<String, Integer> provinceNumMap = new HashMap();
        Map<String, Integer> vipLevelNumMap = new HashMap();
        for(int i=0;i<weiboUserInfos.size();i++){
            WeiboUserInfo weiboUserInfo = weiboUserInfos.get(i);
            double lg=Math.log((double) weiboUserInfo.getFansNum());
            if(lg<=2){
                fansNumMap.put("<100",(int)fansNumMap.get("<100")+1);
            }else if(lg>2&&lg<=3){
                fansNumMap.put("100-1000",(int)fansNumMap.get("100-1000")+1);
            }else if(lg>3&&lg<=4){
                fansNumMap.put("1000-10000",(int)fansNumMap.get("1000-10000")+1);
            }else if(lg>4&&lg<=5){
                fansNumMap.put("10000-100000",(int)fansNumMap.get("10000-100000")+1);
            }else if(lg>5&&lg<=6){
                fansNumMap.put("100000-1000000",(int)fansNumMap.get("100000-1000000")+1);
            }else {
                fansNumMap.put(">1000000",(int)fansNumMap.get(">1000000")+1);
            }
            if(weiboUserInfo.getBirthday().length()==10){
                if(weiboUserInfo.getBirthday().compareTo("1970-01-01")<0){
                    ageNumMap.put("其他",(int)ageNumMap.get("其他")+1);
                }else if(weiboUserInfo.getBirthday().compareTo("1970-01-01")>=0&&weiboUserInfo.getBirthday().compareTo("1980-01-01")<0){
                    ageNumMap.put("70后",(int)ageNumMap.get("70后")+1);
                }else if(weiboUserInfo.getBirthday().compareTo("1980-01-01")>=0&&weiboUserInfo.getBirthday().compareTo("1990-01-01")<0){
                    ageNumMap.put("80后",(int)ageNumMap.get("80后")+1);
                }else if(weiboUserInfo.getBirthday().compareTo("1990-01-01")>=0&&weiboUserInfo.getBirthday().compareTo("2000-01-01")<0){
                    ageNumMap.put("90后",(int)ageNumMap.get("90后")+1);
                }else{
                    ageNumMap.put("00后",(int)ageNumMap.get("00后")+1);
                }
            }
            if(weiboUserInfo.getGender().length()>0){
                if(genderNumMap.containsKey(weiboUserInfo.getGender())){
                    genderNumMap.put(weiboUserInfo.getGender(),(int)genderNumMap.get(weiboUserInfo.getGender())+1);
                }else {
                    genderNumMap.put(weiboUserInfo.getGender(),1);
                }
            }
            if(weiboUserInfo.getProvince().length()>0){
                if(provinceNumMap.containsKey(weiboUserInfo.getProvince())){
                    provinceNumMap.put(weiboUserInfo.getProvince(),(int)provinceNumMap.get(weiboUserInfo.getProvince())+1);
                }else {
                    provinceNumMap.put(weiboUserInfo.getProvince(),1);
                }
            }
            if(weiboUserInfo.getVipLevel().length()>0){
                if(vipLevelNumMap.containsKey(weiboUserInfo.getVipLevel())){
                    vipLevelNumMap.put(weiboUserInfo.getVipLevel(),(int)vipLevelNumMap.get(weiboUserInfo.getVipLevel())+1);
                }else {
                    vipLevelNumMap.put(weiboUserInfo.getVipLevel(),1);
                }
            }
        }
        Map returnMap = new HashMap();
        List<Map> fansNumMapList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : fansNumMap.entrySet()){
            Map map1 = new HashMap();
            map1.put("name",entry.getKey());
            map1.put("num",entry.getValue());
            map1.put("proportion",entry.getValue()/10+"%");
            fansNumMapList.add(map1);
        }

        List<Map> ageNumMapList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : ageNumMap.entrySet()){
            Map map1 = new HashMap();
            map1.put("name",entry.getKey());
            map1.put("num",entry.getValue());
            ageNumMapList.add(map1);
        }
        Collections.sort(ageNumMapList, new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                String str1 = (String)o1.get("name");
                if(str1=="00后"){
                    str1="95后";
                }
                if(str1=="其他"){
                    str1="00";
                }
                String str2 = (String)o2.get("name");
                if(str2=="00后"){
                    str2="95后";
                }
                if(str2=="其他"){
                    str2="00";
                }
                return str1.compareTo(str2);
            }
        });
        List<Map> genderNumMapList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : genderNumMap.entrySet()){
            Map map1 = new HashMap();
            map1.put("name",entry.getKey());
            map1.put("num",entry.getValue());
            genderNumMapList.add(map1);
        }
        List<Map> provinceNumMapList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : provinceNumMap.entrySet()){
            Map map1 = new HashMap();
            map1.put("name",entry.getKey());
            map1.put("num",entry.getValue());
            provinceNumMapList.add(map1);
        }
        List<Map> vipLevelNumMapList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : vipLevelNumMap.entrySet()){
            Map map1 = new HashMap();
            map1.put("name",entry.getKey());
            map1.put("num",entry.getValue());
            map1.put("proportion",entry.getValue()/10+"%");
            vipLevelNumMapList.add(map1);
        }
        Collections.sort(vipLevelNumMapList, new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                String str1 = (String)o1.get("name");
                String str2 = (String)o2.get("name");
                return str1.compareTo(str2);
            }
        });
        returnMap.put("fansNumMap",fansNumMapList);
        returnMap.put("ageNumMap",ageNumMapList);
        returnMap.put("genderNumMap",genderNumMapList);
        returnMap.put("provinceNumMap",provinceNumMapList);
        returnMap.put("vipLevelNumMap",vipLevelNumMapList);
        returnMap.put("allUserCounts",allUserCounts);
        return createResult.getResults(returnMap);
    }


}
