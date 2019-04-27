package cn.mxmefly.app.Bussiness.Dao.Repository;

import cn.mxmefly.app.Bussiness.Bean.WeiboUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeiboUserInfoReposity extends JpaRepository<WeiboUserInfo,Integer> {
    public List<WeiboUserInfo> findAllBy_id(String _id);

}
