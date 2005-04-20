package cz.vsb.pjp.project;

import cz.vsb.uti.sch110.automata.*;

import java.util.Map;
import java.util.Set;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: vsch
 * Date: 20.4.2005
 * Time: 15:45:26
 * To change this template use File | Settings | File Templates.
 */
public class LexicalAutomata {

    private KAutomat ka;
    private StringBuffer sb = new StringBuffer();

    public LexicalAutomata(char[] charset, KAutomat[] data) throws AutomatException {
        ZNKAutomat zn = new ZNKAutomatSymbol(charset);
        Node start = new Node("start");
        zn.addNode(start);
        zn.setStarting(start);
        for (int i = 0; i < data.length; i++) {
            start.addTransition(new ETransition(data[i].getStartingNode()));
        }
        ka = zn.convertToKA();
    }

    public void clearString() {
        sb.delete(0, sb.length());
    }

    public boolean addChar(char a) throws AutomatException {
        sb.append(a);
        Node n = ka.getNodeAfter(sb.toString());
        if (n instanceof SymbolNode)
            return true;
        else
            return false;
    }

    public Symbol getSymbolPushBack() throws AutomatException {
        sb.deleteCharAt(sb.length() - 1);
        Node n = ka.getNodeAfter(sb.toString());
        Symbol s = null;
        if (n instanceof SymbolNode) {
            s = ((SymbolNode) n).getSymbol();
            s.setAtt(sb.toString());
        }
        clearString();
        return s;
    }

    public String toString() {
        return ka.toString();
    }

    public static void main(String[] args) {
        try {
            char[] charset = new String("abcd10").toCharArray();
            KAutomat ka = new KAutomat(charset);
            Node a = new Node("0");
            Node b = new SymbolNode("1", new Symbol("ident"));
            a.addTransition(new Transition('a', b));
            a.addTransition(new Transition('b', a));
            b.addTransition(new Transition('a', a));
            b.addTransition(new Transition('b', b));
            ka.addNode(a);
            ka.addNode(b);
            ka.setStarting(a);

            KAutomat ka2 = new KAutomat(charset);
            a = new Node("2");
            b = new SymbolNode("3", new Symbol("num"));
            a.addTransition(new Transition('1', b));
            b.addTransition(new Transition('0', b));
            ka2.addNode(a);
            ka2.addNode(b);
            ka2.setStarting(a);

            KAutomat[] data = new KAutomat[2];
            data[0] = ka;
            data[1] = ka2;

            LexicalAutomata la = new LexicalAutomata(new String("abcd10").toCharArray(), data);
            //System.out.println(la);

            String s = "a1000001aaa11abbbbbaa110a";
            boolean found = false;
            int cycle = 0;

            for (int i = 0; i < s.length(); i++) {
                cycle++;
                if (la.addChar(s.charAt(i))) {
                    found = true;
                    cycle = 0;
                } else if (found == true) {
                    found = false;
                    i--;
                    Symbol sym = la.getSymbolPushBack();
                    if (sym != null) System.out.println(sym.getName() + " - " + sym.getAtt());
                } else
                    System.out.println("Error - " + cycle);
            }
        } catch (AutomatException e) {
            e.printStackTrace();
        }
    }

    private class ZNKAutomatSymbol extends ZNKAutomat {
        ZNKAutomatSymbol(char[] charset) {
            super(charset);
        }

        protected void followNodes(Map newPoints, KAutomat kat, Set aktualSet, Node aktualNode) throws AutomatException {
            Set tmp;
            Node node;

            for (int i = 0; i < charset.length; i++) {
                tmp = findNext(aktualSet, charset[i]);
                Symbol symbol = null;
                Iterator iter = tmp.iterator();
                while (iter.hasNext()) {
                    Node n = (Node) iter.next();
                    if (n instanceof SymbolNode) {
                        symbol = ((SymbolNode) n).getSymbol();
                    }
                }

                // Je u¾ stav vytvoøen?
                if ((node = (Node) newPoints.get(tmp)) == null) {

                    if (symbol != null) {
                        node = new SymbolNode(Integer.toString(nextNum++), symbol);
                    } else {
                        node = new Node(Integer.toString(nextNum++));
                    }
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

    }
}