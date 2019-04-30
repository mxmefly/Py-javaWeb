package cn.mxmefly.app.Bussiness.Controller;

import cn.mxmefly.app.Bussiness.Bean.WeiboUserInfo;
import cn.mxmefly.app.Bussiness.Dao.Repository.WeiboInfoRepository;
import cn.mxmefly.app.Bussiness.Dao.Repository.WeiboUserInfoReposity;
import cn.mxmefly.app.Common.CreateResult;
import cn.mxmefly.app.Common.Results;
import org.python.core.AstList;
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
}
