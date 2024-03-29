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

import java.security.InvalidParameterException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for the class <code>PA</code>.
 */
public class PATest {

    /**
     * An instance of a test-subclass.
     */
    private Child child;

    /**
     * An instance of a test-subclass in a variable of type superclass.
     */
    private Parent childInParent;
    /**
     * an instance of a test-superclass.
     */
    private Parent parent;

    /**
     * Sets up the test-environment by instantiating the test-instances.
     *
     * @see junit.framework.TestCase#setUp()
     */
    @BeforeEach
    public final void setUp() {
        this.parent = new Parent("Charlie");
        this.child = new Child("Charlie");
        this.childInParent = new Child("Charlie");
    }

    /**
     * Tears down the test-environment by deleting the test-instances.
     *
     * @see junit.framework.TestCase#tearDown()
     */
    @AfterEach
    public final void tearDown() {
        this.parent = null;
        this.child = null;
        this.childInParent = null;
    }

    /**
     * Tests the method <code>instantiate</code>.
     * 
     * @see junit.extensions.PA#instantiate(java.lang.Class)
     */
    @Test
    public void testInstantiate() {
        Parent tp = PA.instantiate(Parent.class);
        assertEquals(this.parent, tp);
        assertNotSame(this.parent, tp);
        assertEquals(this.parent, PA.instantiate(Parent.class));
        assertEquals(this.parent, PA.instantiate(Parent.class, "Charlie"));
        assertEquals(this.child, PA.instantiate(Child.class, "Charlie", 8));
        assertEquals(this.childInParent, PA.instantiate(Child.class, "Charlie", 8));
        assertEquals(this.childInParent, PA.instantiate(Child.class, new Class[] {String.class, Integer.class}, "Charlie", 8));
    }

    @Test
    public void testAutoboxingInConstructor() {
        child = PA.instantiate(Child.class, new Class[] {Integer.TYPE, Float.TYPE}, 3, 5f);
        assertEquals(3, PA.getValue(child, "privateInt"));
        assertEquals(5f, PA.getValue(child, "privateFloat"));

        child = PA.instantiate(Child.class, 4, 6f);
        assertEquals(4, PA.getValue(child, "privateInt"));
        assertEquals(6f, PA.getValue(child, "privateFloat"));

        child = PA.instantiate(Child.class, "Andreas", 0);
        assertEquals(0, PA.getValue(child, "privateInt"));
        assertEquals("Andreas", PA.getValue(child, "privateName"));

        child = PA.instantiate(Child.class, new Class[] {String.class, Integer.TYPE}, "Sebastian", 1);
        assertEquals(1, PA.getValue(child, "privateInt"));
        assertEquals("Sebastian", PA.getValue(child, "privateName"));
    }

    /**
     * Tests the method <code>instantiate</code> with arrays. See https://code.google.com/p/privilegedaccessor/issues/detail?id=17
     * 
     * @see junit.extensions.PA#instantiate(java.lang.Class)
     */
    @Test
    public void testInstantiateWithArray() {
        assertEquals(this.child, PA.instantiate(Child.class, new String[] {"Charlie", "Browne"}));
        assertEquals(this.child, PA.instantiate(Child.class, new Class[] {String[].class}, new String[] {"Charlie", "Browne"}));

        int[] ints = new int[] {1, 2};
        child = PA.instantiate(Child.class, ints);
        assertEquals(ints, PA.getValue(child, "privateInts"));

        child = PA.instantiate(Child.class, new Class[] {int[].class}, ints);
        assertEquals(ints, PA.getValue(child, "privateInts"));
    }

    /**
     * Tests the method <code>instantiate(int[])</code> with Integer[] arrays. Should throw IllegalArgumentException like when calling the
     * method directly
     *
     * @throws Exception
     * @see junit.extensions.PA#instantiate(Class, Object...)
     */
    @Test
    public void testInstantiateWithArrayOfWrongType() throws Exception {
        try {
            PA.instantiate(Child.class, new Integer[] {1, 2});
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            PA.instantiate(Child.class, new Class[] {int[].class}, new Integer[] {1, 2});
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests the method <code>instantiate</code>.
     *
     * @see junit.extensions.PA#instantiate(Class, Object...)
     */
    @Test
    public void testInstantiateOnInvalidParameters() {
        try {
            PA.instantiate(Parent.class, 21);
            fail("instantiating with wrong parameter type should throw Exception");
        } catch (RuntimeException e) {
            // this is what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertEquals(Parent.class.getName() + ".<init>(java.lang.Integer)", e.getCause().getMessage());
        }

        try {
            PA.instantiate(Child.class, "Charlie", "Brown");
            fail("instantiating with wrong second parameter type should throw Exception");
        } catch (Exception e) {
            // this is what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertEquals(Child.class.getName() + ".<init>(java.lang.String, java.lang.String)", e.getCause().getMessage());
        }

        try {
            PA.instantiate(Child.class, new Class[] {String.class, String.class}, "Charlie", 8);
            fail("instantiating with unmatching parameter types should throw Exception");
        } catch (Exception e) {
            // this is what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertEquals(Child.class.getName() + ".<init>(java.lang.String, java.lang.String)", e.getCause().getMessage());
        }

        try {
            PA.instantiate(Child.class, new Class[] {String.class, Integer.class}, "Charlie", "Brown");
            fail("instantiating with unmatching parameter types should throw Exception");
        } catch (Exception e) {
            // this is what we expect
            assertEquals(IllegalArgumentException.class, e.getCause().getClass());
            assertEquals("argument type mismatch", e.getCause().getMessage());
        }

        try {
            PA.instantiate(Child.class, new Class[] {String.class, Integer.class, String.class}, "Charlie", 8, "Brown");
            fail("instantiating with wrong parameter count should throw Exception");
        } catch (Exception e) {
            // this is what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertEquals(
                Child.class.getName() + ".<init>(java.lang.String, java.lang.Integer, java.lang.String)",
                e.getCause().getMessage());
        }

        try {
            PA.instantiate(Child.class, "Charlie", 8, "Brown");
            fail("instantiating with wrong parameter count should throw Exception");
        } catch (Exception e) {
            // this is what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertEquals(
                Child.class.getName() + ".<init>(java.lang.String, java.lang.Integer, java.lang.String)",
                e.getCause().getMessage());
        }
    }

    /**
     * Tests the constructor of PA.
     *
     * @see junit.extensions.PA#instantiate(Class, Object...)
     */
    @Test
    public void testInstantiationThrowsException() {
        try {
            PA.instantiate(PA.class);
            fail("Instantiating PA withoud parameters should throw Exception - you must have enabled assertions to run unit-tests");
        } catch (RuntimeException e) {
            // thats what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
        }
    }

    /**
     * Tests Autoboxing
     *
     * @see junit.extensions.PA#invokeMethod(Object, String, Object...)
     */
    @Test
    public void testAutoboxing() {
        PA.invokeMethod(this.child, "setInt(int)", 4);
        assertEquals(4, PA.getValue(this.child, "privateInt"));

        Integer[] integers = new Integer[] {1, 2, 3};
        PA.invokeMethod(this.child, "setPrivateInts(int[])", integers);
        int[] ints = (int[]) PA.getValue(this.child, "privateInts");

        assertEquals(ints.length, integers.length);
        for (int x = 0; x < ints.length; x++ ) {
            assertEquals((Integer) ints[x], integers[x]);
        }
    }

    /**
     * Tests the method <code>setValue</code> with a static final field.
     *
     * @see junit.extensions.PA#setValue(Object, String, Object)
     */
    @Test
    public void testSetValueOfStaticFinalField() {
        int previousValue = (Integer) PA.getValue(this.parent, "privateStaticFinalInt");
        assertTrue(previousValue != -3);

        assertThrows(IllegalArgumentException.class, () -> {
            PA.setValue(this.parent, "privateStaticFinalInt", -3);
        }, "this is what we expect when accessing private static final fields of non java.lang classes");

        assertThrows(IllegalArgumentException.class, () -> {
            PA.setValue(this.parent, "privateStaticFinalInt", previousValue);
        }, "this is what we expect when accessing private static final fields of non java.lang classes");

    }

    /**
     * Tests the method <code>setValue</code> with a static final object field.
     *
     * @see junit.extensions.PA#setValue(Object, String, Object)
     */
    @Test
    public void testSetValueOfStaticFinalObjectField() {
        String previousValue = (String) PA.getValue(this.parent, "privateStaticFinalString");
        assertNotEquals(previousValue, "Herbert");

        assertThrows(IllegalArgumentException.class, () -> {
            PA.setValue(this.parent, "privateStaticFinalString", "Herbert");
        }, "this is what we expect when accessing private static final fields of non java.lang classes");

        assertThrows(IllegalArgumentException.class, () -> {
            PA.setValue(this.parent, "privateStaticFinalString", previousValue);
        }, "this is what we expect when accessing private static final fields of non java.lang classes");

    }

    /**
     * Tests the method <code>setValue</code> with a non-existing field.
     *
     * @see junit.extensions.PA#setValue(Object, String, Object)
     */
    @Test
    public void testSetValueOnInvalidField() {
        try {
            PA.setValue(this.parent, "noSuchField", "value");
            fail("should throw NoSuchFieldException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchFieldException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.setValue(this.child, "noSuchField", "value");
            fail("should throw NoSuchFieldException");
        } catch (RuntimeException e) {
            assertEquals(NoSuchFieldException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.setValue(this.childInParent, "noSuchField", "value");
            fail("should throw NoSuchFieldException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchFieldException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.setValue(Parent.class, "noSuchField", "value");
            fail("should throw NoSuchFieldException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchFieldException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }
    }

    /**
     * Tests the example given in javadoc of <code>setValue</code>.
     * <p/>
     * This test could fail under some JVMs, since setting the value of final fields at other times than instantiation can have
     * unpredictable effects.
     * 
     * @see junit.extensions.PA#setValue(java.lang.Object, java.lang.String, java.lang.String)
     * @see java.lang.reflect.Field#set(java.lang.Object, java.lang.Object)
     */
    @Test
    public void testSetGetValueExample() {
        String myString = "Not tested";
        // PA.setValue(myString.getClass(), "serialVersionUID", 1); // sets the static final field serialVersionUID
        assertThrows(IllegalArgumentException.class, () -> {
            PA.setValue(myString, "value", new char[] {'T', 'e', 's', 't'});
        }); // sets the final field value
        // assertEquals(1L, PA.getValue(String.class, "serialVersionUID"));
    }

    /**
     * Tests the method <code>setValue</code>.
     * 
     * @see junit.extensions.PA#setValue(java.lang.Object, java.lang.String, java.lang.String)
     */
    @Test
    public void testSetGetValueWithPrimitives() {
        PA.setValue(this.child, "privateInt", 6);
        assertEquals(6, PA.getValue(this.child, "privateInt"));

        PA.setValue(this.childInParent, "privateInt", 6);
        assertEquals(6, PA.getValue(this.childInParent, "privateInt"));

        PA.setValue(this.child, "privateLong", 8L);
        assertEquals(8L, PA.getValue(this.child, "privateLong"));

        PA.setValue(this.child, "privateShort", (short) 6);
        assertEquals((short) 6, PA.getValue(this.child, "privateShort"));

        PA.setValue(this.child, "privateByte", (byte) 2);
        assertEquals((byte) 2, PA.getValue(this.child, "privateByte"));

        PA.setValue(this.child, "privateChar", 'F');
        assertEquals('F', PA.getValue(this.child, "privateChar"));

        PA.setValue(this.child, "privateBoolean", true);
        assertEquals(true, PA.getValue(this.child, "privateBoolean"));

        PA.setValue(this.child, "privateFloat", 1.5f);
        assertEquals(1.5f, PA.getValue(this.child, "privateFloat"));

        PA.setValue(this.child, "privateDouble", 1.175);
        assertEquals(1.175, PA.getValue(this.child, "privateDouble"));
    }

    /**
     * Tests the method <code>setValue</code>.
     * 
     * @see junit.extensions.PA#setValue(java.lang.Object, java.lang.String, java.lang.String)
     */
    @Test
    public void testSetGetValueWithObjectsAndArrays() {
        PA.setValue(this.parent, "privateName", "Hubert");
        assertEquals("Hubert", PA.getValue(this.parent, "privateName"));

        PA.setValue(this.child, "privateName", "Hubert");
        assertEquals("Hubert", PA.getValue(this.child, "privateName"));

        PA.setValue(this.childInParent, "privateName", "Hubert");
        assertEquals("Hubert", PA.getValue(this.childInParent, "privateName"));

        int[] Ints = new int[] {1, 2, 3};
        PA.setValue(this.child, "privateInts", Ints);
        assertEquals(Ints, PA.getValue(this.child, "privateInts"));

        String[] strings = new String[] {"Happy", "Birthday"};
        PA.setValue(this.child, "privateStrings", strings);
        assertEquals(strings, PA.getValue(this.child, "privateStrings"));
    }

    /**
     * Tests the method <code>setValue</code> with a static field.
     * 
     * @see junit.extensions.PA#setValue(java.lang.Object, java.lang.String, java.lang.String)
     */
    @Test
    public void testSetValueOfStaticField() {
        int previousValue = (Integer) PA.getValue(this.parent, "privateStaticInt");
        assertTrue(previousValue != -1);

        PA.setValue(this.parent, "privateStaticInt", -1);
        assertEquals( -1, PA.getValue(this.parent, "privateStaticInt"));

        PA.setValue(Parent.class, "privateStaticInt", previousValue);
        assertEquals(previousValue, PA.getValue(this.parent, "privateStaticInt"));
    }

    /**
     * Tests the method <code>setValue</code> with a final field.
     * 
     * @see junit.extensions.PA#setValue(java.lang.Object, java.lang.String, java.lang.String)
     */
    @Test
    public void testSetValueOfFinalField() {
        int previousValue = (Integer) PA.getValue(this.parent, "privateFinalInt");
        assertTrue(previousValue != -2);

        // The following tests run when using mvn:test, but do not run when using mvn:deploy
        // PA.setValue(this.parent, "privateFinalInt", -2);
        // assertEquals( -2, PA.getValue(this.parent, "privateFinalInt"));
        //
        // PA.setValue(this.parent, "privateFinalInt", previousValue);
        // assertEquals(previousValue, PA.getValue(this.parent, "privateFinalInt"));
    }

    /**
     * Tests the method <code>setValue</code> with a final field.
     * 
     * @see junit.extensions.PA#setValue(java.lang.Object, java.lang.String, java.lang.String)
     */
    @Test
    public void testSetValueOfFinalStringField() {
        String previousValue = (String) PA.getValue(this.parent, "privateFinalString");
        assertNotEquals(previousValue, "Test");

        // The following tests run when using mvn:test, but do not run when using mvn:deploy
        // PA.setValue(this.parent, "privateFinalString", "Test");
        // assertEquals("Test", PA.getValue(this.parent, "privateFinalString"));
        //
        // PA.setValue(this.parent, "privateFinalString", previousValue);
        // assertEquals(previousValue, PA.getValue(this.parent, "privateFinalString"));
    }

    /**
     * Tests the method <code>getValue</code>.
     * 
     * @see junit.extensions.PA#getValue(java.lang.Object, java.lang.String)
     */
    @Test
    public final void testGetValue() {
        assertEquals("Charlie", PA.getValue(this.parent, "privateName"));

        assertEquals("Charlie", PA.getValue(this.child, "privateName"));
        assertEquals(8, PA.getValue(this.child, "privateInt"));

        assertEquals("Charlie", PA.getValue(this.childInParent, "privateName"));
        assertEquals(8, PA.getValue(this.childInParent, "privateInt"));
    }

    /**
     * Tests the method <code>getValue</code> with a non-existing field.
     *
     * @see junit.extensions.PA#getValue(java.lang.Object, java.lang.String)
     */
    @Test
    public void testGetValueOnInvalidField() {
        try {
            PA.getValue(this.parent, "noSuchField");
            fail("should throw RuntimeException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchFieldException.class, e.getCause().getClass());
            assertEquals("noSuchField", e.getCause().getMessage());
        }

        try {
            PA.getValue(this.child, "noSuchField");
            fail("should throw RuntimeException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchFieldException.class, e.getCause().getClass());
            assertEquals("noSuchField", e.getCause().getMessage());
        }

        try {
            PA.getValue(this.childInParent, "noSuchField");
            fail("should throw RuntimeException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchFieldException.class, e.getCause().getClass());
            assertEquals("noSuchField", e.getCause().getMessage());
        }

        try {
            PA.getValue(Parent.class, "noSuchField");
            fail("should throw RuntimeException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchFieldException.class, e.getCause().getClass());
            assertEquals("noSuchField", e.getCause().getMessage());
        }

        try {
            PA.getValue(null, "noSuchField");
            fail("should throw RuntimeException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(InvalidParameterException.class, e.getCause().getClass());
            assertEquals("Can't get field on null object/class", e.getCause().getMessage());
        }

    }

    /**
     * Tests the method <code>getValue</code> with a static field.
     * 
     * @see junit.extensions.PA#getValue(java.lang.Object, java.lang.String)
     */
    @Test
    public void testGetValueOfStaticField() {
        assertEquals(1, PA.getValue(this.parent, "privateStaticInt"));
        assertEquals(1, PA.getValue(Parent.class, "privateStaticInt"));
    }

    @Test
    public final void testMethodChaining() {
        PA.setValue(this.child, "privateInt", 6).setValue("privateLong", 8L);
        assertEquals(6, PA.getValue(this.child, "privateInt"));
        assertEquals(8L, PA.getValue(this.child, "privateLong"));

        assertEquals(8, PA.setValue(this.child, "privateInt", 8).getValue("privateInt"));

        PA.setValue(this.child, "privateInt", 6).invokeMethod("setInt(int)", 3);
        assertEquals(3, PA.getValue(this.child, "privateInt"));
    }

    /**
     * Tests the method <code>toString</code>
     */
    @SuppressWarnings("deprecation")
    @Test
    public final void testToString() {
        assertEquals("java.lang.Object", PA.toString(new Object()));
        assertTrue(toStringEquals(this.parent.toString(), PA.toString(this.parent)));
        assertTrue(toStringEquals(this.child.toString(), PA.toString(this.child)));
        assertTrue(toStringEquals(this.childInParent.toString(), PA.toString(this.childInParent)));
    }

    private boolean toStringEquals(String toString1, String toString2) {
        if (toString1.equals(toString2)) return true;

        String[] string1Parts = toString1.split(" ");
        String[] string2Parts = toString1.split(" ");

        if ( !string1Parts[0].equals(string2Parts[0])) return false;
        if (string1Parts.length != string2Parts.length) return false;

        Collection<String> attributes1 = new ArrayList<>();
        Collection<String> attributes2 = new ArrayList<>();
        for (int x = 1; x < string1Parts.length; x++ ) {
            attributes1.add(string1Parts[x].replace("{", "").replace(",", "").trim());
            attributes2.add(string2Parts[x].replace("{", "").replace(",", "").trim());
        }

        return attributes1.containsAll(attributes2);
    }

    /**
     * Tests the method <code>getFieldNames</code>.
     *
     * @see junit.extensions.PA#getFieldNames(java.lang.Object)
     */
    @Test
    public final void testGetFieldNames() {
        Set<String> testFieldNames = new HashSet<>();

        assertEquals(testFieldNames, PA.getFieldNames(null));

        assertEquals(testFieldNames, PA.getFieldNames(Object.class));

        testFieldNames.add("privateName");
        testFieldNames.add("privateObject");
        testFieldNames.add("privateStaticInt");
        testFieldNames.add("privateFinalInt");
        testFieldNames.add("privateFinalString");
        testFieldNames.add("privateStaticFinalInt");
        testFieldNames.add("privateStaticFinalString");
        assertEquals(testFieldNames, PA.getFieldNames(this.parent));

        testFieldNames = new HashSet<>(
            Arrays
                .asList(
                    new String[] {
                        "privateInt",
                        "privateObject",
                        "privateLong",
                        "privateShort",
                        "privateByte",
                        "privateChar",
                        "privateBoolean",
                        "privateFloat",
                        "privateDouble",
                        "privateInts",
                        "privateStrings",
                        "privateObjects",
                        "privateCollection",
                        "privateName",
                        "privateStaticInt",
                        "privateStaticFinalString",
                        "privateFinalString",
                        "privateFinalInt",
                        "privateStaticFinalInt"}));

        assertEquals(testFieldNames, PA.getFieldNames(this.child), "getFieldNames didn't return all field names");
        assertEquals(testFieldNames, PA.getFieldNames(this.childInParent), "getFieldNames didn't return all field names");
    }

    /**
     * Tests the method <code>getFieldTpye</code>.
     *
     * @see junit.extensions.PA#getFieldType(java.lang.Object, java.lang.String)
     */
    @Test
    public final void testGetFieldType() {
        assertEquals(String.class, PA.getFieldType(this.parent, "privateName"));
        assertEquals(Object.class, PA.getFieldType(this.parent, "privateObject"));
        assertEquals(Integer.TYPE, PA.getFieldType(this.parent, "privateStaticInt"));
        assertEquals(Integer.TYPE, PA.getFieldType(this.parent, "privateFinalInt"));
        assertEquals(String.class, PA.getFieldType(this.parent, "privateFinalString"));
        assertEquals(Integer.TYPE, PA.getFieldType(this.parent, "privateStaticFinalInt"));
        assertEquals(String.class, PA.getFieldType(this.parent, "privateStaticFinalString"));

        assertEquals(Integer.TYPE, PA.getFieldType(this.child, "privateInt"));
        assertEquals(Object.class, PA.getFieldType(this.child, "privateObject"));
        assertEquals(Long.TYPE, PA.getFieldType(this.child, "privateLong"));
        assertEquals(Short.TYPE, PA.getFieldType(this.child, "privateShort"));
        assertEquals(Byte.TYPE, PA.getFieldType(this.child, "privateByte"));
        assertEquals(Character.TYPE, PA.getFieldType(this.child, "privateChar"));
        assertEquals(Boolean.TYPE, PA.getFieldType(this.child, "privateBoolean"));
        assertEquals(Float.TYPE, PA.getFieldType(this.child, "privateFloat"));
        assertEquals(Double.TYPE, PA.getFieldType(this.child, "privateDouble"));
        assertEquals((new int[1]).getClass(), PA.getFieldType(this.child, "privateInts"));
        assertEquals((new String[1]).getClass(), PA.getFieldType(this.child, "privateStrings"));
        assertEquals((new Object[1]).getClass(), PA.getFieldType(this.child, "privateObjects"));
        assertEquals(String.class, PA.getFieldType(this.child, "privateName"));
        assertEquals(Integer.TYPE, PA.getFieldType(this.child, "privateStaticInt"));

        assertEquals(Integer.TYPE, PA.getFieldType(this.childInParent, "privateInt"));
        assertEquals(Object.class, PA.getFieldType(this.childInParent, "privateObject"));
        assertEquals(Long.TYPE, PA.getFieldType(this.childInParent, "privateLong"));
        assertEquals(Short.TYPE, PA.getFieldType(this.childInParent, "privateShort"));
        assertEquals(Byte.TYPE, PA.getFieldType(this.childInParent, "privateByte"));
        assertEquals(Character.TYPE, PA.getFieldType(this.childInParent, "privateChar"));
        assertEquals(Boolean.TYPE, PA.getFieldType(this.childInParent, "privateBoolean"));
        assertEquals(Float.TYPE, PA.getFieldType(this.childInParent, "privateFloat"));
        assertEquals(Double.TYPE, PA.getFieldType(this.childInParent, "privateDouble"));
        assertEquals((new int[1]).getClass(), PA.getFieldType(this.childInParent, "privateInts"));
        assertEquals((new String[1]).getClass(), PA.getFieldType(this.childInParent, "privateStrings"));
        assertEquals((new Object[1]).getClass(), PA.getFieldType(this.childInParent, "privateObjects"));
        assertEquals(String.class, PA.getFieldType(this.childInParent, "privateName"));
        assertEquals(Integer.TYPE, PA.getFieldType(this.childInParent, "privateStaticInt"));
    }

    /**
     * Tests the method <code>getMethodSignatures</code>. This test can't test for equality since if run under code-coverage tool, the tool
     * might add methods. e.g. [Z $jacocoInit(java.lang.invoke.MethodHandles$Lookup, java.lang.String, java.lang.Class)
     *
     * @see junit.extensions.PA#getMethodSignatures(Object)
     */
    @Test
    public final void testGetMethodSignatures() {
        Collection<String> testMethodSignatures = new HashSet<>();

        assertEquals(testMethodSignatures, PA.getMethodSignatures(null));

        testMethodSignatures = Arrays
            .asList(
                new String[] {
                    "void finalize()",
                    "void wait(long, int)",
                    "void wait(long)",
                    "void wait()",
                    "boolean equals(java.lang.Object)",
                    "java.lang.String toString()",
                    "int hashCode()",
                    "java.lang.Class getClass()",
                    "java.lang.Object clone()",
                    "void notify()",
                    "void notifyAll()"});
        assertTrue(
            PA.getMethodSignatures(Object.class).containsAll(testMethodSignatures),
            "getMethodSignatures didn't return correct signatures");

        testMethodSignatures = Arrays
            .asList(
                new String[] {
                    "boolean equals(java.lang.Object)",
                    "java.lang.String toString()",
                    "int hashCode()",
                    "java.lang.Object getObject()",
                    "java.lang.String getName()",
                    "void setName()",
                    "void setName(java.lang.String)",
                    "void methodThrowingRuntimeException()",
                    "void setNamesWithVarargs([Ljava.lang.String;)",
                    "java.lang.String getPrivateFinalString()",
                    "int getPrivateStaticFinalInt()",
                    "java.lang.String getPrivateStaticFinalString()",
                    "void methodThrowingException()",
                    "void setObject(java.lang.Object)",
                    "void setPrivateStaticInt(int)",
                    "int getPrivateStaticInt()",
                    "int getPrivateFinalInt()",
                    "void finalize()",
                    "void wait(long, int)",
                    "void wait(long)",
                    "void wait()",
                    "boolean equals(java.lang.Object)",
                    "java.lang.String toString()",
                    "int hashCode()",
                    "java.lang.Class getClass()",
                    "java.lang.Object clone()",
                    "void notify()",
                    "void notifyAll()"});
        assertTrue(
            PA.getMethodSignatures(this.parent).containsAll(testMethodSignatures),
            "getMethodSignatures didn't return correct signatures");
    }

    /**
     * Tests the method <code>invokeMethod</code>.
     * 
     * @see junit.extensions.PA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Test
    public void testInvokeMethod() {
        assertEquals("Charlie", PA.invokeMethod(this.parent, "getName()"));

        PA.invokeMethod(this.parent, "setName(java.lang.String)", "Herbert");
        assertEquals("Herbert", PA.getValue(this.parent, "privateName"));

        PA.invokeMethod(this.parent, "setName(java.lang.String)", (Object[]) null);
        assertEquals(null, PA.getValue(this.parent, "privateName"));

        PA.invokeMethod(this.parent, "setName()");
        assertEquals("Chaplin", PA.getValue(this.parent, "privateName"));

        PA.invokeMethod(this.child, "setName(java.lang.String)", "Hubert");
        assertEquals("Hubert", PA.getValue(this.child, "privateName"));

        PA.invokeMethod(this.child, "setInt(int)", 3);
        assertEquals(3, PA.invokeMethod(this.child, "getInt()"));

        PA.invokeMethod(this.childInParent, "setName(java.lang.String)", "Norbert");
        PA.invokeMethod(this.childInParent, "setInt(int)", 3);
        assertEquals("Norbert", PA.getValue(this.childInParent, "privateName"));
        assertEquals(3, PA.getValue(this.childInParent, "privateInt"));
    }

    /**
     * Tests the method <code>invokeMethod</code> with not fully declared types.
     * 
     * @see junit.extensions.PA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Test
    public void testInvokeMethodWithNotFullyDeclaredTypes() {
        PA.invokeMethod(this.parent, "setName(String)", "Hubert");
        assertEquals("Hubert", PA.getValue(this.parent, "privateName"));

        PA.invokeMethod(this.parent, "setObject(Object)", "Heribert");
        assertEquals("Heribert", PA.getValue(this.parent, "privateObject"));

        Collection<String> testCollection = new ArrayList<>();
        PA.invokeMethod(this.child, "setPrivateCollection(Collection)", testCollection);
        assertEquals(testCollection, PA.getValue(this.child, "privateCollection"));
    }

    /**
     * Tests the method <code>invokeMethod</code> with different primitives.
     * 
     * @see junit.extensions.PA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Test
    public void testInvokeMethodWithPrimitives() {
        PA.invokeMethod(this.child, "setInt(int)", 3);
        assertEquals(3, PA.invokeMethod(this.child, "getInt()"));

        PA.invokeMethod(this.child, "setPrivateLong(long)", 3L);
        assertEquals(3L, PA.invokeMethod(this.child, "getPrivateLong()"));

        PA.invokeMethod(this.child, "setPrivateShort(short)", (short) 3);
        assertEquals((short) 3, PA.invokeMethod(this.child, "getPrivateShort()"));

        PA.invokeMethod(this.child, "setPrivateByte(byte)", (byte) 3);
        assertEquals((byte) 3, PA.invokeMethod(this.child, "getPrivateByte()"));

        PA.invokeMethod(this.child, "setPrivateBoolean(boolean)", true);
        assertEquals(true, PA.invokeMethod(this.child, "isPrivateBoolean()"));

        PA.invokeMethod(this.child, "setPrivateChar(char)", 'A');
        assertEquals('A', PA.invokeMethod(this.child, "getPrivateChar()"));

        PA.invokeMethod(this.child, "setPrivateFloat(float)", 3.1f);
        assertEquals(3.1f, PA.invokeMethod(this.child, "getPrivateFloat()"));

        PA.invokeMethod(this.child, "setPrivateDouble(double)", 3.1);
        assertEquals(3.1, PA.invokeMethod(this.child, "getPrivateDouble()"));
    }

    /**
     * Tests the method <code>invokeMethod</code> with an array.
     * 
     * @see junit.extensions.PA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Test
    public void testInvokeMethodWithArray() {
        Object[] args = {5};
        PA.invokeMethod(this.childInParent, "setInt(int)", args);
        assertEquals(5, PA.getValue(this.childInParent, "privateInt"));
    }

    /**
     * Tests the method <code>invokeMethod</code> with a typed collection.
     * 
     * @see junit.extensions.PA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Test
    public void testInvokeMethodWithTypedCollection() {
        Collection<String> testCollection = new ArrayList<>();
        PA.invokeMethod(this.child, "setPrivateCollection(java.util.Collection)", testCollection);
        assertEquals(testCollection, PA.getValue(this.child, "privateCollection"));
    }

    /**
     * Tests the method <code>invokeMethod</code> with a varargs. See https://code.google.com/p/privilegedaccessor/issues/detail?id=16
     * 
     * @see junit.extensions.PA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Test
    public void testInvokeMethodWithVarargs() {
        PA.invokeMethod(this.parent, "setNamesWithVarargs(String...)", "Charly", "Browne");
        assertEquals("Charly", PA.getValue(this.parent, "privateName"));
        assertEquals("Browne", PA.getValue(this.parent, "privateObject"));
    }

    /**
     * Tests the method <code>invokeMethod</code> with several primitive arguments.
     * 
     * @see junit.extensions.PA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Test
    public void testInvokeMethodWithMoreThanOnePrimitive() {
        PA.invokeMethod(this.child, "setSumOfTwoInts(int, int)", 5, 3);
        assertEquals(8, PA.getValue(this.child, "privateInt"));

        PA.invokeMethod(this.child, "setSumOfTwoInts(int, int)", Integer.valueOf(5), Integer.valueOf(4));
        assertEquals(9, PA.getValue(this.child, "privateInt"));
    }

    /**
     * Tests the method <code>invokeMethod</code> with arrays as arguments.
     * 
     * @see junit.extensions.PA#invokeMethod(java.lang.Object,java.lang.String, java.lang.Object)
     */
    @Test
    public void testInvokeMethodWithArrays() {
        int[] ints = new int[] {5, 3};
        PA.invokeMethod(this.child, "setPrivateInts(int[])", ints);
        assertEquals(ints, PA.getValue(this.child, "privateInts"));

        ints = new int[] {5};
        PA.invokeMethod(this.child, "setPrivateInts(int[])", ints);
        assertEquals(ints, PA.getValue(this.child, "privateInts"));

        String[] strings = new String[] {"Hello", "Dolly"};
        PA.invokeMethod(this.child, "setPrivateStrings(java.lang.String[])", (Object[]) strings);
        assertEquals(strings, PA.getValue(this.child, "privateStrings"));

        strings = new String[] {"Hello"};
        PA.invokeMethod(this.child, "setPrivateStrings(java.lang.String[])", (Object[]) strings);
        assertEquals(strings, PA.getValue(this.child, "privateStrings"));
    }

    /**
     * Tests the method <code>invokeMethod</code> with empty arrays as arguments.
     * 
     * @see junit.extensions.PA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Test
    public void testInvokeMethodWithEmptyArrays() {
        PA.invokeMethod(this.child, "setPrivateInts(int[])", (int[]) null);
        assertEquals(null, PA.getValue(this.child, "privateInts"));

        int[] ints = new int[0];
        PA.invokeMethod(this.child, "setPrivateInts(int[])", ints);
        assertEquals(ints, PA.getValue(this.child, "privateInts"));

        PA.invokeMethod(this.child, "setPrivateStrings(java.lang.String[])", (Object) null);
        assertEquals(null, PA.getValue(this.child, "privateStrings"));

        String[] strings = new String[0];
        PA.invokeMethod(this.child, "setPrivateStrings(java.lang.String[])", (Object) strings);
        assertEquals(strings, PA.getValue(this.child, "privateStrings"));
    }

    /**
     * Tests the method <code>invokeMethod</code> with filled arrays as arguments.
     * 
     * @see junit.extensions.PA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Test
    public void testInvokeMethodThatRequireArrays() {
        PA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", (Object) new Object[] {1});
        PA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", new Object[] {1});

        PA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", (Object) new Object[] {"Dolly"});
        PA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", new Object[] {"Dolly"});

        PA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", (Object) new Object[] {"Hello", 1});
        PA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", new Object[] {"Hello", 1});

        PA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", (Object) new Object[] {"Hello", 1});
        PA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", new Object[] {"Hello", 1});
    }

    /**
     * Tests the method <code>invokeMethod</code> with filled arrays and something else as arguments.
     * 
     * @see junit.extensions.PA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Test
    public void testInvokeMethodThatRequireArraysAndSomethingElse() {
        String[] strings = new String[] {"Mike"};
        String[] emptyStrings = new String[0];

        PA.invokeMethod(this.child, "setPrivateStringsAndInt(java.lang.String[], int)", strings, 3);
        assertEquals(strings, PA.getValue(this.child, "privateStrings"));
        assertEquals(3, PA.getValue(this.child, "privateInt"));
        PA.invokeMethod(this.child, "setPrivateStringsAndInt(java.lang.String[], int)", emptyStrings, 0);
        assertEquals(emptyStrings, PA.getValue(this.child, "privateStrings"));
        assertEquals(0, PA.getValue(this.child, "privateInt"));

        Object[] objects = new Object[] {"John"};
        Object[] emptyObjects = new Object[0];

        PA.invokeMethod(this.child, "setPrivateObjectsAndInt(java.lang.Object[], int)", objects, 4);
        assertEquals(objects, PA.getValue(this.child, "privateObjects"));
        assertEquals(4, PA.getValue(this.child, "privateInt"));
        PA.invokeMethod(this.child, "setPrivateObjectsAndInt(java.lang.Object[], int)", emptyObjects, 0);
        assertEquals(emptyObjects, PA.getValue(this.child, "privateObjects"));
        assertEquals(0, PA.getValue(this.child, "privateInt"));

        PA.invokeMethod(this.child, "setPrivateIntAndStrings(int, java.lang.String[])", 5, strings);
        assertEquals(strings, PA.getValue(this.child, "privateStrings"));
        assertEquals(5, PA.getValue(this.child, "privateInt"));
        PA.invokeMethod(this.child, "setPrivateIntAndStrings(int, java.lang.String[])", 0, emptyStrings);
        assertEquals(emptyStrings, PA.getValue(this.child, "privateStrings"));
        assertEquals(0, PA.getValue(this.child, "privateInt"));

        PA.invokeMethod(this.child, "setPrivateIntAndObjects(int, java.lang.Object[])", 6, objects);
        assertEquals(objects, PA.getValue(this.child, "privateObjects"));
        assertEquals(6, PA.getValue(this.child, "privateInt"));
        PA.invokeMethod(this.child, "setPrivateIntAndObjects(int, java.lang.Object[])", 0, emptyObjects);
        assertEquals(emptyObjects, PA.getValue(this.child, "privateObjects"));
        assertEquals(0, PA.getValue(this.child, "privateInt"));

        PA.invokeMethod(this.child, "setPrivateStringsAndObjects(java.lang.String[],java.lang.Object[])", strings, objects);
        assertEquals(strings, PA.getValue(this.child, "privateStrings"));
        assertEquals(objects, PA.getValue(this.child, "privateObjects"));
        PA.invokeMethod(this.child, "setPrivateStringsAndObjects(java.lang.String[],java.lang.Object[])", emptyStrings, emptyObjects);
        assertEquals(emptyStrings, PA.getValue(this.child, "privateStrings"));
        assertEquals(emptyObjects, PA.getValue(this.child, "privateObjects"));

        PA.invokeMethod(this.child, "setPrivateObjectsAndStrings(java.lang.Object[],java.lang.String[])", objects, strings);
        assertEquals(strings, PA.getValue(this.child, "privateStrings"));
        assertEquals(objects, PA.getValue(this.child, "privateObjects"));
        PA.invokeMethod(this.child, "setPrivateObjectsAndStrings(java.lang.Object[],java.lang.String[])", emptyObjects, emptyStrings);
        assertEquals(emptyStrings, PA.getValue(this.child, "privateStrings"));
        assertEquals(emptyObjects, PA.getValue(this.child, "privateObjects"));

        PA.invokeMethod(this.child, "setPrivateObjectsAndObjects(java.lang.Object[],java.lang.Object[])", objects, objects);
        assertEquals(objects, PA.getValue(this.child, "privateObjects"));
        PA.invokeMethod(this.child, "setPrivateObjectsAndObjects(java.lang.Object[],java.lang.Object[])", emptyObjects, emptyObjects);
        assertEquals(emptyObjects, PA.getValue(this.child, "privateObjects"));
    }

    /**
     * Tests the method <code>invokeMethod</code> with array of non primitives instead of several arguments. This is ok for several reasons:
     * a) downward compatibility - since this was the only way prior to varargs b) using varargs there is no possibility to distinquish
     * arrays from several arguments. @ * @see junit.extensions.PA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Test
    public void testInvokeMethodWithArrayInsteadOfSingleValues() {
        Object[] oInts = new Object[] {3, 3};
        PA.invokeMethod(this.child, "setSumOfTwoInts(int, int)", oInts);
        assertEquals(6, PA.getValue(this.child, "privateInt"));
    }

    /**
     * Tests the method <code>invokeMethod</code> with single values instead of an array.
     *
     * @see junit.extensions.PA#invokeMethod(Object, String, Object...)
     */
    @Test
    public void testInvokeMethodWithSingleValuesInsteadOfArray() {
        PA.invokeMethod(this.child, "setPrivateInts(int[])", 1, 2);
        int[] ints = (int[]) PA.getValue(this.child, "privateInts");
        assertEquals(1, ints[0]);
        assertEquals(2, ints[1]);

        PA.invokeMethod(this.child, "setPrivateStrings(java.lang.String[])", "Hello", "Bruno");
        String[] strings = (String[]) PA.getValue(this.child, "privateStrings");
        assertEquals("Hello", strings[0]);
        assertEquals("Bruno", strings[1]);
    }

    /**
     * Tests the method <code>invokeMethod</code> on a non-existing method.
     *
     * @see junit.extensions.PA#invokeMethod(Object, String, Object...)
     */
    @Test
    public void testInvokeMethodOnInvalidMethodName() {
        try {
            PA.invokeMethod(this.child, "getInt");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.invokeMethod(this.child, "setInt)(", 5);
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.invokeMethod(this.child, "getInt)");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.invokeMethod(this.child, "noSuchMethod()", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.invokeMethod(this.parent, "noSuchMethod()", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.invokeMethod(this.childInParent, "noSuchMethod()", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.invokeMethod(Parent.class, "noSuchMethod()", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }
    }

    /**
     * Tests the method <code>invokeMethod</code> with invalid arguments.
     *
     * @see junit.extensions.PA#invokeMethod(Object, String, Object...)
     */
    @Test
    public void testInvokeMethodWithInvalidSignature() {
        try {
            PA.invokeMethod(this.child, "setName", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            // that is what we expect - since the signature is missing
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.invokeMethod(this.child, "setName java.lang.String)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            // that is what we expect - since the first brace is missing
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.invokeMethod(this.child, "setName(java.lang.String", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            // that is what we expect - since the last brace is missing
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.invokeMethod(this.child, "setName(java.lang.SString)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            // that is what we expect - since the signature denotes a non-existing class
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }
    }

    /**
     * Tests the method <code>invokeMethod</code> with invalid arguments.
     *
     * @see junit.extensions.PA#invokeMethod(Object, String, Object...)
     */
    @Test
    public void testInvokeMethodWithInvalidArguments() {
        try {
            PA.invokeMethod(this.child, "setData(java.lang.String)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.invokeMethod(this.child, "setData(non.existing.package.NonExistingClass)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.invokeMethod(this.child, "setData()");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.invokeMethod(this.parent, "setData(java.lang.String)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.invokeMethod(this.childInParent, "setData(java.lang.String)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.invokeMethod(this.child, "setName(java.lang.String)", 2);
            fail("should throw IllegalArgumentException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.invokeMethod(this.child, "setName(.String)", "Heribert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.invokeMethod(this.child, "setName(string)", "Heribert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.invokeMethod(this.child, "setName(NotAString)", "Heribert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.invokeMethod(this.child, "setData(Integer)", 2);
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.invokeMethod(this.child, "setInt(java.lang.String)", (Object[]) null);
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.invokeMethod(this.child, "setInt(int)", "Herbert");
            fail("should throw IllegalArgumentException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(IllegalArgumentException.class, e.getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.invokeMethod(Parent.class, "setStaticInt(java.lang.String)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
            assertNotNull(e.getMessage());
        }

        try {
            PA.invokeMethod(this.child, "setInt(int)", (Object[]) null);
            fail("should throw IllegalArgumentException");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    /**
     * Tests the method <code>invokeMethod</code> with array of primitives instead of several arguments. This is not ok for several reasons:
     * a) downward compatibility - was not ok in the past (one had to use Object[]) b) this is the typical behaviour when using varargs
     * (Java doesn't autoconvert primitive arrays)
     *
     * @see junit.extensions.PA#invokeMethod(Object, String, Object...)
     */
    @Test
    public void testInvokeMethodWithPrimitiveArrayInsteadOfSingleValues() {
        try {
            PA.invokeMethod(this.child, "setSumOfTwoInts(int, int)", new int[] {5, 3});
            fail("invoking method with an array of primitives instead of single primitives should raise exception");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(IllegalArgumentException.class, e.getClass());
        }

        try {
            PA.invokeMethod(this.child, "setSumOfTwoInts(int, int)", new Integer[] {4, 3});
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    /**
     * Tests the method <code>invokeMethod</code> with arrays of wrong length instead of several arguments.
     *
     * @see junit.extensions.PA#invokeMethod(Object, String, Object...)
     */
    @Test
    public void testInvokeMethodWithArraysOfWrongLengthInsteadOfSingleValues() {
        try {
            PA.invokeMethod(this.child, "setSumOfTwoInts(int, int)", new int[] {1});
            fail("invoking method with array of wrong size should raise exception");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(IllegalArgumentException.class, e.getClass());
        }

        try {
            PA.invokeMethod(this.child, "setSumOfTwoInts(int, int)", new Integer[] {2});
            fail("invoking method with array of wrong size should raise exception");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(IllegalArgumentException.class, e.getClass());
        }

        try {
            PA.invokeMethod(this.child, "setSumOfTwoInts(int, int)", new Object[] {3});
            fail("invoking method with array of wrong size should raise exception");
        } catch (RuntimeException e) {
            // that is what we expect
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    /**
     * Tests varargs and Object[] can't be distinquished. See http://code.google.com/p/privilegedaccessor/issues/detail?id=11
     * 
     * @see junit.extensions.PA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Test
    public void testVarargsCantBeDistinquishedFromObjectArray() {
        PA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", new Object[] {"Hello", 3});

        // [Issue 11] the next method should fail with IllegalArgumentException, but since we can't distinquish varargs from Object[] it
        // works.
        PA.invokeMethod(this.child, "setPrivateObjects(java.lang.Object[])", "Hello", 3);
    }

    /**
     * Tests the method <code>invokeMethod</code> with arguments of type object and primitive.
     * 
     * @see junit.extensions.PA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Test
    public void testInvokeMethodWithObjectAndPrimitive() {
        Object[] args = {"Marcus", 5};
        PA.invokeMethod(this.child, "setData(java.lang.String, int)", args);
        assertEquals("Marcus", PA.getValue(this.child, "privateName"));
        assertEquals(5, PA.getValue(this.child, "privateInt"));

        PA.invokeMethod(this.childInParent, "setData(java.lang.String, int)", args);
        assertEquals("Marcus", PA.getValue(this.childInParent, "privateName"));
        assertEquals(5, PA.getValue(this.childInParent, "privateInt"));
    }

    /**
     * Tests the method <code>invokeMethod</code> on a static method.
     * 
     * @see junit.extensions.PA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Test
    public void testInvokeStaticMethod() {
        PA.invokeMethod(Parent.class, "setPrivateStaticInt(int)", 3);
        assertEquals(3, PA.getValue(Parent.class, "privateStaticInt"));
    }

    /**
     * Tests the method <code>instantiate</code> on an inner class.
     *
     * @throws ClassNotFoundException
     * @see junit.extensions.PA#instantiate(java.lang.Class)
     */
    @Test
    public void testInstantiateInnerClass() throws ClassNotFoundException {
        Object tic = PA.instantiate(Class.forName("junit.extensions.Child$InnerChild"), this.child);
        assertEquals(Class.forName("junit.extensions.Child$InnerChild"), tic.getClass());
    }

    /**
     * Tests the method <code>getValue</code> with a field of an inner class.
     *
     * @throws ClassNotFoundException
     * @see junit.extensions.PA#getValue(java.lang.Object, java.lang.String)
     */
    @Test
    public void testAccessInnerClass() throws ClassNotFoundException {
        Object tic = PA.instantiate(Class.forName("junit.extensions.Child$InnerChild"), this.child);
        PA.setValue(tic, "privateInnerInt", 5);
        assertEquals(5, PA.getValue(tic, "privateInnerInt"));
    }

    /**
     * Tests the method <code>invokeMethod</code> with a method of an inner class.
     *
     * @throws ClassNotFoundException
     * @see junit.extensions.PA#invokeMethod(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Test
    public void testAccessInnerMethod() throws ClassNotFoundException {
        Object tic = PA.instantiate(Class.forName("junit.extensions.Child$InnerChild"), this.child);
        PA.invokeMethod(tic, "setPrivateInnerInt(int)", 7);
        assertEquals(7, PA.invokeMethod(tic, "getPrivateInnerInt()"));
    }

    @Test
    public void testMethodThrowingRuntimeException() {
        assertThrows(NullPointerException.class, () -> {
            PA.invokeMethod(parent, "methodThrowingRuntimeException()");
        });
    }

    @Test
    public void testMethodThrowingException() {
        try {
            PA.invokeMethod(parent, "methodThrowingException()");
            fail("should throw exception");
        } catch (IllegalArgumentException e) {
            assertEquals(CertificateException.class, e.getCause().getClass());
        } catch (RuntimeException e) {
            fail("wront exception thrown");
        }
    }
}
