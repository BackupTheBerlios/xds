package cz.vsb.uti.sch110.automata;

import cz.vsb.uti.sch110.automata.AutomatException;
import cz.vsb.uti.sch110.automata.ETransition;

import java.util.*;

/**
 * T��da p�edstavuje stav ve stavov�m automatu
 */
public class Node implements Comparable {
    protected String name;
    protected Vector transitions;
    protected boolean normalized = false;

    /**
     *
     * @param name n�zev stavu
     */
    public Node(String name) {
        this.name = name;
        this.transitions = new Vector(4);
    }

    /**
     * P�id� p�echod z tohoto stavu
     * @param tran c�lov� stav a p�echodov� znak
     * @throws AutomatException V�j�mka v sou�asn� dob� nen� vyhazov�na
     */
    public void addTransition(Transition tran) throws AutomatException {
        transitions.add(tran);
    }

    /**
     * Odstran� p�echod
     * @param tran
     */
    public void removeTransition(Transition tran) {
        transitions.remove(tran);
    }

    /**
     * Vraci kolekci vsech nodu dostupnych zadanym znakem a vsech nodu dostupnych z nasledujicich pomoci E prechodu
     * @param znak
     * @return
     */
    public Set getTransition(char znak) {
        HashSet v = new HashSet();
        Transition t;
        for (int i = 0; i < transitions.size(); i++) {
            t = (Transition) transitions.get(i);
            try {
                if (!(t instanceof ETransition)) {
                    if (t.getLetter() == znak) {
                    Node n = t.getTarget();
                    v.add(n);
                    n.addETransition(v);
                    // Pridat stavy dosazitelne pomoci E z Nodu n
                    //v.addAll(c);
                    // Pridat samotny nod n
                    }
                }
            } catch (AutomatException e) {
                continue;
            }
        }
        return v;
    }

    /**
     * Do vlozene mnoziny vklada Nody dostupne pomoci E prechodu z tohoto objektu
     * @param actual
     * @return
     * @throws AutomatException
     */
    protected Set addETransition(Set actual) throws AutomatException {
        Set c = getETransitionTarget();
        Iterator i = c.iterator();
        Node tmp;
        while (i.hasNext()) {
            tmp = (Node)i.next();
            if (!actual.contains(tmp)) {
                actual.add(tmp);
                tmp.addETransition(actual);
            }
        }

        return c;
    }

    /**
     * Vraci kolekci Nodu dostunych jednim prechodem E z tohoto Nodu
     * @return
     */
    private Set getETransitionTarget() {
        HashSet v = new HashSet();
        Transition t;
        for (int i = 0; i < transitions.size(); i++) {
            if (transitions.get(i) instanceof ETransition) v.add(((ETransition)transitions.get(i)).getTarget());
        }
        return v;
    }

    /**
     * Vrac� n�zev stavu
     * @return
     */
    public String getName() {
        return name;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Node) {
            if (name.equals(((Node) obj).name))
                return true;
            else
                return false;
        } else
            return false;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public String toString() {
        return name;
    }

    public int compareTo(Object o) {
        if (!(o instanceof Node)) {
            throw new ClassCastException();
        } else {
            return name.compareTo(((Node)o).name);
        }
    }

}