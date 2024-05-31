package com.example.teamcity.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuildType {
    private String id;
    private String internalId;
    private NewProjectDescription project;
    private String name;
    private Boolean templateFlag;
    private String type; // allowed: regular, composite, deployment
    private Boolean paused;
    private String projectName;
    private String uuid;
    private String description;
    private String projectId;
    private String projectInternalId;
    private String href;
    private String webUrl;
    private Templates templates;
   // private Project project;
    private VscRootEntries vscRootEntries;
    private Properties settings;
    private Properties parameters;
    private Count steps;
    private Count features;
    private Count triggers;
    //private Count snapshot-dependencies;
    /*
inherited

boolean

links

Links

project

Project

templates

BuildTypes

template

BuildType

vcs-root-entries

VcsRootEntries

settings

Properties

parameters

Properties

steps

Steps

features

Features

triggers

Triggers

snapshot-dependencies

SnapshotDependencies

artifact-dependencies

ArtifactDependencies

agent-requirements

AgentRequirements

branches

Branches

builds

Builds

investigations

Investigations

compatibleAgents

Agents

compatibleCloudImages

CloudImages

vcsRootInstances

VcsRootInstances

externalStatusAllowed

boolean

pauseComment

Comment

locator

string
     */
}
