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
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "vacancy_id",
        "position",
        "salary",
        "technology_stack",
        "created_at",
        "updated_at",
        "recruiter"
})
@NoArgsConstructor
@AllArgsConstructor
public final class VacancyResponseDto {

    @Schema(description = "The vacancy's id", example = "1")
    @JsonProperty("vacancy_id")
    private Long id;

    @Schema(description = "The position of the vacancy", example = "Java Developer")
    @JsonProperty("position")
    private String position;

    @Schema(description = "The salary of the vacancy", example = "1000")
    @JsonProperty("salary")
    private Float salary;

    @Schema(description = "The technology stack of the vacancy", example = "[Java, Spring]")
    @JsonProperty("technology_stack")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> technologyStack;

    @Schema(description = "The created date of the vacancy", example = "2021-07-01T12:00:00")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Schema(description = "The updated date of the vacancy", example = "2021-07-01T12:00:00")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @Schema(description = "The recruiter associated with the vacancy")
    @JsonProperty("recruiter")
    private RecruiterResponseDto recruiter;

    @Schema(description = "The flag that indicates whether the vacancy is applied by the current user")
    @JsonProperty("is_applied_by_current_user")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isAppliedByCurrentUser;

}
