package cn.mxmefly.app.Controller;

import cn.mxmefly.app.Bean.pfUser;
import cn.mxmefly.app.Common.CreateResult;
import cn.mxmefly.app.Common.Md5;
import cn.mxmefly.app.Common.Results;
import cn.mxmefly.app.Dao.Repository.pfUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private pfUserRepository pur;

    /*@PostMapping(value = "/login")
    public Results login(@RequestParam("id") String id,@RequestParam("pwd") String pwd){
        *//*System.err.println("id: "+id+"  pwd: "+pwd+"  md5:"+new Md5().md5Password(pwd));*//*
        pfUser user = pur.findByIdAndPwd(id,new Md5().md5Password(pwd));
        if(user==null){
            return new CreateResult().getResults(false,"账号或密码错误");
        }else {
            return new CreateResult().getResults(true,"登陆成功");
        }
    }*/
    @PostMapping(value = "/login")
    public Results login(@RequestBody Map map){
        /*System.err.println("id: "+id+"  pwd: "+pwd+"  md5:"+new Md5().md5Password(pwd));*/
        String id = (String) map.get("id");
        String pwd=(String)map.get("pwd");
        pfUser user = pur.findByIdAndPwd(id,new Md5().md5Password(pwd));
        if(user==null){
            return new CreateResult().getResults(false,"账号或密码错误");
        }else {
            return new CreateResult().getResults(true,"登陆成功");
        }
    }
}
