package assignment.tutoring;

import java.util.Objects;

public class Person {
    protected String firstName;
    protected String lastName;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static String joinToFullName(String firstName, String lastName) {
        return "" + firstName + "." + lastName;
    }

    public String getFullName() {
        return joinToFullName(firstName, lastName);
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public boolean isNamed(String fullName) {
        return this.getFullName().equals(fullName);
    }

    public boolean isNamed(String firstName, String lastName) {
        return this.isNamed(joinToFullName(firstName, lastName));
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Person)) {
            return false;
        }
        Person person = (Person) o;
        return Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

    @Override
    public String toString() {
        return this.getFullName();
    }

}
