package id.co.anfal.bsn.exception;

public class OperationNotPermittedException extends RuntimeException{
    public OperationNotPermittedException() {}

    public OperationNotPermittedException(String message) {
        super(message);
    }
}
