package junit.extensions;

import junit.framework.TestCase;

/**
 * Tests for the class <code>PrivilegedAccessor</code>
 */
public class PrivilegedAccessorTest extends TestCase {
    /**
     * an instance of a test-superclass.
     */
    private TestParent parent;
    /**
     * an instance of a test-subclass
     */
    private TestChild child;
    /**
     * an instance of a test-subclass in a variable of type superclass
     */
    private TestParent childInParent;

    /**
     * The main test-method
     * @param unused not used
     */
    public static void main(final String[] unused) {
        junit.textui.TestRunner.run(PrivilegedAccessorTest.class);
    }

    /**
     * sets up the test-environment by instantiating the test-instances
     * @see junit.framework.TestCase#setUp()
     */
    public final void setUp() {
        this.parent = new TestParent("Charlie");
        this.child = new TestChild("Charlie");
        this.childInParent = new TestChild("Charlie");
    }

    /**
     * tears down the test-environment by deleting the test-instances
     * @see junit.framework.TestCase#tearDown()
     */
    public final void tearDown() {
        this.parent = null;
        this.child = null;
        this.childInParent = null;
    }

    /**
     * tests the method <code>getValue</code>
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#getValue(Object, String)
     */
    public final void testGetValue() throws Exception {
        assertEquals("Charlie", PrivilegedAccessor.getValue(this.parent, "privateName"));

        assertEquals("Charlie", PrivilegedAccessor.getValue(this.child, "privateName"));
        assertEquals(new Integer(8), PrivilegedAccessor.getValue(this.child, "privateNumber"));

        assertEquals("Charlie", PrivilegedAccessor.getValue(this.childInParent, "privateName"));
        assertEquals(new Integer(8), PrivilegedAccessor.getValue(this.childInParent, "privateNumber"));
    }

    /**
     * tests the method <code>getValue</code> with a static field
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#getValue(Object, String)
     */
    public void testGetValueOfStaticField() throws Exception {
        assertEquals(new Integer(1), PrivilegedAccessor.getValue(this.parent, "privateStaticNumber"));
        assertEquals(new Integer(1), PrivilegedAccessor.getValue(TestParent.class, "privateStaticNumber"));
    }

    /**
     * tests the method <code>getValue</code> with a non-existing field
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#getValue(Object, String)
     */
    public void testGetValueOnInvalidField() throws Exception {
        try {
            PrivilegedAccessor.getValue(this.parent, "noSuchField");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            //that is what we expect
        }

        try {
            PrivilegedAccessor.getValue(this.child, "noSuchField");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            //that is what we expect
        }

        try {
            PrivilegedAccessor.getValue(this.childInParent, "noSuchField");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            //that is what we expect
        }

        try {
            PrivilegedAccessor.getValue(TestParent.class, "noSuchField");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            //that is what we expect
        }
    }

    /**
     * tests the method <code>setValue</code>
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#setValue(Object, String, String)
     */
    public void testSetValue() throws Exception {
        PrivilegedAccessor.setValue(this.parent, "privateName", "Hubert");
        assertEquals("Hubert", PrivilegedAccessor.getValue(this.parent, "privateName"));

        PrivilegedAccessor.setValue(this.child, "privateName", "Hubert");
        PrivilegedAccessor.setValue(this.child, "privateNumber", 6);
        assertEquals("Hubert", PrivilegedAccessor.getValue(this.child, "privateName"));
        assertEquals(new Integer(6), PrivilegedAccessor.getValue(this.child, "privateNumber"));

        PrivilegedAccessor.setValue(this.childInParent, "privateName", "Hubert");
        PrivilegedAccessor.setValue(this.childInParent, "privateNumber", 6);
        assertEquals("Hubert", PrivilegedAccessor.getValue(this.childInParent, "privateName"), "Hubert");
        assertEquals(new Integer(6), PrivilegedAccessor.getValue(this.childInParent, "privateNumber"));
    }

    /**
     * tests the method <code>setValue</code> with a static field
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#setValue(Object, String, String)
     */
    public void testSetValueOfStaticField() throws Exception {
        PrivilegedAccessor.setValue(this.parent, "privateStaticNumber", new Integer(6));
        assertEquals(new Integer(6), PrivilegedAccessor.getValue(this.parent, "privateStaticNumber"));

        PrivilegedAccessor.setValue(TestParent.class, "privateStaticNumber", new Integer(7));
        assertEquals(new Integer(7), PrivilegedAccessor.getValue(this.parent, "privateStaticNumber"));
    }

    /**
     * tests the method <code>setValue</code> with a non-existing field
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#setValue(Object, String, String)
     */
    public void testSetValueOnInvalidField() throws Exception {
        try {
            PrivilegedAccessor.setValue(this.parent, "noSuchField", "value");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            //that is what we expect
        }

        try {
            PrivilegedAccessor.setValue(this.child, "noSuchField", "value");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            //that is what we expect
        }

        try {
            PrivilegedAccessor.setValue(this.childInParent, "noSuchField", "value");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            //that is what we expect
        }

        try {
            PrivilegedAccessor.setValue(TestParent.class, "noSuchField", "value");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            //that is what we expect
        }
    }

    /**
     * tests the method <code>invokeMethod</code>
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#invokeMethod(Object, String, Object)
     */
    public void testInvokeMethod() throws Exception {
        PrivilegedAccessor.invokeMethod(this.parent, "setName(java.lang.String)", "Herbert");
        assertEquals("Herbert", PrivilegedAccessor.getValue(this.parent, "privateName"));

        PrivilegedAccessor.invokeMethod(this.child, "setName(java.lang.String)", "Herbert");
        PrivilegedAccessor.invokeMethod(this.child, "setNumber(int)", 3);
        assertEquals("Herbert", PrivilegedAccessor.getValue(this.child, "privateName"), "Herbert");
        assertEquals(new Integer(3), PrivilegedAccessor.getValue(this.child, "privateNumber"));

        Object[] args = {"Marcus", new Integer(5)};
        PrivilegedAccessor.invokeMethod(this.child, "setData(java.lang.String, java.lang.Integer)", args);
        assertEquals("Marcus", PrivilegedAccessor.getValue(this.child, "privateName"));
        assertEquals(new Integer(5), PrivilegedAccessor.getValue(this.child, "privateNumber"));

        PrivilegedAccessor.invokeMethod(this.childInParent, "setName(java.lang.String)", "Herbert");
        PrivilegedAccessor.invokeMethod(this.childInParent, "setNumber(int)", 3);
        assertEquals("Herbert", PrivilegedAccessor.getValue(this.childInParent, "privateName"));
        assertEquals(new Integer(3), PrivilegedAccessor.getValue(this.childInParent, "privateNumber"));

        PrivilegedAccessor.invokeMethod(this.childInParent, "setData(java.lang.String, java.lang.Integer)", args);
        assertEquals("Marcus", PrivilegedAccessor.getValue(this.childInParent, "privateName"));
        assertEquals(new Integer(5), PrivilegedAccessor.getValue(this.childInParent, "privateNumber"));
    }

    /**
     * tests the method <code>invokeMethod</code> on a non-existing method
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#invokeMethod(Object, String, Object)
     */
    public void testInvokeMethodOnInvalidMethodName() throws Exception {
        try {
            PrivilegedAccessor.invokeMethod(this.child, "noSuchMethod", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            //that is what we expect
        }

        try {
            PrivilegedAccessor.invokeMethod(this.parent, "noSuchMethod", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            //that is what we expect
        }

        try {
            PrivilegedAccessor.invokeMethod(this.childInParent, "noSuchMethod", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            //that is what we expect
        }

        try {
            PrivilegedAccessor.invokeMethod(TestParent.class, "noSuchMethod", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            //that is what we expect
        }
    }

    /**
     * tests the method <code>invokeMethod</code> with invalid arguments
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#invokeMethod(Object, String, Object)
     */
    public void testInvokeMethodWithInvalidArguments() throws Exception {
        try {
            PrivilegedAccessor.invokeMethod(this.child, "setData(java.lang.String)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            //that is what we expect
        }

        try {
            PrivilegedAccessor.invokeMethod(this.parent, "setData(java.lang.String)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            //that is what we expect
        }
        
        try {
            PrivilegedAccessor.invokeMethod(this.childInParent, "setData(java.lang.String)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            //that is what we expect
        }

        try {
            PrivilegedAccessor.invokeMethod(this.child, "setName(java.lang.String)", new Integer(2));
            fail("should throw NoSuchMethodException");
        } catch (IllegalArgumentException e) {
            //that is what we expect
        }            

        try {
            PrivilegedAccessor.invokeMethod(this.child, "setNumber(int)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (IllegalArgumentException e) {
            //that is what we expect
        }             

        try {
            PrivilegedAccessor.invokeMethod(TestParent.class, "setStaticNumber(java.lang.String)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            //that is what we expect
        }
    }

    /**
     * tests the method <code>invokeMethod</code> on a static method
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#invokeMethod(Object, String, Object)
     */
    public void testInvokeStaticMethod() throws Exception {
        PrivilegedAccessor.invokeMethod(TestParent.class, "setStaticNumber(int)", 3);
        assertEquals(new Integer(3), PrivilegedAccessor.getValue(TestParent.class, "privateStaticNumber"));
    }
    
    public void testInvokeMethodWithMoreThanOnePrimitive() throws Exception {
        PrivilegedAccessor.invokeMethod(this.child, "setSumOfTwoNumbers(int, int)", new Integer[]{new Integer(5), new Integer(3)});
        assertEquals(new Integer(8), PrivilegedAccessor.getValue(this.child, "privateNumber"));        
    }

    /**
     * tests the method <code>instantiate</code>
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#instantiate(Class)
     */
    public void testInstantiate() throws Exception {
        assertEquals(this.parent, PrivilegedAccessor.instantiate(TestParent.class));
        assertEquals(this.child, PrivilegedAccessor.instantiate(TestChild.class, new Object[] {"Charlie", new Integer(8)}));
        assertEquals(this.childInParent, PrivilegedAccessor.instantiate(TestChild.class, new Object[] {"Charlie", new Integer(8)}));
    }

    /**
     * Constructor for this test.
     * @param name
     * @deprecated only necessary for junit prior to 3.8.1
     */
    public PrivilegedAccessorTest(String name) {
        super(name);
    }
}