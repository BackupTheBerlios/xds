package cz.vsb.pjp.project.grammar;

/**
 * Date: 29.4.2005
 * Time: 0:54:50
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
