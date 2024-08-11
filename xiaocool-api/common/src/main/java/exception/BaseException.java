package exception;


public class BaseException extends RuntimeException {
    private String code;

    public BaseException(String code,String msg){
        super(msg);
        this.code = code;

    }
}
