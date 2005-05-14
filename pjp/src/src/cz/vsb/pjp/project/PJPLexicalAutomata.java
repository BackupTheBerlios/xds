package cz.vsb.pjp.project;

import cz.vsb.uti.sch110.automata.*;

import java.io.*;
import java.util.*;

/**
 * Trida umoznuje dynamicky vytvorit automat pro lexikalni analyzu ze zadaneho vstupniho proudu.
 *
 * @author Vladimir Schafer - 23.4.2005 - 12:49:45
 */
public class PJPLexicalAutomata extends LexicalAutomata {
    private StringBuffer buffer = new StringBuffer();
    private HashMap<String, Symbol> keywords = new HashMap<String, Symbol>();

    private PJPLexicalAutomata(char[] charset, KAutomat[] data) throws AutomatException {
        super(charset, data);
        keywords.put("var", new Symbol("var", "var"));
        keywords.put("and", new Symbol("logic", "and"));
        keywords.put("or", new Symbol("logic", "or"));
        keywords.put("not", new Symbol("not", "not"));
        keywords.put("true", new Symbol("true", "true"));
        keywords.put("false", new Symbol("false", "false"));
        keywords.put("integer", new Symbol("integer", "integer"));
        keywords.put("string", new Symbol("string", "string"));
        keywords.put("real", new Symbol("real", "real"));
        keywords.put("boolean", new Symbol("boolean", "boolean"));
        keywords.put("mod", new Symbol("arithmetical", "mod"));
    }

    // TODO Mit moznost cist cisla radku
    // TODO Vracet Terminal misto Symbolu
    public Symbol getToken() throws IOException, AutomatException, NoMoreTokensException {
        Symbol s = super.getToken();
        while (s.getName().equals("komentar")) s = super.getToken();
        if (s.getName().equals("ident")) {
            if (keywords.containsKey(s.getAtt())) {
                return keywords.get(s.getAtt());
            }
        }
        return s;
    }

    protected void addChar(char a) throws AutomatException {
        /**
         * Osetreni bilych znaku v retezcich - neni mozne je ignorovat jako bile znaky mimo retezce
         */
        buffer.append(a);
        // Osetreni hromadnych znaku
        if (Character.isWhitespace(a)) a = '\'';
        //if (Character.isLetter(a)) a = 'a';
        if (Character.isDigit(a)) a = '0';
        super.addChar(a);
    }

    protected void deleteChar() throws AutomatException {
        super.deleteChar();
        buffer.deleteCharAt(buffer.length() - 1);

    }

    protected Symbol getPushBackSymbol() throws AutomatException {
        buffer.deleteCharAt(buffer.length() - 1);
        Symbol s = super.getPushBackSymbol();
        return s;
    }

    protected Symbol getSymbol() throws AutomatException {
        Symbol s = super.getSymbol();
        if (s != null) {
            s.setAtt(buffer.toString());
        }
        buffer.delete(0, buffer.length());
        return s;
    }

    public static LexicalAutomata getPJPAutomata(InputStream input) {
        try {
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(input));

            String temp;
            KAutomat znk;

            int num = Integer.parseInt(in.readLine());
            int base = 0;
            String alphabet = in.readLine();
            KAutomat[] data = new KAutomat[num];

            String name = null;
            String folName = "";
            start: for (int pocet = 0; pocet < num; pocet++) {

                name = folName;
                Vector states = new Vector();
                Vector transition = new Vector();
                Vector active = states;
                Map m = new HashMap();
                token: while ((temp = in.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(temp);
                    while (st.hasMoreTokens()) {
                        temp = st.nextToken();
                        char drive = temp.toLowerCase().charAt(0);
                        if (drive == '#') break;
                        switch (drive) {
                            case 'q':
                                {
                                    active = states;
                                    break;
                                }
                            case 'd':
                                {
                                    active = transition;
                                    break;
                                }
                            case 'n':
                                {
                                    if (!name.equals("")) {
                                        folName = st.nextToken();
                                        break token;
                                    } else {
                                        name = st.nextToken();
                                        continue token;
                                    }
                                }
                        }

                        while (st.hasMoreTokens()) {
                            active.add(st.nextToken());
                        }
                    }
                }

                znk = new KAutomat(alphabet.toCharArray());
                data[pocet] = znk;

                if (states.size() == 0) {
                    System.err.println("Please specify at least one state");
                    System.exit(1);
                }

                int max = base + states.size();
                for (int i = base; i < max; i++) {
                    String nazev = states.get(i - base).toString();
                    String nextName = Integer.toString(i);
                    Node n;
                    if (nazev.endsWith("F")) {
                        n = new SymbolNode(nextName, new Symbol(name));
                        znk.addNode(n);
                    } else if (nazev.endsWith("I")) {
                        n = new Node(nextName);
                        znk.addNode(n);
                        znk.setStarting(n);
                    } else {
                        n = new Node(nextName);
                        znk.addNode(n);
                    }
                    m.put(nazev, nextName);
                }
                base = max;

                for (int i = 0; i < transition.size(); i += 3) {
                    Node source = znk.getNode(m.get((String) transition.get(i)).toString());
                    Node destination = znk.getNode(m.get((String) transition.get(i + 2)).toString());
                    String prechod = (String) transition.get(i + 1);
                    source.addTransition(new Transition(prechod.charAt(0), destination));
                }
            }

            return new PJPLexicalAutomata(alphabet.toCharArray(), data);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (AutomatException e) {
            e.printStackTrace();
        }

        return null;
    }
}
