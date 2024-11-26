package org.prof.it.soft.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class PersonRequestDto {

    /**
     * The first name of the person.
     */
    @JsonProperty(value = "first_name")
    @NotNull(groups = {Save.class, Update.class}, message = "First name is required")
    @NotBlank(groups = {Save.class, Update.class}, message = "First name is required")
    private String firstName;

    /**
     * The last name of the person.
     */
    @JsonProperty(value = "last_name")
    @NotNull(groups = {Save.class, Update.class}, message = "Last name is required")
    @NotBlank(groups = {Save.class, Update.class}, message = "Last name is required")
    private String lastName;

    /**
     * This interface is used as a group for validation constraints.
     */
    public interface Save {
    }

    /**
     * This interface is used as a group for validation constraints.
     */
    public interface Update {
    }
}