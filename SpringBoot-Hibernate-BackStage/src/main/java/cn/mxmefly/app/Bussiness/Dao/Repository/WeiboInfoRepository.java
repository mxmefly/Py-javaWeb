package cn.mxmefly.app.Bussiness.Dao.Repository;

import cn.mxmefly.app.Bussiness.Bean.WeiboInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeiboInfoRepository extends JpaRepository<WeiboInfo, Integer> {
    public List<WeiboInfo> findAllById(int id);
    public long countByIsprocess(int isprocess);
    public long countByCrawlTimeBetween(int t1,int t2);
    public long countByUserIdAndCreatedAtBetween(String id,String t1,String t2);
    public List<WeiboInfo> findByUserIdAndCreatedAtBetween(String id,String t1,String t2);
}
