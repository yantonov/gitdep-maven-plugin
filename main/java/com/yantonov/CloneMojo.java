package com.yantonov;

import com.yantonov.domain.GitDependency;
import com.yantonov.infrastructure.TemporaryDirectory;
import com.yantonov.infrastructure.UninstalledDependencyActionInvoker;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.RepositorySystem;
import java.util.List;

@Mojo(name = "clone", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
@Execute(goal = "clean")
public class CloneMojo extends AbstractMojo {

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

    public void execute() throws MojoExecutionException {
        UninstalledDependencyActionInvoker actionInvoker = new UninstalledDependencyActionInvoker();
        actionInvoker.invoke(localRepository,
                artifactResolver,
                repositorySystem,
                project,
                gitDependencies,
                (project, gitDependency) -> cloneGitRepo(gitDependency));
    }

    private void cloneGitRepo(GitDependency gitDependency) throws MojoExecutionException {
        Log log = getLog();
        log.info(String.format("Start cloning git dependency %s:%s from %s.",
                gitDependency.getGroupId(),
                gitDependency.getArtifactId(),
                gitDependency.getRepositoryLocation()));

        try {
            TemporaryDirectory tmpDir = new TemporaryDirectory();

            tmpDir.createDependencyDirectory(project, gitDependency);
            tmpDir.remove(project, gitDependency);
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "git",
                    "clone",
                    "-b",
                    gitDependency.getBranch(),
                    gitDependency.getRepositoryLocation(),
                    tmpDir.getTemporaryDirectory(project, gitDependency).getName());
            processBuilder.redirectErrorStream();
            processBuilder.redirectOutput();
            processBuilder.directory(tmpDir.getTemporaryDirectory(project));
            Process gitProcess = processBuilder.start();
            gitProcess.waitFor();
        } catch (Exception err) {
            err.printStackTrace();
            throw new MojoExecutionException(err.getMessage(), err);
        }
        log.info(String.format("Clone repository %s is successfully ended.",
                gitDependency.getGroupId(),
                gitDependency.getArtifactId(),
                gitDependency.getRepositoryLocation()));

    }


}
