package cz.vsb.pjp.project.grammar;

/**
 * Date: 29.4.2005
 * Time: 0:52:56
 */
public class SyntaxErrorException extends LineNumberException {
    SyntaxErrorException(String description, int lineNumber) {
        super(description, lineNumber);
    }
}
