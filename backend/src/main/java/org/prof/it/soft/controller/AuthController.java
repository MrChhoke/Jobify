package org.prof.it.soft.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.prof.it.soft.dto.security.request.LoginRequestDto;
import org.prof.it.soft.dto.security.request.RegistrationRequestDto;
import org.prof.it.soft.dto.security.response.JwtTokenResponseDto;
import org.prof.it.soft.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "Get JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JWT token is returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JwtTokenResponseDto.class))
            ),
            @ApiResponse(responseCode = "401", description = "Not correct credentials",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(userService.login(loginRequestDto));
    }

    @Operation(summary = "Register new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered, JWT token is returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JwtTokenResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "User with such username already exists",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/register")
    public ResponseEntity<JwtTokenResponseDto> register(@RequestBody RegistrationRequestDto requestLoginDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.register(requestLoginDto));
    }

}
