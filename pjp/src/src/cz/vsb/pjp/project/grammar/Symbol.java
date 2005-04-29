package cz.vsb.pjp.project.grammar;

/**
 * Spolecna bazova trida pro neterminalni a terminalni symboly.
 *
 * @author Miroslav.Benes@vsb.cz
 */
public abstract class Symbol implements Comparable {
    /**
     * Vytvori instanci symbolu gramatiky.
     *
     * @param name Jmeno symbolu.
     */
    public Symbol(String name) {
        this.name = name;
    }

    /**
     * Vrati jmeno symbolu
     *
     * @return Jmeno symbolu.
     */
    public String getName() {
        return name;
    }

    /**
     * Porovna dva symboly podle jmena
     *
     * @param obj Objekt, s nimz se porovnava. Musi to byt hodnota kompatibilni s typem Symbol.
     * @return Vrati vysledek porovnani jmen obou symbolu jako hodnotu <, = nebo > 0.
     */
    public int compareTo(Object obj) {
        return name.compareTo(((Symbol) obj).name);
    }

    public String toString() {
        return name;
    }

    public boolean equals(Object obj) {
        return name.equals(((Symbol) obj).name);
    }

    /**
     * Jmeno symbolu.
     */
    private String name;
}
