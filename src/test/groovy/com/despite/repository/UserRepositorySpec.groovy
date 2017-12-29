package com.despite.repository

import com.despite.entities.Role
import com.despite.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

@SpringBootTest
class UserRepositorySpec extends Specification {

    @Autowired
    UserRepository userRepository

    @Autowired
    PasswordEncoder passwordEncoder

    User user
    def USER_NAME = "Johny"
    def PASSWORD = "password"
    def ROLE = "USER_ROLE"

    def "test findByUsername"() {

        setup: "create and save new user "
        user = new User(USER_NAME, passwordEncoder.encode(PASSWORD), Arrays.asList(new Role(ROLE)))
        userRepository.save(user)

        when: "get user by name"
        def newUser = userRepository.findByUsername(USER_NAME)

        then: "user name should be equal"
        newUser.getUsername() == USER_NAME
    }
}
