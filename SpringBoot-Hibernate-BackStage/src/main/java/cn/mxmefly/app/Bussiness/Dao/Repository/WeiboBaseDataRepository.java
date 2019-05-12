package cn.mxmefly.app.Bussiness.Dao.Repository;

import cn.mxmefly.app.Bussiness.Bean.WeiboBaseData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WeiboBaseDataRepository extends JpaRepository<WeiboBaseData,Integer> {
    public List<WeiboBaseData> findByUserIdOrUpUserAndDateBetween(String userId,String upUserId,String t1,String t2);

    @Query(value = "SELECT name,sum(counts) as counts from weibo_base_data WHERE type=:type AND date BETWEEN :t1 AND :t2 GROUP BY name ORDER BY counts DESC LIMIT 10",nativeQuery = true)
    public List getHotBaseDataBytime(@Param("type")String type,@Param("t1")String t1,@Param("t2")String t2);

    @Query(value = "SELECT a.user_id,b.nick_name,a.counts FROM (SELECT user_id ,sum(counts) AS counts FROM weibo_base_data WHERE date BETWEEN :t1 AND :t2 AND name=:name AND type=:type GROUP BY user_id ORDER BY counts DESC LIMIT 10) AS a LEFT JOIN weibo_user_info AS b ON a.user_id=b._id",nativeQuery = true)
    public List getWBaseDataUser(@Param("name")String name,@Param("type")String type,@Param("t1")String t1,@Param("t2")String t2);

    public List<WeiboBaseData> findByNameAndTypeAndDateBetween(String name,String type,String t1,String t2);
}
