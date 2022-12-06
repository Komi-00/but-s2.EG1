package assignment.tutoring;

public class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String studentFullName) {
        super("Student " + studentFullName + " not found !");
    }
}
