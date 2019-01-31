package cn.mxmefly.app.Demo.Dao.Repository;

import cn.mxmefly.app.Demo.Bean.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo,Integer> {

}
