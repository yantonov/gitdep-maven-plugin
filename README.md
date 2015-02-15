### maven-gitdep-plugin

[Apache maven][1] plugin to get maven dependencies from git repository.

[1]: http://maven.apache.org

### usage

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.yantonov</groupId>
            <artifactId>gitdep-maven-plugin</artifactId>
            <version>1.0</version>
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
