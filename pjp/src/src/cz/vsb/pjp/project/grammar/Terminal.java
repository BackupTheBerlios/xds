package cz.vsb.pjp.project.grammar;

/**
 * Reprezentace terminalniho symbolu.
 *
 * @author Miroslav.Benes@vsb.cz
 */
public class Terminal extends Symbol {
    protected String map;
    /**
     * Vytvori instanci terminalniho symbolu.
     *
     * @param name Jmeno symbolu.
     */
    public Terminal(String name) {
        super(name);

        if (name.contains("@")) {
            this.name = name.split("@")[0];
            map = name.split("@")[1];
        }
    }

    public String getMapString() {
        return map;
    }
}