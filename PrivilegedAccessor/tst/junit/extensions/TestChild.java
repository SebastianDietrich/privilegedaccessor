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
    
    private void setSumOfTwoNumbers(int number1, int number2) {
        this.privateNumber = number1 + number2;
    }

    private void setData(String name, int number) {
        setName(name);
        this.privateNumber = number;
    }
}