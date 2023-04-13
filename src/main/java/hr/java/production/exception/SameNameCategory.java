package hr.java.production.exception;

/**
 *  * Exception class that is thrown if multiple object of class CAtegories were being named the same
 */
public class SameNameCategory extends Exception {
    /**
     * Used to get humanoid messsage to user
     * @param poruka String we pass to be displayed to user
     */
    public SameNameCategory(String poruka) {
        super(poruka);
    }

    /**
     * Used to get cause that throwed exception
     * @param uzrok
     */
    public SameNameCategory(Throwable uzrok) {
        super(uzrok);
    }

    /**
     * Gets both message and cause for throwing exception
     * @param poruka
     * @param uzrok
     */
    public SameNameCategory(String poruka, Throwable uzrok) {
        super(poruka, uzrok);
    }


}