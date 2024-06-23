package com.teamcity.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// To prevent warning "Generating equals/hashCode implementation but without a call to superclass."
@EqualsAndHashCode(callSuper = false)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class Build extends BaseModel {

    private String id;
    private BuildType buildType;
    private String status;
    private String state;

}
