package examples;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import assignment.AssignmentPlatform;
import assignment.input.IncorrectFileFormatException;
import assignment.tutoring.NotAllowedException;
import assignment.tutoring.RessourceNotFoundException;
import assignment.tutoring.StudentNotFoundException;
import assignment.tutoring.Teacher;

public class UseAssignmentPlatform {
    static String resPath = System.getProperty("user.dir") + File.separator + "res" + File.separator;
    static String studentsJSONFile = "students.json";
    static String configFile = "config.json";
    static String criteriaFiltersFile = "_filters.json";
    static String defaultCriteriaFiltersFile = "DEFAULT_filters.json";
    static String assignmentsFile = "_assignments.json";

    private static String getRessourcePath(String ressourceName, String fileName) {
        return resPath + ressourceName + File.separator + ressourceName + fileName;
    }

    public static void run(Teacher teacher) {
        AssignmentPlatform platform = new AssignmentPlatform(teacher);

        try {
            platform.importConfigFromJSON(resPath + configFile);
            platform.importStudentsFromJSON(resPath + studentsJSONFile);
        } catch (IOException | IncorrectFileFormatException e) {
            System.out.println(e.getMessage());
        }

        for (String ressourceName : teacher.getSkills()) {
            try {
                runForOneRessource(platform, ressourceName);
            } catch (RessourceNotFoundException | NotAllowedException | IOException | IncorrectFileFormatException
                     | StudentNotFoundException e) {
                System.out.println("Erreur: " + e.getMessage() + "\n");
            }
        }

    }

    public static void runForOneRessource(AssignmentPlatform platform, String ressourceName)
            throws RessourceNotFoundException, NotAllowedException, IOException, IncorrectFileFormatException, StudentNotFoundException {

        System.out.println(ressourceName + ":");
        try {
            platform.importAssignmentsFromJSON(getRessourcePath(ressourceName, assignmentsFile), ressourceName);
        } catch (FileNotFoundException e) {
            System.out.println("Le fichier des affectations n'existe pas encore, il sera donc créé");
        }

        try {
            platform.setApprovalStatusByCriteriaFiltersFromJSON(getRessourcePath(ressourceName, criteriaFiltersFile), ressourceName);
        } catch (FileNotFoundException e) {
            platform.setApprovalStatusByCriteriaFiltersFromJSON(resPath + defaultCriteriaFiltersFile, ressourceName);
            System.out.println("Pas de filtres d'approbation spécifiés, utilisation des filtres par défaut");
        }

        platform.computeAssignments(ressourceName);
        platform.exportAssignmentsToJSON(getRessourcePath(ressourceName, assignmentsFile), ressourceName);

        System.out.println("Vos affectations se trouvent dans le fichier " + getRessourcePath(ressourceName, assignmentsFile) + "\n");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Set<String> skills = new HashSet<>();
        boolean done = false;
        String subject;

        while (!done) {
            System.out.println(
                    "Entrez une matière dans laquelle vous intervenez ou entrez OK si vous avez déjà indiqué toutes vos matières : ");
            subject = sc.next();
            if (subject.equalsIgnoreCase("OK")) {
                sc.close();
                done = true;
            } else {
                skills.add(subject.toUpperCase());
            }
        }
        Teacher teacher = new Teacher("Elon", "Musk", skills);
        run(teacher);
    }

}
