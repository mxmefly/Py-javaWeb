package cn.mxmefly.app.SystemManage.Dao.Repository;

import cn.mxmefly.app.SystemManage.Bean.SysMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SysMsgRepository extends JpaRepository<SysMsg, Integer> {
    @Query("select count(1) from SysMsg where state = 0")
    public List getMsgNum();

    public List<SysMsg> findByState(int state);

}
