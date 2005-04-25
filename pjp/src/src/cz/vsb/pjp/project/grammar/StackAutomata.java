package cz.vsb.pjp.project.grammar;

import cz.vsb.pjp.project.grammar.DecompositionTable;
import cz.vsb.pjp.project.grammar.Rule;

import java.util.Stack;

/**
 * Date: 25.4.2005
 * Time: 13:14:52
 */
public class StackAutomata {
    protected DecompositionTable table;

    public StackAutomata(DecompositionTable table) {
        this.table = table;
    }

    public Stack<Rule> processWord(Stack<Terminal> left, Stack<Symbol> right) {
        Stack<Rule> outputStack = new Stack<Rule>();

/*
        ** jeste neni hotovo **
        
        Terminal a = left.pop();
        Symbol b = right.peek();

        if (b instanceof Nonterminal) {
            Rule r = table.get(new Pair<Terminal, Nonterminal>(a, (Nonterminal)b));

            right.pop();

            outputStack.push(r);

            for (Symbol s: r.getRHS())
                right.push(s);

        } else if (b instanceof Terminal) {
            if (a.compareTo((Terminal)b) == 0) {
                right.pop();
            } else
                System.out.println("Syntax error");//throw new blah("Syntax error");
        } else
            System.out.println("Syntax error");
*/
        return outputStack;
    }


}
