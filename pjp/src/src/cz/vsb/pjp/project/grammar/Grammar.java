package cz.vsb.pjp.project.grammar;

import java.util.Iterator;
import java.io.*;

/**
 * Rozhrani pro praci s bezkontextovou gramatikou.
 *
 * @author Miroslav.Benes@vsb.cz
 */
public interface Grammar {
    /**
     * Umoznuje pruchod mnozinou vsech neterminalnich symbolu.
     *
     * @return Vraci iterator pres hodnoty typu Nonterminal.
     */
    Iterator getNonterminals();

    /**
     * Umoznuje pruchod mnozinou vsech terminalnich symbolu.
     *
     * @return Vraci iterator pres hodnoty typu Terminal.
     */
    Iterator getTerminals();

    /**
     * Umoznuje pruchod mnozinou vsech pravidel.
     * Pro pruchod pravidly prislusnymi ke konkretnimu nonterminalu
     * pouzijte metodu getRules typu Nonterminal.
     *
     * @return Vraci iterator pres hodnoty typu Rule.
     */
    Iterator getRules();

    /**
     * Vrati startpvaci nonterminal gramatiky.
     *
     * @return Startovaci nonterminal.
     */
    Nonterminal getStartNonterminal();

    /**
     * Opise gramatiku na vystup.
     *
     * @param out Vystupni stream.
     */
    void dump(PrintStream out);
}
