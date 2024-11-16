package org.prof.it.soft.controller;

import lombok.RequiredArgsConstructor;
import org.prof.it.soft.dto.request.PersonRequestDto;
import org.prof.it.soft.dto.request.RecruiterRequestDto;
import org.prof.it.soft.dto.response.CandidateApplicationResponseDto;
import org.prof.it.soft.dto.response.RecruiterResponseDto;
import org.prof.it.soft.service.CandidateApplicationService;
import org.prof.it.soft.service.RecruiterService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/recruiter")
@RequiredArgsConstructor
public class RecruiterController {

    /**
     * Service for handling operations related to recruiters.
     */
    protected final RecruiterService recruiterService;
    protected final CandidateApplicationService candidateApplicationService;

    /**
     * Get a recruiter by id.
     * @param id The id of the recruiter.
     * @return The recruiter with the given id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RecruiterResponseDto> getRecruiterById(@PathVariable Long id) {
        return ResponseEntity.ok(recruiterService.getResponseRecruiterDtoById(id));
    }

    /**
     * Save a new recruiter.
     * @param recruiterRequestDto The data of the recruiter to be saved.
     * @return The saved recruiter.
     */
    @PostMapping
    public ResponseEntity<RecruiterResponseDto> saveRecruiter(@Validated({RecruiterRequestDto.Save.class, PersonRequestDto.Save.class})
                                                                  @RequestBody RecruiterRequestDto recruiterRequestDto) {
        RecruiterResponseDto recruiterResponseDto = recruiterService.saveRecruiter(recruiterRequestDto);
        return ResponseEntity.ok(recruiterResponseDto);
    }

    /**
     * Update a recruiter.
     * @param recruiterRequestDto The new data of the recruiter.
     * @param id The id of the recruiter to be updated.
     * @return A message indicating that the recruiter was updated successfully.
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateRecruiter(@Validated(RecruiterRequestDto.Update.class) @RequestBody RecruiterRequestDto recruiterRequestDto,
                                                  @PathVariable Long id) {
        recruiterService.updateRecruiter(id, recruiterRequestDto);
        return ResponseEntity.ok("Recruiter updated successfully");
    }

    /**
     * Delete a recruiter.
     * @param id The id of the recruiter to be deleted.
     * @return A message indicating that the recruiter was deleted successfully.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecruiter(@PathVariable Long id) {
        recruiterService.deleteRecruiter(id);
        return ResponseEntity.ok("Recruiter deleted successfully");
    }

    /**
     * Get all applications which were sent to the recruiter.
     * @param recruiterId The id of the recruiter.
     * @param pageNum The page number for pagination.
     */
    @GetMapping("/{id}/applications")
    public ResponseEntity<Page<CandidateApplicationResponseDto>> getRecruiterVacancies(@PathVariable(name = "id") Long recruiterId,
                                                        @RequestParam Long pageNum) {
        Page<CandidateApplicationResponseDto> candidateApplicationResponseDto =
                candidateApplicationService.getCandidateApplicationsByRecruiterId(recruiterId, pageNum);
        return ResponseEntity.ok(candidateApplicationResponseDto);
    }
}