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
    //TODO Funkce
    //TODO Zotaveni
    LexicalAutomata l;
    Symbol s;
    HashMap<String, Value> symbolTable = new HashMap<String, Value>();

    public Grammar(LexicalAutomata l) {
        this.l = l;
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

    public void expect() throws AutomatException, IOException, NoMoreTokensException {
        s = l.getToken();
    }


    public void Statement() throws AutomatException, IOException, GrammarException, NoMoreTokensException {
        if (l.hasTokens())
            s = l.getToken();
        else
            return;

        Symbol tmp = s;
        expect(); //ident, semicol, ...
        if (tmp.getName().equals("ident")) {
            String name = tmp.getAtt();
            B(name);
            if (!tmp.getName().equals("semicolon")) ; //error;
            Statement();
        } else if (tmp.getName().equals("var")) {
            Declaration();
            if (!tmp.getName().equals("semicolon")) ; //error;
            Statement();
        } else if (tmp.getName().equals("semicolon")) {
            Statement();
        }
    }

    /*public void Declaration() throws AutomatException, GrammarException, IOException, NoMoreTokensException {
        expect(); //var
        C();
    }*/

    public String Declaration() throws AutomatException, GrammarException, IOException, NoMoreTokensException {
        Symbol tmp = s;
        expect(); //ident
        String type = D();

        if (symbolTable.containsKey(tmp.getAtt().toLowerCase())) {
            throw new GrammarException("Variable is already defined");
        } else {
            symbolTable.put(tmp.getAtt().toLowerCase(), Value.getDefaultValue(type));
        }
        System.out.println(tmp.getAtt() + " - " + type);
        return type;
    }

    public String D() throws AutomatException, GrammarException, IOException, NoMoreTokensException {
        if (s.getAtt().equals(",")) {
            expect();
            return Declaration();
        } else if (s.getName().equals("colon")) {
            expect();
            String type;
            if (s.getName().equals("type")) {
                type = s.getAtt();
                expect();
            } else {
                throw new NoMoreTokensException();
            }
            return type;
        }

        throw new NoMoreTokensException();
    }

    public void B(String name) throws AutomatException, IOException, GrammarException, NoMoreTokensException {
        Symbol tmp = s;
        expect(); //:(
        if (tmp.getAtt().equals(":")) {
            Assign(name);
        } else if (tmp.getAtt().equals("(")) {
            Value v = symbolTable.get(name);
            if (v.getType().equals("function")) {
                Function f = (Function) v;
                Func(f);
            } // TODO else error
            expect(); //)
            return;
        }
    }

    public void Func(Function f) throws AutomatException, IOException, GrammarException, NoMoreTokensException {
        LinkedList<Value> ll = new LinkedList<Value>();
        Symbol tmp = s;
        ll.add(Expr());
        Func2(ll);
        f.ExecuteFunction(ll);
    }

    public AbstractList<Value> Func2(AbstractList<Value> ll) throws AutomatException, IOException, GrammarException, NoMoreTokensException {
        if (s.getAtt().equals(",")) {
            expect(); //,
            ll.add(Expr());
        }

        return ll;
    }

    public void Assign(String prom) throws AutomatException, IOException, GrammarException, NoMoreTokensException {
        expect();  //=
        //Assign
        Value value = Expr();
        Value old;
        Variable v;

        prom = prom.toLowerCase();
        old = symbolTable.get(prom);
        if (old != null) {
            old.setValue(value);
            System.out.println(prom + " - " + value);
        } else
            throw new GrammarException("Variable " + prom + " is not defined");
    }

    public Value Expr() throws AutomatException, IOException, GrammarException, NoMoreTokensException {
        Value data = E();
        if (s.getName().equals("arithmeticalb") || s.getName().equals("concat") || s.getName().equals("or") || s.getName().equals("relation")) {
            data = E1(data);
        }
        return data;
    }

    public Value E1(Value in) throws AutomatException, GrammarException, IOException, NoMoreTokensException {
        if (s.getName().equals("arithmeticalb") || s.getName().equals("concat") || s.getName().equals("or")) {
            in = G(in);
        }
        if (s.getName().equals("relation")) {
            in = E2(in);
        }

        return in;
    }

    public Value E2(Value in) throws AutomatException, GrammarException, IOException, NoMoreTokensException {
        Symbol tmp = s;
        if (s.getName().equals("relation")) {
            expect();
            Value value = E();
            //value = E1(value);
            value = G(value);
            in = in.performOperation(tmp.getAtt(), value);
            in = E2(in);
        }

        return in;
    }

    public Value G(Value in) throws AutomatException, GrammarException, IOException, NoMoreTokensException {
        if (s.getName().equals("arithmeticalb") || s.getName().equals("concat") || s.getName().equals("or")) {
            String oper = s.getAtt();
            expect();
            Value value = E();
            // vyhodnotit
            in = in.performOperation(oper, value);
            in = G(in);
        }

        return in;
    }

    public Value E() throws AutomatException, GrammarException, IOException, NoMoreTokensException {
        Value value = H();
        value = T1(value);
        return value;
    }

    public Value T1(Value in) throws AutomatException, GrammarException, IOException, NoMoreTokensException {
        Symbol tmp = s;
        if (tmp.getName().equals("arithmeticala")) {
            expect(); //arithmeticala
            Value valueB = H();
            //Vynasobit in s valueB
            in = in.performOperation(tmp.getAtt(), valueB);
            in = T1(in);
        }
        return in;
    }

    public Value H1() throws AutomatException, IOException, GrammarException, NoMoreTokensException {
        Symbol tmp = s;
        expect(); //leftp, number, ...
        if (tmp.getName().equals("leftp")) {
            Value val = Expr();
            if (s.getName().equals("rightp")) expect();
            return val;
        } else if (tmp.getName().equals("ident")) {
            String val = tmp.getAtt().toLowerCase();
            Value v = symbolTable.get(val);
            if (v != null)
                return v;
            else
                throw new GrammarException("Variable " + tmp.getAtt() + " doesn't exist");
        } else if (tmp.getName().equals("true")) {
            return new BooleanValue(true);
        } else if (tmp.getName().equals("false")) {
            return new BooleanValue(false);
        } else if (tmp.getName().equals("numberint")) {
            return new IntegerValue(tmp.getAtt());
        } else if (tmp.getName().equals("numberdouble")) {
            return new RealValue(tmp.getAtt());
        } else {
            throw new NoMoreTokensException();
        }
    }

    public Value H() throws AutomatException, IOException, GrammarException, NoMoreTokensException {
        Symbol tmp = s;
        if (tmp.getAtt().equals("-")) {
            expect(); //-
            return H1().performUnaryOperation("-");
        } else if (tmp.getName().equals("not")) {
            expect(); //not
            return H1().performUnaryOperation("not");
        } else if (tmp.getName().equals("stringval")) {
            expect(); //stringval
            return new StringValue(tmp.getAtt());
        } else {
            return H1();
        }
    }

    public static void main(String[] args) {
        try {

            InputStream in = new FileInputStream("src//rules2.lex");
            InputStream data = new FileInputStream("src//vtest.txt");
            LexicalAutomata la = PJPLexicalAutomata.getPJPAutomata(in);
            la.setSource(data);

            Grammar g = new Grammar(la);
            g.Statement();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
