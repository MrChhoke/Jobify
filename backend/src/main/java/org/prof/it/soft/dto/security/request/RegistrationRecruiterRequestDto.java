package org.prof.it.soft.dto.security.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.prof.it.soft.dto.request.PersonRequestDto;
import org.springframework.util.StringUtils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class RegistrationRecruiterRequestDto {

    @Schema(name = "username", description = "Username of the recruiter",
            example = "recruiter", requiredProperties = {"password", "first_name", "last_name"})
    @NotBlank(groups = {Save.class}, message = "Username is required")
    private String username;

    @Schema(name = "password", description = "Password of the recruiter",
            example = "recruiter", requiredProperties = {"username", "first_name", "last_name"})
    @NotBlank(groups = {Save.class}, message = "Password is required")
    private String password;

    @Schema(name = "company_name", description = "Company name of the recruiter",
            example = "ProfITsoft", nullable = true)
    @JsonProperty(value = "company_name")
    private String companyName;

    @JsonUnwrapped
    @NotNull(groups = {Save.class})
    @Valid
    private PersonRequestDto person;

    /**
     * This method checks if the recruiter DTO is empty.
     * It returns true if both the first name and last name of the person object are not present.
     */
    @Schema(hidden = true)
    @AssertTrue(groups = {Update.class})
    public boolean isEmptyRequestRecruiterDto() {
        return !StringUtils.hasText(person.getFirstName()) &&
                !StringUtils.hasText(person.getLastName());
    }

    /**
     * This interface is used as a group for validation constraints.
     * It is used when saving a recruiter.
     */
    public interface Save {
    }

    /**
     * This interface is used as a group for validation constraints.
     * It is used when updating a recruiter.
     */
    public interface Update {
    }

}