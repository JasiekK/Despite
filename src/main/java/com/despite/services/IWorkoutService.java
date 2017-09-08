package com.despite.services;

import com.despite.entities.Workout;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface IWorkoutService {
    Optional<Long> insert(Workout workout, Principal principal);
    Optional<Workout> findByWorkoutsId(Long workoutsId);
    Optional<List<Workout>> findAllWorkout();
    void deleteWorkoutByIdAndPrincipal(Long workoutId, Principal creatorId);
    boolean checkIfExist(Long workoutId);
}
