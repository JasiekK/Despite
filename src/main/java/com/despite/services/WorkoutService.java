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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WorkoutService implements IWorkoutService{

    private final Logger log = LogManager.getLogger(WorkoutService.class);

    private WorkoutRepository workoutRepository;
    private PrincipalResolver principalResolver;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository, PrincipalResolver principalResolver) {
        this.workoutRepository = workoutRepository;
        this.principalResolver = principalResolver;
    }

    @Override
    public void insert(Workout workout, Principal principal) {
        workout.setCreator(principalResolver.getUser(principal));
        workout.setCreator(principalResolver.getUser(principal));
        workoutRepository.save(workout);

        log.info("add new workout: {}, by: {}", workout.getName(), principal.getName());
    }

    @Override
    public Optional<List<Workout>> findByUserId(Long userId) {
            return Optional.ofNullable(workoutRepository.findByCreatorId(userId));
    }

}
