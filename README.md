### maven-gitdep-plugin

[Apache maven][1] plugin to get maven dependencies from git repository.

[1]: http://maven.apache.org

### description

Sometimes maven artifacts are not published to maven central (or other public repositories), but can be accessed using scm/vcs (git in particularly).  
Thats why you need to install maven artifacts from remote git repositories and you want to do it with minimal efforts.

This plugin iterates throught git dependency list, check if each maven artifact is already installed, if not - clone given branch from corresponding remote repository and execute mvn install for retrieved artifact.

### usage

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.yantonov</groupId>
            <artifactId>gitdep-maven-plugin</artifactId>
            <version>0.0.1</version>
            <configuration>
                <gitDependencies>
                    <gitDependency>
                        <groupId>**groupId**</groupId>
                        <artifactId>**artifactId**</artifactId>
                        <repositoryLocation>**gitRepository**</repositoryLocation>
                        <branch>master</branch>
                    </gitDependency>
                </gitDependencies>
            </configuration>
            <executions>
                <execution>
                    <id>get-git-dependencies</id>
                    <goals>
                        <goal>exec</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```
