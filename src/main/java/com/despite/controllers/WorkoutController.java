package com.despite.controllers;

import com.despite.entities.Workout;
import com.despite.services.IWorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
public class WorkoutController {

    @Autowired
    private IWorkoutService workoutService;

    @GetMapping("/workout/{userId}")
    public ResponseEntity<List<Workout>> getWorkoutByUserId(@PathVariable Long userId) {
        return workoutService.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @PostMapping("/workout")
    public void addWorkout(@RequestBody Workout workout, Principal principal) {
        workoutService.insert(workout, principal);
    }
}
