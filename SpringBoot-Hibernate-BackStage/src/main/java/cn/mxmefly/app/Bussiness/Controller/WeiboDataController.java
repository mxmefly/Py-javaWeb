package cn.mxmefly.app.Bussiness.Controller;

import cn.mxmefly.app.Bussiness.Dao.Repository.*;
import cn.mxmefly.app.Common.*;
import cn.mxmefly.app.Common.Results;
import cn.mxmefly.app.SystemManage.Bean.PfTourist;
import cn.mxmefly.app.SystemManage.Dao.Repository.PfTouristRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class WeiboDataController {
    @Autowired
    private WeiboUserInfoReposity weiboUserInfoReposity;
    @Autowired
    private WeiboInfoRepository weiboInfoRepository;
    @Autowired
    private WeiboCommentReposity weiboCommentReposity;
    @Autowired
    private PfTouristRepository pfTouristRepository;
    @Autowired
    private WeiboBaseDataRepository weiboBaseDataRepository;
    @Autowired
    private AccountPoolRepository accountPoolRepository;

    private CreateResult createResult = new CreateResult();

    @PostMapping("/getDataSize")
    public Results getDataSize(@RequestBody Map map){
        Map returnMap = new HashMap();
        returnMap.put("weiboUserNum",weiboUserInfoReposity.count());
        returnMap.put("weiboInfoNum",weiboInfoRepository.count());
        returnMap.put("weiboInfoTodoNum",weiboInfoRepository.countByIsprocess(0));
        returnMap.put("weiboCommentNum",weiboCommentReposity.count());
        returnMap.put("weiboCommentTodoNum",weiboCommentReposity.countByIsprocess(0));
        returnMap.put("weiboBaseDataNum",weiboBaseDataRepository.count());
        returnMap.put("touristNum",pfTouristRepository.count());
        returnMap.put("accountPro",accountPoolRepository.countByStatus("success")/accountPoolRepository.count()*100);
        return createResult.getResults(returnMap);
    }

    @PostMapping("/getCpuUsd")
    public Map getCpuUsd(@RequestBody Map map){
        Map returnMap = new HashMap();
        returnMap.put("cpu", CPUMonitorCalc.getInstance().getProcessCpu());
        return  returnMap;
    }
    @PostMapping("/getUserlineDate")
    public Results getUserlineDate() throws ParseException {
        List<Map> mapList =new ArrayList<>();
        Date nowDate = new Date();
        long d=nowDate.getTime();
        DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> dateList = new GeneralMethod().getPastDateList(10);
        long datet2=0;
        for(int i=0;i<dateList.size()-1;i++){
            Map map = new HashMap();
            String dateStr=dateList.get(i);
             long datet1=fmt.parse(dateStr+" 00:00:00").getTime()/1000;
             dateStr=dateList.get(i+1);
             datet2=fmt.parse(dateStr+" 00:00:00").getTime()/1000;
             map.put("时间",dateList.get(i).substring(5));
             map.put("爬取用户数",weiboUserInfoReposity.countByCrawlTimeBetween((int)datet1,(int)datet2));
             mapList.add(map);
        }
        Map map = new HashMap();
        map.put("时间","今日");
        map.put("爬取用户数",weiboUserInfoReposity.countByCrawlTimeBetween((int)datet2,(int)d));
        mapList.add(map);
        return createResult.getResults(mapList);
    }

    @PostMapping("getWeiboDataLineData")
    public Results getWeiboDataLineData() throws ParseException {
        List<Map> mapList =new ArrayList<>();
        Date nowDate = new Date();
        long d=nowDate.getTime();
        DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> dateList = new GeneralMethod().getPastDateList(10);
        long datet2=0;
        for(int i=0;i<dateList.size()-1;i++){
            Map map = new HashMap();
            String dateStr=dateList.get(i);
            long datet1=fmt.parse(dateStr+" 00:00:00").getTime()/1000;
            dateStr=dateList.get(i+1);
            datet2=fmt.parse(dateStr+" 00:00:00").getTime()/1000;
            map.put("时间",dateList.get(i).substring(5));
            map.put("爬取微博数",weiboInfoRepository.countByCrawlTimeBetween((int)datet1,(int)datet2));
            map.put("爬取评论数",weiboCommentReposity.countByCrawlTimeBetween((int)datet1,(int)datet2));
            mapList.add(map);
        }
        Map map = new HashMap();
        map.put("时间","今日");
        map.put("爬取微博数",weiboInfoRepository.countByCrawlTimeBetween((int)datet2,(int)d));
        map.put("爬取评论数",weiboCommentReposity.countByCrawlTimeBetween((int)datet2,(int)d));
        mapList.add(map);
        return createResult.getResults(mapList);
    }


}
