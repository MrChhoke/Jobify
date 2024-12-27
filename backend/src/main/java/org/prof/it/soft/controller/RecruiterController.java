package org.prof.it.soft.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.prof.it.soft.dto.response.CandidateApplicationResponseDto;
import org.prof.it.soft.dto.response.ProfileResponseDto;
import org.prof.it.soft.dto.response.VacancyResponseDto;
import org.prof.it.soft.dto.security.request.RegistrationRecruiterRequestDto;
import org.prof.it.soft.entity.security.User;
import org.prof.it.soft.service.CandidateApplicationService;
import org.prof.it.soft.service.UserService;
import org.prof.it.soft.service.VacancyService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recruiter")
@RequiredArgsConstructor
public class RecruiterController {

    protected final UserService userService;
    protected final VacancyService vacancyService;
    protected final CandidateApplicationService candidateApplicationService;

    @Operation(summary = "Register new recruiter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recruiter is registered",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProfileResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "User with such username already exists",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "401", description = "Not correct credentials",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "403", description = "Access denied",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping("/register")
    public ResponseEntity<ProfileResponseDto> registerRecruiter(@Validated(RegistrationRecruiterRequestDto.Save.class) @RequestBody RegistrationRecruiterRequestDto recruiterRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.registerRecruiter(recruiterRequestDto));
    }

    @Operation(summary = "Get all applications which were sent to the recruiter", parameters = {
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
    @GetMapping("/applications")
    public ResponseEntity<Page<CandidateApplicationResponseDto>> getRecruiterApplications(@AuthenticationPrincipal User user,
                                                                                          @RequestParam(defaultValue = "1") Long pageNum) {
        Page<CandidateApplicationResponseDto> candidateApplicationResponseDto =
                candidateApplicationService.getCandidateApplicationsByRecruiterId(user.getId(), pageNum - 1);
        return ResponseEntity.ok(candidateApplicationResponseDto);
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
    @GetMapping("/vacancies")
    public ResponseEntity<Page<VacancyResponseDto>> getRecruiterVacancies(@AuthenticationPrincipal User user,
                                                                          @RequestParam(defaultValue = "1") Long pageNum) {
        Page<VacancyResponseDto> vacancyResponseDto = vacancyService.getVacanciesByRecruiterId(user.getId(), pageNum - 1);
        return ResponseEntity.ok(vacancyResponseDto);
    }
}