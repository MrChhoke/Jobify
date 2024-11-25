package org.prof.it.soft.integration.security;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.prof.it.soft.dto.request.PersonRequestDto;
import org.prof.it.soft.dto.response.ProfileResponseDto;
import org.prof.it.soft.dto.security.request.RegistrationRecruiterRequestDto;
import org.prof.it.soft.dto.response.RecruiterResponseDto;
import org.prof.it.soft.entity.Recruiter;
import org.prof.it.soft.entity.Vacancy;
import org.prof.it.soft.entity.security.Permission;
import org.prof.it.soft.entity.security.User;
import org.prof.it.soft.integration.annotation.IT;
import org.prof.it.soft.integration.container.ControllerPostgresqlContainer;
import org.prof.it.soft.repo.RecruiterRepository;
import org.prof.it.soft.repo.UserRepository;
import org.prof.it.soft.repo.VacancyRepository;
import org.prof.it.soft.service.JwtService;
import org.prof.it.soft.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IT
@AutoConfigureMockMvc
class PermissionRecruiterControllerTest {

    @ClassRule
    public static ControllerPostgresqlContainer controllerPostgresqlContainer = ControllerPostgresqlContainer.getInstance();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;



    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private VacancyRepository vacancyRepository;

    void setUp() {
        recruiterRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldReturnForbiddenStatus_whenUserWasNotAuthenticated_AndWantCreateNewRecruiter() throws Exception {
        String request = """
                {
                      "first_name": "John",
                      "last_name": "Doe",
                      "company_name": "Google"
                }
                """;


        mockMvc.perform(post("/api/v1/recruiter/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnForbiddenStatus_whenUserWasNotAuthenticated_AndWantEditRecruiter() throws Exception {
        ProfileResponseDto recruiterResponseDto = userService.registerRecruiter(RegistrationRecruiterRequestDto.builder()
                .username("jonh_doe")
                .password("password")
                .companyName("Google")
                .person(PersonRequestDto.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .build())
                .build());


        String request = """
                {
                      "first_name": "John",
                      "last_name": "Doe",
                      "company_name": "Yandex"
                }
                """;

        mockMvc.perform(put("/api/v1/recruiter/%d".formatted(recruiterResponseDto.getUserId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnForbiddenStatus_whenUserWasNotAuthenticated_AndWantDeleteRecruiter() throws Exception {
        ProfileResponseDto recruiterResponseDto = userService.registerRecruiter(RegistrationRecruiterRequestDto.builder()
                .username("jonh_doe")
                .password("password")
                .companyName("Google")
                .person(PersonRequestDto.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .build())
                .build());

        Long recruiterId = recruiterResponseDto.getUserId();

        mockMvc.perform(delete("/api/v1/recruiter/%d".formatted(recruiterId)))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnOk_whenUserWasAuthenticated_AndHasAccessCreateNewRecruiter() throws Exception {
        // Given
        User user = User.builder()
                .username("test")
                .password("test")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Set.of(Permission.CREATE_RECRUITER))
                .build();

        User savedUser = userService.save(user);

        String jwtToken = jwtService.generateToken(savedUser);

        String request = """
                {
                      "username": "john",
                      "password": "password",
                      "first_name": "John",
                      "last_name": "Doe",
                      "company_name": "Google"
                }
                """;

        // When
        mockMvc.perform(post("/api/v1/recruiter/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header("Authorization", "Bearer %s".formatted(jwtToken)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnForbidden_whenUserWasAuthenticated_butHasNotAccessCreateNewRecruiter() throws Exception {
        // Given
        User user = User.builder()
                .username("test")
                .password("test")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Set.of(Permission.VIEW_RECRUITER))
                .build();

        User savedUser = userService.save(user);

        String jwtToken = jwtService.generateToken(savedUser);

        String request = """
                {
                      "username: "john_doe",
                      "password": "password",
                      "first_name": "John",
                      "last_name": "Doe",
                      "company_name": "Google"
                }
                """;

        // When
        mockMvc.perform(post("/api/v1/recruiter/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header("Authorization", "Bearer %s".formatted(jwtToken)))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnForbidden_whenUserWasAuthenticated_butHasNotAccessCreateNewRecruiterAndHasAccessEditRecruiter() throws Exception {
        // Given
        User user = User.builder()
                .username("test")
                .password("test")
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Set.of(Permission.EDIT_RECRUITER))
                .build();

        User savedUser = userService.save(user);

        String jwtToken = jwtService.generateToken(savedUser);

        String request = """
                {
                      "username: "john_doe",
                      "password": "password",
                      "first_name": "John",
                      "last_name": "Doe",
                      "company_name": "Google"
                }
                """;

        // When
        mockMvc.perform(post("/api/v1/recruiter/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .header("Authorization", "Bearer %s".formatted(jwtToken)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testApplyForVacancy_whenUserDoenstAuth() throws Exception {
        // Given
        Recruiter savedRecruiter = recruiterRepository.saveAndFlush(
                Recruiter.builder()
                        .username("john")
                        .password("password")
                        .username("recruiter")
                        .password("password")
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


        String requestBody = """
                {
                }
                """;

        // When and then
        mockMvc.perform(post("/api/v1/vacancy/{id}/apply", savedVacancy.getId())
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isForbidden());
    }
}
