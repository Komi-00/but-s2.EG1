package assignment.tutoring;

public class RessourceNotFoundException extends Exception {

    public RessourceNotFoundException(String ressourceName) {
        super("Ressource " + ressourceName + " not found !");
    }
}