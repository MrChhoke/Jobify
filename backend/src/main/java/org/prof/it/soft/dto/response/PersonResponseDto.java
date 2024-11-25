package org.prof.it.soft.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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

    /**
     * The person's first name.
     */
    @JsonProperty(value = "first_name")
    private String firstName;

    /**
     * The person's last name.
     */
    @JsonProperty(value = "last_name")
    private String lastName;

}
