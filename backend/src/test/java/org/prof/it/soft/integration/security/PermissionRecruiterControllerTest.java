package org.prof.it.soft.integration.security;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.prof.it.soft.dto.request.RequestPersonDto;
import org.prof.it.soft.dto.request.RequestRecruiterDto;
import org.prof.it.soft.dto.response.ResponseRecruiterDto;
import org.prof.it.soft.entity.Vacancy;
import org.prof.it.soft.entity.security.Permission;
import org.prof.it.soft.entity.security.User;
import org.prof.it.soft.integration.annotation.IT;
import org.prof.it.soft.integration.container.ControllerPostgresqlContainer;
import org.prof.it.soft.repo.RecruiterRepository;
import org.prof.it.soft.repo.UserRepository;
import org.prof.it.soft.service.JwtService;
import org.prof.it.soft.service.RecruiterService;
import org.prof.it.soft.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
    private RecruiterService recruiterService;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

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


        mockMvc.perform(post("/api/v1/recruiter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnForbiddenStatus_whenUserWasNotAuthenticated_AndWantEditRecruiter() throws Exception {
        ResponseRecruiterDto responseRecruiterDto = recruiterService.saveRecruiter(RequestRecruiterDto.builder()
                .companyName("Google")
                .person(RequestPersonDto.builder()
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

        mockMvc.perform(put("/api/v1/recruiter/%d".formatted(responseRecruiterDto.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnForbiddenStatus_whenUserWasNotAuthenticated_AndWantDeleteRecruiter() throws Exception {
        ResponseRecruiterDto responseRecruiterDto = recruiterService.saveRecruiter(RequestRecruiterDto.builder()
                .companyName("Google")
                .person(RequestPersonDto.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .build())
                .build());

        Long recruiterId = responseRecruiterDto.getId();

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
                      "first_name": "John",
                      "last_name": "Doe",
                      "company_name": "Google"
                }
                """;

        // When
        mockMvc.perform(post("/api/v1/recruiter")
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
                      "first_name": "John",
                      "last_name": "Doe",
                      "company_name": "Google"
                }
                """;

        // When
        mockMvc.perform(post("/api/v1/recruiter")
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
                      "first_name": "John",
                      "last_name": "Doe",
                      "company_name": "Google"
                }
                """;

        // When
        mockMvc.perform(post("/api/v1/recruiter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
                .header("Authorization", "Bearer %s".formatted(jwtToken)))
                .andExpect(status().isForbidden());
    }
}
