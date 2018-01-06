package com.despite.controllers

import com.despite.config.helper.WithCustomUser
import com.despite.entities.*
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@SpringBootTest
@Transactional
class WorkoutControllerSpec extends Specification {

    @Shared
    def WORKOUT_PATH = 'http://localhost:8080/api/workouts'

    @Shared
    def USER_PATH = 'http://localhost:8080/addtestuser'

    @Shared
    MockMvc mvc

    @Autowired
    WebApplicationContext context

    @Shared
    Workout workout

    @Shared
    User user

    @Shared
    Gson gson = new Gson()

    def setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build()
        user = new User(id: WithCustomUser.annotationType.memberDefaults().get('id'),
                username: WithCustomUser.annotationType.memberDefaults().get('userName'))
        workout = populateWorkout(user)
    }

    def 'Get list of workout without basic auth should not be possible'() {

        when: 'get list of workout - get method - /api/workouts'
        MvcResult mvcResult = mvc.perform(get(WORKOUT_PATH)).andReturn()

        then: 'response status should be - unauthorised'
        mvcResult.response.status == 401
    }

    @WithCustomUser
    def 'Get list of workout with basic auth should be possible'() {

        when: 'get list of workout - get method - /api/workouts'
        MvcResult mvcResult = mvc.perform(get(WORKOUT_PATH)).andReturn()

        then: 'response status should be - ok : 200'
        mvcResult.response.status == 200

        and: 'response list of workout should be empty'
        mvcResult.response.getContentAsString() == [] as String
    }

    @WithCustomUser
    def 'Create new workout save then try to get this one using find by id method'() {

        setup: 'create new workout object'
        MvcResult mvcResult = mvc.perform(post(WORKOUT_PATH)
                .contentType('application/json;charset=UTF-8')
                .content(gson.toJson(workout)))
                .andReturn()

        when: 'response status should be - created : 201 '
        mvcResult.response.status == 201

        and: 'get workout id from response headers'
        Long id = getIdFromUri(mvcResult.response.headers.get('Location').value as String)

        then: 'get workout by id'
        def pathAndId = WORKOUT_PATH + '/' + id.toString()
        MvcResult mvcResultById = mvc.perform(get(pathAndId)).andReturn()

        then: 'response status should be ok - 200'
        mvcResultById.response.status == 200

        and: 'id\'s should be equal'
        gson.fromJson(mvcResultById.response.getContentAsString(), Workout).id == id
    }

    @WithCustomUser
    def 'Create new workout save then try to modify'() {

        setup: 'create and post new workout'
        MvcResult mvcResult = mvc.perform(post(WORKOUT_PATH)
                .contentType('application/json;charset=UTF-8')
                .content(gson.toJson(workout)))
                .andReturn()

        when: 'response status should be - created : 201 '
        mvcResult.response.status == 201

        and: 'get workout id from response headers'
        Long id = getIdFromUri(mvcResult.response.headers.get('Location').value as String)

        then: 'get this workout by id'
        def pathAndId = WORKOUT_PATH + '/' + id.toString()
        MvcResult mvcGetResult = mvc.perform(get(pathAndId)).andReturn()

        and: 'response status should be ok - 200'
        mvcGetResult.response.status == 200

        and: 'update workout'
        Workout updatedWorkout = gson.fromJson(mvcGetResult.response.getContentAsString(), Workout)
        updatedWorkout.setSets(999)
        updatedWorkout.setName('test name')

        then: 'put updated workout'
        MvcResult mvcPUTResult = mvc
                .perform(put(pathAndId)
                .contentType('application/json;charset=UTF-8')
                .content(gson.toJson(updatedWorkout)))
                .andReturn()

        and: 'response status should be NO CONTENT - 204 and reason message - workout updated'
        mvcPUTResult.response.status == 204
        mvcPUTResult.response.errorMessage == 'workout updated'

        then: 'get updated workout by id'
        MvcResult mvcGetResultAfterUpdated = mvc.perform(get(pathAndId)).andReturn()
        Workout workoutAfterUpdated = gson.fromJson(mvcGetResultAfterUpdated.response.getContentAsString(), Workout)

        and: 'sets and name should be changed'
        workoutAfterUpdated.getSets() == 999
        workoutAfterUpdated.getName() == 'test name'
    }

    @WithCustomUser
    def 'Create workout then try to delete'() {

        setup: 'create and post new workout'
        MvcResult mvcResult = mvc.perform(post(WORKOUT_PATH)
                .contentType('application/json;charset=UTF-8')
                .content(gson.toJson(workout)))
                .andReturn()

        when: 'response status should be - created : 201'
        mvcResult.response.status == 201

        and: 'get workout id from response headers'
        Long id = getIdFromUri(mvcResult.response.headers.get('Location').value as String)

        then: 'delete workout by id'
        def pathAndId = WORKOUT_PATH + '/' + id.toString()
        MvcResult mvcDeleteResult = mvc.perform(delete(pathAndId)).andReturn()

        and: 'response status should be NO CONTENT - 204 and reason message - workout deleted'
        mvcDeleteResult.response.status == 204
        mvcDeleteResult.response.errorMessage == 'workout deleted'
    }

    Workout populateWorkout(User user) {

        HashSet<WorkoutDetails> hashSet = new HashSet<>()
        hashSet.add(new WorkoutDetails(new Exercise('e1'), 1))
        hashSet.add(new WorkoutDetails(new Exercise('e2'), 2))
        hashSet.add(new WorkoutDetails(new Exercise('e3'), 3))
        hashSet.add(new WorkoutDetails(new Exercise('e4'), 4))

        new Workout('WorkoutName', user, 5, hashSet)
    }

    Long getIdFromUri(String url) {
        Long.parseLong(url.substring(url.lastIndexOf('/') + 1))
    }

}
