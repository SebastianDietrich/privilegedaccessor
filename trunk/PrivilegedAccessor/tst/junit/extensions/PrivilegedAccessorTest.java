package junit.extensions;

import junit.framework.TestCase;

public class PrivilegedAccessorTest extends TestCase {
    private TestParent parent;

    private TestChild child;

    private TestParent childInParent;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(PrivilegedAccessorTest.class);
    }

    public void setUp() {
        this.parent = new TestParent("Charlie");
        this.child = new TestChild("Charlie");
        this.childInParent = new TestChild("Charlie");
    }

    public void tearDown() {
        this.parent = null;
        this.child = null;
        this.childInParent = null;
    }

    public void testGetValue() throws Exception {
        assertEquals("Charlie", PrivilegedAccessor.getValue(this.parent, "privateName"));

        assertEquals("Charlie", PrivilegedAccessor.getValue(this.child, "privateName"));
        assertEquals(new Integer(8), PrivilegedAccessor.getValue(this.child, "privateNumber"));

        assertEquals("Charlie", PrivilegedAccessor.getValue(this.childInParent, "privateName"));
        assertEquals(new Integer(8), PrivilegedAccessor.getValue(this.childInParent, "privateNumber"));
    }

    public void testGetValueOfStaticField() throws Exception {
        assertEquals(new Integer(1), PrivilegedAccessor.getValue(this.parent, "privateStaticNumber"));
        
        assertEquals(new Integer(1), PrivilegedAccessor.getValue(TestParent.class, "privateStaticNumber"));
    }

    public void testGetValueOnInvalidField() throws Exception {
        try {
            PrivilegedAccessor.getValue(this.parent, "noSuchField");
        } catch (Exception e) {
            assertEquals(NoSuchFieldException.class, e.getClass());
        }

        try {
            PrivilegedAccessor.getValue(this.child, "noSuchField");
        } catch (Exception e) {
            assertEquals(NoSuchFieldException.class, e.getClass());
        }

        try {
            PrivilegedAccessor.getValue(this.childInParent, "noSuchField");
        } catch (Exception e) {
            assertEquals(NoSuchFieldException.class, e.getClass());
        }
        
        try {
            PrivilegedAccessor.getValue(TestParent.class, "noSuchField");
        } catch (Exception e) {
            assertEquals(NoSuchFieldException.class, e.getClass());
        }
    }

    public void testSetValue() throws Exception {
        PrivilegedAccessor.setValue(this.parent, "privateName", "Hubert");
        assertEquals("Hubert", PrivilegedAccessor.getValue(this.parent, "privateName"));

        PrivilegedAccessor.setValue(this.child, "privateName", "Hubert");
        PrivilegedAccessor.setValue(this.child, "privateNumber", new Integer(6));
        assertEquals("Hubert", PrivilegedAccessor.getValue(this.child, "privateName"));
        assertEquals(new Integer(6), PrivilegedAccessor.getValue(this.child, "privateNumber"));

        PrivilegedAccessor.setValue(this.childInParent, "privateName", "Hubert");
        PrivilegedAccessor.setValue(this.childInParent, "privateNumber", new Integer(6));
        assertEquals("Hubert", PrivilegedAccessor.getValue(this.childInParent, "privateName"), "Hubert");
        assertEquals(new Integer(6), PrivilegedAccessor.getValue(this.childInParent, "privateNumber"));
    }
    
    public void testSetValueOfStaticField() throws Exception {
        PrivilegedAccessor.setValue(this.parent, "privateStaticNumber", new Integer(6));
        assertEquals(new Integer(6), PrivilegedAccessor.getValue(this.parent, "privateStaticNumber"));

        PrivilegedAccessor.setValue(TestParent.class, "privateStaticNumber", new Integer(7));
        assertEquals(new Integer(7), PrivilegedAccessor.getValue(this.parent, "privateStaticNumber"));
    }

    public void testSetValueOnInvalidField() throws Exception {
        try {
            PrivilegedAccessor.setValue(this.parent, "noSuchField", "value");
        } catch (Exception e) {
            assertEquals(NoSuchFieldException.class, e.getClass());
        }

        try {
            PrivilegedAccessor.setValue(this.child, "noSuchField", "value");
        } catch (Exception e) {
            assertEquals(NoSuchFieldException.class, e.getClass());
        }

        try {
            PrivilegedAccessor.setValue(this.childInParent, "noSuchField", "value");
        } catch (Exception e) {
            assertEquals(NoSuchFieldException.class, e.getClass());
        }
        
        try {
            PrivilegedAccessor.setValue(TestParent.class, "noSuchField", "value");
        } catch (Exception e) {
            assertEquals(NoSuchFieldException.class, e.getClass());
        }
    }

    public void testInvokeMethod() throws Exception {
        PrivilegedAccessor.invokeMethod(this.parent, "setName(java.lang.String)", "Herbert");
        assertEquals("Herbert", PrivilegedAccessor.getValue(this.parent, "privateName"));

        PrivilegedAccessor.invokeMethod(this.child, "setName(java.lang.String)", "Herbert");
        PrivilegedAccessor.invokeMethod(this.child, "setNumber(int)", 3);
        assertEquals("Herbert", PrivilegedAccessor.getValue(this.child, "privateName"), "Herbert");
        assertEquals(new Integer(3), PrivilegedAccessor.getValue(this.child, "privateNumber"));

        Object args[] = { "Marcus", new Integer(5) };
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
    
    public void testInvokeMethodWithPrimitiveParameter() throws Exception {
        PrivilegedAccessor.invokeMethod(this.parent, "setName(java.lang.String)", "Herbert");
        assertEquals(PrivilegedAccessor.getValue(this.parent, "privateName"), "Herbert");

        PrivilegedAccessor.invokeMethod(this.child, "setName(java.lang.String)", "Herbert");
        PrivilegedAccessor.invokeMethod(this.child, "setNumber(int)", 3);
        assertEquals(PrivilegedAccessor.getValue(this.child, "privateName"), "Herbert");
        assertEquals(PrivilegedAccessor.getValue(this.child, "privateNumber"), new Integer(3));

        Object args[] = { "Marcus", new Integer(5) };
        PrivilegedAccessor.invokeMethod(this.child, "setData(java.lang.String, java.lang.Integer)", args);
        assertEquals(PrivilegedAccessor.getValue(this.child, "privateName"), "Marcus");
        assertEquals(PrivilegedAccessor.getValue(this.child, "privateNumber"), new Integer(5));

        PrivilegedAccessor.invokeMethod(this.childInParent,"setName(java.lang.String)", "Herbert");
        PrivilegedAccessor.invokeMethod(this.childInParent, "setNumber(int)", 3);
        assertEquals(PrivilegedAccessor.getValue(this.childInParent, "privateName"), "Herbert");
        assertEquals(PrivilegedAccessor.getValue(this.childInParent, "privateNumber"), new Integer(3));

        PrivilegedAccessor.invokeMethod(this.childInParent, "setData(java.lang.String, java.lang.Integer)", args);
        assertEquals(PrivilegedAccessor.getValue(this.childInParent, "privateName"), "Marcus");
        assertEquals(PrivilegedAccessor.getValue(this.childInParent, "privateNumber"), new Integer(5));
    }

    public void testInvokeMethodOnInvalidMethodName() throws Exception {
        try {
            PrivilegedAccessor.invokeMethod(this.child, "noSuchMethod", "Herbert");
        } catch (Exception e) {
            assertEquals(NoSuchMethodException.class, e.getClass());
        }

        try {
            PrivilegedAccessor.invokeMethod(this.parent, "noSuchMethod", "Herbert");
        } catch (Exception e) {
            assertEquals(NoSuchMethodException.class, e.getClass());
        }

        try {
            PrivilegedAccessor.invokeMethod(this.childInParent, "noSuchMethod", "Herbert");
        } catch (Exception e) {
            assertEquals(NoSuchMethodException.class, e.getClass());
        }
        
        try {
            PrivilegedAccessor.invokeMethod(TestParent.class, "noSuchMethod", "Herbert");
        } catch (Exception e) {
            assertEquals(NoSuchMethodException.class, e.getClass());
        }
    }

    public void testInvokeMethodWithInvalidArguments() throws Exception {
        try {
            PrivilegedAccessor.invokeMethod(this.child, "setData(java.lang.String)", "Herbert");
        } catch (Exception e) {
            assertEquals(NoSuchMethodException.class, e.getClass());
        }

        try {
            PrivilegedAccessor.invokeMethod(this.parent, "setData(java.lang.String)", "Herbert");
        } catch (Exception e) {
            assertEquals(NoSuchMethodException.class, e.getClass());
        }

        try {
            PrivilegedAccessor.invokeMethod(this.childInParent, "setData(java.lang.String)", "Herbert");
        } catch (Exception e) {
            assertEquals(NoSuchMethodException.class, e.getClass());
        }
        
        try {
            PrivilegedAccessor.invokeMethod(TestParent.class, "setStaticNumber(java.lang.String)", "Herbert");
        } catch (Exception e) {
            assertEquals(NoSuchMethodException.class, e.getClass());
        }
    }

    public void testInvokeStaticMethod() throws Exception {
        PrivilegedAccessor.invokeMethod(TestParent.class, "setStaticNumber(int)", 3);
        assertEquals(new Integer(3), PrivilegedAccessor.getValue(TestParent.class, "privateStaticNumber"));
    }
    
    public void testInstantiate() throws Exception {
        assertEquals(this.parent, PrivilegedAccessor.instantiate(TestParent.class));
        assertEquals(this.child, PrivilegedAccessor.instantiate(TestChild.class, new Object[] { "Charlie", new Integer(8) }));
        assertEquals(this.childInParent, PrivilegedAccessor.instantiate(TestChild.class, new Object[] { "Charlie", new Integer(8) }));
    }

    public PrivilegedAccessorTest(String name) {
        super(name);
    }
}