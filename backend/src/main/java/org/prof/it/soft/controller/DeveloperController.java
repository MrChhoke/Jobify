package org.prof.it.soft.controller;

import lombok.RequiredArgsConstructor;
import org.prof.it.soft.entity.security.Permission;
import org.prof.it.soft.entity.security.User;
import org.prof.it.soft.repo.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/developers")
@RestController
@RequiredArgsConstructor
@Profile("dev")
public class DeveloperController {

    private final UserRepository userRepository;

    @PutMapping("/update-to-admin")
    public ResponseEntity<String> updateToAdmin(@AuthenticationPrincipal User user) {
        user.setPermissions(Permission.ADMIN_PERMISSIONS);
        userRepository.saveAndFlush(user);
        return ResponseEntity.ok("Successfully updated to admin");
    }

    @PutMapping("/update-to-recruiter")
    public ResponseEntity<String> updateToRecruiter(@AuthenticationPrincipal User user) {
        user.setPermissions(Permission.RECRUITER_PERMISSIONS);
        userRepository.saveAndFlush(user);
        return ResponseEntity.ok("Successfully updated to recruiter");
    }

    @PutMapping("/update-to-user")
    public ResponseEntity<String> updateToUser(@AuthenticationPrincipal User user) {
        user.setPermissions(Permission.USER_PERMISSIONS);
        userRepository.saveAndFlush(user);
        return ResponseEntity.ok("Successfully updated to user");
    }
}
