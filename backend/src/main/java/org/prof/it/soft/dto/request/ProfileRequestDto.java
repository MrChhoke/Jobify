package org.prof.it.soft.dto.request;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequestDto {

    @JsonUnwrapped
    private PersonRequestDto person;

    @AssertTrue
    public boolean isProfileValid() {
        return person != null &&
                StringUtils.hasText(person.getFirstName()) &&
                StringUtils.hasText(person.getLastName());
    }
}