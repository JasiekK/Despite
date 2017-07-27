package com.despite.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Workout {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @ManyToOne
    private User creator;
    private int sets;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "workout_id")
    private Set<Exercise> exercises;

    public Workout() {
    }

    public Workout(String name, User creator, int sets, Set<Exercise> exercises) {
        this.name = name;
        this.creator = creator;
        this.sets = sets;
        this.exercises = exercises;
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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public Set<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(Set<Exercise> exercises) {
        this.exercises = exercises;
    }

}
