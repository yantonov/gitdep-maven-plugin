package com.yantonov.infrastructure;

import com.yantonov.domain.GitDependency;
import com.yantonov.exceptions.GitDependencyConfigurationIsEmptyException;
import com.yantonov.exceptions.GitDependencyIsNotUsedInPomFile;
import com.yantonov.helpers.StringHelpers;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.RepositorySystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UninstalledDependencyActionInvoker {

    public void invoke(ArtifactRepository localRepository,
                       ArtifactResolver artifactResolver,
                       RepositorySystem repositorySystem,
                       MavenProject project,
                       List<GitDependency> gitDependencies,
                       GitDependencyAction action) throws MojoExecutionException {

        createTempDir(project);
        Map<String, Dependency> dependencyMap = getDependencyMap(project);
        ExistingArtifactResolver resolve = new ExistingArtifactResolver(localRepository, artifactResolver, repositorySystem);

        for (GitDependency gitDependency : sageGetGitDependencies(gitDependencies)) {
            try {
                validate(gitDependency);
            } catch (GitDependencyConfigurationIsEmptyException e) {
                throw new MojoExecutionException(e.getMessage(), e);
            }
            Dependency dependency = getDependency(dependencyMap, gitDependency);
            if (!resolve.isInstalled(dependency))
                action.apply(project, gitDependency);
        }
    }

    private List<GitDependency> sageGetGitDependencies(List<GitDependency> gitDependencies) {
        return gitDependencies == null ? new ArrayList<GitDependency>() : gitDependencies;
    }

    private void createTempDir(MavenProject project) {
        TemporaryDirectory tmpDir = new TemporaryDirectory();
        tmpDir.create(project);
    }

    private Dependency getDependency(Map<String, Dependency> dependencyMap, GitDependency gitDependency) throws GitDependencyIsNotUsedInPomFile {
        DependencyTupple tupple = new DependencyTupple(gitDependency);
        Dependency dependency = dependencyMap.get(tupple.toString());
        if (dependency == null)
            throw new GitDependencyIsNotUsedInPomFile(gitDependency);
        return dependency;
    }

    private void validate(GitDependency gitDependency) throws GitDependencyConfigurationIsEmptyException {
        String groupId = gitDependency.getGroupId();
        String artifactId = gitDependency.getArtifactId();
        String repositoryLocation = gitDependency.getRepositoryLocation();

        if (StringHelpers.isNullOrEmpty(groupId)
                || StringHelpers.isNullOrEmpty(artifactId)
                || StringHelpers.isNullOrEmpty(repositoryLocation))
            throw new GitDependencyConfigurationIsEmptyException(gitDependency);
    }

    private Map<String, Dependency> getDependencyMap(MavenProject project) {
        Map<String, Dependency> result = new HashMap<String, Dependency>();
        for (Dependency dependency : project.getDependencies())
            result.put(new DependencyTupple(
                            dependency.getGroupId(),
                            dependency.getArtifactId()).toString(),
                    dependency);
        return result;
    }

    private static class DependencyTupple {
        private final String artifactId;

        private final String groupId;

        public DependencyTupple(GitDependency gitDependency) {
            this(gitDependency.getGroupId(),
                    gitDependency.getArtifactId());
        }

        public DependencyTupple(String groupId, String artifactId) {
            this.artifactId = artifactId;
            this.groupId = groupId;
        }

        @Override
        public String toString() {
            return String.format("%s:%s", groupId, artifactId);
        }
    }
}
