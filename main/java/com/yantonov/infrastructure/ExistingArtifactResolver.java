package com.yantonov.infrastructure;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.model.Dependency;
import org.apache.maven.repository.RepositorySystem;

import java.util.ArrayList;

public class ExistingArtifactResolver {
    private final ArtifactRepository local;
    private final ArtifactResolver artifactResolver;
    private RepositorySystem repositorySystem;

    public ExistingArtifactResolver(ArtifactRepository local,
                                    ArtifactResolver artifactResolver,
                                    RepositorySystem repositorySystem) {
        this.local = local;
        this.artifactResolver = artifactResolver;
        this.repositorySystem = repositorySystem;
    }

    public boolean isInstalled(Dependency dependency) {
        final Artifact artifact = repositorySystem.createArtifactWithClassifier(
                dependency.getGroupId(),
                dependency.getArtifactId(),
                dependency.getVersion(),
                dependency.getType(),
                dependency.getClassifier());
        try {
            artifactResolver.resolve(artifact, new ArrayList(), local);
        } catch (ArtifactResolutionException ex) {
            throw new IllegalStateException(String.format(
                    "Failed to find artifact [%s:%s:%s] in local repository.",
                    dependency.getGroupId(),
                    dependency.getArtifactId(),
                    dependency.getVersion()
            ), ex);
        } catch (ArtifactNotFoundException e) {
            return false;
        }

        return true;
    }
}
