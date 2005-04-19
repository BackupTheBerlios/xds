package cz.vsb.uti.sch110.automata;

/**
 * Obecná výjimka automatu 
 */
public class AutomatException extends Exception {
    AutomatException(String s) {
        super(s);
    }

    AutomatException() {
        super();
    }
}
