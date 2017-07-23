package com.despite.repository;

import com.despite.entities.Exercise;
import com.despite.entities.Role;
import com.despite.entities.User;
import com.despite.entities.Workout;
import com.google.gson.Gson;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

public class WorkoutRepositoryTest {

    @Test
    public void test() {
        Workout workout = new Workout("Nazwa treningu",
                new User("Imie", "haslo", Arrays.asList(new Role("USER"))),
                5,
                new HashSet<>(Arrays.asList(
                        new Exercise("berpisy", 1),
                        new Exercise("przysiady", 2),
                        new Exercise("pompki", 3),
                        new Exercise("brzuszi", 4)))
        );

        Gson gson = new Gson();
        System.out.println(gson.toJson(workout));
    }


}