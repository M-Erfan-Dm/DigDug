package ir.ac.kntu.model;

public class InvalidMapException extends Exception{
    public InvalidMapException() {
        super("Invalid Map");
    }

    public InvalidMapException(String message) {
        super(message);
    }
}
