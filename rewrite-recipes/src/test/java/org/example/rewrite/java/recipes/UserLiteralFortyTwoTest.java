package org.example.rewrite.java.recipes;

import org.example.rewrite.java.recipes.UserLiteralFortyTwo;
import org.junit.jupiter.api.Test;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.java.Assertions.java;

class UserLiteralFortyTwoTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipe(new UserLiteralFortyTwo());
    }

    @Test
    void useFortyTwo() {
        //language=java
        rewriteRun(
                java(
                        """
                                class Test {
                                    int n = 30;
                                }
                                """,
                        """
                                class Test {
                                    int n = 42;
                                }
                                """)
        );
    }
}