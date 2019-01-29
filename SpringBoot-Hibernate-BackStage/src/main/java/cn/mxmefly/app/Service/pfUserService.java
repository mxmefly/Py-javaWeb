package cn.mxmefly.app.Service;

import cn.mxmefly.app.Bean.pfUser;
import cn.mxmefly.app.Dao.Repository.pfUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@Service
public class pfUserService {

    @Autowired
    private pfUserRepository ud;

    public List<pfUser> getAllUser(){
        List<pfUser> lpf= new ArrayList<pfUser>();
        lpf=ud.findAll();
        return  lpf;
    }

}
