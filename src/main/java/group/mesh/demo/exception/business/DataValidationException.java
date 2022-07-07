package group.mesh.demo.exception.business;

public class DataValidationException extends RuntimeException {

    public DataValidationException(String message) {
        super(message);
    }

    public DataValidationException() {
        super("Data validation failed");
    }
}
