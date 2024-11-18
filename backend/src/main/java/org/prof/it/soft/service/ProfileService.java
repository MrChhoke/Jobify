package org.prof.it.soft.service;

import org.prof.it.soft.dto.request.ProfileRequestDto;
import org.prof.it.soft.dto.response.ProfileResponseDto;
import org.prof.it.soft.entity.security.User;

public interface ProfileService {

    void updateProfile(ProfileRequestDto profileRequestDto, User currentUser);

    ProfileResponseDto getProfile(String username);

}
