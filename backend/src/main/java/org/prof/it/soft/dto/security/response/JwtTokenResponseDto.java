package org.prof.it.soft.dto.security.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenResponseDto {

    @Schema(description = "The JWT token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjI2MjM5MDIyfQ")
    @JsonProperty(value = "token")
    private String token;

    @Schema(description = "The expiration date of the JWT token", example = "2021-07-01T12:00:00")
    @JsonProperty(value = "expiration_date")
    private LocalDateTime expirationDate;

}
