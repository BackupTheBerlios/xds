package cz.vsb.uti.sch110.automata;

import java.util.*;

/**
Obecn� implementace automatu
 */
public abstract class AbstractAutomat {
    /**
     * Stavy
     */
    protected HashSet nodes;
    /**
     * Po��te�n� stavy
     */
    protected HashSet start;
    /**
     * P�ij�mac� stavy
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
     * P�id�v� nov� stav
     * @param n stav
     * @return Vrac� true, pokud byl stav �sp�n� p�id�n
     * @throws cz.vsb.uti.sch110.automata.AutomatException Pokud ji� existuje stav se stejn�m n�zvem
     */
    public boolean addNode(Node n) throws AutomatException {
        if (!nodes.contains(n))
            return nodes.add(n);
        else
            throw new AutomatException("State already exists");
    }

    /**
     * Vrac� stav se zadan�m n�zvem
     * @param s n�zev
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
     * Ozna�� stav jako startovac�
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
     * Odzna�� stav jako startovac�
     * @param n
     * @throws AutomatException Pokud stav neexistuje
     */
    public void unsetStarting(Node n) throws AutomatException {
        if (start.contains(n)) start.remove(n);
        else throw new AutomatException("Trying to unset non existing state");
    }

    /**
     * Nastav� stav n jako p�ij�mac�
     * @param n
     * @throws AutomatException Pokud stav neexistuje - je t�eba jej nejd��ve p�idat
     */
    public void setAccepting(Node n) throws AutomatException {
        if (nodes.contains(n)) {
            end.add(n);
        } else {
            throw new AutomatException("Trying to set non existing state as accepting");
        }
    }

    /**
     * Odzna�� stav jako p�ij�mac�
     * @param n Stav
     * @throws AutomatException Pokud automat ��dn� stav n neobsahuje
     */
    public void unsetAccepting(Node n) throws AutomatException {
        if (end.contains(n)) end.remove(n);
        else throw new AutomatException("Trying to unset non existing state");
    }

    /**
     * V�pis ve form�tu dle http://www.cs.vsb.cz/hlineny/vyuka/UTI-referaty/UTI-autpr1.html
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
     * Vrac� true, pokud automat dan� slovo p�ijme, jinak false
     * @param word
     * @return
     * @throws AutomatException
     */
    public abstract boolean checkWord(String word) throws AutomatException;

    /**
     * Ov��uje korektnost automatu
     * @return True v p��pad�, �e je automat v po��dku, jinak false
     */
    public abstract boolean verify();

    /**
     * Vrac� mno�inu v�ech stav� dostupn�ch ze stav� ve vlo�en� mno�in� konkr�tn�m znakem
     * @param act Mno�ina, ze kter� se m� prohled�vat
     * @param znak P�echodov� znak
     * @return Mno�ina dostupn�ch stav�
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