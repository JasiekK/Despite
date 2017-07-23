package com.despite.services;

import com.despite.entities.Workout;
import com.despite.repository.WorkoutRepository;

import com.despite.services.helper.PrincipalResolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
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
    public Optional<List<Workout>> findByUserId(Long userId) {
        return Optional.ofNullable(workoutRepository.findAllByCreatorId(userId));
    }

    @Override
    public Optional<List<Workout>> findAllWorkout() {
        return Optional.ofNullable(workoutRepository.findAllByPrincipal());
    }

    @Override
    public void updateWorkout(Workout workout, Long workoutId) {
        if (Optional.ofNullable(workoutRepository.findOne(workout.getId())).isPresent())
            workoutRepository.save(workout);
        else
            throw new EntityNotFoundException(String.format("Workout with ID %d not exist!", workoutId));
    }
}
