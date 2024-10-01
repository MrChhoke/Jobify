package org.prof.it.soft.dto.security.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ResponseJwtTokenDto {

    @JsonProperty(value = "token")
    private String token;

    @JsonProperty(value = "expiration_date")
    private LocalDateTime expirationDate;

}
