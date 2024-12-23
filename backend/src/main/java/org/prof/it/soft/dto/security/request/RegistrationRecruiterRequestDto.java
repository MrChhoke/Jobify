package org.prof.it.soft.dto.security.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
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

    /**
     * The username of the recruiter, which will be used for authentication.
     */
    @NotBlank(groups = {Save.class}, message = "Username is required")
    private String username;

    /**
     * The password of the recruiter, which will be used for authentication.
     */
    @NotBlank(groups = {Save.class}, message = "Password is required")
    private String password;

    /**
     * The name of the company the recruiter works for.
     */
    @JsonProperty(value = "company_name")
    private String companyName;

    /**
     * The person object associated with the recruiter.
     */
    @JsonUnwrapped
    @NotNull(groups = {Save.class})
    @Valid
    private PersonRequestDto person;

    /**
     * This method checks if the recruiter DTO is empty.
     * It returns true if both the first name and last name of the person object are not present.
     */
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