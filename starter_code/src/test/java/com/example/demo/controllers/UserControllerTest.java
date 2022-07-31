package com.example.demo.controllers;

import com.example.demo.model.requests.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.example.demo.controllers.TestUtils.injectValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest extends TestBase {
    private UserController userController;
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @BeforeEach
    public void init() {
        userController = new UserController();
        injectValue(userController, "userRepository", userRepository);
        injectValue(userController, "cartRepository", cartRepository);
        injectValue(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    private void withEncodePassword() {
        when(bCryptPasswordEncoder.encode("password")).thenReturn("hashed-password");
    }

    @Test
    void can_find_exists_user_by_id() {
        withExistsUser(1);
        var response = userController.findById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void can_not_find_not_exists_user_by_id() {
        withExistsUser(1);
        var response = userController.findById(2L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void can_find_exists_user_by_username() {
        withExistsUser(1);
        var response = userController.findByUserName("admin");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("admin", response.getBody().getUsername());
    }

    @Test
    void can_not_find_not_exists_user_by_username() {
        withExistsUser(1);
        var response = userController.findByUserName("not_exists_user");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void can_create_user_with_valid_info() {
        withEncodePassword();

        var request = new CreateUserRequest();
        request.setUsername("admin");
        request.setPassword("password");
        request.setConfirmPassword("password");
        var response = userController.createUser(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("admin", response.getBody().getUsername());
        assertNotEquals("password", response.getBody().getPassword());
    }

    @Test
    void can_not_create_user_with_invalid_username() {
        withEncodePassword();

        var request = new CreateUserRequest();
        request.setUsername("adm");
        request.setPassword("password");
        request.setConfirmPassword("password");
        var response = userController.createUser(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void can_not_create_user_with_invalid_password() {
        withEncodePassword();

        var request = new CreateUserRequest();
        request.setUsername("admin");
        request.setPassword("pss");
        request.setConfirmPassword("pss");
        var response = userController.createUser(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void can_not_create_user_with_invalid_confirm_password() {
        withEncodePassword();

        var request = new CreateUserRequest();
        request.setUsername("admin");
        request.setPassword("password");
        request.setConfirmPassword("password1");
        var response = userController.createUser(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    void can_not_create_user_with_duplicated_username() {
        withExistsUser(1);
        withEncodePassword();

        var request = new CreateUserRequest();
        request.setUsername("admin");
        request.setPassword("password");
        request.setConfirmPassword("password");
        var response = userController.createUser(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}