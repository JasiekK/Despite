package com.despite.entities;

import javax.persistence.*;

@Entity
public class Exercise {

    @Id
    @GeneratedValue
    private Long id;
    private String name;


    public Exercise() {
    }

    public Exercise(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
