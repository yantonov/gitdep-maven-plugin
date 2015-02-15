package com.yantonov.domain;

import org.apache.maven.plugins.annotations.Parameter;

public class GitDependency {
    @Parameter(property = "groupId")
    private String groupId;

    @Parameter(property = "artifactId")
    private String artifactId;

    @Parameter(property = "repositoryLocation")
    private String repositoryLocation;

    @Parameter(property = "branch")
    private String branch = "master";

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getRepositoryLocation() {
        return repositoryLocation;
    }

    public void setRepositoryLocation(String repositoryLocation) {
        this.repositoryLocation = repositoryLocation;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
