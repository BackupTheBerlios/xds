package cz.vsb.pjp.project.grammar;

/**
 * General grammar exception
 * 
 * @author luk117
 */
public class GrammarException extends Exception {
    GrammarException(String message) {
        super(message);
    }

    GrammarException() {
        super();
    }
}
