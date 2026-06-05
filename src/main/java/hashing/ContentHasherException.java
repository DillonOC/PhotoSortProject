package hashing;

public class ContentHasherException extends Exception {

    public ContentHasherException(String errorMessage) {
        super(errorMessage);
    }   

    public ContentHasherException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }   
}
