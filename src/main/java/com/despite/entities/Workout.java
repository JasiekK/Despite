package com.despite.entities;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class Workout {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    private String name;
    @ManyToOne
    private User creator;
    @NotNull
    private int sets;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "workout_id")
    private Set<WorkoutDetails> workoutDetails;

    public Workout() {
    }

    public Workout(String name, User creator, int sets, Set<WorkoutDetails> workoutDetails) {
        this.name = name;
        this.creator = creator;
        this.sets = sets;
        this.workoutDetails = workoutDetails;
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

    public Set<WorkoutDetails> getWorkoutDetails() {
        return workoutDetails;
    }

    public void setWorkoutDetails(Set<WorkoutDetails> workoutDetails) {
        this.workoutDetails = workoutDetails;
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

}
