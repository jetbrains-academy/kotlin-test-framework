package org.jetbrains.academy.test.system.core.testData.java;

import java.util.List;

public class JavaClass {

    public static final int PUBLIC_CONSTANT = 1;
    private static final int PRIVATE_CONSTANT = 2;

    public static int publicStaticVar = 3;
    private static int privateStaticVar = 4;

    public String publicVar = "publicVar";
    private String privateVar = "privateVar";

    public CustomJavaClass customClass = null;

    public String primaryConstructorVar;

    public JavaClass(String primaryVar) {
        this.primaryConstructorVar = primaryVar;
    }

    public void publicMethod() {
        System.out.println("Public instance method called!");
    }

    private void privateMethod() {
        System.out.println("Private instance method called!");
    }

    public double calculateSum(double a, double b) {
        return a + b;
    }

    private String getString(String string) {
        return string;
    }

    public List<String> processList(List<String> list) {
        for (String item : list) {
            System.out.println("Processing item: " + item);
        }
        return List.of("item1", "item2", "item3");
    }
}
