package cz.vsb.uti.sch110.automata;

import java.util.*;

/**
 * Deterministický automat
 */
public class KAutomat extends AbstractAutomat {
    /**
     * Základní konstruktor
     *
     * @param charset abeceda
     */
    public KAutomat(char[] charset) {
        super(charset);
    }

    /**
     * Nastaví startovací stav. Lze nastavit pouze jeden
     *
     * @param n
     * @throws AutomatException
     */
    public void setStarting(Node n) throws AutomatException {
        if (start.size() == 1)
            throw new AutomatException("KA can have only one start state");
        else
            super.setStarting(n);
    }

    public Node getStartingNode() {
        Iterator i = start.iterator();
        if (i.hasNext())
            return (Node) i.next();
        else
            return null;
    }

    /**
     * Provede normalizaci automatu
     */
    public void normalize() {
        int i = 1;
        Node n = (Node) start.iterator().next();
        n.name = Integer.toString(i++);
        n.normalized = true;

        LinkedList ll = new LinkedList();
        ll.add(n);

        while (!ll.isEmpty()) {
            n = (Node) ll.removeFirst();
            for (int j = 0; j < charset.length; j++) {
                Node m = findNext(n, charset[j]);

                if (m.normalized == false) {
                    ll.add(m);
                    m.name = Integer.toString(i++);
                    m.normalized = true;
                }
            }
        }
    }

    /**
     * Nalezne následující stav
     *
     * @param act  Poèáteèní stav
     * @param znak Pøechodový znak
     * @return následující stav
     */
    public static Node findNext(Node act, char znak) {
        HashSet tmp = new HashSet();
        tmp.add(act);
        Set s = findNext(tmp, znak);
        return (Node) s.iterator().next();
    }

    /**
     * Zkontroluje, zda dané slovo je automatem pøijato èi nikoliv
     *
     * @param word Zkoumané slovo
     * @return True, pokud automat slovo pøijme, jinak false
     * @throws AutomatException
     */
    public boolean checkWord(String word) throws AutomatException {
        if (end.contains(getNodeAfter(word)))
            return true;
        else
            return false;
    }

    public Node getNodeAfter(String word) throws AutomatException {
        Iterator it = start.iterator();
        Node n;
        if (it.hasNext()) {
            n = (Node) it.next();
        } else
            throw new AutomatException("You must specify starting state first");

        Collection next;
        Vector v = new Vector(6);
        v.add(n);
        for (int i = 0; i < word.length(); i++) {
            Vector temp = new Vector();
            while (!v.isEmpty()) {
                n = (Node) v.remove(0);
                next = n.getTransition(word.charAt(i));

                it = next.iterator();
                while (it.hasNext()) {
                    Node node = (Node) it.next();
                    if (!temp.contains(node)) {
                        temp.add(node);
                    }
                }
            }
            if (temp.isEmpty()) throw new NoTransitionException("No transition defined for char '" + word.charAt(i) + "' (position " + i + ") in word " + word);
            v = temp;
        }
        while (!v.isEmpty()) {
            n = (Node) v.remove(0);
            return n;
        }
        return null;
    }

    public boolean verify() {
        return true;
    }
}