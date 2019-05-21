package cn.mxmefly.app.Bussiness.Dao.Repository;

import cn.mxmefly.app.Bussiness.Bean.WeiboTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WeiboTopicRepository extends JpaRepository<WeiboTopic,Integer> {

    @Query(value = "SELECT a.topic,a.counts,AVG(b.repost_num*1.1+b.like_num*1.0+b.comment_num*1.2) AS hotData from (SELECT topic ,count(*) as counts FROM weibo_topic WHERE date BETWEEN :t1 AND :t2 GROUP BY topic ORDER BY counts DESC LIMIT 10) AS a LEFT JOIN weibo_info AS b  ON INSTR(b.content,a.topic)>0 WHERE b.created_at BETWEEN :t1 AND :t2   GROUP BY a.topic ORDER BY hotData DESC", nativeQuery = true)
    public List getHotTopicOrder(@Param("t1") String t1, @Param("t2") String t2);

    @Query(value = "SELECT a.user_id,b.nick_name,b.brief_introduction,a.counts FROM (SELECT user_id , count(*) AS counts FROM weibo_topic WHERE topic=:topic AND date BETWEEN :t1 AND :t2 GROUP BY user_id ORDER BY counts DESC LIMIT 10) AS a LEFT JOIN  weibo_user_info AS b ON a.user_id= b._id", nativeQuery = true)
    public List getHotTopicUser(@Param("topic")String topic,@Param("t1") String t1, @Param("t2") String t2);


}