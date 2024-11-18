package org.prof.it.soft.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
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

    @JsonProperty(value = "username")
    private String username;

    @JsonProperty(value = "role")
    private String role;

    @JsonUnwrapped
    @JsonIgnoreProperties({"id", "created_at", "updated_at"})
    private PersonResponseDto personResponseDto;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

}
