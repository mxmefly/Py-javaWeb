package cn.mxmefly.app.Dao.Repository;

import cn.mxmefly.app.Bean.pfUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface pfUserRepository extends JpaRepository<pfUser,Integer> {

    pfUser findByIdAndPwd(Integer id, String pwd);

}
