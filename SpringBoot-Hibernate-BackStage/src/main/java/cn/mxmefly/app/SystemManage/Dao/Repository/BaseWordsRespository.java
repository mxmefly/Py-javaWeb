package cn.mxmefly.app.SystemManage.Dao.Repository;

import cn.mxmefly.app.SystemManage.Bean.BaseWords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseWordsRespository extends JpaRepository<BaseWords,Integer> {
    public List<BaseWords> findAllByWordLike(String exWord);
    public BaseWords findByWord(String word);
}
