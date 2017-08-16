package com.despite.services;

import com.despite.entities.Exercise;

import java.util.List;
import java.util.Optional;

public interface IExerciseService {
    Optional<List<Exercise>> findAllExercise();
    Optional<Exercise> findExerciseById(Long exerciseId);
}
