package org.prof.it.soft.service;

import org.prof.it.soft.dto.request.RecruiterRequestDto;
import org.prof.it.soft.dto.response.RecruiterResponseDto;
import org.prof.it.soft.entity.Recruiter;

import java.util.Set;

/**
 * Service for working with recruiters
 *
 * @see org.prof.it.soft.entity.Recruiter
 * @see RecruiterRequestDto
 * @see RecruiterResponseDto
 */
public interface RecruiterService {

    /**
     * Saves a new recruiter.
     *
     * @param recruiterDto the DTO of the recruiter to save
     * @return the {@link RecruiterResponseDto} of the saved recruiter
     */
    RecruiterResponseDto saveRecruiter(RecruiterRequestDto recruiterDto);

    /**
     * Updates an existing recruiter.
     *
     * @param recruiterId  the id of the recruiter to update
     * @param recruiterDto the {@link RecruiterResponseDto} of the recruiter with the updated data
     * @throws org.prof.it.soft.exception.NotFoundException if the recruiter with the given id is not found
     */
    void updateRecruiter(Long recruiterId, RecruiterRequestDto recruiterDto);

    /**
     * Deletes a recruiter by id.
     *
     * @param id the id of the {@link Recruiter} to delete
     * @throws org.prof.it.soft.exception.NotFoundException if the recruiter with the given id is not found
     */
    void deleteRecruiter(Long id);

    /**
     * Gets a recruiter by id.
     *
     * @param recruiterId the id of the recruiter to get
     * @return the DTO of the recruiter with the given id
     * @throws org.prof.it.soft.exception.NotFoundException if the recruiter with the given id is not found
     */
    RecruiterResponseDto getResponseRecruiterDtoById(Long recruiterId);

    /**
     * Gets a recruiter by id.
     *
     * @param recruiterId the id of the {@link Recruiter} to get
     * @return the recruiter with the given id
     * @throws org.prof.it.soft.exception.NotFoundException if the recruiter with the given id is not found
     */
    Recruiter getRecruiterById(Long recruiterId);

    /**
     * Gets the ids of all recruiters.
     *
     * @return a set of the ids of all recruiters
     */
    Set<Long> getRecruiterIds();
}
