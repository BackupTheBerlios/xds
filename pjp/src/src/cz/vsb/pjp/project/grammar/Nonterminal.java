package cz.vsb.pjp.project.grammar;

import java.util.*;

/**
 * Reprezentace neterminalniho symbolu.
 *
 * @author Miroslav.Benes@vsb.cz
 */
public class Nonterminal extends Symbol {
    /**
     * Vytvori instanci nonterminalniho symbolu.
     *
     * @param name Jmeno symbolu.
     */
    public Nonterminal(String name) {
        super(name);
    }

    /**
     * Umoznuje pruchod vsemi pravidly pro prislusny nonterminal
     *
     * @return Vraci iterator pres pravidla, na jejichz leve strane je aktualni nonterminal.
     */
    public Iterator getRules() {
        return rules.iterator();
    }

    /**
     * Prida nove pravidlo do seznamu
     */
    void addRule(Rule rule) {
        // assert rule.getLHS() == this;
        rules.add(rule);
    }

    /**
     * Seznam pravidel pro tento nonterminal
     */
    private List rules = new ArrayList();
}
