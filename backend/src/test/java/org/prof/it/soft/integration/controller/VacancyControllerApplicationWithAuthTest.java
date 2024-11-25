package org.prof.it.soft.integration.controller;

import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.prof.it.soft.entity.Person;
import org.prof.it.soft.entity.Recruiter;
import org.prof.it.soft.entity.Vacancy;
import org.prof.it.soft.entity.security.Permission;
import org.prof.it.soft.entity.security.User;
import org.prof.it.soft.integration.annotation.IT;
import org.prof.it.soft.integration.container.ControllerPostgresqlContainer;
import org.prof.it.soft.repo.CandidateApplicationRepository;
import org.prof.it.soft.repo.RecruiterRepository;
import org.prof.it.soft.repo.VacancyRepository;
import org.prof.it.soft.service.JwtService;
import org.prof.it.soft.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class VacancyControllerApplicationWithAuthTest {

    @ClassRule
    public static ControllerPostgresqlContainer controllerPostgresqlContainer = ControllerPostgresqlContainer.getInstance();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private VacancyRepository vacancyRepository;

    @Autowired
    private CandidateApplicationRepository candidateApplicationRepository;

    @BeforeEach
    void setUp() {
        candidateApplicationRepository.deleteAll();
        vacancyRepository.deleteAll();
        recruiterRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testApplyForVacancy_whenVacancyExists() throws Exception {
        // Given
        Recruiter savedRecruiter = recruiterRepository.saveAndFlush(
                Recruiter.builder()
                        .username("recruiter")
                        .password("recruiter")
                        .companyName("Google")
                        .firstName("Anna")
                        .lastName("Petrov")
                        .build()
        );

        Vacancy savedVacancy = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Java Developer")
                        .salary(1000.0f)
                        .technologyStack(List.of("Java", "Spring"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        User user = Person.builder()
                .username("test")
                .password("test")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Set.of(Permission.APPLY_VACANCY))
                .firstName("John")
                .lastName("Doe")
                .build();

        User savedUser = userService.save(user);

        String jwtToken = jwtService.generateToken(savedUser);

        String requestBody = """
                {
                }
                """;

        // When and then
        mockMvc.perform(post("/api/v1/vacancy/{id}/apply", savedVacancy.getId())
                        .contentType("application/json")
                        .header("Authorization", "Bearer %s".formatted(jwtToken))
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Application created successfully"))
                .andExpect(jsonPath("$.candidate_application_id").isNumber())
                .andExpect(jsonPath("$.candidate_id").value(savedUser.getId()))
                .andExpect(jsonPath("$.vacancy_id").value(savedVacancy.getId()));
    }

    @Test
    @Order(2)
    public void testApplyForVacancy_whenYouDidItBefore() throws Exception {
        // Given
        Recruiter savedRecruiter = recruiterRepository.saveAndFlush(
                Recruiter.builder()
                        .username("recruiter")
                        .password("recruiter")
                        .companyName("Google")
                        .firstName("Anna")
                        .lastName("Petrov")
                        .build()
        );

        Vacancy savedVacancy = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Java Developer")
                        .salary(1000.0f)
                        .technologyStack(List.of("Java", "Spring"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        User user = Person.builder()
                .username("test")
                .password("test")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Set.of(Permission.APPLY_VACANCY))
                .firstName("John")
                .lastName("Doe")
                .build();

        User savedUser = userService.save(user);

        String jwtToken = jwtService.generateToken(savedUser);

        String requestBody = """
                {
                }
                """;

        mockMvc.perform(post("/api/v1/vacancy/{id}/apply", savedVacancy.getId())
                        .contentType("application/json")
                        .header("Authorization", "Bearer %s".formatted(jwtToken))
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Application created successfully"))
                .andExpect(jsonPath("$.candidate_application_id").isNumber())
                .andExpect(jsonPath("$.candidate_id").value(savedUser.getId()))
                .andExpect(jsonPath("$.vacancy_id").value(savedVacancy.getId()));


        // When and then
        mockMvc.perform(post("/api/v1/vacancy/{id}/apply", savedVacancy.getId())
                        .contentType("application/json")
                        .header("Authorization", "Bearer %s".formatted(jwtToken))
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Application has been already created"))
                .andExpect(jsonPath("$.candidate_application_id").doesNotExist())
                .andExpect(jsonPath("$.candidate_id").doesNotExist())
                .andExpect(jsonPath("$.vacancy_id").doesNotExist());
    }

    @Test
    @Order(3)
    public void testApplyForVacancy_whenVacancyDoesNotExist() throws Exception {
        // Given
        User user = Person.builder()
                .username("test")
                .password("test")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Set.of(Permission.APPLY_VACANCY))
                .firstName("John")
                .lastName("Doe")
                .build();

        User savedUser = userService.save(user);

        String jwtToken = jwtService.generateToken(savedUser);

        String requestBody = """
                {
                }
                """;

        // When and then
        mockMvc.perform(post("/api/v1/vacancy/{id}/apply", 1)
                        .contentType("application/json")
                        .header("Authorization", "Bearer %s".formatted(jwtToken))
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }
}
