package org.prof.it.soft.dto.security.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestRegistrationDto {

    @JsonProperty(value = "username")
    @NotBlank(message = "Username is required")
    private String username;

    @JsonProperty(value = "password")
    @NotBlank(message = "Password is required")
    private String password;

}
