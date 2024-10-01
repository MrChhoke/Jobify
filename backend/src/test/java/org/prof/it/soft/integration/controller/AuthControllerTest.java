package org.prof.it.soft.integration.controller;

import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.prof.it.soft.entity.security.Permission;
import org.prof.it.soft.entity.security.User;
import org.prof.it.soft.integration.annotation.IT;
import org.prof.it.soft.integration.container.ControllerPostgresqlContainer;
import org.prof.it.soft.repo.UserRepository;
import org.prof.it.soft.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.prof.it.soft.matcher.JwtTokenMatcher.JwtTokenMatcher;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
@AutoConfigureMockMvc
class AuthControllerTest {

    @ClassRule
    public static ControllerPostgresqlContainer controllerPostgresqlContainer = ControllerPostgresqlContainer.getInstance();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void login_shouldReturnOk_whenCredentialIsRight() throws Exception {
        User user = User.builder()
                .username("test")
                .password("test")
                .permissions(Set.of(Permission.CREATE_RECRUITER, Permission.VIEW_RECRUITER))
                .build();

        userService.save(user);

        String request = """
                {
                    "username": "test",
                    "password": "test"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.token").value(JwtTokenMatcher()))
                .andExpect(jsonPath("$.expiration_date").exists())
                .andExpect(jsonPath("$.expiration_date").isNotEmpty());
    }

    @Test
    void login_shouldReturnBadRequest_whenPasswordIsWrong() throws Exception {
        User user = User.builder()
                .username("test")
                .password("test")
                .permissions(Set.of(Permission.CREATE_RECRUITER, Permission.VIEW_RECRUITER))
                .build();

        userService.save(user);

        String request = """
                {
                    "username": "test",
                    "password": "wrong"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors[0]").value("Invalid password"));
    }



    @Test
    void login_shouldReturnBadRequest_whenUserDoesNotExist() throws Exception {
        String request = """
                {
                    "username": "test",
                    "password": "test"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors[0]").value("User not found"));
    }

    @Test
    void login_shouldReturnBadRequest_whenUserDoesNotExist_2() throws Exception {
        User user = User.builder()
                .username("test")
                .password("test")
                .permissions(Set.of(Permission.CREATE_RECRUITER, Permission.VIEW_RECRUITER))
                .build();

        userService.save(user);

        String request = """
                {
                    "username": "test2",
                    "password": "test"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors[0]").value("User not found"));
    }

    @Test
    void register_shouldReturnOk_whenUserIsCreated() throws Exception {
        String request = """
                {
                    "username": "test",
                    "password": "test"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.token").value(JwtTokenMatcher()))
                .andExpect(jsonPath("$.expiration_date").exists())
                .andExpect(jsonPath("$.expiration_date").isNotEmpty());
    }

    @Test
    void register_shouldReturnBadRequest_whenUserAlreadyExists() throws Exception {
        User user = User.builder()
                .username("test")
                .password("test")
                .permissions(Set.of(Permission.CREATE_RECRUITER, Permission.VIEW_RECRUITER))
                .build();

        userService.save(user);

        String request = """
                {
                    "username": "test",
                    "password": "test"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors[0]").value("User with such username already exists"));
    }

    @Test
    void register_shouldReturnBadRequest_whenUserAlreadyExists_2() throws Exception {
        User user = User.builder()
                .username("test")
                .password("test")
                .permissions(Set.of(Permission.CREATE_RECRUITER, Permission.VIEW_RECRUITER))
                .build();

        userService.save(user);

        String request = """
                {
                    "username": "test",
                    "password": "test"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors[0]").value("User with such username already exists"));
    }

    @Test
    void register_shouldReturnBadRequest_whenUserAlreadyExists_3() throws Exception {
        // Given
        String request = """
                {
                    "username": "test",
                    "password": "test"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.token").value(JwtTokenMatcher()))
                .andExpect(jsonPath("$.expiration_date").exists())
                .andExpect(jsonPath("$.expiration_date").isNotEmpty());

        // When
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors[0]").value("User with such username already exists"));
    }
}