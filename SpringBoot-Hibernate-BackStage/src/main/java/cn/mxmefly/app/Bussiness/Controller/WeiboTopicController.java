package cn.mxmefly.app.Bussiness.Controller;

import cn.mxmefly.app.Bussiness.Dao.Repository.WeiboTopicRepository;
import cn.mxmefly.app.Common.CreateResult;
import cn.mxmefly.app.Common.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class WeiboTopicController {
    @Autowired
    private WeiboTopicRepository weiboTopicRepository;

    private CreateResult createResult = new CreateResult();

    @PostMapping("/getTopicOderData")
    public Results getTopicOderData(@RequestBody Map map){
        String t1=(String) map.get("startTime");
        String t2=(String) map.get("endTime");
        return  createResult.getResults(weiboTopicRepository.getHotTopicOrder(t1,t2));
    }
}
