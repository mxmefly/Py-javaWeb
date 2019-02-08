package cn.mxmefly.app.SystemManage.Dao.Repository;

import cn.mxmefly.app.SystemManage.Bean.SysMsg;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SysMsgRepository extends JpaRepository<SysMsg, Integer> {
    @Query("select count(1) from SysMsg where state = 0")
    public List getMsgNum();

    public List<SysMsg> findByState(int state, Sort sort);

    @Query(nativeQuery = true, value = "update sys_msg set state = :state where state= :oldState")
    @Modifying
    public int updateStateAll(@Param("state") int state, @Param("oldState") int oldState);

}
