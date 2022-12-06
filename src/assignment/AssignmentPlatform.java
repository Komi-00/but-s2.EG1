package assignment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import assignment.calculation.Assignment;
import assignment.calculation.AssignmentType;
import assignment.calculation.ScoreCalculator;
import assignment.calculation.ScoreCriteria;
import assignment.input.AssignmentsJSONReader;
import assignment.input.ConfigJSONReader;
import assignment.input.FiltersJSONReader;
import assignment.input.IncorrectFileFormatException;
import assignment.input.StudentsCSVReader;
import assignment.input.StudentsJSONReader;
import assignment.output.AssignmentsJSONWriter;
import assignment.output.configJSONWriter;
import assignment.tutoring.CriteriaFilter;
import assignment.tutoring.NotAllowedException;
import assignment.tutoring.Person;
import assignment.tutoring.Ressource;
import assignment.tutoring.RessourceNotFoundException;
import assignment.tutoring.StudentInDifficulty;
import assignment.tutoring.StudentNotFoundException;
import assignment.tutoring.StudentTutor;
import assignment.tutoring.Teacher;

public class AssignmentPlatform {
    private Teacher connectedTeacher;
    private Map<String, Ressource> ressources;

    /**
     * Constructor to create an assignment platform with a teacher and his ressources
     *
     * @param connectedTeacher
     * @param ressourcesNames
     */
    public AssignmentPlatform(Teacher connectedTeacher, Map<String, Integer> ressourcesNames) {
        this.connectedTeacher = connectedTeacher;
        this.ressources = new HashMap<>();
        this.setRessources(ressourcesNames);
    }

    /**
     * Constructor to create an assignment platform with a teacher
     *
     * @param connectedTeacher
     */
    public AssignmentPlatform(Teacher connectedTeacher) {
        this(connectedTeacher, new HashMap<>());
    }

    /**
     * update ressources
     *
     * @param ressourcesNames
     */
    private void setRessources(Map<String, Integer> ressourcesNames) {
        this.ressources.clear();
        for (Map.Entry<String, Integer> entry : ressourcesNames.entrySet()) {
            this.ressources.put(entry.getKey(), new Ressource(entry.getKey(), entry.getValue()));
        }
    }

    /**
     * Add the student in difficulty for each subject in which he is registered
     *
     * @param studentInDifficulty
     */
    public void addStudentInDifficulty(StudentInDifficulty studentInDifficulty) {
        for (Ressource r : this.ressources.values()) {
            if (studentInDifficulty.getEnrollments().contains(r.getName())) {
                r.addStudentInDifficulty(studentInDifficulty);
            }
        }
    }

    /**
     * Add the student tutor for the subject in which he is registered
     *
     * @param studentTutor
     */
    public void addStudentTutor(StudentTutor studentTutor) {
        for (Ressource r : this.ressources.values()) {
            if (r.getName().equals(studentTutor.getEnrollment())) {
                r.addStudentTutor(studentTutor);
            }
        }
    }

    /**
     * Remove the student in difficulty for each subject in which he is involved
     *
     * @param studentInDifficulty
     */
    public void removeStudentInDifficulty(StudentInDifficulty studentInDifficulty) {
        for (Ressource r : this.ressources.values()) {
            r.removeStudentInDifficulty(studentInDifficulty);
        }
    }

    /**
     * Remove the student tutor for the subject in which he is involved
     *
     * @param studentTutor
     */
    public void removeStudentTutor(StudentTutor studentTutor) {
        for (Ressource r : this.ressources.values()) {
            r.removeStudentTutor(studentTutor);
        }
    }

    public Set<StudentTutor> getStudentsTutor(String ressourceName) throws RessourceNotFoundException, NotAllowedException {
        return this.getRessource(ressourceName).getStudentsTutors();
    }

    public Set<StudentInDifficulty> getStudentsInDifficulty(String ressourceName) throws RessourceNotFoundException, NotAllowedException {
        return this.getRessource(ressourceName).getStudentsInDifficulty();
    }

    /**
     * Get the teacher logged in the platform
     *
     * @return a teacher
     */
    public Teacher getConnectedTeacher() {
        return connectedTeacher;
    }

    /**
     * Connect the teacher on the platform
     *
     * @param teacher
     */
    public void setConnectedTeacher(Teacher teacher) {
        this.connectedTeacher = teacher;
    }

    public Map<ScoreCriteria, Double> getCriteriaCoefficients() {
        return ScoreCalculator.getCriteriaCoefficients();
    }

    /**
     * Set the importance for several criteria by coefficients
     *
     * @param criteriaCoefficientsUpdate
     */
    public void setCriteriaCoefficients(Map<ScoreCriteria, Double> criteriaCoefficientsUpdate) {
        ScoreCalculator.setCriteriaCoefficients(criteriaCoefficientsUpdate);
    }

    /**
     * Set the importance for a criteria by a coefficient
     *
     * @param criteria
     * @param coefficient
     */
    public void setCriteriaCoefficient(ScoreCriteria criteria, double coefficient) {
        ScoreCalculator.setCriteriaCoefficient(criteria, coefficient);
    }

    /**
     * Reset the criteria's importance and replace it with his default coefficient
     *
     * @param criteria
     */
    public void resetCriteriaCoefficient(ScoreCriteria criteria) {
        ScoreCalculator.setCriteriaCoefficient(criteria, criteria.getDefaultCoefficient());
    }

    /**
     * Reset all the criteria's importance and replace them with their default coefficient
     */
    public void resetCriteriaCoefficients() {
        ScoreCalculator.resetCriteriaCoefficients();
    }

    /**
     * Add a forced assignment between a student tutor and a student in difficulty for a subject
     *
     * @param ressourceName
     * @param studentTutor
     * @param studentInDifficulty
     * @throws RessourceNotFoundException
     * @throws NotAllowedException
     * @throws StudentNotFoundException
     */
    public void addForcedAssignment(String ressourceName, StudentTutor studentTutor, StudentInDifficulty studentInDifficulty)
            throws RessourceNotFoundException, NotAllowedException, StudentNotFoundException {
        this.getRessource(ressourceName).addAssignment(studentTutor, studentInDifficulty, AssignmentType.FORCED);
    }

    /**
     * Add a forbidden assignment between a student tutor and a student in difficulty for a subject
     *
     * @param ressourceName
     * @param studentTutor
     * @param studentInDifficulty
     * @throws RessourceNotFoundException
     * @throws NotAllowedException
     * @throws StudentNotFoundException
     */

    public void addForbiddenAssignment(String ressourceName, StudentTutor studentTutor, StudentInDifficulty studentInDifficulty)
            throws RessourceNotFoundException, NotAllowedException, StudentNotFoundException {
        this.getRessource(ressourceName).addAssignment(studentTutor, studentInDifficulty, AssignmentType.FORBIDDEN);
    }

    /**
     * Remove an assignment between a student tutor and a student in difficulty for a subject
     *
     * @param ressourceName
     * @param studentTutor
     * @param studentInDifficulty
     * @throws RessourceNotFoundException
     * @throws NotAllowedException
     */
    public void removeAssignment(String ressourceName, StudentTutor studentTutor, StudentInDifficulty studentInDifficulty)
            throws RessourceNotFoundException, NotAllowedException {
        this.getRessource(ressourceName).removeAssignment(studentTutor, studentInDifficulty);
    }

    /**
     * Compute the assignments for a subject
     *
     * @param ressourceName
     * @return a set of effective assignments (forced and computed assignments combined)
     * @throws RessourceNotFoundException
     * @throws NotAllowedException
     */
    public Set<Assignment<StudentTutor, StudentInDifficulty>> computeAssignments(String ressourceName) throws RessourceNotFoundException, NotAllowedException {
        return this.getRessource(ressourceName).computeAssignments();
    }

    /**
     * Reset the assignments for a subject and an assignment type (computed, forced or forbidden assignments)
     *
     * @param ressourceName
     * @param assignmentType
     * @throws RessourceNotFoundException
     * @throws NotAllowedException
     */
    public void resetAssignments(String ressourceName, AssignmentType assignmentType) throws RessourceNotFoundException, NotAllowedException {
        this.getRessource(ressourceName).resetAssignments(assignmentType);
    }

    // TODO : some of these are useless , i did it for test 
    public void resetAllAssignments(String ressourceName) throws RessourceNotFoundException, NotAllowedException {
        for (AssignmentType assignmentType : AssignmentType.values()) {
            this.resetAssignments(ressourceName, assignmentType);
        }
    }

    public void resetAllAssignments() throws RessourceNotFoundException, NotAllowedException {
        for (Ressource ressource : this.ressources.values()) {
            this.resetAllAssignments(ressource.getName());
        }
    }

    /**
     * Return all the effective assignments for a ressource (computed and forced assignments)
     *
     * @param ressourceName
     * @return a set of effective assignments (forced and computed assignements combined)
     * @throws RessourceNotFoundException
     * @throws NotAllowedException
     */
    public Set<Assignment<StudentTutor, StudentInDifficulty>> getEffectiveAssignments(String ressourceName)
            throws RessourceNotFoundException, NotAllowedException {
        return this.getRessource(ressourceName).getEffectiveAssignments();
    }

    /**
     * Return all the forbidden assignments for a subject
     *
     * @param ressourceName
     * @return a set of forbidden assignments
     * @throws RessourceNotFoundException
     * @throws NotAllowedException
     */
    public Set<Assignment<StudentTutor, StudentInDifficulty>> getForbiddenAssignments(String ressourceName)
            throws RessourceNotFoundException, NotAllowedException {
        return this.getRessource(ressourceName).getAssignments(AssignmentType.FORBIDDEN);
    }

    /**
     * Return all assignments for a subject (computed, forced, forbidden assignments)
     *
     * @param ressourceName
     * @return
     * @throws RessourceNotFoundException
     * @throws NotAllowedException
     */
    public Set<Assignment<StudentTutor, StudentInDifficulty>> getAllAssignments(String ressourceName) throws RessourceNotFoundException, NotAllowedException {
        return this.getRessource(ressourceName).getAllAssignments();
    }

    public Set<Assignment<StudentTutor, StudentInDifficulty>> getAssignments(String ressourceName, AssignmentType type)
            throws RessourceNotFoundException, NotAllowedException {
        return this.getRessource(ressourceName).getAssignments(type);
    }

    /**
     * Set all students tutor approval status for a subject thanks to filters on criteria
     *
     * @param criteriaFilter
     * @param ressourceName
     * @throws RessourceNotFoundException
     * @throws NotAllowedException
     */
    public void setStudentsTutorApprovalStatusByCriteria(CriteriaFilter criteriaFilter, String ressourceName)
            throws RessourceNotFoundException, NotAllowedException {
        this.getRessource(ressourceName).setStudentsTutorApprovalStatusByCriteria(criteriaFilter);
    }

    /**
     * Set all students in difficulty approval status for a subject thanks to filters on criteria
     *
     * @param criteriaFilter
     * @param ressourceName
     * @throws RessourceNotFoundException
     * @throws NotAllowedException
     */
    public void setStudentsInDifficultyApprovalStatusByCriteria(CriteriaFilter criteriaFilter, String ressourceName)
            throws RessourceNotFoundException, NotAllowedException {
        this.getRessource(ressourceName).setStudentsInDifficultyApprovalStatusByCriteria(criteriaFilter);
    }

    /**
     * Get a ressource with his name
     *
     * @param ressourceName
     * @return a ressource
     * @throws RessourceNotFoundException
     * @throws NotAllowedException
     */
    private Ressource getRessource(String ressourceName) throws RessourceNotFoundException, NotAllowedException {
        Ressource ressource = this.ressources.get(ressourceName);
        if (ressource == null)
            throw new RessourceNotFoundException(ressourceName);
        if (!this.connectedTeacher.isTeacherOf(ressourceName))
            throw new NotAllowedException(ressourceName);

        return ressource;
    }

    /**
     * Add students tutor and students in difficulty from a CSV file
     *
     * @param path
     * @throws IOException
     * @throws IncorrectFileFormatException
     */
    public void importStudentsFromCSV(String path) throws IOException, IncorrectFileFormatException {
        StudentsCSVReader reader = new StudentsCSVReader(path);

        for (StudentTutor st : reader.readStudentsTutor()) {
            this.addStudentTutor(st);
        }
        for (StudentInDifficulty sid : reader.readStudentsInDifficulty()) {
            this.addStudentInDifficulty(sid);
        }
    }

    /**
     * Import students tutor and students in difficulty from a JSON file
     *
     * @param path
     * @throws IOException
     * @throws IncorrectFileFormatException
     */
    public void importStudentsFromJSON(String path) throws IOException, IncorrectFileFormatException {
        StudentsJSONReader studentsJSONReader = new StudentsJSONReader(path);
        for (StudentInDifficulty sid : studentsJSONReader.readStudentsInDifficulty()) {
            this.addStudentInDifficulty(sid);
        }
        for (StudentTutor st : studentsJSONReader.readStudentsTutor()) {
            this.addStudentTutor(st);
        }
    }

    /**
     * Import assignments from an external JSON file
     *
     * @param path
     * @param ressourceName
     * @throws IOException
     * @throws IncorrectFileFormatException
     * @throws RessourceNotFoundException
     * @throws NotAllowedException
     * @throws StudentNotFoundException
     */
    public void importAssignmentsFromJSON(String path, String ressourceName)
            throws IOException, IncorrectFileFormatException, RessourceNotFoundException, NotAllowedException, StudentNotFoundException {
        Ressource ressource = this.getRessource(ressourceName);
        AssignmentsJSONReader assignmentsJSONReader = new AssignmentsJSONReader(path);

        for (Assignment<String, String> nameAssignment : assignmentsJSONReader.readNamesAssignments()) {
            StudentTutor studentTutor = ressource.getStudentTutor(nameAssignment.getLeft());
            StudentInDifficulty studentInDifficulty = ressource.getStudentInDifficulty(nameAssignment.getRight());

            Assignment<StudentTutor, StudentInDifficulty> assignmentToAdd = new Assignment<>(studentTutor, studentInDifficulty, nameAssignment.getType(),
                    nameAssignment.getScore());

            ressource.addAssignment(assignmentToAdd);
        }
    }

    /**
     * Export assignments to an external JSON file
     *
     * @param path
     * @param ressourceName
     * @throws IOException
     * @throws RessourceNotFoundException
     * @throws NotAllowedException
     */
    public void exportAssignmentsToJSON(String path, String ressourceName) throws IOException, RessourceNotFoundException, NotAllowedException {
        Ressource ressource = this.getRessource(ressourceName);
        AssignmentsJSONWriter assignmentsJSONWriter = new AssignmentsJSONWriter(path);
        assignmentsJSONWriter.writeAssignments(ressource.getAllAssignments());
    }

    /**
     * Set the importance for criteria by coefficients from a JSON file
     *
     * @param path
     * @throws IOException
     * @throws IncorrectFileFormatException
     */
    public void importConfigFromJSON(String path) throws IOException, IncorrectFileFormatException {
        ConfigJSONReader configJSONReader = new ConfigJSONReader(path);
        this.setCriteriaCoefficients(configJSONReader.readCoefficients());
        this.setRessources(configJSONReader.readRessources());
    }

    public void importCoefficientFromJSON(String path) throws IOException, IncorrectFileFormatException {
        ConfigJSONReader configJSONReader = new ConfigJSONReader(path);
        this.setCriteriaCoefficients(configJSONReader.readCoefficients());
    }

    /**
     * Set all students approval status for a subject thanks to filters on criteria from a JSON file
     *
     * @param path
     * @param ressourceName
     * @throws IOException
     * @throws IncorrectFileFormatException
     * @throws RessourceNotFoundException
     * @throws NotAllowedException
     */
    public void setApprovalStatusByCriteriaFiltersFromJSON(String path, String ressourceName)
            throws IOException, IncorrectFileFormatException, RessourceNotFoundException, NotAllowedException {
        FiltersJSONReader filtersJSONReader = new FiltersJSONReader(path);

        Ressource ressource = this.getRessource(ressourceName);
        for (CriteriaFilter cf : filtersJSONReader.readCriteriaFiltersStudentsTutor()) {
            ressource.setStudentsTutorApprovalStatusByCriteria(cf);
        }

        for (CriteriaFilter cf : filtersJSONReader.readCriteriaFiltersStudentsInDifficulty()) {
            ressource.setStudentsInDifficultyApprovalStatusByCriteria(cf);
        }
    }

    /**
     * Get a student tutor with his fullname
     *
     * @param firstName
     * @param lastName
     * @return a student tutor
     * @throws StudentNotFoundException
     */
    public StudentTutor getStudentTutorByName(String firstName, String lastName) throws StudentNotFoundException {
        for (Ressource ressource : this.ressources.values()) {
            try {
                return ressource.getStudentTutor(Person.joinToFullName(firstName, lastName));
            } catch (StudentNotFoundException e) {
                // do nothing
            }
        }
        throw new StudentNotFoundException(Person.joinToFullName(firstName, lastName));
    }

    /**
     * Get a student in difficulty with his fullname
     *
     * @param firstName
     * @param lastName
     * @return a student in difficulty
     * @throws StudentNotFoundException
     */
    public StudentInDifficulty getStudentInDifficultyByName(String firstName, String lastName) throws StudentNotFoundException {
        for (Ressource ressource : this.ressources.values()) {
            try {
                return ressource.getStudentInDifficulty(Person.joinToFullName(firstName, lastName));
            } catch (StudentNotFoundException e) {
                // do nothing
            }
        }
        throw new StudentNotFoundException(Person.joinToFullName(firstName, lastName));
    }

    public void exportCoefficientsToJSON(String path) throws IOException {
        configJSONWriter config = new configJSONWriter(path);
        config.writeCoefficients(this.getCriteriaCoefficients());
    }
}