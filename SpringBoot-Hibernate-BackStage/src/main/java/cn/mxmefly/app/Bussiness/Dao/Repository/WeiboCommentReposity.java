package cn.mxmefly.app.Bussiness.Dao.Repository;

import cn.mxmefly.app.Bussiness.Bean.WeiboComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeiboCommentReposity extends JpaRepository<WeiboComment,Integer> {
    public long countByIsprocess(int isProcess);
    public long countByCrawlTimeBetween(int t1,int t2);
    public List<WeiboComment> findByWeiboUrlLikeAndCreatedAtBetween(String weiboUrl,String t1,String t2);
}
