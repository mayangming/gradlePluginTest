package plugin

import org.gradle.api.Plugin;
import org.gradle.api.Project;

class ThreePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
//        println("this is a buildSrc plugin...Groovy")
        println "打印的第一种方式"
        println '打印的第二种方式'
        println("打印的第三种方式")
        System.out.println("打印的第四种方式")
    }
}