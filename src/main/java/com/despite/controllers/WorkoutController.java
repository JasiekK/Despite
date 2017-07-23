package com.despite.controllers;

import com.despite.entities.Workout;
import com.despite.services.IWorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class WorkoutController {

    @Autowired
    private IWorkoutService workoutService;

    @GetMapping("/workouts")
    public ResponseEntity<List<Workout>> workout() {
        return workoutService.findAllWorkout()
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @GetMapping("/workouts/{userId}")
    public ResponseEntity<List<Workout>> workoutById(@PathVariable Long userId) {
        return workoutService.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @PostMapping("/workouts")
    public ResponseEntity addWorkout(@RequestBody Workout workout, Principal principal, UriComponentsBuilder b) {
        Optional<Long> workoutsId = workoutService.insert(workout, principal);
        UriComponents uriComponents = b.path("/api/workouts/{workoutsId}").buildAndExpand(workoutsId);
        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @PutMapping("/workouts/{workoutId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "workout updated!")
    public void updateWorkout(@RequestBody Workout workout, @PathVariable Long workoutId) {
        workoutService.updateWorkout(workout, workoutId);
    }
}
