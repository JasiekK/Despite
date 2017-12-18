package com.despite.services;

import com.despite.entities.Exercise;
import com.despite.entities.Workout;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface IExerciseService {
    Optional<List<Exercise>> findAllExercise();

    Optional<Exercise> findExerciseById(Long exerciseId);

    Optional<Long> insert(Exercise exercise, Principal principal);

    Optional<Exercise> findExerciseByIdAndWorkoutId(Long workoutId, Long exercisesId);

    boolean checkIfExist(Long exerciseId);

    void deleteExerciseByIdPrincipal(Long exerciseId, Principal principal);
}
