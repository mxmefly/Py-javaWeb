package cn.mxmefly.app.SystemManage.Dao.Repository;

import cn.mxmefly.app.SystemManage.Bean.PfConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PfConfigRepository extends JpaRepository<PfConfig,Integer> {
    public List<PfConfig> findByName(String name);
}
