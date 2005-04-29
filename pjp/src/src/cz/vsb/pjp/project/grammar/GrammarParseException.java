package cz.vsb.pjp.project.grammar;

/**
 * Vyjimka zpusobena chybou pri cteni gramatiky
 *
 * @author Miroslav.Benes@vsb.cz
 */
public class GrammarParseException extends LineNumberException {
    GrammarParseException(String str, int line) {
        super(str, line);
    }
}
