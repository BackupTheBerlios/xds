package cz.vsb.uti.sch110.automata;

/**
 * Obecná výjimka automatu
 */
public class AutomatException extends Exception {
    public AutomatException(String s) {
        super(s);
    }

    public AutomatException() {
        super();
    }
}
