package cn.mxmefly.app.Bussiness.Dao.Repository;

import cn.mxmefly.app.Bussiness.Bean.TempWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TempWordRepository extends JpaRepository<TempWord,Integer> {
    public List<TempWord> findByName(String name);
}

