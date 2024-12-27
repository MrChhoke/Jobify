package org.prof.it.soft.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@JsonPropertyOrder({
        "message",
        "candidate_application_id",
        "candidate_id",
        "vacancy_id"
})
@NoArgsConstructor
@AllArgsConstructor
public class CreatingCandidateApplicationResponseDto {

    @Schema(description = "The message of the response", example = "Candidate application was created successfully")
    @JsonProperty("message")
    private String message;

    @Schema(description = "The ID of the candidate application", example = "1")
    @JsonProperty("candidate_application_id")
    @NotNull
    private Long candidateApplicationId;

    @Schema(description = "The ID of the candidate", example = "1")
    @JsonProperty("candidate_id")
    @NotNull
    private Long candidateId;

    @Schema(description = "The ID of the vacancy", example = "1")
    @JsonProperty("vacancy_id")
    @NotNull
    private Long vacancyId;

}
