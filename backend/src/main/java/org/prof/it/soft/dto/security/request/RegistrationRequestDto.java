package org.prof.it.soft.dto.security.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequestDto {

    @Schema(description = "The username of the user", example = "user",
            requiredProperties = {"password", "first_name", "last_name"})
    @JsonProperty(value = "username")
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(description = "The password of the user", example = "password",
            requiredProperties = {"username", "first_name", "last_name"})
    @JsonProperty(value = "password")
    @NotBlank(message = "Password is required")
    private String password;

    @Schema(description = "The first name of the user", example = "John",
            requiredProperties = {"username", "password", "last_name"})
    @JsonProperty(value = "first_name")
    @NotBlank(message = "First name is required")
    private String firstName;

    @Schema(description = "The last name of the user", example = "Doe",
            requiredProperties = {"username", "password", "first_name"})
    @JsonProperty(value = "last_name")
    @NotBlank(message = "Last name is required")
    private String lastName;
}
