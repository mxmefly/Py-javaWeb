package cn.mxmefly.app.Bussiness.Controller;

import cn.mxmefly.app.Bussiness.Bean.WeiboInfo;
import cn.mxmefly.app.Bussiness.Bean.WeiboUserInfo;
import cn.mxmefly.app.Bussiness.Dao.Repository.WeiboInfoRepository;
import cn.mxmefly.app.Bussiness.Dao.Repository.WeiboUserInfoReposity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class WeiboInfoController {
    @Autowired
    private WeiboInfoRepository weiboInfoRepository;
    @Autowired
    private WeiboUserInfoReposity weiboUserInfoReposity;

    @PostMapping("/weiboInfoTest")
    public Map weiboInfoTest(@RequestBody Map map){
        List<WeiboInfo> weiboInfoList = weiboInfoRepository.findAllBy_id("1095343047_GdoYGptjJ");
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


}
