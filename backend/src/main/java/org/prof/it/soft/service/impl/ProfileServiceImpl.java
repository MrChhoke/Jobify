package org.prof.it.soft.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.prof.it.soft.dto.request.PersonRequestDto;
import org.prof.it.soft.dto.request.ProfileRequestDto;
import org.prof.it.soft.dto.response.PersonResponseDto;
import org.prof.it.soft.dto.response.ProfileResponseDto;
import org.prof.it.soft.entity.Person;
import org.prof.it.soft.entity.security.Permission;
import org.prof.it.soft.entity.security.User;
import org.prof.it.soft.exception.NotFoundException;
import org.prof.it.soft.repo.PersonRepository;
import org.prof.it.soft.repo.UserRepository;
import org.prof.it.soft.service.ProfileService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper;

    @Override
    public void updateProfile(ProfileRequestDto profileRequestDto, User currentUser) {
        Person mappedPersonFromDto = modelMapper.map(profileRequestDto.getPerson(), Person.class);
        Person person = personRepository.getReferenceById(currentUser.getId());

        person.setPermissions(new HashSet<>(currentUser.getPermissions()));
        person.setFirstName(mappedPersonFromDto.getFirstName());
        person.setLastName(mappedPersonFromDto.getLastName());
        personRepository.saveAndFlush(person);
    }

    @Override
    public ProfileResponseDto getProfile(String username) {
        Optional<User> maybeUser = userRepository.findByUsername(username);

        if (maybeUser.isEmpty()) {
            throw new NotFoundException(String.format("User with username %s not found", username));
        }

        Person person = personRepository.getReferenceById(maybeUser.get().getId());

        return ProfileResponseDto.builder()
                .userId(person.getId())
                .username(person.getUsername())
                .role(getRoleByPermissions(person.getPermissions()))
                .createdAt(person.getCreatedAt())
                .personResponseDto(
                        PersonResponseDto.builder()
                                .firstName(person.getFirstName())
                                .lastName(person.getLastName())
                                .build())
                .build();
    }

    private String getRoleByPermissions(Collection<Permission> permissions) {
        if (permissions.containsAll(Permission.ADMIN_PERMISSIONS)) {
            return "ADMIN";
        } else if (permissions.containsAll(Permission.RECRUITER_PERMISSIONS)) {
            return "RECRUITER";
        } else if (permissions.containsAll(Permission.USER_PERMISSIONS)) {
            return "USER";
        } else {
            return "UNKNOWN";
        }
    }
}
