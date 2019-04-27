package cn.mxmefly.app.Bussiness.Dao.Repository;

import cn.mxmefly.app.Bussiness.Bean.WeiboInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeiboInfoRepository extends JpaRepository<WeiboInfo, Integer> {
    public List<WeiboInfo> findAllBy_id(String _id);
    public long countByIsProcess(int isProcess);
}
