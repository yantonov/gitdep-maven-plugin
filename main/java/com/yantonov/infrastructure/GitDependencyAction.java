package com.yantonov.infrastructure;

import com.yantonov.domain.GitDependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

@FunctionalInterface
public interface GitDependencyAction {
    void apply(MavenProject project,
               GitDependency gitDependency) throws MojoExecutionException;
}
