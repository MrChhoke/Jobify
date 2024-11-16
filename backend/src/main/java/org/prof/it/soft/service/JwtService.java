package org.prof.it.soft.service;

import org.prof.it.soft.dto.security.response.JwtTokenResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public interface JwtService {

    String extractUserName(String token);
    String generateToken(UserDetails userDetails);
    JwtTokenResponseDto generateTokenResponse(UserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);
    Date extractExpiration(String token);

}
