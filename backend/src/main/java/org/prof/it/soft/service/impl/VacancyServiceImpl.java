package org.prof.it.soft.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.prof.it.soft.dto.filter.VacancyFilterDto;
import org.prof.it.soft.dto.request.VacancyRequestDto;
import org.prof.it.soft.dto.response.VacancyResponseDto;
import org.prof.it.soft.entity.Recruiter;
import org.prof.it.soft.entity.Vacancy;
import org.prof.it.soft.entity.security.User;
import org.prof.it.soft.exception.NotFoundException;
import org.prof.it.soft.repo.RecruiterRepository;
import org.prof.it.soft.repo.VacancyRepository;
import org.prof.it.soft.service.VacancyService;
import org.prof.it.soft.spec.VacancySpecification;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {

    /**
     * The repository for the Vacancy entity.
     */
    protected final VacancyRepository vacancyRepository;

    protected final RecruiterRepository recruiterRepository;

    /**
     * The mapper for converting between DTOs and entities.
     */
    protected final ModelMapper modelMapper;

    /**
     * Saves a new vacancy.
     *
     * @param vacancyDto the DTO of the vacancy to save
     * @return the DTO of the saved vacancy
     * @throws NotFoundException if the recruiter with the given id is not found
     */
    @Override
    public VacancyResponseDto saveVacancy(VacancyRequestDto vacancyDto) {
        Vacancy vacancyFromDto = modelMapper.map(vacancyDto, Vacancy.class);
        Recruiter recruiterById = recruiterRepository.getRecruiterById(vacancyDto.getRecruiterUserId())
                .orElseThrow(() -> new NotFoundException(String.format("Recruiter with id %d not found", vacancyDto.getRecruiterUserId())));
        recruiterById.addVacancy(vacancyFromDto);

        Vacancy savedVacancy = vacancyRepository.saveAndFlush(vacancyFromDto);
        log.info("Vacancy[id={}, position={}, recruiterId={}] saved successfully",
                savedVacancy.getId(), savedVacancy.getPosition(), savedVacancy.getRecruiter().getId());
        return modelMapper.map(savedVacancy, VacancyResponseDto.class);
    }

    /**
     * Updates an existing vacancy.
     *
     * @param vacancyId  the id of the vacancy to update
     * @param vacancyDto the DTO of the vacancy with the updated data
     * @throws NotFoundException if the vacancy with the given id is not found
     */
    @Override
    public void updateVacancy(Long vacancyId, VacancyRequestDto vacancyDto) {
        Vacancy vacancyFromDto = modelMapper.map(vacancyDto, Vacancy.class);
        Vacancy vacancy = getVacancyById(vacancyId);

        vacancy.setPosition(vacancyFromDto.getPosition());
        vacancy.setSalary(vacancyFromDto.getSalary());
        vacancy.setTechnologyStack(vacancyFromDto.getTechnologyStack());
        vacancy.setRecruiter(recruiterRepository.getRecruiterById(vacancyDto.getRecruiterUserId())
                .orElseThrow(() -> new NotFoundException(String.format("Recruiter with id %d not found", vacancyDto.getRecruiterUserId()))));

        vacancyRepository.saveAndFlush(vacancy);
        log.info("Vacancy[id={}, position={}, recruiterId={}] updated successfully",
                vacancy.getId(), vacancy.getPosition(), vacancy.getRecruiter().getId());
    }

    /**
     * Deletes a vacancy by id.
     *
     * @param id the id of the vacancy to delete
     * @throws NotFoundException if the vacancy with the given id is not found
     */
    @Override
    public void deleteVacancy(Long id) {
        // check if vacancy exists (throws NotFoundException if not found)
        Vacancy vacancy = getVacancyById(id);

        vacancy.getRecruiter().removeVacancy(vacancy);

        vacancyRepository.deleteById(id);
        log.info("Vacancy[id={}] deleted successfully", id);
    }

    /**
     * Gets a vacancy by id as a DTO {@link VacancyResponseDto}.
     *
     * @param vacancyId the id of the vacancy to get
     * @return the DTO of the vacancy with the given id
     * @throws NotFoundException if the vacancy with the given id is not found
     */
    @Override
    public VacancyResponseDto getResponseVacancyDtoById(Long vacancyId, User user) {
        VacancyResponseDto vacancyResponseDto = vacancyRepository.findById(vacancyId)
                .map(vacancy -> modelMapper.map(vacancy, VacancyResponseDto.class))
                .orElseThrow(() -> new NotFoundException(String.format("Vacancy with id %d not found", vacancyId)));

        if (Objects.nonNull(user)) {
            boolean vacancyIsAppliedByCandidate =
                    vacancyRepository.isVacancyIsAppliedByCandidate(vacancyId, user.getId());
            vacancyResponseDto.setIsAppliedByCurrentUser(vacancyIsAppliedByCandidate);
        }

        return vacancyResponseDto;
    }

    @Override
    public VacancyResponseDto getResponseVacancyDtoById(Long vacancyId) {
        return getResponseVacancyDtoById(vacancyId, null);
    }

    /**
     * Gets all vacancies as a page.
     *
     * @param vacancyFilterDto the filter for the number of vacancies
     *                         page number is required,
     *                         page size is required,
     *                         the other fields are optional
     * @param user             the user who requested the vacancies
     * @return a page of all vacancies
     * @see org.prof.it.soft.dto.filter.VacancyFilterDto
     * @see org.springframework.data.domain.Page
     * @see VacancyResponseDto
     */
    @Override
    public Page<VacancyResponseDto> getFilteredVacancies(VacancyFilterDto vacancyFilterDto, User user) {
        Page<VacancyResponseDto> vacancies = vacancyRepository.findAll(VacancySpecification.of(vacancyFilterDto),
                        PageRequest.of(vacancyFilterDto.getPage(), vacancyFilterDto.getSize(),
                                Sort.by(Sort.Direction.ASC, "id")))
                .map(vacancy -> modelMapper.map(vacancy, VacancyResponseDto.class));

        if (Objects.nonNull(user)) {
            Set<Long> ids = vacancyRepository.findAllVacancyIdAppliedByCandidate(user.getId());

            vacancies.forEach(vacancy ->
                    vacancy.setIsAppliedByCurrentUser(ids.contains(vacancy.getId())));
        }

        return vacancies;
    }

    @Override
    public Page<VacancyResponseDto> getFilteredVacancies(VacancyFilterDto vacancyFilterDto) {
        return getFilteredVacancies(vacancyFilterDto, null);
    }

    /**
     * Genereates an Excel report with vacancies.
     * The report contains the following columns:
     * - Vacancy ID
     * - Position
     * - Salary
     * - Technology Stack
     * - Company Name
     * - Created At
     * - Recruiter_id
     * - Recruiter First Name
     * - Recruiter Last Name
     * The report is generated based on the filter. All filter fields are optional.
     * All vacancies that match the filter are included in the report.
     *
     * @param vacancyFilterDto the filter for the vacancies
     * @return the Excel file as a Resource
     */
    @Override
    @SneakyThrows
    public Resource generateReportExcel(VacancyFilterDto vacancyFilterDto) {
        // try-with-resources to close workbook and output stream
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // create a sheet for vacancies
            XSSFSheet vacanciesSheet = workbook.createSheet("Vacancies");

            List<Vacancy> filterVacancies = vacancyRepository.findAll(VacancySpecification.of(vacancyFilterDto),
                    Sort.by(Sort.Direction.ASC, "id"));

            int rowNum = 0;
            int cellNum = 0;

            // create header row
            Row headerRow = vacanciesSheet.createRow(rowNum++);
            String[] headers = {"Vacancy ID", "Position", "Salary", "Technology Stack", "Company Name",
                    "Created At", "Recruiter_id", "Recruiter First Name", "Recruiter Last Name", "Recruiter company"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // create data rows for all filtered vacancies
            for (Vacancy vacancy : filterVacancies) {
                Row row = vacanciesSheet.createRow(rowNum++);
                row.createCell(0).setCellValue(vacancy.getId());
                row.createCell(1).setCellValue(vacancy.getPosition());
                if (vacancy.getSalary() != null) {
                    row.createCell(2).setCellValue(vacancy.getSalary());
                }
                row.createCell(3).setCellValue(String.join(", ", vacancy.getTechnologyStack()));
                row.createCell(4).setCellValue(vacancy.getRecruiter().getCompanyName());
                row.createCell(5).setCellValue(vacancy.getCreatedAt().toString());
                row.createCell(6).setCellValue(vacancy.getRecruiter().getId());
                row.createCell(7).setCellValue(vacancy.getRecruiter().getFirstName());
                row.createCell(8).setCellValue(vacancy.getRecruiter().getLastName());
                if (vacancy.getRecruiter().getCompanyName() != null) {
                    row.createCell(9).setCellValue(vacancy.getRecruiter().getCompanyName());
                }
            }
            workbook.write(outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        }
    }


    /**
     * Gets a vacancy by id.
     *
     * @param id the id of the vacancy to get
     * @return the vacancy with the given id
     * @throws NotFoundException if the vacancy with the given id is not found
     */
    public Vacancy getVacancyById(Long id) {
        return vacancyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Vacancy with id %d not found", id)));
    }

    @Override
    public Page<VacancyResponseDto> getVacanciesByRecruiterId(Long recruiterId, Long pageNum) {
        return vacancyRepository.findAllByRecruiterId(recruiterId, PageRequest.of(pageNum.intValue(), 10))
                .map(vacancy -> modelMapper.map(vacancy, VacancyResponseDto.class));
    }
}
