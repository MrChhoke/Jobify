package org.prof.it.soft.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDto {

    @Schema(description = "The user id", example = "1")
    @JsonProperty(value = "user_id")
    private Long userId;

    @Schema(description = "The username (login) of the user", example = "john_doe")
    @JsonProperty(value = "username")
    private String username;

    @Schema(description = "The role of the user", example = "USER")
    @JsonProperty(value = "role")
    private String role;

    @JsonUnwrapped
    private PersonResponseDto personResponseDto;

    @Schema(description = "The created date of the user", example = "2021-07-01T12:00:00")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

}
