package cn.mxmefly.app.SystemManage.Dao.Repository;

import cn.mxmefly.app.SystemManage.Bean.BaseWords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseWordsRespository extends JpaRepository<BaseWords,Integer> {
    
}
