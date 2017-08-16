package com.despite.controllers;

import com.despite.entities.Workout;
import com.despite.entities.helper.WorkoutNotFound;
import com.despite.services.IWorkoutService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
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

    @GetMapping("/workouts/{workoutsId}")
    public ResponseEntity<Workout> workoutById(@PathVariable Long workoutsId) {
        return workoutService.findByWorkoutsId(workoutsId)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @PostMapping("/workouts")
    public ResponseEntity addWorkout(@RequestBody Workout workout, Principal principal, UriComponentsBuilder b) {
        Optional<Long> workoutsId = workoutService.insert(workout, principal);
        UriComponents uriComponents = b.path("/api/workouts/{workoutsId}").buildAndExpand(workoutsId.get());
        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @PutMapping("/workouts/{workoutId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "workout updated")
    public void updateWorkout(@RequestBody Workout workout, @PathVariable Long workoutId, Principal principal) throws WorkoutNotFound {
        if (workoutService.checkIfExist(workoutId)) {
            workoutService.updateWorkout(workout, principal);
        } else {
            throw new WorkoutNotFound(String.format("Workout not found, workout_id: %d", workoutId));
        }
    }

    @DeleteMapping("/workouts/{workoutId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "workout deleted")
    public void deleteWorkout(@PathVariable Long workoutId, Principal principal) throws WorkoutNotFound {
        if (workoutService.checkIfExist(workoutId)) {
            workoutService.delete(workoutId, principal);
        } else {
            throw new WorkoutNotFound(String.format("Workout not found, workout_id: %d", workoutId));
        }
    }
}
