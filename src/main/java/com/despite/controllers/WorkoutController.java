package com.despite.controllers;

import com.despite.entities.Workout;
import com.despite.entities.helper.WorkoutNotFound;
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
    public ResponseEntity<List<Workout>> getWorkouts() {
        return workoutService.findAllWorkout()
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.ok()::build);
    }

    @PostMapping("/workouts")
    public ResponseEntity createWorkout(@RequestBody Workout workout, Principal principal, UriComponentsBuilder b) {
        Optional<Long> workoutsId = workoutService.insert(workout, principal);
        UriComponents uriComponents = b.path("/api/workouts/{workoutsId}").buildAndExpand(workoutsId.get());
        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @GetMapping("/workouts/{workoutsId}")
    public ResponseEntity<Workout> getWorkoutById(@PathVariable Long workoutsId) {
        return workoutService.findByWorkoutsId(workoutsId)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @PutMapping("/workouts/{workoutId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "workout updated")
    public void updateWorkout(@RequestBody Workout workout, @PathVariable Long workoutId, Principal principal) throws WorkoutNotFound {
        if (workoutService.checkIfExist(workoutId)) {
            workoutService.insert(workout, principal);
        } else {
            throw new WorkoutNotFound(String.format("Workout not found, workout_id: %d", workoutId));
        }
    }

    @DeleteMapping("/workouts/{workoutId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "workout deleted")
    public void deleteWorkout(@PathVariable Long workoutId, Principal principal) throws WorkoutNotFound {
        if (workoutService.checkIfExist(workoutId)) {
            workoutService.deleteWorkoutByIdAndPrincipal(workoutId, principal);
        } else {
            throw new WorkoutNotFound(String.format("Workout not found, workout_id: %d", workoutId));
        }
    }
}
