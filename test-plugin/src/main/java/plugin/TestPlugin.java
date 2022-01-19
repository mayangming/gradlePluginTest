package plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

class TestPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        System.out.println("打印的第四种方式-test");
    }
}