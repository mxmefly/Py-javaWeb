package cn.mxmefly.app.SystemManage.Service;

import cn.mxmefly.app.SystemManage.Bean.pfUser;
import cn.mxmefly.app.SystemManage.Dao.Repository.PfUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class pfUserService {

    @Autowired
    private PfUserRepository ud;

    public List<pfUser> getAllUser(){
        List<pfUser> lpf= new ArrayList<pfUser>();
        lpf=ud.findAll();
        return  lpf;
    }

}
