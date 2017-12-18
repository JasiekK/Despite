package com.despite.services;

import com.despite.entities.User;
import com.despite.entities.Workout;
import com.despite.repository.ExerciseRepository;
import com.despite.repository.WorkoutRepository;

import com.despite.services.helper.PrincipalResolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WorkoutService implements IWorkoutService {

    private final Logger log = LogManager.getLogger(WorkoutService.class);

    private WorkoutRepository workoutRepository;
    private ExerciseRepository exerciseRepository;
    private PrincipalResolver principalResolver;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository, ExerciseRepository exerciseRepository, PrincipalResolver principalResolver) {
        this.workoutRepository = workoutRepository;
        this.exerciseRepository = exerciseRepository;
        this.principalResolver = principalResolver;
    }

    @Override
    public Optional<Long> insert(Workout workout, Principal principal) {
        User user = principalResolver.getUser(principal);
        workout.setCreator(user);
        workout.getWorkoutDetails().forEach(workoutDetails -> {
            workoutDetails.getExercise().setCreator(user);
            exerciseRepository.save(workoutDetails.getExercise());

        });
        return Optional.ofNullable(workoutRepository.save(workout).getId());
    }

    @Override
    public Optional<Workout> findByWorkoutsId(Long workoutsId) {
        return Optional.ofNullable(workoutRepository.findOne(workoutsId));
    }

    @Override
    public Optional<List<Workout>> findAllWorkout() {
        return Optional.ofNullable(workoutRepository.findAllByPrincipal());
    }

    @Override
    public void deleteWorkoutByIdAndPrincipal(Long workoutId, Principal principal) {
        Long id = principalResolver.getUser(principal).getId();
        workoutRepository.deleteByIdAndCreatorId(workoutId, id);
    }

    @Override
    public boolean checkIfExist(Long workoutId) {
        return workoutRepository.exists(workoutId);
    }
}
