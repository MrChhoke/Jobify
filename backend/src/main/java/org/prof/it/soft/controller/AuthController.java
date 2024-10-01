package org.prof.it.soft.controller;

import lombok.RequiredArgsConstructor;
import org.prof.it.soft.dto.security.request.RequestLoginDto;
import org.prof.it.soft.dto.security.request.RequestRegistrationDto;
import org.prof.it.soft.dto.security.response.ResponseJwtTokenDto;
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
    public ResponseJwtTokenDto login(@RequestBody RequestLoginDto requestLoginDto) {
        return userService.login(requestLoginDto);
    }

    @PostMapping("/register")
    public ResponseJwtTokenDto register(@RequestBody RequestRegistrationDto requestLoginDto) {
        return userService.register(requestLoginDto);
    }

}
