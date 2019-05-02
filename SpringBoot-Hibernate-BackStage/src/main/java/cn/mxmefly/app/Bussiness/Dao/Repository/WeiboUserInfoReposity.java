package cn.mxmefly.app.Bussiness.Dao.Repository;

import cn.mxmefly.app.Bussiness.Bean.WeiboUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WeiboUserInfoReposity extends JpaRepository<WeiboUserInfo,Integer> {
    public List<WeiboUserInfo> findAllBy_id(String _id);
    public Long countByCrawlTimeBetween(int t1,int t2);
    public WeiboUserInfo findBy_id(String id);

    @Query(value = "SELECT AVG(b.repost_num*1.2+b.like_num*1.0+b.comment_num*1.1) AS num from weibo_user_info AS a LEFT JOIN weibo_info AS b  " +
            "on a._id=b.user_id WHERE a._id=:id AND b.created_at BETWEEN :t1 AND :t2 ",nativeQuery = true)
    public float getUserHotData(@Param("id")String id,@Param("t1")String t1,@Param("t2") String t2);



    @Query(value = "SELECT * from weibo_user_info WHERE fans_num>5000000 ORDER BY fans_num DESC",nativeQuery = true)
    public List<WeiboUserInfo> findAllBigV();
}
