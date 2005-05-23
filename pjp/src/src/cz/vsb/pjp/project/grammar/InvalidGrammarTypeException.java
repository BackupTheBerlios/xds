package cz.vsb.pjp.project.grammar;

/**
 * Invalid grammar type (not LL(1) etc.)
 * @author luk117
 */
public class InvalidGrammarTypeException extends GrammarException {
    InvalidGrammarTypeException(String details) {
        super(details);
    }
}
