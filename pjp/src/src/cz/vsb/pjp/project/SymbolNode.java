package cz.vsb.pjp.project;

import cz.vsb.uti.sch110.automata.*;

/**
 * Automata state (Node) containing information about symbol being accepted in accepting state
 */
public class SymbolNode extends Node {
    protected Symbol symbol;

    public SymbolNode(String name, Symbol symbol) {
        super(name);
        this.symbol = symbol;
    }

    public Symbol getSymbol() {
        return symbol;
    }
}