package cn.mxmefly.app.SystemManage.Dao.Repository;

import cn.mxmefly.app.SystemManage.Bean.PfMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PfMenuRepository extends JpaRepository<PfMenu,Integer> {
     public List<PfMenu> findByParentIdOrderByMenuOrder(String parentId);
     public List<PfMenu> findByIsParent(int isParent);
}
