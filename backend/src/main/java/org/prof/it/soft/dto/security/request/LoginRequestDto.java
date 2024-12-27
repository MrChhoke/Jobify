package org.prof.it.soft.dto.security.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    @Schema(description = "The username of the user", example = "user",
            requiredProperties = {"password"})
    @JsonProperty(value = "username")
    @NotNull(message = "Username is required")
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(description = "The password of the user", example = "password",
            requiredProperties = {"username"})
    @JsonProperty(value = "password")
    @NotNull(message = "Password is required")
    @NotBlank(message = "Password is required")
    private String password;

}
