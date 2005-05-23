package cz.vsb.pjp.project.grammar;

/**
 * Syntax error
 * @author luk117
 */
public class SyntaxErrorException extends LineNumberException {
    SyntaxErrorException(String description, int lineNumber) {
        super(description, lineNumber);
    }
}
