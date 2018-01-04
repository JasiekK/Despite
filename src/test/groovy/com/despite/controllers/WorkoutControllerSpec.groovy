package com.despite.controllers

import com.despite.config.helper.WithCustomUser
import com.despite.entities.*
import com.despite.repository.UserRepository
import com.despite.services.helper.PrincipalResolver
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.ResponseEntity
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@SpringBootTest
class WorkoutControllerSpec extends Specification {

    @Shared
    String PATH = "http://localhost:8080/api/workouts"

    @Shared
    MockMvc mvc

    @Autowired
    WebApplicationContext context

    @Shared
    Workout workout

    @Shared
    Gson gson = new Gson()

    def userRepository = Mock(UserRepository)

    PrincipalResolver principalResolver

    def setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build()
        principalResolver= new PrincipalResolver(userRepository)

        workout = populateWorkout()
    }

    def "Get list of workout without basic auth should not be possible"() {

        setup: "create rest template without basic auth"
        TestRestTemplate testRestTemplateWithoutBasicAuth = new TestRestTemplate()

        when: "get list of workout - get method - /api/workouts"
        ResponseEntity responseEntity = testRestTemplateWithoutBasicAuth.getForEntity(PATH, Workout)

        then: "response status should be - unauthorised"
        responseEntity.statusCode.name() == 'UNAUTHORIZED'
        responseEntity.statusCode.value() == 401
    }

    @WithCustomUser
    def "Get list of workout with basic auth should be possible"() {

        when: "get list of workout - get method - /api/workouts"
        MvcResult mvcResult = mvc.perform(get(PATH)).andReturn()

        then: "response status should be - ok : 200"
        mvcResult.response.status == 200

        and: "response list of workout should be empty"
        mvcResult.response.getContentAsString() == [].toString()
    }

    @WithMockUser
    def "Create new workout save then try to get this one using find by id method"() {

        setup: "create new workout object"
        MvcResult mvcResult = mvc.perform(post(PATH)
                .contentType("application/json;charset=UTF-8")
                .content(gson.toJson(workout)))
                .andReturn()

        when: "response status should be - created : 201 "
        mvcResult.response.status == 201

        and: "get workout id from response headers"
        Long id = getIdFromUri(mvcResult.response.headers.get("Location").value)

        then: "get workout by id"
        def pathAndId = PATH + "/" + id.toString()
        MvcResult mvcResultById = mvc.perform(get(pathAndId)).andReturn()

        then: "response status should be ok - 200"
        mvcResultById.response.status == 200

        and: "id's should be equal"
        gson.fromJson(mvcResultById.response.getContentAsString(), Workout).id == id
    }

    @WithMockUser
    def "Create new workout save then try to modify"() {

        setup: "create and post new workout"
        MvcResult mvcResult = mvc.perform(post(PATH)
                .contentType("application/json;charset=UTF-8")
                .content(gson.toJson(workout)))
                .andReturn()

        when: "response status should be - created : 201 "
        mvcResult.response.status == 201

        and: "get workout id from response headers"
        Long id = getIdFromUri(mvcResult.response.headers.get("Location").value)

        then: "get this workout by id"
        def pathAndId = PATH + "/" + id.toString()
        MvcResult mvcGetResult = mvc.perform(get(pathAndId)).andReturn()

        and: "response status should be ok - 200"
        mvcGetResult.response.status == 200

        and: "update workout"
        Workout updatedWorkout = gson.fromJson(mvcGetResult.response.getContentAsString(), Workout)
        updatedWorkout.setSets(999)
        updatedWorkout.setName("test name")

        then: "put updated workout"
        MvcResult mvcPUTResult = mvc
                .perform(put(pathAndId)
                .contentType("application/json;charset=UTF-8")
                .content(gson.toJson(updatedWorkout)))
                .andReturn()

        and: "response status should be NO CONTENT - 204"
        mvcPUTResult.response.status == 204

        then: "get updated workout by id"
        MvcResult mvcGetResultAfterUpdated = mvc.perform(get(pathAndId)).andReturn()
        Workout workoutAfterUpdated = gson.fromJson(mvcGetResultAfterUpdated.response.getContentAsString(), Workout)

        and: "sets and name should be changed"
        workoutAfterUpdated.getSets() == 999
        workoutAfterUpdated.getName() == "test name"
    }

    @WithMockUser
    def "Create workout then try to delete"() {

        setup: "create and post new workout"
        MvcResult mvcResult = mvc.perform(post(PATH)
                .contentType("application/json;charset=UTF-8")
                .content(gson.toJson(workout)))
                .andReturn()

        when: "response status should be - created : 201"
        mvcResult.response.status == 201

        and: "get workout id from response headers"
        Long id = getIdFromUri(mvcResult.response.headers.get("Location").value)

        then: "delete workout by id"
        def pathAndId = PATH + "/" + id.toString()
        MvcResult mvcGetResult = mvc.perform(delete(pathAndId)).andReturn()

        and: "response status should be NO CONTENT - 204"
        mvcGetResult.response.status == 204
    }

    private static Workout populateWorkout() {

        HashSet<WorkoutDetails> hashSet = new HashSet<>()
        hashSet.add(new WorkoutDetails(new Exercise("e1"), 1))
        hashSet.add(new WorkoutDetails(new Exercise("e2"), 2))
        hashSet.add(new WorkoutDetails(new Exercise("e3"), 3))
        hashSet.add(new WorkoutDetails(new Exercise("e4"), 4))

        return new Workout("WorkoutName",
                new User("user", "user", Arrays.asList(
                        new Role("ROLE_USER"))), 5, hashSet)
    }

    private static Long getIdFromUri(String url) {
        return Long.parseLong(url.substring(url.lastIndexOf('/') + 1))
    }

}
