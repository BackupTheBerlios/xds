package cz.vsb.uti.sch110.automata;

/**
 * @author Vladimir Schafer - 18.5.2005 - 10:50:51
 */
public class NoTransitionException extends AutomatException {
    public NoTransitionException(String s) {
        super(s);
    }

    public NoTransitionException() {
        super();
    }
}
