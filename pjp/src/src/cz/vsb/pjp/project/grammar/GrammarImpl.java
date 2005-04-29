package cz.vsb.pjp.project.grammar;

import java.util.*;
import java.io.*;

/**
 * Implementace bezkontextove gramatiky.
 *
 * @author Miroslav.Benes@vsb.cz
 */
public class GrammarImpl implements Grammar {
    /**
     * Umoznuje pruchod pres vsechny terminalni symboly.
     *
     * @return Iterator pres vsechny terminalni symboly.
     */
    public Iterator getTerminals() {
        return terminals.values().iterator();
    }

    /**
     * Zarazeni noveho terminalniho symbolu do seznamu. Pokud
     * uz v seznamu symbol existuje, zarazeni se neprovede.
     *
     * @param name Jmeno symbolu.
     * @return Terminalni symbol.
     */
    public Terminal addTerminal(String name) {
        Terminal t = (Terminal) terminals.get(name);
        if (t == null) {
            t = new Terminal(name);
            terminals.put(name, t);
        }

        return t;
    }

    /**
     * Umoznuje pruchod pres vsechny nonterminalni symboly.
     *
     * @return Iterator pres vsechny nonterminalni symboly.
     */
    public Iterator getNonterminals() {
        return nonterminals.values().iterator();
    }

    /**
     * Zarazeni noveho neterminalniho symbolu do seznamu. Pokud
     * uz v seznamu symbol existuje, zarazeni se neprovede.
     *
     * @param name Jmeno symbolu.
     * @return Neterminalni symbol.
     */
    public Nonterminal addNonterminal(String name) {
        Nonterminal nt = (Nonterminal) nonterminals.get(name);
        if (nt == null) {
            nt = new Nonterminal(name);
            nonterminals.put(name, nt);
        }
        return nt;
    }

    /**
     * Implementace iteratoru prochazejiciho vsechna pravidla gramatiky.
     */
    private class RuleIterator implements Iterator, Cloneable {
        /**
         * Konstruktor.
         */
        public RuleIterator() {
            i_lhs = getNonterminals();
            if (i_lhs.hasNext()) {
                Nonterminal lhs = (Nonterminal) i_lhs.next();
                i_rules = lhs.getRules();
            } else
                i_rules = null;
        }

        /**
         * Test, zda ma iterator k dispozici jeste dalsi prvek.
         *
         * @return Vraci false, pokud volani next() zpusobi vyjimku.
         */
        public boolean hasNext() {
            if (i_rules == null) return false;

            while (!i_rules.hasNext()) {
                if (i_lhs.hasNext()) {
                    Nonterminal lhs = (Nonterminal) i_lhs.next();
                    i_rules = lhs.getRules();
                } else {
                    i_rules = null;
                    return false;
                }
            }
            return true;
        }

        /**
         * Vyber dalsiho prvku z kolekce.
         *
         * @return Dalsi prvek kolekce.
         */
        public Object next() {
            if (!hasNext())
                throw new NoSuchElementException();
            return i_rules.next();
        }

        /**
         * Zruseni prvku, na ktery ukazuje iterator.
         * Tato funkce neni podporovana.
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Object clone() {
            return new RuleIterator();
        }

        private Iterator i_lhs;
        private Iterator i_rules;
    }

    /**
     * Umozni pruchod pres vsechna pravidla gramatiky.
     *
     * @return Iterator pres vsechna pravidla gramatiky.
     */
    public Iterator getRules() {
        return new RuleIterator();
    }

    /**
     * Prida dalsi pravidlo do gramatiky.
     *
     * @param rule Pridavane pravidlo.
     */
    public void addRule(Rule rule) {
        rule.getLHS().addRule(rule);
    }

    /**
     * Vrati startovaci nonterminal.
     *
     * @return Startovaci nonterminal.
     */
    public Nonterminal getStartNonterminal() {
        return startNonterminal;
    }

    /**
     * Vrati startovaci nonterminal.
     *
     * @param start Startovaci nonterminal.
     */
    public void setStartNonterminal(Nonterminal start) {
        startNonterminal = start;
    }

    /**
     * Vypis gramatiky na vystup.
     *
     * @param out Vystupni stream.
     */
    public void dump(PrintStream out) {
        out.print("Terminalni symboly:");
        Iterator i_t = getTerminals();
        while (i_t.hasNext()) {
            Terminal t = (Terminal) i_t.next();
            out.print(" " + t.getName());
        }
        out.println();

        out.print("Neterminalni symboly:");
        Iterator i_nt = getNonterminals();
        while (i_nt.hasNext()) {
            Nonterminal nt = (Nonterminal) i_nt.next();
            out.print(" " + nt.getName());
        }
        out.println();

        out.println("Startovaci nonterminal: " + getStartNonterminal().getName());

        out.println("Pravidla:");
        Iterator i_rules = getRules();
        for (int i = 1; i_rules.hasNext(); i++) {
            Rule rule = (Rule) i_rules.next();
            out.print("[" + i + "] " + rule.getLHS().getName() + " -> ");

            Collection<Symbol> rhs = rule.getRHS();
            for (Symbol s : rhs)
                out.print(s.getName() + " ");

            out.println();
        }

    }

    public static final EmptyTerminal EMPTY_TERMINAL = new EmptyTerminal();

/*
	public void dump(PrintStream out)
	{
		Iterator i_nt = getNonterminals();
		while( i_nt.hasNext() ) {
			Nonterminal nt = (Nonterminal)i_nt.next();
			out.println(nt.getName()+":");

			Iterator i_rules = nt.getRules();
			while( i_rules.hasNext() ) {
				Rule rule = (Rule)i_rules.next();
				out.print("\t");
				Iterator i_rhs = rule.getRHS();
				while( i_rhs.hasNext() ) {
					Symbol s = (Symbol)i_rhs.next();
					out.print(s.getName()+" ");
				}
				out.println();
				out.print( i_rules.hasNext() ? "|" : ";");
			}
			out.println();
		}

	}
*/

    /**
     * Seznam terminalnich symbolu.
     */
    private Map terminals = new TreeMap();

    /**
     * Seznam neterminalnich symbolu.
     */
    private Map nonterminals = new TreeMap();

    /**
     * Startovaci nonterminal.
     */
    private Nonterminal startNonterminal;
}
