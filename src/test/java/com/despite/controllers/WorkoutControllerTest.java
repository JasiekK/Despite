package com.despite.controllers;

import com.despite.entities.*;
import com.despite.services.IWorkoutService;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(WorkoutController.class)
public class WorkoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IWorkoutService workoutService;

    private Workout workout;

    private Gson gson;

    @Before
    public void setUp() throws Exception {

        gson = new Gson();
        HashSet<WorkoutDetails> hashSet = new HashSet<>();

        hashSet.add(new WorkoutDetails(new Exercise("e1"),1));
        hashSet.add(new WorkoutDetails(new Exercise("e2"),2));
        hashSet.add(new WorkoutDetails(new Exercise("e3"),3));
        hashSet.add(new WorkoutDetails(new Exercise("e4"),4));

        workout = new Workout("WorkoutName",
                new User("userName", "password", Arrays.asList(new Role("USER"))),
                5, hashSet
        );

    }

    @Test
    public void shouldReturn401WhenYouDoNotHaveProperAuthentication() throws Exception {
        mockMvc.perform(get("/api/workouts")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", password = "user")
    public void shouldReturn200AndEmptyBodyWhenAnyWorkoutDoNotExist() throws Exception {

        given(workoutService.findAllWorkout()).willReturn(Optional.empty());
        mockMvc.perform(get("/api/workouts"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    @WithMockUser(username = "user", password = "user")
    public void shouldReturn404WhenWorkoutDoNotExist() throws Exception {

        given(workoutService.findByWorkoutsId(anyLong())).willReturn(Optional.empty());
        mockMvc.perform(get("/api/workouts/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", password = "user")
    public void shouldReturn200WhenWorkoutExist() throws Exception {

        given(workoutService.findByWorkoutsId(anyLong())).willReturn(Optional.of(workout));
        mockMvc.perform(get("/api/workouts/{id}", 1L))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk());
//                .andExpect(content().json(gson.toJson(workout)));
        // TODO PROBLEM with workout - JsonProperty.Access.WRITE_ONLY
    }

    @Test
    @WithMockUser(username = "user", password = "user")
    public void shouldReturn201WhenInsertNewWorkout() throws Exception {

        given(workoutService.insert(anyObject(), anyObject())).willReturn(Optional.of(1L));
        mockMvc.perform(post("/api/workouts/")
                .contentType("application/json;charset=UTF-8")
                .content(gson.toJson(workout)))

                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("http://localhost/api/workouts/1"));
    }

    @Test
    @WithMockUser(username = "user", password = "user")
    public void shouldReturn404WhenTryUpdateWorkoutWhichDoNotExist() throws Exception {

        given(workoutService.checkIfExist(anyLong())).willReturn(false);
        mockMvc.perform(put("/api/workouts/{workoutId}", 1L)
                .contentType("application/json;charset=UTF-8")
                .content(gson.toJson(workout)))

                .andExpect(status().isNotFound());

    }

    @Test
    @WithMockUser(username = "user", password = "user")
    public void shouldReturn204WhenUpdateWorkout() throws Exception {

        given(workoutService.checkIfExist((anyLong()))).willReturn(true);
        mockMvc.perform(put("/api/workouts/{workoutId}", 1L)
                .contentType("application/json;charset=UTF-8")
                .content(gson.toJson(workout)))

                .andExpect(status().isNoContent())
                .andExpect(status().reason("workout updated"));
    }

    @Test
    @WithMockUser(username = "user", password = "user")
    public void shouldReturn404WhenTryDeleteWorkoutWhichDoNotExist() throws Exception {

        given(workoutService.checkIfExist(anyLong())).willReturn(false);
        mockMvc.perform(delete("/api/workouts/{workoutId}", 1L)
                .contentType("application/json;charset=UTF-8"))

                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", password = "user")
    public void shouldReturn204WhenDeleteWorkout() throws Exception {

        given(workoutService.checkIfExist(anyLong())).willReturn(true);
        mockMvc.perform(delete("/api/workouts/{workoutId}", 1L)
                .contentType("application/json;charset=UTF-8"))

                .andExpect(status().isNoContent())
                .andExpect(status().reason("workout deleted"));
    }
}