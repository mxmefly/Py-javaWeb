package cn.mxmefly.app.Bussiness.Dao.Repository;

import cn.mxmefly.app.Bussiness.Bean.WeiboComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeiboCommentReposity extends JpaRepository<WeiboComment,Integer> {
    public long countByIsprocess(int isProcess);
    public long countByCrawlTimeBetween(int t1,int t2);
}
