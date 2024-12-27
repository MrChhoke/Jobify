package org.prof.it.soft.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(name = "first_name", description = "First name of the person",
            example = "John", requiredProperties = {"last_name"})
    @JsonProperty(value = "first_name")
    @NotNull(groups = {Save.class, Update.class}, message = "First name is required")
    @NotBlank(groups = {Save.class, Update.class}, message = "First name is required")
    private String firstName;

    @Schema(name = "last_name", description = "Last name of the person",
            example = "Doe", requiredProperties = {"first_name"})
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