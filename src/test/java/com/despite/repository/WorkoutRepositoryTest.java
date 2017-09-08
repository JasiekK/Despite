package com.despite.repository;

import com.despite.config.DaoConfig;
import com.despite.entities.*;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoConfig.class}, loader = AnnotationConfigContextLoader.class)
@Transactional
public class WorkoutRepositoryTest {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void test() {
        HashSet<WorkoutDetails> hash = new HashSet<>();

        Exercise exercise = exerciseRepository.save(new Exercise("exercise"));

        hash.add(new WorkoutDetails(exercise,1));
        Workout workout = new Workout("Nazwa treningu",
                new User("Imie", "haslo", Arrays.asList(new Role("USER"))),
                5, hash
        );

        Workout save = workoutRepository.save(new Workout("nazwa treningu", new User("Imie", "haslo", Arrays.asList(new Role("USER"))), 5, hash));
        Workout one = workoutRepository.findOne(save.getId());

        Gson gson = new Gson();
        System.out.println(gson.toJson(one));
    }

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private UserRepository userRepository;


}