package cz.vsb.pjp.project.grammar;

import cz.vsb.pjp.project.grammar.*;

import java.io.*;

/**
 * Ukazkovy priklad pro praci s gramatikou
 */
public class Test {

    public static void main(String[] args) {
        // vytvorime prazdnou gramatiku
        Grammar grammar;

        // nacteme jeji obsah ze souboru
        try {
            GrammarReader inp = new GrammarReader(new FileReader(args[0]));
            grammar = inp.read();
        } catch (GrammarException e) {
            // chyba pri analyze textu
            System.err.println("Error(" + e.getLineNumber() + ") " + e.getMessage());
            return;
        } catch (IOException e) {
            // chyba vstupu/vystupu
            System.err.println("Error: " + e.getMessage());
            return;
        }

        // muzeme opsat, co jsme precetli

//grammar.dump(System.out);

        GrammarOps go = new GrammarOps(grammar);

        DecompositionTable d = go.getDecompositionTable();
        StackAutomata sa = new StackAutomata(d);

        // vypiseme mnozinu nonterminalu generujicich prazdne slovo
        /*Set empty = go.getEmptyNonterminals();
        Iterator i_e = empty.iterator();
        while( i_e.hasNext() ) {
            Nonterminal nt = (Nonterminal)i_e.next();
            System.out.print(nt.getName()+" ");
        }
        System.out.println(); */

    }
}
