package com.despite.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ExerciseDetails {

    @Id
    @GeneratedValue
    private Long id;
    private int time;

    public ExerciseDetails() {
    }

    public ExerciseDetails(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
