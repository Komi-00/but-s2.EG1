package assignment.input;

public class IncorrectFileFormatException extends Exception {
    public IncorrectFileFormatException(String filePath) {
        super("Le fichier avec le chemin " + filePath + " ne possède pas un format correct !");
    }
}
