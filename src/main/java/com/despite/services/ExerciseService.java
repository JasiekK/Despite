package com.despite.services;

import com.despite.entities.Exercise;
import com.despite.repository.ExerciseRepository;
import com.despite.repository.WorkoutDetailsRepository;
import com.despite.services.helper.PrincipalResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ExerciseService implements IExerciseService {

    private ExerciseRepository exerciseRepository;
    private PrincipalResolver principalResolver;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, PrincipalResolver principalResolver) {
        this.exerciseRepository = exerciseRepository;
        this.principalResolver = principalResolver;
    }

    @Override
    public Optional<List<Exercise>> findAllExercise() {
        return Optional.ofNullable(exerciseRepository.findAllByPrincipal());
    }

    @Override
    public Optional<Exercise> findExerciseById(Long exerciseId) {
        return Optional.ofNullable(exerciseRepository.findOneByPrincipal(exerciseId));
    }

    @Override
    public Optional<Long> insert(Exercise exercise, Principal principal) {
        exercise.setCreator(principalResolver.getUser(principal));
        return Optional.ofNullable(exerciseRepository.save(exercise).getId());
    }

    @Override
    public Optional<Exercise> findExerciseByIdAndWorkoutId(Long workoutId, Long exercisesId) {
        return Optional.ofNullable(exerciseRepository.findExerciseByIdAndWorkoutId(workoutId, exercisesId));
    }


}
