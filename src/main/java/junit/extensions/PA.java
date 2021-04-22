/*
 * Copyright Sebastian Dietrich (Sebastian.Dietrich@e-movimento.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package junit.extensions;

import java.util.Arrays;
import java.util.Collection;

/**
 * This class is used to access a method or field of an object no matter what the access modifier of the method or field. The syntax for
 * accessing fields and methods is out of the ordinary because this class uses reflection to peel away protection.
 * <p>
 * Here is an example of using this to access a private member: <br>
 * Given the following class <code>MyClass</code>: <br>
 * </p>
 * 
 * <pre>
 * public class MyClass {
 *   private String name; // private attribute
 *
 *   // private constructor
 *   private MyClass() {
 *     super();
 *   }
 *
 *   // private method
 *   private void setName(String newName) {
 *     this.name = newName;
 *   }
 * }
 * </pre>
 * <p>
 * We now want to access the class: <br>
 * 
 * <pre>
 * MyClass myObj = PA.instantiate(MyClass.class);
 * PA.invokeMethod(myObj, &quot;setName(java.lang.String)&quot;, &quot;myNewName&quot;);
 * String name = PA.getValue(myObj, &quot;name&quot;);
 * </pre>
 *
 * @author Sebastian Dietrich (sebastian.dietrich@e-movimento.com)
 */
public class PA<T> {
  private final T instanceOrClass;

  /**
   * Private constructor to make it impossible to instantiate this class from outside of PA.
   *
   * @param instanceOrClass the instanceOrClass privileged access is provided upon
   */
  private PA(T instanceOrClass) {
    this.instanceOrClass = instanceOrClass;
  }

  /**
   * Returns a string representation of the given object. The string has the following format: {@code <classname> {<attributes and values>}}
   * whereas {@code <attributes and values>} is a comma separated list with {@code <attributeName>=<attributeValue>}.
   * {@code <attributes and values>} includes all attributes of the objects class followed by the attributes of its superclass (if any) and
   * so on.
   *
   * @param instanceOrClass the object or class to get a string representation of
   * @return a string representation of the given object
   * @see PrivilegedAccessor#toString(Object)
   * @deprecated use org.apache.commons.lang3.builder.ToStringBuilder instead
   */
  @Deprecated
  @SuppressWarnings("deprecation")
  public static String toString(final Object instanceOrClass) {
    return PrivilegedAccessor.toString(instanceOrClass);
  }

  /**
   * Gets the name of all fields (public, private, protected, default) of the given instance or class. This includes as well all fields
   * (public, private, protected, default) of all its super classes.
   *
   * @param instanceOrClass the instance or class to get the fields of
   * @return the collection of field names of the given instance or class
   * @see PrivilegedAccessor#getFieldNames(Object)
   */
  @SuppressWarnings("deprecation")
  public static Collection<String> getFieldNames(final Object instanceOrClass) {
    return PrivilegedAccessor.getFieldNames(instanceOrClass);
  }

  /**
   * Gets the type of the field with the given fieldName in the given instance or class. If not found in the given instance or class checks
   * as well its super classes.
   *
   * @param instanceOrClass the instance or class to get the field type of
   * @param fieldName the name of the field to get the type of
   * @return the collection of field names of the given instance or class
   * @see PrivilegedAccessor#getFieldType(Object, String)
   */
  @SuppressWarnings("deprecation")
  public static Class<?> getFieldType(final Object instanceOrClass, final String fieldName) {
    try {
      return PrivilegedAccessor.getFieldType(instanceOrClass, fieldName);
    } catch (NoSuchFieldException e) {
      throw new IllegalArgumentException("Can't get type of " + fieldName + " from " + instanceOrClass, e);
    }
  }

  /**
   * Gets the signatures (including return types) of all methods (public, private, protected, default) of the given instance or class. This
   * includes as well all methods (public, private, protected, default) of all its super classes. This does not include constructors.
   *
   * @param instanceOrClass the instance or class to get the method signatures of
   * @return the collection of method signatures of the given instance or class
   * @see PrivilegedAccessor#getMethodSignatures(Object)
   */
  @SuppressWarnings("deprecation")
  public static Collection<String> getMethodSignatures(final Object instanceOrClass) {
    return PrivilegedAccessor.getMethodSignatures(instanceOrClass);
  }

  /**
   * Gets the value of the named field and returns it as an object. If instanceOrClass is a class then a static field is returned.
   *
   * @param instanceOrClass the instance or class to get the field from
   * @param fieldName the name of the field
   * @return an object representing the value of the field
   * @throws IllegalArgumentException if the field does not exist
   * @see PrivilegedAccessor#getValue(Object, String)
   */
  @SuppressWarnings("deprecation")
  public static Object getValue(final Object instanceOrClass, final String fieldName) {
    try {
      return PrivilegedAccessor.getValue(instanceOrClass, fieldName);
    } catch (Exception e) {
      throw new IllegalArgumentException("Can't get value of " + fieldName + " from " + instanceOrClass, e);
    }
  }

  /**
   * Instantiates an object of the given class with the given arguments and the given argument types. If you want to instantiate a member
   * class, you must provide the object it is a member of as first argument.
   *
   * @param fromClass the class to instantiate an object from
   * @param arguments the arguments to pass to the constructor
   * @param argumentTypes the fully qualified types of the arguments of the constructor
   * @param <T> the type of the to be instantiated object
   * @return an object of the given type
   * @throws IllegalArgumentException if the class can't be instantiated. This could be the case if the number of actual and formal
   *         parameters differ; if an unwrapping conversion for primitive arguments fails; if, after possible unwrapping, a parameter value
   *         cannot be converted to the corresponding formal parameter type by a method invocation conversion; if this Constructor object
   *         enforces Java language access control and the underlying constructor is inaccessible; if the underlying constructor throws an
   *         exception; if the constructor could not be found; or if the class that declares the underlying constructor represents an
   *         abstract class.
   * @see PrivilegedAccessor#instantiate(Class, Class[], Object[])
   */
  @SuppressWarnings("deprecation")
  public static <T> T instantiate(final Class<? extends T> fromClass, final Class<?>[] argumentTypes, final Object... arguments) {
    try {
      return PrivilegedAccessor.instantiate(fromClass, argumentTypes, correctVarargs(arguments));
    } catch (Exception e) {
      throw new IllegalArgumentException("Can't instantiate class " + fromClass + " with arguments " + Arrays.toString(arguments), e);
    }
  }

  /**
   * Instantiates an object of the given class with the given arguments. If you want to instantiate a member class, you must provide the
   * object it is a member of as first argument (like Class.forName("mypackage.MyClass$MyInnerClass")).
   *
   * @param fromClass the class to instantiate an object from
   * @param arguments the arguments to pass to the constructor
   * @param <T> the type of the to be instantiated object
   * @return an object of the given type
   * @throws IllegalArgumentException if the class can't be instantiated. This could be the case if the number of actual and formal
   *         parameters differ; if an unwrapping conversion for primitive arguments fails; or if, after possible unwrapping, a parameter
   *         value cannot be converted to the corresponding formal parameter type by a method invocation conversion; if this Constructor
   *         object enforces Java language access control and the underlying constructor is inaccessible; if the underlying constructor
   *         throws an exception; if the constructor could not be found; or if the class that declares the underlying constructor represents
   *         an abstract class.
   * @see PrivilegedAccessor#instantiate(Class, Object[])
   */
  @SuppressWarnings("deprecation")
  public static <T> T instantiate(final Class<? extends T> fromClass, final Object... arguments) {
    try {
      return PrivilegedAccessor.instantiate(fromClass, correctVarargs(arguments));
    } catch (Exception e) {
      throw new IllegalArgumentException("Can't instantiate class " + fromClass + " with arguments " + Arrays.toString(arguments), e);
    }
  }

  /**
   * Calls a method on the given object instance with the given arguments. Arguments can be object types or representations for primitives.
   *
   * @param instanceOrClass the instance or class to invoke the method on
   * @param methodSignature the name of the method and the parameters <br>
   *        (e.g. "myMethod(java.lang.String, com.company.project.MyObject)")
   * @param arguments an array of objects to pass as arguments
   * @return the return value of this method or null if void
   * @throws RuntimeException any runtime exception the invoked method has thrown
   * @throws IllegalArgumentException if the method could not be invoked or the method threw a non-runtime exception or error. This could be
   *         the case if the method is inaccessible; if the underlying method throws an exception; if no method with the given
   *         <code>methodSignature</code> could be found; or if an argument couldn't be converted to match the expected type
   * @see PrivilegedAccessor#invokeMethod(Object, String, Object[])
   */
  @SuppressWarnings("deprecation")
  public static Object invokeMethod(final Object instanceOrClass, final String methodSignature, final Object... arguments) {
    try {
      return PrivilegedAccessor.invokeMethod(instanceOrClass, methodSignature, correctVarargs(arguments));
    } catch (IllegalAccessException e) {
      throw new IllegalArgumentException(
        "Can't access method " + methodSignature + " of " + instanceOrClass + " with arguments " + Arrays.toString(arguments),
        e);
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException("Can't find method " + methodSignature + " in " + instanceOrClass, e);
    } catch (RuntimeException e) {
      throw e;
    } catch (Throwable e) {
      throw new IllegalArgumentException(
        "Invoking method " + methodSignature + " on " + instanceOrClass + " with arguments " + Arrays.toString(arguments)
            + " threw the non-runtime exception " + e.getClass().getName(),
        e);
    }
  }

  /**
   * Corrects varargs to their initial form. If you call a method with an object-array as last argument the Java varargs mechanism converts
   * this array in single arguments. This method returns an object array if the arguments are all of the same type.
   *
   * @param arguments the possibly converted arguments of a vararg method
   * @return arguments possibly converted
   */
  private static Object[] correctVarargs(final Object... arguments) {
    if ((arguments == null) || changedByVararg(arguments)) return new Object[] {arguments};
    return arguments;
  }

  /**
   * Tests if the arguments were changed by vararg. Arguments are changed by vararg if they are of a non primitive array type. E.g.
   * arguments[] = Object[String[]] is converted to String[] while e.g. arguments[] = Object[int[]] is not converted and stays Object[int[]]
   * <p/>
   * Unfortunately we can't detect the difference for arg = Object[primitive] since arguments[] = Object[Object[primitive]] which is
   * converted to Object[primitive] and arguments[] = Object[primitive] which stays Object[primitive]
   * <p/>
   * and we can't detect the difference for arg = Object[non primitive] since arguments[] = Object[Object[non primitive]] is converted to
   * Object[non primitive] and arguments[] = Object[non primitive] stays Object[non primitive]
   *
   * @param parameters the parameters
   * @return true if parameters were changes by varargs, false otherwise
   */
  private static boolean changedByVararg(final Object[] parameters) {
    if ((parameters.length == 0) || (parameters[0] == null)) return false;
    return parameters.getClass() != Object[].class;
  }

  /**
   * Sets the value of the named field. If fieldName denotes a static field, provide a class, otherwise provide an instance. If the
   * fieldName denotes a final field, this method tries to set it nevertheless (by temporarily removing the modifier).<br/>
   * <br/>
   * Example:<br/>
   * <br/>
   * <code>
   * String myString = "Test"; <br/>
   * <br/>
   * //setting the private field value<br/>
   * PrivilegedAccessor.setValue(myString, "value", new char[] {'T', 'e', 's', 't'});<br/>
   * <br/>
   * //setting the static final field serialVersionUID - MIGHT FAIL<br/>
   * PrivilegedAccessor.setValue(myString.getClass(), "serialVersionUID", 1);<br/>
   * <br/>
   * </code>
   *
   * @param instanceOrClass the instance or class to set the field
   * @param fieldName the name of the field
   * @param value the new value of the field
   * @param <T> the type of the object to set the value
   * @return this, so that calls to this method can be chained
   * @throws IllegalArgumentException if the value could not be set. This could be the case if no field with the given
   *         <code>fieldName</code> can be found; or if the field was final
   * @see junit.extensions.PrivilegedAccessor#setValue(Object, String, Object)
   */
  @SuppressWarnings("deprecation")
  public static <T> PA<T> setValue(final T instanceOrClass, final String fieldName, final Object value) {
    try {
      PrivilegedAccessor.setValue(instanceOrClass, fieldName, value);
    } catch (Exception e) {
      throw new IllegalArgumentException("Can't set value " + value + " at " + fieldName + " in " + instanceOrClass, e);
    }
    return new PA<>(instanceOrClass);
  }

  /**
   * Gets the value of the named field and returns it as an object.
   *
   * @param fieldName the name of the field
   * @return an object representing the value of the field
   * @throws IllegalArgumentException if the field does not exist
   * @see PA#getValue(Object, String)
   */
  public Object getValue(final String fieldName) {
    return PA.getValue(instanceOrClass, fieldName);
  }

  /**
   * Calls a method with the given arguments. Arguments can be object types or representations for primitives.
   *
   * @param methodSignature the name of the method and the parameters <br>
   *        (e.g. "myMethod(java.lang.String, com.company.project.MyObject)")
   * @param arguments an array of objects to pass as arguments
   * @return the return value of this method or null if void
   * @throws RuntimeException any runtime exception the invoked method has thrown
   * @throws IllegalArgumentException if the method could not be invoked or threw a non-runtime exception or error. This could be the case
   *         if the method is inaccessible; if the underlying method throws an exception; if no method with the given
   *         <code>methodSignature</code> could be found; or if an argument couldn't be converted to match the expected type
   * @see PA#invokeMethod(Object, String, Object...)
   */
  public Object invokeMethod(final String methodSignature, final Object... arguments) {
    return PA.invokeMethod(instanceOrClass, methodSignature, arguments);
  }

  /**
   * Sets the value of the named field. If fieldName denotes a static field, provide a class, otherwise provide an instance. If the
   * fieldName denotes a final field, this method could fail with an IllegalAccessException, since setting the value of final fields at
   * other times than instantiation can have unpredictable effects.<br/>
   * <br/>
   * Example:<br/>
   * <br/>
   * <code>
   * String myString = "Test"; <br/>
   * <br/>
   * //setting the private field value<br/>
   * PrivilegedAccessor.setValue(myString, "value", new char[] {'T', 'e', 's', 't'});<br/>
   * <br/>
   * //setting the static final field serialVersionUID - MIGHT FAIL<br/>
   * PrivilegedAccessor.setValue(myString.getClass(), "serialVersionUID", 1);<br/>
   * <br/>
   * </code>
   *
   * @param fieldName the name of the field
   * @param value the new value of the field
   * @return this, so calls to this method can be chained
   * @throws IllegalArgumentException if the value could not be set. This could be the case if no field with the given
   *         <code>fieldName</code> can be found; or if the field was final
   * @see junit.extensions.PA#setValue(Object, String, Object)
   */
  public PA<T> setValue(final String fieldName, final Object value) {
    PA.setValue(instanceOrClass, fieldName, value);
    return this;
  }
}
