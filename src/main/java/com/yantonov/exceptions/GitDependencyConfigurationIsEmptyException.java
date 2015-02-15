package com.yantonov.exceptions;

import com.yantonov.domain.GitDependency;
import org.apache.maven.plugin.MojoExecutionException;

public class GitDependencyConfigurationIsEmptyException extends MojoExecutionException {
    public GitDependencyConfigurationIsEmptyException(GitDependency dependency) {

        super(String.format("Git dependency has instalid configuration. GroupId, artifactId, repositoryLocation are required fields. [%s:%s repo=%s]",
                dependency.getGroupId() == null ? "" : dependency.getGroupId(),
                dependency.getArtifactId() == null ? "" : dependency.getArtifactId(),
                dependency.getRepositoryLocation() == null ? "" : dependency.getRepositoryLocation()));
    }
}
