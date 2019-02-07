package cn.mxmefly.app.SystemManage.Controller;

import cn.mxmefly.app.Common.CreateResult;
import cn.mxmefly.app.Common.Results;
import cn.mxmefly.app.SystemManage.Dao.Repository.SysMsgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SysMsgController {
    @Autowired
    private SysMsgRepository sysMsgRepository;

    @PostMapping("getMsgNum")
    public Results getMsgNum(@RequestBody Map map){
       return new CreateResult().getResults(sysMsgRepository.getMsgNum().get(0));
    }

}
