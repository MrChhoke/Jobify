package org.prof.it.soft.dto.response;

import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "recruiter_user_id",
        "company_name",
        "person",
        "created_at",
        "updated_at"
})
@NoArgsConstructor
@AllArgsConstructor
public final class RecruiterResponseDto {

    @Schema(description = "The recruiter's id", example = "1")
    @JsonProperty("recruiter_user_id")
    private Long id;

    @Schema(description = "The company name of the recruiter", example = "PROFITSOFT")
    @JsonProperty("company_name")
    private String companyName;

    @Schema(description = "The person associated with the recruiter")
    @JsonUnwrapped
    private PersonResponseDto person;

    @Schema(description = "The created date of the recruiter", example = "2021-07-01T12:00:00")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Schema(description = "The updated date of the recruiter", example = "2021-07-01T12:00:00")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

}

