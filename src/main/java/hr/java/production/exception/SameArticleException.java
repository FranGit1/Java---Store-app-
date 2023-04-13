package hr.java.production.exception;

/**
 * Exception class that is thrown if same article was being added to multiple classes
 */
public class SameArticleException extends Exception {
    /**
     * Used to get humanoid messsage to user
     * @param poruka String we pass to be displayed to user
     */
    public SameArticleException(String poruka) {
        super(poruka);
    }
    /**
     * Used to get cause that throwed exception
     * @param uzrok
     */
    public SameArticleException(Throwable uzrok) {
        super(uzrok);
    }
    /**
     * Gets both message and cause for throwing exception
     * @param poruka
     * @param uzrok
     */
    public SameArticleException(String poruka, Throwable uzrok) {
        super(poruka, uzrok);
    }
}