package com.luxoft.horizon.dir.entities.app;

import com.google.gson.annotations.Expose;
import com.luxoft.horizon.dir.utils.Utils;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by bogatp on 12.04.16.
 */
@Entity
public class Period {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Expose
    private String value;



    public Period() {
    }

    public Period(String value) {
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Period{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}
