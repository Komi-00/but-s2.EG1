package assignment.tutoring;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import assignment.calculation.Assignment;
import assignment.calculation.AssignmentCalculator;
import assignment.calculation.AssignmentType;

public class Ressource {
    private final String NAME;
    private final int NB_MAX_PLACES;
    private final Set<StudentTutor> studentsTutors = new HashSet<>();
    private final Set<StudentInDifficulty> studentsInDifficulty = new HashSet<>();
    private final Set<Assignment<StudentTutor, StudentInDifficulty>> assignments = new HashSet<>();

    public Ressource(String name, int nbMaxPlaces) {
        this.NAME = name;
        this.NB_MAX_PLACES = nbMaxPlaces;
    }

    private static <T extends Student> void containsStudent(Collection<T> students, T studentToFind) throws StudentNotFoundException {
        if (!students.contains(studentToFind)) {
            throw new StudentNotFoundException(studentToFind.getFullName());
        }
    }

    private static <T extends Student> T getStudent(Collection<T> students, String fullname) throws StudentNotFoundException {
        for (T student : students) {
            if (student.isNamed(fullname)) {
                return student;
            }
        }
        throw new StudentNotFoundException(fullname);
    }

    public String getName() {
        return this.NAME;
    }

    public int getNbMaxPlaces() {
        return NB_MAX_PLACES;
    }

    public Set<StudentTutor> getStudentsTutors() {
        return studentsTutors;
    }

    public Set<StudentInDifficulty> getStudentsInDifficulty() {
        return studentsInDifficulty;
    }

    public Set<Assignment<StudentTutor, StudentInDifficulty>> getAllAssignments() {
        return this.assignments;
    }

    public Set<Assignment<StudentTutor, StudentInDifficulty>> getEffectiveAssignments() {
        Set<Assignment<StudentTutor, StudentInDifficulty>> computedAssignments = new HashSet<>();
        for (Assignment<StudentTutor, StudentInDifficulty> assignment : this.assignments) {
            if (assignment.isEffective()) {
                computedAssignments.add(assignment);
            }
        }
        return computedAssignments;
    }

    public Set<Assignment<StudentTutor, StudentInDifficulty>> computeAssignments() {
        AssignmentCalculator<StudentTutor, StudentInDifficulty> assignmentCalculator = new AssignmentCalculator<>(this.studentsTutors,
                this.studentsInDifficulty, this.assignments, this.NAME);

        List<Assignment<StudentTutor, StudentInDifficulty>> computedAssignments = assignmentCalculator.computeAssignments();
        Collections.sort(computedAssignments);
        computedAssignments = computedAssignments.subList(0, Math.min(NB_MAX_PLACES, computedAssignments.size()));

        this.assignments.addAll(computedAssignments);

        return this.getEffectiveAssignments();
    }

    public void resetAssignments(AssignmentType assignmentType) {
        Iterator<Assignment<StudentTutor, StudentInDifficulty>> it = this.assignments.iterator();
        while (it.hasNext()) {
            Assignment<StudentTutor, StudentInDifficulty> assignment = it.next();
            if (assignment.getType() == assignmentType) {
                it.remove();
            }
        }
    }

    // The three dot syntax (varargs) allows to pass multiple arguments of same type
    // into the method, the set of parameters passed to the method is
    // "transformed" into an array
    public void addStudentTutor(StudentTutor... students) {
        this.studentsTutors.addAll(Arrays.asList(students));
    }

    public void removeStudentTutor(StudentTutor... students) {
        this.studentsTutors.removeAll(Arrays.asList(students));
    }

    public void addStudentInDifficulty(StudentInDifficulty... students) {
        this.studentsInDifficulty.addAll(Arrays.asList(students));
    }

    public void removeStudentInDifficulty(StudentInDifficulty... students) {
        this.studentsInDifficulty.removeAll(Arrays.asList(students));
    }

    public void addAssignment(Assignment<StudentTutor, StudentInDifficulty> assignment) throws StudentNotFoundException {
        containsStudent(this.studentsTutors, assignment.getLeft());
        containsStudent(this.studentsInDifficulty, assignment.getRight());
        this.assignments.remove(assignment);
        this.assignments.add(assignment);
    }

    public void addAssignment(StudentTutor st, StudentInDifficulty sid, AssignmentType assignmentType) throws StudentNotFoundException {
        this.addAssignment(new Assignment<>(st, sid, assignmentType));
    }

    public StudentInDifficulty getStudentInDifficulty(String fullname) throws StudentNotFoundException {
        return getStudent(this.studentsInDifficulty, fullname);
    }

    public StudentTutor getStudentTutor(String fullName) throws StudentNotFoundException {
        return getStudent(this.studentsTutors, fullName);
    }

    public void removeAssignment(StudentTutor st, StudentInDifficulty sid) {
        Iterator<Assignment<StudentTutor, StudentInDifficulty>> it = this.assignments.iterator();
        while (it.hasNext()) {
            if (it.next().contains(st, sid)) {
                it.remove();
                return;
            }
        }
    }

    public void setApprovalStatus(StudentTutor st, boolean status) throws StudentNotFoundException {
        containsStudent(this.studentsTutors, st);
        st.setApprovalStatus(this.NAME, status);
    }

    public void setApprovalStatus(StudentInDifficulty sid, boolean status) throws StudentNotFoundException {
        containsStudent(this.studentsInDifficulty, sid);
        sid.setApprovalStatus(this.NAME, status);
    }

    public void setStudentsApprovalStatusByCriteria(Collection<? extends Student> students, CriteriaFilter criteriaFilter) {
        for (Student student : students) {
            if (student.isCriteriaFilterSatisfied(criteriaFilter, this.NAME)) {
                student.setApprovalStatus(this.NAME, criteriaFilter.shallApprove());
            }
        }
    }

    public void setStudentsTutorApprovalStatusByCriteria(CriteriaFilter criteriaFilter) {
        this.setStudentsApprovalStatusByCriteria(this.studentsTutors, criteriaFilter);
    }

    public void setStudentsInDifficultyApprovalStatusByCriteria(CriteriaFilter criteriaFilter) {
        this.setStudentsApprovalStatusByCriteria(this.studentsInDifficulty, criteriaFilter);
    }

    public Set<Assignment<StudentTutor, StudentInDifficulty>> getAssignments(AssignmentType type) {
        Set<Assignment<StudentTutor, StudentInDifficulty>> assignments = new HashSet<>();
        for (Assignment<StudentTutor, StudentInDifficulty> assignment : this.assignments) {
            if (assignment.getType() == type) {
                assignments.add(assignment);
            }
        }
        return assignments;
    }

}
