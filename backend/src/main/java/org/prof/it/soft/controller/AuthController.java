package org.prof.it.soft.controller;

import lombok.RequiredArgsConstructor;
import org.prof.it.soft.dto.security.request.LoginRequestDto;
import org.prof.it.soft.dto.security.request.RegistrationRequestDto;
import org.prof.it.soft.dto.security.response.JwtTokenResponseDto;
import org.prof.it.soft.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public JwtTokenResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto);
    }

    @PostMapping("/register")
    public JwtTokenResponseDto register(@RequestBody RegistrationRequestDto requestLoginDto) {
        return userService.register(requestLoginDto);
    }

}
