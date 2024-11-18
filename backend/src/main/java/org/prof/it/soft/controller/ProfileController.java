package org.prof.it.soft.controller;

import lombok.RequiredArgsConstructor;
import org.prof.it.soft.dto.request.ProfileRequestDto;
import org.prof.it.soft.dto.response.ProfileResponseDto;
import org.prof.it.soft.entity.security.User;
import org.prof.it.soft.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    /**
     * Update profile of authenticated user
     */
    @PutMapping("/update")
    public ResponseEntity<ProfileResponseDto> updateProfile(@Validated @RequestBody ProfileRequestDto profileRequestDto,
                                                            @AuthenticationPrincipal User user) {
        profileService.updateProfile(profileRequestDto, user);
        return ResponseEntity.ok(profileService.getProfile(user.getUsername()));
    }

    /**
     * Get profile of authenticated user
     */
    @GetMapping
    public ResponseEntity<ProfileResponseDto> getOwnProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(profileService.getProfile(user.getUsername()));
    }
}
