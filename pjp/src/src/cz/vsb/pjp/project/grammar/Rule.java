package cz.vsb.pjp.project.grammar;

import cz.vsb.pjp.project.grammar.Nonterminal;

import java.util.*;

/**
 * Reprezentace pravidla gramatiky
 */
public class Rule {
    /**
     * Vytvori instanci pravidla.
     *
     * @param lhs Nonterminal na leve strane pravidla.
     */
    public Rule(Nonterminal lhs) {
        this.lhs = lhs;
    }

    /**
     * Vrati nonterminal na leve strane pravidla
     *
     * @return Nonterminal na leve strane pravidla.
     */
    public Nonterminal getLHS() {
        return lhs;
    }

    /**
     * Umozni pruchod symboly na prave strane pravidla
     *
     * @return Vrati iterator pres vsechny symboly na prave strane pravidla.
     */
    public Collection<Symbol> getRHS() {
        return rhs;
    }

    /**
     * Prida dalsi symbol na konec prave strany pravidla
     *
     * @param s Pridavany symbol.
     */
    public void addSymbol(Symbol s) {
        rhs.add(s);
    }

    /**
     * Leva strana pravidla
     */
    private Nonterminal lhs;

    /**
     * Prava strana pravidla
     */
    private List<Symbol> rhs = new ArrayList<Symbol>();

    public String toString() {
        return getLHS() + "->" + getRHS();
    }
}
