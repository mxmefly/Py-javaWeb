package cn.mxmefly.app.SystemManage.Dao.Repository;

import cn.mxmefly.app.SystemManage.Bean.PfDispatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PfDispatchRespository extends JpaRepository<PfDispatch,Integer> {
    @Query(value = "CALL clear_notShow " ,nativeQuery = true)
    @Modifying
    public List  clearNotShow();

    @Query(value = "CALL import_newWord " ,nativeQuery = true)
    @Modifying
    public  List importNewWord();

    @Query(value = "CALL update_tempWord " ,nativeQuery = true)
    @Modifying
    public List updateTempWord();

    @Query(value = "select name,interval_value,interval_field,created,comment from mysql.event",nativeQuery = true)
    public List getEventsList();

    @Query(value = " SHOW VARIABLES LIKE'%event_sche%'",nativeQuery = true)
    public List getEventStatus();

    public List<PfDispatch> findAll();



}
