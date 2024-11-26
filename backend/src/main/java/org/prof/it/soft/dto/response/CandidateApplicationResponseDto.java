package org.prof.it.soft.dto.response;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@JsonPropertyOrder({
        "vacancy",
        "candidate"
})
@NoArgsConstructor
@AllArgsConstructor
public class CandidateApplicationResponseDto {

    @JsonProperty("vacancy")
    @JsonIncludeProperties({"vacancy_id", "position", "salary"})
    VacancyResponseDto vacancy;

    @JsonProperty("candidate")
    @JsonIncludeProperties({"first_name", "last_name"})
    PersonResponseDto candidate;

}
