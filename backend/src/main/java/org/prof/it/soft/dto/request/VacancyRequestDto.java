package org.prof.it.soft.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public final class VacancyRequestDto {

    /**
     * The position of the vacancy.
     */
    @Schema(description = "The position of the vacancy", example = "Java Developer",
            requiredProperties = {"salary", "recruiter_id"})
    @JsonProperty(value = "position")
    @NotNull(groups = {Save.class, Update.class}, message = "Position is required")
    @NotBlank(groups = {Save.class, Update.class}, message = "Position is required")
    private String position;

    @Schema(description = "The salary of the vacancy", example = "1000",
            requiredProperties = {"position", "recruiter_id"})
    @JsonProperty(value = "salary")
    @Positive(groups = {Save.class, Update.class}, message = "Salary must be greater than 0")
    private Float salary;

    @Schema(description = "The technology stack of the vacancy", example = "[Java, Spring]",
            requiredProperties = {"position", "salary", "recruiter_id"})
    @JsonProperty(value = "technology_stack")
    private List<String> technologyStack;

    @Schema(description = "The company name of the vacancy", example = "PROFITSOFT",
            requiredProperties = {"position", "salary"})
    @JsonProperty(value = "recruiter_id")
    @NotNull(groups = {Save.class, Update.class}, message = "Recruiter id is required")
    private Long recruiterUserId;

    public interface Save {
    }

    public interface Update {
    }
}
