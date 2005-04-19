package cz.vsb.uti.sch110.automata;

import cz.vsb.uti.sch110.automata.AutomatException;
import cz.vsb.uti.sch110.automata.Node;

/**
    P�echod mezi stavy
 */
public class Transition {
    char letter;
    Node target;

    /**
     * Z�kladn� konstruktor
     * @param letter p�echodov� znak
     * @param target c�lov� stav
     */
    public Transition(char letter, Node target) {
        this.letter = letter;
        this.target = target;
    }

    /**
     * Vrac� p�echodov� znak
     * @return
     * @throws AutomatException
     */
    public char getLetter() throws AutomatException {
        return letter;
    }

    /**
     * Vrac� c�lov� stav
     * @return
     */
    public Node getTarget() {
        return target;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Transition) {
            Transition t = (Transition)obj;
            try {
            if ((letter == t.getLetter()) && (target == t.getTarget())) return true;
            else return false;
            } catch (AutomatException e) {
                e.printStackTrace();
                return false;
            }
        } else return false;
    }

    public int hashCode() {
        return target.hashCode() + (letter%5);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(letter).append(" ").append(target);
        return sb.toString();
    }
}