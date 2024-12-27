package org.prof.it.soft.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.prof.it.soft.dto.request.ProfileRequestDto;
import org.prof.it.soft.dto.response.ProfileResponseDto;
import org.prof.it.soft.entity.security.User;
import org.prof.it.soft.service.ProfileService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @Operation(summary = "Update profile of authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile is updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProfileResponseDto.class))
            ),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "401", description = "Not correct credentials",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PutMapping("/update")
    public ResponseEntity<ProfileResponseDto> updateProfile(@Validated @RequestBody ProfileRequestDto profileRequestDto,
                                                            @AuthenticationPrincipal User user) {
        profileService.updateProfile(profileRequestDto, user);
        return ResponseEntity.ok(profileService.getProfile(user.getUsername()));
    }

    @Operation(summary = "Get profile of authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile is returned",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProfileResponseDto.class))
            ),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "401", description = "Not correct credentials",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping
    public ResponseEntity<ProfileResponseDto> getOwnProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(profileService.getProfile(user.getUsername()));
    }
}
