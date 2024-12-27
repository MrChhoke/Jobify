package org.prof.it.soft.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.prof.it.soft.dto.filter.VacancyFilterDto;
import org.prof.it.soft.dto.request.VacancyRequestDto;
import org.prof.it.soft.dto.response.CandidateApplicationResponseDto;
import org.prof.it.soft.dto.response.CreatingCandidateApplicationResponseDto;
import org.prof.it.soft.dto.response.VacancyResponseDto;
import org.prof.it.soft.entity.security.User;
import org.prof.it.soft.service.CandidateApplicationService;
import org.prof.it.soft.service.VacancyService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/vacancy")
@RequiredArgsConstructor
public class VacancyController {

    /**
     * Service for handling operations related to vacancies.
     */
    protected final VacancyService vacancyService;
    protected final CandidateApplicationService candidateApplicationService;

    @Operation(summary = "Get vacancy by id", parameters = {
            @Parameter(name = "id", description = "Vacancy id which need to be found", example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacancy was found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VacancyResponseDto.class))
            ),
            @ApiResponse(responseCode = "401", description = "Not correct credentials",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "404", description = "Vacancy was not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<VacancyResponseDto> getVacancyById(@PathVariable Long id,
                                                             @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(vacancyService.getResponseVacancyDtoById(id, user));
    }

    @Operation(summary = "Save new vacancy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vacancy was saved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VacancyResponseDto.class))
            ),
            @ApiResponse(responseCode = "401", description = "Not correct credentials",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping
    public ResponseEntity<VacancyResponseDto> saveVacancy(@Validated(VacancyRequestDto.Save.class) @RequestBody VacancyRequestDto responseVacancyDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(vacancyService.saveVacancy(responseVacancyDto));
    }


    @Operation(summary = "Update vacancy by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacancy was updated",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "Message", value = "Vacancy updated successfully")
                    }
            )),
            @ApiResponse(responseCode = "401", description = "Not correct credentials",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "404", description = "Vacancy was not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateVacancy(@Validated(VacancyRequestDto.Update.class) @RequestBody VacancyRequestDto responseVacancyDto,
                                                @PathVariable("id") Long id) {
        vacancyService.updateVacancy(id, responseVacancyDto);
        return ResponseEntity.ok("Vacancy updated successfully");
    }

    @Operation(summary = "Delete vacancy by id", parameters = {
            @Parameter(name = "id", description = "Vacancy id", example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vacancy was deleted",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "Message", value = "Vacancy deleted successfully")
                    }
            )),
            @ApiResponse(responseCode = "401", description = "Not correct credentials",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "404", description = "Vacancy was not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVacancy(@PathVariable Long id) {
        vacancyService.deleteVacancy(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("Vacancy deleted successfully");
    }

    @Operation(summary = "Get all vacancies with filter and pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacancies were returned",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VacancyResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Not correct credentials",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "404", description = "Vacancies were not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping(value = "_list", consumes = "application/json")
    public ResponseEntity<Page<VacancyResponseDto>> getFilteredVacancies(@Validated(VacancyFilterDto.JsonResponse.class) @RequestBody VacancyFilterDto vacancyFilterDto,
                                                                         @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(vacancyService.getFilteredVacancies(vacancyFilterDto, user));
    }

    @Operation(summary = "Apply for a vacancy", parameters = {
            @Parameter(name = "id", description = "Vacancy id which need to be applied", example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Application was created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreatingCandidateApplicationResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Not correct credentials",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "404", description = "Vacancy was not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping(value = "/{id}/apply")
    public ResponseEntity<CreatingCandidateApplicationResponseDto> applyForVacancy(@PathVariable Long id,
                                                                                   @AuthenticationPrincipal User user) {
        CreatingCandidateApplicationResponseDto creatingCandidateApplicationResponseDto =
                candidateApplicationService.createCandidateApplication(id, user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(creatingCandidateApplicationResponseDto);
    }

    @Operation(summary = "Get all applications for the vacancy", parameters = {
            @Parameter(name = "id", description = "Vacancy id which associated with applications", example = "1"),
            @Parameter(name = "pageNum", description = "Page number", example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Applications were found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CandidateApplicationResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Not correct credentials",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "404", description = "Applications were not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{id}/applications")
    public ResponseEntity<Page<CandidateApplicationResponseDto>> getVacancyApplications(@PathVariable(name = "id") Long vacancyId,
                                                                                        @RequestParam(defaultValue = "1") Long pageNum) {
        Page<CandidateApplicationResponseDto> candidateApplicationResponseDto =
                candidateApplicationService.getCandidateApplicationsByVacancyId(vacancyId, pageNum - 1);
        return ResponseEntity.ok(candidateApplicationResponseDto);
    }

    @Operation(summary = "Get all applications for the person", parameters = {
            @Parameter(name = "id", description = "Person id which associated with applications", example = "1"),
            @Parameter(name = "pageNum", description = "Page number", example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Applications were returned",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CandidateApplicationResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Not correct credentials",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "404", description = "Applications were not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/person/{id}/applications")
    public ResponseEntity<Page<CandidateApplicationResponseDto>> getPersonApplications(@PathVariable(name = "id") Long personId,
                                                                                       @RequestParam Long pageNum) {
        Page<CandidateApplicationResponseDto> candidateApplicationResponseDto =
                candidateApplicationService.getCandidateApplicationsByPersonId(personId, pageNum);
        return ResponseEntity.ok(candidateApplicationResponseDto);
    }

    @Operation(summary = "Generate report in Excel format")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report was generated",
                    content = @Content(mediaType = "application/octet-stream")
            ),
            @ApiResponse(responseCode = "401", description = "Not correct credentials",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping(value = "_report", consumes = "application/json")
    public ResponseEntity<Resource> generateReportExcel(@Validated @RequestBody(required = false) VacancyFilterDto vacancyFilterDto) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vacancies_" + LocalDateTime.now() + ".xlsx");

        Resource resource = vacancyService.generateReportExcel(vacancyFilterDto);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @Operation(summary = "Get all vacancies which were created by the recruiter", parameters = {
            @Parameter(name = "pageNum", description = "Page number", example = "1")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vacancies were found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = VacancyResponseDto.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Not correct credentials",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "404", description = "Vacancies were not found",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/applied")
    public ResponseEntity<Page<CandidateApplicationResponseDto>> getAppliedVacancies(@AuthenticationPrincipal User user,
                                                                                     @RequestParam(defaultValue = "1") Long pageNum) {
        Page<CandidateApplicationResponseDto> candidateApplicationResponseDto =
                candidateApplicationService.getCandidateApplicationsByPersonId(user.getId(), pageNum - 1);
        return ResponseEntity.ok(candidateApplicationResponseDto);
    }
}