package cn.mxmefly.app.SystemManage.Controller;

import cn.mxmefly.app.Common.Listener.MySessionContext;
import cn.mxmefly.app.Common.Log.Bean.PfLog;
import cn.mxmefly.app.Common.Log.Dao.Repository.PfLogRepository;
import cn.mxmefly.app.Common.Log.LogType;
import cn.mxmefly.app.SystemManage.Bean.PfMenu;
import cn.mxmefly.app.SystemManage.Bean.TreeNode;
import cn.mxmefly.app.SystemManage.Bean.pfUser;
import cn.mxmefly.app.Common.CreateResult;
import cn.mxmefly.app.Common.Md5;
import cn.mxmefly.app.Common.Results;
import cn.mxmefly.app.SystemManage.Dao.Repository.PfMenuRepository;
import cn.mxmefly.app.SystemManage.Dao.Repository.PfRightsReposotory;
import cn.mxmefly.app.SystemManage.Dao.Repository.SysMsgRepository;
import cn.mxmefly.app.SystemManage.Dao.Repository.pfUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
public class LoginController {

    @Autowired
    private pfUserRepository pur;
    @Autowired
    private SysMsgRepository sysMsgRepository;
    @Autowired
    private PfMenuRepository pfMenuRepository;
    @Autowired
    private PfRightsReposotory pfRightsReposotory;
    @Autowired
    private PfLogRepository pfLogRepository;

    private CreateResult createResult = new CreateResult();


    @PostMapping(value = "/login")
    public Results login(@RequestBody Map map, HttpServletRequest request){
        String id = (String) map.get("id");
        String pwd=(String)map.get("pwd");
        pfUser user = pur.findByIdAndPwd(id,new Md5().md5Password(pwd));
        if(user==null){
            return createResult.getResults(false,"账号或密码错误");
        }else {
            /*登陆成功 创建session*/
            HttpSession session = request.getSession(true);
            session.setAttribute("id",user.getId());
            session.setAttribute("name",user.getName());
            session.setAttribute("des",user.getDes());
            Map returnMap = new HashMap<>();
            returnMap.put("sessionId",session.getId());
            pfLogRepository.save(new PfLog(user.getName(),user.getName()+"已登录",LogType.LOGIN,new Date()));
            return createResult.getResults(true,"登陆成功",returnMap);
        }
    }
    @PostMapping("/touristLogin")
    public Results touristLogin(HttpServletRequest request){
        String ip;
       /* 获取真实IP*/
        if (request.getHeader("x-forwarded-for") == null) {
            ip= request.getRemoteAddr();
        }else{
            ip= request.getHeader("x-forwarded-for");
        }
        String Agent = request.getHeader("User-Agent");
        StringTokenizer st = new StringTokenizer(Agent,";");
        st.nextToken();
        String useros = st.nextToken();
        String userbower = st.nextToken();
        return  createResult.getResults("");
    }
    @PostMapping("/getInfo")
    public Results getUserInfoById(@RequestBody Map map){
        String sessionId= (String) map.get("sessionId");
        MySessionContext mySessionContext = MySessionContext.getInstance();
        HttpSession session = mySessionContext.getSession(sessionId);
        if(session==null){
            return createResult.getResults(false,"未查询到用户信息");
        }else{
            Map returnMap = new HashMap<>();
            String id = (String)session.getAttribute("id");
            returnMap.put("id",id);
            returnMap.put("name",session.getAttribute("name"));
            returnMap.put("des",session.getAttribute("des"));
            returnMap.put("unreadMsgNum",sysMsgRepository.getMsgNum().get(0)+"");
            if(pur.findById(id)==null){
                returnMap.put("menuList",getMenuNodes("tourist"));
                returnMap.put("routList",getRouters("tourist"));
            }else{
                returnMap.put("menuList",getMenuNodes("admin"));
                returnMap.put("routList",getRouters("admin"));
            }
            return createResult.getResults(returnMap);
        }
    }

    @PostMapping("/logout")
    public Results logout(@RequestBody Map map){
        String sessionId= (String) map.get("sessionId");
        MySessionContext mySessionContext = MySessionContext.getInstance();
        HttpSession session = mySessionContext.getSession(sessionId);
        if(session==null){
            return createResult.getResults(false,"该用户未登录");
        }else {
            mySessionContext.delSession(sessionId);
            return createResult.getResults(false,"登出成功");
        }
    }

    private List<PfMenu> getRouters(String role){
        List<PfMenu> lpm =pfMenuRepository.findAll();
        List<PfMenu> list = new ArrayList<>();
        for(int i=0; i<lpm.size();i++){
            /*根据权限表 删除权限*/
            PfMenu pfMenu = lpm.get(i);
            if(pfMenu.getIsParent()==1){
                List<PfMenu> pfMenusByParent = pfMenuRepository.findByParentIdOrderByMenuOrder(pfMenu.getId());
                list.addAll(pfMenusByParent);
            }else{
                if(pfRightsReposotory.findByRoleAndMenuId(role,pfMenu.getId()).size()>0){
                    list.add(lpm.get(i));
                }
            }
        }
        return  list;
    }
    private List<TreeNode> getMenuNodes(String role){
        List<PfMenu> lpm = pfMenuRepository.findByParentIdOrderByMenuOrder("ROOT");
        List<TreeNode> list = new ArrayList<>();
        for(int i=0;i<lpm.size();i++){
            PfMenu pfMenu = lpm.get(i);
            if(pfRightsReposotory.findByRoleAndMenuId(role,pfMenu.getId()).size()>0){
                TreeNode treeNode =new TreeNode();
                treeNode.setId(pfMenu.getId());
                treeNode.setName(pfMenu.getName());
                treeNode.setIsparent(pfMenu.getIsParent());
                treeNode.setIcon(pfMenu.getIcon());
                treeNode.setPath(pfMenu.getPath());
                treeNode.setDes(pfMenu.getDes());
                if(pfMenu.getIsParent()==1){
                    treeNode.setChildren(getMenuTreeNode(pfMenuRepository.findByParentIdOrderByMenuOrder(pfMenu.getId())));
                }
                list.add(treeNode);
            }else {
                continue;
            }

        }
        return list;
    }
    private List<TreeNode> getMenuTreeNode(List<PfMenu> lpm){
        List<TreeNode> ltn = new ArrayList<>();
        for(int i=0;i<lpm.size();i++){
            TreeNode treeNode = new TreeNode();
            treeNode.setId(lpm.get(i).getId());
            treeNode.setName(lpm.get(i).getName());
            treeNode.setIsparent(lpm.get(i).getIsParent());
            treeNode.setIcon(lpm.get(i).getIcon());
            treeNode.setPath(lpm.get(i).getPath());
            treeNode.setDes(lpm.get(i).getDes());
            ltn.add(treeNode);
        }
        return ltn;
    }
}
