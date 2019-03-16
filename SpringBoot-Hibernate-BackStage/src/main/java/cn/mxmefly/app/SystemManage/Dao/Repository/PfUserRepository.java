package cn.mxmefly.app.SystemManage.Dao.Repository;

import cn.mxmefly.app.SystemManage.Bean.pfUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PfUserRepository extends JpaRepository<pfUser,Integer> {

    pfUser findByIdAndPwd(String id, String pwd);
    pfUser findById(String id);
}
