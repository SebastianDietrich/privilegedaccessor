package junit.extensions;

/**
 * Test class with private methods to invoke via PrivilegedAccessor
 */
public class TestChild extends TestParent {
    private int privateNumber;

    private TestChild(String name, Integer number) {
        super(name);
        this.privateNumber = number.intValue();
    }

    public TestChild(String name) {
        this(name, new Integer(8));
    }

    private int getNumber() {
        return this.privateNumber;
    }

    private void setNumber(int number) {
        this.privateNumber = number;
    }

    private void setData(String name, Integer number) {
        setName(name);
        this.privateNumber = number.intValue();
    }
}