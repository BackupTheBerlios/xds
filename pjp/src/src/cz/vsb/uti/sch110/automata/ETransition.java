package cz.vsb.uti.sch110.automata;

import cz.vsb.uti.sch110.automata.AutomatException;

/**
    T��da p�edstavuje E p�echod nedeterministick�ho automatu
 */
public class ETransition extends Transition {
    /**
     *
     * @param target C�l p�echodu
     */
    public ETransition(Node target) {
        super('E', target);
    }

    /**
     * Metoda v�dy h�e v�jimku - ��dn� p�smeno pro p�echod neexistuje
     * @return
     * @throws AutomatException
     */
    public char getLetter() throws AutomatException {
        throw new AutomatException("E transition doesn't have letter asssigned");
    }

    public boolean equals(Object obj) {
        if (obj instanceof ETransition) {
            ETransition t = (ETransition)obj;
            if (target == t.getTarget()) return true;
            else return false;
        } else return false;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<E>").append(" ").append(target);
        return sb.toString();
    }
}
