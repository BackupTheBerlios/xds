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
        ErrorNode start = new ErrorNode();
        zn.addNode(start);
        zn.setStarting(start);
        start.addTransition(new Transition('\'', start));
        for (int i = 0; i < data.length; i++) {
            start.addTransition(new ETransition(data[i].getStartingNode()));
        }
        ka = zn.convertToKA();
    }

    public void setSource(InputStream r) {
        br = new BufferedReader(new InputStreamReader(r));
    }

    // TODO Mit moznost cist cisla radku
    // TODO Vracet Terminal misto Symbolu
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
            if (next != null) {
                next = next.toLowerCase();
                tmp.append(next);
            } else
                return;

            for (int i = 0; i < tmp.length(); i++) {
                act = tmp.charAt(i);

                addChar(act);
                Node n = ka.getNodeAfter(sb.toString());
                if (n instanceof SymbolNode)
                    found = true;
                else if (n instanceof StartNode) {
                    deleteChar();
                } else if (n instanceof ErrorNode) {
                    if (found == true) {
                        do {
                            deleteChar();
                            i--;
                        } while (!(ka.getNodeAfter(sb.toString()) instanceof SymbolNode));
                        fronta.add(getSymbol());
                    }
                }
            }
            if (found == true) {
                fronta.add(getSymbol());
            }
            tmp.delete(0, sb.length());
        }
    }

    protected void addChar(char a) throws AutomatException {
        sb.append(a);
    }

    protected void deleteChar() throws AutomatException {
        sb.deleteCharAt(sb.length() - 1);
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

            InputStream in = new FileInputStream("src//rules.lex");
            InputStream data = new FileInputStream("src//vtest.txt");
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

        public KAutomat convertToKA() throws AutomatException {
            KAutomat kat = new KAutomat(charset);
            // Mapuje na nove Nody ze starych
            HashMap hm = new HashMap();

            Vector v = findStartingNodes();
            Set akt = new HashSet(start);
            akt.addAll(v);

            // Stav, ze ktereho hledam okoli
            StartNode n = new StartNode();
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

                // Je u� stav vytvo�en?
                if ((node = (Node) newPoints.get(tmp)) == null) {

                    if (symbol != null) {
                        node = new SymbolNode(Integer.toString(nextNum++), symbol);
                    } else if (tmp.size() == 0) {
                        node = new ErrorNode();
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