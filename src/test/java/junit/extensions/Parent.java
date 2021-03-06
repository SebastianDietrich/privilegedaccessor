/*
 * Copyright 2004-2012 Sebastian Dietrich (Sebastian.Dietrich@e-movimento.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package junit.extensions;

import java.security.cert.CertificateException;

/**
 * Test class with private methods to invoke via PrivilegedAccessor
 */
public class Parent {
    private static final int privateStaticFinalInt;
    private static final String privateStaticFinalString;
    static {
        privateStaticFinalInt = 3;
        privateStaticFinalString = "Tester";
    }
    private static int privateStaticInt;
    private final int privateFinalInt;
    private final String privateFinalString;
    private String privateName;
    private Object privateObject;

    public Parent(String name) {
        this(name, "Brown");
    }

    private Parent(String name, Object object) {
        this.privateName = name;
        this.privateObject = object;
        privateStaticInt = 1;
        privateFinalInt = 2;
        privateFinalString = "Tom";
    }

    @SuppressWarnings("unused")
    private Parent() {
        this("Charlie", "Brown");
    }

    private static int getPrivateStaticInt() {
        return Parent.privateStaticInt;
    }

    @SuppressWarnings("unused")
    private static void setPrivateStaticInt(int number) {
        Parent.privateStaticInt = number;
    }

    private static int getPrivateStaticFinalInt() {
        return Parent.privateStaticFinalInt;
    }

    private static String getPrivateStaticFinalString() {
        return Parent.privateStaticFinalString;
    }

    private String getName() {
        return this.privateName;
    }

    protected void setName(String newName) {
        this.privateName = newName;
    }

    /**
     * overloading setName(String) *
     */
    @SuppressWarnings("unused")
    private void setName() {
        this.privateName = "Chaplin";
    }

    @SuppressWarnings("unused")
    private void setNamesWithVarargs(String... name) {
        this.privateName = name[0];
        this.privateObject = name[1];
    }

    private Object getObject() {
        return this.privateObject;
    }

    @SuppressWarnings("unused")
    private void setObject(Object newObject) {
        this.privateObject = newObject;
    }

    private int getPrivateFinalInt() {
        return privateFinalInt;
    }

    private String getPrivateFinalString() {
        return privateFinalString;
    }

    @SuppressWarnings("unused")
    private void methodThrowingRuntimeException() throws NullPointerException {
        throw new NullPointerException("thrown exception");
    }

    @SuppressWarnings("unused")
    private void methodThrowingException() throws CertificateException {
        throw new CertificateException("thrown exception");
    }

    @Override
    public int hashCode() {
        return this.privateName.hashCode() + this.privateObject.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Parent)) {
            return false;
        }

        Parent otherParent = (Parent) other;
        return this.privateName.equals(otherParent.privateName) && this.privateObject.equals(otherParent.privateObject);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " {privateStaticInt=" +Parent.getPrivateStaticInt() + ", privateObject=" + getObject() + ", privateStaticFinalInt=" + Parent.getPrivateStaticFinalInt() + ", privateName=" + getName() + ", privateStaticFinalString=" + Parent.getPrivateStaticFinalString() + ", privateFinalString=" + getPrivateFinalString() +
                ", privateFinalInt=" + getPrivateFinalInt() + "}";

    }
}
