package junit.extensions;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This class is used to access a method or field of an object no matter what
 * the access modifier of the method or field. The syntax for accessing fields
 * and methods is out of the ordinary because this class uses reflection to
 * peel away protection.
 * <p>
 * a.k.a. The "ObjectMolester"
 * <p>
 * Here is an example of using this to access a private member: <br>
 * <code>myObject<code> is an object of type <code>MyClass<code>.
 * <code>setName(String)</code> is a private method of <code>MyClass</code>.
 *
 * <pre>
 * PrivilegedAccessor.invokeMethod(myObject,
 *         &quot;setName(java.lang.String)&quot;, &quot;newName&quot;);
 * </pre>
 *
 * @author Charlie Hubbard (chubbard@iss.net)
 * @author Prashant Dhokte (pdhokte@iss.net)
 * @author Sebastian Dietrich (sebastian.dietrich@anecon.com)
 */
public final class PrivilegedAccessor {
    /**
     * Private constructor to make it impossible to instantiate this class
     */
    private PrivilegedAccessor() {
        assert false; //should never be called
    }

    /**
     * Gets the value of the named field and returns it as an object.
     * If instanceOrClass is a class then a static field is returned.
     *
     * @param instanceOrClass the instance or class to get the field from
     * @param fieldName the name of the field
     * @return an object representing the value of the field
     * @throws NoSuchFieldException if the field does not exist
     */
    public static Object getValue(final Object instanceOrClass,
            final String fieldName) throws NoSuchFieldException {
        Field field = getField(instanceOrClass, fieldName);
        field.setAccessible(true);
        try {
            return field.get(instanceOrClass);
        } catch (IllegalAccessException e) {
            assert false; // would mean that setAccessible(true) didn't work
        }
        return null;
    }

    /**
     * Sets the value of the named field.
     * If instanceOrClass is a class then a static field is returned.
     *
     * @param instanceOrClass the instance or class to set the field
     * @param fieldName the name of the field
     * @param value the new value of the field
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final Object value)
    throws IllegalAccessException, NoSuchFieldException {
        Field field = getField(instanceOrClass, fieldName);
        field.setAccessible(true);
        field.set(instanceOrClass, value);
    }

    /**
     * Return the named field from the given instance or class.
     * Returns a static field if instanceOrClass is a class.
     *
     * @param instanceOrClass the instance or class to get the field from
     * @param fieldName the name of the field to get
     * @return the field
     * @throws NoSuchFieldException if no such field can be found
     */
    private static Field getField(final Object instanceOrClass,
            final String fieldName)
    throws NoSuchFieldException {
        if (instanceOrClass == null) {
            throw new NoSuchFieldException("Invalid field : " + fieldName);
        }

        Class type = null;
        if (instanceOrClass instanceof Class) {
            type = (Class) instanceOrClass;
        } else {
            type = instanceOrClass.getClass();
        }

        try {
            return type.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return getField(type.getSuperclass(), fieldName);
        }
    }

    /**
     * Calls a method without parameters on the given object instance or class.
     * If the given instanceOrClass is a class then a static method is called.
     *
     * @param instanceOrClass the instance or class to call the method on
     * @param methodSignature the name of the method with brackets
     *        (e.g. "test (java.lang.String, com.company.project.MyObject)")
     * @return the return value of this method or null if void
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature)
    throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        if (methodSignature.indexOf('(') != methodSignature.indexOf(')') - 1) {
            throw new NoSuchMethodException("Method '" + methodSignature
                    + "' must have no parameters");
        }
        return getMethod(instanceOrClass, getMethodName(methodSignature), null)
            .invoke(instanceOrClass, null);
    }

    /**
     * Calls a method on the given object instance with the given argument.
     *
     * @param instanceOrClass the instance or class to invoke the method on
     * @param methodSignature the name of the method and the parameters
     *        (e.g. "test (java.lang.String, com.company.project.MyObject)")
     * @param arg the argument to pass to the method
     * @return the return value of this method or null if void
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object[])
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final Object arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return invokeMethod(instanceOrClass, methodSignature, new Object[] {arg});
    }

    /**
     * Calls a method on the given object instance with the given int argument.
     *
     * @param instanceOrClass the instance or class to invoke the method on
     * @param methodSignature the name of the method and the parameters
     *        (e.g. "myMethod(int)")
     * @param arg the integer argument to pass to the method
     * @return the return value of this method or null if void
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final int arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
         return getMethod(instanceOrClass, getMethodName(methodSignature),
                    new Class[] {Integer.TYPE}).invoke(instanceOrClass,
                            new Object[] {new Integer(arg)});
    }

    /**
     * Calls a method on the given object instance with the given float
     * argument.
     *
     * @param instanceOrClass the instance or class to invoke the method on
     * @param methodSignature the name of the method and the parameters
     *        (e.g. "myMethod(flaot)")
     * @param arg the float argument to pass to the method
     * @return the return value of this method or null if void
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final float arg)
    throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        return getMethod(instanceOrClass, getMethodName(methodSignature),
                new Class[] {Float.TYPE}).invoke(instanceOrClass,
                new Object[] {new Float(arg)});
    }

    /**
     * Calls a method on the given object instance with the given double
     * argument.
     *
     * @param instanceOrClass the instance or class to invoke the method on
     * @param methodSignature the name of the method and the parameters
     *        (e.g. "myMethod(double)")
     * @param arg the double argument to pass to the method
     * @return the return value of this method or null if void
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final double arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return getMethod(instanceOrClass, getMethodName(methodSignature),
                new Class[] {Double.TYPE}).invoke(instanceOrClass,
                new Object[] {new Double(arg)});
    }

    /**
     * Calls a method on the given object instance with the given short
     * argument.
     *
     * @param instanceOrClass the instance or class to invoke the method on
     * @param methodSignature the name of the method and the parameters
     *        (e.g. "myMethod(short)")
     * @param arg the short argument to pass to the method
     * @return the return value of this method or null if void
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final short arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return getMethod(instanceOrClass, getMethodName(methodSignature),
                new Class[] {Short.TYPE}).invoke(instanceOrClass,
                new Object[] {new Short(arg)});
    }

    /**
     * Calls a method on the given object instance with the given byte argument.
     *
     * @param instanceOrClass the instance or class to invoke the method on
     * @param methodSignature the name of the method and the parameters
     *        (e.g. "myMethod(byte)")
     * @param arg the byte argument to pass to the method
     * @return the return value of this method or null if void
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final byte arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return getMethod(instanceOrClass, getMethodName(methodSignature),
                new Class[] {Byte.TYPE}).invoke(instanceOrClass,
                new Object[] {new Byte(arg)});
    }

    /**
     * Calls a method on the given object instance with the given char argument.
     *
     * @param instanceOrClass the instance or class to invoke the method on
     * @param methodSignature the name of the method and the parameters
     *        (e.g. "myMethod(char)")
     * @param arg the char argument to pass to the method
     * @return the return value of this method or null if void
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final char arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return getMethod(instanceOrClass, getMethodName(methodSignature),
                new Class[] {Character.TYPE}).invoke(instanceOrClass,
                new Object[] {new Character(arg)});
    }

    /**
     * Calls a method on the given object instance with the given boolean
     * argument.
     *
     * @param instanceOrClass the instance or class to invoke the method on
     * @param methodSignature the name of the method and the parameters
     *        (e.g. "myMethod(boolean)")
     * @param arg the boolean argument to pass to the method
     * @return the return value of this method or null if void
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final boolean arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return getMethod(instanceOrClass, getMethodName(methodSignature),
                new Class[] {Boolean.TYPE}).invoke(instanceOrClass,
                new Object[] {new Boolean(arg)});
    }

    /**
     * Calls a method on the given object instance with the given long argument.
     *
     * @param instanceOrClass the instance or class to invoke the method on
     * @param methodSignature the name of the method and the parameters
     *        (e.g. "myMethod(long)")
     * @param arg the long argument to pass to the method
     * @return the return value of this method or null if void
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final long arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return getMethod(instanceOrClass, getMethodName(methodSignature),
                new Class[] {Long.TYPE}).invoke(instanceOrClass,
                new Object[] {new Long(arg)});
    }

    /**
     * Calls a method on the given object instance with the given arguments.
     * Arguments can be object types or representations for primitives.
     *
     * @param instanceOrClass the instance or class to invoke the method on
     * @param methodSignature the name of the method and the parameters <br>
     *        (e.g. "myMethod(java.lang.String, com.company.project.MyObject)")
     * @param args an array of objects to pass as arguments
     * @return the return value of this method or null if void
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @see PrivilegedAccessor#invokeMethod(Class,String,Object[])
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final Object[] args)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        if (args == null) {
            return invokeMethod(instanceOrClass, methodSignature,
                    new Object[] {null});
        }
        return getMethod(instanceOrClass, getMethodName(methodSignature),
                getParameterTypes(methodSignature, args))
                .invoke(instanceOrClass, args);
    }

    /**
     * Gets the name of a method
     *
     * @param methodSignature the signature of the method
     * @return the name of the method
     * @throws NoSuchMethodException
     */
    private static String getMethodName(final String methodSignature)
    throws NoSuchMethodException {
        if (methodSignature.indexOf('(') >= methodSignature.indexOf(')')) {
            throw new NoSuchMethodException("Method '" + methodSignature
                    + "' must have brackets");
        }

        String methodName = methodSignature.substring(0,
                methodSignature.indexOf('(')).trim();
        return methodName;
    }

    /**
     * Instantiates an object of the given class without parameters.
     * e.g. instantiate(String.class);
     *
     * @param fromClass the class to instantiate an object from
     * @return the newly created null
     * @throws IllegalArgumentException if the number of actual and formal
     *         parameters differ; if an unwrapping conversion for primitive
     *         arguments fails; or if, after possible unwrapping, a parameter
     *         value cannot be converted to the corresponding formal parameter
     *         type by a method invocation conversion.
     * @throws IllegalAccessException if this Constructor object enforces Java
     *         language access control and the underlying constructor is
     *         inaccessible.
     * @throws InvocationTargetException if the underlying constructor throws
     *         an exception.
     * @throws NoSuchMethodException if the constructor could not be found
     * @throws InstantiationException if the class that declares the underlying
     *         constructor represents an abstract class.
     *
     * @see PrivilegedAccessor#instantiate(Class,Object)
     */
    public static Object instantiate(final Class fromClass)
    throws IllegalArgumentException, InstantiationException,
    IllegalAccessException, InvocationTargetException,
    NoSuchMethodException {
        return instantiate(fromClass, null);
    }

    /**
     * Instantiates an object of the given class with the given argument. e.g.
     * instantiate(String.class, "string to copy");
     *
     * @param fromClass the class to instantiate an object from
     * @param argument the argument to pass to the constructor
     * @return an null of the given type
     * @throws IllegalArgumentException if the number of actual and formal
     *         parameters differ; if an unwrapping conversion for primitive
     *         arguments fails; or if, after possible unwrapping, a parameter
     *         value cannot be converted to the corresponding formal parameter
     *         type by a method invocation conversion.
     * @throws IllegalAccessException if this Constructor object enforces Java
     *         language access control and the underlying constructor is
     *         inaccessible.
     * @throws InvocationTargetException if the underlying constructor throws
     *         an exception.
     * @throws NoSuchMethodException if the constructor could not be found
     * @throws InstantiationException if the class that declares the underlying
     *         constructor represents an abstract class.
     *
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object[])
     */
    public static Object instantiate(final Class fromClass,
            final Object argument)
    throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        Object[] args = new Object[1];
        args[0] = argument;
        return instantiate(fromClass, args);
    }

    /**
     * Instantiates an object of the given class with the given arguments.
     *
     * @param fromClass the class to instantiate an object from
     * @param args the arguments to pass to the constructor
     * @return an null of the given type
     * @throws IllegalArgumentException if the number of actual and formal
     *         parameters differ; if an unwrapping conversion for primitive
     *         arguments fails; or if, after possible unwrapping, a parameter
     *         value cannot be converted to the corresponding formal parameter
     *         type by a method invocation conversion.
     * @throws IllegalAccessException if this Constructor object enforces Java
     *         language access control and the underlying constructor is
     *         inaccessible.
     * @throws InvocationTargetException if the underlying constructor throws
     *         an exception.
     * @throws NoSuchMethodException if the constructor could not be found
     * @throws InstantiationException if the class that declares the underlying
     *         constructor represents an abstract class.
     *
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
     */
    public static Object instantiate(final Class fromClass, final Object[] args)
            throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        return instantiate(fromClass, getParameterTypes(args), args);
    }

    /**
     * Instantiates an object of the given class with the given arguments and
     * the given argument types
     *
     * @param type the class to instantiate an object from
     * @param args the arguments to pass to the constructor
     * @param argumentTypes the types of the arguments of the constructor
     * @return an null of the given type
     * @throws IllegalArgumentException if the number of actual and formal
     *         parameters differ; if an unwrapping conversion for primitive
     *         arguments fails; or if, after possible unwrapping, a parameter
     *         value cannot be converted to the corresponding formal parameter
     *         type by a method invocation conversion.
     * @throws IllegalAccessException if this Constructor object enforces Java
     *         language access control and the underlying constructor is
     *         inaccessible.
     * @throws InvocationTargetException if the underlying constructor throws
     *         an exception.
     * @throws NoSuchMethodException if the constructor could not be found
     * @throws InstantiationException if the class that declares the underlying
     *         constructor represents an abstract class.
     *
     * @see PrivilegedAccessor#invokeMethod(Object,String,Object)
     */
    public static Object instantiate(final Class type,
            final Class[] argumentTypes, final Object[] args)
    throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        return getConstructor(type, argumentTypes).newInstance(args);
    }

    /**
     * Gets the types of the parameters
     *
     * @param parameters the parameters
     * @return the class-types of the arguments
     */
    private static Class[] getParameterTypes(final Object[] parameters) {
        if (parameters == null) {
            return null;
        }

        Class[] classTypes = new Class[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            classTypes[i] = parameters[i].getClass();
        }
        return classTypes;
    }

    /**
     * @param methodSignature the signature of the method
     * @param parameters the arguments of the method
     * @return the parameter types as class[]
     * @throws NoSuchMethodException if the method could not be found
     */
    private static Class[] getParameterTypes(final String methodSignature,
            final Object[] parameters)
    throws NoSuchMethodException {
        Class[] classTypes = new Class[parameters.length];
        String[] params = methodSignature.substring(
                methodSignature.indexOf('(') + 1, methodSignature.indexOf(')'))
                .split(", *");

        for (int i = 0; i < params.length; i++) {
            try {
                classTypes[i] = getClassForName(params[i]);
            } catch (ClassNotFoundException e) {
                throw new NoSuchMethodException("Method '" + methodSignature
                        + "'s parameter nr" + i + " (" + params[i]
                        + ") not found");
            }
            if (parameters[i] == null
                || classTypes[i].isAssignableFrom(parameters[i].getClass())) {
                continue;
            }
            throw new IllegalArgumentException("Method '" + methodSignature
                    + "'s parameter nr" + i + " (" + params[i]
                    + ") does not math argument type ("
                    + parameters.getClass().getName() + ")");
        }
        return classTypes;
    }

    /**
     * Gets the class with the given className
     *
     * @param className the name of the class to get
     * @return the class for the given className
     * @throws ClassNotFoundException if the class could not be found
     */
    private static Class getClassForName(final String className)
            throws ClassNotFoundException {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            if (className.equals("int")) {
                return Integer.TYPE;
            }
            if (className.equals("float")) {
                return Float.TYPE;
            }
            if (className.equals("double")) {
                return Double.TYPE;
            }
            if (className.equals("short")) {
                return Short.TYPE;
            }
            if (className.equals("long")) {
                return Long.TYPE;
            }
            if (className.equals("byte")) {
                return Byte.TYPE;
            }
            if (className.equals("char")) {
                return Character.TYPE;
            }
            if (className.equals("boolean")) {
                return Boolean.TYPE;
            }
            throw e;
        }
    }

    /**
     * gets the constructor for a given class with the given parameters
     *
     * @param type the class to instantiate
     * @param parameterTypes the types of the parameters
     * @return the constructor
     * @throws NoSuchMethodException if the method could not be found
     */
    private static Constructor getConstructor(final Class type,
            final Class[] parameterTypes)
            throws NoSuchMethodException {
        Constructor constructor = type.getDeclaredConstructor(parameterTypes);
        constructor.setAccessible(true);
        return constructor;
    }

    /**
     * Gets the method with the given name and parameters from the given
     * instance or class. If instanceOrClass is a class, then we get
     * a static method.
     *
     * @param instanceOrClass the instance or class to get the method of
     * @param methodName the name of the method
     * @param parameterTypes the parameter-types of the method to get
     * @return the method
     * @throws NoSuchMethodException if the method could not be found
     */
    private static Method getMethod(final Object instanceOrClass,
            final String methodName, final Class[] parameterTypes)
    throws NoSuchMethodException {
        Class type;

        if (instanceOrClass instanceof Class) {
            type = (Class) instanceOrClass;
        } else {
            type = instanceOrClass.getClass();
        }

        Method accessMethod = getMethod(type, methodName, parameterTypes);
        accessMethod.setAccessible(true);
        return accessMethod;
    }

    /**
     * Return the named method with a method signature matching classTypes
     * from the given class.
     *
     * @param type the class to get the method from
     * @param methodName the name of the method to get
     * @param parameterTypes the parameter-types of the method to get
     * @return the method
     * @throws NoSuchMethodException if the method could not be found
     */
    private static Method getMethod(final Class type, final String methodName,
            final Class[] parameterTypes) throws NoSuchMethodException {
        try {
            return type.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            if (type.getSuperclass() == null) {
                throw new NoSuchMethodException("Invalid method : "
                        + type.getName() + "." + methodName + "("
                        + getParameterTypesAsString(parameterTypes) + ")");
            }
            return getMethod(type.getSuperclass(), methodName, parameterTypes);
        }
    }

    /**
     * Gets the parameter types as a string
     *
     * @param classTypes the types to get as names
     * @return the parameter types as a string
     */
    private static String getParameterTypesAsString(final Class[] classTypes) {
        if (classTypes == null) {
            return "";
        }

        String parameterTypes = "";
        for (int x = 0; x < classTypes.length; x++) {
            if (classTypes[x] == null) {
                parameterTypes += "null";
            } else {
                parameterTypes += classTypes[x].getName();
            }
            parameterTypes += ", ";
        }
        parameterTypes = parameterTypes.substring(0,
                parameterTypes.length() - 2);
        return parameterTypes;
    }
}