package cz.vsb.uti.sch110.automata;

import java.util.*;

/**
Obecná implementace automatu
 */
public abstract class AbstractAutomat {
    /**
     * Stavy
     */
    protected HashSet nodes;
    /**
     * Poèáteèní stavy
     */
    protected HashSet start;
    /**
     * Pøijímací stavy
     */
    protected HashSet end;
    /**
     * Abeceda
     */
    protected char[] charset;

    public AbstractAutomat(char[] charset) {
        Arrays.sort(charset);
        this.charset = charset;
        this.nodes = new HashSet(8);
        this.start = new HashSet(4);
        this.end = new HashSet(4);
    }

    /**
     * Pøidává nový stav
     * @param n stav
     * @return Vrací true, pokud byl stav úspì¹nì pøidán
     * @throws cz.vsb.uti.sch110.automata.AutomatException Pokud ji¾ existuje stav se stejným názvem
     */
    public boolean addNode(Node n) throws AutomatException {
        if (!nodes.contains(n))
            return nodes.add(n);
        else
            throw new AutomatException("State already exists");
    }

    /**
     * Vrací stav se zadaným názvem
     * @param s název
     * @return
     */
    public Node getNode(String s) {
        Node n;
        Iterator i = nodes.iterator();
        while (i.hasNext()) {
            n = (Node) i.next();
            if (n.getName().equals(s)) return n;
        }

        return null;
    }

    /**
     * Oznaèí stav jako startovací
     * @param n
     * @throws AutomatException Pokud stav neexistuje
     */
    public void setStarting(Node n) throws AutomatException {
        if (nodes.contains(n)) {
            start.add(n);
        } else {
            throw new AutomatException("Trying to set non existing state as starting");
        }
    }

    /**
     * Vraci true, pokud mnozina obsahuje alespon jeden akceptujici stav
     * @param s
     * @return
     */
    public boolean isAccepting(Set s) {
        Node n;
        Iterator i = s.iterator();
        while (i.hasNext()) {
            n = (Node)i.next();
            if (end.contains(n)) return true;
        }
        return false;
    }

    /**
     * Odznaèí stav jako startovací
     * @param n
     * @throws AutomatException Pokud stav neexistuje
     */
    public void unsetStarting(Node n) throws AutomatException {
        if (start.contains(n)) start.remove(n);
        else throw new AutomatException("Trying to unset non existing state");
    }

    /**
     * Nastaví stav n jako pøijímací
     * @param n
     * @throws AutomatException Pokud stav neexistuje - je tøeba jej nejdøíve pøidat
     */
    public void setAccepting(Node n) throws AutomatException {
        if (nodes.contains(n)) {
            end.add(n);
        } else {
            throw new AutomatException("Trying to set non existing state as accepting");
        }
    }

    /**
     * Odznaèí stav jako pøijímací
     * @param n Stav
     * @throws AutomatException Pokud automat ¾ádný stav n neobsahuje
     */
    public void unsetAccepting(Node n) throws AutomatException {
        if (end.contains(n)) end.remove(n);
        else throw new AutomatException("Trying to unset non existing state");
    }

    /**
     * Výpis ve formátu dle http://www.cs.vsb.cz/hlineny/vyuka/UTI-referaty/UTI-autpr1.html
     * @return
     */
    public String toString() {
        StringBuffer sb = new StringBuffer(100);
        Vector v;
        v = new Vector(nodes.size());
        sb.append("Q: ");
        v.addAll(nodes);
        Collections.sort(v);
        for (int j = 0; j < v.size(); j++) {
            sb.append(v.get(j)).append(" ");
        }

        sb.append("\nE:");
        for (int j = 0; j < charset.length; j++) {
            sb.append(" ").append(charset[j]);
        }
        v.removeAllElements();

        sb.append("\nI: ");
        v.addAll(start);
        Collections.sort(v);
        for (int j = 0; j < v.size(); j++) {
            sb.append(v.get(j)).append(" ");
        }
        v.removeAllElements();

        sb.append("\nF: ");
        v.addAll(end);
        Collections.sort(v);
        for (int j = 0; j < v.size(); j++) {
            sb.append(v.get(j)).append(" ");
        }
        v.removeAllElements();
        
        sb.append("\n");
        Iterator i = nodes.iterator();
        Node tmp;
        Collection tmpcol;
        while (i.hasNext()) {
            tmp = (Node)i.next();
            tmpcol = tmp.transitions;
            Iterator it = tmpcol.iterator();
            while (it.hasNext()) {
                v.add(new StringBuffer().append("d: ").append(tmp).append(" ").append(it.next()).append("\n").toString());
            }
        }
        Collections.sort(v);
        for (int j = 0; j < v.size(); j++) {
            sb.append(v.get(j));
        }
        return sb.toString();
    }

    /**
     * Vrací true, pokud automat dané slovo pøijme, jinak false
     * @param word
     * @return
     * @throws AutomatException
     */
    public abstract boolean checkWord(String word) throws AutomatException;

    /**
     * Ovìøuje korektnost automatu
     * @return True v pøípadì, ¾e je automat v poøádku, jinak false
     */
    public abstract boolean verify();

    /**
     * Vrací mno¾inu v¹ech stavù dostupných ze stavù ve vlo¾ené mno¾inì konkrétním znakem
     * @param act Mno¾ina, ze které se má prohledávat
     * @param znak Pøechodový znak
     * @return Mno¾ina dostupných stavù
     */
    public static Set findNext(Set act, char znak) {
        HashSet tmp = new HashSet();
        Iterator it = act.iterator();
        Node m;
        while (it.hasNext()) {
            m = (Node) it.next();
            tmp.addAll(m.getTransition(znak));
        }
        return tmp;
    }
}