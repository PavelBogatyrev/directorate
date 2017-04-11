package com.luxoft.horizon.dir.entities.app;

import com.google.gson.annotations.Expose;

import javax.persistence.*;


/**
 * Created by bogatp on 23.03.16.
 */
@Entity
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Expose
    private String identifier;

    @Expose
    private String description;

    @Expose
    @Lob
    private String label;



    public Label() {
    }

    public Label(String identifier, String description, String label) {
        this.identifier = identifier;
        this.description = description;
        this.label = label;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return "Label{" +
                "id=" + id +
                ", identifier='" + identifier + '\'' +
                ", description='" + description + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
