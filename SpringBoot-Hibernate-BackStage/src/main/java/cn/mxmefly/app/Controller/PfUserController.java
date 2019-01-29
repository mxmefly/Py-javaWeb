package cn.mxmefly.app.Controller;

import cn.mxmefly.app.Bean.pfUser;
import cn.mxmefly.app.Common.CreateResult;
import cn.mxmefly.app.Common.Results;
import cn.mxmefly.app.Dao.Repository.pfUserRepository;
import cn.mxmefly.app.Service.pfUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

}
