package cz.vsb.uti.sch110.automata;

/**
 * Obecn� v�jimka automatu 
 */
public class AutomatException extends Exception {
    AutomatException(String s) {
        super(s);
    }

    AutomatException() {
        super();
    }
}
