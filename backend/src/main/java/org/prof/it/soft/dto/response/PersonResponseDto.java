package org.prof.it.soft.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "first_name",
        "last_name",
        "created_at",
        "updated_at"
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class PersonResponseDto {

    @Schema(description = "The first name of the person", example = "John")
    @JsonProperty(value = "first_name")
    private String firstName;

    @Schema(description = "The last name of the person", example = "Doe")
    @JsonProperty(value = "last_name")
    private String lastName;

}
