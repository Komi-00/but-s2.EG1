package assignment.tutoring;

import java.util.Objects;
import java.util.Set;

public class Teacher extends Person {
    private final Set<String> skills;

    public Teacher(String firstName, String lastName, Set<String> skills) {
        super(firstName, lastName);
        this.skills = skills;
    }

    public boolean isTeacherOf(String skill) {
        return this.skills.contains(skill);
    }

    public Set<String> getSkills() {
        return skills;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Teacher)) {
            return false;
        }
        Teacher teacher = (Teacher) o;
        return Objects.equals(skills, teacher.skills);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(skills);
    }

    @Override
    public String toString() {
        return "T " + super.toString();
    }
}
