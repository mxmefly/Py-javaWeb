package cn.mxmefly.app.SystemManage.Dao.Repository;

import cn.mxmefly.app.SystemManage.Bean.NewWords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewWordsRespository extends JpaRepository<NewWords,Integer> {
    public NewWords findByWord(String word);
}
