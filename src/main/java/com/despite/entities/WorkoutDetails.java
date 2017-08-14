package com.despite.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class WorkoutDetails {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Column(name = "exercise_order_number")
    @NotNull
    private Integer orderNumber;

    public WorkoutDetails() {
    }

    public WorkoutDetails(Exercise exercise, Integer orderNumber) {
        this.exercise = exercise;
        this.orderNumber = orderNumber;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
}
