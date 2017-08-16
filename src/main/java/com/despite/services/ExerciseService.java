package com.despite.services;

import com.despite.entities.Exercise;
import com.despite.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ExerciseService implements IExerciseService {

    private ExerciseRepository exerciseRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public Optional<List<Exercise>> findAllExercise() {
        return Optional.ofNullable(exerciseRepository.findAllByPrincipal());
    }

    @Override
    public Optional<Exercise> findExerciseById(Long exerciseId) {
     return Optional.ofNullable(exerciseRepository.findOne(exerciseId));
    }
}
