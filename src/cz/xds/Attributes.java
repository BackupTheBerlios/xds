package cz.xds;

/**
 * Parametry polozky souboroveho systemu
 */
public class Attributes {
    private boolean isHidden;
    private boolean isReadOnly;

    public Attributes() {
    }

    /**
     * Zakladni konstruktor, hidden oznacuje skryty soubor, readOnly soubor jen pro cteni.
     *
     * @param isHidden   parametr hidden
     * @param isReadOnly parametr readOnly
     */
    public Attributes(boolean isHidden, boolean isReadOnly) {
        this.isHidden = isHidden;
        this.isReadOnly = isReadOnly;
    }

    public Attributes(String attrString) {
        isReadOnly = attrString.toLowerCase().indexOf('r') != -1;
        isHidden = attrString.toLowerCase().indexOf('h') != -1;
        if (attrString.indexOf('-') != -1) {
            isReadOnly = false;
            isHidden = false;
        }
    }

    /**
     * Vraci bool hodnotu reprezentujici atribut "hidden"
     *
     * @return parametr hidden
     */
    public boolean isHidden() {
        return isHidden;
    }

    /**
     * Vraci bool hodnotu reprezentujici atribut "isReadOnly"
     *
     * @return parametr isReadOnly
     */
    public boolean isReadOnly() {
        return isReadOnly;
    }

    /**
     * Nastavuje parametr hidden
     *
     * @param isHidden hodnota parametru hidden
     */
    public void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    /**
     * Nastavuje parametr readOnly
     *
     * @param isReadOnly hodnota parametru readOnly
     */
    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(isReadOnly ? "r" : "-").append(isHidden ? "h" : "-");

        return sb.toString();
    }
}
