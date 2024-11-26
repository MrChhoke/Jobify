package org.prof.it.soft.service;

import org.prof.it.soft.dto.filter.VacancyFilterDto;
import org.prof.it.soft.dto.request.VacancyRequestDto;
import org.prof.it.soft.dto.response.VacancyResponseDto;
import org.prof.it.soft.entity.security.User;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;

/**
 * Service for working with vacancies
 *
 * @see org.prof.it.soft.entity.Vacancy
 * @see VacancyRequestDto
 * @see VacancyResponseDto
 * @see org.prof.it.soft.dto.filter.VacancyFilterDto
 */
public interface VacancyService {

    /**
     * Saves a new vacancy.
     *
     * @param vacancyDto the DTO of the vacancy to save
     * @return the DTO of the saved vacancy
     */
    VacancyResponseDto saveVacancy(VacancyRequestDto vacancyDto);

    /**
     * Updates an existing vacancy.
     *
     * @param vacancyId  the id of the vacancy to update
     * @param vacancyDto the DTO of the vacancy with the updated data
     * @throws org.prof.it.soft.exception.NotFoundException if the vacancy with the given id is not found
     */
    void updateVacancy(Long vacancyId, VacancyRequestDto vacancyDto);

    /**
     * Deletes a vacancy by id.
     *
     * @param id the id of the vacancy to delete
     * @throws org.prof.it.soft.exception.NotFoundException if the vacancy with the given id is not found
     */
    void deleteVacancy(Long id);

    /**
     * Gets a vacancy by id.
     *
     * @param vacancyId the id of the vacancy to get
     * @return the DTO of the vacancy with the given id
     * @throws org.prof.it.soft.exception.NotFoundException if the vacancy with the given id is not found
     */
    VacancyResponseDto getResponseVacancyDtoById(Long vacancyId);

    VacancyResponseDto getResponseVacancyDtoById(Long vacancyId, User user);

    /**
     * Generates a report in Excel format based on the specified filter.
     *
     * @param vacancyFilterDto the filter for generating the report
     * @return the report in Excel format
     */
    Resource generateReportExcel(VacancyFilterDto vacancyFilterDto);

    Page<VacancyResponseDto> getFilteredVacancies(VacancyFilterDto vacancyFilterDto);

    Page<VacancyResponseDto> getFilteredVacancies(VacancyFilterDto vacancyFilterDto, User user);

}
