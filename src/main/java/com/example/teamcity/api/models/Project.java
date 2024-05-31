package com.example.teamcity.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    private String id;
    private String name;
    private Boolean virtual;
    private String parentProjectId;
    private String locator;
    private String href;
    private String webUrl;
    private Project parentProject;
    private BuildTypes buildTypes;
    private String description;
    private Templates templates;
    private Count deploymentDashboards;
    private Parameters parameters;
    private CountAndHref vcsRoots;
    private CountAndHref ProjectFeatures;
    private Count projects;
}
