package com.despite.services;

import com.despite.entities.Workout;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface IWorkoutService {
    void insert(Workout workout, Principal principal);
    Optional<List<Workout>> findByUserId(Long userId);

}
