package org.prof.it.soft.integration.controller;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.prof.it.soft.entity.Person;
import org.prof.it.soft.entity.Recruiter;
import org.prof.it.soft.entity.Vacancy;
import org.prof.it.soft.entity.security.Permission;
import org.prof.it.soft.integration.annotation.IT;
import org.prof.it.soft.integration.container.ControllerPostgresqlContainer;
import org.prof.it.soft.repo.PersonRepository;
import org.prof.it.soft.repo.RecruiterRepository;
import org.prof.it.soft.repo.VacancyRepository;
import org.prof.it.soft.service.CandidateApplicationService;
import org.prof.it.soft.service.JwtService;
import org.prof.it.soft.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
@AutoConfigureMockMvc
public class RecruiterControllerWithAuthTest {

    @ClassRule
    public static ControllerPostgresqlContainer controllerPostgresqlContainer = ControllerPostgresqlContainer.getInstance();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    RecruiterRepository recruiterRepository;

    @Autowired
    VacancyRepository vacancyRepository;

    @Autowired
    JwtService jwtService;

    @Autowired
    CandidateApplicationService candidateApplicationService;

    @Autowired
    UserService userService;

    @Test
    public void testGetAllApplications_shouldReturnOk_whenRecruiterHasApplicationsFromAnotherUserAndPermissions() throws Exception {
        // Given
        Recruiter savedRecruiter = recruiterRepository.saveAndFlush(
                Recruiter.builder()
                        .username("recruiter")
                        .password("recruiter")
                        .companyName("Google")
                        .firstName("Anna")
                        .lastName("Petrov")
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .enabled(true)
                        .permissions(Permission.RECRUITER_PERMISSIONS)
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

        Person savedPerson = personRepository.saveAndFlush(Person.builder()
                .username("test")
                .password("test")
                .firstName("John")
                .lastName("Doe")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Permission.USER_PERMISSIONS)
                .build());

        candidateApplicationService.createCandidateApplication(savedVacancy.getId(), savedPerson);

        String jwtToken = jwtService.generateToken(savedRecruiter);

        // When
        mockMvc.perform(get("/api/v1/recruiter/applications")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].vacancy.vacancy_id").value(savedVacancy.getId()))
                .andExpect(jsonPath("$.content[0].vacancy.position").value("Java Developer"))
                .andExpect(jsonPath("$.content[0].vacancy.salary").value("1000.0"))
                .andExpect(jsonPath("$.content[0].candidate.first_name").value("John"))
                .andExpect(jsonPath("$.content[0].candidate.last_name").value("Doe"));

    }

    @Test
    public void testGetAllApplications_shouldReturnOk_whenTwoRecruitersHaveApplicationsFromOneUsers() throws Exception {
        // Given
        Recruiter savedRecruiter = recruiterRepository.saveAndFlush(
                Recruiter.builder()
                        .username("recruiter")
                        .password("recruiter")
                        .companyName("Google")
                        .firstName("Anna")
                        .lastName("Petrov")
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .enabled(true)
                        .permissions(Permission.RECRUITER_PERMISSIONS)
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

        Recruiter savedRecruiter2 = recruiterRepository.saveAndFlush(
                Recruiter.builder()
                        .username("recruiter2")
                        .password("recruiter2")
                        .companyName("Google")
                        .firstName("Anna")
                        .lastName("Petrov")
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .enabled(true)
                        .permissions(Permission.RECRUITER_PERMISSIONS)
                        .build()
        );

        Vacancy savedVacancy2 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Java Developer")
                        .salary(1000.0f)
                        .technologyStack(List.of("Java", "Spring"))
                        .recruiter(savedRecruiter2)
                        .build()
        );

        Person savedPerson = personRepository.saveAndFlush(Person.builder()
                .username("test")
                .password("test")
                .firstName("John")
                .lastName("Doe")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Permission.USER_PERMISSIONS)
                .build());

        candidateApplicationService.createCandidateApplication(savedVacancy.getId(), savedPerson);
        candidateApplicationService.createCandidateApplication(savedVacancy2.getId(), savedPerson);

        String jwtTokenForFirstRecruiter = jwtService.generateToken(savedRecruiter);
        String jwtTokenForSecondRecruiter = jwtService.generateToken(savedRecruiter2);

        // When
        mockMvc.perform(get("/api/v1/recruiter/applications")
                        .header("Authorization", "Bearer " + jwtTokenForFirstRecruiter))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].vacancy.vacancy_id").value(savedVacancy.getId()))
                .andExpect(jsonPath("$.content[0].vacancy.position").value("Java Developer"))
                .andExpect(jsonPath("$.content[0].vacancy.salary").value("1000.0"))
                .andExpect(jsonPath("$.content[0].candidate.first_name").value("John"))
                .andExpect(jsonPath("$.content[0].candidate.last_name").value("Doe"));

        mockMvc.perform(get("/api/v1/recruiter/applications")
                        .header("Authorization", "Bearer " + jwtTokenForSecondRecruiter))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].vacancy.vacancy_id").value(savedVacancy2.getId()))
                .andExpect(jsonPath("$.content[0].vacancy.position").value("Java Developer"))
                .andExpect(jsonPath("$.content[0].vacancy.salary").value("1000.0"))
                .andExpect(jsonPath("$.content[0].candidate.first_name").value("John"))
                .andExpect(jsonPath("$.content[0].candidate.last_name").value("Doe"));
    }

    @Test
    public void testGetAllApplications_shouldReturnOk_whenRecruiterHasNoApplications() throws Exception {
        // Given
        Recruiter savedRecruiter = recruiterRepository.saveAndFlush(
                Recruiter.builder()
                        .username("recruiter")
                        .password("recruiter")
                        .companyName("Google")
                        .firstName("Anna")
                        .lastName("Petrov")
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .enabled(true)
                        .permissions(Permission.RECRUITER_PERMISSIONS)
                        .build()
        );

        String jwtToken = jwtService.generateToken(savedRecruiter);

        // When
        mockMvc.perform(get("/api/v1/recruiter/applications")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    public void testGetAllApplications_shouldReturn_whenTwoRecruitersHaveNoApplications() throws Exception {
        // Given
        Recruiter savedRecruiter = recruiterRepository.saveAndFlush(
                Recruiter.builder()
                        .username("recruiter")
                        .password("recruiter")
                        .companyName("Google")
                        .firstName("Anna")
                        .lastName("Petrov")
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .enabled(true)
                        .permissions(Permission.RECRUITER_PERMISSIONS)
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

        Vacancy savedVacancy2 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Java Developer")
                        .salary(1000.0f)
                        .technologyStack(List.of("Java", "Spring"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        Recruiter savedRecruiter2 = recruiterRepository.saveAndFlush(
                Recruiter.builder()
                        .username("recruiter2")
                        .password("recruiter2")
                        .companyName("Google")
                        .firstName("Anna")
                        .lastName("Petrov")
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .enabled(true)
                        .permissions(Permission.RECRUITER_PERMISSIONS)
                        .build()
        );

        Vacancy savedVacancy3 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Java Developer")
                        .salary(1000.0f)
                        .technologyStack(List.of("Java", "Spring"))
                        .recruiter(savedRecruiter2)
                        .build()
        );

        String jwtTokenForFirstRecruiter = jwtService.generateToken(savedRecruiter);
        String jwtTokenForSecondRecruiter = jwtService.generateToken(savedRecruiter2);

        // When
        mockMvc.perform(get("/api/v1/recruiter/applications")
                        .header("Authorization", "Bearer " + jwtTokenForFirstRecruiter))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(0)));

        mockMvc.perform(get("/api/v1/recruiter/applications")
                        .header("Authorization", "Bearer " + jwtTokenForSecondRecruiter))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    public void testGetAllApplications_shouldReturn_whenOneRecruiterHasApplicationAndAnotherRecruiterHasVacancyButDontHaveApplication() throws Exception {
        // Given
        Recruiter savedRecruiter = recruiterRepository.saveAndFlush(
                Recruiter.builder()
                        .username("recruiter")
                        .password("recruiter")
                        .companyName("Google")
                        .firstName("Anna")
                        .lastName("Petrov")
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .enabled(true)
                        .permissions(Permission.RECRUITER_PERMISSIONS)
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

        Vacancy savedVacancy2 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Java Developer")
                        .salary(1000.0f)
                        .technologyStack(List.of("Java", "Spring"))
                        .recruiter(savedRecruiter)
                        .build()
        );

        Recruiter savedRecruiter2 = recruiterRepository.saveAndFlush(
                Recruiter.builder()
                        .username("recruiter2")
                        .password("recruiter2")
                        .companyName("Google")
                        .firstName("Anna")
                        .lastName("Petrov")
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .enabled(true)
                        .permissions(Permission.RECRUITER_PERMISSIONS)
                        .build()
        );

        Vacancy savedVacancy3 = vacancyRepository.saveAndFlush(
                Vacancy.builder()
                        .position("Java Developer")
                        .salary(1000.0f)
                        .technologyStack(List.of("Java", "Spring"))
                        .recruiter(savedRecruiter2)
                        .build()
        );

        Person savedPerson = personRepository.saveAndFlush(Person.builder()
                .username("test")
                .password("test")
                .firstName("John")
                .lastName("Doe")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Permission.USER_PERMISSIONS)
                .build());

        candidateApplicationService.createCandidateApplication(savedVacancy.getId(), savedPerson);
        candidateApplicationService.createCandidateApplication(savedVacancy2.getId(), savedPerson);

        String jwtTokenForFirstRecruiter = jwtService.generateToken(savedRecruiter);
        String jwtTokenForSecondRecruiter = jwtService.generateToken(savedRecruiter2);

        // When
        mockMvc.perform(get("/api/v1/recruiter/applications")
                        .header("Authorization", "Bearer " + jwtTokenForFirstRecruiter))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].vacancy.vacancy_id").value(savedVacancy.getId()))
                .andExpect(jsonPath("$.content[0].vacancy.position").value("Java Developer"))
                .andExpect(jsonPath("$.content[0].vacancy.salary").value("1000.0"))
                .andExpect(jsonPath("$.content[0].candidate.first_name").value("John"))
                .andExpect(jsonPath("$.content[0].candidate.last_name").value("Doe"))
                .andExpect(jsonPath("$.content[1].vacancy.vacancy_id").value(savedVacancy2.getId()))
                .andExpect(jsonPath("$.content[1].vacancy.position").value("Java Developer"))
                .andExpect(jsonPath("$.content[1].vacancy.salary").value("1000.0"))
                .andExpect(jsonPath("$.content[1].candidate.first_name").value("John"))
                .andExpect(jsonPath("$.content[1].candidate.last_name").value("Doe"));

        mockMvc.perform(get("/api/v1/recruiter/applications")
                        .header("Authorization", "Bearer " + jwtTokenForSecondRecruiter))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    public void testGetAllApplications_shouldReturn_whenRecruiterHasApplicationsFromTwoAnotherUsers() throws Exception {
        // Given
        Recruiter savedRecruiter = recruiterRepository.saveAndFlush(
                Recruiter.builder()
                        .username("recruiter")
                        .password("recruiter")
                        .companyName("Google")
                        .firstName("Anna")
                        .lastName("Petrov")
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .enabled(true)
                        .permissions(Permission.RECRUITER_PERMISSIONS)
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

        Person savedPerson = personRepository.saveAndFlush(Person.builder()
                .username("test")
                .password("test")
                .firstName("John")
                .lastName("Doe")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Permission.USER_PERMISSIONS)
                .build());

        Person savedPerson2 = personRepository.saveAndFlush(Person.builder()
                .username("test2")
                .password("test2")
                .firstName("Anna")
                .lastName("Pannda")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Permission.USER_PERMISSIONS)
                .build());

        candidateApplicationService.createCandidateApplication(savedVacancy.getId(), savedPerson);
        candidateApplicationService.createCandidateApplication(savedVacancy.getId(), savedPerson2);

        String jwtToken = jwtService.generateToken(savedRecruiter);

        // When
        mockMvc.perform(get("/api/v1/recruiter/applications")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].vacancy.vacancy_id").value(savedVacancy.getId()))
                .andExpect(jsonPath("$.content[0].vacancy.position").value("Java Developer"))
                .andExpect(jsonPath("$.content[0].vacancy.salary").value("1000.0"))
                .andExpect(jsonPath("$.content[0].candidate.first_name").value("John"))
                .andExpect(jsonPath("$.content[0].candidate.last_name").value("Doe"))
                .andExpect(jsonPath("$.content[1].vacancy.vacancy_id").value(savedVacancy.getId()))
                .andExpect(jsonPath("$.content[1].vacancy.position").value("Java Developer"))
                .andExpect(jsonPath("$.content[1].vacancy.salary").value("1000.0"))
                .andExpect(jsonPath("$.content[1].candidate.first_name").value("Anna"))
                .andExpect(jsonPath("$.content[1].candidate.last_name").value("Pannda"));
    }
}