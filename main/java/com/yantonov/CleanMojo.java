package com.yantonov;

import com.yantonov.infrastructure.TemporaryDirectory;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;

@Mojo(name = "clean",defaultPhase = LifecyclePhase.CLEAN)
public class CleanMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    public void execute() throws MojoExecutionException {
        try {
            TemporaryDirectory tmpDir = new TemporaryDirectory();
            tmpDir.remove(project);
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage());
        }
    }
}
