package cn.mxmefly.app.Bussiness.Dao.Repository;

import cn.mxmefly.app.Bussiness.Bean.WeiboInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WeiboInfoRepository extends JpaRepository<WeiboInfo, Integer> {
    public List<WeiboInfo> findAllById(int id);
    public long countByIsprocess(int isprocess);
    public long countByCrawlTimeBetween(int t1,int t2);
    public long countByUserIdAndCreatedAtBetween(String id,String t1,String t2);
    public List<WeiboInfo> findByUserIdAndCreatedAtBetween(String id,String t1,String t2);

    @Query(value = "SELECT * FROM weibo_info WHERE INSTR(content,:topic)>0 AND created_at BETWEEN :t1 AND :t2 AND user_id = :id ORDER BY like_num DESC LIMIT 10",nativeQuery = true)
    public List<WeiboInfo> getByTopicAndUserIdAndTime(@Param("topic")String topic, @Param("t1") String t1, @Param("t2") String t2,@Param("id")String id);
    @Query(value = "SELECT * FROM weibo_info WHERE INSTR(content,:topic)>0 AND created_at BETWEEN :t1 AND :t2 ",nativeQuery = true)
    public List<WeiboInfo> getByTopicWeiboInfo(@Param("topic")String topic, @Param("t1") String t1, @Param("t2") String t2);
}
