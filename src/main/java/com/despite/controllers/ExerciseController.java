package com.despite.controllers;

import com.despite.entities.Exercise;
import com.despite.services.IExerciseService;
import com.despite.services.IWorkoutService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/exercises")
    public ResponseEntity createExercise(Exercise exercise, Principal principal, UriComponentsBuilder b) {
        Optional<Long> exerciseId = exerciseService.insert(exercise, principal);
        UriComponents uriComponents = b.path("/api/exercises/{exerciseId}").buildAndExpand(exerciseId.get());
        return ResponseEntity.created(uriComponents.toUri()).build();
    }


    @GetMapping("/workouts/{workoutsId}/exercises/{exercisesId}")
    public ResponseEntity<Exercise> getExercisesFromWorkout(@PathVariable Long workoutsId,
                                                            @PathVariable Long exercisesId) {

        return exerciseService.findExerciseByIdAndWorkoutId(workoutsId, exercisesId)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

}
