package com.yantonov;

import com.yantonov.domain.GitDependency;
import com.yantonov.infrastructure.TemporaryDirectory;
import com.yantonov.infrastructure.UninstalledDependencyActionInvoker;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.RepositorySystem;
import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.util.Collections;
import java.util.List;


@Mojo(name = "install", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
@Execute(goal = "clone")
public class InstallMojo extends AbstractMojo {

    @Parameter(property = "gitDependencies")
    private List<GitDependency> gitDependencies;

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Parameter(defaultValue = "${localRepository}", readonly = true, required = true)
    private ArtifactRepository localRepository;

    @Component()
    private ArtifactResolver artifactResolver;

    @Component
    private RepositorySystem repositorySystem;

    private void install(MavenProject project, GitDependency gitDependency) {
        getLog().info("Start gitdep:install goal");
        final TemporaryDirectory tmpDir = new TemporaryDirectory();
        final InvocationRequest request = new DefaultInvocationRequest();
        final Invoker invoker = new DefaultInvoker();

        File dependencyDirectory = tmpDir.getTemporaryDirectory(project, gitDependency);
        invoker.setWorkingDirectory(dependencyDirectory);
        request.setPomFile(new File("pom.xml"));
        request.setGoals(Collections.singletonList("install"));
        request.setShowErrors(true);
        request.setBaseDirectory(dependencyDirectory);

        try {
            getLog().info(String.format("Invoker executes for %s directory", dependencyDirectory.getPath()));
            final InvocationResult result = invoker.execute(request);
            if (result.getExitCode() != 0) {
                throw new IllegalStateException(
                        String.format("Cant install dependency [%s:%s]",
                                gitDependency.getGroupId(),
                                gitDependency.getArtifactId()));
            }
        } catch (MavenInvocationException ex) {
            getLog().debug(ex);
        }
    }

    public void execute() throws MojoExecutionException {
        UninstalledDependencyActionInvoker actionInvoker = new UninstalledDependencyActionInvoker();
        actionInvoker.invoke(localRepository,
                artifactResolver,
                repositorySystem,
                project,
                gitDependencies,
                (project, gitDependency) -> install(project, gitDependency));
    }
}
