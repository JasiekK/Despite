package com.despite.entities.helper;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WorkoutNotFound extends Exception {

    public WorkoutNotFound(String message) {
        super(message);
    }
}
