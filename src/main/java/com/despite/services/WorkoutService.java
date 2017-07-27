package com.despite.services;

import com.despite.entities.Workout;
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
    private PrincipalResolver principalResolver;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository, PrincipalResolver principalResolver) {
        this.workoutRepository = workoutRepository;
        this.principalResolver = principalResolver;
    }

    @Override
    public Optional<Long> insert(Workout workout, Principal principal) {
        workout.setCreator(principalResolver.getUser(principal));
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
    public void updateWorkout(Workout workout) {
        workoutRepository.save(workout);
    }

    @Override
    public void delete(Long workoutId, Principal principal) {
        Long id = principalResolver.getUser(principal).getId();
        workoutRepository.deleteByIdAndCreatorId(workoutId, id);
    }
}
