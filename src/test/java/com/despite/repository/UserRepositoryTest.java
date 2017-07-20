package com.despite.repository;

import com.despite.config.DaoConfig;
import com.despite.entities.Role;
import com.despite.entities.User;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DaoConfig.class}, loader = AnnotationConfigContextLoader.class)
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private static final String NAME = "John";

    @Test
    public void saveNewUser() {

        userRepository.save(new User(NAME, "password", Arrays.asList(new Role("ROLE_USER"))));
        User user = userRepository.findByUserName(NAME);

        assertEquals("Name is incorrect !", NAME, user.getUserName());
    }

}