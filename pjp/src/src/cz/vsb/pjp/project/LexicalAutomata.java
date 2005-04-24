package cz.vsb.pjp.project;

import cz.vsb.uti.sch110.automata.*;

import java.util.*;
import java.io.*;

/**
 * @author Vladimir Schafer - 20.4.2005 - 15:45:26
 */
public class LexicalAutomata {

    private KAutomat ka;
    private StringBuffer sb = new StringBuffer();
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private LinkedList<Symbol> fronta = new LinkedList<Symbol>();

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

    protected void clearString() {

    }

    public void setSource(InputStream r) {
        br = new BufferedReader(new InputStreamReader(r));
    }

    public Symbol getToken() throws IOException, AutomatException, NoMoreTokensException {
        fillQueue();
        if (fronta.size() == 0) throw new NoMoreTokensException();
        return fronta.poll();
    }

    public boolean hasTokens() throws IOException, AutomatException {
        fillQueue();
        return fronta.size() > 0;
    }

    private void fillQueue() throws IOException, AutomatException {
        if (fronta.size() > 0) return;
        StringBuffer tmp = new StringBuffer();
        while (fronta.size() == 0) {
            boolean found = false;
            char act;

            String next = br.readLine();
            if (next != null)
                tmp.append(next);
            else
                return;

            for (int i = 0; i < tmp.length(); i++) {
                act = tmp.charAt(i);
                if (Character.isWhitespace(act)) {
                    if (found == true) {
                        found = false;
                        Symbol sym = getSymbol();
                        if (sym != null) fronta.add(sym);
                    }
                    continue;
                }

                if (addChar(act)) {
                    found = true;
                } else if (found == true) {
                    found = false;
                    i--;
                    fronta.add(getPushBackSymbol());
                }
            }
            if (found == true) {
                fronta.add(getSymbol());
            }
            tmp.delete(0, sb.length());
        }
    }

    protected boolean addChar(char a) throws AutomatException {
        sb.append(a);
        Node n = ka.getNodeAfter(sb.toString());
        if (n instanceof SymbolNode)
            return true;
        else
            return false;
    }

    protected Symbol getPushBackSymbol() throws AutomatException {
        sb.deleteCharAt(sb.length() - 1);
        return getSymbol();
    }

    protected Symbol getSymbol() throws AutomatException {
        Node n = ka.getNodeAfter(sb.toString());
        Symbol s = null;
        if (n instanceof SymbolNode) {
            try {
                s = (((SymbolNode) n).getSymbol()).clone();
            } catch (Exception e) {
                e.printStackTrace();
            }
            s.setAtt(sb.toString());
        }
        //clearString();
        sb.delete(0, sb.length());
        return s;
    }

    public String toString() {
        return ka.toString();
    }

    public static void main(String[] args) {
        try {

            InputStream in = new FileInputStream("c:\\test.txt");
            InputStream data = new FileInputStream("c:\\data.txt");
            LexicalAutomata la = PJPLexicalAutomata.getPJPAutomata(in);
            la.setSource(data);

            while (la.hasTokens()) {
                System.out.println(la.getToken());
            }

        } catch (Exception e) {
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