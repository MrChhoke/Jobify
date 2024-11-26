package org.prof.it.soft.integration.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.ClassRule;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.prof.it.soft.integration.annotation.IT;
import org.prof.it.soft.integration.container.ControllerPostgresqlContainer;
import org.prof.it.soft.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProfileControllerWithAuthTest {

    @ClassRule
    public static ControllerPostgresqlContainer controllerPostgresqlContainer = ControllerPostgresqlContainer.getInstance();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    public void testUpdate_profileJustCreatedUser() throws Exception {
        // Given
        String request = """
                {
                    "username": "test",
                    "password": "test",
                    "first_name": "John",
                    "last_name": "Doe"
                }
                """;

        String token = JsonPath.read(mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), "$.token");

        // When
        String updateRequest = """
                {
                    "first_name": "John",
                    "last_name": "Doe"
                }
                """;

        mockMvc.perform(put("/api/v1/profile/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.first_name").value("John"))
                .andExpect(jsonPath("$.last_name").value("Doe"));
    }

    @Test
    @Order(2)
    public void testUpdate_updateProfileTwice() throws Exception {
        // Given
        String request = """
                {
                    "username": "test",
                    "password": "test",
                    "first_name": "John",
                    "last_name": "Doe"
                }
                """;

        String token = JsonPath.read(mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), "$.token");

        String updateRequest = """
                {
                    "first_name": "John",
                    "last_name": "Doe"
                }
                """;

        mockMvc.perform(put("/api/v1/profile/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.first_name").value("John"))
                .andExpect(jsonPath("$.last_name").value("Doe"));

        // When
        updateRequest = """
                {
                    "first_name": "Petro",
                    "last_name": "Mostavs"
                }
                """;

        mockMvc.perform(put("/api/v1/profile/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("test"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.first_name").value("Petro"))
                .andExpect(jsonPath("$.last_name").value("Mostavs"));
    }
}
