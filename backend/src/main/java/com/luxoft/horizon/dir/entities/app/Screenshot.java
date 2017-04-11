package com.luxoft.horizon.dir.entities.app;

import javax.persistence.*;

/**
 * Created by bogatp on 17.04.16.
 */
@Entity
public class Screenshot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String period;

    private String view;

    @Lob
    private byte[] data;

    private ScreenshotStatus status = ScreenshotStatus.NONE;

    public Screenshot() {
    }

    public Screenshot(String period, String view, ScreenshotStatus status) {
        this.period = period;
        this.view = view;
        this.status = status;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public ScreenshotStatus getStatus() {
        return status;
    }

    public void setStatus(ScreenshotStatus status) {
        this.status = status;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }
}
