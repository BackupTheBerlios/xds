package cz.vsb.pjp.project.grammar;

import cz.vsb.pjp.project.*;
import cz.vsb.pjp.project.grammar.client.StackCodeProcessor;
import cz.vsb.uti.sch110.automata.AutomatException;

import java.io.*;

/**
 * Ukazkovy priklad pro praci s gramatikou
 */
public class Test {

    public static void main(String[] args) {
        // vytvorime prazdnou gramatiku
        Grammar grammar;

        if (args.length != 3) {
            System.err.println("Specify 3 parameters please.");
            return;
        }

        // nacteme jeji obsah ze souboru
        try {
            GrammarReader inp = new GrammarReader(new FileReader(args[1]));
            grammar = inp.read();
            GrammarOps go = new GrammarOps(grammar);

            // load and prepare grammar

            DecompositionTable d = go.getDecompositionTable();
            StackAutomata sa = new StackAutomata(d);
            StackCodeProcessor proc = new StackCodeProcessor();

            // set up lexical analyser

            InputStream in = new FileInputStream(args[0]);
            LexicalAutomata la = PJPLexicalAutomata.getPJPAutomata(in);

            InputStream data = new FileInputStream(args[2]);
            la.setSource(data);

            sa.processWord(la, grammar, go, proc);
        } catch (GrammarParseException e) {
            // chyba pri analyze textu gramatiky
            System.err.println("Grammar parse exception: Line " + e.getLineNumber() + " - " + e.getMessage());
            return;
        } catch (InvalidGrammarTypeException e) {
            System.err.println("Can't build decomposition table: not an LL1 grammar?");
            System.err.println("--- ambiguous table key was " + e.getMessage());
            return;
        } catch (IOException e) {
            // chyba vstupu/vystupu
            System.err.println("I/O Error: " + e.getMessage());
            return;
        } catch (SyntaxErrorException e) {
            System.err.println("SYNTAX ERROR at line " + e.getLineNumber() + ": " + e.getMessage());
        } catch (AutomatException e) {
            System.err.println("Lexical automaton has reported an exception: " + e.getMessage());
        }
    }
}
