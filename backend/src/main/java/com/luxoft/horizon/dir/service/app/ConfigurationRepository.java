package com.luxoft.horizon.dir.service.app;

import com.luxoft.horizon.dir.entities.app.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

/**
 * Created by pavelbogatyrev on 03/04/16.
 */
@Service
public interface ConfigurationRepository extends CrudRepository<Configuration, Long> {
}
