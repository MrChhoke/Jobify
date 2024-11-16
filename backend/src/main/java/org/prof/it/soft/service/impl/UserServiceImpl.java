package org.prof.it.soft.service.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.proxy.HibernateProxy;
import org.prof.it.soft.dto.security.request.LoginRequestDto;
import org.prof.it.soft.dto.security.request.RegistrationRequestDto;
import org.prof.it.soft.dto.security.response.JwtTokenResponseDto;
import org.prof.it.soft.entity.security.Permission;
import org.prof.it.soft.entity.security.User;
import org.prof.it.soft.repo.UserRepository;
import org.prof.it.soft.service.JwtService;
import org.prof.it.soft.service.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtTokenResponseDto login(LoginRequestDto loginRequestDto) {
        User user = loadUserByUsername(loginRequestDto.getUsername());

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return jwtService.generateTokenResponse(user);
    }

    @Override
    public JwtTokenResponseDto register(RegistrationRequestDto registrationRequestDto) {
        Optional<User> maybeUser = userRepository.findByUsername(registrationRequestDto.getUsername());

        if (maybeUser.isPresent()) {
            throw new BadCredentialsException("User with such username already exists");
        }

        User user = User.builder()
                .username(registrationRequestDto.getUsername())
                .password(registrationRequestDto.getPassword())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .permissions(Permission.USER_PERMISSIONS)
                .build();

        save(user);

        return jwtService.generateTokenResponse(user);
    }

    @Override
    public User save(User user) {
        if (user.getId() != null || user instanceof HibernateProxy) {
            throw new IllegalStateException("User must be not saved yet in the database");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.saveAndFlush(user);
    }

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            throw new UsernameNotFoundException("User ID cannot be null");
        }

        return userRepository.saveAndFlush(user);
    }

    @Override
    public void block(String username) {
        User user = loadUserByUsername(username);
        user.setAccountNonLocked(false);
        update(user);
    }

    @Override
    public void unblock(String username) {
        User user = loadUserByUsername(username);
        user.setAccountNonLocked(true);
        update(user);
    }

    @Override
    public void changePassword(String username, String password) {
        User user = loadUserByUsername(username);
        user.setPassword(password);
        update(user);
    }

    @Override
    public void removePermission(String username, Permission... permission) {
        User user = loadUserByUsername(username);
        user.getAuthorities().removeAll(List.of(permission));
        update(user);
    }

    @Override
    public void addPermission(String username, Permission... permission) {
        User user = loadUserByUsername(username);
        user.getAuthorities().addAll(List.of(permission));
        update(user);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> maybeUser = userRepository.findByUsername(username);

        if (!StringUtils.hasText(username) ||  maybeUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return maybeUser.get();
    }
}
