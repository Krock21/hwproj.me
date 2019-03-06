package me.hwproj.gaev;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import me.hwproj.gaev.testClasses.ClassWithOneClassDependency;
import me.hwproj.gaev.testClasses.ClassWithOneInterfaceDependency;
import me.hwproj.gaev.testClasses.ClassWithoutDependencies;
import me.hwproj.gaev.testClasses.InterfaceImpl;

import java.util.Collections;

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
}
