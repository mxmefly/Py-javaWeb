package cn.mxmefly.app.SystemManage.Controller;

import cn.mxmefly.app.Common.CreateResult;
import cn.mxmefly.app.Common.Results;
import cn.mxmefly.app.SystemManage.Bean.SysMsg;
import cn.mxmefly.app.SystemManage.Dao.Repository.SysMsgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SysMsgController {
    @Autowired
    private SysMsgRepository sysMsgRepository;

    @PostMapping("getMsgNum")
    public Results getMsgNum(@RequestBody Map map){
       return new CreateResult().getResults(sysMsgRepository.getMsgNum().get(0));
    }

    @PostMapping("getMsgData")
    public Results getMsgData(){
        Map returnMap = new HashMap();
        /*排序查询*/
        Sort sort = new Sort(Sort.Direction.DESC,"messageDate");
        returnMap.put("unread",sysMsgRepository.findByState(0,sort));
        returnMap.put("read",sysMsgRepository.findByState(1,sort));
        returnMap.put("recycle",sysMsgRepository.findByState(2,sort));
        return new CreateResult().getResults(returnMap);
    }

    @PostMapping("updateMsgState")
    public Results updateMsgState(@RequestBody Map map){
        int id = (int) map.get("id");
        int state= (int) map.get("state");
        SysMsg sysMsg = sysMsgRepository.findOne(id);
        sysMsg.setState(state);
        sysMsgRepository.save(sysMsg);
        return new CreateResult().getResults(true,"更新成功");
    }

    @PostMapping("updateStateAll")
    public Results updateStateAll(@RequestBody Map map){
        int state = (int)map.get("state");
        int oldState = (int)map.get("oldState");
        return new CreateResult().getResults(sysMsgRepository.updateStateAll(state,oldState));
    }
}
