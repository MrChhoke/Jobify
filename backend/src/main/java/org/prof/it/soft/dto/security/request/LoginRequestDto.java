package org.prof.it.soft.dto.security.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    @JsonProperty(value = "username")
    @NotNull(message = "Username is required")
    @NotBlank(message = "Username is required")
    private String username;

    @JsonProperty(value = "password")
    @NotNull(message = "Password is required")
    @NotBlank(message = "Password is required")
    private String password;

}
