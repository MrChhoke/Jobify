package org.prof.it.soft.service.impl;

import org.junit.jupiter.api.Test;
import org.prof.it.soft.entity.security.Permission;
import org.prof.it.soft.entity.security.User;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceImplTest {

    private final JwtServiceImpl jwtService = JwtServiceImpl.builder()
            .jwtSigningKey("VERYSTRONGANDPOWERFULKEY11111APIKEY1111GLHLDDHJLGDLGHJKSW")
            .jwtExpirationInMs(2000)
            .build();

    @Test
    void extractUserName() {
        User user = User.builder()
                .id(1L)
                .username("username")
                .password("12345")
                .permissions(Set.of(Permission.CREATE_RECRUITER, Permission.CREATE_VACANCY))
                .build();

        String token = jwtService.generateToken(user);
        String username = jwtService.extractUserName(token);

        assertNotNull(username);
        assertThat(username).isEqualTo(user.getUsername());
    }

    @Test
    void generateToken() {
        String token = jwtService.generateToken(User.builder()
                .id(1L)
                .username("username")
                .password("12345")
                .permissions(Set.of(Permission.CREATE_RECRUITER, Permission.CREATE_VACANCY))
                .build());

        assertNotNull(token);
        assertThat(token).isNotBlank();
    }

    @Test
    void isTokenValid() throws InterruptedException {
        User user = User.builder()
                .id(1L)
                .username("username")
                .password("12345")
                .permissions(Set.of(Permission.CREATE_RECRUITER, Permission.CREATE_VACANCY))
                .build();

        String token = jwtService.generateToken(user);

        // Check if the token is valid
        assertTrue(jwtService.isTokenValid(token, user));

        Thread.sleep(2000);

        // Check if the token is invalid because it has expired
        assertFalse(jwtService.isTokenValid(token, user));
    }
}