package com.despite.entities;

import javax.persistence.*;

@Entity
public class Exercise {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @Column(name = "exercise_order_number")
    private Integer orderNumber;

    @OneToOne()
    @JoinColumn(name = "exercise_details")
    private ExerciseDetails exerciseDetails;

    public Exercise() {
    }

    public Exercise(String name, Integer orderNumber) {
        this.name = name;
        this.orderNumber = orderNumber;
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

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
}
