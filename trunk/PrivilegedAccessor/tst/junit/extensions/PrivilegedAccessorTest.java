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
     * 
     * @param unused not used
     */
    public static void main(final String[] unused) {
        junit.textui.TestRunner.run(PrivilegedAccessorTest.class);
    }

    /**
     * sets up the test-environment by instantiating the test-instances
     * 
     * @see junit.framework.TestCase#setUp()
     */
    public final void setUp() {
        this.parent = new TestParent("Charlie");
        this.child = new TestChild("Charlie");
        this.childInParent = new TestChild("Charlie");
    }

    /**
     * tears down the test-environment by deleting the test-instances
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    public final void tearDown() {
        this.parent = null;
        this.child = null;
        this.childInParent = null;
    }

    /**
     * tests the method <code>getValue</code>
     * 
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#getValue(Object, String)
     */
    public final void testGetValue() throws Exception {
        assertEquals("Charlie", PA.getValue(this.parent, "privateName"));

        assertEquals("Charlie", PA.getValue(this.child, "privateName"));
        assertEquals(new Integer(8), PA.getValue(this.child, "privateNumber"));

        assertEquals("Charlie", PA.getValue(this.childInParent, "privateName"));
        assertEquals(new Integer(8), PA.getValue(this.childInParent,
                "privateNumber"));
    }

    /**
     * tests the method <code>getValue</code> with a static field
     * 
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#getValue(Object, String)
     */
    public void testGetValueOfStaticField() throws Exception {
        assertEquals(new Integer(1), PA.getValue(this.parent,
                "privateStaticNumber"));
        assertEquals(new Integer(1), PA.getValue(TestParent.class,
                "privateStaticNumber"));
    }

    /**
     * tests the method <code>getValue</code> with a non-existing field
     * 
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#getValue(Object, String)
     */
    public void testGetValueOnInvalidField() throws Exception {
        try {
            PA.getValue(this.parent, "noSuchField");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            // that is what we expect
        }

        try {
            PA.getValue(this.child, "noSuchField");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            // that is what we expect
        }

        try {
            PA.getValue(this.childInParent, "noSuchField");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            // that is what we expect
        }

        try {
            PA.getValue(TestParent.class, "noSuchField");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            // that is what we expect
        }
    }

    /**
     * tests the method <code>setValue</code>
     * 
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#setValue(Object, String, String)
     */
    public void testSetValue() throws Exception {
        PA.setValue(this.parent, "privateName", "Hubert");
        assertEquals("Hubert", PA.getValue(this.parent, "privateName"));

        PA.setValue(this.child, "privateName", "Hubert");
        PA.setValue(this.child, "privateNumber", 6);
        assertEquals("Hubert", PA.getValue(this.child, "privateName"));
        assertEquals(new Integer(6), PA.getValue(this.child, "privateNumber"));

        PA.setValue(this.childInParent, "privateName", "Hubert");
        PA.setValue(this.childInParent, "privateNumber", 6);
        assertEquals("Hubert", PA.getValue(this.childInParent, "privateName"),
                "Hubert");
        assertEquals(new Integer(6), PA.getValue(this.childInParent,
                "privateNumber"));
    }

    /**
     * tests the method <code>setValue</code> with a static field
     * 
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#setValue(Object, String, String)
     */
    public void testSetValueOfStaticField() throws Exception {
        PA.setValue(this.parent, "privateStaticNumber", new Integer(6));
        assertEquals(new Integer(6), PA.getValue(this.parent,
                "privateStaticNumber"));

        PA.setValue(TestParent.class, "privateStaticNumber", new Integer(7));
        assertEquals(new Integer(7), PA.getValue(this.parent,
                "privateStaticNumber"));
    }

    /**
     * tests the method <code>setValue</code> with a non-existing field
     * 
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#setValue(Object, String, String)
     */
    public void testSetValueOnInvalidField() throws Exception {
        try {
            PA.setValue(this.parent, "noSuchField", "value");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            // that is what we expect
        }

        try {
            PA.setValue(this.child, "noSuchField", "value");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            // that is what we expect
        }

        try {
            PA.setValue(this.childInParent, "noSuchField", "value");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            // that is what we expect
        }

        try {
            PA.setValue(TestParent.class, "noSuchField", "value");
            fail("should throw NoSuchFieldException");
        } catch (NoSuchFieldException e) {
            // that is what we expect
        }
    }

    /**
     * tests the method <code>invokeMethod</code>
     * 
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#invokeMethod(Object, String, Object)
     */
    public void testInvokeMethod() throws Exception {
        PA.invokeMethod(this.parent, "setName(java.lang.String)", "Herbert");
        assertEquals("Herbert", PA.getValue(this.parent, "privateName"));

        PA.invokeMethod(this.child, "setName(java.lang.String)", "Herbert");
        PA.invokeMethod(this.child, "setNumber(int)", 3);
        assertEquals("Herbert", PA.getValue(this.child, "privateName"),
                "Herbert");
        assertEquals(new Integer(3), PA.getValue(this.child, "privateNumber"));

        PA.invokeMethod(this.childInParent, "setName(java.lang.String)",
                "Herbert");
        PA.invokeMethod(this.childInParent, "setNumber(int)", 3);
        assertEquals("Herbert", PA.getValue(this.childInParent, "privateName"));
        assertEquals(new Integer(3), PA.getValue(this.childInParent,
                "privateNumber"));
    }

    /**
     * tests the method <code>invokeMethod</code> on a non-existing method
     * 
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#invokeMethod(Object, String, Object)
     */
    public void testInvokeMethodOnInvalidMethodName() throws Exception {
        try {
            PA.invokeMethod(this.child, "noSuchMethod", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect
        }

        try {
            PA.invokeMethod(this.parent, "noSuchMethod", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect
        }

        try {
            PA.invokeMethod(this.childInParent, "noSuchMethod", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect
        }

        try {
            PA.invokeMethod(TestParent.class, "noSuchMethod", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect
        }
    }

    /**
     * tests the method <code>invokeMethod</code> with invalid arguments
     * 
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#invokeMethod(Object, String, Object)
     */
    public void testInvokeMethodWithInvalidArguments() throws Exception {
        try {
            PA.invokeMethod(this.child, "setData(java.lang.String)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect
        }

        try {
            PA.invokeMethod(this.parent, "setData(java.lang.String)",
                            "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect
        }

        try {
            PA.invokeMethod(this.childInParent, "setData(java.lang.String)",
                    "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect
        }

        try {
            PA.invokeMethod(this.child, "setName(java.lang.String)",
                    new Integer(2));
            fail("should throw NoSuchMethodException");
        } catch (IllegalArgumentException e) {
            // that is what we expect
        }

        try {
            PA.invokeMethod(this.child, "setNumber(int)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (IllegalArgumentException e) {
            // that is what we expect
        }

        try {
            PA.invokeMethod(TestParent.class,
                    "setStaticNumber(java.lang.String)", "Herbert");
            fail("should throw NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // that is what we expect
        }
    }

    /**
     * tests the method <code>invokeMethod</code> on a static method
     * 
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#invokeMethod(Object, String, Object)
     */
    public void testInvokeStaticMethod() throws Exception {
        PA.invokeMethod(TestParent.class, "setStaticNumber(int)", 3);
        assertEquals(new Integer(3), PA.getValue(TestParent.class,
                "privateStaticNumber"));
    }

    public void testInvokeMethodWithMoreThanOnePrimitive() throws Exception {
        PA.invokeMethod(this.child, "setSumOfTwoNumbers(int, int)",
                new Integer[] { new Integer(5), new Integer(3) });
        assertEquals(new Integer(8), PA.getValue(this.child, "privateNumber"));
        
        PA.invokeMethod(this.child, "setSumOfTwoNumbers(int, int)",
                new Object[] { new Integer(5), new Integer(3) });
        assertEquals(new Integer(8), PA.getValue(this.child, "privateNumber"));
    }

    public void testInvokeMethodWithObjectAndPrimitive() throws Exception {
        Object[] args = { "Marcus", new Integer(5) };
        PA.invokeMethod(this.child,
                "setData(java.lang.String, int)", args);
        assertEquals("Marcus", PA.getValue(this.child, "privateName"));
        assertEquals(new Integer(5), PA.getValue(this.child, "privateNumber"));
        
        PA.invokeMethod(this.childInParent,
                "setData(java.lang.String, int)", args);
        assertEquals("Marcus", PA.getValue(this.childInParent, "privateName"));
        assertEquals(new Integer(5), PA.getValue(this.childInParent,
                "privateNumber"));
    }
    
    public void testInvokeMethodWithArray() throws Exception {
        Object[] args = { new Integer(5) };
        PA.invokeMethod(this.childInParent, "setNumber(int)", args);
        assertEquals(new Integer(5), PA.getValue(this.childInParent,
                "privateNumber"));
    }
    
    /**
     * tests the method <code>instantiate</code>
     * 
     * @throws Exception
     * @see junit.extensions.PrivilegedAccessor#instantiate(Class)
     */
    public void testInstantiate() throws Exception {
        assertEquals(this.parent, PA.instantiate(TestParent.class));
        assertEquals(this.child, PA.instantiate(TestChild.class, new Object[] {
                "Charlie", new Integer(8) }));
        assertEquals(this.childInParent, PA.instantiate(TestChild.class,
                new Object[] { "Charlie", new Integer(8) }));
    }

    /**
     * Constructor for this test.
     * 
     * @param name
     * @deprecated only necessary for junit prior to 3.8.1
     */
    public PrivilegedAccessorTest(String name) {
        super(name);
    }
}