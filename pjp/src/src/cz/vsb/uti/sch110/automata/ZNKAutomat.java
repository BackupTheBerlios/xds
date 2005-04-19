package cz.vsb.uti.sch110.automata;

import cz.vsb.uti.sch110.automata.*;

import java.util.*;

/**
    Nedeterministický automat
 */
public class ZNKAutomat extends AbstractAutomat {
    public ZNKAutomat(char[] charset) {
        super(charset);
    }

    public boolean checkWord(String word) throws AutomatException {
        Vector v = findStartingNodes();
        Node n;
        Set next;
        Iterator it;

        if (v.size() == 0) throw new AutomatException("You must specify starting state first");

        // Ve vektoru v jsou vzdy dosazene stavy po predhozim prechodu, na startu obsahuje pocatecni stavy. Vektor temp obsahuje stavy dosazene aktualnim pismenem
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
            if (temp.isEmpty()) return false;
            v = temp;
        }
        while (!v.isEmpty()) {
            n = (Node) v.remove(0);
            if (end.contains(n)) return true;
        }
        return false;
    }

    /**
     * Vraci vector vsech stavu dosazitelnych ze startovnich pomoci E prechodu
      * @return
     * @throws AutomatException
     */
    private Vector findStartingNodes() throws AutomatException {
        Iterator it = start.iterator();
        // Startovaci Nody
        Vector v = new Vector(6);
        Set next;
        Node n;
        while (it.hasNext()) {
            next = new HashSet();
            n = (Node) it.next();
            if (!v.contains(n)) v.add(n);
            n.addETransition(next);
            Iterator it2 = next.iterator();
            while (it2.hasNext()) {
                Node n2 = (Node) it2.next();
                if (!v.contains(n2)) v.add(n2);
            }

        }
        return v;
    }

    /**
     * Vrací odpovídající deterministický automat v normovaném tvaru
     * @return
     * @throws AutomatException
     */
    public KAutomat convertToKA() throws AutomatException {
        KAutomat kat = new KAutomat(charset);
        // Mapuje na nove Nody ze starych
        HashMap hm = new HashMap();

        Vector v = findStartingNodes();
        Set akt = new HashSet(start);
        akt.addAll(v);

        // Stav, ze ktereho hledam okoli
        Node n = new Node(Integer.toString(nextNum++));
        hm.put(akt, n);
        kat.addNode(n);
        kat.setStarting(n);

        if (isAccepting(akt)) {
            kat.setAccepting(n);
        }

        followNodes(hm, kat, akt, n);
        kat.normalize();
        nextNum = 1;

        return kat;
    }

    private int nextNum = 1;
    /**
     *
     * @param newPoints Mapování podmno¾iny stavù ZNKA na jeden stav KA
     * @param kat Nový automat
     * @param aktualSet Aktuální mno¾ina stavù u které zkoumáme, kam se lze dostat
     * @param aktualNode Stav v KA odpovídající aktualSet (¹el by získat i z newPoints)
     * @throws AutomatException
     */
    private void followNodes(Map newPoints, KAutomat kat, Set aktualSet, Node aktualNode) throws AutomatException {
        Set tmp;
        Node node;

        for (int i = 0; i < charset.length; i++) {
            tmp = findNext(aktualSet, charset[i]);

            // Je u¾ stav vytvoøen?
            if ((node = (Node) newPoints.get(tmp)) == null) {
                node = new Node(Integer.toString(nextNum++));
                kat.addNode(node);
                if (isAccepting(tmp)) {
                    kat.setAccepting(node);
                }
                aktualNode.addTransition(new Transition(charset[i], node));
                newPoints.put(tmp, node);
                followNodes(newPoints, kat, tmp, node);
            } else {
                aktualNode.addTransition(new Transition(charset[i], node));
            }
        }
    }

    public boolean verify() {
        return true;
    }
}