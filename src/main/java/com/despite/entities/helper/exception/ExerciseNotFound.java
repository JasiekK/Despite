package com.despite.entities.helper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExerciseNotFound extends Exception {
    public ExerciseNotFound(String message) {
        super(message);
    }
}
