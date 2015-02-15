package com.yantonov.exceptions;

import com.yantonov.domain.GitDependency;
import org.apache.maven.plugin.MojoExecutionException;

public class GitDependencyIsNotUsedInPomFile extends MojoExecutionException {
    public GitDependencyIsNotUsedInPomFile(GitDependency dependency) {
        super(String.format("Dependency %s:%s is not found in pom.xml",
                dependency.getGroupId(),
                dependency.getArtifactId()));
    }
}