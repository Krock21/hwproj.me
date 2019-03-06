package me.hwproj.gaev;

import static org.junit.jupiter.api.Assertions.*;

import me.hwproj.gaev.testClasses.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class InjectorTest {

    @Test
    public void injectorShouldInitializeClassWithoutDependencies()
            throws Exception {
        Object object = Injector.initialize("me.hwproj.gaev.testClasses.ClassWithoutDependencies", Collections.emptyList());
        assertTrue(object instanceof ClassWithoutDependencies);
    }

    @Test
    public void injectorShouldInitializeClassWithOneClassDependency()
            throws Exception {
        Object object = Injector.initialize(
                "me.hwproj.gaev.testClasses.ClassWithOneClassDependency",
                Collections.singletonList("me.hwproj.gaev.testClasses.ClassWithoutDependencies")
        );
        assertTrue(object instanceof ClassWithOneClassDependency);
        ClassWithOneClassDependency instance = (ClassWithOneClassDependency) object;
        assertTrue(instance.dependency != null);
    }

    @Test
    public void injectorShouldInitializeClassWithOneInterfaceDependency()
            throws Exception {
        Object object = Injector.initialize(
                "me.hwproj.gaev.testClasses.ClassWithOneInterfaceDependency",
                Collections.singletonList("me.hwproj.gaev.testClasses.InterfaceImpl")
        );
        assertTrue(object instanceof ClassWithOneInterfaceDependency);
        ClassWithOneInterfaceDependency instance = (ClassWithOneInterfaceDependency) object;
        assertTrue(instance.dependency instanceof InterfaceImpl);
    }

    @Test
    public void difficultTest()
            throws Exception {
        try {
            Object object = Injector.initialize(
                    "me.hwproj.gaev.testClasses.DifficultClass",
                    Stream.of("me.hwproj.gaev.testClasses.InterfaceImpl", "me.hwproj.gaev.testClasses.ClassWithoutDependencies",
                            "me.hwproj.gaev.testClasses.ClassWithOneClassDependency", "me.hwproj.gaev.testClasses.ClassWithOneInterfaceDependency").collect(Collectors.toList())
            );
        } catch (AmbiguousImplementationException e) {
            // it's ok
        }
    }
}
