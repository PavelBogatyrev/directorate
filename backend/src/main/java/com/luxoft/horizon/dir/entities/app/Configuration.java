package com.luxoft.horizon.dir.entities.app;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.List;

/**
 * Created by bogatp on 23.03.16.
 */
@Entity
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Expose
    private long id;




    @Expose
    public String currentPeriod;

    public Configuration() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }






    public String getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(String currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "id=" + id +
                ", currentPeriod='" + currentPeriod + '\'' +
                '}';
    }
}
