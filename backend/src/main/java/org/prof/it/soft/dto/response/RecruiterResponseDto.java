package org.prof.it.soft.dto.response;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "recruiter_user_id",
        "company_name",
        "person",
        "created_at",
        "updated_at"
})
@NoArgsConstructor
@AllArgsConstructor
public final class RecruiterResponseDto {

    /**
     * The recruiter's id.
     */
    @JsonProperty("recruiter_user_id")
    private Long id;

    /**
     * The recruiter's company name.
     */
    @JsonProperty("company_name")
    private String companyName;

    /**
     * The person associated with the recruiter.
     * It will be serialized as a part of this object. The created_at and updated_at fields will be ignored.
     *
     * @see PersonResponseDto
     */
    @JsonUnwrapped
    private PersonResponseDto person;

    /**
     * The date and time when the recruiter was created.
     */
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    /**
     * The date and time when the recruiter was updated.
     */
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

}

