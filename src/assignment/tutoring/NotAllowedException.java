package assignment.tutoring;

public class NotAllowedException extends Exception {

    public NotAllowedException(String ressourceName) {
        super("You are not allowed to perform actions on the ressource named " + ressourceName + " !");
    }
}