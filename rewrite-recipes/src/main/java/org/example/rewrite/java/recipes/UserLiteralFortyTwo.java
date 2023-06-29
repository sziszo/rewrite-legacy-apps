package org.example.rewrite.java.recipes;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;

public class UserLiteralFortyTwo extends Recipe {

    @Override
    public @NotNull String getDisplayName() {
        return "Always use 42";
    }

    @Override
    public @NotNull String getDescription() {
        return "Because 42 is the answer to life, the universe..";
    }

    @Override
    public @NotNull TreeVisitor<?, ExecutionContext> getVisitor() {
        return new JavaIsoVisitor<ExecutionContext>() {
            @Override
            public J.@NotNull Literal visitLiteral(J.@NotNull Literal literal, @NotNull ExecutionContext ctx) {
                if (literal.getValue() instanceof Integer && ((Integer) literal.getValue()) != 42) {
                    return literal.withValue(42).withValueSource("42");
                }
                return super.visitLiteral(literal, ctx);
            }
        };
    }
}
