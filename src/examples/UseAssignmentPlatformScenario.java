package examples;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import assignment.AssignmentPlatform;
import assignment.calculation.Assignment;
import assignment.input.IncorrectFileFormatException;
import assignment.tutoring.MotivationScale;
import assignment.tutoring.NotAllowedException;
import assignment.tutoring.RessourceNotFoundException;
import assignment.tutoring.StudentInDifficulty;
import assignment.tutoring.StudentNotFoundException;
import assignment.tutoring.StudentTutor;
import assignment.tutoring.Teacher;

/**
 * @deprecated Use UseAssignmentPlatform instead
 */
@Deprecated
public class UseAssignmentPlatformScenario {
    // Path et files
    static String myPath = System.getProperty("user.dir") + File.separator + "res" + File.separator;
    static String myFileStudents = "students.json";
    static String myFileConfig = "config.json";
    static String myFileCriteriaFiltersIHM = "IHM_filters.json";
    static String myFileCriteriaFiltersPOO = "POO_filters.json";

    public static void scenario() {
        // Creation et connexion de l'enseignant et des ressources affectées
        Set<String> skills = Set.of("IHM", "POO");
        Teacher teacher = new Teacher("firstName", "lastName", skills);
        Map<String, Integer> ressourceName = new HashMap<>();
        ressourceName.put("IHM", 18);
        ressourceName.put("POO", 19);
        AssignmentPlatform platform = new AssignmentPlatform(teacher, ressourceName);

        // Création d'étudiants manuellement et ajouts aux listes des étudiants
        // candidats
        Map<String, Double> gradesSt1 = new HashMap<>();
        gradesSt1.put("POO", 17.2);
        StudentTutor st1 = new StudentTutor("Florian", "Etrillard", gradesSt1, 17.5, MotivationScale.HIGH_MOTIVATION, 0, "POO", 2);
        platform.addStudentTutor(st1);
        Map<String, Double> gradesSt2 = new HashMap<>();
        gradesSt2.put("IHM", 16.2);
        StudentTutor st2 = new StudentTutor("Felix", "Pereira", gradesSt2, 16.3, MotivationScale.HIGH_MOTIVATION, 1, "IHM", 2);
        platform.addStudentTutor(st2);

        Map<String, Double> gradesSid1 = new HashMap<>();
        gradesSid1.put("POO", 11.3);
        gradesSid1.put("IHM", 6.6);
        Set<String> enrollmentsSid1 = Set.of("POO", "IHM");
        StudentInDifficulty sid1 = new StudentInDifficulty("Renan", "Declercq", gradesSid1, 12, MotivationScale.HIGH_MOTIVATION, 1, enrollmentsSid1);
        platform.addStudentInDifficulty(sid1);

        try {
            // Import et création des étudiants
            platform.importStudentsFromJSON(myPath + myFileStudents);
            // Ajout d'assignations forcées et interdites
            platform.addForcedAssignment("POO", st1, sid1);
            platform.addForcedAssignment("IHM", platform.getStudentTutorByName("Mans", "Vise"), platform.getStudentInDifficultyByName("Tou", "Juste"));
            platform.addForbiddenAssignment("IHM", st2, sid1);
            platform.addForbiddenAssignment("POO", platform.getStudentTutorByName("Jean", "Teager"), platform.getStudentInDifficultyByName("Hakan", "Ryman"));

            // Import des filtres par critères et fixation des status d'approbation des
            // étudiants pour IHM et POO
            platform.setApprovalStatusByCriteriaFiltersFromJSON(myPath + myFileCriteriaFiltersIHM, "IHM");
            platform.setApprovalStatusByCriteriaFiltersFromJSON(myPath + myFileCriteriaFiltersPOO, "POO");

            Set<Assignment<StudentTutor, StudentInDifficulty>> assignmentsIHM = platform.computeAssignments("IHM");
            printAssignments(assignmentsIHM, "IHM");
            printAssignments(platform.getForbiddenAssignments("IHM"), "Forbidden assignments IHM");

            Set<Assignment<StudentTutor, StudentInDifficulty>> assignmentsPOO = platform.computeAssignments("POO");
            printAssignments(assignmentsPOO, "POO");
            printAssignments(platform.getForbiddenAssignments("POO"), "Forbidden assignments POO");
        } catch (RessourceNotFoundException | NotAllowedException | StudentNotFoundException | IOException |
                 IncorrectFileFormatException e) {
            System.out.println(e.getMessage());
        }

        try {
            platform.exportCoefficientsToJSON(myPath + "coefficients_config.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printAssignments(Set<Assignment<StudentTutor, StudentInDifficulty>> assignments, String title) {
        int cpt = 1;
        System.out.println("\n" + title + " :");
        for (Assignment<StudentTutor, StudentInDifficulty> assignment : assignments) {
            System.out.println(cpt + " : " + assignment);
            cpt++;
        }
    }

    public static void main(String[] args) {
        scenario();
    }

}
