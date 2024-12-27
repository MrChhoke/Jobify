package org.prof.it.soft.dto.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object (DTO) for filtering vacancies.
 * It includes fields like recruiter id, position, salary range, company name,
 * technology stack, creation date range, page number, and page size.
 */
@Data
public class VacancyFilterDto {

    /**
     * The id of the recruiter.
     */
    @Schema(description = "The id of the recruiter", example = "1")
    @JsonProperty("recruiter_id")
    private final Long recruiterId;

    @Schema(description = "The position of the vacancy", example = "Java Developer")
    @JsonProperty("position")
    private final String position;

    @Schema(description = "The minimum salary of the vacancy", example = "1000")
    @JsonProperty("min_salary")
    private final Float minSalary;

    @Schema(description = "The maximum salary of the vacancy", example = "2000")
    @JsonProperty("max_salary")
    private final Float maxSalary;

    @Schema(description = "The company name", example = "PROFITSOFT")
    @JsonProperty("company_name")
    private final String companyName;

    @Schema(description = "The technology stack", example = "[Java, Spring]")
    @JsonProperty("technology_stack")
    private final List<String> technologyStack;

    @Schema(description = "The minimum creation date of the vacancy", example = "2021-01-01T00:00:00")
    @JsonProperty("created_at_min")
    private final LocalDateTime createdAtMin;

    @Schema(description = "The maximum creation date of the vacancy", example = "2021-12-31T23:59:59")
    @JsonProperty("created_at_max")
    private final LocalDateTime createdAtMax;

    @Schema(description = "The page number for pagination, " +
            "it is required for JsonResponse", example = "1")
    @JsonProperty("page")
    @NotNull(message = "Page number is required", groups = JsonResponse.class)
    private Integer page;

    @Schema(description = "The page size for pagination, " +
            "it is required for JsonResponse", example = "10")
    @JsonProperty("size")
    @NotNull(message = "Page size is required", groups = JsonResponse.class)
    private Integer size;

    /**
     * Interface for JsonResponse.
     */
    public interface JsonResponse {
    }

    /**
     * Interface for ExcelResponse.
     */
    public interface ExcelResponse {
    }
}