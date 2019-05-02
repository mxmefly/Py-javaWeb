package cn.mxmefly.app.Bussiness.Dao.Repository;

import cn.mxmefly.app.Bussiness.Bean.WeiboBaseData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeiboBaseDataRepository extends JpaRepository<WeiboBaseData,Integer> {
    public List<WeiboBaseData> findByUserIdOrUpUserAndDateBetween(String userId,String upUserId,String t1,String t2);

}
