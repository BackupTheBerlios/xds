package cz.vsb.pjp.project.grammar;

/**
 * Exception that carries line number
 * @author luk117
 */
public class LineNumberException extends Exception {
    /**
     * Vytvori instanci vyjimky ohlasujici chybu pri prekladu
     * gramatiky.
     *
     * @param msg    Text chyboveho hlaseni.
     * @param lineNo Cislo zdrojoveho radku s chybou.
     */
    public LineNumberException(String msg, int lineNo) {
        super(msg);
        this.lineNumber = lineNo;
    }

    /**
     * Vrati cislo radku, na kterem byla chyba nalezena
     *
     * @return Cislo radku s chybou
     */
    public int getLineNumber() {
        return lineNumber;
    }

    private int lineNumber;
}
