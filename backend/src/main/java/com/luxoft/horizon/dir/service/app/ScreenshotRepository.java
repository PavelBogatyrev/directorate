package com.luxoft.horizon.dir.service.app;

import com.luxoft.horizon.dir.entities.app.Screenshot;
import com.luxoft.horizon.dir.entities.app.ScreenshotStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by bogatp on 19.04.16.
 */
@Service
public interface ScreenshotRepository extends CrudRepository<Screenshot, Long> {

    List<Screenshot> findByStatus(ScreenshotStatus status);

    List<Screenshot> findByPeriodAndViewAndStatus(String period, String view, ScreenshotStatus status);

    @Query(value = "select count(s.id) from Screenshot s WHERE s.period = ?1  and s.status = ?2")
    int findNumByPeriodAndStatus(String period, ScreenshotStatus status);

    @Query(value = "select count(s.id) from Screenshot s WHERE s.period = ?1")
    int findNumByPeriod(String period);

    @Modifying
    @Transactional
    @Query(value = "update Screenshot s set s.status=?1 WHERE s.period = ?2")
    int updateStatus(ScreenshotStatus status, String period);

    @Modifying
    @Transactional
    @Query(value = "update Screenshot s set s.status=?1")
    int updateStatusAll(ScreenshotStatus status );

    @Modifying
    @Transactional
    @Query(value = "delete from Screenshot s where s.period = ?1")
    int deleteByPeriod(String period);

}
