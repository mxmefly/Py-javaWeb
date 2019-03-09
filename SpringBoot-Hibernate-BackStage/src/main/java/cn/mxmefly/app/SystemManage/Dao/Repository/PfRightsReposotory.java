package cn.mxmefly.app.SystemManage.Dao.Repository;

import cn.mxmefly.app.SystemManage.Bean.PfRights;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PfRightsReposotory extends JpaRepository<PfRights,Integer> {
    /*通过角色获取权限*/
    public List<PfRights> findByRole(String role);
    /*通过角色权限获取*/
    public List<PfRights> findByRoleAndMenuId(String role,String menuId);

}
