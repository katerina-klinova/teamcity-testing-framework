package com.teamcity.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
// @Jacksonized is used for deserialization with Jackson. It allows not keep using Gson
@Jacksonized
// @JsonIgnoreProperties(ignoreUnknown = true) allows to ignore the fields in the response that the data model doesn't have
// otherwise the request errors out with an exception
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthModule extends BaseModel {

    @Builder.Default
    private String name = "HTTP-Basic";

}
