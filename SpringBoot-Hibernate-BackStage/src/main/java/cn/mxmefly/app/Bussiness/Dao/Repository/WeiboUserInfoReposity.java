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



    @Query(value = "SELECT * from weibo_user_info WHERE fans_num>5000000 ORDER BY fans_num DESC LIMIT 40",nativeQuery = true)
    public List<WeiboUserInfo> findAllBigV();

    @Query(value = "SELECT a.* from weibo_user_info AS a LEFT JOIN weibo_user_rela AS b ON a._id=b.fan_id WHERE b.followed_id=:id",nativeQuery = true)
    public List<WeiboUserInfo> findFansByFollowedId(@Param("id")String followedId);

    public List<WeiboUserInfo> findByNickNameLike(String name);

    @Query(value="SELECT * FROM weibo_user_info ORDER BY RAND() LIMIT 3000",nativeQuery = true)
    public List<WeiboUserInfo> getUserRandom();

    @Query(value = "SELECT a._id,a.nick_name,a.authentication,AVG(b.repost_num*1.1+b.like_num*1.0+b.comment_num*1.2) AS num from weibo_user_info AS a LEFT JOIN weibo_info AS b on a._id=b.user_id WHERE a._id IN (SELECT _id FROM weibo_user_info WHERE fans_num>5000000 ) AND b.created_at BETWEEN  :t1 AND :t2 GROUP BY a._id ORDER BY num DESC LIMIT 15",nativeQuery = true)
    public List getHotOder(@Param("t1")String t1,@Param("t2") String t2);

    @Query(value = "SELECT a._id,a.nick_name,a.authentication,AVG(b.repost_num*1.1+b.like_num*1.0+b.comment_num*1.2) AS num from weibo_user_info AS a LEFT JOIN weibo_info AS b on a._id=b.user_id WHERE a._id IN (SELECT _id FROM weibo_user_info WHERE nick_name like :name) AND b.created_at BETWEEN  :t1 AND :t2 GROUP BY a._id ORDER BY num DESC LIMIT 15",nativeQuery = true)
    public List getHotOderByName(@Param("t1")String t1,@Param("t2") String t2,@Param("name") String name);


}