package com.despite.controllers

import com.despite.entities.Workout
import com.despite.services.helper.WithCustomUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@SpringBootTest
class WorkoutControllerSpec extends Specification {

    @Shared
    String PATH = "http://localhost:8080/api/workouts"

    @Shared
    MockMvc mvc

    @Autowired
    WebApplicationContext context


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

        setup:
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build()

        when: "get list of workout - get method - /api/workouts"
        MvcResult mvcResult = mvc.perform(get(PATH)).andReturn()

        then: "response status should be - ok"
        mvcResult.response.status == 200

        and: "response list of workout should be empty"
        mvcResult.response.getContentAsString() == [].toString()
    }
}
