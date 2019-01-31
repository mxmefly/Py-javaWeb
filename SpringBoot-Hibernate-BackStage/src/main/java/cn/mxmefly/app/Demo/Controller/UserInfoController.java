package cn.mxmefly.app.Demo.Controller;

import cn.mxmefly.app.Common.CreateResult;
import cn.mxmefly.app.Common.Results;
import cn.mxmefly.app.Demo.Bean.UserInfo;
import cn.mxmefly.app.Demo.Dao.Repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
public class UserInfoController {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @PostMapping("/getTable")
    public Results getUserInfoByage(@RequestBody Map map){
        int page = (int) map.get("page");
        int size = (int) map.get("size");
        Pageable pageable = new PageRequest(page, size);
        Page<UserInfo> pui=userInfoRepository.findAll(pageable);
        return new CreateResult().getResults(pui);
    }

}
