package cn.mxmefly.app.Bussiness.Dao.Repository;

import cn.mxmefly.app.Bussiness.Bean.WeiboComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeiboCommentReposity extends JpaRepository<WeiboComment,Integer> {
    public long countByIsProcess(int isProcess);
}
