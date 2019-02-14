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
import java.util.List;
import java.util.Map;

@RestController
public class SysMsgController {
    @Autowired
    private SysMsgRepository sysMsgRepository;

    private CreateResult createResult = new CreateResult();

    @PostMapping("getMsgNum")
    public Results getMsgNum(@RequestBody Map map){
       return createResult.getResults(sysMsgRepository.getMsgNum().get(0));
    }

    @PostMapping("getMsgData")
    public Results getMsgData(){
        Map returnMap = new HashMap();
        /*排序查询*/
        Sort sort = new Sort(Sort.Direction.DESC,"messageDate");
        returnMap.put("unread",sysMsgRepository.findByState(0,sort));
        returnMap.put("read",sysMsgRepository.findByState(1,sort));
        returnMap.put("recycle",sysMsgRepository.findByState(2,sort));
        return createResult.getResults(returnMap);
    }

    @PostMapping("updateMsgState")
    public Results updateMsgState(@RequestBody Map map){
        int id = (int) map.get("id");
        int state= (int) map.get("state");
        SysMsg sysMsg = sysMsgRepository.findOne(id);
        sysMsg.setState(state);
        sysMsgRepository.save(sysMsg);
        return createResult.getResults(true,"更新成功");
    }

    @PostMapping("updateStateAll")
    public Results updateStateAll(@RequestBody Map map){
        int state = (int)map.get("state");
        int oldState = (int)map.get("oldState");
        List<SysMsg> list = sysMsgRepository.findByState(oldState);
        for(int i=0;i<list.size();i++){
            list.get(i).setState(state);
        }
        sysMsgRepository.save(list);
        return createResult.getResults(list.size());
    }

    @PostMapping("delMsgById")
    public Results delMsgById(@RequestBody Map map){
        int id = (int)map.get("id");
        sysMsgRepository.delete(id);
        return createResult.getResults(true,"删除成功");
    }

    @PostMapping("delMsgByAll")
    public Results delMsgByAll(){
        List<SysMsg> list = sysMsgRepository.findByState(1);
        sysMsgRepository.delete(list);
        return createResult.getResults(list.size());
    }


}
