package cz.vsb.pjp.project.recursivegrammar;

import cz.vsb.uti.sch110.automata.AutomatException;
import cz.vsb.pjp.project.LexicalAutomata;
import cz.vsb.pjp.project.Symbol;
import cz.vsb.pjp.project.NoMoreTokensException;
import cz.vsb.pjp.project.PJPLexicalAutomata;

import java.io.*;
import java.util.*;

/**
 * @author Vladimir Schafer - 15.5.2005 - 10:40:34
 */
public class Grammar {
    LexicalAutomata l;
    Symbol s;
    HashMap<String, Value> symbolTable = new HashMap<String, Value>();

    public Grammar(LexicalAutomata l) throws AutomatException, IOException, NoMoreTokensException {
        this.l = l;
        s = l.getToken();
        symbolTable.put("write", new Function() {
            public Object ExecuteFunction(AbstractList<Value> values) {
                int size = values.size();
                for (int i = 0; i < size; i++) {
                    System.out.println(values.get(i).toString());
                }
                return null;
            }
        });

        symbolTable.put("read", new Function() {
            public Object ExecuteFunction(AbstractList<Value> values) {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                int size = values.size();
                for (int i = 0; i < size; i++) {
                    try {
                        values.get(i).setValue(br.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        });
    }

    public void expect(String sym) throws AutomatException, IOException {
        if (s.getName().equals(sym) || s.getAtt().equals(sym))
            try {
                s = l.getToken();
            } catch (NoMoreTokensException e) {
                e.printStackTrace();
            }
        else {
            System.err.println("Syntax Error at line: " + l.getLine() + ", position: " + l.getPosition() + ", expected " + sym);
        }
    }

    public void synchro(HashSet<String> context) throws IOException, GrammarException, AutomatException {
        System.err.println("Syntax Error at line: " + l.getLine() + ", position: " + l.getPosition());
        try {
            while (!context.contains(s.getName())) {
                s = l.getToken();
            }
        } catch (NoMoreTokensException e) {
            e.printStackTrace();
        }
    }

    public void Statement(HashSet<String> context) throws AutomatException, IOException, GrammarException {
        if (s.getName().equals(LexicalAutomata.EOF)) return;

        if (!(s.getName().equals("ident") || s.getName().equals("var") || s.getName().equals("semicolon"))) synchro(context);

        if (s.getName().equals("ident")) {
            String name = s.getAtt();
            expect("ident");
            HashSet<String> next = (HashSet<String>) context.clone();
            next.add("semicolon");
            B(name, next);
        } else if (s.getName().equals("var")) {
            expect("var");
            HashSet<String> next = (HashSet<String>) context.clone();
            next.add("semicolon");
            Declaration(next);
        } else if (s.getName().equals("semicolon")) {
            expect(";");
        }

        // Osetreni konce vyhodnocovani
        expect(";");
        Statement(context);
    }

    public String Declaration(HashSet<String> context) throws AutomatException, GrammarException, IOException {
        Symbol tmp = s;
        expect("ident");
        HashSet<String> next = (HashSet<String>) context.clone();
        String type = D(next);

        if (symbolTable.containsKey(tmp.getAtt().toLowerCase())) {
            throw new GrammarException("Variable is already defined");
        } else {
            symbolTable.put(tmp.getAtt().toLowerCase(), Value.getDefaultValue(type));
        }
        System.out.println(tmp.getAtt() + " - " + type);
        return type;
    }

    public String D(HashSet<String> context) throws AutomatException, GrammarException, IOException {
        if (!(s.getName().equals("comma") || s.getName().equals("colon"))) synchro(context);

        if (s.getAtt().equals(",")) {
            expect(",");
            HashSet<String> next = (HashSet<String>) context.clone();
            return Declaration(next);
        } else if (s.getName().equals("colon")) {
            expect(":");
            String type;
            if (s.getName().equals("type")) {
                type = s.getAtt();
                expect("type");
            } else {
                throw new GrammarException("Expected other token in D");
            }
            return type;
        } else {
            throw new GrammarException("Error in D");
        }
    }

    public void B(String name, HashSet<String> context) throws AutomatException, IOException, GrammarException {
        if (!(s.getName().equals("leftp") || s.getName().equals("colon"))) synchro(context);
        Symbol tmp = s;
        if (tmp.getAtt().equals(":")) {
            expect(":");
            HashSet<String> next = (HashSet<String>) context.clone();
            Assign(name, next);
        } else if (tmp.getAtt().equals("(")) {
            expect("(");
            Value v = symbolTable.get(name);
            if (v != null && v.getType().equals("function")) {
                Function f = (Function) v;
                HashSet<String> next = (HashSet<String>) context.clone();
                next.add("rightp");
                Func(f, next);
            }
            expect(")");
            return;
        }
    }

    public void Func(Function f, HashSet<String> context) throws AutomatException, IOException, GrammarException {
        LinkedList<Value> ll = new LinkedList<Value>();
        HashSet<String> next = (HashSet<String>) context.clone();
        next.add("comma");
        ll.add(Expr(next));
        Func2(ll, context);
        f.ExecuteFunction(ll);
    }

    public AbstractList<Value> Func2(AbstractList<Value> ll, HashSet<String> context) throws AutomatException, IOException, GrammarException {
        if (s.getAtt().equals(",")) {
            expect(",");
            HashSet<String> next = (HashSet<String>) context.clone();
            ll.add(Expr(next));
            Func2(ll, context);
        }

        return ll;
    }

    public void Assign(String prom, HashSet<String> context) throws AutomatException, IOException, GrammarException {
        expect("=");
        HashSet<String> next = (HashSet<String>) context.clone();
        next.add("comma");
        Value value = Expr(next);
        Value old;

        prom = prom.toLowerCase();
        old = symbolTable.get(prom);
        if (old != null) {
            old.setValue(value);
            System.out.println(prom + " - " + value);
        } else
            throw new GrammarException("Variable " + prom + " is not defined");
    }

    public Value Expr(HashSet<String> context) throws AutomatException, IOException, GrammarException {
        HashSet<String> next = (HashSet<String>) context.clone();
        next.add("arithmeticalb");
        next.add("concat");
        next.add("or");
        next.add("relation");
        Value data = E(next);
        if (s.getName().equals("arithmeticalb") || s.getName().equals("concat") || s.getName().equals("or") || s.getName().equals("relation")) {
            next = (HashSet<String>) context.clone();
            data = E1(data, next);
        }
        return data;
    }

    public Value E1(Value in, HashSet<String> context) throws AutomatException, GrammarException, IOException {
        if (s.getName().equals("arithmeticalb") || s.getName().equals("concat") || s.getName().equals("or")) {
            HashSet<String> next = (HashSet<String>) context.clone();
            in = G(in, next);
        } else if (s.getName().equals("relation")) {
            HashSet<String> next = (HashSet<String>) context.clone();
            in = E2(in, next);
        }

        return in;
    }

    public Value E2(Value in, HashSet<String> context) throws AutomatException, GrammarException, IOException {
        Symbol tmp = s;
        if (s.getName().equals("relation")) {
            expect("relation");
            HashSet<String> next = (HashSet<String>) context.clone();
            Value value = E(next);
            //value = E1(value);
            next = (HashSet<String>) context.clone();
            value = G(value, next);
            in = in.performOperation(tmp.getAtt(), value);
            next = (HashSet<String>) context.clone();
            in = E2(in, next);
        }

        return in;
    }

    public Value G(Value in, HashSet<String> context) throws AutomatException, GrammarException, IOException {
        String oper = s.getAtt();
        if (s.getName().equals("arithmeticalb")) {
            expect("arithmeticalb");
            HashSet<String> next = (HashSet<String>) context.clone();
            Value value = E(next);
            in = in.performOperation(oper, value);
            next = (HashSet<String>) context.clone();
            in = G(in, next);
        } else if (s.getName().equals("concat")) {
            expect(".");
            HashSet<String> next = (HashSet<String>) context.clone();
            Value value = E(next);
            in = in.performOperation(oper, value);
            next = (HashSet<String>) context.clone();
            in = G(in, next);
        } else if (s.getName().equals("or")) {
            expect("or");
            HashSet<String> next = (HashSet<String>) context.clone();
            Value value = E(next);
            in = in.performOperation(oper, value);
            next = (HashSet<String>) context.clone();
            in = G(in, next);
        }

        return in;
    }

    public Value E(HashSet<String> context) throws AutomatException, GrammarException, IOException {
        HashSet<String> next = (HashSet<String>) context.clone();
        next.add("arithmeticala");
        Value value = H(next);
        next = (HashSet<String>) context.clone();
        value = T1(value, next);
        return value;
    }

    public Value T1(Value in, HashSet<String> context) throws AutomatException, GrammarException, IOException {
        Symbol tmp = s;
        if (tmp.getName().equals("arithmeticala")) {
            expect("arithmeticala");
            HashSet<String> next = (HashSet<String>) context.clone();
            Value valueB = H(next);
            in = in.performOperation(tmp.getAtt(), valueB);
            next = (HashSet<String>) context.clone();
            in = T1(in, next);
        }
        return in;
    }

    public Value H1(HashSet<String> context) throws AutomatException, IOException, GrammarException {
        if (!(s.getName().equals("leftp") || s.getName().equals("ident") || s.getName().equals("true") || s.getName().equals("false") || s.getName().equals("numberint") || s.getName().equals("numberdouble"))) synchro(context);
        Symbol tmp = s;
        if (s.getName().equals("leftp")) {
            expect("(");
            HashSet<String> next = (HashSet<String>) context.clone();
            next.add("rightp");
            Value val = Expr(next);
            expect(")");
            return val;
        } else if (s.getName().equals("ident")) {
            expect("ident");
            String val = tmp.getAtt().toLowerCase();
            Value v = symbolTable.get(val);
            if (v != null)
                return v;
            else
                throw new GrammarException("Variable " + tmp.getAtt() + " doesn't exist");
        } else if (s.getName().equals("true")) {
            expect("true");
            return new BooleanValue(true);
        } else if (s.getName().equals("false")) {
            expect("false");
            return new BooleanValue(false);
        } else if (s.getName().equals("numberint")) {
            expect("numberint");
            return new IntegerValue(tmp.getAtt());
        } else if (s.getName().equals("numberdouble")) {
            expect("numberdouble");
            return new RealValue(tmp.getAtt());
        } else {
            return new FakeValue();
        }
    }

    public Value H(HashSet<String> context) throws AutomatException, IOException, GrammarException {
        Symbol tmp = s;
        if (tmp.getAtt().equals("-")) {
            expect("-");
            HashSet<String> next = (HashSet<String>) context.clone();
            return H1(next).performUnaryOperation("-");
        } else if (tmp.getName().equals("not")) {
            expect("not");
            HashSet<String> next = (HashSet<String>) context.clone();
            return H1(next).performUnaryOperation("not");
        } else if (tmp.getName().equals("stringval")) {
            expect("stringval");
            return new StringValue(tmp.getAtt());
        } else {
            HashSet<String> next = (HashSet<String>) context.clone();
            return H1(next);
        }
    }

    public static void main(String[] args) {
        try {

            InputStream in = new FileInputStream("src//rules2.lex");
            InputStream data = new FileInputStream("src//vtest2.txt");
            LexicalAutomata la = PJPLexicalAutomata.getPJPAutomata(in, true);
            la.setSource(data);

            Grammar g = new Grammar(la);
            HashSet<String> follow = new HashSet<String>();
            follow.add(LexicalAutomata.EOF);
            g.Statement(follow);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
