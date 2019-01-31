package cn.mxmefly.app.SystemManage.Controller;

import cn.mxmefly.app.SystemManage.Bean.pfUser;
import cn.mxmefly.app.Common.CreateResult;
import cn.mxmefly.app.Common.Results;
import cn.mxmefly.app.SystemManage.Dao.Repository.pfUserRepository;
import cn.mxmefly.app.SystemManage.Service.pfUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PfUserController {
    @Autowired
    private pfUserRepository pur;

    private pfUserService pus=new pfUserService();

    /*获取所有用户*/
    @GetMapping("/getAllPfUsers")
    public Results getAllPfUser(){
        return new CreateResult().getResults(pur.findAll());
    }

    @PostMapping("/getInfo")
    public Results getUserInfoById(@RequestBody Map map){
        pfUser user = pur.findById((String)map.get("id"));
        if(user!=null){
            return new CreateResult().getResults(user);
        }else{
            return new CreateResult().getResults(false,"未查询到用户信息");
        }

    }

}
