package cz.vsb.pjp.project.recursivegrammar;

import cz.vsb.uti.sch110.automata.AutomatException;
import cz.vsb.pjp.project.LexicalAutomata;
import cz.vsb.pjp.project.Symbol;
import cz.vsb.pjp.project.NoMoreTokensException;

import java.io.*;
import java.util.*;

/**
 * @author Vladimir Schafer - 15.5.2005 - 10:40:34
 */
public class Grammar {
    LexicalAutomata l;
    Symbol s;
    HashMap<String, Value> symbolTable = new HashMap<String, Value>();
    private boolean errorOccured = false;

    public Grammar(LexicalAutomata l) throws AutomatException, IOException, NoMoreTokensException {
        this.l = l;
        s = l.getToken();
        symbolTable.put("write", new Function() {
            public Object ExecuteFunction(AbstractList<Value> values) {
                int size = values.size();
                for (int i = 0; i < size; i++) {
                    if (!errorOccured) System.out.println(values.get(i).toString());
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
                        if (!errorOccured) values.get(i).setValue(br.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        });
    }

    public void expect(String sym, HashSet<String> context) throws AutomatException, IOException {
        try {
            if (s.getName().equals(sym) || s.getAtt().equals(sym))
                s = l.getToken();
            else {
                System.err.println("Syntax Error at line: " + l.getLine() + ", position: " + l.getPosition() + ", expected " + sym);
                errorOccured = true;
                // Synchronizace
                while (!context.contains((s = l.getToken()).getName())) ;
            }
        } catch (NoMoreTokensException e) {
            e.printStackTrace();
        }
    }

    public void synchro(HashSet<String> context) throws IOException, GrammarException, AutomatException {
        System.err.println("Syntax Error at line: " + l.getLine() + ", position: " + l.getPosition());
        errorOccured = true;
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
            expect("ident", context);
            HashSet<String> next = (HashSet<String>) context.clone();
            next.add("semicolon");
            B(name, next);
        } else if (s.getName().equals("var")) {
            expect("var", context);
            HashSet<String> next = (HashSet<String>) context.clone();
            next.add("semicolon");
            Declaration(next);
        }

        context.add("semicolon");
        expect(";", context);
        Statement(context);
    }

    public String Declaration(HashSet<String> context) throws AutomatException, GrammarException, IOException {
        Symbol tmp = s;
        expect("ident", context);
        HashSet<String> next = (HashSet<String>) context.clone();
        String type = D(next);

        if (symbolTable.containsKey(tmp.getAtt().toLowerCase())) {
            throw new GrammarException("Variable is already defined");
        } else {
            symbolTable.put(tmp.getAtt().toLowerCase(), Value.getDefaultValue(type));
        }
        //System.out.println(tmp.getAtt() + " - " + type);
        return type;
    }

    public String D(HashSet<String> context) throws AutomatException, GrammarException, IOException {
        if (!(s.getName().equals("comma") || s.getName().equals("colon"))) synchro(context);

        if (s.getAtt().equals(",")) {
            expect(",", context);
            HashSet<String> next = (HashSet<String>) context.clone();
            return Declaration(next);
        } else if (s.getName().equals("colon")) {
            expect(":", context);
            String type = "fake";
            Symbol tmp = s;
            expect("type", context);
            if (tmp.getName().equals("type")) {
                type = tmp.getAtt();
            }
            return type;
        } else {
            throw new GrammarException("Error in D");
        }
    }

    public void B(String name, HashSet<String> context) throws AutomatException, IOException, GrammarException {
        if (!(s.getName().equals("leftp") || s.getName().equals("assign"))) synchro(context);
        Symbol tmp = s;
        if (tmp.getAtt().equals(":=")) {
            expect(":=", context);
            HashSet<String> next = (HashSet<String>) context.clone();
            Assign(name, next);
        } else if (tmp.getAtt().equals("(")) {
            expect("(", context);
            Value v = symbolTable.get(name.toLowerCase());
            if (v != null && v.getType().equals("function")) {
                Function f = (Function) v;
                HashSet<String> next = (HashSet<String>) context.clone();
                next.add("rightp");
                Func(f, next);
            }
            expect(")", context);
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
            expect(",", context);
            HashSet<String> next = (HashSet<String>) context.clone();
            ll.add(Expr(next));
            Func2(ll, context);
        }

        return ll;
    }

    public void Assign(String prom, HashSet<String> context) throws AutomatException, IOException, GrammarException {
        HashSet<String> next = (HashSet<String>) context.clone();
        next.add("comma");
        Value value = Expr(next);
        Value old;

        prom = prom.toLowerCase();
        old = symbolTable.get(prom);
        if (old != null) {
            old.setValue(value);
            //System.out.println(prom + " - " + value);
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
        if (s.getName().equals("arithmeticalb") || s.getName().equals("concat") || s.getAtt().equals("or") || s.getName().equals("relation")) {
            next = (HashSet<String>) context.clone();
            data = E1(data, next);
        }
        return data;
    }

    public Value E1(Value in, HashSet<String> context) throws AutomatException, GrammarException, IOException {
        if (s.getName().equals("arithmeticalb") || s.getName().equals("concat") || s.getAtt().equals("or") || s.getName().equals("relation")) {
            HashSet<String> next = (HashSet<String>) context.clone();
            in = G(in, next);
            next = (HashSet<String>) context.clone();
            in = E2(in, next);
        }

        return in;
    }

    public Value E2(Value in, HashSet<String> context) throws AutomatException, GrammarException, IOException {
        Symbol tmp = s;
        if (s.getName().equals("relation")) {
            expect("relation", context);
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
            expect("arithmeticalb", context);
            HashSet<String> next = (HashSet<String>) context.clone();
            Value value = E(next);
            in = in.performOperation(oper, value);
            next = (HashSet<String>) context.clone();
            in = G(in, next);
        } else if (s.getName().equals("concat")) {
            expect(".", context);
            HashSet<String> next = (HashSet<String>) context.clone();
            Value value = E(next);
            in = in.performOperation(oper, value);
            next = (HashSet<String>) context.clone();
            in = G(in, next);
        } else if (s.getAtt().equals("or")) {
            expect("or", context);
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
        next.add("and");
        Value value = H(next);
        next = (HashSet<String>) context.clone();
        value = T1(value, next);
        return value;
    }

    public Value T1(Value in, HashSet<String> context) throws AutomatException, GrammarException, IOException {
        Symbol tmp = s;
        if (tmp.getName().equals("arithmeticala")) {
            expect("arithmeticala", context);
            HashSet<String> next = (HashSet<String>) context.clone();
            Value valueB = H(next);
            in = in.performOperation(tmp.getAtt(), valueB);
            next = (HashSet<String>) context.clone();
            in = T1(in, next);
        } else if (tmp.getAtt().equals("and")) {
            expect("and", context);
            HashSet<String> next = (HashSet<String>) context.clone();
            Value valueB = H(next);
            in = in.performOperation(tmp.getAtt(), valueB);
            next = (HashSet<String>) context.clone();
            in = T1(in, next);
        }
        return in;
    }

    public Value H1(HashSet<String> context) throws AutomatException, IOException, GrammarException {
        context.add("leftp");
        context.add("ident");
        context.add("true");
        context.add("false");
        context.add("numberint");
        context.add("numberdouble");
        if (!(s.getName().equals("leftp") || s.getName().equals("ident") || s.getName().equals("true") || s.getName().equals("false") || s.getName().equals("numberint") || s.getName().equals("numberdouble"))) synchro(context);
        context.remove("leftp");
        context.remove("ident");
        context.remove("true");
        context.remove("false");
        context.remove("numberint");
        context.remove("numberdouble");

        Symbol tmp = s;
        if (s.getName().equals("leftp")) {
            expect("(", context);
            HashSet<String> next = (HashSet<String>) context.clone();
            next.add("rightp");
            Value val = Expr(next);
            expect(")", context);
            return val;
        } else if (s.getName().equals("ident")) {
            expect("ident", context);
            String val = tmp.getAtt().toLowerCase();
            Value v = symbolTable.get(val);
            if (v != null)
                return v;
            else
                throw new GrammarException("Variable " + tmp.getAtt() + " doesn't exist");
        } else if (s.getName().equals("true")) {
            expect("true", context);
            return new BooleanValue(true);
        } else if (s.getName().equals("false")) {
            expect("false", context);
            return new BooleanValue(false);
        } else if (s.getName().equals("numberint")) {
            expect("numberint", context);
            return new IntegerValue(tmp.getAtt());
        } else if (s.getName().equals("numberdouble")) {
            expect("numberdouble", context);
            return new RealValue(tmp.getAtt());
        } else {
            return new FakeValue();
        }
    }

    public Value H(HashSet<String> context) throws AutomatException, IOException, GrammarException {
        Symbol tmp = s;
        if (tmp.getAtt().equals("-")) {
            expect("-", context);
            HashSet<String> next = (HashSet<String>) context.clone();
            return H(next).performUnaryOperation("-");
        } else if (tmp.getName().equals("not")) {
            expect("not", context);
            HashSet<String> next = (HashSet<String>) context.clone();
            return H1(next).performUnaryOperation("not");
        } else if (tmp.getName().equals("stringval")) {
            expect("stringval", context);
            return new StringValue(tmp.getAtt());
        } else {
            HashSet<String> next = (HashSet<String>) context.clone();
            return H1(next);
        }
    }
}