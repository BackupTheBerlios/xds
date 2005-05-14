package cz.vsb.pjp.project.grammar;

import cz.vsb.pjp.project.LexicalAutomata;
import cz.vsb.uti.sch110.automata.AutomatException;

import java.util.Stack;
import java.util.Iterator;
import java.io.IOException;

/**
 * Date: 25.4.2005
 * Time: 13:14:52
 */
public class StackAutomata {
    protected DecompositionTable table;

    public StackAutomata(DecompositionTable table) {
        this.table = table;
    }

    public Stack<Rule> processWord(LexicalAutomata lex, Grammar g, GrammarOps ops, StackCodeProcessor proc)
            throws SyntaxErrorException, IOException, AutomatException {
        Stack<Rule> outputStack = new Stack<Rule>();

        Stack<Symbol> right = new Stack<Symbol>();

        boolean wantNextLexem = true;
        Terminal term = null;
        cz.vsb.pjp.project.Symbol sym = null;

        DerivationTree tree = null;

        while (true) {
            // some code in stack to process?
            if (right.isEmpty() && tree != null && !tree.root.children.isEmpty()) {
                proc.processCode(tree.getExecuteStack());
            }

            if (right.isEmpty() && lex.hasTokens()) {
                right.add(g.getStartNonterminal());
                tree = new DerivationTree();
            }

            if (!lex.hasTokens())
                break;

            Symbol b = right.peek();

            if (wantNextLexem) {
                try {
                    term = null;
                    if (!lex.hasTokens())
                        term = GrammarImpl.EMPTY_TERMINAL;
                    else
                        sym = lex.getToken();
                } catch (Exception e) {
                    System.err.println("Something in sch110's package went wrong. Maybe it was " + e);
                    e.printStackTrace();
                    return outputStack;
                }

                // convert lex symbol to our Terminal

                if (term == null) {
                    Iterator i = g.getTerminals();

                    while (i.hasNext()) {
                        Terminal t = (Terminal)i.next();

                        // TODO: this is *VERY* bad!
                        // tohle je samozrejme nesmysl, ktery slouzi pouze k otestovani automatu
                        if (t.getName().equals(sym.getName()) || t.getName().equals(sym.getAtt())) {
                            term = t;
                            break;
                        }
                    }

                    if (term == null) {
                        System.out.println("lex symbol [" + sym + "] couldn't be converted to any known terminal. Check the grammar vs. lex rules");
                        break;
                    }
                }

                wantNextLexem = false;
            }

            // now that we have a valid Terminal..

            if (b instanceof Nonterminal) {
                Rule r = table.get(new Pair<Terminal, Nonterminal>(term, (Nonterminal)b));

                // is the decomposition table filled-in at given coordinates?
                if (r == null) {
                    throw new SyntaxErrorException("No entry for "+ new Pair<Terminal, Nonterminal>(term, (Nonterminal)b) +
                            ". Expected one of: " + ops.first(b), -1);
                }

                //System.out.println("Using rule " + sym.getAtt() + ", " + r);

                right.pop();

                outputStack.push(r);

                // build node of derivation tree. Note we must obey given order, because the tree
                // has to be constructed using 'translation' grammar
                for (Symbol sp: r.getRHS()) {
                    Object trans = proc.translate(sp);

                    if (trans != null)
                        tree.addNode(sp, sp instanceof Terminal);
                }

                tree.setValue(sym.getAtt());
                tree.setRule(r);

                tree.advance();

                if (r.getRHS().isEmpty())
                    continue;

                // we have to push it backwards on the top. That's why this hell...
                Object symbols[] = r.getRHS().toArray();
                for (int x=symbols.length-1; x>=0; x--) {
                    right.push((Symbol)symbols[x]);
                }

            } else if (b instanceof Terminal) {
                // on our 'right' stack is Terminal
                // Now we have to decide, if we accept terminal and pop both stacks, or deny it and throw an exception
                // because of syntax error.
                if (term.equals(b)) {
                    right.pop();
                    wantNextLexem = true;
                    //System.out.println("Removed terminal " + term + ": " + sym.getAtt() +". Requesting lex to pass new lexem.");
                } else
                    throw new SyntaxErrorException("Expected only " + b, -1);
            } else
                throw new SyntaxErrorException("Something is totally wrong", -1);
        }

        return outputStack;
    }
}
