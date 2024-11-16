package org.prof.it.soft.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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

    @JsonProperty("message")
    private String message;

    @JsonProperty("candidate_application_id")
    @NotNull
    private Long candidateApplicationId;

    @JsonProperty("candidate_id")
    @NotNull
    private Long candidateId;

    @JsonProperty("vacancy_id")
    @NotNull
    private Long vacancyId;

}
