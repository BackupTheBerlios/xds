package cz.vsb.pjp.project.grammar;

import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Set;

/**
 * Date: 25.4.2005
 * Time: 14:37:42
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
