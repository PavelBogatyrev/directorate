package com.luxoft.horizon.dir.service.app;

import com.luxoft.horizon.dir.entities.app.Label;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

/**
 * Created by bogatp on 30.03.16.
 */
@Service
public interface LabelRepository extends CrudRepository<Label, Long> {
}
