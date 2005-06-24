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
    private int line = 0;
    private int word = 0;
    private int linew = 0;
    private int wordw = 0;
    public final static String EOF = "@EOF";
    private boolean returnEOF = false;

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

    public LexicalAutomata(char[] charset, KAutomat[] data, boolean ret) throws AutomatException {
        this(charset, data);
        returnEOF = ret;
    }

    public void setSource(InputStream r) {
        br = new BufferedReader(new InputStreamReader(r));
    }

    /**
     * Vraci radek, na kterem se vyskytuje aktualni symbol ve zdrojovem souboru
     *
     * @return
     */
    public int getLine() {
        return linew;
    }

    /**
     * Vraci pozici na radku, na ktere se vyskytuje aktualni symbol ve zdrojovem souboru
     *
     * @return
     */
    public int getPosition() {
        return wordw;
    }

    /**
     * Vraci nasledujici symbol
     *
     * @return
     * @throws IOException
     * @throws AutomatException
     * @throws NoMoreTokensException Pokud symboly dosly :)
     */
    public Symbol getToken() throws IOException, AutomatException, NoMoreTokensException {
        fillQueue();
        if (fronta.size() == 0) throw new NoMoreTokensException();
        Symbol s = fronta.poll();
        linew = s.line;
        wordw = s.pos;
        return s;
    }

    /**
     * True, pokud lze volanim getToken ziskat dalsi symbol, jinak false
     *
     * @return
     * @throws IOException
     * @throws AutomatException
     */
    public boolean hasTokens() throws IOException, AutomatException {
        fillQueue();
        return fronta.size() > 0;
    }

    /**
     * Nacita symboly do fronty
     *
     * @throws IOException
     * @throws AutomatException
     */
    private void fillQueue() throws IOException, AutomatException {
        if (fronta.size() > 0) return;
        StringBuffer tmp = new StringBuffer();
        while (fronta.size() == 0) {
            boolean found = false;
            char act;

            String next = br.readLine();
            if (next != null) {
                tmp.append(next);
                line++;
                word = 0;
            } else {
                if (returnEOF) fronta.add(new Symbol(EOF, EOF));
                return;
            }

            while (sb.length() > 0) {
                deleteChar();
            }

            for (int i = 0; i < tmp.length(); i++) {
                word++;
                act = tmp.charAt(i);

                addChar(act);
                Node n;
                try {
                    n = ka.getNodeAfter(sb.toString());
                } catch (NoTransitionException e) {
                    int len = sb.length() - 1;
                    sb.deleteCharAt(len);
                    sb.append('~');
                    n = ka.getNodeAfter(sb.toString());
                }
                if (n instanceof SymbolNode)
                    found = true;
                else if (n instanceof StartNode) {
                    deleteChar();
                } else if (!(n instanceof ErrorNode)) {
                    found = false;
                } else {

                    // Vyzkouset, jestli nemuze nasledovat libovolny znak - pro komentare a stringy. Je to hnus, ale co se da delat :)
                    int len = sb.length() - 1;
                    char temp = sb.charAt(len);
                    sb.deleteCharAt(len);
                    sb.append('~');
                    n = ka.getNodeAfter(sb.toString());

                    if (!(n instanceof ErrorNode)) {
                        if (n instanceof SymbolNode)
                            found = true;
                        else
                            found = false;
                        continue;
                    } else {
                        sb.deleteCharAt(len);
                        sb.append(temp);
                    }

                    // Posledni znak zpusobil chybu
                    if (sb.length() > 1) {
                        deleteChar();
                        i--;
                        word--;
                    }
                    // Pokud po odmazani posledniho znaku dostanu symbol je to ok, jinak syntax error
                    if (sb.length() > 0 && (ka.getNodeAfter(sb.toString()) instanceof SymbolNode)) {
                        Symbol s = getSymbol();
                        s.setLine(line);
                        // Nastavi zacatek slova ve zdrojaku
                        s.setPos(word - s.getAtt().length());
                        fronta.add(s);
                        found = false;
                    } else {
                        clearBuffer();
                        System.err.println("Lex: syntax error: char " + act + " at line " + line + ", char " + word);
                        Symbol s = new Symbol("error");
                        s.setLine(line);
                        s.setPos(word);
                        fronta.add(s);
                    }
                }
            }
            if (found == true) {
                Symbol s = getSymbol();
                s.setLine(line);
                s.setPos(word - s.getAtt().length());
                fronta.add(s);
            } else if (tmp.length() > 0) {
                clearBuffer();
                System.err.println("Lex: syntax error: char " + tmp.charAt(tmp.length() - 1) + " at line " + line + ", char " + word);
                Symbol s = new Symbol("error");
                s.setLine(line);
                s.setPos(word);
                fronta.add(s);
            }

            tmp.delete(0, tmp.length());
        }
    }

    /**
     * Prida dalsi znak do aktualniho lexemu
     *
     * @param a
     * @throws AutomatException
     */
    protected void addChar(char a) throws AutomatException {
        sb.append(a);
    }

    /**
     * Smaze posledni znak z aktualniho lexemu
     *
     * @throws AutomatException
     */
    protected void deleteChar() throws AutomatException {
        sb.deleteCharAt(sb.length() - 1);
    }

    /**
     * Smaze vsechny znaky aktualniho lexemu
     *
     * @throws AutomatException
     */
    protected void clearBuffer() throws AutomatException {
        while (sb.length() > 0) {
            deleteChar();
        }
    }

    /**
     * Vraci symbol
     *
     * @return
     * @throws AutomatException
     */
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
            InputStream data = new FileInputStream("src//vtest2.txt");
            LexicalAutomata la = PJPLexicalAutomata.getPJPAutomata(in);
            la.setSource(data);

            while (la.hasTokens()) {
                System.out.println(la.getToken());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Trida rozsiruje bezny konecny automat o specifikaci chyboveho stavu a dokaze rozlisovat, jakeho typu je konecny stav (jaky symbol ma vracet)
     */
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

                // Je u¾ stav vytvoøen?
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