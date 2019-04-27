package cn.mxmefly.app.Bussiness.Controller;

import cn.mxmefly.app.Bussiness.Dao.Repository.WeiboBaseDataRepository;
import cn.mxmefly.app.Bussiness.Dao.Repository.WeiboCommentReposity;
import cn.mxmefly.app.Bussiness.Dao.Repository.WeiboInfoRepository;
import cn.mxmefly.app.Bussiness.Dao.Repository.WeiboUserInfoReposity;
import cn.mxmefly.app.Common.CreateResult;
import cn.mxmefly.app.Common.Results;
import cn.mxmefly.app.Common.Results;
import cn.mxmefly.app.SystemManage.Bean.PfTourist;
import cn.mxmefly.app.SystemManage.Dao.Repository.PfTouristRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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

    private CreateResult createResult = new CreateResult();

    @PostMapping("/getDataSize")
    public Results getDataSize(@RequestBody Map map){
        Map returnMap = new HashMap();
        returnMap.put("weiboUserNum",weiboUserInfoReposity.count());
        returnMap.put("weiboInfoNum",weiboInfoRepository.count());
        returnMap.put("weiboCommentNum",weiboCommentReposity.count());
        returnMap.put("weiboBaseDataNum",weiboBaseDataRepository.count());
        returnMap.put("weiboToProcessNum",weiboInfoRepository.countByIsProcess(0)+weiboCommentReposity.countByIsProcess(0));
        returnMap.put("touristNum",pfTouristRepository.count());
        return createResult.getResults(returnMap);
    }
}
