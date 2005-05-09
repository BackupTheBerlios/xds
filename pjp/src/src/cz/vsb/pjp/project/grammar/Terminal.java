package cz.vsb.pjp.project.grammar;

import cz.vsb.pjp.project.grammar.Symbol;

/**
 * Reprezentace terminalniho symbolu.
 *
 * @author Miroslav.Benes@vsb.cz
 */
public class Terminal extends Symbol {
    /**
     * Vytvori instanci terminalniho symbolu.
     *
     * @param name Jmeno symbolu.
     */
    public Terminal(String name) {
        super(name);
    }
}