package cz.vsb.pjp.project.grammar;

import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Set;

/**
 * Decomposition table implementation
 *
 * @author luk117
 */
public class DecompositionTable extends TreeMap<Pair<Terminal, Nonterminal>, Rule> {
    public TreeSet<Nonterminal> getLeavesForNonterminal(Nonterminal t) {
        Set<Pair<Terminal, Nonterminal>> tableSet = keySet();
        TreeSet<Nonterminal> terminalTree = new TreeSet<Nonterminal>();

        for (Pair<Terminal, Nonterminal> setMember: tableSet) {
            if (setMember.second() == t)
                terminalTree.add(t);
        }

        return terminalTree;
    }
}
