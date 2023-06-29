package org.example.legacy.app;

class A {
    int n = 30;

    void method1() {
        StringBuilder sb = new StringBuilder();
        String op = "+";
        sb.append("A" + op + "B");
        sb.append(1 + op + 2);
    }
}