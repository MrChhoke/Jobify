package org.prof.it.soft.service;

import org.prof.it.soft.dto.response.ProfileResponseDto;
import org.prof.it.soft.dto.security.request.LoginRequestDto;
import org.prof.it.soft.dto.security.request.RegistrationRecruiterRequestDto;
import org.prof.it.soft.dto.security.request.RegistrationRequestDto;
import org.prof.it.soft.dto.security.response.JwtTokenResponseDto;
import org.prof.it.soft.entity.Recruiter;
import org.prof.it.soft.entity.security.Permission;
import org.prof.it.soft.entity.security.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Service interface for managing users.
 * Extends the UserDetailsService interface to provide user-specific data.
 */
public interface UserService extends UserDetailsService {

    /**
     * Allows to get JWT token for user by request
     *
     * @param loginRequestDto - request with user credentials
     * @return response body with JWT token
     * @throws UsernameNotFoundException if the user is not found
     * @throws BadCredentialsException   if the password is incorrect
     */
    JwtTokenResponseDto login(LoginRequestDto loginRequestDto);

    /**
     * Allows to get JWT token for user by request
     *
     * @param RegistrationDto - request with user credentials
     * @return response body with JWT token
     * @throws BadCredentialsException if user with such username already exists
     */
    JwtTokenResponseDto register(RegistrationRequestDto RegistrationDto);

    /**
     * Allows to get JWT token for recruiter by request
     *
     * @param RegistrationDto - request with recruiter credentials
     * @return response body with JWT token
     * @throws BadCredentialsException if user with such username already exists
     */
    ProfileResponseDto registerRecruiter(RegistrationRecruiterRequestDto RegistrationDto);

    /**
     * Saves a new user.
     *
     * @param user the user to save
     * @return the saved user
     */
    User save(User user);

    /**
     * Updates an existing user.
     *
     * @param user the user to update
     * @return the updated user
     */
    User update(User user);

    /**
     * Blocks a user by their username.
     *
     * @param username the username of the user to block
     */
    void block(String username);

    /**
     * Unblocks a user by their username.
     *
     * @param username the username of the user to unblock
     */
    void unblock(String username);

    /**
     * Changes the password of a user.
     *
     * @param username the username of the user
     * @param password the new password
     */
    void changePassword(String username, String password);

    /**
     * Removes permissions from a user.
     *
     * @param username   the username of the user
     * @param permission the permissions to remove
     */
    void removePermission(String username, Permission... permission);

    /**
     * Adds permissions to a user.
     *
     * @param username   the username of the user
     * @param permission the permissions to add
     */
    void addPermission(String username, Permission... permission);

}