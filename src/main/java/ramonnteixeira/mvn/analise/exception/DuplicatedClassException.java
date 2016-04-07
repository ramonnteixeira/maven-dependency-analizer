package ramonnteixeira.mvn.analise.exception;

public class DuplicatedClassException extends RuntimeException {

    private static final long serialVersionUID = 1184721090136281496L;

    public DuplicatedClassException(String message) {
        super(message);
    }
    
}
