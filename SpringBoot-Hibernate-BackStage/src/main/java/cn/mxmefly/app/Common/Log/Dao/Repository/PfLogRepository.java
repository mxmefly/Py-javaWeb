package cn.mxmefly.app.Common.Log.Dao.Repository;

import cn.mxmefly.app.Common.Log.Bean.PfLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PfLogRepository extends JpaRepository<PfLog, Integer> {

}
