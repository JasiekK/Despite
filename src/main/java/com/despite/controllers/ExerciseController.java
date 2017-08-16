package com.despite.controllers;

import com.despite.entities.Exercise;
import com.despite.services.IExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ExerciseController {

    @Autowired
    private IExerciseService exerciseService;

    @GetMapping("/exercises")
    public ResponseEntity<List<Exercise>> exercises(){
        return exerciseService.findAllExercise()
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @GetMapping("exercises/{exerciseId}")
    public ResponseEntity<Exercise> exercise(@PathVariable Long exerciseId){
        return exerciseService.findExerciseById(exerciseId)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }


}
