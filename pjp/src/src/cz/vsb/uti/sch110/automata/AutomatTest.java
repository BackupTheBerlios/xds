package cz.vsb.uti.sch110.automata;

import cz.vsb.uti.sch110.automata.AutomatException;

import java.util.StringTokenizer;
import java.util.Vector;
import java.io.*;

/**
 * Testovací tøída automatu, obsahuje jednoduchý parser
 */
public class AutomatTest {
    public static void main(String[] args) {

        try {
            BufferedReader in;
            if (args.length < 1) {
                System.err.println("Using stdin as source of data");
                in = new BufferedReader(new InputStreamReader(System.in));
            } else {
                System.err.println("Using file " + args[0] + " as source of data");
                in = new BufferedReader(new FileReader(args[0]));
            }


            String temp;
            ZNKAutomat znk;

            Vector states = new Vector();
            Vector start = new Vector();
            Vector end = new Vector();
            Vector transition = new Vector();
            Vector active = states;
            Vector alphabet = new Vector();

            while ((temp = in.readLine()) != null) {
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
                        case 'e':
                            {
                                active = alphabet;
                                break;
                            }
                        case 'i':
                            {
                                active = start;
                                break;
                            }
                        case 'f':
                            {
                                active = end;
                                break;
                            }
                        case 'd':
                            {
                                active = transition;
                                break;
                            }
                    }

                    while (st.hasMoreTokens()) {
                        active.add(st.nextToken());
                    }
                }
            }

            StringBuffer sb = new StringBuffer();
            if (alphabet.size() == 0) {
                System.err.println("Alphabet cant't be empty");
                System.exit(1);
            }
            for (int i = 0; i < alphabet.size(); i++) {
                sb.append(alphabet.get(i));
            }
            znk = new ZNKAutomat(sb.toString().toCharArray());

            if (states.size() == 0) {
                //    System.err.println("Please specify at least one state");
                System.exit(1);
            }
            for (int i = 0; i < states.size(); i++) {
                znk.addNode(new Node((String) states.get(i)));
            }

            if (start.size() == 0) {
                System.err.println("Please specify at least one start state");
                System.exit(1);
            }
            for (int i = 0; i < start.size(); i++) {
                znk.setStarting(znk.getNode((String) start.get(i)));
            }
            for (int i = 0; i < end.size(); i++) {
                znk.setAccepting(znk.getNode((String) end.get(i)));
            }
            for (int i = 0; i < transition.size(); i += 3) {
                Node source = znk.getNode((String) transition.get(i));
                Node destination = znk.getNode((String) transition.get(i + 2));
                String prechod = (String) transition.get(i + 1);
                if (prechod.toLowerCase().equals("<e>")) {
                    source.addTransition(new ETransition(destination));
                } else {
                    source.addTransition(new Transition(prechod.charAt(0), destination));
                }
            }

            System.out.println(znk.convertToKA());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AutomatException e) {
            e.printStackTrace();
        }
    }

    private static void generate(KAutomat kat, ZNKAutomat zat, StringBuffer sb, char[] charset) throws AutomatException {
        if (sb.length() >= 10) return;
        for (int i = 0; i < charset.length; i++) {
            sb.append(charset[i]);
            String data = sb.toString();
            if (kat.checkWord(data) != zat.checkWord(data)) {
                System.out.println("Kua:" + data);
                System.out.println("ZNKA: " + zat.checkWord(data));
                System.out.println("KA: " + kat.checkWord(data));
            }
            generate(kat, zat, sb, charset);
            sb.deleteCharAt(sb.length() - 1);
        }
    }
}