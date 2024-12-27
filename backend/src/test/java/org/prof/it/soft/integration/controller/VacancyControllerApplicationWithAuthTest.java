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
import org.prof.it.soft.service.CandidateApplicationService;
import org.prof.it.soft.service.JwtService;
import org.prof.it.soft.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
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

    @Autowired
    private CandidateApplicationService candidateApplicationService;

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
                .andExpect(status().isCreated())
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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Application created successfully"))
                .andExpect(jsonPath("$.candidate_application_id").isNumber())
                .andExpect(jsonPath("$.candidate_id").value(savedUser.getId()))
                .andExpect(jsonPath("$.vacancy_id").value(savedVacancy.getId()));


        // When and then
        mockMvc.perform(post("/api/v1/vacancy/{id}/apply", savedVacancy.getId())
                        .contentType("application/json")
                        .header("Authorization", "Bearer %s".formatted(jwtToken))
                        .content(requestBody))
                .andExpect(status().isCreated())
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

    @Test
    public void getVacancyById_shouldBeOk_whenUserAppliedVacancy() throws Exception {
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

        candidateApplicationService.createCandidateApplication(savedVacancy.getId(), savedUser);

        String jwtToken = jwtService.generateToken(savedUser);

        // When and then
        mockMvc.perform(get("/api/v1/vacancy/{id}", savedVacancy.getId())
                        .header("Authorization", "Bearer %s".formatted(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vacancy_id").value(savedVacancy.getId()))
                .andExpect(jsonPath("$.position").value(savedVacancy.getPosition()))
                .andExpect(jsonPath("$.salary").value(savedVacancy.getSalary()))
                .andExpect(jsonPath("$.technology_stack").isArray())
                .andExpect(jsonPath("$.technology_stack", hasSize(2)))
                .andExpect(jsonPath("$.technology_stack[0]").value("Java"))
                .andExpect(jsonPath("$.technology_stack[1]").value("Spring"))
                .andExpect(jsonPath("$.recruiter.recruiter_user_id").value(savedRecruiter.getId()))
                .andExpect(jsonPath("$.is_applied_by_current_user").value(true));
    }

    @Test
    public void getVacancyById_shouldBeOk_whenUserDidNotApplyVacancy() throws Exception {
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

        // When and then
        mockMvc.perform(get("/api/v1/vacancy/{id}", savedVacancy.getId())
                        .header("Authorization", "Bearer %s".formatted(jwtToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vacancy_id").value(savedVacancy.getId()))
                .andExpect(jsonPath("$.position").value(savedVacancy.getPosition()))
                .andExpect(jsonPath("$.salary").value(savedVacancy.getSalary()))
                .andExpect(jsonPath("$.technology_stack").isArray())
                .andExpect(jsonPath("$.technology_stack", hasSize(2)))
                .andExpect(jsonPath("$.technology_stack[0]").value("Java"))
                .andExpect(jsonPath("$.technology_stack[1]").value("Spring"))
                .andExpect(jsonPath("$.recruiter.recruiter_user_id").value(savedRecruiter.getId()))
                .andExpect(jsonPath("$.is_applied_by_current_user").value(false));
    }

    @Test
    public void getVacancyById_shouldBeOk_whenOneUserAppliedVacancyAndAnotherDidNotApplied() throws Exception {
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

        User user1 = Person.builder()
                .username("test1")
                .password("test1")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Set.of(Permission.APPLY_VACANCY))
                .firstName("John")
                .lastName("Doe")
                .build();

        User user2 = Person.builder()
                .username("test2")
                .password("test2")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Set.of(Permission.APPLY_VACANCY))
                .firstName("Jane")
                .lastName("Doe")
                .build();

        User savedUser1 = userService.save(user1);
        User savedUser2 = userService.save(user2);

        candidateApplicationService.createCandidateApplication(savedVacancy.getId(), savedUser1);

        String jwtToken1 = jwtService.generateToken(savedUser1);
        String jwtToken2 = jwtService.generateToken(savedUser2);

        // When and then
        mockMvc.perform(get("/api/v1/vacancy/{id}", savedVacancy.getId())
                        .header("Authorization", "Bearer %s".formatted(jwtToken1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vacancy_id").value(savedVacancy.getId()))
                .andExpect(jsonPath("$.position").value(savedVacancy.getPosition()))
                .andExpect(jsonPath("$.salary").value(savedVacancy.getSalary()))
                .andExpect(jsonPath("$.technology_stack").isArray())
                .andExpect(jsonPath("$.technology_stack", hasSize(2)))
                .andExpect(jsonPath("$.technology_stack[0]").value("Java"))
                .andExpect(jsonPath("$.technology_stack[1]").value("Spring"))
                .andExpect(jsonPath("$.recruiter.recruiter_user_id").value(savedRecruiter.getId()))
                .andExpect(jsonPath("$.is_applied_by_current_user").value(true));

        mockMvc.perform(get("/api/v1/vacancy/{id}", savedVacancy.getId())
                        .header("Authorization", "Bearer %s".formatted(jwtToken2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vacancy_id").value(savedVacancy.getId()))
                .andExpect(jsonPath("$.position").value(savedVacancy.getPosition()))
                .andExpect(jsonPath("$.salary").value(savedVacancy.getSalary()))
                .andExpect(jsonPath("$.technology_stack").isArray())
                .andExpect(jsonPath("$.technology_stack", hasSize(2)))
                .andExpect(jsonPath("$.technology_stack[0]").value("Java"))
                .andExpect(jsonPath("$.technology_stack[1]").value("Spring"))
                .andExpect(jsonPath("$.recruiter.recruiter_user_id").value(savedRecruiter.getId()))
                .andExpect(jsonPath("$.is_applied_by_current_user").value(false));
    }

    @Test
    void getFilteredVacancies_shouldReturnOk_whenOneUserAppliedTwoVacanciesAndAnotherAppliedTwoAnotherVacancies() throws Exception {
        // Given
        Recruiter savedRecruiter = recruiterRepository.saveAndFlush(
                Recruiter.builder()
                        .username("anna")
                        .password("password")
                        .companyName("Google")
                        .firstName("Anna")
                        .lastName("Petrov")
                        .build()
        );

        Vacancy savedVacancy1 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Java Developer")
                        .salary(1000.0f)
                        .technologyStack(List.of("Java", "Spring"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        Vacancy savedVacancy2 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Python Developer")
                        .salary(2000.0f)
                        .technologyStack(List.of("Python", "Django"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        Vacancy savedVacancy3 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Java Developer")
                        .salary(3000.0f)
                        .technologyStack(List.of("Java", "Spring", "Hibernate"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        Vacancy savedVacancy4 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Sql Developer")
                        .salary(4000.0f)
                        .technologyStack(List.of("SQL", "PostgreSQL"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        User user1 = Person.builder()
                .username("test1")
                .password("test1")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Set.of(Permission.APPLY_VACANCY))
                .firstName("John")
                .lastName("Doe")
                .build();

        User user2 = Person.builder()
                .username("test2")
                .password("test2")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Set.of(Permission.APPLY_VACANCY))
                .firstName("Sasha")
                .lastName("Petrov")
                .build();

        User savedUser1 = userService.save(user1);
        User savedUser2 = userService.save(user2);

        candidateApplicationService.createCandidateApplication(savedVacancy1.getId(), savedUser1);
        candidateApplicationService.createCandidateApplication(savedVacancy2.getId(), savedUser1);
        candidateApplicationService.createCandidateApplication(savedVacancy3.getId(), savedUser2);
        candidateApplicationService.createCandidateApplication(savedVacancy4.getId(), savedUser2);

        String jwtToken1 = jwtService.generateToken(savedUser1);
        String jwtToken2 = jwtService.generateToken(savedUser2);

        String request = """
                {
                    "page": 0,
                    "size": 10
                }
                """;

        mockMvc.perform(post("/api/v1/vacancy/_list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(jwtToken1))
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(10))
                .andExpect(jsonPath("$.numberOfElements").value(4))
                .andExpect(jsonPath("$.content", hasSize(4)))
                .andExpect(jsonPath("$.content[0].vacancy_id").value(savedVacancy1.getId()))
                .andExpect(jsonPath("$.content[0].position").value(savedVacancy1.getPosition()))
                .andExpect(jsonPath("$.content[0].salary").value(savedVacancy1.getSalary()))
                .andExpect(jsonPath("$.content[0].is_applied_by_current_user").value(true))
                .andExpect(jsonPath("$.content[1].vacancy_id").value(savedVacancy2.getId()))
                .andExpect(jsonPath("$.content[1].position").value(savedVacancy2.getPosition()))
                .andExpect(jsonPath("$.content[1].salary").value(savedVacancy2.getSalary()))
                .andExpect(jsonPath("$.content[1].is_applied_by_current_user").value(true))
                .andExpect(jsonPath("$.content[2].vacancy_id").value(savedVacancy3.getId()))
                .andExpect(jsonPath("$.content[2].position").value(savedVacancy3.getPosition()))
                .andExpect(jsonPath("$.content[2].salary").value(savedVacancy3.getSalary()))
                .andExpect(jsonPath("$.content[2].is_applied_by_current_user").value(false))
                .andExpect(jsonPath("$.content[3].vacancy_id").value(savedVacancy4.getId()))
                .andExpect(jsonPath("$.content[3].position").value(savedVacancy4.getPosition()))
                .andExpect(jsonPath("$.content[3].is_applied_by_current_user").value(false));

        mockMvc.perform(post("/api/v1/vacancy/_list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer %s".formatted(jwtToken2))
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(10))
                .andExpect(jsonPath("$.numberOfElements").value(4))
                .andExpect(jsonPath("$.content", hasSize(4)))
                .andExpect(jsonPath("$.content[0].vacancy_id").value(savedVacancy1.getId()))
                .andExpect(jsonPath("$.content[0].position").value(savedVacancy1.getPosition()))
                .andExpect(jsonPath("$.content[0].salary").value(savedVacancy1.getSalary()))
                .andExpect(jsonPath("$.content[0].is_applied_by_current_user").value(false))
                .andExpect(jsonPath("$.content[1].vacancy_id").value(savedVacancy2.getId()))
                .andExpect(jsonPath("$.content[1].position").value(savedVacancy2.getPosition()))
                .andExpect(jsonPath("$.content[1].salary").value(savedVacancy2.getSalary()))
                .andExpect(jsonPath("$.content[1].is_applied_by_current_user").value(false))
                .andExpect(jsonPath("$.content[2].vacancy_id").value(savedVacancy3.getId()))
                .andExpect(jsonPath("$.content[2].position").value(savedVacancy3.getPosition()))
                .andExpect(jsonPath("$.content[2].salary").value(savedVacancy3.getSalary()))
                .andExpect(jsonPath("$.content[2].is_applied_by_current_user").value(true))
                .andExpect(jsonPath("$.content[3].vacancy_id").value(savedVacancy4.getId()))
                .andExpect(jsonPath("$.content[3].position").value(savedVacancy4.getPosition()))
                .andExpect(jsonPath("$.content[3].is_applied_by_current_user").value(true));
    }

    @Test
    void getFilteredVacancies_shouldReturnOk_whenOneUserAppliedTwoVacanciesAndAnotherAppliedTwoAnotherVacancies_butUserDoNotAuth() throws Exception {
        // Given
        Recruiter savedRecruiter = recruiterRepository.saveAndFlush(
                Recruiter.builder()
                        .username("anna")
                        .password("password")
                        .companyName("Google")
                        .firstName("Anna")
                        .lastName("Petrov")
                        .build()
        );

        Vacancy savedVacancy1 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Java Developer")
                        .salary(1000.0f)
                        .technologyStack(List.of("Java", "Spring"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        Vacancy savedVacancy2 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Python Developer")
                        .salary(2000.0f)
                        .technologyStack(List.of("Python", "Django"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        Vacancy savedVacancy3 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Java Developer")
                        .salary(3000.0f)
                        .technologyStack(List.of("Java", "Spring", "Hibernate"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        Vacancy savedVacancy4 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Sql Developer")
                        .salary(4000.0f)
                        .technologyStack(List.of("SQL", "PostgreSQL"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        User user1 = Person.builder()
                .username("test1")
                .password("test1")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Set.of(Permission.APPLY_VACANCY))
                .firstName("John")
                .lastName("Doe")
                .build();

        User user2 = Person.builder()
                .username("test2")
                .password("test2")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Set.of(Permission.APPLY_VACANCY))
                .firstName("Sasha")
                .lastName("Petrov")
                .build();

        User savedUser1 = userService.save(user1);
        User savedUser2 = userService.save(user2);

        candidateApplicationService.createCandidateApplication(savedVacancy1.getId(), savedUser1);
        candidateApplicationService.createCandidateApplication(savedVacancy2.getId(), savedUser1);
        candidateApplicationService.createCandidateApplication(savedVacancy3.getId(), savedUser2);
        candidateApplicationService.createCandidateApplication(savedVacancy4.getId(), savedUser2);

        String request = """
                {
                    "page": 0,
                    "size": 10
                }
                """;

        mockMvc.perform(post("/api/v1/vacancy/_list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(10))
                .andExpect(jsonPath("$.numberOfElements").value(4))
                .andExpect(jsonPath("$.content", hasSize(4)))
                .andExpect(jsonPath("$.content[0].vacancy_id").value(savedVacancy1.getId()))
                .andExpect(jsonPath("$.content[0].position").value(savedVacancy1.getPosition()))
                .andExpect(jsonPath("$.content[0].salary").value(savedVacancy1.getSalary()))
                .andExpect(jsonPath("$.content[0].is_applied_by_current_user").doesNotExist())
                .andExpect(jsonPath("$.content[1].vacancy_id").value(savedVacancy2.getId()))
                .andExpect(jsonPath("$.content[1].position").value(savedVacancy2.getPosition()))
                .andExpect(jsonPath("$.content[1].salary").value(savedVacancy2.getSalary()))
                .andExpect(jsonPath("$.content[1].is_applied_by_current_user").doesNotExist())
                .andExpect(jsonPath("$.content[2].vacancy_id").value(savedVacancy3.getId()))
                .andExpect(jsonPath("$.content[2].position").value(savedVacancy3.getPosition()))
                .andExpect(jsonPath("$.content[2].salary").value(savedVacancy3.getSalary()))
                .andExpect(jsonPath("$.content[2].is_applied_by_current_user").doesNotExist())
                .andExpect(jsonPath("$.content[3].vacancy_id").value(savedVacancy4.getId()))
                .andExpect(jsonPath("$.content[3].position").value(savedVacancy4.getPosition()))
                .andExpect(jsonPath("$.content[3].is_applied_by_current_user").doesNotExist());
    }

    @Test
    public void getAppliedVacancies_shouldReturnOk_whenTwoUsersAppliedDifferentVacancies() throws Exception {
        // Given
        Recruiter savedRecruiter = recruiterRepository.saveAndFlush(
                Recruiter.builder()
                        .username("anna")
                        .password("password")
                        .companyName("Google")
                        .firstName("Anna")
                        .lastName("Petrov")
                        .build()
        );

        Vacancy savedVacancy1 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Java Developer")
                        .salary(1000.0f)
                        .technologyStack(List.of("Java", "Spring"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        Vacancy savedVacancy2 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Python Developer")
                        .salary(2000.0f)
                        .technologyStack(List.of("Python", "Django"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        Vacancy savedVacancy3 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Senior Java Developer")
                        .salary(3000.0f)
                        .technologyStack(List.of("Java", "Spring", "Hibernate"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        Vacancy savedVacancy4 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Sql Developer")
                        .salary(4000.0f)
                        .technologyStack(List.of("SQL", "PostgreSQL"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        User user1 = Person.builder()
                .username("test1")
                .password("test1")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Set.of(Permission.VIEW_OWN_APPLICATIONS))
                .firstName("John")
                .lastName("Doe")
                .build();

        User user2 = Person.builder()
                .username("test2")
                .password("test2")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Set.of(Permission.VIEW_OWN_APPLICATIONS))
                .firstName("Sasha")
                .lastName("Petrov")
                .build();

        User savedUser1 = userService.save(user1);
        User savedUser2 = userService.save(user2);

        candidateApplicationService.createCandidateApplication(savedVacancy1.getId(), savedUser1);
        candidateApplicationService.createCandidateApplication(savedVacancy2.getId(), savedUser1);
        candidateApplicationService.createCandidateApplication(savedVacancy3.getId(), savedUser2);
        candidateApplicationService.createCandidateApplication(savedVacancy4.getId(), savedUser2);

        String jwtToken1 = jwtService.generateToken(savedUser1);
        String jwtToken2 = jwtService.generateToken(savedUser2);

        // When and then
        mockMvc.perform(get("/api/v1/vacancy/applied")
                        .header("Authorization", "Bearer %s".formatted(jwtToken1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].vacancy.vacancy_id").value(savedVacancy1.getId()))
                .andExpect(jsonPath("$.content[0].vacancy.position").value("Java Developer"))
                .andExpect(jsonPath("$.content[0].vacancy.salary").value(1000.0))
                .andExpect(jsonPath("$.content[0].vacancy.technology_stack").isArray())
                .andExpect(jsonPath("$.content[0].vacancy.technology_stack", hasSize(2)))
                .andExpect(jsonPath("$.content[0].candidate.first_name").value("John"))
                .andExpect(jsonPath("$.content[0].candidate.last_name").value("Doe"))
                .andExpect(jsonPath("$.content[1].vacancy.vacancy_id").value(savedVacancy2.getId()))
                .andExpect(jsonPath("$.content[1].vacancy.position").value("Python Developer"))
                .andExpect(jsonPath("$.content[1].vacancy.salary").value(2000.0))
                .andExpect(jsonPath("$.content[1].vacancy.technology_stack").isArray())
                .andExpect(jsonPath("$.content[1].vacancy.technology_stack", hasSize(2)))
                .andExpect(jsonPath("$.content[1].candidate.first_name").value("John"))
                .andExpect(jsonPath("$.content[1].candidate.last_name").value("Doe"));

        mockMvc.perform(get("/api/v1/vacancy/applied")
                        .header("Authorization", "Bearer %s".formatted(jwtToken2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].vacancy.vacancy_id").value(savedVacancy3.getId()))
                .andExpect(jsonPath("$.content[0].vacancy.position").value("Senior Java Developer"))
                .andExpect(jsonPath("$.content[0].vacancy.salary").value(3000.0))
                .andExpect(jsonPath("$.content[0].vacancy.technology_stack").isArray())
                .andExpect(jsonPath("$.content[0].vacancy.technology_stack", hasSize(3)))
                .andExpect(jsonPath("$.content[0].candidate.first_name").value("Sasha"))
                .andExpect(jsonPath("$.content[0].candidate.last_name").value("Petrov"))
                .andExpect(jsonPath("$.content[1].vacancy.vacancy_id").value(savedVacancy4.getId()))
                .andExpect(jsonPath("$.content[1].vacancy.position").value("Sql Developer"))
                .andExpect(jsonPath("$.content[1].vacancy.salary").value(4000.0))
                .andExpect(jsonPath("$.content[1].vacancy.technology_stack").isArray())
                .andExpect(jsonPath("$.content[1].vacancy.technology_stack", hasSize(2)))
                .andExpect(jsonPath("$.content[1].candidate.first_name").value("Sasha"))
                .andExpect(jsonPath("$.content[1].candidate.last_name").value("Petrov"));
    }

    @Test
    public void getAppliedVacancies_shouldReturnOk_whenOnlyOneUserAppliedDifferentVacancies() throws Exception {
        // Given
        Recruiter savedRecruiter = recruiterRepository.saveAndFlush(
                Recruiter.builder()
                        .username("anna")
                        .password("password")
                        .companyName("Google")
                        .firstName("Anna")
                        .lastName("Petrov")
                        .build()
        );

        Vacancy savedVacancy1 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Java Developer")
                        .salary(1000.0f)
                        .technologyStack(List.of("Java", "Spring"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        Vacancy savedVacancy2 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Python Developer")
                        .salary(2000.0f)
                        .technologyStack(List.of("Python", "Django"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        Vacancy savedVacancy3 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Senior Java Developer")
                        .salary(3000.0f)
                        .technologyStack(List.of("Java", "Spring", "Hibernate"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        Vacancy savedVacancy4 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Sql Developer")
                        .salary(4000.0f)
                        .technologyStack(List.of("SQL", "PostgreSQL"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        User user1 = Person.builder()
                .username("test1")
                .password("test1")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Set.of(Permission.VIEW_OWN_APPLICATIONS))
                .firstName("John")
                .lastName("Doe")
                .build();

        User user2 = Person.builder()
                .username("test2")
                .password("test2")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Set.of(Permission.VIEW_OWN_APPLICATIONS))
                .firstName("Sasha")
                .lastName("Petrov")
                .build();

        User savedUser1 = userService.save(user1);
        User savedUser2 = userService.save(user2);

        candidateApplicationService.createCandidateApplication(savedVacancy1.getId(), savedUser1);
        candidateApplicationService.createCandidateApplication(savedVacancy2.getId(), savedUser1);
        candidateApplicationService.createCandidateApplication(savedVacancy4.getId(), savedUser1);

        String jwtToken1 = jwtService.generateToken(savedUser1);
        String jwtToken2 = jwtService.generateToken(savedUser2);

        // When and then
        mockMvc.perform(get("/api/v1/vacancy/applied")
                        .header("Authorization", "Bearer %s".formatted(jwtToken1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].vacancy.vacancy_id").value(savedVacancy1.getId()))
                .andExpect(jsonPath("$.content[0].vacancy.position").value("Java Developer"))
                .andExpect(jsonPath("$.content[0].vacancy.salary").value(1000.0))
                .andExpect(jsonPath("$.content[0].vacancy.technology_stack").isArray())
                .andExpect(jsonPath("$.content[0].vacancy.technology_stack", hasSize(2)))
                .andExpect(jsonPath("$.content[0].candidate.first_name").value("John"))
                .andExpect(jsonPath("$.content[0].candidate.last_name").value("Doe"))
                .andExpect(jsonPath("$.content[1].vacancy.vacancy_id").value(savedVacancy2.getId()))
                .andExpect(jsonPath("$.content[1].vacancy.position").value("Python Developer"))
                .andExpect(jsonPath("$.content[1].vacancy.salary").value(2000.0))
                .andExpect(jsonPath("$.content[1].vacancy.technology_stack").isArray())
                .andExpect(jsonPath("$.content[1].vacancy.technology_stack", hasSize(2)))
                .andExpect(jsonPath("$.content[1].candidate.first_name").value("John"))
                .andExpect(jsonPath("$.content[1].candidate.last_name").value("Doe"))
                .andExpect(jsonPath("$.content[2].vacancy.vacancy_id").value(savedVacancy4.getId()))
                .andExpect(jsonPath("$.content[2].vacancy.position").value("Sql Developer"))
                .andExpect(jsonPath("$.content[2].vacancy.salary").value(4000.0))
                .andExpect(jsonPath("$.content[2].vacancy.technology_stack").isArray())
                .andExpect(jsonPath("$.content[2].vacancy.technology_stack", hasSize(2)))
                .andExpect(jsonPath("$.content[2].candidate.first_name").value("John"))
                .andExpect(jsonPath("$.content[2].candidate.last_name").value("Doe"));

        mockMvc.perform(get("/api/v1/vacancy/applied")
                        .header("Authorization", "Bearer %s".formatted(jwtToken2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    public void getAppliedVacancies_shouldReturnForbidden_whenOnlyOneUserHasPermissionToViewOwnApplications() throws Exception {
        // Given
        Recruiter savedRecruiter = recruiterRepository.saveAndFlush(
                Recruiter.builder()
                        .username("anna")
                        .password("password")
                        .companyName("Google")
                        .firstName("Anna")
                        .lastName("Petrov")
                        .build()
        );

        Vacancy savedVacancy1 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Java Developer")
                        .salary(1000.0f)
                        .technologyStack(List.of("Java", "Spring"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        Vacancy savedVacancy2 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Python Developer")
                        .salary(2000.0f)
                        .technologyStack(List.of("Python", "Django"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        Vacancy savedVacancy3 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Senior Java Developer")
                        .salary(3000.0f)
                        .technologyStack(List.of("Java", "Spring", "Hibernate"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        Vacancy savedVacancy4 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Sql Developer")
                        .salary(4000.0f)
                        .technologyStack(List.of("SQL", "PostgreSQL"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        User user1 = Person.builder()
                .username("test1")
                .password("test1")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Set.of(Permission.APPLY_VACANCY, Permission.VIEW_OWN_APPLICATIONS))
                .firstName("John")
                .lastName("Doe")
                .build();

        User user2 = Person.builder()
                .username("test2")
                .password("test2")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Set.of(Permission.APPLY_VACANCY))
                .firstName("Sasha")
                .lastName("Petrov")
                .build();

        User savedUser1 = userService.save(user1);
        User savedUser2 = userService.save(user2);

        candidateApplicationService.createCandidateApplication(savedVacancy1.getId(), savedUser1);
        candidateApplicationService.createCandidateApplication(savedVacancy2.getId(), savedUser1);
        candidateApplicationService.createCandidateApplication(savedVacancy3.getId(), savedUser2);
        candidateApplicationService.createCandidateApplication(savedVacancy4.getId(), savedUser2);

        String jwtToken1 = jwtService.generateToken(savedUser1);
        String jwtToken2 = jwtService.generateToken(savedUser2);

        // When and then
        mockMvc.perform(get("/api/v1/vacancy/applied")
                        .header("Authorization", "Bearer %s".formatted(jwtToken1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].vacancy.vacancy_id").value(savedVacancy1.getId()))
                .andExpect(jsonPath("$.content[0].vacancy.position").value("Java Developer"))
                .andExpect(jsonPath("$.content[0].vacancy.salary").value(1000.0))
                .andExpect(jsonPath("$.content[0].vacancy.technology_stack").isArray())
                .andExpect(jsonPath("$.content[0].vacancy.technology_stack", hasSize(2)))
                .andExpect(jsonPath("$.content[0].candidate.first_name").value("John"))
                .andExpect(jsonPath("$.content[0].candidate.last_name").value("Doe"))
                .andExpect(jsonPath("$.content[1].vacancy.vacancy_id").value(savedVacancy2.getId()))
                .andExpect(jsonPath("$.content[1].vacancy.position").value("Python Developer"))
                .andExpect(jsonPath("$.content[1].vacancy.salary").value(2000.0))
                .andExpect(jsonPath("$.content[1].vacancy.technology_stack").isArray())
                .andExpect(jsonPath("$.content[1].vacancy.technology_stack", hasSize(2)))
                .andExpect(jsonPath("$.content[1].candidate.first_name").value("John"));

        mockMvc.perform(get("/api/v1/vacancy/applied")
                        .header("Authorization", "Bearer %s".formatted(jwtToken2)))
                .andExpect(status().isForbidden());
    }
}
