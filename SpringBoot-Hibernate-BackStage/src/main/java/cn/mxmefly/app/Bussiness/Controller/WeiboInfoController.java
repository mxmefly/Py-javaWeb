package cn.mxmefly.app.Bussiness.Controller;

import cn.mxmefly.app.Bussiness.Bean.WeiboInfo;
import cn.mxmefly.app.Bussiness.Bean.WeiboUserInfo;
import cn.mxmefly.app.Bussiness.Dao.Repository.WeiboInfoRepository;
import cn.mxmefly.app.Bussiness.Dao.Repository.WeiboUserInfoReposity;
import cn.mxmefly.app.Common.CreateResult;
import cn.mxmefly.app.Common.LinearRegression.MyLinearRegression;
import cn.mxmefly.app.Common.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class WeiboInfoController {
    @Autowired
    private WeiboInfoRepository weiboInfoRepository;
    @Autowired
    private WeiboUserInfoReposity weiboUserInfoReposity;

    private CreateResult createResult = new CreateResult();
    @PostMapping("/weiboInfoTest")
    public Map weiboInfoTest(@RequestBody Map map){
        List hotData = new ArrayList();
        List<WeiboInfo> weiboInfoList = weiboInfoRepository.findAllById(1);
        Map returnMap = new HashMap();
        returnMap.put("test",weiboInfoList);
        return  returnMap;
    }

    @PostMapping("/weiboUserInfoTest")
    public Map weiboUserInfoTest(@RequestBody Map map){
        List<WeiboUserInfo> weiboInfoList = weiboUserInfoReposity.findAllBy_id("5676095533");
        Map returnMap = new HashMap();
        returnMap.put("test",weiboInfoList);
        return  returnMap;
    }

    @PostMapping("/ArimaTest")
    public Results cpuTest(@RequestBody Map map){
        List<Double> doubles = new ArrayList<>();
        doubles.add((double)1000);
        doubles.add((double)1100);
        doubles.add((double)1200);
        doubles.add((double)1300);
        doubles.add((double)1200);
        doubles.add((double)1300);
        doubles.add((double)1400);
        doubles.add((double)1300);
        doubles.add((double)1400);
        doubles.add((double)1450);
        MyLinearRegression myLinearRegression = new MyLinearRegression(doubles);

        return  createResult.getResults(myLinearRegression.getPrediction(5));
    }


}
