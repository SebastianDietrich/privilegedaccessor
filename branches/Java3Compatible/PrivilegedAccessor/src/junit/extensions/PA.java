package junit.extensions;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import junit.extensions.PrivilegedAccessor;

/**
 * This is the short named interface to the PrivilegedAccessor class.
 * Use this if the name "PrivilegedAccessor" is too long for your application
 *
 * @author Sebastian Dietrich (sebastian.dietrich@anecon.com)
 */
public final class PA {
    /**
     * Private constructor to make it impossible to instantiate this class.
     */
    private PA() {
        throw new Error("Assertion failed"); //should never be called
    }

    /**
     * {@inheritDoc}
     */
    public static Collection getFieldNames(final Object instanceOrClass) {
        return PrivilegedAccessor.getFieldNames(instanceOrClass);
    }
    
    /**
     * {@inheritDoc}
     */
    public static Collection getMethodSignatures(final Object instanceOrClass) {
        return PrivilegedAccessor.getMethodSignatures(instanceOrClass);
    }
    
    /**
     * {@inheritDoc}
     */
    public static Object getValue(final Object instanceOrClass,
            final String fieldName) throws NoSuchFieldException {
        return PrivilegedAccessor.getValue(instanceOrClass, fieldName);
    }

    /**
     * {@inheritDoc}
     */
    public static Object instantiate(final Class fromClass)
    throws IllegalArgumentException, InstantiationException,
    IllegalAccessException, InvocationTargetException,
    NoSuchMethodException {
        return PrivilegedAccessor.instantiate(fromClass, null);
    }

    /**
     * {@inheritDoc}
     */
    public static Object instantiate(final Class fromClass,
            final Class[] argumentTypes, final Object[] args)
    throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        return PrivilegedAccessor.instantiate(fromClass, argumentTypes, args);
    }

    /**
     * {@inheritDoc}
     */
    public static Object instantiate(final Class fromClass,
            final Object argument)
    throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        return PrivilegedAccessor.instantiate(fromClass, argument);
    }

    /**
     * {@inheritDoc}
     */
    public static Object instantiate(final Class fromClass, final Object[] args)
    throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        return PrivilegedAccessor.instantiate(fromClass, args);
    }

    /**
     * {@inheritDoc}
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature)
    throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature);
    }

    /**
     * {@inheritDoc}
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final boolean arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, arg);
    }

    /**
     * {@inheritDoc}
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final byte arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, arg);
    }

    /**
     * {@inheritDoc}
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final char arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, arg);
    }

    /**
     * {@inheritDoc}
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final double arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, arg);
    }

    /**
     * {@inheritDoc}
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final float arg)
    throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, arg);
    }

    /**
     * {@inheritDoc}
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final int arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, arg);
    }

    /**
     * {@inheritDoc}
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final long arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, arg);
    }

    /**
     * {@inheritDoc}
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final Object arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, arg);
    }

    /**
     * {@inheritDoc}
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final Object[] arguments)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, arguments);
    }

    /**
     * {@inheritDoc}
     */
    public static Object invokeMethod(final Object instanceOrClass,
            final String methodSignature, final short arg)
    throws IllegalArgumentException, IllegalAccessException,
    InvocationTargetException, NoSuchMethodException {
        return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, arg);
    }

    /**
     * {@inheritDoc}
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final boolean value)
    throws NoSuchFieldException {
        PrivilegedAccessor.setValue(instanceOrClass, fieldName, value);
    }

    /**
     * {@inheritDoc}
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final byte value)
    throws NoSuchFieldException {
        PrivilegedAccessor.setValue(instanceOrClass, fieldName, value);
    }

    /**
     * {@inheritDoc}
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final char value)
    throws NoSuchFieldException {
        PrivilegedAccessor.setValue(instanceOrClass, fieldName, value);
    }

    /**
     * {@inheritDoc}
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final double value)
    throws NoSuchFieldException {
        PrivilegedAccessor.setValue(instanceOrClass, fieldName, value);
    }

    /**
     * {@inheritDoc}
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final float value)
    throws NoSuchFieldException {
        PrivilegedAccessor.setValue(instanceOrClass, fieldName, value);
    }

    /**
     * {@inheritDoc}
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final int value)
    throws NoSuchFieldException {
        PrivilegedAccessor.setValue(instanceOrClass, fieldName, value);
    }

    /**
     * {@inheritDoc}
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final long value)
    throws NoSuchFieldException {
        PrivilegedAccessor.setValue(instanceOrClass, fieldName, value);
    }

    /**
     * {@inheritDoc}
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final Object value)
    throws NoSuchFieldException {
        PrivilegedAccessor.setValue(instanceOrClass, fieldName, value);
    }

    /**
     * {@inheritDoc}
     */
    public static void setValue(final Object instanceOrClass,
            final String fieldName, final short value)
    throws NoSuchFieldException {
        PrivilegedAccessor.setValue(instanceOrClass, fieldName, value);
    }
}