package com.yantonov.infrastructure;

import com.yantonov.domain.GitDependency;
import com.yantonov.helpers.DirectoryUtils;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;

public class TemporaryDirectory {
    private final String temporaryDirectoryName = "gitdep-maven-plugin-tmp";

    public void create(MavenProject project) {
        File directory = getTemporaryDirectory(project);
        if (!directory.exists())
            directory.mkdirs();
    }

    public void remove(MavenProject project) throws IOException {
        File directory = getTemporaryDirectory(project);
        DirectoryUtils.deleteRecursive(directory);
    }

    public void createDependencyDirectory(MavenProject project, GitDependency dependency) {
        File directory = getTemporaryDirectory(project, dependency);
        if (!directory.exists())
            directory.mkdirs();
    }

    public File getTemporaryDirectory(MavenProject project, GitDependency dependency) {
        return new File(String.format("%s%s%s-%s",
                getBaseTemporaryDirectoryPath(project),
                File.separator,
                dependency.getGroupId(),
                dependency.getArtifactId()));
    }

    public File getTemporaryDirectory(MavenProject project) {
        return new File(getBaseTemporaryDirectoryPath(project));
    }

    private String getBaseTemporaryDirectoryPath(MavenProject project) {
        return String.format("%s%s%s",
                project.getBuild().getDirectory(),
                File.separator,
                temporaryDirectoryName);
    }

    public void remove(MavenProject project, GitDependency gitDependency) throws IOException {
        DirectoryUtils.deleteRecursive(getTemporaryDirectory(project, gitDependency));
    }
}
