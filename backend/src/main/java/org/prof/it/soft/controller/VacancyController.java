package org.prof.it.soft.controller;

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

    /**
     * Get a vacancy by id.
     *
     * @param id The id of the vacancy.
     * @return The vacancy with the given id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VacancyResponseDto> getVacancyById(@PathVariable Long id,
                                                             @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(vacancyService.getResponseVacancyDtoById(id, user));
    }

    /**
     * Save a new vacancy.
     *
     * @param responseVacancyDto The data of the vacancy to be saved.
     * @return The saved vacancy.
     */
    @PostMapping
    public ResponseEntity<VacancyResponseDto> saveVacancy(@Validated(VacancyRequestDto.Save.class) @RequestBody VacancyRequestDto responseVacancyDto) {
        return ResponseEntity.ok(vacancyService.saveVacancy(responseVacancyDto));
    }

    /**
     * Update a vacancy.
     *
     * @param responseVacancyDto The new data of the vacancy.
     * @param id                 The id of the vacancy to be updated.
     * @return A message indicating that the vacancy was updated successfully.
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateVacancy(@Validated(VacancyRequestDto.Update.class) @RequestBody VacancyRequestDto responseVacancyDto,
                                                @PathVariable("id") Long id) {
        vacancyService.updateVacancy(id, responseVacancyDto);
        return ResponseEntity.ok("Vacancy updated successfully");
    }

    /**
     * Delete a vacancy.
     *
     * @param id The id of the vacancy to be deleted.
     * @return A message indicating that the vacancy was deleted successfully.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVacancy(@PathVariable Long id) {
        vacancyService.deleteVacancy(id);
        return ResponseEntity.ok("Vacancy deleted successfully");
    }

    /**
     * Get filtered vacancies.
     *
     * @param vacancyFilterDto The filter criteria.
     * @return The vacancies that match the filter criteria.
     */
    @PostMapping(value = "_list", consumes = "application/json")
    public ResponseEntity<Page<VacancyResponseDto>> getFilteredVacancies(@Validated(VacancyFilterDto.JsonResponse.class) @RequestBody VacancyFilterDto vacancyFilterDto,
                                                                         @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(vacancyService.getFilteredVacancies(vacancyFilterDto, user));
    }

    /**
     * Apply for a vacancy.
     *
     * @param id   The id of the vacancy to apply for.
     * @param user The user who is applying for the vacancy.
     * @return A message indicating that the application was created successfully.
     */
    @PostMapping(value = "/{id}/apply")
    public ResponseEntity<CreatingCandidateApplicationResponseDto> applyForVacancy(@PathVariable Long id,
                                                                                   @AuthenticationPrincipal User user) {
        CreatingCandidateApplicationResponseDto creatingCandidateApplicationResponseDto =
                candidateApplicationService.createCandidateApplication(id, user);
        return ResponseEntity.ok(creatingCandidateApplicationResponseDto);
    }

    /**
     * Get the applications for a vacancy.
     *
     * @param vacancyId The id of the vacancy.
     * @param pageNum   The page number.
     * @return The applications for the vacancy.
     */
    @GetMapping("/{id}/applications")
    public ResponseEntity<Page<CandidateApplicationResponseDto>> getVacancyApplications(@PathVariable(name = "id") Long vacancyId,
                                                                                        @RequestParam(defaultValue = "1") Long pageNum) {
        Page<CandidateApplicationResponseDto> candidateApplicationResponseDto =
                candidateApplicationService.getCandidateApplicationsByVacancyId(vacancyId, pageNum - 1);
        return ResponseEntity.ok(candidateApplicationResponseDto);
    }

    /**
     * Get the applications for a person.
     *
     * @param personId The id of the person.
     * @param pageNum  The page number.
     * @return The applications for the person.
     */
    @GetMapping("/person/{id}/applications")
    public ResponseEntity<Page<CandidateApplicationResponseDto>> getPersonApplications(@PathVariable(name = "id") Long personId,
                                                                                       @RequestParam Long pageNum) {
        Page<CandidateApplicationResponseDto> candidateApplicationResponseDto =
                candidateApplicationService.getCandidateApplicationsByPersonId(personId, pageNum);
        return ResponseEntity.ok(candidateApplicationResponseDto);
    }

    /**
     * Generate a report in Excel format.
     *
     * @param vacancyFilterDto The filter criteria.
     * @return The report in Excel format.
     * @throws IOException If an I/O error occurs.
     */
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
}