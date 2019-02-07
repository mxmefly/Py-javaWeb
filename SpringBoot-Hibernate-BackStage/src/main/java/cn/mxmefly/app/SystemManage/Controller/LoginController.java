package cn.mxmefly.app.SystemManage.Controller;

import cn.mxmefly.app.Common.Listener.MySessionContext;
import cn.mxmefly.app.SystemManage.Bean.pfUser;
import cn.mxmefly.app.Common.CreateResult;
import cn.mxmefly.app.Common.Md5;
import cn.mxmefly.app.Common.Results;
import cn.mxmefly.app.SystemManage.Dao.Repository.SysMsgRepository;
import cn.mxmefly.app.SystemManage.Dao.Repository.pfUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private pfUserRepository pur;
    @Autowired
    private SysMsgRepository sysMsgRepository;

    @PostMapping(value = "/login")
    public Results login(@RequestBody Map map, HttpServletRequest request){
        String id = (String) map.get("id");
        String pwd=(String)map.get("pwd");
        pfUser user = pur.findByIdAndPwd(id,new Md5().md5Password(pwd));
        if(user==null){
            return new CreateResult().getResults(false,"账号或密码错误");
        }else {
            /*登陆成功 创建session*/
            HttpSession session = request.getSession(true);
            session.setAttribute("id",user.getId());
            session.setAttribute("name",user.getName());
            session.setAttribute("des",user.getDes());
            return new CreateResult().getResults(true,"登陆成功",session.getId());
        }
    }
    @PostMapping("/getInfo")
    public Results getUserInfoById(@RequestBody Map map){
        String sessionId= (String) map.get("sessionId");
        MySessionContext mySessionContext = MySessionContext.getInstance();
        HttpSession session = mySessionContext.getSession(sessionId);
        if(session==null){
            return new CreateResult().getResults(false,"未查询到用户信息");
        }else{
            Map returnMap = new HashMap<>();
            returnMap.put("id",session.getAttribute("id"));
            returnMap.put("name",session.getAttribute("name"));
            returnMap.put("des",session.getAttribute("des"));
            returnMap.put("unreadMsgNum",sysMsgRepository.getMsgNum().get(0)+"");
            return  new CreateResult().getResults(returnMap);
        }
    }

    @PostMapping("/logout")
    public Results logout(@RequestBody Map map){
        String sessionId= (String) map.get("sessionId");
        MySessionContext mySessionContext = MySessionContext.getInstance();
        HttpSession session = mySessionContext.getSession(sessionId);
        if(session==null){
            return new CreateResult().getResults(false,"该用户未登录");
        }else {
            mySessionContext.delSession(sessionId);
            return new CreateResult().getResults(false,"登出成功");
        }
    }


}
