package cz.vsb.pjp.project;

import cz.vsb.pjp.project.recursivegrammar.Grammar;

import java.io.InputStream;
import java.io.FileInputStream;
import java.util.HashSet;

/**
 * @author Vladimir Schafer - 19.5.2005 - 22:30:36
 */
public class Main {
    public static void main(String[] args) {
        try {
            if (args.length > 1) {
                if (args[0].equals("a")) {
                    System.out.println("Using recursive descent: ");
                    InputStream in = new FileInputStream("data//rules.lex");
                    InputStream data = new FileInputStream(args[1]);
                    LexicalAutomata la = PJPLexicalAutomata.getPJPAutomata(in, true);
                    la.setSource(data);

                    Grammar g = new Grammar(la);
                    HashSet<String> follow = new HashSet<String>();
                    follow.add(LexicalAutomata.EOF);
                    g.Statement(follow);
                } else {
                    System.out.println("Using stack automata: ");
                    String[] params = {"data//rules.lex", "data//main-grammar.txt", args[1] };
                    cz.vsb.pjp.project.grammar.Test.main(params);
                }
            } else {
                System.out.println("Usage: java -jar pjp.jar [a | b] sourcefile");
                System.out.println("-----------------------------");
                System.out.println("a - recursive descent");
                System.out.println("b - stack automata");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
