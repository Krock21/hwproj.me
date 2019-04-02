package me.hwproj.gaev;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;


public class Injector {

    private static Map<String, Object> objects = new HashMap<>();

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    private static void addClass(String rootClassName, List<String> implementationClassNames) throws Exception {
        Class.forName(rootClassName);
        Constructor<?> constructor = Class.forName(rootClassName).getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        List<Object> parameters = new ArrayList<>();
        int currentnum = 0;
        for (var parameterType : constructor.getParameterTypes()) {
            Class<?> dependency = parameterType;
            String implementationClassName = null;
            for (var potentialImplementationClassName : implementationClassNames) {
                if (dependency.isAssignableFrom(Class.forName(potentialImplementationClassName))) {
                    if (objects.containsKey(potentialImplementationClassName) &&
                            objects.get(potentialImplementationClassName) == null) {
                        throw new InjectionCycleException();
                    }
                    if (implementationClassName == null) {
                        implementationClassName = potentialImplementationClassName;
                    } else {
                        throw new AmbiguousImplementationException();
                    }
                }
            }
            Object instance = null;
            for (var entry : objects.entrySet()) {
                if (dependency.isInstance(entry.getValue())) {
                    if (instance != null) {
                        throw new AmbiguousImplementationException();
                    }
                    instance = entry.getValue();
                }
            }
            if (instance == null) {
                objects.put(rootClassName, null);
                addClass(implementationClassName, implementationClassNames);
                instance = objects.get(implementationClassName);
            }
            parameters.add(instance);
        }
        objects.put(rootClassName, constructor.newInstance(parameters.toArray()));
    }

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {
        objects = new HashMap<>();
        objects.put("0", Injector.class.getClassLoader());
        List<String> implementationClasses = new ArrayList<>(implementationClassNames);
        implementationClasses.add(rootClassName);
        addClass(rootClassName, implementationClasses);
        return objects.get(rootClassName);
    }
}