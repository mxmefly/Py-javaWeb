package cn.mxmefly.app.SystemManage.Controller;

import cn.mxmefly.app.Common.CreateResult;
import cn.mxmefly.app.Common.Results;
import cn.mxmefly.app.SystemManage.Bean.PfDispatch;
import cn.mxmefly.app.SystemManage.Dao.Repository.PfDispatchRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ConfigController {
    @Autowired
    private PfDispatchRespository pfDispatchRespository;

    CreateResult createResult = new CreateResult();

    @PostMapping("/getEventConfigDate")
    public Results getEventConfigDate(){
        Map returnMap = new HashMap();
        returnMap.put("eventsList",pfDispatchRespository.getEventsList());
        returnMap.put("eventStatus",pfDispatchRespository.getEventStatus());
        return createResult.getResults(returnMap);
    }
    @PostMapping("/getLogTable")
    public Results getLogTable(@RequestBody Map map){
        int page = (int) map.get("page");
        int size = (int) map.get("size");
        Sort sort = new Sort(Sort.Direction.DESC,"date");
        Pageable pageable = new PageRequest(page, size,sort);
        Page<PfDispatch> pfDispatches = pfDispatchRespository.findAll(pageable);
        return createResult.getResults(pfDispatches);
    }
}
