package no.dervis;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class StartAppPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.task("startApp")
          .doLast(task -> System.out.println("A future plugin to start and stop background processes."));
    }
}
