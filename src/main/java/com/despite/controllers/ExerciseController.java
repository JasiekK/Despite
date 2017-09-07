package com.despite.controllers;

import com.despite.entities.Exercise;
import com.despite.entities.helper.ExerciseNotFound;
import com.despite.services.IExerciseService;
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
public class ExerciseController {

    @Autowired
    private IExerciseService exerciseService;

    @GetMapping("/exercises")
    public ResponseEntity<List<Exercise>> getExercises() {
        return exerciseService.findAllExercise()
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @GetMapping("/exercises/{exerciseId}")
    public ResponseEntity<Exercise> getExercise(@PathVariable Long exerciseId) {
        return exerciseService.findExerciseById(exerciseId)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @GetMapping("/workouts/{workoutsId}/exercises/{exercisesId}")
    public ResponseEntity<Exercise> getExercisesFromWorkout(@PathVariable Long workoutsId,
                                                            @PathVariable Long exercisesId) {
        return exerciseService.findExerciseByIdAndWorkoutId(workoutsId, exercisesId)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @PostMapping("/exercises")
    public ResponseEntity createExercise(@RequestBody Exercise exercise, Principal principal, UriComponentsBuilder b) {
        Optional<Long> exerciseId = exerciseService.insert(exercise, principal);
        UriComponents uriComponents = b.path("/api/exercises/{exerciseId}").buildAndExpand(exerciseId.get());
        return ResponseEntity.created(uriComponents.toUri()).build();
    }

    @PutMapping("/exercises/{exerciseId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "exercise updated")
    public void updateExercise(@RequestBody Exercise exercise, @PathVariable Long exerciseId, Principal principal) throws ExerciseNotFound {
        if (exerciseService.checkIfExist(exerciseId)) {
            exerciseService.insert(exercise, principal);
        } else {
            throw new ExerciseNotFound(String.format("Workout not found, workout_id: %d", exerciseId));
        }
    }

    @DeleteMapping("/exercises/{exerciseId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "exercise deleted")
    public void deleteExercise(@PathVariable Long exerciseId, Principal principal) throws ExerciseNotFound {
        if (exerciseService.checkIfExist(exerciseId)) {
            exerciseService.deleteExerciseByIdPrincipal(exerciseId, principal);
        }else {
            throw new ExerciseNotFound(String.format("Workout not found, workout_id: %d", exerciseId));
        }
    }

}
