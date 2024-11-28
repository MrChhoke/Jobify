package org.prof.it.soft.config;

import lombok.RequiredArgsConstructor;
import org.prof.it.soft.entity.security.Permission;
import org.prof.it.soft.filter.JwtAuthenticationFilter;
import org.prof.it.soft.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOriginPatterns(List.of("*"));
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                .authorizeHttpRequests(request -> request
                        /** Allow access only in dev mode */
                        .requestMatchers("/api/v1/developers/**").authenticated()
                        /** Allow access to production */
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/vacancy/applied").hasAnyAuthority(Permission.VIEW_OWN_APPLICATIONS.toString())
                        .requestMatchers(HttpMethod.GET, "/api/v1/vacancy/{id:[0-9]+}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/vacancy/_list").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/vacancy").hasAnyAuthority(Permission.CREATE_VACANCY.toString())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/vacancy/{id:[0-9]+}").hasAnyAuthority(Permission.EDIT_VACANCY.toString())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/vacancy/{id:[0-9]+}").hasAnyAuthority(Permission.DELETE_VACANCY.toString())
                        //.requestMatchers(HttpMethod.GET, "/api/v1/recruiter/{id}").hasAnyAuthority(Permission.VIEW_RECRUITER.toString())
                        .requestMatchers(HttpMethod.POST, "/api/v1/recruiter/register").hasAnyAuthority(Permission.CREATE_RECRUITER.toString())
                        .requestMatchers(HttpMethod.GET, "/api/v1/recruiter/applications").hasAnyAuthority(Permission.VIEW_RECRUITER_OWN_APPLICATIONS.toString())
                        .requestMatchers(HttpMethod.GET, "/api/v1/recruiter/vacancies").hasAnyAuthority(Permission.VIEW_RECRUITER_OWN_VACANCIES.toString())
                        //.requestMatchers(HttpMethod.PUT, "/api/v1/recruiter/{id}").hasAnyAuthority(Permission.EDIT_RECRUITER.toString())
                        //.requestMatchers(HttpMethod.DELETE, "/api/v1/recruiter/{id}").hasAnyAuthority(Permission.DELETE_RECRUITER.toString())
                        .requestMatchers(HttpMethod.POST, "/api/v1/vacancy/{id:[0-9]+}/apply").hasAnyAuthority(Permission.APPLY_VACANCY.toString())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/profile/update").hasAnyAuthority(Permission.UPDATE_OWN_PROFILE.toString())
                        .requestMatchers(HttpMethod.GET, "/api/v1/profile").hasAnyAuthority(Permission.GET_OWN_PROFILE.toString())
                        .requestMatchers(HttpMethod.POST, "/api/v1/vacancy/_report").hasAnyAuthority(Permission.GENERATE_VACANCY_REPORT.toString())
                        .requestMatchers(HttpMethod.GET, "/api/v1/vacancy/{id:[0-9]+}/applications").hasAnyAuthority(Permission.VIEW_VACANCY_APPLICATIONS.toString())
                        .requestMatchers(HttpMethod.GET, "/api/v1/vacancy/person/{id}/applications").hasAnyAuthority(Permission.VIEW_PERSON_APPLICATIONS.toString())
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

}
