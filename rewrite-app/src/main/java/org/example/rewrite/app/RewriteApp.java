package org.example.rewrite.app;

import org.example.rewrite.java.recipes.UserLiteralFortyTwo;
import org.openrewrite.*;
import org.openrewrite.config.Environment;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.openrewrite.java.JavaParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

public class RewriteApp {
    private static  final Logger LOGGER = LoggerFactory.getLogger(RewriteApp.class);

    public static void main(String[] args) throws IOException {
        // determine your project directory and provide a list of
        // paths to jars that represent the project's classpath
        Path projectDir = Paths.get("legacy-app");
        List<Path> classpath = emptyList();


        // put any rewrite recipe jars on this main method's runtime classpath
        // and either construct the recipe directly or via an Environment
        Environment environment = Environment.builder().scanRuntimeClasspath().build();
        Recipe recipe = environment.activateRecipes("org.openrewrite.staticanalysis.CommonStaticAnalysis", UserLiteralFortyTwo.class.getName());

        // create a JavaParser instance with your classpath
        JavaParser javaParser = JavaParser.fromJavaVersion()
                .classpath(classpath)
                .build();

        // walk the directory structure where your Java sources are located
        // and create a list of them
        try (Stream<Path> sourcePathStream = Files.find(projectDir, 999, (p, bfa) ->
                bfa.isRegularFile() && p.getFileName().toString().endsWith(".java"))) {

            List<Path> sourcePaths = sourcePathStream.collect(Collectors.toList());
            ExecutionContext ctx = new InMemoryExecutionContext(Throwable::printStackTrace);

            // parser the source files into LSTs
            List<SourceFile> cus = javaParser.parse(sourcePaths, projectDir, ctx).collect(Collectors.toList());

            // collect results
            List<Result> results = recipe.run(new InMemoryLargeSourceSet(cus), ctx).getChangeset().getAllResults();

            for (Result result : results) {
                // print diffs to the console
                LOGGER.info(result.diff(projectDir));

                // or overwrite the file on disk with changes.
                // Files.writeString(result.getAfter().getSourcePath(),
                //        result.getAfter().printAll());
            }
        }
    }
}